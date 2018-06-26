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
 * 商户回调常量
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
public class BgReturnConstant {

	/**
	 * TIMERS_LIMIT-回调次数上限
	 */
	public final static int NOTIFY_LIMIT = 10;

	/**
	 * UNFEEDBACK-回调未反馈
	 */
	public final static int UNFEEDBACK = 0;

	/**
	 * FEEDBACKED-回调已反馈
	 */
	public final static int FEEDBACKED = 1;

	/**
	 * BGRETURN_TYPE_PAYMENT-支付回调
	 */
	public final static int BGRETURN_TYPE_PAYMENT = 0;

	/**
	 * BGRETURN_TYPE_REFUND-退款回调
	 */
	public final static int BGRETURN_TYPE_REFUND = 1;

}
