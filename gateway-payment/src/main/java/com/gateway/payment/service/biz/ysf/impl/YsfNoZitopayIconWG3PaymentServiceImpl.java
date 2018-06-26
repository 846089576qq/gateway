package com.gateway.payment.service.biz.ysf.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gateway.channel.service.ysf.IYsfChannelService;
import com.gateway.common.annotaion.Gateway;
import com.gateway.common.constants.config.GatewayConstants;
import com.gateway.payment.service.IBaseService;
import com.gateway.payment.service.biz.ysf.IYsfWGPamentService;

@Gateway(gatewayId = IBaseService.BEAN_PREFIX_PAY + GatewayConstants.GATEWAY_YSF_NO_ICON_WG_3)
public class YsfNoZitopayIconWG3PaymentServiceImpl extends BaseysfPaymentServiceImpl implements IYsfWGPamentService {

	@Reference
	private IYsfChannelService channelService;

	@Override
	public IYsfChannelService getChannelService() {
		return channelService;
	}
	
}
