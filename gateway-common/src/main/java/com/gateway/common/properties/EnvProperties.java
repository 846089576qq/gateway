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
package com.gateway.common.properties;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月8日
 */
@Component
public class EnvProperties {

	/**
	 * PREFIX_GATEWAY_URL-支付网关请求url前缀
	 */
	@Value("${prefix.url.gateway}")
	public String PREFIX_GATEWAY_URL;

	@Value("${prefix.url.notify}")
	public String PREFIX_NOTIFY_URL;
	
	/**
	 * 获取前台URL
	 * 
	 * @param merchantId
	 * @param applicationId
	 * @param gatewayId
	 * @param serialNo
	 * @return
	 */
	public String getReturnUrl(Object orderid) {
		Assert.notNull(PREFIX_GATEWAY_URL, "Notify URL can't be null!");
		Assert.notNull(orderid, "orderid can't be null!");
		return MessageFormat.format("{0}/cashier/payresult/{1}", PREFIX_GATEWAY_URL, String.valueOf(orderid));
	}

	/**
	 * 获取通知URL
	 * 
	 * @param merchantId
	 * @param applicationId
	 * @param gatewayId
	 * @param serialNo
	 * @return
	 */
	public String getNotifyUrl(Object merchantId, Object applicationId, Object gatewayId, String serialNo) {
		Assert.notNull(PREFIX_NOTIFY_URL, "Notify URL can't be null!");
		Assert.notNull(merchantId, "Merchant number can't be null!");
		Assert.notNull(applicationId, "Application number can't be null!");
		Assert.notNull(gatewayId, "Gateway number can't be null!");
		Assert.notNull(serialNo, "Trade serial number can't be null!");
		return MessageFormat.format("{0}/payment/success/{1}/{2}/{3}/{4}", PREFIX_NOTIFY_URL, String.valueOf(merchantId), String.valueOf(applicationId), String.valueOf(gatewayId),String.valueOf(serialNo));
	}
	
	/**
	 * 获取退款回调通知URL
	 * 
	 * @param merchantId
	 * @param applicationId
	 * @param gatewayId
	 * @param serialNo
	 * @return
	 */
	public String getRefundNotifyUrl(Object merchantId, Object applicationId, Object gatewayId, String serialNo) {
		Assert.notNull(PREFIX_NOTIFY_URL, "Notify URL can't be null!");
		Assert.notNull(merchantId, "Merchant number can't be null!");
		Assert.notNull(applicationId, "Application number can't be null!");
		Assert.notNull(gatewayId, "Gateway number can't be null!");
		Assert.notNull(serialNo, "Trade serial number can't be null!");
		return MessageFormat.format("{0}/refund/success/{1}/{2}/{3}/{4}", PREFIX_NOTIFY_URL, String.valueOf(merchantId), String.valueOf(applicationId), String.valueOf(gatewayId),String.valueOf(serialNo));
	}



	/**
	 * 获取表单URL
	 * 
	 * @param formParam
	 * @return
	 */
	public String getPaymentFormUrl(Object formParam) {
		Assert.notNull(PREFIX_GATEWAY_URL, "Gageway URL can't be null!");
		Assert.notNull(formParam, "Form parameters can't be null!");
		return MessageFormat.format("{0}/payment/form?{1}", PREFIX_GATEWAY_URL, String.valueOf(formParam));
	}
}
