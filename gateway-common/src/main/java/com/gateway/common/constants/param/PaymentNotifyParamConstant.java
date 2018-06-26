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
 * 支付通知参数常量类
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年1月22日
 */
public class PaymentNotifyParamConstant extends BaseParamConstant {

	/**
	 * order_id 支付流水号(平台订单号,zitopay_order.orderId)
	 */
	public static final String param_orderid = "order_id";

	/**
	 * param_payOrderId 支付渠道订单号,用于上游对账用(zitopay_order.threeorderid)
	 */
	public static final String param_payOrderId = "pay_order_id";

	/**
	 * order_title 订单标题(zitopay_order.ordertitle)
	 */
	@Deprecated
	public static final String param_orderTitle = "order_title";

	/**
	 * order_time 下单时间(yyyyMMddHHmmssSSS,zitopay_order.posttime)
	 */
	public static final String param_orderTime = "order_time";

	/**
	 * pay_time 支付时间(yyyy-MM-dd HH:mm:ss,zitopay_order.paysucctime)
	 */
	public static final String param_payTime = "pay_time";

	/**
	 * order_amount 订单金额(##.##,单位:元,zitopay_order.totalprice)
	 */
	public static final String param_orderAmount = "order_amt";

	/**
	 * pay_amt 支付金额(##.##,单位:元,zitopay_order.amount)
	 */
	public static final String param_payAmount = "pay_amt";

	/**
	 * pay_fee 手续费(##.##,单位:元,zitopay_order.ratemmoney)
	 */
	public static final String param_payFee = "pay_fee";

	/**
	 * mer_order_id 商户订单号((^[a-z0-9A-Z])[a-z0-9A-Z_]+([a-z0-9-A-Z]),zitopay_order.orderidinf)
	 */
	public static final String param_merchantOrderId = "mer_order_id";

	/**
	 * mer_cust_id 商户客户唯一标识(协议支付取zitopay_payment_agreement.mer_cust_id,跨境取 TODO)
	 */
	@Deprecated
	public static final String param_merchantCustomerId = "mer_cust_id";

	/**
	 * cust_acct_no 商户客户支付账号(zitopay_order.accountname,银行卡支付存银行卡号)
	 */
	@Deprecated
	public static final String param_bankCardNo = "cust_acct_no";

	/**
	 * agmt_id 协议(agreement)ID(协议签约后返回,zitopay_payment_agreement.agreement_id)
	 */
	@Deprecated
	public static final String param_agreementId = "agmt_id";

	/**
	 * pay_no 支付渠道(交易)流水号(支付成功后上游返回,zitopay_order.payno)
	 */
	public static final String param_payNo = "pay_no";

	/**
	 * top_pay_no 上游支付渠道(交易)流水号(支付成功后上游返回,zitopay_order.toppayno)
	 */
	public static final String param_topPayNo = "top_pay_no";

}
