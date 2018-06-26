package com.gateway.payment.service.base.impl;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.transaction.annotation.Transactional;

import com.gateway.common.constants.param.PaymentNotifyParamConstant;
import com.gateway.common.constants.status.BgReturnConstant;
import com.gateway.common.constants.status.OrderStatusConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.common.utils.StringUtils;
import com.gateway.payment.entity.BgreturnEntity;
import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.OrderEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.gateway.payment.entity.TradeMonitorEntity;
import com.gateway.payment.service.IBaseService;
import com.gateway.payment.service.base.IBasePaymentService;
import com.gateway.payment.service.transfer.IPaymentTransfer1To2Service;
import com.zitopay.datagram.request.RequestDatagram;
import com.zitopay.foundation.common.exception.ServiceException;
import com.zitopay.foundation.common.util.BeanUtils;

public abstract class BasePaymentServiceImpl extends BaseServiceImpl implements IBasePaymentService {

	public static final String payment_verson2 = "2.0";

	@Autowired
	private IPaymentTransfer1To2Service iPaymentTransfer1To2Service;

	private static final Logger logger = LoggerFactory.getLogger(BasePaymentServiceImpl.class);

	public void payingPayment(OrderEntity order, PersonGatewayEntity personGeteway, String serialNo) {
		if(order == null) {
			return;
		}
		logger.info("订单状态修改为支付中,报文序列号:{},参数:{}", serialNo, order);
		
		order.setRate(personGeteway.getRate());// 商户通道表的费率
		order.setState(OrderStatusConstant.STATUS_UNPAYING);// //订单表
															// 订单状态（0初始状态,1成功，2未支付，3失败，4失效，5已退款,6部分退款）
		order.setMin(personGeteway.getMin() != null ? personGeteway.getMin().toString() : "0");
		order.setMax(personGeteway.getMax() != null ? personGeteway.getMax().toString() : "0");
		order.setFix(personGeteway.getFix() != null ? personGeteway.getFix().toString() : "0");
		order.setSettlementType(personGeteway.getClearform());
		orderService.updateByPrimaryKeySelective(order);// 根据主键修改订单表
		logger.info("订单状态修改为支付中,报文序列号:{},结果:{}", serialNo, order);
		putmq(order);
	}

	/**
	 * 支付/查询后操作 <br/>
	 * 1-根据订单id查询订单信息.<br/>
	 * 2-更新订单状态,支付流水号,订单支付时间.<br/>
	 * 3-记录回调(查询)数据.<br/>
	 * 4-记录通知商户信息并通知商户.<br/>
	 * 5-处理商户反馈.<br/>
	 * 
	 * @param merchantId
	 * @param threeorderid
	 * @param paysucctime
	 * @param payno
	 * @param channelReturnValue
	 * @param tradeStatus
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	@Override
	public Map<String, Object> afterPayment(String merchantId, String merchantPublicKey, String threeorderid, Date paysucctime, String payno, String toppayno, String channelReturnValue, String tradeStatus, String serialNo) throws ServiceException {
		logger.info("回调/查询支付后操作-开始,支付状态(成功):{}", tradeStatus);
		try {
			OrderEntity orderEntity = orderService.findByhreerderid(threeorderid);
			logger.info("支付回调处理-orderEntity{}", BeanUtils.bean2JSON(orderEntity));
			if (null == orderEntity) {
				String msg = "未找到对应订单,订单号[" + threeorderid + "]不存在.";
				logger.warn(msg);
				return ResponseInfoEnum.订单不存在.getMap(msg);
			}
			boolean flag = Boolean.valueOf(tradeStatus);// 是否支付、查询成功
			if (flag) {// 如果第三方返回支付状态为成功，则进行融智付订单状态修改、分润处理、异步通知商户以及更新回调信息，反之只对第三方返回信息进行记录
				boolean isputmq = true;
				if (OrderStatusConstant.STATUS_UNPAYING == orderEntity.getState() || OrderStatusConstant.STATUS_EXPIRED == orderEntity.getState()) {// 未支付和已失效订单修改为已支付
					orderEntity.setState(OrderStatusConstant.STATUS_PAYED);
				} else {
					isputmq = false;
				}
				orderEntity.setPaysucctime(paysucctime);// 交易结束时间
				if (!StringUtils.isNull(payno)) {
					orderEntity.setPayno(payno);// 支付流水号
				}
				if (!StringUtils.isNull(toppayno)) {
					orderEntity.setToppayno(toppayno);// 支付流水号
				}
				BigDecimal ratemmoney = iEmployeeProfitService.cacuPoundage(orderEntity.getTotalprice(), new BigDecimal(orderEntity.getMin()), new BigDecimal(orderEntity.getMax()), new BigDecimal(orderEntity.getFix()), orderEntity.getRate());// 手续费//此处说明手续费没用
				orderEntity.setRatemmoney(ratemmoney);
				orderEntity.setAmount(orderEntity.getTotalprice().subtract(ratemmoney));
				orderService.updateByPrimaryKeySelective(orderEntity); // 更新订单信息
				logger.info("支付回调处理-支付订单信息修改,修改后数据:{}", orderEntity);
				// dealWithProfit(orderEntity); // 分润处理
				String notifyParam = assemblyNotifyParam(merchantId, merchantPublicKey, serialNo, orderEntity);
				// 异步回调地址为terminal-client时,不发送异步通知
				if (!orderEntity.getBgreturl().equals("terminal-client")) {
					sendNotify(orderEntity, notifyParam);// 通知商户
				}

				if (isputmq) {
					putmq(orderEntity);
				}

			}
			saveCallbackTradeMonitor(orderEntity, channelReturnValue, tradeStatus);// 保存通道回调/查询数据
			logger.info("支付回调处理-记录通道回调数据成功.");
			return ResponseInfoEnum.调用成功.getMap();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",支付回调处理异常", e);
		}

	}

	@Override
	public void sendNotify(final OrderEntity orderEntity, final String notifyParam) {

		/* 通知商户 */

