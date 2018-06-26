package com.gateway.payment.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.gateway.payment.entity.OrderEntity;
import com.github.abel533.mapper.Mapper;

/**
 * 订单mapper
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Repository
public interface IOrderMapper extends Mapper<OrderEntity> {

	/**
	 * 订单查询接口
	 * 
	 * @param personId
	 * @param appId
	 * @param gatewayid
	 * @param startDate
	 * @param endDate
	 * @param pageNo
	 * @param pagesize
	 * @return
	 */
	List<OrderEntity> queryOrder(@Param(value = "personId") Integer personId, @Param(value = "appId") String appId, @Param(value = "gatewayid") Integer gatewayid, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pagesize);

	/**
	 * 订单查询接口_查询总条数
	 * 
	 * @param personId
	 * @param appId
	 * @param gatewayid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	int queryOrderCount(@Param(value = "personId") Integer personId, @Param(value = "appId") String appId, @Param(value = "gatewayid") Integer gatewayid, @Param("startDate") String startDate, @Param("endDate") String endDate);

	int countOrderByOrderidinf(@Param("orderidinf") String orderidinf);

	int batchUpdate(List<OrderEntity> list);

	/**
	 * @param startOrderId
	 * @param endOrderId
	 */
	List<OrderEntity> selectOrderStartToEnd(@Param("startOrderId") String startOrderId, @Param("endOrderId") String endOrderId, @Param("fromStatus") int fromStatus);

	/**
	 * @param startOrderId
	 * @param endOrderId
	 * @param status
	 * @return
	 */
	List<OrderEntity> queryOrdersByStatus(@Param("startOrderId") String startOrderId, @Param("endOrderId") String endOrderId, @Param("status") int status);

}