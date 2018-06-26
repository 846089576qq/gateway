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
 * 转账返回参数常量类
 * 
 */
public class TransferResultParamConstant {
	
	/**
	 * transfer_id 支付流水号(平台转账订单号)
	 */
	public static final String param_transferid = "transfer_id";

	/**
	 * status 状态返回参数是否转账成功  
	 */
	public static final String status = "status";
	
	/**
	 * success -返回状态   转账成功
	 */
	public static final String success = "success";
	
	/**
	 * failure -返回状态   转账成失败
	 */
	public static final String failure = "failure";
	
	/**
	 * paying - 返回状态   转账中
	 */
	public static final String transfering = "transfering";


}
