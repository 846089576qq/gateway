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
 * 退款常量类
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年1月22日
 */
public class RefundParamConstant extends BaseParamConstant{
	
	/**
	 * order_id 融智付订单ID(zitopay_order_refund.order_id)
	 */
	public static final String param_order_id = "order_id";

	/**
	 * mer_refund_order_id 商户退款订单号((^[a-z0-9A-Z])[a-z0-9A-Z_]+([a-z0-9-A-Z]),zitopay_order_refund.orderidinf)
	 */
	public static final String param_merchantRefundOrderId = "mer_refund_order_id";
	
	/**
	 * refund_amt 退款金额(##.##,单位:元,zitopay_order_refund.amount)
	 */
	public static final String param_refundAmount = "refund_amt";
	
	/**
	 * mer_async_notify 商户异步通知地址(zitopay_order_refund.bgreturl)
	 */
	public static final String param_merchantNotifyUrl = "mer_async_notify";
	
	/**
	 * reason 原因(zitopay_order_refund.reason)
	 */
	public static final String param_reason = "reason";
	
	/**
	 * pay_no 支付流水号(zitopay_order.payno)
	 */
	public static final String param_payNo = "pay_no";

	/**
	 * top_pay_no 上游支付渠道(交易)流水号(支付成功后上游返回,zitopay_order.toppayno)
	 */
	public static final String param_topPayNo = "top_pay_no";
	
	
}
