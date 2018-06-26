package com.gateway.payment.service.adapter;

import java.util.Map;

import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 代付服务接口
 * 
 * 作者：王政 创建时间：2017年3月6日 下午6:01:25
 */
public interface IIssuedServiceAdapter extends IBaseServiceAdapter {
	
	/**
	 * @param paramMap
	 * @return
	 * @throws ServiceException
	 */
	Map<String, Object> checkIssuedParameter(Map<String, String> paramMap, String serialNo) throws ServiceException;


	/**
	 * 进行代付处理
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
	public Map<String, Object> execIssued(PersonEntity zitopayPerson, GatewayEntity zitopayGeteway, PersonGatewayEntity zitopayPersonGeteway,
			Map<String, String> paramMap, String serialNo) throws ServiceException;

	/**
	 * 检查商户权限
	 * 
	 * @param merchantId
	 * @param gatewayId
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> checkIssuedPermission(String merchantId, String gatewayId, String serialNo) throws ServiceException;


}
