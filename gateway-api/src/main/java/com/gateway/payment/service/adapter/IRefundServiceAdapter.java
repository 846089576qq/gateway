package com.gateway.payment.service.adapter;

import java.util.Map;

import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.PersonApplicationEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 融智付退款服务接口
 * 
 * 作者：王政
 * 创建时间：2017年3月6日 下午6:01:35
 */
public interface IRefundServiceAdapter extends IBaseServiceAdapter {
	
	
	/**
	 * @param paramMap
	 * @return
	 * @throws ServiceException
	 */
	Map<String, Object> checkRefundParameter(Map<String, String> paramMap, String serialNo) throws ServiceException;

	/**
	 * @param merchantId
	 * @param applicationId
	 * @param gatewayId
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	Map<String, Object> checkRefundPermission(String merchantId, String applicationId, String gatewayId, String serialNo) throws ServiceException;
	
	
	/**
	 * 进行退款处理
	 * 
	 * @param zitopayPerson
	 * @param zitopayGeteway
	 * @param zitopayPersonApplication
	 * @param zitopayPersonGeteway
	 * @param paramMap
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> execRefund(PersonEntity zitopayPerson, PersonApplicationEntity zitopayPersonApplication, Map<String, String> paramMap, String serialNo) throws ServiceException;

	/**
	 * 处理支付公司回调
	 * 
	 * @param zitopayPerson
	 * @param zitopayGeteway
	 * @param zitopayPersonApplication
	 * @param zitopayPersonGeteway
	 * @param paramMap
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> execCallback(PersonEntity zitopayPerson, GatewayEntity zitopayGeteway, PersonApplicationEntity zitopayPersonApplication, PersonGatewayEntity zitopayPersonGeteway, Map<String, String> paramMap, String serialNo) throws ServiceException;

}
