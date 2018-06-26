package com.gateway.payment.persistence.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.gateway.payment.entity.OrderRefundEntity;
import com.github.abel533.mapper.Mapper;

/**
 * 退款mapper
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Repository
public interface IOrderRefundMapper extends Mapper<OrderRefundEntity> {

}