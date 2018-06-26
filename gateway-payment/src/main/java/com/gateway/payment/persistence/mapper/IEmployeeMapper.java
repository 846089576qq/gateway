package com.gateway.payment.persistence.mapper;


import org.springframework.stereotype.Repository;

import com.gateway.payment.entity.EmployeeEntity;
import com.github.abel533.mapper.Mapper;

@Repository
public interface IEmployeeMapper extends Mapper<EmployeeEntity> {

}
