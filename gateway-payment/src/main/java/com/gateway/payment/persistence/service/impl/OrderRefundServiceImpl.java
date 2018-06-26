package com.gateway.payment.persistence.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gateway.payment.entity.OrderRefundEntity;
import com.gateway.payment.persistence.mapper.IOrderRefundMapper;
import com.gateway.payment.persistence.service.IOrderRefundService;

/**
 * 退款接口实现类
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Service
public class OrderRefundServiceImpl extends BaseGenericServiceImpl<OrderRefundEntity> implements IOrderRefundService {

	@Autowired
	private IOrderRefundMapper orderRefundMapper;

	@Override
	public OrderRefundEntity getZitopayOrderRefund(String appId, String orderidinf) {
		OrderRefundEntity zitopayOrderRefund = new OrderRefundEntity();
		zitopayOrderRefund.setAppId(appId);
		zitopayOrderRefund.setOrderidinf(orderidinf);
		return queryOne(zitopayOrderRefund);
	}


	@Override
	public OrderRefundEntity findByOrderId(String orderId) {
		OrderRefundEntity orderRefundEntity=new OrderRefundEntity();
		orderRefundEntity.setOrderid(orderId);
		return queryOne(orderRefundEntity);
	}


	@Override
	public OrderRefundEntity findBythreeid(String threeid) {
		OrderRefundEntity orderRefundEntity=new OrderRefundEntity();
		orderRefundEntity.setThreeid(threeid);
		return queryOne(orderRefundEntity);
	}

}