package com.gateway.payment.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.service.biz.ysf.IYsfWGCallbackPamentService;
import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 易收付回调对账
 */
@Controller
@RequestMapping("/ysfcallback")
public class YsfCallbackCheckController {

	private static final Logger logger = LoggerFactory.getLogger(YsfCallbackCheckController.class);

	@Resource
	IYsfWGCallbackPamentService ysfWGPamentService;

	/**
	 * 支付成功接收通知
	 * 
	 * @param request
	 * @param entity
	 * @return
	 */
	@RequestMapping("check")
	public void callback(HttpServletRequest request, HttpServletResponse response) {
		String serialNo = "ysfCallbackCheck";
		response.setContentType("text/html;charset=utf-8");
		try {
			Map<String, String> paramMap = getStrictParams(request.getParameterMap());// 转化map

			Map<String, Object> callbackMap = ysfWGPamentService.callbackCheck(paramMap, serialNo);
			logger.info("融智付-回调,报文序列号:{},结果:{}", serialNo, callbackMap);
			if (ResponseInfoEnum.调用成功.equals(callbackMap)) {
				response.getWriter().write("ipscheckok");
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
	
	/**
	 * 普通获取参数为map
	 * 
	 * @param params
	 * @return
	 * @authorXC 创建日期:2012-8-23 下午1:33:28
	 */
	public static Map<String, String> getStrictParams(Map<String, String[]> params) {
		Map<String, String> strictParams = new TreeMap<String, String>();

		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			String value = params.get(key)[0];
			/*if (value != null && !value.trim().equals("")) {*/
				strictParams.put(key, value);
			/*}*/
		}
		logger.debug("参数信息:" + JSON.toJSONString(strictParams));
		return strictParams;
	}
	
}
