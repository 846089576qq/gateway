package com.gateway.payment.persistence.service;

import com.gateway.payment.entity.OrderRefundEntity;

/**
 * 退款接口
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
public interface IOrderRefundService extends IBaseGenericService<OrderRefundEntity> {

	/**
	 * 获取融智付退款订单
	 * 
	 * @param appId
	 *            应用ID
	 * @param orderidinf
	 *            商户退款订单号
	 * @return
	 */
	public OrderRefundEntity getZitopayOrderRefund(String appId, String orderidinf);

//	/**
//	 * 获取融智付退款订单
//	 *
//	 * @param orderidinf
//	 *            商户退款订单号
//	 * @return
//	 */
//	public OrderRefundEntity getZitopayOrderRefundByOrderidinf(String orderidinf);
	
	/**
	 * 根据第三方订单号查询退款订单
	 * 
	 * @param threeorderid
	 * @return
	 */
	public OrderRefundEntity findBythreeid(String threeid);

	/**
	 * @param orderId
	 * @return
	 */
	public OrderRefundEntity findByOrderId(String orderId);
}