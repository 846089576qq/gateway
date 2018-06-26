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
 * 支付协议参数常量类
 * 
 * 作者：王政
 * 创建时间：2017年3月15日 上午10:52:18
 */
public class PaymentAgreementParamConstant{
	
	/**
	 * mer_cust_id 商户客户唯一标识(zitopay_payment_agreement.mer_cust_id)
	 */
	public static final String param_merchantCustomerId = "mer_cust_id";

	/**
	 * cust_card_type 客户卡类型(0-借记卡,1-贷记卡,zitopay_payment_agreement.cust_card_type)
	 */
	public static final String param_customerCardType = "cust_card_type";
	
	/**
	 * cust_bank_name 银行总行名称
	 */
	public static final String param_bankName = "cust_bank_name";
	
	/**
	 * cust_branch_bank_name 银行支行名称
	 */
	public static final String param_branchBankName = "cust_branch_bank_name";

	/**
	 * cust_card_no 银行卡号(鉴权要素之一,zitopay_payment_agreement.cust_card_no)
	 */
	public static final String param_bankCardNo = "cust_card_no";
	
	/**
	 * cust_bank_code 银行简码(鉴权要素之一,zitopay_payment_agreement.cust_bank_code)
	 */
	public static final String param_bankCode = "cust_bank_code";

	/**
	 * cust_contact 客户联系方式(银行预留手机号,鉴权要素之一,zitopay_payment_agreement.cust_contact)
	 */
	public static final String param_customerContact = "cust_contact";

	/**
	 * cust_identity_code 客户身份证号(鉴权要素之一,zitopay_payment_agreement.cust_identity_code)
	 */
	public static final String param_customerIdentityCode = "cust_identity_code";

	/**
	 * cust_name 客户真实姓名(鉴要素之一,zitopay_payment_agreement.cust_name)
	 */
	public static final String param_customerRealName = "cust_name";

	/**
	 * credit_valid_date 信用卡过期时间(鉴要素之一,zitopay_payment_agreement.credit_valid_date)
	 */
	public static final String param_bankCardValidDate = "credit_valid_date";

	/**
	 * cust_credit_cvv2 信用卡安全码(鉴权要素之一,zitopay_payment_agreement.cust_credit_cvv2)
	 */
	public static final String param_bankCardCVV2 = "cust_credit_cvv2";

	/**
	 * agreement_id 支付协议编号(zitopay_payment_agreement.agreement_id)
	 */
	public static final String param_agreementId = "agreement_id";
	
}
