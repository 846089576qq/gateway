package com.gateway.controller.gateway;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.controller.gateway.base.BaseController;
import com.gateway.controller.gateway.util.SecretUtil;
import com.gateway.payment.service.adapter.IZitopayMerchantRegisterServiceAdapter;
import com.zitopay.datagram.request.RequestDatagram;
import com.zitopay.datagram.response.ResponseDatagram;
import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 融智付商户入驻入口
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年1月19日
 */
@Controller
@RequestMapping("/merchant")
public class RegisterController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

	@Reference
	IZitopayMerchantRegisterServiceAdapter registerServiceAdapter;

	/**
	 * 商户入驻接口
	 * 
	 * @param request
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/{method}/json", method = { RequestMethod.POST })
	public String jsonPayment(HttpServletRequest request, HttpEntity<String> entity, @PathVariable String method) {
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
			logger.info("融智付-json,入驻请求报文:{}", rd);
			paramMap = (Map<String, String>) rd.getData();
			rsd.setSerialNo(rd.getSerialNo());// 设置响应报文序列号与请求一致
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return rsd.setResponseTips(ResponseInfoEnum.报文异常).toEncryptString(publicKey);
		}
		try {
			/**
			 * 2.参数验证
			 */
			Map<String, Object> checkMap = registerServiceAdapter.checkRegisterParameter(method, paramMap, rd.getSerialNo());
			logger.info("融智付-json参数验证,报文序列号:{},验证结果：{}", rd.getSerialNo(), checkMap);
			if (!ResponseInfoEnum.调用成功.equals(checkMap)) {
				return rsd.setResponseTips(ResponseInfoEnum.valueOf(checkMap)).toEncryptString(publicKey);
			}
			/**
			 * 3.权限验证
			 */
			Map<String, Object> permissionMap = registerServiceAdapter.checkRegisterPermission(method, paramMap, rd.getSerialNo());
			logger.info("融智付-json权限验证,报文序列号:{},验证结果：{}", rd.getSerialNo(), permissionMap);
			if (!ResponseInfoEnum.调用成功.equals(permissionMap)) {// 判断权限验证结果
				return rsd.setResponseTips(ResponseInfoEnum.valueOf(permissionMap)).toEncryptString(publicKey);
			}
			/**
			 * 4.支付调用
			 */
			Map<String, Object> registerResultMap = registerServiceAdapter.execRegister(method, paramMap, permissionMap, rd.getSerialNo());
			logger.info("融智付-json支付调用,报文序列号:{},调用结果:{}", rd.getSerialNo(), registerResultMap);
			rsd.setResponseTips(ResponseInfoEnum.valueOf(registerResultMap));
			if (!ResponseInfoEnum.调用成功.equals(registerResultMap)) {// 判断入驻结果并返回
				return rsd.toEncryptString(publicKey);
			}
			rsd.setData(registerResultMap);
			logger.info("融智付-json,入驻响应报文:{}", rsd);
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
