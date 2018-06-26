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
 * 退款订单状态
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年4月6日
 */
public class RefundOrderStatusConstant {

	/**
	 * 退款订单状态-退款已处理(3) STATUS_PROCESSING
	 */
	public static final int STATUS_PROCESSING = 3;

	/**
	 * 退款订单状态-初始化(2) STATUS_INITIALIZED
	 */
	public static final int STATUS_INITIALIZED = 2;

	/**
	 * 订单状态-已退款(1) STATUS_REFUNDED
	 */
	public static final int STATUS_REFUNDED = 1;

	/**
	 * 订单状态-失败(0) STATUS_REFUNDFAIL
	 */
	public static final int STATUS_REFUNDFAILED = 0;

}
