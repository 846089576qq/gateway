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
package com.gateway.common.constants;

/**
 * 支付跳转类型常量
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年8月4日
 */
public class ForwardConstant {

	/**
	 * 类型为支付公司返回url跳转支付公司收银台的形式
	 */
	public static final String CASHIER_URL_TYPE_THREE = "three";

	/**
	 * 微信-类型为支付公司返回二维码链接 融拓生成二维码 扫码的形式
	 */
	public static final String CASHIER_URL_TYPE_WXQRCODE = "wxqrcode";

	/**
	 * 支付宝-类型为支付公司返回二维码链接 融拓生成二维码 扫码的形式
	 */
	public static final String CASHIER_URL_TYPE_ZFBQRCODE = "zfbqrcode";
	
	/**
	 * QQ-类型为支付公司返回二维码链接 融拓生成二维码 扫码的形式
	 */
	public static final String CASHIER_URL_TYPE_QQQRCODE = "qqqrcode";
	
	/**
	 * 银联-类型为支付公司返回二维码链接 融拓生成二维码 扫码的形式
	 */
	public static final String CASHIER_URL_TYPE_YLQRCODE = "ylqrcode";

	/**
	 * 京东-类型为支付公司返回二维码链接 融拓生成二维码 扫码的形式
	 */
	public static final String CASHIER_URL_TYPE_JDQRCODE = "jdqrcode";
	
	/**
	 * 百度-类型为支付公司返回二维码链接 融拓生成二维码 扫码的形式
	 */
	public static final String CASHIER_URL_TYPE_BDQRCODE = "bdqrcode";
	
	/**
	 * 网关-类型为进入网关收银台的形式
	 */
	public static final String CASHIER_URL_TYPE_WG = "wg";
	
	/**
	 * 网关(没有图标)-类型为进入网关收银台的形式
	 */
	public static final String CASHIER_URL_TYPE_WG_NOICON = "wgNoIcon";
	
	/**
	 * 公众号-类型为支付公司返回支付数据 通过微信JSAPI调起支付
	 */
	public static final String CASHIER_URL_TYPE_WXJSAPI = "wxjsapi";

	/**
	 * 服务窗-类型为支付公司返回支付宝单号 通过支付宝JSAPI调起支付
	 */
	public static final String CASHIER_URL_TYPE_ZFBFWC = "zfbfwc";

}
