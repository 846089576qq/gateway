package com.gateway.payment.service.base;

import java.util.Date;
import java.util.Map;

import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.OrderEntity;
import com.gateway.payment.entity.PersonApplicationEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 支付接口基类
 */
public interface IBasePaymentService {

	/**
	 * 自定义支付请求参数校验
	 * 
	 * @param paramMap
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> checkPaymentParam(Map<String, String> paramMap, String serialNo) throws ServiceException;

	/**
	 * 支付操作接口
	 * 
	 * @param personEntity
	 * @param gatewayEntity
	 * @param personApplicationEntity
	 * @param personGatewayEntity
	 * @param orderEntity
	 * @param paramMap
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> pay(PersonEntity personEntity, GatewayEntity gatewayEntity, PersonApplicationEntity personApplicationEntity, PersonGatewayEntity personGatewayEntity, OrderEntity orderEntity, Map<String, String> paramMap, String serialNo) throws ServiceException;

	/**
	 * 支付回调接口
	 * 
	 * @param personEntity
	 * @param gatewayEntity
	 * @param personApplicationEntity
	 * @param personGatewayEntity
	 * @param paramMap
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> callback(PersonEntity personEntity, GatewayEntity gatewayEntity, PersonApplicationEntity personApplicationEntity, PersonGatewayEntity personGatewayEntity, Map<String, String> paramMap, String merchantKey, String serialNo) throws ServiceException;

	/**
	 * 支付订单查询接口
	 * 
	 * @param orderEntity
	 * @param personGatewayEntity
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> queryOrder(OrderEntity orderEntity, PersonGatewayEntity personGatewayEntity, String serialNo) throws ServiceException;

	/**
	 * 获取银行卡信息<br>
	 * 第一层key bankCodeMap银行卡代码,bankNameMap银行卡名称;<br/>
	 * 第二层key 卡类型：1借记卡,2贷记卡;<br/>
	 * 第三层key 银行卡简称;<br/>
	 * 
	 * @param gatewayId
	 * @return
	 */
	public Map<String, Map<String, Map<String, String>>> getBankInfo(String gatewayId);

	/** 支付中处理
	 * @param order
	 * @param personGeteway
	 * @param serialNo
	 */
	public void payingPayment(OrderEntity order, PersonGatewayEntity personGeteway, String serialNo);

	/**
	 * 支付成功后处理
	 * 
	 * @param merchantId
	 * @param merchantPublicKey
	 * @param threeorderid
	 * @param paysucctime
	 * @param payno
	 * @param channelReturnValue
	 * @param tradeStatus
	 * @param serialNo
	 * @return
	 */
	Map<String, Object> afterPayment(String merchantId, String merchantPublicKey, String threeorderid, Date paysucctime, String payno, String toppayno, String channelReturnValue, String tradeStatus, String serialNo) throws ServiceException;

	/**
	 * 修改订单状态为失败
	 * @param entity
	 * @param serialNo
	 */
	public void changeOrderStateFail(OrderEntity entity, String serialNo);

	/**
	 * 异步通知商户
	 * 
	 * @param orderEntity
	 * @param notifyParam
	 */
	public void sendNotify(OrderEntity orderEntity, String notifyParam);

	/**
	 * 组织商户异步通知参数
	 * 
	 * @param merchantId
	 * @param merchantPublicKey
	 * @param serialNo
	 * @param orderEntity
	 * @return
	 * @throws ServiceException
	 */
	public String assemblyNotifyParam(String merchantId, String merchantPublicKey, String serialNo, OrderEntity orderEntity) throws ServiceException;

}
