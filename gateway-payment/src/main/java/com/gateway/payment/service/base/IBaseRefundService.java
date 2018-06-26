package com.gateway.payment.service.base;

import com.gateway.payment.entity.*;
import com.zitopay.foundation.common.exception.ServiceException;

import java.util.Map;

/**
 * 订单退款接口基类
 * 
 * 作者：王政
 * 创建时间：2017年3月17日 下午12:38:08
 */
public interface IBaseRefundService {
	
	/**
	 * 检查请求参数
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> checkParam(Map<String, String> paramMap) throws ServiceException;
	
	/**
	 * 退款
	 * 
	 * @param zitopayPerson
	 * @param zitopayGeteway
	 * @param zitopayPersonApplication
	 * @param zitopayPersonGeteway
	 * @param zitopayOrder
	 * @param paramMap
	 * @param serialNo
	 * @return
	 */
	public abstract Map<String, Object> refund(PersonEntity zitopayPerson, GatewayEntity zitopayGeteway, PersonApplicationEntity zitopayPersonApplication, PersonGatewayEntity zitopayPersonGeteway, OrderEntity zitopayOrder, OrderRefundEntity zitopayOrderRefund, Map<String, String> paramMap, String serialNo) throws ServiceException;

	/**
	 * 退款订单查询接口
	 * 
	 * @param orderRefund
	 * @param orderEntity
	 * @param personGatewayEntity
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> queryOrder(OrderRefundEntity orderRefund, OrderEntity orderEntity, PersonGatewayEntity personGatewayEntity, String serialNo) throws ServiceException;

	/**
	 * 退款成功操作
	 * @param merchantId 商户PID
	 * @param merchantPublicKey 商户RSA公钥
	 * @param threeid 请求退款流水号
	 * @param refundno 上游通道返回流水号
	 * @param topRefundNo 支付公司流水号
	 * @param serialNo 报文序列
	 * @return
	 */
	public Map<String, Object> refundSuccess(String merchantId, String merchantPublicKey, String threeid, String refundno, String topRefundNo, String serialNo);

	/**
	 * 退款失败操作
	 * @param merchantId 商户PID
	 * @param merchantPublicKey 商户RSA公钥
	 * @param threeid 请求退款流水号
	 * @param desc 提示信息
	 * @param serialNo 报文序列
	 * @return
	 */
	public Map<String, Object> refundFailed(String merchantId, String merchantPublicKey, String threeid, String desc, String serialNo);
	
	/**
	 * 退款接收通知参数
	 * 
	 * @param zitopayPerson
	 * @param zitopayGeteway
	 * @param zitopayPersonApplication
	 * @param zitopayPersonGeteway
	 * @param paramMap
	 * @param merchantPublicKey
	 * @param serialNo
	 * @return
	 */
	public abstract Map<String, Object> refundCallback(PersonEntity zitopayPerson, GatewayEntity zitopayGeteway, PersonApplicationEntity zitopayPersonApplication, PersonGatewayEntity zitopayPersonGeteway, Map<String, String> paramMap, String merchantPublicKey, String serialNo) throws ServiceException;
}
