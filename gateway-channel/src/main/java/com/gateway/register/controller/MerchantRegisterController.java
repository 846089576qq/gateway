/**
 * Copyright [2015-2017]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the Licensee. You may
 * obtain a copy of the License at
 * 
 * http://www.apachee.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the Licensee.
 */
package com.gateway.register.controller;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.common.constants.param.MerchantRegisterMethodConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.service.adapter.IMerchantRegisterServiceAdapter;
import com.zitopay.datagram.request.RequestDatagram;
import com.zitopay.datagram.response.ResponseDatagram;
import com.zitopay.foundation.common.exception.ServiceException;
import com.zitopay.foundation.common.util.BeanUtils;

/**
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年4月19日
 */
@CrossOrigin
@RestController
@RequestMapping("/merchant")
public class MerchantRegisterController {

	private static final Logger logger = LoggerFactory.getLogger(MerchantRegisterController.class);
	
	@Autowired
	private IMerchantRegisterServiceAdapter merchantRegisterServiceAdapter;


	@SuppressWarnings("unchecked")
	@RequestMapping("/{method}")
	public String register(HttpEntity<String> entity, @PathVariable String method) {
		ResponseDatagram rsd = new ResponseDatagram();
		if(!MerchantRegisterMethodConstant.method_register.equals(method) && !MerchantRegisterMethodConstant.method_update.equals(method) && !MerchantRegisterMethodConstant.method_query.equals(method)) {
			return rsd.setResponseTips(ResponseInfoEnum.调用失败).toString();
		}
		
		RequestDatagram rd = new RequestDatagram().initDatagram();
		Map<String, String> paramMap = new HashMap<String, String>();
		/**
		 * 1.报文验证
		 */
		try {
			String body = URLDecoder.decode(entity.getBody(), "UTF-8");
			rd = BeanUtils.json2Bean(body, RequestDatagram.class);
			logger.info("融智付-json,商户入驻请求报文:{}", rd);
			paramMap = (Map<String, String>) rd.getData();
			rsd.setSerialNo(rd.getSerialNo());// 设置响应报文序列号与请求一致
		} catch (Exception ee) {
			logger.error(ee.getMessage(), ee);
			return rsd.setResponseTips(ResponseInfoEnum.报文异常).toString();
		}
		
		try {
			/**
			 * 2.参数验证
			 */
			Map<String, Object> checkMap = merchantRegisterServiceAdapter.checkRegisterParameter(method, paramMap, rd.getSerialNo());
			logger.info("融智付-json参数验证,报文序列号:{},验证结果：{}", rd.getSerialNo(), checkMap);
			if (!ResponseInfoEnum.调用成功.equals(checkMap)) {
				return rsd.setResponseTips(ResponseInfoEnum.valueOf(checkMap)).toString();
			}
			
			/**
			 * 3.入驻调用
			 */
			Map<String, Object> paymentResultMap = merchantRegisterServiceAdapter.execRegister(method, paramMap, rd.getSerialNo());
			logger.info("融智付-json入驻调用,报文序列号:{},调用结果:{}", rd.getSerialNo(), paymentResultMap);
			if (!ResponseInfoEnum.调用成功.equals(paymentResultMap)) {// 判断支付结果并返回
				return rsd.setResponseTips(ResponseInfoEnum.valueOf(paymentResultMap)).toString();
			}
			rsd.setData(paymentResultMap);
			logger.info("融智付-json,入驻响应报文:{}", rsd);
			
			return rsd.toString();
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			return rsd.setResponseTips(ResponseInfoEnum.服务处理异常).toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return rsd.setResponseTips(ResponseInfoEnum.系统异常).toString();
		}
	}
	
}
