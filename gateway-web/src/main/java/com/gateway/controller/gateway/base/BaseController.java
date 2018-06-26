package com.gateway.controller.gateway.base;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.common.properties.EnvProperties;
import com.gateway.controller.gateway.util.SecretUtil;
//import com.gateway.controller.gateway.util.WechatOAuthUtil;
import com.zitopay.datagram.request.RequestDatagram;
import com.zitopay.datagram.response.ResponseDatagram;
import com.zitopay.foundation.common.util.BeanUtils;

/**
 * 融智付基类控制器
 * 
 * 作者：王政 创建时间：2017年3月6日 下午5:55:08
 */
public class BaseController {

	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

	@Autowired
	protected RestTemplate restTemplate;

	@Autowired
	protected EnvProperties properties;

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

	/**
	 * 调接口
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"/call", "{uri}/call"}, produces = "application/json;charset=UTF-8")
	public String call(HttpServletRequest request, @PathVariable(required = false) String uri) {
		String method = StringUtils.substringBeforeLast(request.getServletPath(), "/call");
		try {
			RequestDatagram rd = new RequestDatagram().initDatagram();
			rd.setData(getStrictParams(request.getParameterMap()));
			String encryptData = rd.toEncryptString(SecretUtil.publicKey);
			HttpEntity<String> httpEntity = new HttpEntity<String>(URLEncoder.encode(encryptData, "UTF-8"));
			ResponseDatagram rsd = restTemplate.postForObject(properties.PREFIX_GATEWAY_URL + method + "/json", httpEntity, ResponseDatagram.class);
			return rsd.toDecryptString(SecretUtil.privateKey);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return "处理异常";
	}

	@RequestMapping(value = "/{type}/encryptData")
	public String encryptData(HttpServletRequest request,@PathVariable String type) {
		RequestDatagram rd = new RequestDatagram().initDatagram();
		rd.setData(getStrictParams(request.getParameterMap()));
		String encryptData = rd.toEncryptString(SecretUtil.publicKey);
		request.setAttribute("encryptData", encryptData);
		return "zitopay/util/encryptData";
	}
	

	@ResponseBody
	@RequestMapping(value = "/decrypt", method = RequestMethod.POST)
	public String encryptDataForurl(HttpServletRequest request) {
		ResponseDatagram rsd = new ResponseDatagram();
		try {
			Map<String, String> params = getStrictParams(request.getParameterMap());// 参数转化map
			if (params != null && !params.isEmpty()) {
				rsd = BeanUtils.map2Bean(params, ResponseDatagram.class);
				return rsd.toDecryptString(SecretUtil.privateKey);
			}
			return rsd.setResponseTips(ResponseInfoEnum.报文异常).toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return rsd.setResponseTips(ResponseInfoEnum.系统异常).toString();
		}

	}


	@RequestMapping(value = "/form")
	public String form(HttpServletRequest request, ModelMap modelMap) {
		modelMap.put("datas", getStrictParams(request.getParameterMap()));
		return "gateway/util/form";
	}


	@RequestMapping(value = "/test/{type}", method = { RequestMethod.POST, RequestMethod.GET })
	public String test(@PathVariable String type) {
		return "gateway/test/" + type;
	}


//	@RequestMapping(value = "/getOpenid", method = { RequestMethod.POST, RequestMethod.GET })
//	public String getOpenid(HttpServletRequest request, ModelMap modelMap) {
//		Map<String, String> paramsMap = getStrictParams(request.getParameterMap());
//		String code = (StringUtils.isNotBlank(paramsMap.get("code")) ? paramsMap.get("code") : paramsMap.get("auth_code"));
//		String type = WechatOAuthUtil.getRequestType(request.getHeader("User-Agent").toLowerCase());
//		if(null == type){
//			modelMap.put("msg","请使用微信/支付宝浏览器请求");
//			return "zitopay/util/toOAuth";
//		}
//		if(StringUtils.isEmpty(code)){
//			modelMap.put("url",WechatOAuthUtil.createOAuthUrl(type, properties.PREFIX_GATEWAY_URL, paramsMap));
//		}else{
//			modelMap.put("openid",WechatOAuthUtil.exchangeOpendId(type, code, paramsMap));
//		}
//		return "zitopay/util/toOAuth";
//	}
}
