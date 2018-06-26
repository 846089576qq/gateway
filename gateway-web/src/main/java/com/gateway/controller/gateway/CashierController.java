package com.gateway.controller.gateway;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gateway.common.constants.CommonConstant;
import com.gateway.common.constants.ForwardConstant;
import com.gateway.common.constants.param.PaymentMethodConstant;
import com.gateway.common.constants.param.PaymentParamConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.controller.gateway.base.BaseController;
import com.gateway.controller.gateway.util.SecretUtil;
import com.gateway.payment.entity.OrderEntity;
import com.gateway.payment.entity.PersonApplicationEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.service.adapter.ICashierServiceAdapter;
import com.gateway.payment.service.adapter.IQueryServiceAdapter;
import com.zitopay.datagram.request.RequestDatagram;
import com.zitopay.foundation.common.exception.ServiceException;
import com.zitopay.foundation.common.util.BeanUtils;

/**
 * 收银台
 * 
 * 作者：王政 创建时间：2017年3月6日 下午5:55:19
 */
@Controller
@RequestMapping("/cashier")
public class CashierController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(CashierController.class);

	@Reference
	private ICashierServiceAdapter cashierServiceAdapter;

	@Reference
	private IQueryServiceAdapter queryServiceAdapter;

	/**
	 * 收银台接口
	 * 
	 * @param request
	 * @param modelMap
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/{type}", method = { RequestMethod.POST })
	public String desk(HttpServletRequest request, ModelMap modelMap, @PathVariable String type, @RequestParam String encryptData) {
		RequestDatagram rd = new RequestDatagram();
		try {
			/**
			 * 1.报文验证
			 */
			Map<String, String> paramMap = null;
			try {
				rd.decrypt(encryptData, SecretUtil.privateKey);
				paramMap = (Map<String, String>) rd.getData();
				/* modelMap.put("paramMap", paramMap); */
				modelMap.put("encryptData", encryptData);
				logger.info("融智付-json接收参数,报文序列号:{},参数内容：{}", rd.getSerialNo(), paramMap);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				modelMap.putAll(ResponseInfoEnum.报文异常.getMap());
				return "gateway/cashier/" + type;
			}
			logger.info("融智付-json,支付请求报文:{}", rd);
			/**
			 * 2.参数验证
			 */
			Map<String, Object> checkMap = cashierServiceAdapter.checkCashierParameter(paramMap, rd.getSerialNo());
			logger.info("融智付-json参数验证,报文序列号:{},验证结果：{}", rd.getSerialNo(), checkMap);
			if (!ResponseInfoEnum.调用成功.equals(checkMap)) {
				modelMap.putAll(checkMap);
				return "gateway/cashier/" + type;
			}
			/**
			 * 3.权限验证
			 */
			String merchantId = paramMap.get(PaymentParamConstant.param_merchantId);
			String applicationId = paramMap.get(PaymentParamConstant.param_appId);
			Map<String, Object> permissionMap = cashierServiceAdapter.checkCashierPermission(merchantId, applicationId, null);
			logger.info("融智付-json权限验证,报文序列号:{},验证结果：{}", rd.getSerialNo(), permissionMap);
			if (!ResponseInfoEnum.调用成功.equals(permissionMap)) {// 判断权限验证结果
				modelMap.putAll(permissionMap);
				return "gateway/cashier/" + type;
			}
			/**
			 * 4.去调用
			 */
			PersonEntity zitopayPerson = (PersonEntity) permissionMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON);
			PersonApplicationEntity zitopayPersonApplication = (PersonApplicationEntity) permissionMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON_APPLICATION);

			Map<String, Object> paymentResultMap = cashierServiceAdapter.execCashier(zitopayPerson, zitopayPersonApplication, paramMap, type, rd.getSerialNo());
			logger.warn("融智付-json收银台调用,报文序列号:{},调用结果:{}", rd.getSerialNo(), paymentResultMap);
			modelMap.putAll(paymentResultMap);
			return "gateway/cashier/" + type;
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			modelMap.putAll(ResponseInfoEnum.服务处理异常.getMap(e.getMessage()));
			return "gateway/cashier/" + type;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			modelMap.putAll(ResponseInfoEnum.系统异常.getMap(e.getMessage()));
			return "gateway/cashier/" + type;
		}
	}

	/**
	 * 发起通道调用
	 * 
	 * @param request
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/channel/{type}", method = { RequestMethod.POST,RequestMethod.GET })
	public String channel(HttpServletRequest request, HttpEntity<String> entity, ModelMap modelMap, @PathVariable String type, @RequestParam(required = false) String encryptData) {
		RequestDatagram rd = new RequestDatagram();
		try {
			/**
			 * 解析请求参数并添加通道公用参数
			 */
			Map<String, String> params = null;
			if("POST".equals(request.getMethod())) {
				rd.decrypt(URLDecoder.decode(encryptData, "UTF-8"), SecretUtil.privateKey);
			}else {
				rd.decrypt(encryptData, SecretUtil.privateKey);
			}
			params = (Map<String, String>) rd.getData();
			params.put(PaymentParamConstant.param_gatewayId, request.getParameter("gateway_id"));
			params.put(PaymentParamConstant.param_is_zitopay_cashier, "1");
			params.put(PaymentParamConstant.param_card_type, request.getParameter("card_type"));
			params.put(PaymentParamConstant.param_bank_code, request.getParameter("bank_code"));
			/**
			 * 微信扫码、支付宝扫码、融智付网关通道需要增加参数
			 */
			if (ForwardConstant.CASHIER_URL_TYPE_WXQRCODE.equals(type) || ForwardConstant.CASHIER_URL_TYPE_ZFBQRCODE.equals(type) || ForwardConstant.CASHIER_URL_TYPE_QQQRCODE.equals(type) || ForwardConstant.CASHIER_URL_TYPE_JDQRCODE.equals(type) || ForwardConstant.CASHIER_URL_TYPE_BDQRCODE.equals(type) || ForwardConstant.CASHIER_URL_TYPE_YLQRCODE.equals(type) || ForwardConstant.CASHIER_URL_TYPE_WG.equals(type) || ForwardConstant.CASHIER_URL_TYPE_WG_NOICON.equals(type) || ForwardConstant.CASHIER_URL_TYPE_WXJSAPI.equals(type) || ForwardConstant.CASHIER_URL_TYPE_ZFBFWC.equals(type)) {
				Map<String, Object> paramsdataMap = cashierServiceAdapter.findOrder(params);
				OrderEntity orderEntity = (OrderEntity) paramsdataMap.get("zitopayOrder");
				modelMap.put("order", BeanUtils.bean2Map(orderEntity));
				if(ForwardConstant.CASHIER_URL_TYPE_WG.equals(type) || ForwardConstant.CASHIER_URL_TYPE_WG_NOICON.equals(type)) {
					params.put(PaymentParamConstant.param_oper_type, PaymentMethodConstant.param_operate_banklist);
					params.put(PaymentParamConstant.param_order_id, orderEntity.getOrderid());
					if(ForwardConstant.CASHIER_URL_TYPE_WG_NOICON.equals(type)) {
						type = ForwardConstant.CASHIER_URL_TYPE_WG;
						modelMap.put(PaymentParamConstant.param_is_show_zitopay_icon, "0");
					}else {
						modelMap.put(PaymentParamConstant.param_is_show_zitopay_icon, "1");
					}
				}
			}
			/**
			 * 微信公众号通道需要增加参数
			 */
			if (ForwardConstant.CASHIER_URL_TYPE_WXJSAPI.equals(type) || ForwardConstant.CASHIER_URL_TYPE_ZFBFWC.equals(type)) {
				Map<String, Object> paramsdataMap = cashierServiceAdapter.findOrder(params);
				OrderEntity orderEntity = (OrderEntity) paramsdataMap.get(CommonConstant.CASHIER_ZITOPA_ORDER);
				modelMap.put(PaymentParamConstant.param_orderTitle, orderEntity.getOrdertitle());
				modelMap.put(PaymentParamConstant.param_orderAmount, orderEntity.getTotalprice());
				modelMap.put(PaymentParamConstant.param_merchantUrl, orderEntity.getReturnurl());
				Map<String, Object> merchantMap = queryServiceAdapter.getMerchant(params.get(PaymentParamConstant.param_merchantId), rd.getSerialNo());
				PersonEntity personEntity = (PersonEntity) merchantMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON);
				modelMap.put("personName", personEntity.getUsername());
			}
			
			/**
			 * 对增加参数RSA加密
			 */
			RequestDatagram installDatagram = new RequestDatagram().initDatagram();
			installDatagram.setData(params);
