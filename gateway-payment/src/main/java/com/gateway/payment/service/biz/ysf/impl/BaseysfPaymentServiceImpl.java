package com.gateway.payment.service.biz.ysf.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gateway.channel.entity.ysf.pay.YsfPayInDto;
import com.gateway.channel.entity.ysf.query.YsfQueryInDto;
import com.gateway.channel.service.ysf.IYsfChannelService;
import com.gateway.common.constants.CommonConstant;
import com.gateway.common.constants.param.PaymentParamConstant;
import com.gateway.common.constants.param.PaymentResultParamConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.OrderEntity;
import com.gateway.payment.entity.PersonApplicationEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.gateway.payment.service.base.impl.BasePaymentServiceImpl;
import com.zitopay.foundation.common.exception.ServiceException;
import com.zitopay.foundation.common.util.BeanUtils;

public abstract class BaseysfPaymentServiceImpl extends BasePaymentServiceImpl {

	protected static final Logger logger = LoggerFactory.getLogger(BaseysfPaymentServiceImpl.class);

	public abstract IYsfChannelService getChannelService();

	@Override
	public Map<String, Object> pay(PersonEntity personEntity, GatewayEntity gatewayEntity, PersonApplicationEntity personApplicationEntity, PersonGatewayEntity personGatewayEntity, OrderEntity orderEntity, Map<String, String> paramMap, String serialNo) throws ServiceException {
		try {
			// 商户号
			String mch_id = personGatewayEntity.getGatewaypid();
			// 商户名称
			String mch_name = personGatewayEntity.getGatewayname();
			// 商户号名称
			String mch_account = personGatewayEntity.getChannelid();
			// 秘钥
			String key = personGatewayEntity.getGatewaykey();
			// 订单号
			String outTradeNo = orderEntity.getThreeorderid();
			// 订单标题
			String ordertitle = orderEntity.getOrdertitle();
			final String orderId = orderEntity.getOrderid();
			// 订单金额 单位：元
			// 融智付平台单位：元
			String totalAmount = orderEntity.getTotalprice().toString();
			String notifyUrl = envProperties.getNotifyUrl(personEntity.getPid(), personApplicationEntity.getAppId(), gatewayEntity.getId(), serialNo);
			
			String cardType = paramMap.get(PaymentParamConstant.param_card_type).toString();
			// 银行英文简称
			String bankCode = paramMap.get(PaymentParamConstant.param_bank_code).toString();
			
			YsfPayInDto in = new YsfPayInDto();
			in.setMerBillNo(outTradeNo);
			in.setAmount(totalAmount);
			in.setDate(orderEntity.getCreatedate());
			in.setGatewayType("0" + cardType);
			in.setMerchantUrl(envProperties.getReturnUrl(orderId));
			in.setFailUrl(envProperties.getReturnUrl(orderId));
			in.setServerUrl(notifyUrl);
			in.setGoodsName(ordertitle);
			in.setIsCredit("1");
			in.setBankcode(bankCode);
			in.setProductType("1");
			in.setMerCode(mch_id);
			in.setMerName(mch_name);
			in.setMerAcccode(mch_account);

			logger.info("~~~易收付支付调用,入参：" + in.toString() + "--zitopayPersonGeteway" + personGatewayEntity.toString());

			final String url = getChannelService().pay(in, key);

			if (StringUtils.isEmpty(url)) {
				return ResponseInfoEnum.调用失败.getMap("跳转结果为空请联系管理员");
			}
			/* 3.更新订单表 */
			return ResponseInfoEnum.调用成功.getMap(new HashMap<String, Object>() {

				private static final long serialVersionUID = 1L;
				{
					put(PaymentResultParamConstant.param_url, url);
					put(PaymentResultParamConstant.param_orderid, orderId);
				}
			});
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",易收付-下订单异常,请检查!", e);
		}
	}

	@Override
	public Map<String, Object> callback(PersonEntity personEntity, GatewayEntity gatewayEntity, PersonApplicationEntity personApplicationEntity, PersonGatewayEntity personGatewayEntity, Map<String, String> paramMap, String merchantKey, String serialNo) throws ServiceException {
		try {
			logger.info("易收付api-支付回调,报文序列号:{},接收参数{}", serialNo, BeanUtils.bean2JSON(paramMap));

			// 获取xml
			String resultXml = paramMap.get("paymentResult");
			
			// 商户号
			String mch_id = personGatewayEntity.getGatewaypid();
			// 秘钥
			String key = personGatewayEntity.getGatewaykey();
			String publickey = personGatewayEntity.getRsa();
						

			/** 数据验签 */
			paramMap.remove(CommonConstant.REQUEST_CONTENT);
			Map<String, String> checkMap = getChannelService().checkNotify(resultXml, mch_id, key, publickey);
			if (!"true".equals(checkMap.get("success"))) {
				return ResponseInfoEnum.验签失败.getMap();
			}
			
			//2、验签通过，判断IPS返回状态码
			if (!checkMap.get("rspCode").equals("000000")) {
				return ResponseInfoEnum.调用失败.getMap("请求响应不成功");
			}

			String order_id = checkMap.get("merBillNo");// 第三方订单号
			String payno = checkMap.get("ipsTradeNo");// 流水号
			String topPayno = checkMap.get("bankBillNo");
			String tradeStatus = "false";
			if (checkMap.get("status").equals("Y"))
				tradeStatus = "true";

			return afterPayment(personEntity.getPid(), merchantKey, order_id, new Date(), payno, topPayno, BeanUtils.bean2JSON(paramMap), tradeStatus, serialNo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",易收付-回调异常,请检查!", e);
		}
	}

	@Override
	public Map<String, Object> queryOrder(OrderEntity orderEntity, PersonGatewayEntity personGatewayEntity, String serialNo) throws ServiceException {
		// 商户号
		String mch_id = personGatewayEntity.getGatewaypid();
		// 商户名称
		String mch_name = personGatewayEntity.getGatewayname();
		// 商户号名称
		String mch_account = personGatewayEntity.getChannelid();
		// 秘钥
		String key = personGatewayEntity.getGatewaykey();
		String order_no = orderEntity.getThreeorderid();// 订单号
		YsfQueryInDto in = new YsfQueryInDto();
		in.setMerCode(mch_id);
		in.setMerName(mch_name);
		in.setAccCode(mch_account);
		in.setMerBillNo(order_no);
		in.setDate(orderEntity.getCreatedate());
		in.setAmount(orderEntity.getTotalprice().toString());

		logger.info("易收付api-查询订单请求,报文序列号:{},接收参数{}", serialNo, BeanUtils.bean2JSON(in));
		final Map<String, String> queryMap = getChannelService().query(in, key);
		logger.info("易收付api-查询订单请求,报文序列号:{},响应参数{}", serialNo, BeanUtils.bean2JSON(queryMap));

		if (null == queryMap) {
			logger.error("返回数据为null");
			return ResponseInfoEnum.调用失败.getMap("返回数据为null");
		}
		logger.info("通道查询响应数据：" + queryMap.toString());
		if (!"000000".equals(queryMap.get("rspCode"))) {
			return ResponseInfoEnum.调用失败.getMap("错误码:" + queryMap.get("rspCode"));
		}
		logger.info("调用易收付查询接口===单笔交易查询接口结束");
		return ResponseInfoEnum.调用成功.getMap(new HashMap<String, Object>() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			{
				put(CommonConstant.TRADESTATUS, "Y".equals(queryMap.get("status")));
				put(CommonConstant.RESULT, queryMap);
				put(CommonConstant.TRADENO, queryMap.get("IpsBillNo"));
			}
		});
	}

}
