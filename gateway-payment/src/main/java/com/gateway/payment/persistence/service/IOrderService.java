package com.gateway.payment.persistence.service;

import java.util.List;
import java.util.Map;

import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.OrderEntity;
import com.gateway.payment.entity.PersonApplicationEntity;
import com.gateway.payment.entity.PersonEntity;

/**
 * 订单接口
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
public interface IOrderService extends IBaseGenericService<OrderEntity> {

	/**
	 * 创建支付订单
	 * 
	 * @param merchantId
	 * @param appId
	 * @param orderidinf
	 * @param posttime
	 * @param totalPrice
	 * @param orderTitle
	 * @param returnUrl
	 * @param bgRetUrl
	 * @return
	 */
	public OrderEntity createOrder(PersonApplicationEntity zitopayPersonApplication, PersonEntity zitopayPerson, GatewayEntity zitopayGeteway, String orderidinf, Map<String, String> paramMap);

	/**
	 * 获取融智付商户应用订单
	 * 
	 * @param personId
	 *            商户ID
	 * @param appId
	 *            应用ID
	 * @param orderidinf
	 *            商户订单号
	 * @return
	 */
	public OrderEntity getPersonApplicationOrder(Integer personId, String appId, String orderidinf);

	/**
	 * 更新订单
	 * 
	 * @param orderEntity
	 * @return
	 */
	public int updateByPrimaryKeySelective(OrderEntity orderEntity);

	/**
	 * 创建订单
	 * 
	 * @param orderEntity
	 * @return
	 */
	public int createOrder(OrderEntity orderEntity);

	/**
	 * 平台订单号查询订单
	 * 
	 * @param orderId
	 * @return
	 */
	public OrderEntity findByOrderId(String orderId);

	/**
	 * 平台订单号查询订单
	 * 
	 * @param orderIds
	 * @return
	 */
	public List<OrderEntity> findByOrderIds(String[] orderIds);

	/**
	 * 平台订单号查询订单
	 * 
	 * @param orderId
	 * @return
	 */
	public OrderEntity findByOrderId(Integer personid, String appId, String orderId);

	/**
	 * 平台订单号查询订单
	 * 
	 * @param orderIds
	 * @return
	 */
	public List<OrderEntity> findByOrderIds(Integer personid, String appId, String[] orderIds);

	/**
	 * 根据第三方订单号查询订单
	 * 
	 * @param threeorderid
	 * @return
	 */
	public OrderEntity findByhreerderid(String threeorderid);

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
	public List<OrderEntity> queryOrder(Integer personId, String appId, Integer gatewayid, String startDate, String endDate, Integer pageNo, Integer pagesize);

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
	public Integer queryOrderCount(Integer personId, String appId, Integer gatewayid, String startDate, String endDate);

	/**
	 * 根据商户ID查询
	 * 
	 * @param orderidinf
	 * @return
	 */
	public int countOrderByOrderidinf(String orderidinf);

	/**
	 * 更新订单状态
	 * 
	 * @param startOrderId
	 * @param endOrderId
	 * @param fromStatus
	 * @param toStatus
	 */
	public void updateOrder(String startOrderId, String endOrderId, int fromStatus, int toStatus);

	/**
	 * 根据状态查询订单区间订单
	 * 
	 * @param startOrderId
	 * @param endOrderId
	 * @param statusUnpaying
	 * @return
	 */
	public List<OrderEntity> queryOrderByStatus(String startOrderId, String endOrderId, int statusUnpaying);

	/**
	 * 根据商户订单号查询订单
	 * 
	 * @param merchantOrderId
	 * @return
	 */
	public OrderEntity findByMerchantOrderId(String merchantOrderId);

	/**
	 * 根据商户订单号查询订单
	 * 
	 * @param merchantOrderId
	 * @return
	 */
	public List<OrderEntity> findByMerchantOrderIds(String[] merchantOrderIds);

	/**
	 * 根据商户订单号查询订单
	 * 
	 * @param personid
	 * @param appId
	 * @param merchantOrderId
	 * @return
	 */
	public OrderEntity findByMerchantOrderId(Integer personid, String appId, String merchantOrderId);

	/**
	 * 根据商户订单号查询订单
	 * 
	 * @param merchantOrderId
	 * @return
	 */
	public List<OrderEntity> findByMerchantOrderIds(Integer personid, String appId, String[] merchantOrderIds);

}