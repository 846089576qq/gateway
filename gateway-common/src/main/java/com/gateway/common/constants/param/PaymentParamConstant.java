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
 * 支付请求参数常量类
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年1月22日
 */
public class PaymentParamConstant extends BaseParamConstant {

	/**
	 * order_time 下单时间(yyyyMMddHHmmssSSS,zitopay_order.posttime)
	 */
	public static final String param_orderTime = "order_time";

	/**
	 * order_amt 订单金额(##.##,单位:元,zitopay_order.totalprice)
	 */
	public static final String param_orderAmount = "order_amt";

	/**
	 * mer_order_id 商户订单号((^[a-z0-9A-Z])[a-z0-9A-Z_]+([a-z0-9-A-Z]),zitopay_order.orderidinf)
	 */
	public static final String param_merchantOrderId = "mer_order_id";

	/**
	 * mer_async_notify 商户异步通知地址(zitopay_order.bgreturl)
	 */
	public static final String param_merchantNotifyUrl = "mer_async_notify";

	/**
	 * mer_url 商户(商品)页面(zitopay_order.returnurl)
	 */
	public static final String param_merchantUrl = "mer_url";

	/**
	 * order_title 订单标题(zitopay_order.ordertitle)
	 */
	public static final String param_orderTitle = "order_title";

	/**
	 * open_id 用户在公众号下唯一标示(公众号支付必填)
	 */
	public static final String param_openId = "open_id";

	/**
	 * user_id 用户在支付宝下唯一标示(服务窗支付必填)
	 */
	public static final String param_userId = "user_id";

//	/**
//	 * other_official 是否使用融智付提供的公众号（1：是 0：否 0时open_id必填，1时other_official_type必填）
//	 */
//	public static final String param_other_official = "other_official";
//
//	/**
//	 * other_official_type 使用融智付提供公众号的类别（test：测试 formal：正式）
//	 */
//	public static final String param_other_official_type = "other_official_type";

	/**
	 * oper_type 操作类型（支付：pay(默认),bindCard：绑卡,unBindCard：解绑,发送短信：sendMssage,短信确认：messageConfirm）
	 */
	public static final String param_oper_type = "oper_type";

	/**
	 * pay_confirm 是否需要支付确认(短信形式)
	 */
	public static final String param_pay_confirm = "pay_confirm";

	/**
	 * verify_code 短信验证码
	 */
	public static final String param_verify_code = "verify_code";
	
	/**
	 * face_id 人脸id
	 */
	public static final String param_face_id = "face_id";
	
	/**
	 * pre_id 预授权id
	 */
	public static final String param_pre_id = "pre_id";

	/**
	 * card_type 银行卡类型（1、借记卡 2、贷记卡）
	 */
	public static final String param_card_type = "card_type";
	
	/**
	 * bank_code 银行卡简码
	 */
	public static final String param_bank_code = "bank_code";
	
	/**
	 * order_id 支付序列号(zitopay_order.order_id)
	 */
	public static final String param_order_id = "order_id";

	/**
	 * param_gateway_info gateway信息,用于返回给商户自行调用
	 */
	public static final String param_gateway_info = "gateway_info";

	/**
	 * is_our_cashier 是否融智付收银台（1：是，0：否）
	 */
	public static final String param_is_zitopay_cashier = "is_zitopay_cashier";
	
	/**
	 * param_is_show_zitopay_icon 是否显示融智付图标（1：是，0：否）
	 */
	public static final String param_is_show_zitopay_icon = "is_show_zitopay_icon";
	
	/**
	 * bar_code 通过扫码枪/声波获取设备获取
	 */
	public static final String param_bar_code = "bar_code";
	
	/**
	 * terminal_no 扫码枪序列号
	 */
	public static final String param_terminal_no = "terminal_no";
	
	/**
	 * shop_no 门店编号
	 */
	public static final String param_shop_no = "shop_no";
	
	/**
	 * cashier_no 操作员编号
	 */
	public static final String param_cashier_no = "cashier_no";
	
}
