/**
 * Copyright [2015-2017]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.gateway.payment.service.adapter;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gateway.common.constants.CommonConstant;
import com.gateway.common.constants.ForwardConstant;
import com.gateway.common.constants.config.GatewayConstants;
import com.gateway.common.constants.param.PaymentMethodConstant;
import com.gateway.common.constants.param.PaymentParamConstant;
import com.gateway.common.constants.param.PaymentResultParamConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.common.utils.StringUtils;
import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.OrderEntity;
import com.gateway.payment.entity.PersonApplicationEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.gateway.payment.service.adapter.IPaymentServiceAdapter;
import com.gateway.payment.service.base.IBasePaymentService;
import com.zitopay.datagram.request.RequestDatagram;
import com.zitopay.foundation.common.exception.ServiceException;
import com.zitopay.foundation.common.util.PKUtils;

/**
 * 融智付非收银台服务实现
 * 
 * 作者：王政 创建时间：2017年3月6日 下午5:59:13
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class PaymentServiceImplAdapter extends BaseServiceImplAdapter implements IPaymentServiceAdapter {

	private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImplAdapter.class);

	@Override
	public Map<String, Object> checkPaymentParameter(Map<String, String> paramMap, String serialNo) throws ServiceException {
		try {
			// 非收银台接口需要检查通道ID
			String gatewayId = paramMap.get(PaymentParamConstant.param_gatewayId); // 通道ID，由融智付分配
			if (StringUtils.isBlank(gatewayId)) {
				return ResponseInfoEnum.参数为空.getMap("参数[" + PaymentParamConstant.param_gatewayId + "]不能为空");
			}
			IBasePaymentService basePayApiService = getPaymentService(gatewayId);
			if (null == basePayApiService) {
				return ResponseInfoEnum.调用失败.getMap("未找到编号[" + gatewayId + "]的支付通道");
			}
			return basePayApiService.checkPaymentParam(paramMap, serialNo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",支付参数校验异常,请检查!", e);
		}

	}

	@Override
	public Map<String, Object> checkPaymentPermission(String merchantId, String applicationId, String gatewayId, String serialNo) throws ServiceException {
		try {
			return checkPermission(merchantId, applicationId, gatewayId, serialNo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",支付权限校验异常,请检查!", e);
		}
	}

	@Override
	public Map<String, Object> execPayment(final PersonEntity personEntity, GatewayEntity gatewayEntity, PersonApplicationEntity personApplicationEntity, final PersonGatewayEntity personGatewayEntity, Map<String, String> paramMap, final String serialNo) throws ServiceException {
		try {
			String gatewayId = String.valueOf(gatewayEntity.getId());
			String operType = paramMap.get(PaymentParamConstant.param_oper_type);// 支付方法
			String orderidinf = paramMap.get(PaymentParamConstant.param_merchantOrderId);
			final IBasePaymentService basePayApiService = getPaymentService(gatewayId);
			if (null == basePayApiService) {
				return ResponseInfoEnum.调用失败.getMap("未找到编号[" + gatewayId + "]的支付通道");
			}
			OrderEntity orderEntity = null;
			if (PaymentMethodConstant.param_operate_pay.equals(operType)) {
				// 生成订单
				if ("1".equals(paramMap.get(PaymentParamConstant.param_is_zitopay_cashier))) {
					orderEntity = orderService.getPersonApplicationOrder(personEntity.getId(), personApplicationEntity.getAppId(), orderidinf);
					orderEntity.setGetewayid(Integer.valueOf(gatewayId));
					orderService.updateByPrimaryKeySelective(orderEntity);
				} else {
					Map<String, Object> buildOrderMap = initPaymentTrade(personApplicationEntity, personEntity, gatewayEntity, paramMap, serialNo);
					if (!ResponseInfoEnum.调用成功.equals(buildOrderMap)) {
						return buildOrderMap;
					}
					orderEntity = (OrderEntity) buildOrderMap.get(CommonConstant.ZITOPAY_ENTITY_ORDER);
				}
				if (StringUtils.isEmpty(orderEntity.getThreeorderid())) {
					orderEntity.setThreeorderid(String.valueOf(PKUtils.getInstance().longPK()));// 下单时创建第三方订单号
					orderService.updateByPrimaryKeySelective(orderEntity);
				}
			} else {
				String orderId = String.valueOf(paramMap.get(PaymentParamConstant.param_order_id));
				orderEntity = orderService.findByOrderId(orderId);
			}
			if (orderEntity == null && !PaymentMethodConstant.param_operate_bindcard.equals(operType) && !PaymentMethodConstant.param_operate_bindcardconfirm.equals(operType) && !PaymentMethodConstant.param_operate_unbindcard.equals(operType) && !PaymentMethodConstant.param_operate_banklist.equals(operType) && !PaymentMethodConstant.param_operate_facecheck.equals(operType) && !PaymentMethodConstant.param_operate_facecheckquery.equals(operType)) {
				return ResponseInfoEnum.参数组装失败.getMap("创建商户[" + personEntity.getPid() + "],支付订单失败");
			}

			// String otherOfficial = paramMap.get(PaymentParamConstant.param_other_official);// 是否大商户模式
			// if(null != otherOfficial && otherOfficial.equals("1")){
			// // 更新订单表
			// basePayApiService.payingPayment(orderEntity, orderEntity.getThreeorderid(), personGatewayEntity);
			// return WechatUtil.createPayUrl(paramMap, orderEntity.getOrderid(), personGatewayEntity.getGetewayid(),
			// personEntity.getRsapublickey(), envProperties.PREFIX_GATEWAY_URL, extensionProperties.getWechatProperty().WECHAT_RTZF_APPID,
			// serialNo);// 大商户模式-生成支付地址
			// }

			if (gatewayService.isCategoryGateway(gatewayId, "WY")) {
				// 获取银行卡列表
				if (PaymentMethodConstant.param_operate_banklist.equals(paramMap.get(PaymentParamConstant.param_oper_type))) {
					final Map<String, Map<String, String>> bankCodeMap = basePayApiService.getBankInfo(gatewayId).get("bankNameMap");
					return ResponseInfoEnum.调用成功.getMap(new HashMap<String, Object>() {

						private static final long serialVersionUID = 1L;
						{
							put("debitMap", bankCodeMap.get("1"));
							put("creditMap", bankCodeMap.get("2"));
						}
					});
				}

				String cardType = paramMap.get(PaymentParamConstant.param_card_type);
				String bankCode = paramMap.get(PaymentParamConstant.param_bank_code);

				// 默认跳转融智付收银台
				if (!ForwardConstant.CASHIER_URL_TYPE_THREE.equals(GatewayConstants.GatewayURL.getEnum(gatewayId).getUrl()) && (paramMap.get(PaymentParamConstant.param_card_type) == null || paramMap.get(PaymentParamConstant.param_bank_code) == null)) {
					RequestDatagram rd = new RequestDatagram().initDatagram();
					Map<String, String> dataMap = new HashMap<String, String>();
					dataMap.put(PaymentParamConstant.param_merchantId, personEntity.getPid());
					dataMap.put(PaymentParamConstant.param_appId, personApplicationEntity.getAppId());
					dataMap.put(PaymentParamConstant.param_merchantOrderId, orderEntity.getOrderidinf());
					dataMap.put(PaymentParamConstant.param_orderTitle, orderEntity.getOrdertitle());
					dataMap.put(PaymentParamConstant.param_orderAmount, orderEntity.getTotalprice().toString());
					dataMap.put(PaymentParamConstant.param_orderTime, DateFormatUtils.format(orderEntity.getCreatetime(), "yyyyMMddHHmmssSSS"));
					dataMap.put(PaymentParamConstant.param_merchantOrderId, orderEntity.getOrderidinf());
					dataMap.put(PaymentParamConstant.param_merchantNotifyUrl, orderEntity.getBgreturl());
					dataMap.put(PaymentParamConstant.param_merchantUrl, orderEntity.getReturnurl());
					rd.setData(dataMap);
					String encryptData = rd.toEncryptString(secretService.getRsaPublicKey());

					final String url = envProperties.PREFIX_GATEWAY_URL + "/cashier/channel/" + GatewayConstants.GatewayURL.getEnum(gatewayEntity.getId().toString()).getUrl() + "?gateway_id=" + gatewayEntity.getId() + "&encryptData=" + URLEncoder.encode(encryptData, "UTF-8");
					final String orderId = orderEntity.getOrderid();
					return ResponseInfoEnum.调用成功.getMap(new HashMap<String, Object>() {

						private static final long serialVersionUID = 1L;
						{
							put(PaymentResultParamConstant.param_url, url);
							put(PaymentResultParamConstant.param_orderid, orderId);
						}
					});
				}

				if (!StringUtils.isNull(cardType) && !StringUtils.isNull(bankCode)) {
					// paramMap.put(PaymentParamConstant.param_card_type, "");
					Map<String, Map<String, String>> bankCodeMap = basePayApiService.getBankInfo(gatewayId).get("bankCodeMap");
					Map<String, String> map = bankCodeMap.get(cardType);
					paramMap.put(PaymentParamConstant.param_bank_code, map.get(bankCode));
				}
			}

			/**
			 * 调用支付接口
			 */
			Map<String, Object> map = basePayApiService.pay(personEntity, gatewayEntity, personApplicationEntity, personGatewayEntity, orderEntity, paramMap, serialNo);

			/**
			 * 更新订单状态为未支付、同时更新费率等信息
			 */
			basePayApiService.payingPayment(orderEntity, personGatewayEntity, serialNo);

			if (ResponseInfoEnum.调用成功.equals(map) && orderEntity != null) {
				final OrderEntity order = orderEntity;
				final Date paysuccesstime = new Date();
				String threeorderid = order.getThreeorderid();
				if (PaymentResultParamConstant.paying.equals(map.get(PaymentResultParamConstant.pay_status))) {
					map = ResponseInfoEnum.等待用户输入密码.getMap(new HashMap<String, Object>() {

						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						{
							put(PaymentResultParamConstant.param_orderid, order.getOrderid());
						}

					});
					/*
					 * 开启线程查询 taskExecutor.execute(new Runnable() {
					 * @Override public void run() { boolean threadFlag = true; int count = 1; while (threadFlag) { try {
					 * Thread.sleep(5000); Map<String, Object> map = basePayApiService.queryOrder(order, personGatewayEntity, serialNo); if
					 * (ResponseInfoEnum.调用成功.equals(map)) { if ((boolean) map.get(CommonConstant.TRADESTATUS)) {
					 * basePayApiService.afterPayment(personEntity.getPid(), personEntity.getRsapublickey(), order.getThreeorderid(),
					 * paysuccesstime, String.valueOf(map.get(CommonConstant.TRADENO)), String.valueOf(map.get(CommonConstant.RESULT)),
					 * "true", serialNo); threadFlag = false; } else { basePayApiService.changeOrderStateFail(order); threadFlag = false; }
					 * } count++; if (count == 5) { threadFlag = false; } } catch (Exception e) { logger.error("报文序列号:" + serialNo +
					 * ",条码-异步线程查询出错,请检查!", e); } } } });
					 */

				} else if (PaymentResultParamConstant.success.equals(map.get(PaymentResultParamConstant.pay_status))) {
					String payNo = String.valueOf(map.get(CommonConstant.TRADENO));
					String topPayNo = String.valueOf(map.get(CommonConstant.TOPTRADENO));
					String result = String.valueOf(map.get(CommonConstant.RESULT));
					basePayApiService.afterPayment(personEntity.getPid(), personEntity.getRsapublickey(), threeorderid, paysuccesstime, payNo, topPayNo, result, "true", serialNo);
					map.remove(CommonConstant.RESULT);
					map.remove(PaymentResultParamConstant.pay_status);
				} else if (PaymentResultParamConstant.failure.equals(map.get(PaymentResultParamConstant.pay_status))) {
					basePayApiService.changeOrderStateFail(order, serialNo);
					map = ResponseInfoEnum.调用失败.getMap();
				}
			} else {
				logger.warn("交易处理失败,报文序列号:{},内容:\n{}", serialNo, map);
			}
			return map;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("交易处理异常,报文序列号:" + serialNo + ",请检查!", e);
		}

	}

	@Override
	public Map<String, Object> execCallback(PersonEntity personEntity, GatewayEntity gatewayEntity, PersonApplicationEntity personApplicationEntity, PersonGatewayEntity personGatewayEntity, Map<String, String> paramMap, String serialNo) throws ServiceException {
		try {
			String merchantKey = getMerchantPublicKey(personEntity.getPid(), serialNo);
			String gatewayId = String.valueOf(gatewayEntity.getId());
			IBasePaymentService basePayApiService = getPaymentService(gatewayId);
			if (null == basePayApiService) {
				return ResponseInfoEnum.支付回调失败.getMap("未找到编号[" + gatewayId + "]的支付通道");
			}
			Map map = basePayApiService.callback(personEntity, gatewayEntity, personApplicationEntity, personGatewayEntity, paramMap, merchantKey, serialNo);
			logger.info("回调处理结果:{},报文序列号:{}", map, serialNo);
			return map;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("交易-回调用处理异常,报文序列号:" + serialNo + ",请检查!", e);
		}
	}

}