/**
 * Copyright [2015-2017]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.gateway.payment.service;

import java.util.Map;

import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 融智付基类服务接口
 * 
 * 作者：王政 创建时间：2017年3月8日 上午11:53:10
 */
public interface IBaseService {

	/**
	 * NOTIFY_RECEIPT_UPPERCASE 商户回执
	 */
	String NOTIFY_RECEIPT_UPPERCASE = "SUCCESS";

	/**
	 * NOTIFY_RECEIPT_LOWERCASE 商户回执
	 */
	String NOTIFY_RECEIPT_LOWERCASE = "success";

	/**
	 * BEAN_PREFIX_PAY 支付bean前缀
	 */
	String BEAN_PREFIX_PAY = "pay_";

	/**
	 * BEAN_PREFIX_DRAW 提现bean前缀
	 */
	String BEAN_PREFIX_DRAW = "draw_";
	
	/**
	 * BEAN_PREFIX_TRANSFER 转账bean前缀
	 */
	String BEAN_PREFIX_TRANSFER = "transfer_";
	
	/**
	 * BEAN_PREFIX_ISSUED 代付bean前缀
	 */
	String BEAN_PREFIX_ISSUED = "issued_";

	/**
	 * BEAN_PREFIX_REFUND 退款bean前缀
	 */
	String BEAN_PREFIX_REFUND = "refund_";

	/**
	 * 获取商户公钥
	 * 
	 * @param merchantId
	 *            商户id
	 * @param serialNo
	 *            报文序列号
	 * @return
	 * @throws ServiceException
	 */
	public String getMerchantPublicKey(String merchantId, String serialNo) throws ServiceException;

	/**
	 * 校验参数
	 * 
	 * @param map
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> checkParameter(Map<String, String> map, String serialNo) throws ServiceException;

	/**
	 * 校验权限
	 * 
	 * @param merchantId
	 * @param applicationId
	 * @param gatewayId
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> checkPermission(String merchantId, String applicationId, String gatewayId, String serialNo) throws ServiceException;

}
