package com.gateway.payment.service.base;

import java.util.Map;

import com.gateway.payment.entity.AccountSystemCashEntity;
import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 账户提现接口基类
 * 
 * 作者：王政 创建时间：2017年3月17日 下午12:38:08
 */
public interface IBaseDrawService {

	/**
	 * 检查请求参数
	 * 
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> checkParam(Map<String, String> paramMap) throws ServiceException;

	/**
	 * 提现
	 * 
	 * @param zitopayPerson
	 * @param zitopayGeteway
	 * @param zitopayPersonApplication
	 * @param zitopayPersonGeteway
	 * @param zitopayOrder
	 * @param accountSystemCash
	 * @param paramMap
	 * @param serialNo
	 * @return
	 */
	public abstract Map<String, Object> draw(PersonEntity zitopayPerson, GatewayEntity zitopayGeteway,
			PersonGatewayEntity zitopayPersonGeteway, AccountSystemCashEntity accountSystemCash, Map<String, String> paramMap,
			String serialNo) throws ServiceException;

	/**
	 * 提现接收通知参数
	 * 
	 * @param zitopayPerson
	 * @param zitopayGeteway
	 * @param zitopayPersonApplication
	 * @param zitopayPersonGeteway
	 * @param accountSystemCash
	 * @param paramMap
	 * @return
	 */
	public abstract Map<String, Object> drawCallback(PersonEntity zitopayPerson, GatewayEntity zitopayGeteway,
			PersonGatewayEntity zitopayPersonGeteway, String merchantKey, Map<String, String> paramMap) throws ServiceException;
}
