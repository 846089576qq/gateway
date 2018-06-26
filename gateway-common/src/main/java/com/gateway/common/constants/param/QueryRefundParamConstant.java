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
 * 退款查询参数
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年7月21日
 */
public class QueryRefundParamConstant extends BaseParamConstant {

	/**
	 * mer_order_id 商户退款订单号((^[a-z0-9A-Z])[a-z0-9A-Z_]+([a-z0-9-A-Z]),zitopay_order_refund.orderidinf)
	 */
	public static final String param_merchantRefundOrderId = "mer_refund_order_id";
}
