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
 * 支付返回参数常量类
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年1月22日
 */
public class PaymentResultParamConstant {

	/**
	 * order_id 支付流水号(平台订单号,zitopay_order.orderId)
	 */
	public static final String param_orderid = "order_id";

	/**
	 * param_url url(网关(快捷)跳转url和二维码url)
	 */
	public static final String param_url = "url";
	
	/**
	 * pay_status 条码返回参数是否支付成功  
	 */
	public static final String pay_status = "pay_status";
	
	/**
	 * success -条码 返回状态   支付成功
	 */
	public static final String success = "success";
	
	/**
	 * failure -条码 返回状态   支付成失败
	 */
	public static final String failure = "failure";
	
	/**
	 * paying - 条码 返回状态   支付中
	 */
	public static final String paying = "paying";

	/**
	 * pay_data 调起支付数据
	 */
	public static final String param_pay_data_wechat = "pay_data_wechat";
	
	/**
	 * pay_data 调起支付数据
	 */
	public static final String param_pay_data_alipay = "pay_data_alipay";

	/**
	 * agreement_id 支付协议编号
	 */
	public static final String param_agreementId = "agreement_id";
	
	/**
	 * cardlist 银行卡列表
	 */
	public static final String param_cardlist = "cardlist";
	
	/**
	 * face_id 人脸id
	 */
	public static final String param_face_id = "face_id";
	
	/**
	 * pre_id 预授权id
	 */
	public static final String param_pre_id = "pre_id";

}
