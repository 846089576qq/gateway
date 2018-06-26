package com.gateway.payment.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.gateway.payment.entity.EmployeeGatewayEntity;
import com.gateway.payment.persistence.service.IEmployeeGatewayService;

/**
 * 代理通道的实现类
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Service
public class EmployeeGatewayServiceImpl extends BaseGenericServiceImpl<EmployeeGatewayEntity> implements IEmployeeGatewayService {
	
	@Override
	public EmployeeGatewayEntity getEmployeeGateway(Integer employeeId, Integer gatewayId) {
		EmployeeGatewayEntity employeeGatewayEntity = new EmployeeGatewayEntity();
		employeeGatewayEntity.setEmployeeId(employeeId);
		employeeGatewayEntity.setGetewayid(gatewayId);
		return queryOne(employeeGatewayEntity);
	}
}