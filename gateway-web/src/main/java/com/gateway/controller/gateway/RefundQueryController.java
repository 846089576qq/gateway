package com.gateway.controller.gateway;

import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gateway.common.constants.CommonConstant;
import com.gateway.common.constants.param.PaymentParamConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.controller.gateway.base.BaseController;
import com.gateway.controller.gateway.util.SecretUtil;
import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.PersonApplicationEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.gateway.payment.service.adapter.IRefundQueryServiceAdapter;
import com.zitopay.datagram.request.RequestDatagram;
import com.zitopay.datagram.response.ResponseDatagram;
import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 融智付查询入口
 * 
 * 作者：王政 创建时间：2017年3月6日 下午5:57:03
 */
@Controller
@RequestMapping("/refundQuery")
public class RefundQueryController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(RefundQueryController.class);

	@Reference
	IRefundQueryServiceAdapter queryServiceAdapter;

	/**
	 * 订单查询接口
	 * 
	 * @param request
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/json", method = { RequestMethod.POST })
	public String jsonPayment(HttpServletRequest request, HttpEntity<String> entity) {
		String publicKey = SecretUtil.publicKey;
		ResponseDatagram rsd = new ResponseDatagram();
		RequestDatagram rd = new RequestDatagram();
		try {
			/**
			 * 1.报文验证
			 */
			Map<String, String> paramMap = null;
			try {
				String body = URLDecoder.decode(entity.getBody(), "UTF-8");
				rd.decrypt(body, SecretUtil.privateKey);
				paramMap = (Map<String, String>) rd.getData();
				rsd.setSerialNo(rd.getSerialNo());// 设置响应报文序列号与请求一致
				String pid = paramMap.get(PaymentParamConstant.param_merchantId);
				try {
					publicKey = queryServiceAdapter.getMerchantPublicKey(pid, rd.getSerialNo());// 根据应用ID查询商户公钥
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					return rsd.setResponseTips(ResponseInfoEnum.商户公钥不存在).toEncryptString(publicKey);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return rsd.setResponseTips(ResponseInfoEnum.报文异常).toEncryptString(publicKey);
			}
			logger.info("融智付-json,退款查询请求报文:{}", rd);
			/**
			 * 2.参数验证
			 */
			Map<String, Object> checkMap = queryServiceAdapter.checkQueryParameter(paramMap, rd.getSerialNo());
			logger.info("融智付-json参数验证,报文序列号:{},验证结果：{}", rd.getSerialNo(), checkMap);
			if (!ResponseInfoEnum.调用成功.equals(checkMap)) {
				return rsd.setResponseTips(ResponseInfoEnum.valueOf(checkMap)).toEncryptString(publicKey);
			}
			/**
			 * 3.权限验证
			 */
			String merchantId = paramMap.get(PaymentParamConstant.param_merchantId);
			String applicationId = paramMap.get(PaymentParamConstant.param_appId);
			String gatewayId = paramMap.get(PaymentParamConstant.param_gatewayId);
			Map<String, Object> permissionMap = queryServiceAdapter.checkQueryPermission(merchantId, applicationId, gatewayId, rd.getSerialNo());
			logger.info("融智付-json权限验证,报文序列号:{},验证结果：{}", rd.getSerialNo(), permissionMap);
			if (!ResponseInfoEnum.调用成功.equals(permissionMap)) {// 判断权限验证结果
				return rsd.setResponseTips(ResponseInfoEnum.valueOf(permissionMap)).toEncryptString(publicKey);
			}
			/**
			 * 4.查询调用
			 */
			PersonEntity zitopayPerson = (PersonEntity) permissionMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON);
			GatewayEntity zitopayGeteway = (GatewayEntity) permissionMap.get(CommonConstant.ZITOPAY_ENTITY_GATEWAY);
			PersonApplicationEntity zitopayPersonApplication = (PersonApplicationEntity) permissionMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON_APPLICATION);
			PersonGatewayEntity zitopayPersonGeteway = (PersonGatewayEntity) permissionMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON_GATEWAY);
			Map<String, Object> queryResultMap = queryServiceAdapter.execQuery(zitopayPerson, zitopayGeteway, zitopayPersonApplication, zitopayPersonGeteway, paramMap, rd.getSerialNo());
			logger.warn("融智付-json查询调用,报文序列号:{},调用结果:{}", rd.getSerialNo(), queryResultMap);
			rsd.setResponseTips(ResponseInfoEnum.valueOf(queryResultMap));
			if (!ResponseInfoEnum.调用成功.equals(queryResultMap)) {// 判断支付结果并返回
				return rsd.toEncryptString(publicKey);
			}
			rsd.setData(queryResultMap);
			logger.info("融智付-json,查询响应报文:{}", rsd);
			return rsd.toEncryptString(publicKey);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			return rsd.setResponseTips(ResponseInfoEnum.服务处理异常).toEncryptString(publicKey);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return rsd.setResponseTips(ResponseInfoEnum.系统异常).toEncryptString(publicKey);
		}
	}
}
