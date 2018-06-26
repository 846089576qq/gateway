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
 * 转账常量类
 * 
 */
public class TransferParamConstant extends BaseParamConstant {

	/**
	 * mer_draw_order_id 商户转账订单号
	 */
	public static final String param_merchantTransferOrderId = "mer_transfer_order_id";
	
	/**
	 * order_amt 转账金额(##.##,单位:元,zitopay_order.totalprice)
	 */
	public static final String param_orderAmount = "order_amt";

	/**
	 * order_title 订单标题(zitopay_order.ordertitle)
	 */
	public static final String param_orderTitle = "order_title";

}
