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
import com.gateway.common.constants.param.BaseParamConstant;
import com.gateway.common.constants.param.PaymentParamConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.controller.gateway.base.BaseController;
import com.gateway.controller.gateway.util.SecretUtil;
import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.gateway.payment.service.adapter.IIssuedServiceAdapter;
import com.zitopay.datagram.request.RequestDatagram;
import com.zitopay.datagram.response.ResponseDatagram;
import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 代付
 * 
 * 作者：王政 创建时间：2017年3月6日 下午5:57:23
 */
@Controller
@RequestMapping("/issued")
public class IssuedController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(IssuedController.class);

	@Reference
	IIssuedServiceAdapter issuedServiceAdapter;

	/**
	 * 订单代付接口
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
					if (!"1".equals(paramMap.get(BaseParamConstant.param_is_zitopay_v1))) {
						publicKey = issuedServiceAdapter.getMerchantPublicKey(pid, rd.getSerialNo());// 根据应用ID查询商户公钥
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					return rsd.setResponseTips(ResponseInfoEnum.商户公钥不存在).toEncryptString(publicKey);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return rsd.setResponseTips(ResponseInfoEnum.报文异常).toEncryptString(publicKey);
			}
			logger.info("融智付-json,支付请求报文:{}", rd);
			/**
			 * 2.参数验证
			 */
			Map<String, Object> checkMap = issuedServiceAdapter.checkIssuedParameter(paramMap, rd.getSerialNo());
			logger.info("融智付-json参数验证,报文序列号:{},验证结果：{}", rd.getSerialNo(), checkMap);
			if (!ResponseInfoEnum.调用成功.equals(checkMap)) {
				return rsd.setResponseTips(ResponseInfoEnum.valueOf(checkMap)).toEncryptString(publicKey);
			}
			/**
			 * 3.权限验证
			 */
			String merchantId = paramMap.get(PaymentParamConstant.param_merchantId);
			String gatewayId = paramMap.get(PaymentParamConstant.param_gatewayId);
			Map<String, Object> permissionMap = issuedServiceAdapter.checkIssuedPermission(merchantId, gatewayId, rd.getSerialNo());
			logger.info("融智付-json权限验证,报文序列号:{},验证结果：{}", rd.getSerialNo(), permissionMap);
			if (!ResponseInfoEnum.调用成功.equals(permissionMap)) {// 判断权限验证结果
				return rsd.setResponseTips(ResponseInfoEnum.valueOf(permissionMap)).toEncryptString(publicKey);
			}
			/**
			 * 4.代付调用
			 */
			PersonEntity zitopayPerson = (PersonEntity) permissionMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON);
			GatewayEntity zitopayGeteway = (GatewayEntity) permissionMap.get(CommonConstant.ZITOPAY_ENTITY_GATEWAY);
			PersonGatewayEntity zitopayPersonGeteway = (PersonGatewayEntity) permissionMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON_GATEWAY);

			Map<String, Object> drawResultMap = issuedServiceAdapter.execIssued(zitopayPerson, zitopayGeteway, zitopayPersonGeteway, paramMap, rd.getSerialNo());

			logger.warn("融智付-json代付调用,报文序列号:{},调用结果:{}", rd.getSerialNo(), drawResultMap);
			rsd.setResponseTips(ResponseInfoEnum.valueOf(drawResultMap));
			if (!ResponseInfoEnum.调用成功.equals(drawResultMap)) {// 判断代付结果并返回
				return rsd.toEncryptString(publicKey);
			}
			rsd.setData(drawResultMap);
			logger.info("融智付-json,代付响应报文:{}", rsd);
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
