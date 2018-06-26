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
 * 通道查询常量类
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年1月22日
 */
public class QueryChannelParamConstant extends BaseParamConstant {

	/**
	 * order_id 融智付订单ID(zitopay_order.order_id)
	 */
	public static final String param_order_id = "order_id";

	/**
	 * cashier 融智付收银台前台传过来的通道号保存字段
	 */
	public static final String cashier_param_gatewayId = "gatewayid";

}
