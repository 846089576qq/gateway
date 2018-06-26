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
package com.gateway.common.constants.status;

/**
 * 提现状态常量类
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年8月4日
 */
public class WithdrawStatusConstant {

	/**
	 * 提现状态（0处理中）
	 */
	public final static String STATUS_WITHDRAWING = "0";

	/**
	 * 提现状态（1成功）
	 */
	public final static String STATUS_WITHDRAW_SUCCESS = "1";

	/**
	 * 提现状态（2失败）
	 */
	public final static String STATUS_WITHDRAW_FAILURE = "2";

}