//			if(StringUtils.isNotBlank(request.getParameter("code"))){
//				String openId = wechatOAuthUtil.getOpendid(request.getParameter("code"));
//				params.put(PaymentParamConstant.param_openId, openId);
//				modelMap.put(PaymentParamConstant.param_openId,openId);
//			}
			String installData = URLEncoder.encode(installDatagram.toEncryptString(SecretUtil.publicKey), "UTF-8");
			modelMap.put("encryptData", installData);

			return "gateway/channel/" + type;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			modelMap.putAll(ResponseInfoEnum.系统异常.getMap(e.getMessage()));
			return "gateway/channel/" + type;
		}
	}

	@RequestMapping(value = "/payresult/{orderid}", produces = "text/html;charset=UTF-8")
	public String payResult(HttpServletRequest request, ModelMap modelMap, @PathVariable("orderid") String orderid) {
		try {
			Map<String, Object> payResultMap = cashierServiceAdapter.payResult(orderid);
			modelMap.putAll(payResultMap);
			if (ResponseInfoEnum.调用成功.equals(payResultMap)) {
				return "gateway/payresult/success";// 跳转到成功页面
			} else {
				return "gateway/payresult/fail";// 失败页面
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			modelMap.putAll(ResponseInfoEnum.系统异常.getMap(e.getMessage()));
			return "gateway/payresult/fail";// 失败页面
		}
	}

}