		taskExecutor.execute(new Runnable() {

			@Override
			public void run() {

				String receipt = "";
				try {
					boolean preCondition = preNotifyMerchant(orderEntity);
					if (preCondition) {
						HttpEntity<String> httpEntity = new HttpEntity<String>(URLEncoder.encode(notifyParam, "UTF-8"));
						receipt = restTemplate.postForObject(orderEntity.getBgreturl(), httpEntity, String.class);
					}
				} catch (Exception e) {
					logger.warn("通知商户异常,订单ID:{}", orderEntity.getOrderid());
					logger.error(e.getMessage(), e);
				}
				try {
					postNotifyMerchant(orderEntity, receipt, notifyParam);
					logger.info("支付回调/查询处理-通知商户成功.");
				} catch (Exception e) {
					logger.warn("记录通知商户信息异常,订单ID:{}", orderEntity.getOrderid());
					logger.error(e.getMessage(), e);
				}

			}
		});

	}

	private boolean preNotifyMerchant(OrderEntity orderEntity) throws ServiceException {
		BgreturnEntity bgreturnEntity = bgreturnService.queryById(orderEntity.getOrderid());
		if (bgreturnEntity == null) {
			bgreturnEntity = new BgreturnEntity();
			bgreturnEntity.setNum(0);
			bgreturnEntity.setIsfeedback(BgReturnConstant.UNFEEDBACK);// 未反馈
			bgreturnEntity.setId(orderEntity.getOrderid());// 订单id
			bgreturnEntity.setBgreturl(orderEntity.getBgreturl());// 异步通知url
			bgreturnEntity.setType(BgReturnConstant.BGRETURN_TYPE_PAYMENT);// 支付回调
			bgreturnService.save(bgreturnEntity);
			logger.info("支付回调处理-创建通知信息成功:{}", bgreturnEntity);
		}
		if (bgreturnEntity.getNum() <= BgReturnConstant.NOTIFY_LIMIT && BgReturnConstant.UNFEEDBACK == bgreturnEntity.getIsfeedback()) {
			return true;
		} else {
			logger.info("订单[{}],已经超过重复通知次数或已收到反馈，无需再进行重复通知!", orderEntity.getOrderid());
			return false;
		}

	}

	// 分润处理
	private void dealWithProfit(final OrderEntity orderEntity) {
		taskExecutor.execute(new Runnable() {

			@Override
			public void run() {
				try {
					iEmployeeProfitService.countAndSaveProfit(orderEntity.getGetewayid().toString(), orderEntity.getPersionid().toString(), orderEntity.getOrderid(), orderEntity.getTotalprice(), orderEntity.getRatemmoney());
				} catch (Exception e) {
					logger.warn("分润处理失败", orderEntity.getOrderid());
					logger.error(e.getMessage(), e);
				}
			}
		});
	}

	private void postNotifyMerchant(OrderEntity orderEntity, String receipt, String notifyParam) throws ServiceException {
		BgreturnEntity bgreturnEntity = bgreturnService.queryById(orderEntity.getOrderid());
		saveNotifyMerchantTradeMonitor(orderEntity, notifyParam);// 记录回调商户交易信息
		logger.info("支付回调处理-记录通知数据成功,上回返回信息:{}.", receipt);
		if (!StringUtils.isEmpty(receipt)) {// 成功收到回执
			if (receipt.contains(IBaseService.NOTIFY_RECEIPT_LOWERCASE) || receipt.contains(IBaseService.NOTIFY_RECEIPT_UPPERCASE)) {
				logger.info("成功收到商户反馈,反馈结果:[SUCCESS-{},success-{}]", receipt.contains(IBaseService.NOTIFY_RECEIPT_UPPERCASE), receipt.contains(IBaseService.NOTIFY_RECEIPT_LOWERCASE));
				bgreturnEntity.setIsfeedback(BgReturnConstant.FEEDBACKED);// 已反馈
				bgreturnEntity.setResulttime(new Date());// 反馈时间
			}
		}
		bgreturnEntity.setNum(bgreturnEntity.getNum() + 1);// 回调次数+1
		bgreturnService.updateSelective(bgreturnEntity);
		logger.info("支付回调处理-更新通知信息成功,通知信息:{}", bgreturnEntity);
	}

	/**
	 * 组装异步通知参数
	 * 
	 * @param merchantId
	 * @param merchantPublicKey
	 * @param serialNo
	 * @param orderEntity
	 * @return
	 * @throws ServiceException
	 */
	public String assemblyNotifyParam(String merchantId, String merchantPublicKey, String serialNo, OrderEntity orderEntity) throws ServiceException {
		Map<String, String> dataMap = new HashMap<String, String>();
		PersonEntity personEntity = personService.getZitopayPersonByPid(merchantId);
		if (personEntity != null) {
			if (payment_verson2.equals(personEntity.getCallVersion())) {
				dataMap.put(PaymentNotifyParamConstant.param_gatewayId, orderEntity.getGetewayid().toString());
				dataMap.put(PaymentNotifyParamConstant.param_merchantId, merchantId);
				dataMap.put(PaymentNotifyParamConstant.param_appId, orderEntity.getAppId());
				dataMap.put(PaymentNotifyParamConstant.param_orderTime, orderEntity.getPosttime());
				dataMap.put(PaymentNotifyParamConstant.param_payTime, DateFormatUtils.format(orderEntity.getPaysucctime(), "yyyyMMddHHmmss"));
				dataMap.put(PaymentNotifyParamConstant.param_orderAmount, orderEntity.getTotalprice() == null ? null : orderEntity.getTotalprice().toString());
				dataMap.put(PaymentNotifyParamConstant.param_payAmount, orderEntity.getAmount() == null ? null : orderEntity.getAmount().toString());
				dataMap.put(PaymentNotifyParamConstant.param_payFee, orderEntity.getRatemmoney() == null ? null : orderEntity.getRatemmoney().toString());
				dataMap.put(PaymentNotifyParamConstant.param_merchantOrderId, orderEntity.getOrderidinf());
				dataMap.put(PaymentNotifyParamConstant.param_orderid, orderEntity.getOrderid());
				dataMap.put(PaymentNotifyParamConstant.param_payOrderId, orderEntity.getThreeorderid());
				dataMap.put(PaymentNotifyParamConstant.param_payNo, orderEntity.getPayno());
				dataMap.put(PaymentNotifyParamConstant.param_topPayNo, orderEntity.getToppayno());
				RequestDatagram rd = new RequestDatagram().initDatagram();
				rd.setSerialNo(serialNo);
				rd.setData(dataMap);
				logger.info("{}-异步通知商户前置操作-组装异步回调参数: {}", serialNo, rd.toString());
				return rd.toEncryptString(merchantPublicKey);
			}
		}
		String noticeParams = iPaymentTransfer1To2Service.noticeParams(orderEntity);
		logger.info("{}-异步通知商户前置操作-组装异步回调参数: {}", serialNo, noticeParams);
		return noticeParams;
	}

	/**
	 * 记录接收支付公司回调/查询支付交易后监控信息
	 * 
	 * @param orderEntity
	 * @param monitorValue
	 * @param gatewayStatus
	 */
	private void saveCallbackTradeMonitor(OrderEntity orderEntity, String monitorValue, String gatewayStatus) {
		TradeMonitorEntity tradeMonitorEntity = new TradeMonitorEntity();
		tradeMonitorEntity.setzOrderid(orderEntity.getOrderid());// 融智付订单号
		tradeMonitorEntity.setzOrderState(orderEntity.getState());// 融支付订单状态
		tradeMonitorEntity.setsOrderState(gatewayStatus);// 支付通道订单状态
		tradeMonitorEntity.setsIsNotify(1);// 是否回调
		tradeMonitorEntity.setsNotifyTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));// 回调时间
		tradeMonitorEntity.setsNotifyValue(monitorValue);// 回调值
		tradeMonitorEntity.setsIsCheck(1);// 查询上游之后将此值修改为已检查，回调之后也默认将此值设置为1,与支付交易1.0实现略有差别
		tradeMonitorService.updateSelective(tradeMonitorEntity);
		logger.info("支付回调监控-记录回调信息,监控内容:", tradeMonitorEntity.toString());
	}

	/**
	 * 记录异步通知商户交易信息
	 * 
	 * @param orderEntity
	 * @param monitorValue
	 */
	private void saveNotifyMerchantTradeMonitor(OrderEntity orderEntity, String monitorValue) {
		TradeMonitorEntity tradeMonitorEntity = new TradeMonitorEntity();
		tradeMonitorEntity.setzOrderid(orderEntity.getOrderid());// 融智付订单号
		tradeMonitorEntity.setcIsNotify(1);// 是否通知商户
		tradeMonitorEntity.setcNotifyTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		tradeMonitorEntity.setcNotifyValue(monitorValue);
		tradeMonitorService.updateSelective(tradeMonitorEntity);
		logger.info("支付回调监控-异步通知商户,监控内容:{}", tradeMonitorEntity.toString());
	}

	/**
	 * 修改订单状态为失败
	 */
	public void changeOrderStateFail(OrderEntity entity, String serialNo) {
		logger.info("订单状态修改为支付失败,报文序列号:{},参数:{}", serialNo, entity);
		entity.setState(3);
		orderService.updateSelective(entity);
		logger.info("订单状态修改为支付失败,报文序列号:{},结果:{}", serialNo, entity);
	}

	/**
	 * 获取各通道银行卡信息
	 */
	public Map<String, Map<String, Map<String, String>>> getBankInfo(String gatewayId) {
		GatewayEntity entity = gatewayService.getGateway(Integer.valueOf(gatewayId));
		String bankList = entity.getExt();
		Map<String, List> map = BeanUtils.json2Bean(bankList, Map.class);
		Map<String, Map<String, String>> bankNameMap = new HashMap<String, Map<String, String>>();
		Map<String, Map<String, String>> bankCodeMap = new HashMap<String, Map<String, String>>();
		for (Map.Entry<String, List> entry : map.entrySet()) {
			Map<String, String> nameMap = new HashMap<String, String>();
			Map<String, String> codeMap = new HashMap<String, String>();
			String typeKey = entry.getKey();
			List<Map> list = entry.getValue();
			for (Map listMap : list) {
				nameMap.put(String.valueOf(listMap.get("bankShortName")), String.valueOf(listMap.get("bankName")));
				codeMap.put(String.valueOf(listMap.get("bankShortName")), String.valueOf(listMap.get("bankCode")));
			}
			bankNameMap.put(typeKey, nameMap);
			bankCodeMap.put(typeKey, codeMap);
		}
		Map<String, Map<String, Map<String, String>>> resultMap = new HashMap<String, Map<String, Map<String, String>>>(2);
		resultMap.put("bankNameMap", bankNameMap);
		resultMap.put("bankCodeMap", bankCodeMap);
		return resultMap;
	}
}
