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
package com.gateway.common.constants.param;

/**
 * 支付方法常量类
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年1月22日
 */
public class PaymentMethodConstant {

	/**
	 * 支付方法类型（支付）
	 */
	public final static String param_operate_pay = "pay";
	
	/**
	 * 支付方法类型（人脸认证）
	 */
	public final static String param_operate_facecheck = "facecheck";
	
	/**
	 * 支付方法类型（人脸认证查询）
	 */
	public final static String param_operate_facecheckquery = "facecheckquery";
	
	/**
	 * 支付方法类型（绑卡）
	 */
	public final static String param_operate_bindcard = "bindcard";
	
	/**
	 * 支付方法类型（绑卡确认）
	 */
	public final static String param_operate_bindcardconfirm = "bindcardConfirm";
	
	/**
	 * 支付方法类型（解绑）
	 */
	public final static String param_operate_unbindcard = "unbindcard";

	/**
	 * 支付方法类型（发送短信）
	 */
	public final static String param_operate_sendmessage = "sendMessage";

	/**
	 * 支付方法类型（短信确认）
	 */
	public final static String param_operate_messageconfirm = "messageConfirm";

	/**
	 * 支付方法类型（获取银行卡列表）
	 */
	public final static String param_operate_banklist = "banklist";

}
