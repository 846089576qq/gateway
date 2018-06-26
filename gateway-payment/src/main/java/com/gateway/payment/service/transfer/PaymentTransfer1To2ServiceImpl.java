package com.gateway.payment.service.transfer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gateway.common.constants.CommonConstant;
import com.gateway.common.constants.param.BaseParamConstant;
import com.gateway.common.constants.param.IssuedParamConstant;
import com.gateway.common.constants.param.IssuedResultParamConstant;
import com.gateway.common.constants.param.PaymentBindCardParamConstant;
import com.gateway.common.constants.param.PaymentBindCardResulltaramConstant;
import com.gateway.common.constants.param.PaymentMethodConstant;
import com.gateway.common.constants.param.PaymentParamConstant;
import com.gateway.common.constants.param.PaymentResultParamConstant;
import com.gateway.common.constants.param.QueryParamConstant;
import com.gateway.common.constants.param.RefundParamConstant;
import com.gateway.common.constants.param.TransferParamConstant;
import com.gateway.common.constants.param.TransferResultParamConstant;
import com.gateway.common.constants.param.ZitopayMerchantRegisterParamConstant;
import com.gateway.common.constants.param.ZitopayMerchantRegisterResultParamConstant;
import com.gateway.common.constants.param.ZitopayMerchantRegisterUpdateParamConstant;
import com.gateway.common.constants.status.DisabledStatusConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.entity.OrderEntity;
import com.gateway.payment.entity.PersonApplicationEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.TransferEntity;
import com.gateway.payment.service.base.impl.BaseServiceImpl;
import com.zitopay.datagram.request.RequestDatagram;
import com.zitopay.datagram.response.ResponseDatagram;
import com.zitopay.foundation.common.util.BeanUtils;

@Service
@com.alibaba.dubbo.config.annotation.Service
public class PaymentTransfer1To2ServiceImpl extends BaseServiceImpl implements IPaymentTransfer1To2Service {

	public final static String ZITOPAY_SIGN_HEAD = "payreturn";// 回调头信息
	
	public final static String ZITOPAY_TRANSFER__SIGN_HEAD = "transferreturn";

	public final static String ZITOPAY_MERCHANT_SIGN_HEAD = "merchantregister";

