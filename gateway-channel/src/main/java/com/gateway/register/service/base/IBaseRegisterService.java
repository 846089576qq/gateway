package com.gateway.register.service.base;

import java.util.Map;

import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 入驻接口基类
 */
public interface IBaseRegisterService {
	/**
	 * 入驻操作接口
	 * 
	 * @param personEntity
	 * @param paramMap
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> register(Map<String, String> paramMap, String serialNo) throws ServiceException;
	
	/**
	 * 入驻更新接口
	 * 
	 * @param personEntity
	 * @param paramMap
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> update(Map<String, String> paramMap, String serialNo) throws ServiceException;

	/**
	 * 入驻查询接口
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> query(Map<String, String> paramMap, String serialNo) throws ServiceException;

}
