package com.gateway.payment.service.base;

import java.util.Map;

import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.gateway.payment.entity.TransferEntity;
import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 账户转账接口基类
 * 
 * 作者：王政 创建时间：2017年3月17日 下午12:38:08
 */
public interface IBaseTransferService {

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
	public abstract Map<String, Object> transfer(PersonEntity zitopayPerson, GatewayEntity zitopayGeteway,
			PersonGatewayEntity zitopayPersonGeteway, TransferEntity transferEntity, Map<String, String> paramMap,
			String serialNo) throws ServiceException;

}
