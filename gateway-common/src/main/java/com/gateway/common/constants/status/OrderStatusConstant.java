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
 * 订单状态
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年4月6日
 */
public class OrderStatusConstant {

	/**
	 * 订单状态-初始化(0) STATUS_INITIALIZED
	 */
	public static final int STATUS_INITIALIZED = 0;

	/**
	 * 订单状态-已支付(1) STATUS_PAYED
	 */
	public static final int STATUS_PAYED = 1;

	/**
	 * 订单状态-未支付(2) STATUS_UNPAYING
	 */
	public static final int STATUS_UNPAYING = 2;

	/**
	 * 订单状态-支付失败(3) STATUS_PAYFAILED
	 */
	public static final int STATUS_PAYFAILED = 3;

	/**
	 * 订单状态-失效(4) STATUS_EXPIRED
	 */
	public static final int STATUS_EXPIRED = 4;

	/**
	 * 订单状态-已退款(5) STATUS_REFUNDED
	 */
	public static final int STATUS_REFUNDED = 5;

	/**
	 * 订单状态-部分退款(6) STATUS_PARTREFUNDED
	 */
	public static final int STATUS_REFUNDEDPART = 6;

}
