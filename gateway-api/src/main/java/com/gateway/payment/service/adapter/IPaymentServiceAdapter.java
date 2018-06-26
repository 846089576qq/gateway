package com.gateway.payment.service.adapter;

import java.util.Map;

import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.PersonApplicationEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 融智付服务接口
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年1月20日
 */
public interface IPaymentServiceAdapter extends IBaseServiceAdapter {

	/**
	 * 校验支付参数
	 * @param paramMap
	 * @return
	 * @throws ServiceException
	 */
	Map<String, Object> checkPaymentParameter(Map<String, String> paramMap, String serialNo) throws ServiceException;

	/**
	 * @param merchantId
	 * @param applicationId
	 * @param gatewayId
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	Map<String, Object> checkPaymentPermission(String merchantId, String applicationId, String gatewayId, String serialNo) throws ServiceException;

	/**
	 * 进行支付处理
	 * 
	 * @param personEntity
	 * @param gatewayEntity
	 * @param personApplicationEntity
	 * @param personGatewayEntity
	 * @param paramMap
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> execPayment(PersonEntity personEntity, GatewayEntity gatewayEntity, PersonApplicationEntity personApplicationEntity, PersonGatewayEntity personGatewayEntity, Map<String, String> paramMap, String serialNo) throws ServiceException;

	/**
	 * 处理支付公司回调
	 * 
	 * @param personEntity
	 * @param gatewayEntity
	 * @param personApplicationEntity
	 * @param personGatewayEntity
	 * @param paramMap
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> execCallback(PersonEntity personEntity, GatewayEntity gatewayEntity, PersonApplicationEntity personApplicationEntity, PersonGatewayEntity personGatewayEntity, Map<String, String> paramMap, String serialNo) throws ServiceException;

}
