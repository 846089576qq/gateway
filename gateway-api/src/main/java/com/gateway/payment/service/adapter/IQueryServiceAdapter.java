package com.gateway.payment.service.adapter;

import java.util.Map;

import com.gateway.payment.entity.PersonApplicationEntity;
import com.gateway.payment.entity.PersonEntity;
import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 融智付查询服务接口
 * 
 * 作者：王政 创建时间：2017年3月6日 下午6:01:25
 */
public interface IQueryServiceAdapter extends IBaseServiceAdapter {

	/**
	 * @param paramMap
	 * @return
	 * @throws ServiceException
	 */
	Map<String, Object> checkQueryParameter(Map<String, String> paramMap, String serialNo) throws ServiceException;

	/**
	 * @param merchantId
	 * @param applicationId
	 * @param gatewayId
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	Map<String, Object> checkQueryPermission(String merchantId, String applicationId, String serialNo) throws ServiceException;

	/**
	 * 进行查询处理
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
	public Map<String, Object> execQuery(PersonEntity zitopayPerson, PersonApplicationEntity zitopayPersonApplication, Map<String, String> paramMap, String serialNo) throws ServiceException;

	/**
	 * 查询商户信息
	 *
	 * @param merchantId
	 *            商户id
	 * @param serialNo
	 *            报文序列号
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> getMerchant(String merchantId, String serialNo) throws ServiceException;
	
	public void allunpayingOrderScheduler(String threeorderid);

}
