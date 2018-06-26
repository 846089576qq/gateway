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
 * 退款通知参数常量类
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年1月22日
 */
public class RefundNotifyConstant extends BaseParamConstant {
	
	/**
	 * refund_id 融智付退款订单ID(zitopay_order_refund.id)
	 */
	public static final String param_refund_id = "refund_id";

	/**
	 * order_id 融智付订单ID(zitopay_order_refund.orderid)
	 */
	public static final String param_order_id = "order_id";

	/**
	 * mer_order_id 商户退款订单号((^[a-z0-9A-Z])[a-z0-9A-Z_]+([a-z0-9-A-Z]),zitopay_order_refund.orderidinf)
	 */
	public static final String param_merchantRefundOrderId = "mer_refund_order_id";

	/**
	 * refund_amt 退款金额(##.##,单位:元,zitopay_order_refund.amount)
	 */
	public static final String param_refundAmount = "refund_amt";

	/**
	 * refund_status 退款状态
	 */
	public static final String param_refundStatus = "refund_status";
	
	/**
	 * refund_no 退款流水号
	 */
	public static final String param_refundNo = "refund_no";

	/**
	 * top_refund_no 退款流水号
	 */
	public static final String param_topRefundNo = "top_refund_no";

}
