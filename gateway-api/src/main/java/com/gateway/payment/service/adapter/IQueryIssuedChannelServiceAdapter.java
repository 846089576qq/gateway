package com.gateway.payment.service.adapter;

import java.util.Map;

import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 代付通道查询服务接口
 * 作者：王政
 * 创建时间：2017年3月6日 下午7:10:18
 */
public interface IQueryIssuedChannelServiceAdapter extends IBaseServiceAdapter {
	
	/**
	 * @param paramMap
	 * @return
	 * @throws ServiceException
	 */
	Map<String, Object> checkQueryChannelParameter(Map<String, String> paramMap, String serialNo) throws ServiceException;

	/**
	 * @param merchantId
	 * @param gatewayId
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	Map<String, Object> checkQueryChannelPermission(String merchantId,String gatewayId, String serialNo) throws ServiceException;
	
	/**
	 * 进行支付查询处理
	 * 
	 * @param zitopayPerson
	 * @param zitopayGeteway
	 * @param zitopayPersonGeteway
	 * @param paramMap
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> execChannelQuery(PersonEntity zitopayPerson, GatewayEntity zitopayGeteway, PersonGatewayEntity zitopayPersonGeteway, Map<String, String> paramMap, String serialNo) throws ServiceException;

}