	@Override
	public Map<String, Object> toV2Pay(String publicKey, String privateKey, Map<String, String> params) {
		try {
			Map<String, Object> verifyMap = verifySign(false, params);
			if (!(boolean) verifyMap.get("success")) {
				return verifyMap;
			}
			RequestDatagram rd = new RequestDatagram().initDatagram();
			rd.setData(transferPayParamsV1ToV2(params));
			String encryptData = rd.toEncryptString(publicKey);
			HttpEntity<String> httpEntity = new HttpEntity<String>(URLEncoder.encode(encryptData, "UTF-8"));
			ResponseDatagram rsd = restTemplate.postForObject(envProperties.PREFIX_GATEWAY_URL + "/payment/json", httpEntity, ResponseDatagram.class);
			rsd.toDecryptString(privateKey);
			@SuppressWarnings("unchecked")
			Map<String, String> dataMap = (Map<String, String>) rsd.getData();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (ResponseInfoEnum.调用成功.equals(BeanUtils.bean2Map(rsd))) {
				resultMap.put("success", true);
				String url = dataMap.get(PaymentResultParamConstant.param_url);
				if (StringUtils.isNotBlank(url)) {
					if (url.startsWith("https://qr.alipay.com/") || url.startsWith("weixin://")) {
						resultMap.put("codeurl", dataMap.get(PaymentResultParamConstant.param_url));
					} else {
						resultMap.put("cashway", dataMap.get(PaymentResultParamConstant.param_url));
					}
				}
				String wxjsapiStr = dataMap.get(PaymentResultParamConstant.param_pay_data_wechat);
				if (StringUtils.isNotBlank(wxjsapiStr)) {
					resultMap.put("wxjsapiStr", wxjsapiStr);
				}
				String alipayChannelNo = dataMap.get(PaymentResultParamConstant.param_pay_data_alipay);
				if (StringUtils.isNotBlank(alipayChannelNo)) {
					resultMap.put("alipayChannelNo", alipayChannelNo);
				}
				String orderid = dataMap.get(PaymentResultParamConstant.param_orderid);
				if (StringUtils.isNotBlank(orderid)) {
					resultMap.putAll(pubVerifyToSignWithPay(dataMap.get(PaymentResultParamConstant.param_orderid)));
				}

			} else {
				if (ResponseInfoEnum.等待用户输入密码.equals(BeanUtils.bean2Map(rsd))) {
					resultMap.put("orderid", dataMap.get(PaymentResultParamConstant.param_orderid));// 订单号
				}
				resultMap.put("success", false);
			}
			resultMap.put("code", rsd.getCode());
			resultMap.put("msg", rsd.getMsg());
			return resultMap;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public Map<String, Object> toV2Query(String publicKey, String privateKey, Map<String, String> params) {
		try {
			Map<String, Object> verifyMap = verifySign(true, params);
			if (!(boolean) verifyMap.get("success")) {
				return verifyMap;
			}
			RequestDatagram rd = new RequestDatagram().initDatagram();
			rd.setData(transferQueryParamsV1ToV2(params));
			String encryptData = rd.toEncryptString(publicKey);
			HttpEntity<String> httpEntity = new HttpEntity<String>(URLEncoder.encode(encryptData, "UTF-8"));
			ResponseDatagram rsd = restTemplate.postForObject(envProperties.PREFIX_GATEWAY_URL + "/query/json", httpEntity, ResponseDatagram.class);
			rsd.toDecryptString(privateKey);
			@SuppressWarnings("unchecked")
			Map<String, Object> dataMap = (Map<String, Object>) rsd.getData();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (ResponseInfoEnum.调用成功.equals(BeanUtils.bean2Map(rsd))) {
				resultMap.put("success", true);
				resultMap.putAll(transferQueryResult(dataMap));

			} else {
				resultMap.put("success", false);
			}
			resultMap.put("code", rsd.getCode());
			resultMap.put("msg", rsd.getMsg());
			return resultMap;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public Map<String, Object> toV2Refund(String publicKey, String privateKey, Map<String, String> params) {
		try {
			Map<String, Object> verifyMap = verifySign(false, params);
			if (!(boolean) verifyMap.get("success")) {
				return verifyMap;
			}
			RequestDatagram rd = new RequestDatagram().initDatagram();
			rd.setData(transferRefundParamsV1ToV2(params));
			String encryptData = rd.toEncryptString(publicKey);
			HttpEntity<String> httpEntity = new HttpEntity<String>(URLEncoder.encode(encryptData, "UTF-8"));
			ResponseDatagram rsd = restTemplate.postForObject(envProperties.PREFIX_GATEWAY_URL + "/refund/json", httpEntity, ResponseDatagram.class);
			rsd.toDecryptString(privateKey);
			@SuppressWarnings("unchecked")
			Map<String, String> dataMap = (Map<String, String>) rsd.getData();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (ResponseInfoEnum.退款成功.equals(BeanUtils.bean2Map(rsd))) {
				resultMap.put("success", true);
				String orderid = dataMap.get(PaymentResultParamConstant.param_orderid);
				if (StringUtils.isNotBlank(orderid)) {
					resultMap.putAll(pubVerifyToSignWithPay(dataMap.get(PaymentResultParamConstant.param_orderid)));
				}

			} else {
				resultMap.put("success", false);
			}
			resultMap.put("code", rsd.getCode());
			resultMap.put("msg", rsd.getMsg());
			return resultMap;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public Map<String, Object> toV2Register(String publicKey, String privateKey, Map<String, String> params) {
		try {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			String account = params.get("account");
			String bankNo = params.get("bankNo");
			String phone = params.get("phone");
			String idCard = params.get("idCard");
			String password = params.get("password");
			String sign = params.get("sign");
			if (StringUtils.isBlank(sign)) {
				resultMap.put("success", false);
				resultMap.put("msg", "签名[sign]不能为空");
				return resultMap;
			}

			if (!verifySign(ZITOPAY_MERCHANT_SIGN_HEAD + account, bankNo, phone, idCard, sign, password)) {
				resultMap.put("success", false);
				resultMap.put("msg", "验证签名失败!");
				return resultMap;
			}

			RequestDatagram rd = new RequestDatagram().initDatagram();
			rd.setData(transferRegisterParamsV1ToV2(params));
			String encryptData = rd.toEncryptString(publicKey);
			HttpEntity<String> httpEntity = new HttpEntity<String>(URLEncoder.encode(encryptData, "UTF-8"));
			ResponseDatagram rsd = restTemplate.postForObject(envProperties.PREFIX_GATEWAY_URL + "/merchant/register/json", httpEntity, ResponseDatagram.class);
			rsd.toDecryptString(privateKey);
			@SuppressWarnings("unchecked")
			Map<String, Object> dataMap = (Map<String, Object>) rsd.getData();
			if (ResponseInfoEnum.调用成功.equals(BeanUtils.bean2Map(rsd))) {
				resultMap.put("success", true);
				resultMap.put("pid", dataMap.get(ZitopayMerchantRegisterResultParamConstant.param_merchantId));
				resultMap.put("appId", dataMap.get(ZitopayMerchantRegisterResultParamConstant.param_appId));
				resultMap.put("appKey", dataMap.get(ZitopayMerchantRegisterResultParamConstant.param_appKey));

			} else {
				resultMap.put("success", false);
			}
			resultMap.put("code", rsd.getCode());
			resultMap.put("msg", rsd.getMsg());
			return resultMap;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public Map<String, Object> toV2RegisterUpdate(String publicKey, String privateKey, Map<String, String> params) {
		try {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			Map<String, Object> verifyMap = verifySign(true, params);
			if (!(boolean) verifyMap.get("success")) {
				return verifyMap;
			}

			RequestDatagram rd = new RequestDatagram().initDatagram();
			rd.setData(transferRegisterUpdateParamsV1ToV2(params));
			String encryptData = rd.toEncryptString(publicKey);
			HttpEntity<String> httpEntity = new HttpEntity<String>(URLEncoder.encode(encryptData, "UTF-8"));
			ResponseDatagram rsd = restTemplate.postForObject(envProperties.PREFIX_GATEWAY_URL + "/merchant/update/json", httpEntity, ResponseDatagram.class);
			rsd.toDecryptString(privateKey);
			if (ResponseInfoEnum.调用成功.equals(BeanUtils.bean2Map(rsd))) {
				resultMap.put("success", true);

			} else {
				resultMap.put("success", false);
			}
			resultMap.put("code", rsd.getCode());
			resultMap.put("msg", rsd.getMsg());
			return resultMap;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public Map<String, Object> toV2BindCard(String publicKey, String privateKey, Map<String, String> params) {
		try {
			Map<String, Object> resultMap = verifyBindCardSign(params);
			if (!ResponseInfoEnum.调用成功.equals(resultMap)) {
				return resultMap;
			}

			RequestDatagram rd = new RequestDatagram().initDatagram();
			rd.setData(transferBindCardParamsV1ToV2(params));
			String encryptData = rd.toEncryptString(publicKey);
			HttpEntity<String> httpEntity = new HttpEntity<String>(URLEncoder.encode(encryptData, "UTF-8"));
			ResponseDatagram rsd = restTemplate.postForObject(envProperties.PREFIX_GATEWAY_URL + "/payment/json", httpEntity, ResponseDatagram.class);
			rsd.toDecryptString(privateKey);
			@SuppressWarnings("unchecked")
			Map<String, Object> dataMap = (Map<String, Object>) rsd.getData();
			if (ResponseInfoEnum.调用成功.equals(BeanUtils.bean2Map(rsd))) {
				resultMap.put("success", true);
				resultMap.put("agreementId", dataMap.get(PaymentBindCardResulltaramConstant.param_agreementId));
				resultMap.put("id", dataMap.get(PaymentBindCardResulltaramConstant.param_merchantId));
				resultMap.put("mer_cust_id", dataMap.get(PaymentBindCardResulltaramConstant.param_merchantCustomerId));

			} else {
				resultMap.put("success", false);
			}
			resultMap.put("code", rsd.getCode());
			resultMap.put("msg", rsd.getMsg());
			return resultMap;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	private Map<String, Object> verifyBindCardSign(Map<String, String> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String id = params.get("id");
		String appid = params.get("appid");
		String cust_card_no = params.get("cust_card_no");
		String cust_identity_code = params.get("cust_identity_code");
		String sign = params.get("sign");
		if (StringUtils.isBlank(sign)) {
			resultMap.put("success", false);
			resultMap.put("msg", "签名[sign]不能为空");
			return resultMap;
		}

		resultMap = checkPermission(id, appid);
		if (!ResponseInfoEnum.调用成功.equals(resultMap)) {
			return resultMap;
		}
		PersonApplicationEntity personApplication = (PersonApplicationEntity) resultMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON_APPLICATION);

		if (!verifySign(id, appid, cust_card_no, cust_identity_code, personApplication.getApplicationKey())) {
			resultMap.put("success", false);
			resultMap.put("msg", "验证签名失败!");
			return resultMap;
		}
		return ResponseInfoEnum.调用成功.getMap();
	}

	@Override
	public Map<String, Object> toV2UnBindCard(String publicKey, String privateKey, Map<String, String> params) {
		try {
			Map<String, Object> resultMap = verifyBindCardSign(params);
			if (!ResponseInfoEnum.调用成功.equals(resultMap)) {
				return resultMap;
			}

			RequestDatagram rd = new RequestDatagram().initDatagram();
			rd.setData(transferUnBindCardParamsV1ToV2(params));
			String encryptData = rd.toEncryptString(publicKey);
			HttpEntity<String> httpEntity = new HttpEntity<String>(URLEncoder.encode(encryptData, "UTF-8"));
			ResponseDatagram rsd = restTemplate.postForObject(envProperties.PREFIX_GATEWAY_URL + "/payment/json", httpEntity, ResponseDatagram.class);
			rsd.toDecryptString(privateKey);
			return BeanUtils.bean2Map(rsd);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public Map<String, Object> toV2FindBindCard(String publicKey, String privateKey, Map<String, String> params) {
		try {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			String id = params.get("id");
			String appid = params.get("appid");
			String mer_cust_id = params.get("mer_cust_id");
			String sign = params.get("sign");
			if (StringUtils.isBlank(sign)) {
				resultMap.put("success", false);
				resultMap.put("msg", "签名[sign]不能为空");
				return resultMap;
			}

			resultMap = checkPermission(id, appid);
			if (!ResponseInfoEnum.调用成功.equals(resultMap)) {
				return resultMap;
			}
			PersonApplicationEntity personApplication = (PersonApplicationEntity) resultMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON_APPLICATION);

			if (!verifySign(id, appid, mer_cust_id, "", personApplication.getApplicationKey())) {
				resultMap.put("success", false);
				resultMap.put("msg", "验证签名失败!");
				return resultMap;
			}

			RequestDatagram rd = new RequestDatagram().initDatagram();
			rd.setData(transferFindBindCardParamsV1ToV2(params));
			String encryptData = rd.toEncryptString(publicKey);
			HttpEntity<String> httpEntity = new HttpEntity<String>(URLEncoder.encode(encryptData, "UTF-8"));
			ResponseDatagram rsd = restTemplate.postForObject(envProperties.PREFIX_GATEWAY_URL + "/payment/json", httpEntity, ResponseDatagram.class);
			rsd.toDecryptString(privateKey);
			return BeanUtils.bean2Map(rsd);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	private Map<String, Object> verifySign(boolean isQuery, Map<String, String> params) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String id = params.get("id");// 商户id，由融智付分配
		String appid = params.get("appid");// 商户应用appid，由融智付分配
		String totalPrice = params.get("totalPrice");// 商品总价格，以元为单位，小数点后2位
		String orderidinf = params.get("orderidinf");// 商户传入订单号
		String sign = params.get("sign");
		if (StringUtils.isBlank(sign)) {
			returnMap.put("success", false);
			returnMap.put("msg", "签名[sign]不能为空");
			return returnMap;
		}
		returnMap = checkPermission(id, appid);
		if (!ResponseInfoEnum.调用成功.equals(returnMap)) {
			returnMap.put("success", false);
			return returnMap;
		}
		PersonApplicationEntity personApplication = (PersonApplicationEntity) returnMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON_APPLICATION);
		if (isQuery) {
			orderidinf = "";
			totalPrice = "";
		}
		boolean vSign = verifySign(id, appid, orderidinf, totalPrice, sign, personApplication.getApplicationKey());// 商户表授权id+用户名+支付金额+授权key
		if (!vSign) {
			returnMap.put("success", false);
			returnMap.put("msg", "验证签名失败!");
			return returnMap;
		}
		returnMap.put("success", true);
		returnMap.put("msg", "ok");
		return returnMap;
	}

	private Map<String, Object> checkPermission(String id, String appid) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 验证商户是否存在以及启用
		PersonEntity zitopayPerson = personService.getZitopayPersonByPid(id);
		if (null == zitopayPerson) {
			return ResponseInfoEnum.商户不存在.getMap();
		}
		if (DisabledStatusConstant.STATUS_DISABLED.equals(zitopayPerson.getStatus())) {
			return ResponseInfoEnum.商户已禁用.getMap();
		}

		PersonApplicationEntity personApplication = personApplicationService.getZitopayPersonApplicationByAppid(zitopayPerson.getId(), appid);
		if (personApplication == null) {
			returnMap.put("success", false);
			returnMap.put("msg", "该商户未开通支付应用，请联系管理员!");
			return returnMap;
		}
		returnMap.put(CommonConstant.ZITOPAY_ENTITY_PERSON_APPLICATION, personApplication);
		return ResponseInfoEnum.调用成功.getMap(returnMap);
	}

	@Override
	public Map<String, String> transferPayParamsV1ToV2(Map<String, String> params) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put(PaymentParamConstant.param_gatewayId, params.get("gid"));
		transferCommonParamsV1ToV2(params, resultMap);
		resultMap.put(PaymentParamConstant.param_oper_type, PaymentMethodConstant.param_operate_pay);
		resultMap.put(PaymentParamConstant.param_orderTime, params.get("posttime"));
		resultMap.put(PaymentParamConstant.param_orderAmount, params.get("totalPrice"));
		resultMap.put(PaymentParamConstant.param_merchantOrderId, params.get("orderidinf"));
		String bgReturl = params.get("bgRetUrl");
		if ("www.pos.com".equals(bgReturl)) {
			bgReturl = "terminal-client";
		}
		resultMap.put(PaymentParamConstant.param_merchantNotifyUrl, bgReturl);
		resultMap.put(PaymentParamConstant.param_merchantUrl, params.get("returnUrl"));
		resultMap.put(PaymentParamConstant.param_orderTitle, params.get("ordertitle"));
		String barCode = params.get("authCode");
		if (StringUtils.isBlank(barCode)) {
			barCode = params.get("auth_code");
		}
		resultMap.put(PaymentParamConstant.param_bar_code, barCode);
		resultMap.put(PaymentParamConstant.param_terminal_no, params.get("terminalNo"));
		resultMap.put(PaymentParamConstant.param_shop_no, params.get("shopNo"));
		resultMap.put(PaymentParamConstant.param_cashier_no, params.get("cashierNo"));
		resultMap.put(PaymentParamConstant.param_openId, params.get("openid"));
		return resultMap;
	}

	@Override
	public Map<String, String> transferQueryParamsV1ToV2(Map<String, String> params) {
		Map<String, String> resultMap = new HashMap<String, String>();
		transferCommonParamsV1ToV2(params, resultMap);
		resultMap.put(QueryParamConstant.param_merchantOrderId, params.get("orderidinf"));
		resultMap.put(QueryParamConstant.param_order_id, params.get("orderId"));
		return resultMap;
	}
	
	@Override
	public Map<String, String> transferTransferParamsV1ToV2(Map<String, String> params) {
		Map<String, String> resultMap = new HashMap<String, String>();
		transferCommonParamsV1ToV2(params, resultMap);
		resultMap.put(TransferParamConstant.param_gatewayId, params.get("gid"));
		resultMap.put(TransferParamConstant.param_merchantTransferOrderId, params.get("orderidinf"));
		resultMap.put(TransferParamConstant.param_orderAmount, params.get("totalPrice"));
		resultMap.put(TransferParamConstant.param_orderTitle, params.get("ordertitle"));
		return resultMap;
	}
	

	@Override
	public Map<String, String> transferIssuedParamsV1ToV2(Map<String, String> params) {
		Map<String, String> resultMap = new HashMap<String, String>();
		transferCommonParamsV1ToV2(params, resultMap);
		resultMap.put(IssuedParamConstant.param_gatewayId, params.get("gid"));
		resultMap.put(IssuedParamConstant.param_merchantOrderId, params.get("orderidinf"));
		resultMap.put(IssuedParamConstant.param_orderAmount, params.get("totalPrice"));
		resultMap.put(IssuedParamConstant.param_bankName, params.get("bankName"));
		resultMap.put(IssuedParamConstant.param_branchBankName, params.get("branchBankName"));
		resultMap.put(IssuedParamConstant.param_custCardNo, params.get("bankNo"));
		resultMap.put(IssuedParamConstant.param_custCardMobile, params.get("phone"));
		resultMap.put(IssuedParamConstant.param_custIdentityCode, params.get("idCard"));
		resultMap.put(IssuedParamConstant.param_custRealName, params.get("userName"));
		resultMap.put(IssuedParamConstant.param_bankProvince, params.get("bankAddrProvince"));
		resultMap.put(IssuedParamConstant.param_bankCity, params.get("bankAddrCity"));
		return resultMap;
	}

	public Map<String, String> transferRegisterParamsV1ToV2(Map<String, String> params) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put(ZitopayMerchantRegisterParamConstant.param_account, params.get("account"));
		resultMap.put(ZitopayMerchantRegisterParamConstant.param_password, params.get("password"));
		resultMap.put(ZitopayMerchantRegisterParamConstant.param_gatewayId, params.get("gatewayId"));
		resultMap.put(ZitopayMerchantRegisterParamConstant.param_merName, params.get("merchantName"));
		resultMap.put(ZitopayMerchantRegisterParamConstant.param_merShortName, params.get("merchantShortName"));
		resultMap.put(ZitopayMerchantRegisterParamConstant.param_merAddress, params.get("address"));
		resultMap.put(ZitopayMerchantRegisterParamConstant.param_merContact, params.get("phone"));
		resultMap.put(ZitopayMerchantRegisterParamConstant.param_merEmail, params.get("email"));
		resultMap.put(ZitopayMerchantRegisterParamConstant.param_bankName, params.get("bankName"));
		resultMap.put(ZitopayMerchantRegisterParamConstant.param_bankNo, params.get("bankCode"));
		resultMap.put(ZitopayMerchantRegisterParamConstant.param_bankProvince, params.get("bankAddrProvince"));
		resultMap.put(ZitopayMerchantRegisterParamConstant.param_bankCity, params.get("bankAddrCity"));
		resultMap.put(ZitopayMerchantRegisterParamConstant.param_accType, params.get("accType"));
		resultMap.put(ZitopayMerchantRegisterParamConstant.param_cardNo, params.get("bankNo"));
		resultMap.put(ZitopayMerchantRegisterParamConstant.param_cardMobile, params.get("phone"));
		resultMap.put(ZitopayMerchantRegisterParamConstant.param_cardIdentityCode, params.get("idCard"));
		resultMap.put(ZitopayMerchantRegisterParamConstant.param_cardRealName, params.get("userName"));
		resultMap.put(ZitopayMerchantRegisterParamConstant.param_fixFee, params.get("extraRate"));
		resultMap.put(ZitopayMerchantRegisterParamConstant.param_settleRate, params.get("settleRate"));
		return resultMap;
	}

	public Map<String, String> transferRegisterUpdateParamsV1ToV2(Map<String, String> params) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put(ZitopayMerchantRegisterUpdateParamConstant.param_merchantId, params.get("pid"));
		resultMap.put(ZitopayMerchantRegisterUpdateParamConstant.param_appId, params.get("appId"));
		resultMap.put(ZitopayMerchantRegisterUpdateParamConstant.param_gatewayId, params.get("gatewayId"));
		resultMap.put(ZitopayMerchantRegisterUpdateParamConstant.param_bankName, params.get("bankName"));
		resultMap.put(ZitopayMerchantRegisterUpdateParamConstant.param_bankNo, params.get("bankCode"));
		resultMap.put(ZitopayMerchantRegisterUpdateParamConstant.param_bankProvince, params.get("bankAddrProvince"));
		resultMap.put(ZitopayMerchantRegisterUpdateParamConstant.param_bankCity, params.get("bankAddrCity"));
		resultMap.put(ZitopayMerchantRegisterUpdateParamConstant.param_accType, params.get("accType"));
		resultMap.put(ZitopayMerchantRegisterUpdateParamConstant.param_cardNo, params.get("bankNo"));
		resultMap.put(ZitopayMerchantRegisterUpdateParamConstant.param_cardMobile, params.get("phone"));
		resultMap.put(ZitopayMerchantRegisterUpdateParamConstant.param_cardIdentityCode, params.get("idCard"));
		resultMap.put(ZitopayMerchantRegisterUpdateParamConstant.param_cardRealName, params.get("userName"));
		resultMap.put(ZitopayMerchantRegisterUpdateParamConstant.param_fixFee, params.get("extraRate"));
		resultMap.put(ZitopayMerchantRegisterUpdateParamConstant.param_settleRate, params.get("settleRate"));
		return resultMap;
	}

	public Map<String, String> transferBindCardParamsV1ToV2(Map<String, String> params) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put(PaymentBindCardParamConstant.param_merchantId, params.get("id"));
		resultMap.put(PaymentBindCardParamConstant.param_appId, params.get("appid"));
		resultMap.put(PaymentBindCardParamConstant.param_gatewayId, params.get("gatewayId"));
		resultMap.put(PaymentBindCardParamConstant.param_merchantCustomerId, params.get("mer_cust_id"));
		resultMap.put(PaymentBindCardParamConstant.param_customerCardType, params.get("cust_card_type"));
		resultMap.put(PaymentBindCardParamConstant.param_bankCardNo, params.get("cust_card_no"));
		resultMap.put(PaymentBindCardParamConstant.param_bankCode, params.get("cust_bank_code"));
		resultMap.put(PaymentBindCardParamConstant.param_customerContact, params.get("cust_bank_code"));
		resultMap.put(PaymentBindCardParamConstant.param_customerIdentityCode, params.get("cust_identity_code"));
		resultMap.put(PaymentBindCardParamConstant.param_customerRealName, params.get("cust_name"));
		resultMap.put(PaymentBindCardParamConstant.param_bankCardValidDate, params.get("credit_valid_date"));
		resultMap.put(PaymentBindCardParamConstant.param_bankCardCVV2, params.get("cust_credit_cvv2"));
		resultMap.put(PaymentParamConstant.param_oper_type, PaymentMethodConstant.param_operate_bindcard);
		return resultMap;
	}

	public Map<String, String> transferUnBindCardParamsV1ToV2(Map<String, String> params) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put(PaymentBindCardParamConstant.param_merchantId, params.get("id"));
		resultMap.put(PaymentBindCardParamConstant.param_appId, params.get("appid"));
		resultMap.put(PaymentBindCardParamConstant.param_gatewayId, params.get("gatewayId"));
		resultMap.put(PaymentBindCardParamConstant.param_merchantCustomerId, params.get("mer_cust_id"));
		resultMap.put(PaymentBindCardParamConstant.param_customerIdentityCode, params.get("cust_identity_code"));
		resultMap.put(PaymentParamConstant.param_oper_type, PaymentMethodConstant.param_operate_unbindcard);
		return resultMap;
	}

	public Map<String, String> transferFindBindCardParamsV1ToV2(Map<String, String> params) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put(PaymentBindCardParamConstant.param_merchantId, params.get("id"));
		resultMap.put(PaymentBindCardParamConstant.param_appId, params.get("appid"));
		resultMap.put(PaymentBindCardParamConstant.param_gatewayId, params.get("gatewayId"));
		resultMap.put(PaymentBindCardParamConstant.param_merchantCustomerId, params.get("mer_cust_id"));
		resultMap.put(PaymentBindCardParamConstant.param_customerIdentityCode, params.get("cust_identity_code"));
		resultMap.put(PaymentParamConstant.param_oper_type, PaymentMethodConstant.param_operate_unbindcard);
		return resultMap;
	}

	@Override
	public Map<String, String> transferRefundParamsV1ToV2(Map<String, String> params) {
		Map<String, String> resultMap = new HashMap<String, String>();
		transferCommonParamsV1ToV2(params, resultMap);
		resultMap.put(RefundParamConstant.param_merchantRefundOrderId, params.get("orderidinf"));
		resultMap.put(RefundParamConstant.param_order_id, params.get("orderId"));
		resultMap.put(RefundParamConstant.param_refundAmount, params.get("totalPrice"));
		resultMap.put(RefundParamConstant.param_merchantNotifyUrl, params.get("bgretUrl"));
		resultMap.put(RefundParamConstant.param_reason, params.get("reason"));
		resultMap.put(RefundParamConstant.param_payNo, params.get("payNo"));
		resultMap.put(RefundParamConstant.param_topPayNo, params.get("topPayNo"));
		return resultMap;
	}

	private void transferCommonParamsV1ToV2(Map<String, String> params, Map<String, String> resultMap) {
		resultMap.put(BaseParamConstant.param_is_zitopay_v1, "1");
		resultMap.put(BaseParamConstant.param_merchantId, params.get("id"));
		resultMap.put(BaseParamConstant.param_appId, params.get("appid"));
	}

	public Map<String, Object> pubVerifyToSignWithPay(String orderId) {
		OrderEntity order = orderService.findByOrderId(orderId);
		PersonEntity zitopayPerson = personService.queryByPersonId(order.getPersionid());// 查询商户表
		PersonApplicationEntity zitopayPersonApplication = personApplicationService.selectByApplicationId(order.getApplicationId());// 查询商户应用表
		String appid = zitopayPersonApplication.getAppId();// 给客户的appid
		String appkey = zitopayPersonApplication.getApplicationKey();// 应用key
		String sign = verifyToSign(ZITOPAY_SIGN_HEAD + zitopayPerson.getPid(), appid, order.getOrderidinf(), order.getTotalprice().toString(), appkey);// 生成签名)

		Map<String, Object> resultMap = new HashMap<String, Object>();// 回调发送的参数

		resultMap.put("sign", sign);// 签名
		resultMap.put("orderId", order.getOrderid());// 订单号
		resultMap.put("orderidinf", order.getOrderidinf());// 商户给我们的订单号
		resultMap.put("payno", order.getPayno());
		resultMap.put("toppayno", order.getToppayno());

		return resultMap;
	}
	
	public Map<String, Object> pubVerifyToSignWithTransfer(String transferid) {
		TransferEntity transferEntity = transferService.getByTransferid(transferid);
		PersonEntity zitopayPerson = personService.queryByPersonId(transferEntity.getPersonid());// 查询商户表
		String sign = verifyToSign(ZITOPAY_TRANSFER__SIGN_HEAD + zitopayPerson.getPid(), "", transferEntity.getOrderidinf(), transferEntity.getTotalprice().toString(), zitopayPerson.getPersonkey());// 生成签名)

		Map<String, Object> resultMap = new HashMap<String, Object>();// 回调发送的参数
		resultMap.put("transferid", transferid);
		resultMap.put("sign", sign);// 签名
		return resultMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> transferQueryResult(Map<String, Object> returnParams) {

		Map<String, Object> resultMap = new HashMap<String, Object>();// 回调发送的参数

		resultMap.put("allnum", returnParams.get(QueryParamConstant.param_order_num));
		resultMap.put("page", "0");// 商户给我们的订单号
		resultMap.put("posttime", DateUtils.formatDate(new Date(), "yyyyMMddhhmmssSSS"));
		List<Map<String, Object>> orderList = (List<Map<String, Object>>) returnParams.get(QueryParamConstant.param_order_list);
		JSONArray resultArray = new JSONArray();
		if (orderList != null && orderList.size() > 0) {
			for (Iterator<Map<String, Object>> itor = orderList.iterator(); itor.hasNext();) {
				Map<String, Object> result = itor.next();
				resultArray.add(buildCommonQueryResult(result));
			}
		} else {
			resultArray.add(buildCommonQueryResult(returnParams));
		}
		resultMap.put("orderList", resultArray);

		return resultMap;
	}

	private JSONObject buildCommonQueryResult(Map<String, Object> result) {
		JSONObject resultJson = new JSONObject();
		resultJson.put("amount", result.get(QueryParamConstant.param_actualAmount));
		resultJson.put("appId", result.get(QueryParamConstant.param_orderAmount));
		resultJson.put("getewayid", result.get(QueryParamConstant.param_gatewayId));
		resultJson.put("orderId", result.get(QueryParamConstant.param_order_id));
		resultJson.put("orderidinf", result.get(QueryParamConstant.param_merchantOrderId));
		resultJson.put("ordertitle", result.get(QueryParamConstant.param_orderTitle));
		resultJson.put("paysucctime", result.get(QueryParamConstant.param_payTime));
		resultJson.put("posttime", result.get(QueryParamConstant.param_orderTime));
		resultJson.put("ratemmoney", result.get(QueryParamConstant.param_fee));
		resultJson.put("state", result.get(QueryParamConstant.param_state));
		resultJson.put("totalprice", result.get(QueryParamConstant.param_orderAmount));
		resultJson.put("zongduanno", result.get(QueryParamConstant.param_terminal_no));
		return resultJson;
	}

	public String noticeParams(OrderEntity order) {
		PersonEntity zitopayPerson = personService.queryById(order.getPersionid());// 查询商户表
		PersonApplicationEntity zitopayPersonApplication = personApplicationService.queryById(order.getApplicationId());// 查询商户应用表
		String appid = zitopayPersonApplication.getAppId();// 给客户的appid
		String appkey = zitopayPersonApplication.getApplicationKey();// 应用key
		String sign = verifyToSign(ZITOPAY_SIGN_HEAD + zitopayPerson.getPid(), appid, order.getOrderidinf(), order.getTotalprice().toString(), appkey);// 生成签名)
		Map<String, Object> job = new HashMap<String, Object>();// 回调发送的参数
		job.put("success", true);// 请求结果
		job.put("msg", "ok");// 请求结果
		job.put("orderId", order.getOrderid());// 订单号
		job.put("totalPrice", order.getTotalprice());// 订单金额
		job.put("tradeAmount", String.valueOf(order.getAmount()));// 订单实际支付金额（实际到账金额）
		job.put("tradeFee", String.valueOf(order.getRatemmoney()));// 手续费金额
		job.put("rate", order.getRate());// 手续费费率
		String paySuccessTime = "";
		if (order.getPaysucctime() != null) {
			paySuccessTime = DateUtils.formatDate(order.getPaysucctime(), "yyyy-MM-dd HH:mm:ss");
		}
		job.put("paysucctime", paySuccessTime);// 交易成功时间
		job.put("bankAccount", order.getAccountname());// 支付方帐号（银行卡号或支付账号）
		job.put("orderidinf", order.getOrderidinf());// 商户给我们的订单号
		job.put("appid", appid);// 应用id
		job.put("sign", sign);// 签名
		return JSON.toJSONString(job);
	}

	/**
	 * 方法描述: <br>
	 * 
	 * @param id
	 *            商户ID
	 * @param username
	 *            商户名
	 * @param totalPrice
	 *            商品总金额
	 * @param sign
	 *            签名
	 * @return boolean类型验签结果 作者： 张雨生 <br>
	 *         创建时间： 2016-8-25 下午06:00:10
	 */
	public static boolean verifySign(String id, String username, String totalPrice, String sign, String key) {
		boolean vSign = false;
		String md5Si = verifyToSign(id, username, "", totalPrice, key);
		if (sign != null && sign.equals(md5Si)) {// 验签通过，返回true
			logger.debug("验证签名成功!");
			vSign = true;
		}
		return vSign;
	}

	/**
	 * 方法描述: <br>
	 * 
	 * @param id
	 *            商户ID
	 * @param username
	 *            商户名
	 * @param totalPrice
	 *            商品总金额
	 * @param sign
	 *            签名
	 * @return boolean类型验签结果 作者： 张雨生 <br>
	 *         创建时间： 2016-8-25 下午06:00:10
	 */
	public static boolean verifySign(String id, String appid, String orderidinf, String totalPrice, String sign, String key) {
		boolean vSign = false;
		String md5Si = verifyToSign(id, appid, orderidinf, totalPrice, key);
		if (sign != null && sign.equals(md5Si)) {// 验签通过，返回true
			logger.debug("验证签名成功!");
			vSign = true;
		}
		return vSign;
	}

	/**
	 * 方法描述: <br>
	 * 
	 * @param id
	 *            商户ID
	 * @param username
	 *            商户名
	 * @param totalPrice
	 *            金额
	 * @param sign
	 *            签名
	 * @return boolean类型验签结果 作者： 张雨生 <br>
	 *         创建时间： 2016-8-25 下午06:00:10
	 */
	public static String verifyToSign(String id, String appid, String orderidinf, String totalPrice, String key) {
		logger.debug("key=" + key);
		StringBuffer sbf = new StringBuffer("");
		sbf.append(id).append(appid).append(orderidinf).append(totalPrice).append(key);
		String si = sbf.toString();// 加密原串//生成方式md5(id+username+totalPrice+key)
		logger.debug("md5原串:" + si);
		String sign = encode(si);// 生成签名
		logger.debug("md5生成的签名:" + sign);
		return sign;
	}

	/**
	 * 
	 * 
	 * 方法描述:利用java自带的MD5加密，生成32个字符的16进制(utf-8编码) <br>
	 * 
	 * @return String <br>
	 *         作者： 徐诚 <br>
	 *         创建时间： 2016-6-14 下午01:43:33
	 */
	public final static String encode(String s) {
		logger.debug("md5加密前:" + s);
		// 16进制字符
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		try {
			if (s == null) {
				return null;
			}
			byte[] btInput = s.getBytes("utf-8");
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			String jm = new String(str);
			logger.debug("md5加密后:" + jm);
			return jm;
		} catch (Exception e) {
			logger.error("md5加密失败", e);
			return "";
		}
	}

	@Override
	public Map<String, Object> toV2Transfer(String publicKey, String privateKey, Map<String, String> params) {
		try {
			Map<String, Object> returnMap = new HashMap<String, Object>();
			String id = params.get("id");// 商户id，由融智付分配
			String totalPrice = params.get("totalPrice");// 商品总价格，以元为单位，小数点后2位
			String orderidinf = params.get("orderidinf");// 商户传入订单号
			String sign = params.get("sign");
			if (StringUtils.isBlank(sign)) {
				returnMap.put("success", false);
				returnMap.put("msg", "签名[sign]不能为空");
				return returnMap;
			}
			// 验证商户是否存在以及启用
			PersonEntity zitopayPerson = personService.getZitopayPersonByPid(id);
			if (null == zitopayPerson) {
				return ResponseInfoEnum.商户不存在.getMap();
			}
			if (DisabledStatusConstant.STATUS_DISABLED.equals(zitopayPerson.getStatus())) {
				return ResponseInfoEnum.商户已禁用.getMap();
			}
			
			boolean vSign = verifySign(id, "", orderidinf, totalPrice, sign, zitopayPerson.getPersonkey());
			if (!vSign) {
				returnMap.put("success", false);
				returnMap.put("msg", "验证签名失败!");
				return returnMap;
			}
			RequestDatagram rd = new RequestDatagram().initDatagram();
			rd.setData(transferTransferParamsV1ToV2(params));
			String encryptData = rd.toEncryptString(publicKey);
			HttpEntity<String> httpEntity = new HttpEntity<String>(URLEncoder.encode(encryptData, "UTF-8"));
			ResponseDatagram rsd = restTemplate.postForObject(envProperties.PREFIX_GATEWAY_URL + "/transfer/json", httpEntity, ResponseDatagram.class);
			rsd.toDecryptString(privateKey);
			@SuppressWarnings("unchecked")
			Map<String, String> dataMap = (Map<String, String>) rsd.getData();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (ResponseInfoEnum.调用成功.equals(BeanUtils.bean2Map(rsd))) {
				resultMap.put("success", true);
				String status = dataMap.get(TransferResultParamConstant.status);
				if (StringUtils.isNotBlank(status)) {
					resultMap.put("status", status);
				}
				String transferid = dataMap.get(TransferResultParamConstant.param_transferid);
				if (StringUtils.isNotBlank(transferid)) {
					resultMap.putAll(pubVerifyToSignWithTransfer(transferid));
				}
			} else {
				resultMap.put("success", false);
			}
			resultMap.put("code", rsd.getCode());
			resultMap.put("msg", rsd.getMsg());
			return resultMap;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public Map<String, Object> toV2Issued(String publicKey, String privateKey, Map<String, String> params) {
		try {
			Map<String, Object> returnMap = new HashMap<String, Object>();
			String id = params.get("id");// 商户id，由融智付分配
			String totalPrice = params.get("totalPrice");// 商品总价格，以元为单位，小数点后2位
			String orderidinf = params.get("orderidinf");// 商户传入订单号
			String sign = params.get("sign");
			if (StringUtils.isBlank(sign)) {
				returnMap.put("success", false);
				returnMap.put("msg", "签名[sign]不能为空");
				return returnMap;
			}
			// 验证商户是否存在以及启用
			PersonEntity zitopayPerson = personService.getZitopayPersonByPid(id);
			if (null == zitopayPerson) {
				return ResponseInfoEnum.商户不存在.getMap();
			}
			if (DisabledStatusConstant.STATUS_DISABLED.equals(zitopayPerson.getStatus())) {
				return ResponseInfoEnum.商户已禁用.getMap();
			}
			
			boolean vSign = verifySign(id, "", orderidinf, totalPrice, sign, zitopayPerson.getPersonkey());
			if (!vSign) {
				returnMap.put("success", false);
				returnMap.put("msg", "验证签名失败!");
				return returnMap;
			}
			RequestDatagram rd = new RequestDatagram().initDatagram();
			rd.setData(transferIssuedParamsV1ToV2(params));
			String encryptData = rd.toEncryptString(publicKey);
			HttpEntity<String> httpEntity = new HttpEntity<String>(URLEncoder.encode(encryptData, "UTF-8"));
			ResponseDatagram rsd = restTemplate.postForObject(envProperties.PREFIX_GATEWAY_URL + "/issued/json", httpEntity, ResponseDatagram.class);
			rsd.toDecryptString(privateKey);
			@SuppressWarnings("unchecked")
			Map<String, String> dataMap = (Map<String, String>) rsd.getData();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (ResponseInfoEnum.调用成功.equals(BeanUtils.bean2Map(rsd))) {
				resultMap.put("success", true);
				String status = dataMap.get(IssuedResultParamConstant.status);
				if (StringUtils.isNotBlank(status)) {
					resultMap.put("status", status);
				}
				String transferid = dataMap.get(IssuedResultParamConstant.param_issuedid);
				if (StringUtils.isNotBlank(transferid)) {
					resultMap.putAll(pubVerifyToSignWithTransfer(transferid));
				}
			} else {
				resultMap.put("success", false);
			}
			resultMap.put("code", rsd.getCode());
			resultMap.put("msg", rsd.getMsg());
			return resultMap;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return null;
	}

}
