package com.gateway.payment.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.gateway.payment.entity.EmployeeEntity;
import com.gateway.payment.persistence.service.IEmployeeService;

/**
 * 通道的实现类
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Service
public class EmployeeServiceImpl extends BaseGenericServiceImpl<EmployeeEntity> implements IEmployeeService {
	
	@Override
	public EmployeeEntity getEmployee(String account, String password) {
		EmployeeEntity employeeEntity = new EmployeeEntity();
		employeeEntity.setAccount(account);
		employeeEntity.setPassword(password);
		employeeEntity.setIflock(0);
		return queryOne(employeeEntity);
	}
}