package com.gateway.register.service.biz.guangda.impl;

import java.util.Map;

import com.gateway.common.annotaion.MerchantRegisterChannel;
import com.gateway.common.constants.config.GatewayConstants;
import com.gateway.register.service.biz.guangda.IGuangDaRegisterService;
import com.zitopay.foundation.common.exception.ServiceException;

@MerchantRegisterChannel(channelId = GatewayConstants.GATEWAY_GD_WX_SM)
public class GuangDaWXSMRegisterServiceImpl implements IGuangDaRegisterService {

	@Override
	public Map<String, Object> register(Map<String, String> paramMap, String serialNo) throws ServiceException {
		return null;
	}

	@Override
	public Map<String, Object> update(Map<String, String> paramMap, String serialNo) throws ServiceException {
		return null;
	}

	@Override
	public Map<String, Object> query(Map<String, String> paramMap, String serialNo) throws ServiceException {
		return null;
	}

}
