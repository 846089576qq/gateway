package com.gateway.payment.persistence.mapper;



import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.gateway.payment.entity.OrderRecheckEntity;
import com.github.abel533.mapper.Mapper;
@Repository
public interface IOrderRecheckMapper extends Mapper<OrderRecheckEntity>{

//    int deleteByPrimaryKey(Long id);
//
//    OrderRecheckEntity selectByPrimaryKey(Long id);
//
//    OrderRecheckEntity selectByOrderId(@Param("orderid") String orderid);
}