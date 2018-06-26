package com.gateway.controller.gateway;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gateway.common.constants.CommonConstant;
import com.gateway.common.constants.param.BaseParamConstant;
import com.gateway.common.constants.param.PaymentMethodConstant;
import com.gateway.common.constants.param.PaymentParamConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.common.utils.StringUtils;
import com.gateway.controller.gateway.base.BaseController;
import com.gateway.controller.gateway.util.SecretUtil;
import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.PersonApplicationEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.gateway.payment.service.adapter.IPaymentServiceAdapter;
import com.zitopay.datagram.request.RequestDatagram;
import com.zitopay.datagram.response.ResponseDatagram;
import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 融智付入口
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年1月19日
 */
@Controller
@RequestMapping("/payment")
public class PaymentController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

	@Reference
	IPaymentServiceAdapter paymentServiceAdapter;

	/**
	 * 非收银台接口
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
		Map<String, String> paramMap = new HashMap<String, String>();
		/**
		 * 1.报文验证
		 */
		try {
			String body = URLDecoder.decode(entity.getBody(), "UTF-8");
			rd.decrypt(body, SecretUtil.privateKey);
			logger.info("融智付-json,支付请求报文:{}", rd);
			paramMap = (Map<String, String>) rd.getData();
			rsd.setSerialNo(rd.getSerialNo());// 设置响应报文序列号与请求一致
			try {
				String pid = paramMap.get(PaymentParamConstant.param_merchantId);
				if (!"1".equals(paramMap.get(PaymentParamConstant.param_is_zitopay_cashier)) && !"1".equals(paramMap.get(BaseParamConstant.param_is_zitopay_v1))) {
					publicKey = paymentServiceAdapter.getMerchantPublicKey(pid, rd.getSerialNo());// 根据应用ID查询商户公钥
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return rsd.setResponseTips(ResponseInfoEnum.获取商户公钥异常).toEncryptString(publicKey);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return rsd.setResponseTips(ResponseInfoEnum.报文异常).toEncryptString(publicKey);
		}
		try {
			String operType = paramMap.get(PaymentParamConstant.param_oper_type);// 支付方法
			if (StringUtils.isEmpty(operType)) {
				paramMap.put(PaymentParamConstant.param_oper_type, PaymentMethodConstant.param_operate_pay);
			}
			/**
			 * 2.参数验证
			 */
			Map<String, Object> checkMap = paymentServiceAdapter.checkPaymentParameter(paramMap, rd.getSerialNo());
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
			Map<String, Object> permissionMap = paymentServiceAdapter.checkPaymentPermission(merchantId, applicationId, gatewayId, rd.getSerialNo());
			logger.info("融智付-json权限验证,报文序列号:{},验证结果：{}", rd.getSerialNo(), permissionMap);
			if (!ResponseInfoEnum.调用成功.equals(permissionMap)) {// 判断权限验证结果
				return rsd.setResponseTips(ResponseInfoEnum.valueOf(permissionMap)).toEncryptString(publicKey);
			}
			/**
			 * 4.支付调用
			 */
			PersonEntity zitopayPerson = (PersonEntity) permissionMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON);
			GatewayEntity zitopayGeteway = (GatewayEntity) permissionMap.get(CommonConstant.ZITOPAY_ENTITY_GATEWAY);
			PersonApplicationEntity zitopayPersonApplication = (PersonApplicationEntity) permissionMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON_APPLICATION);
			PersonGatewayEntity zitopayPersonGeteway = (PersonGatewayEntity) permissionMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON_GATEWAY);
			Map<String, Object> paymentResultMap = paymentServiceAdapter.execPayment(zitopayPerson, zitopayGeteway, zitopayPersonApplication, zitopayPersonGeteway, paramMap, rd.getSerialNo());
			logger.info("融智付-json支付调用,报文序列号:{},调用结果:{}", rd.getSerialNo(), paymentResultMap);
			rsd.setResponseTips(ResponseInfoEnum.valueOf(paymentResultMap));
			if (!ResponseInfoEnum.调用成功.equals(paymentResultMap) && !ResponseInfoEnum.等待用户输入密码.equals(paymentResultMap)) {// 判断支付结果并返回
				return rsd.toEncryptString(publicKey);
			}
			rsd.setData(paymentResultMap);
			logger.info("融智付-json,支付响应报文:{}", rsd);
			return rsd.toEncryptString(publicKey);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			return rsd.setResponseTips(ResponseInfoEnum.服务处理异常).toEncryptString(publicKey);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return rsd.setResponseTips(ResponseInfoEnum.系统异常).toEncryptString(publicKey);
		}
	}

	/**
	 * 支付成功接收通知
	 * 
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping(value = "/success/{merchantId}/{appid}/{gatewayid}/{serialNo}")
	public void callback(HttpServletRequest request, HttpServletResponse response, @PathVariable String merchantId, @PathVariable String appid, @PathVariable String gatewayid, @PathVariable String serialNo, HttpEntity<String> entity) {
		response.setContentType("text/html;charset=utf-8");
		try {
			/**
			 * 1.权限验证
			 */
			Map<String, Object> permissionMap = paymentServiceAdapter.checkPaymentPermission(merchantId, appid, gatewayid, serialNo);
			logger.info("融智付-回调权限验证,报文序列号:{},验证结果：{}", serialNo, permissionMap);
			if (!ResponseInfoEnum.调用成功.equals(permissionMap)) {// 判断权限验证结果
				response.getWriter().write("fail!");
			}

			/**
			 * 2.回调处理
			 */
			PersonEntity zitopayPerson = (PersonEntity) permissionMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON);
			GatewayEntity zitopayGeteway = (GatewayEntity) permissionMap.get(CommonConstant.ZITOPAY_ENTITY_GATEWAY);
			PersonApplicationEntity zitopayPersonApplication = (PersonApplicationEntity) permissionMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON_APPLICATION);
			PersonGatewayEntity zitopayPersonGeteway = (PersonGatewayEntity) permissionMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON_GATEWAY);

			Map<String, String> paramMap = getStrictParams(request.getParameterMap());// 转化map

			paramMap.put(CommonConstant.REQUEST_CONTENT, entity.getBody());
			Map<String, Object> callbackMap = paymentServiceAdapter.execCallback(zitopayPerson, zitopayGeteway, zitopayPersonApplication, zitopayPersonGeteway, paramMap, serialNo);
			logger.info("融智付-回调,报文序列号:{},结果:{}", serialNo, callbackMap);
			if (ResponseInfoEnum.调用成功.equals(callbackMap)) {
				String content = String.valueOf(callbackMap.get(ResponseInfoEnum.KEY_CONTENT));
				if (StringUtils.isBlank(content) || "null".equals(content)) {
					content = "success";
				}
				response.getWriter().write(content);
			} else {
				response.getWriter().write(callbackMap.get(ResponseInfoEnum.KEY_MSG).toString());
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}
}
