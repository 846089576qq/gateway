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
 * 商户入驻更新参数常量类
 * 
 * 作者：王政
 * 创建时间：2017年3月15日 上午10:52:18
 */
public class MerchantRegisterUpdateParamConstant {
	
	/**
	 * channel_id 通道编号
	 */
	public static final String param_gatewayId = "gateway_id";

	/**
	 * mer_name 商户名称
	 */
	public static final String param_merName = "mer_name";

	/**
	 * mer_name 商户简称
	 */
	public static final String param_merShortName = "mer_short_name";

	/**
	 * mer_address 商户地址
	 */
	public static final String param_merAddress = "mer_address";

	/**
	 * mer_contact 商户联系电话
	 */
	public static final String param_merContact = "mer_contact";

	/**
	 * bank_name 银行总行名称
	 */
	public static final String param_bankName = "bank_name";
	
	/**
	 * bank_no 银行联行号
	 */
	public static final String param_bankNo = "bank_no";
	
	/**
	 * bank_province 开户行省份
	 */
	public static final String param_bankProvince = "bank_province";
	
	/**
	 * bank_city 开户行城市
	 */
	public static final String param_bankCity = "bank_city";
	
	/**
	 * acc_type 账户类型(0-对私,1-对公)
	 */
	public static final String param_accType = "acc_type";
	
	/**
	 * card_no 银行卡号
	 */
	public static final String param_cardNo = "card_no";

	/**
	 * card_mobile 银行卡预留手机号
	 */
	public static final String param_cardMobile = "card_mobile";

	/**
	 * card_identity_code 银行卡预留身份证号
	 */
	public static final String param_cardIdentityCode = "card_identity_code";

	/**
	 * card_real_name 银行卡真实姓名
	 */
	public static final String param_cardRealName = "card_real_name";
	
	/**
	 * fix_fee 单笔手续费（固定值单位：元）
	 */
	public static final String param_fixFee = "fix_fee";
	
	/**
	 * settle_rate 结算费率（“0.003” 代表费率千分之3）
	 */
	public static final String param_settleRate = "settle_rate";
	
	/**
	 * mer_config 商户配置(json)
	 */
	public static final String param_merConfig = "mer_config";

	
}
