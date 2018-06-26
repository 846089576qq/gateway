package com.gateway.payment.service.adapter;

import java.util.Map;

import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 融智付服务接口
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年1月20日
 */
public interface IZitopayMerchantRegisterServiceAdapter extends IBaseServiceAdapter {

	/**
	 * 校验支付参数
	 * @param method
	 * @param paramMap
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	Map<String, Object> checkRegisterParameter(String method, Map<String, String> paramMap, String serialNo) throws ServiceException;

	/**
	 * @param method
	 * @param paramMap
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	Map<String, Object> checkRegisterPermission(String method, Map<String, String> paramMap, String serialNo) throws ServiceException;

	/**
	 * 进行入驻处理
	 * 
	 * @param method
	 * @param paramMap
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> execRegister(String method, Map<String, String> paramMap, Map<String, Object> permissionMap, String serialNo) throws ServiceException;

}
