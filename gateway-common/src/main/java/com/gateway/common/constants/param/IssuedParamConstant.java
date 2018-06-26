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
 * 代付常量类
 * 
 */
public class IssuedParamConstant extends BaseParamConstant {
	
	/**
	 * issued_id 支付序列号
	 */
	public static final String param_issued_id = "issued_id";

	/**
	 * mer_order_id 商户代付订单号
	 */
	public static final String param_merchantOrderId = "mer_order_id";
	
	/**
	 * order_amt 转账金额(##.##,单位:元,zitopay_order.totalprice)
	 */
	public static final String param_orderAmount = "order_amt";

	/**
	 * cust_bank_name 银行总行名称
	 */
	public static final String param_bankName = "cust_bank_name";
	
	/**
	 * cust_branch_bank_name 银行支行名称
	 */
	public static final String param_branchBankName = "cust_branch_bank_name";

	/**
	 * cust_card_no 银行卡号(鉴权要素之一)
	 */
	public static final String param_custCardNo = "cust_card_no";
	
	/**
	 * cust_card_mobile 银行预留手机号(鉴权要素之一)
	 */
	public static final String param_custCardMobile = "cust_card_mobile";

	/**
	 * cust_identity_code 客户身份证号(鉴权要素之一,zitopay_payment_agreement.cust_identity_code)
	 */
	public static final String param_custIdentityCode = "cust_identity_code";

	/**
	 * cust_name 客户真实姓名(鉴要素之一,zitopay_payment_agreement.cust_name)
	 */
	public static final String param_custRealName = "cust_name";
	
	/**
	 * bank_city 开户行城市
	 */
	public static final String param_bankCity = "bank_city";
	
	/**
	 * bank_province 开户行省份
	 */
	public static final String param_bankProvince = "bank_province";

}
