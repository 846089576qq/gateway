package com.gateway.controller.payment;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.gateway.controller.gateway.base.BaseController;
import com.gateway.controller.gateway.util.SecretUtil;
import com.gateway.payment.service.transfer.IPaymentTransfer1To2Service;

@Controller
@RequestMapping("/api/alwaypay/register")
public class ToRegisterController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ToRegisterController.class);

	@Reference
	public IPaymentTransfer1To2Service iPaymentTransfer1To2Service;
	
	/**
	 * 请求商户入驻
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "toRegister", produces = "text/html;charset=UTF-8")
	public String toRegister(HttpServletRequest request) {
		Map<String, String> paramMap = getStrictParams(request.getParameterMap());
		logger.info("融智付商户入驻开始，入参：{}", JSON.toJSONString(paramMap));
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (null == paramMap || paramMap.isEmpty()) {
				resultMap.put("success", false);
				resultMap.put("msg", "系统调用失败!请联系管理员!");
				resultMap.put("code", "2x0003");
			} else {
				Map<String, Object> dataMap = iPaymentTransfer1To2Service.toV2Register(SecretUtil.publicKey, SecretUtil.privateKey, paramMap);
				resultMap.putAll(dataMap);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resultMap.put("success", false);
			resultMap.put("msg", "参数有误,支付渠道系统调用失败!请联系管理员!");
			resultMap.put("code", "2x0003");
		}
		logger.info("融智付商户入驻结束，出参：{}", JSON.toJSONString(resultMap));
		return JSON.toJSONString(resultMap);
	}

	/**
	 * 商户信息修改
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "toUpdate", produces = "text/html;charset=UTF-8")
	public String toUpdate(HttpServletRequest request) {
		Map<String, String> paramMap = getStrictParams(request.getParameterMap());
		logger.info("融智付商户信息修改开始，入参：{}", JSON.toJSONString(paramMap));
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (null == paramMap || paramMap.isEmpty()) {
				resultMap.put("success", false);
				resultMap.put("msg", "系统调用失败!请联系管理员!");
				resultMap.put("code", "2x0003");
			} else {
				Map<String, Object> dataMap = iPaymentTransfer1To2Service.toV2RegisterUpdate(SecretUtil.publicKey, SecretUtil.privateKey, paramMap);
				resultMap.putAll(dataMap);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resultMap.put("success", false);
			resultMap.put("msg", "参数有误,支付渠道系统调用失败!请联系管理员!");
			resultMap.put("code", "2x0003");
		}
		logger.info("融智付商户信息修改结束，出参：{}", JSON.toJSONString(resultMap));
		return JSON.toJSONString(resultMap);
	}

	/**
	 * 商户绑卡
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "toBindCard", produces = "text/html;charset=UTF-8")
	public String toBindCard(HttpServletRequest request) {
		Map<String, String> paramMap = getStrictParams(request.getParameterMap());
		logger.info("融智付【商户绑卡】开始，入参：{}", JSON.toJSONString(paramMap));
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (null == paramMap || paramMap.isEmpty()) {
				resultMap.put("success", false);
				resultMap.put("msg", "系统调用失败!请联系管理员!");
				resultMap.put("code", "2x0003");
			} else {
				Map<String, Object> dataMap = iPaymentTransfer1To2Service.toV2BindCard(SecretUtil.publicKey, SecretUtil.privateKey, paramMap);
				resultMap.putAll(dataMap);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resultMap.put("success", false);
			resultMap.put("msg", "参数有误,支付渠道系统调用失败!请联系管理员!");
			resultMap.put("code", "2x0003");
		}
		logger.info("融智付【商户绑卡】结束，出参：{}", JSON.toJSONString(resultMap));
		return JSON.toJSONString(resultMap);
	}

	/**
	 * 商户解绑
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "unBindCard", produces = "text/html;charset=UTF-8")
	public String unBindCard(HttpServletRequest request) {
		Map<String, String> paramMap = getStrictParams(request.getParameterMap());
		logger.info("融智付【商户解绑】开始，入参：{}", JSON.toJSONString(paramMap));
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (null == paramMap || paramMap.isEmpty()) {
				resultMap.put("success", false);
				resultMap.put("msg", "系统调用失败!请联系管理员!");
				resultMap.put("code", "2x0003");
			} else {
				Map<String, Object> dataMap = iPaymentTransfer1To2Service.toV2UnBindCard(SecretUtil.publicKey, SecretUtil.privateKey, paramMap);
				resultMap.putAll(dataMap);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resultMap.put("success", false);
			resultMap.put("msg", "参数有误,支付渠道系统调用失败!请联系管理员!");
			resultMap.put("code", "2x0003");
		}
		logger.info("融智付【商户解绑】结束，出参：{}", JSON.toJSONString(resultMap));
		return JSON.toJSONString(resultMap);
	}

	/**
	 * 查询商户绑卡信息
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "tofindCard", produces = "text/html;charset=UTF-8")
	public String tofindCard(HttpServletRequest request) {
		Map<String, String> paramMap = getStrictParams(request.getParameterMap());
		logger.info("融智付【绑卡信息查询】开始，入参：{}", JSON.toJSONString(paramMap));
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (null == paramMap || paramMap.isEmpty()) {
				resultMap.put("success", false);
				resultMap.put("msg", "系统调用失败!请联系管理员!");
				resultMap.put("code", "2x0003");
			} else {
				Map<String, Object> dataMap = iPaymentTransfer1To2Service.toV2FindBindCard(SecretUtil.publicKey, SecretUtil.privateKey, paramMap);
				resultMap.putAll(dataMap);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resultMap.put("success", false);
			resultMap.put("msg", "参数有误,支付渠道系统调用失败!请联系管理员!");
			resultMap.put("code", "2x0003");
		}
		logger.info("融智付【绑卡信息查询】结束，出参：{}", JSON.toJSONString(resultMap));
		return JSON.toJSONString(resultMap);
	}



}