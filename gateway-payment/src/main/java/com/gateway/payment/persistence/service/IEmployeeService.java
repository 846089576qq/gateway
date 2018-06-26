package com.gateway.payment.persistence.service;

import com.gateway.payment.entity.EmployeeEntity;


/**
 * 代理商接口
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
public interface IEmployeeService extends IBaseGenericService<EmployeeEntity>{

	/**
	 * 获取代理信息
	 *
	 * @param gatewayId
	 * @return
	 */
	public EmployeeEntity getEmployee(String account, String password);


}