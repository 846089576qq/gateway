package com.gateway.payment.service.adapter;

import java.util.Map;

import com.gateway.payment.entity.PersonApplicationEntity;
import com.gateway.payment.entity.PersonEntity;
import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 融智付收银台服务接口
 * 
 * 作者：王政 创建时间：2017年3月6日 下午6:00:52
 */
public interface ICashierServiceAdapter extends IBaseServiceAdapter {

	/**
	 * 收银台参数校验
	 * 
	 * @param paramMap
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> checkCashierParameter(Map<String, String> paramMap, String serialNo) throws ServiceException;

	/**
	 * 收银台商户权限验证
	 * 
	 * @param merchantId
	 * @param applicationId
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> checkCashierPermission(String merchantId, String applicationId, String serialNo) throws ServiceException;

	/**
	 * 进入收银台
	 * 
	 * @param zitopayPerson
	 * @param zitopayPersonApplication
	 * @param paramMap
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> execCashier(PersonEntity zitopayPerson, PersonApplicationEntity zitopayPersonApplication, Map<String, String> paramMap, String type, String serialNo) throws ServiceException;

	/**
	 * 订单支付结果
	 * 
	 * @param orderid
	 * @return
	 */
	public Map<String, Object> payResult(String orderid);

}
