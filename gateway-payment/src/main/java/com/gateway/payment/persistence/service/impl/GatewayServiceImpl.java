package com.gateway.payment.persistence.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.persistence.mapper.IGatewayMapper;
import com.gateway.payment.persistence.service.IGatewayService;

/**
 * 通道的实现类
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Service
public class GatewayServiceImpl extends BaseGenericServiceImpl<GatewayEntity> implements IGatewayService {

	@Autowired
	IGatewayMapper gatewayMapper;

	@Override
	public GatewayEntity getGateway(Integer gatewayId) {
		GatewayEntity entity = new GatewayEntity();
		entity.setId(gatewayId);
		return queryOne(entity);
	}

	@Override
	public boolean isCategoryGateway(String gatewayId, String categoryCode) {
		return gatewayMapper.isCategoryGateway(gatewayId, categoryCode);
	}

	@Override
	public String getGatewayIdByPersonidAndCategoryCode(Integer merchantId, String categoryCode) {
		return gatewayMapper.getGatewayIdByPersonidAndCategoryCode(merchantId, categoryCode);
	}
}