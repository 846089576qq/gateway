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
package com.gateway.common.constants;

/**
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年8月10日
 */
public class SchedulerTaskConstant {

	/**
	 * PAYMENTNOTIFY_SCHEDULER 异步通知分布式锁key
	 */
	public static final String PAYMENTNOTIFY_SCHEDULER = "PAYMENTNOTIFYSCHEDULER_KEY";

	/**
	 * UNPAYINGORDERSCHEDULER 未支付订单轮询上游分布式锁key
	 */
	public static final String UNPAYINGORDERSCHEDULER = "UNPAYINGORDERSCHEDULER_KEY";

	/**
	 * ISSUEDINGSCHEDULER 代付审核中轮询上游分布式锁key
	 */
	public static final String ISSUEDINGSCHEDULER = "ISSUEDINGSCHEDULER_KEY";

	/**
	 * EXPIREDORDERSCHEDULER 失效订单轮询上游分布式锁key
	 */
	public static final String EXPIREDORDERSCHEDULER = "EXPIREDORDERSCHEDULER_KEY";

	/**
	 * UNPAYING2EXPIREDORDERSCHEDULER 未支付订单设置为失效分布式锁key
	 */
	public static final String UNPAYING2EXPIREDORDERSCHEDULER = "UNPAYING2EXPIREDORDERSCHEDULER_KEY";

	/**
	 * INITIAL2FAILUREORDERSCHEDULER 初始化订单设置为支付失败分布式锁key
	 */
	public static final String INITIAL2FAILUREORDERSCHEDULER = "INITIAL2FAILUREORDERSCHEDULER_KEY";

}
