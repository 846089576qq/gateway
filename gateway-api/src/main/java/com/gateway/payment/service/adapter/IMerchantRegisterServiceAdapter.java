package com.gateway.payment.service.adapter;

import java.util.Map;

import com.zitopay.foundation.common.exception.ServiceException;

public interface IMerchantRegisterServiceAdapter {
	
	/**
	 * 校验入驻参数
	 * @param paramMap
	 * @return
	 * @throws ServiceException
	 */
	Map<String, Object> checkRegisterParameter(String method, Map<String, String> paramMap, String serialNo) throws ServiceException;
	
	/**
	 * 进行入驻处理
	 * 
	 * @param paramMap
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> execRegister(String method, Map<String, String> paramMap, String serialNo) throws ServiceException;

}
