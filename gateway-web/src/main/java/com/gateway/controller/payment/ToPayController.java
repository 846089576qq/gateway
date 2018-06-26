package com.gateway.controller.payment;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.gateway.controller.gateway.base.BaseController;
import com.gateway.controller.gateway.util.SecretUtil;
import com.gateway.payment.service.transfer.IPaymentTransfer1To2Service;

@Controller
@RequestMapping("/api/controller/alwaypay/")
public class ToPayController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ToPayController.class);

	@Reference
	public IPaymentTransfer1To2Service iPaymentTransfer1To2Service;

	/**
	 * 收银台
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("topay")
	public String topay(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		try {
			response.addHeader("version", "2.0");
			Map<String, String> params = getStrictParams(request.getParameterMap());
			logger.info("进入融智付-收银台,请求参数: {}", params);
			if (params.isEmpty()) {
				map.put("success", false);
				map.put("msg", "订单数据不能为空!");
				map.put("code", "2x0003");
				return "cashier/pc";
			}
			map.putAll(iPaymentTransfer1To2Service.transferPayParamsV1ToV2(params));
			logger.info("融智付-收银台参数转换为: {}", map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			map.put("success", false);
			map.put("msg", "系统调用异常");
			map.put("code", "2x0003");
		}
		return "redirect:/cashier/pc/encryptData";
	}

	/**
	 * 非收银台
	 * 
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "topayByMc", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String topayByMc(HttpServletRequest request, HttpServletResponse response) {
		response.addHeader("version", "2.0");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, String> paramMap = getStrictParams(request.getParameterMap());
			logger.info("进入融智付-非收银台,请求参数: {}", paramMap);
			if (paramMap.isEmpty()) {
				map.put("success", false);
				map.put("msg", "订单数据不能为空!");
				map.put("code", "2x0003");
				return JSON.toJSONString(map);
			}
			map = iPaymentTransfer1To2Service.toV2Pay(SecretUtil.publicKey, SecretUtil.privateKey, getStrictParams(request.getParameterMap()));
			logger.info("融智付-非收银台返回参数: {}", map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			map.put("success", false);
			map.put("msg", "系统调用异常");
			map.put("code", "0x0004");
		}
		return JSON.toJSONString(map);
	}
	
	/**
	 * 转账
	 * 
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "transfer", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String transfer(HttpServletRequest request, HttpServletResponse response) {
		response.addHeader("version", "2.0");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, String> paramMap = getStrictParams(request.getParameterMap());
			logger.info("进入融智付-转账,请求参数: {}", paramMap);
			if (paramMap.isEmpty()) {
				map.put("success", false);
				map.put("msg", "订单数据不能为空!");
				map.put("code", "2x0003");
				return JSON.toJSONString(map);
			}
			map = iPaymentTransfer1To2Service.toV2Transfer(SecretUtil.publicKey, SecretUtil.privateKey, getStrictParams(request.getParameterMap()));
			logger.info("融智付-转账返回参数: {}", map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			map.put("success", false);
			map.put("msg", "系统调用异常");
			map.put("code", "0x0004");
		}
		return JSON.toJSONString(map);
	}
	
	/**
	 * 代付
	 * 
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "issued", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String issued(HttpServletRequest request, HttpServletResponse response) {
		response.addHeader("version", "2.0");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, String> paramMap = getStrictParams(request.getParameterMap());
			logger.info("进入融智付-代付,请求参数: {}", paramMap);
			if (paramMap.isEmpty()) {
				map.put("success", false);
				map.put("msg", "代付订单数据不能为空!");
				map.put("code", "2x0003");
				return JSON.toJSONString(map);
			}
			map = iPaymentTransfer1To2Service.toV2Issued(SecretUtil.publicKey, SecretUtil.privateKey, getStrictParams(request.getParameterMap()));
			logger.info("融智付-代付返回参数: {}", map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			map.put("success", false);
			map.put("msg", "系统调用异常");
			map.put("code", "0x0004");
		}
		return JSON.toJSONString(map);
	}

	/**
	 * 订单查询接口
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("findOrder")
	@ResponseBody
	public String findOrder(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, String> params = getStrictParams(request.getParameterMap());
			logger.info("进入融智付-订单查询,请求参数: {}", params);
			if (params.isEmpty()) {
				map.put("success", false);
				map.put("msg", "订单查询参数不能为空!");
				map.put("code", "2x0003");
				return JSON.toJSONString(map);
			}
			map = iPaymentTransfer1To2Service.toV2Query(SecretUtil.publicKey, SecretUtil.privateKey, params);
			logger.info("融智付-查询返回参数: {}", map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			map.put("success", false);
			map.put("msg", "系统调用异常");
			map.put("code", "2x0003");
		}
		return JSON.toJSONString(map);
	}
	
	/**
	 * 退款
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("refund")
	@ResponseBody
	public String refund(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, String> params = getStrictParams(request.getParameterMap());
			logger.info("进入融智付-退款,请求参数: {}", params);
			if (params.isEmpty()) {
				map.put("success", false);
				map.put("msg", "退款参数不能为空!");
				map.put("code", "2x0003");
				return JSON.toJSONString(map);
			}
			map = iPaymentTransfer1To2Service.toV2Refund(SecretUtil.publicKey, SecretUtil.privateKey, params);
			logger.info("融智付-退款返回参数: {}", map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			map.put("success", false);
			map.put("msg", "系统调用异常");
			map.put("code", "2x0003");
		}
		return JSON.toJSONString(map);
	}
	
}