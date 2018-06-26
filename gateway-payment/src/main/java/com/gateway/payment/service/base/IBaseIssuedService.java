package com.gateway.payment.service.base;

import java.util.Map;

import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.IssuedEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 账户代付接口基类
 * 
 * 作者：王政 创建时间：2017年3月17日 下午12:38:08
 */
public interface IBaseIssuedService {

	/**
	 * 检查请求参数
	 * 
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> checkParam(Map<String, String> paramMap) throws ServiceException;

	/**
	 * 转账
	 * 
	 * @param zitopayPerson
	 * @param zitopayGeteway
	 * @param zitopayPersonApplication
	 * @param zitopayPersonGeteway
	 * @param transferEntity
	 * @param paramMap
	 * @param serialNo
	 * @return
	 */
	public abstract Map<String, Object> issued(PersonEntity zitopayPerson, GatewayEntity zitopayGeteway, PersonGatewayEntity zitopayPersonGeteway, IssuedEntity issuedEntity, Map<String, String> paramMap, String serialNo) throws ServiceException;

	/**
	 * 代付订单查询接口
	 * 
	 * @param issuedEntity
	 * @param personGatewayEntity
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> queryOrder(IssuedEntity issuedEntity, PersonGatewayEntity personGatewayEntity, String serialNo) throws ServiceException;

	/**
	 * 代付成功后处理
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
	public Map<String, Object> afterIssued(String merchantId, String merchantPublicKey, String threeorderid, String paysucctime, String payno, String toppayno, String threepoundage, String status, String serialNo) throws ServiceException;

}
