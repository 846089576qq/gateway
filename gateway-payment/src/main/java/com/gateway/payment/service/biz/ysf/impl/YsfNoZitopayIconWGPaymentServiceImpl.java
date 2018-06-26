package com.gateway.payment.service.biz.ysf.impl;

import java.util.Date;
import java.util.Map;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gateway.channel.service.ysf.IYsfChannelService;
import com.gateway.common.annotaion.Gateway;
import com.gateway.common.constants.CommonConstant;
import com.gateway.common.constants.config.GatewayConstants;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.entity.OrderEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.gateway.payment.service.IBaseService;
import com.gateway.payment.service.biz.ysf.IYsfWGCallbackPamentService;
import com.gateway.payment.service.biz.ysf.IYsfWGPamentService;
import com.zitopay.foundation.common.exception.ServiceException;
import com.zitopay.foundation.common.util.BeanUtils;

@Gateway(gatewayId = IBaseService.BEAN_PREFIX_PAY + GatewayConstants.GATEWAY_YSF_NO_ICON_WG)
public class YsfNoZitopayIconWGPaymentServiceImpl extends BaseysfPaymentServiceImpl implements IYsfWGPamentService, IYsfWGCallbackPamentService {

	@Reference
	private IYsfChannelService channelService;

	@Override
	public IYsfChannelService getChannelService() {
		return channelService;
	}
	

	@Override
	public Map<String, Object> callbackCheck(Map<String, String> paramMap, String serialNo) throws ServiceException {
		try {
			logger.info("易收付api-支付回调,报文序列号:{},接收参数{}", serialNo, BeanUtils.bean2JSON(paramMap));

			// 获取xml
			String resultXml = paramMap.get("paymentResult");
			
			String threeorderid = channelService.getMerBillNo(resultXml);
			
			OrderEntity orderEntity = orderService.findByhreerderid(threeorderid);
			if (null == orderEntity) {
				String msg = "未找到对应订单,订单号[" + threeorderid + "]不存在.";
				logger.warn(msg);
				return ResponseInfoEnum.订单不存在.getMap(msg);
			}
			
			PersonGatewayEntity personGatewayEntity = personGatewayService.getZitopayPersonGateway(orderEntity.getPersionid(), orderEntity.getGetewayid());
			
			PersonEntity personEntity = personService.queryByPersonId(orderEntity.getPersionid());
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

			return afterPayment(personEntity.getPid(), personEntity.getRsapublickey(), order_id, new Date(), payno, topPayno, BeanUtils.bean2JSON(paramMap), tradeStatus, serialNo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",易收付-回调异常,请检查!", e);
		}
	}
	
}
