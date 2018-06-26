package com.gateway.payment.persistence.service;

import com.gateway.payment.entity.EmployeeGatewayEntity;


/**
 * 代理通道接口
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
public interface IEmployeeGatewayService extends IBaseGenericService<EmployeeGatewayEntity>{

	/**
	 * 获取代理通道信息
	 *
	 * @param employeeId
	 * @param gatewayId
	 * @return
	 */
	public EmployeeGatewayEntity getEmployeeGateway(Integer employeeId, Integer gatewayId);


}