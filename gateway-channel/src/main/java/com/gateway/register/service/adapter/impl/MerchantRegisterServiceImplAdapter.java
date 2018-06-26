package com.gateway.register.service.adapter.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Service;

import com.gateway.common.constants.param.MerchantRegisterMethodConstant;
import com.gateway.common.constants.param.MerchantRegisterParamConstant;
import com.gateway.common.constants.param.MerchantRegisterUpdateParamConstant;
import com.gateway.common.constants.param.ZitopayMerchantRegisterParamConstant;
import com.gateway.common.context.BeanContextUtil;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.service.adapter.IMerchantRegisterServiceAdapter;
import com.gateway.register.service.base.IBaseRegisterService;
import com.zitopay.foundation.common.exception.ServiceException;

@Service
@com.alibaba.dubbo.config.annotation.Service
public class MerchantRegisterServiceImplAdapter implements IMerchantRegisterServiceAdapter {

	private static final Logger logger = LoggerFactory.getLogger(MerchantRegisterServiceImplAdapter.class);

	@Override
	public Map<String, Object> checkRegisterParameter(String method, Map<String, String> paramMap, String serialNo) throws ServiceException {
		try {
			String channelId = paramMap.get(ZitopayMerchantRegisterParamConstant.param_gatewayId);
			final IBaseRegisterService baseRegisterService = getRegisterService(channelId);
			if (null == baseRegisterService) {
				return ResponseInfoEnum.调用失败.getMap("未找到编号[" + channelId + "]的商户入驻通道");
			}
			if (method.equals(MerchantRegisterMethodConstant.method_register)) {
				return checkRegisterParam(paramMap, serialNo);
			}else if(method.equals(MerchantRegisterMethodConstant.method_update)) {
				return checkRegisterUpdateParam(paramMap, serialNo);
			}
			return ResponseInfoEnum.调用失败.getMap("融智付商户入驻方法异常");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",入驻参数校验异常,请检查!", e);
		}
	}

	@Override
	public Map<String, Object> execRegister(String method, Map<String, String> paramMap, String serialNo) throws ServiceException {
		String channelId = paramMap.get(MerchantRegisterParamConstant.param_gatewayId);
		IBaseRegisterService baseRegisterService = getRegisterService(channelId);
		if (null == baseRegisterService) {
			return ResponseInfoEnum.调用失败.getMap("未找到编号[" + channelId + "]的商户注册通道");
		}
		if(method.equals(MerchantRegisterMethodConstant.method_register)) {
			return baseRegisterService.register(paramMap, serialNo);
		}else if(method.equals(MerchantRegisterMethodConstant.method_update)) {
			return baseRegisterService.update(paramMap, serialNo);
		}else if(method.equals(MerchantRegisterMethodConstant.method_query)) {
			return baseRegisterService.query(paramMap, serialNo);
		}
		return ResponseInfoEnum.调用失败.getMap("未找到该方法");
	}
	
	@SuppressWarnings({ "unchecked" })
	protected <T> T getRegisterService(String channelId) throws ServiceException {
		try {
			return (T) BeanContextUtil.getMerchantRegisterBean(channelId);
		} catch (BeansException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e);
		}
	}
	

	/**
	 * 商户注册公共参数验证方法
	 * @param paramMap
	 * @return
	 */
	protected Map<String, Object> checkRegisterParam(Map<String, String> paramMap, String serialNo) {
		String merName = paramMap.get(MerchantRegisterParamConstant.param_merName);
		if (StringUtils.isBlank(merName)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterParamConstant.param_merName + "]不能为空");
		}

		String merShortName = paramMap.get(MerchantRegisterParamConstant.param_merShortName);
		if (StringUtils.isBlank(merShortName)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterParamConstant.param_merShortName + "]不能为空");
		}

		String merAddress = paramMap.get(MerchantRegisterParamConstant.param_merAddress);
		if (StringUtils.isBlank(merAddress)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterParamConstant.param_merAddress + "]不能为空");
		}

		String merContact = paramMap.get(MerchantRegisterParamConstant.param_merContact);
		if (StringUtils.isBlank(merContact)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterParamConstant.param_merContact + "]不能为空");
		}

		String merEmail = paramMap.get(MerchantRegisterParamConstant.param_merEmail);
		if (StringUtils.isBlank(merEmail)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterParamConstant.param_merEmail + "]不能为空");
		}

		String bankCode = paramMap.get(MerchantRegisterParamConstant.param_bankNo);
		if (StringUtils.isBlank(bankCode)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterParamConstant.param_bankNo + "]不能为空");
		}

		String bankProvince = paramMap.get(MerchantRegisterParamConstant.param_bankProvince);
		if (StringUtils.isBlank(bankProvince)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterParamConstant.param_bankProvince + "]不能为空");
		}

		String bankCity = paramMap.get(MerchantRegisterParamConstant.param_bankCity);
		if (StringUtils.isBlank(bankCity)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterParamConstant.param_bankCity + "]不能为空");
		}

		String accType = paramMap.get(MerchantRegisterParamConstant.param_accType);
		if (StringUtils.isBlank(accType)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterParamConstant.param_accType + "]不能为空");
		}

		String cardNo = paramMap.get(MerchantRegisterParamConstant.param_cardNo);
		if (StringUtils.isBlank(cardNo)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterParamConstant.param_cardNo + "]不能为空");
		}

		String cardMobile = paramMap.get(MerchantRegisterParamConstant.param_cardMobile);
		if (StringUtils.isBlank(cardMobile)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterParamConstant.param_cardMobile + "]不能为空");
		}

		String cardIdentityCode = paramMap.get(MerchantRegisterParamConstant.param_cardIdentityCode);
		if (StringUtils.isBlank(cardIdentityCode)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterParamConstant.param_cardIdentityCode + "]不能为空");
		}

		String cardRealName = paramMap.get(MerchantRegisterParamConstant.param_cardRealName);
		if (StringUtils.isBlank(cardRealName)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterParamConstant.param_cardRealName + "]不能为空");
		}

		String fixFee = paramMap.get(MerchantRegisterParamConstant.param_fixFee);
		if (StringUtils.isBlank(fixFee)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterParamConstant.param_fixFee + "]不能为空");
		}

		String rate = paramMap.get(MerchantRegisterParamConstant.param_settleRate);
		if (StringUtils.isBlank(rate)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterParamConstant.param_settleRate + "]不能为空");
		}

		String merConfig = paramMap.get(MerchantRegisterParamConstant.param_emplConfig);
		if (StringUtils.isBlank(merConfig)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterParamConstant.param_emplConfig + "]不能为空");
		}
		return ResponseInfoEnum.调用成功.getMap();
	}
	
	/**
	 * 商户注册更新公共参数验证方法
	 * @param paramMap
	 * @return
	 */
	protected Map<String, Object> checkRegisterUpdateParam(Map<String, String> paramMap, String serialNo) {
		String bankCode = paramMap.get(MerchantRegisterUpdateParamConstant.param_bankNo);
		if (StringUtils.isBlank(bankCode)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterUpdateParamConstant.param_bankNo + "]不能为空");
		}

		String bankProvince = paramMap.get(MerchantRegisterUpdateParamConstant.param_bankProvince);
		if (StringUtils.isBlank(bankProvince)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterUpdateParamConstant.param_bankProvince + "]不能为空");
		}

		String bankCity = paramMap.get(MerchantRegisterUpdateParamConstant.param_bankCity);
		if (StringUtils.isBlank(bankCity)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterUpdateParamConstant.param_bankCity + "]不能为空");
		}

		String accType = paramMap.get(MerchantRegisterUpdateParamConstant.param_accType);
		if (StringUtils.isBlank(accType)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterUpdateParamConstant.param_accType + "]不能为空");
		}

		String cardNo = paramMap.get(MerchantRegisterUpdateParamConstant.param_cardNo);
		if (StringUtils.isBlank(cardNo)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterUpdateParamConstant.param_cardNo + "]不能为空");
		}

		String cardMobile = paramMap.get(MerchantRegisterUpdateParamConstant.param_cardMobile);
		if (StringUtils.isBlank(cardMobile)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterUpdateParamConstant.param_cardMobile + "]不能为空");
		}

		String cardIdentityCode = paramMap.get(MerchantRegisterUpdateParamConstant.param_cardIdentityCode);
		if (StringUtils.isBlank(cardIdentityCode)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterUpdateParamConstant.param_cardIdentityCode + "]不能为空");
		}

		String cardRealName = paramMap.get(MerchantRegisterUpdateParamConstant.param_cardRealName);
		if (StringUtils.isBlank(cardRealName)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterUpdateParamConstant.param_cardRealName + "]不能为空");
		}

		String fixFee = paramMap.get(MerchantRegisterUpdateParamConstant.param_fixFee);
		if (StringUtils.isBlank(fixFee)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterUpdateParamConstant.param_fixFee + "]不能为空");
		}

		String rate = paramMap.get(MerchantRegisterUpdateParamConstant.param_settleRate);
		if (StringUtils.isBlank(rate)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterUpdateParamConstant.param_settleRate + "]不能为空");
		}

		String merConfig = paramMap.get(MerchantRegisterUpdateParamConstant.param_merConfig);
		if (StringUtils.isBlank(merConfig)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + MerchantRegisterUpdateParamConstant.param_merConfig + "]不能为空");
		}
		return ResponseInfoEnum.调用成功.getMap();
	}

}
