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
package com.gateway.common.constants.config;

import com.gateway.common.constants.ForwardConstant;
import com.zitopay.foundation.common.util.StringUtils;

/**
 * 通道编号配置类V2.0</br> 通道编号规则: 2位通道编号+2位通道子编号4位长度数字组成。例如厦门民生银行支付宝扫码编号为1001,10表示厦门民生银行通道,01表示支付宝扫码子通道。
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年4月6日
 */
public class GatewayConstants {
	
	/**
	 * pos下单-99 GATEWAY_POS_WX_ZFB_SM
	 */
	public static final String GATEWAY_POS_WX_ZFB_SM = "99";

	/**
	 * 支付宝官方扫码通道-1001 GATEWAY_GF_ZFB_SM
	 */
	public static final String GATEWAY_GF_ZFB_SM = "13_1001";

	/**
	 * 支付宝官方手机网站通道-1002 GATEWAY_GF_ZFB_MOBILE_WAP
	 */
	public static final String GATEWAY_GF_ZFB_MOBILE_WAP = "22_1002";

	/**
	 * 支付宝官方手机APP通道-1003 GATEWAY_GF_ZFB_APP
	 */
	public static final String GATEWAY_GF_ZFB_APP = "7_1003";

	/**
	 * 支付宝官方条码通道-1004 GATEWAY_GF_ZFB_TM
	 */
	public static final String GATEWAY_GF_ZFB_TM = "15_1004";

	/**
	 * 支付宝官方电脑网站通道-1005 GATEWAY_GF_ZFB_PC_WAP
	 */
	public static final String GATEWAY_GF_ZFB_PC_WAP = "1005";

	/**
	 * 微信官方扫码通道-1011 GATEWAY_GF_WX_SM
	 */
	public static final String GATEWAY_GF_WX_SM = "4_1011";

	/**
	 * 微信官方条码通道-1012 GATEWAY_GF_WX_TM
	 */
	public static final String GATEWAY_GF_WX_TM = "27_1012";

	/**
	 * 微信官方公众号支付通道-1013 GATEWAY_GF_WX_GZH
	 */
	public static final String GATEWAY_GF_WX_GZH = "5_1013";

	/**
	 * 微信官方APP通道-1014 GATEWAY_GF_WX_APP
	 */
	public static final String GATEWAY_GF_WX_APP = "8_1014";

	/**
	 * 兴业银行支付宝扫码通道-1101 GATEWAY_XYYH_ZFB_SM
	 */
	public static final String GATEWAY_XYYH_ZFB_SM = "56_1101";

	/**
	 * 兴业银行微信扫码通道-1102 GATEWAY_XYYH_WX_SM
	 */
	public static final String GATEWAY_XYYH_WX_SM = "57_1102";

	/**
	 * 兴业银行微信公众号通道-1103 GATEWAY_XYYH_WX_OF
	 */
	public static final String GATEWAY_XYYH_WX_OF = "58_1103";

	/**
	 * 兴业银行支付宝条码
	 */
	public static final String GATEWAY_XYYH_ZFB_TM = "63_1104";

	/**
	 * 兴业银行微信条码
	 */
	public static final String GATEWAY_XYYH_WX_TM = "62_1105";

	/**
	 * 兴业银行支付宝H5
	 */
	public static final String GATEWAY_XYYH_ZFB_H5 = "78_1106";

	/**
	 * 兴业银行微信APP
	 */
	public static final String GATEWAY_XYYH_WX_APP = "60_1107";
	
	/**
	 * 兴业银行支付宝服务窗
	 */
	public static final String GATEWAY_XYYH_ZFB_FWC = "1108";

	/**
	 * 联动优势代扣通道-1201 GATEWAY_LD_DK
	 */
	public static final String GATEWAY_LD_DK = "1201";

	/**
	 * 银生宝委托代扣通道-1301 GATEWAY_YSB_WT_DK
	 */
	public static final String GATEWAY_YSB_WT_DK = "1301";

	/**
	 * 银生宝网关通道-1302 GATEWAY_YSB_WG
	 */
	public static final String GATEWAY_YSB_WG = "36_1302";

	/**
	 * 银生宝实时代扣通道-1303 GATEWAY_YSB_SS_DK
	 */
	public static final String GATEWAY_YSB_SS_DK = "1303";

	/**
	 * 盛付通快捷通道-1401 GATEWAY_SFT_KJ
	 */
	public static final String GATEWAY_SFT_KJ = "1401";

	/**
	 * 盛付通网关通道-1402 GATEWAY_SFT_WG
	 */
	public static final String GATEWAY_SFT_WG = "1402";

	/**
	 * 盛付通代扣通道-1403 GATEWAY_SFT_DK
	 */
	public static final String GATEWAY_SFT_DK = "1403";

	/**
	 * 盛付通网关直连通道-1404 GATEWAY_SFT_WG_ZL
	 */
	public static final String GATEWAY_SFT_WG_ZL = "1404";

	/**
	 * 华势在线微信扫码
	 */
	public static final String GATEWAY_HSZX_WX_SM = "68_1501";

	/**
	 * 华势在线支付宝扫码
	 */
	public static final String GATEWAY_HSZX_ZFB_SM = "67_1502";

	/**
	 * 华势在线QQ扫码
	 */
	public static final String GATEWAY_HSZX_QQ_SM = "80";
	
	/**
	 * 华势在线QQH5
	 */
	public static final String GATEWAY_HSZX_QQ_H5 = "132";
	
	/**
	 * 华势在线网银
	 */
	public static final String GATEWAY_HSZX_WY = "66_1503";
	
	/**
	 * 华势在线京东钱包H5
	 */
	public static final String GATEWAY_HSZX_JD_H5 = "1504";
	
	/**
	 * 华势在线京东钱包扫码
	 */
	public static final String GATEWAY_HSZX_JD_SM = "114";

	/**
	 * 民生银行支付宝扫码
	 */
	public static final String GATEWAY_MSYH_ZFB_SM = "40_1601";

	/**
	 * 民生银行微信扫码
	 */
	public static final String GATEWAY_MSYH_WX_SM = "41_1602";

	/**
	 * 民生银行支付宝H5
	 */
	public static final String GATEWAY_MSYH_ZFB_H5 = "77_1603";

	/**
	 * 民生银行微信公众号
	 */
	public static final String GATEWAY_MSYH_WX_GZH = "42_1604";

	/**
	 * 民生银行微信条码
	 */
	public static final String GATEWAY_MSYH_WX_TM = "47_1605";

	/**
	 * 民生银行支付宝条码
	 */
	public static final String GATEWAY_MSYH_ZFB_TM = "50_1606";

	/**
	 * 民生银行支付宝服务窗
	 */
	public static final String GATEWAY_MSYH_ZFB_FWC = "52_1607";

	/**
	 * 平安银行微信扫码
	 */
	public static final String GATEWAY_PAYH_WX_SM = "74_1701";

	/**
	 * 平安银行支付宝扫码
	 */
	public static final String GATEWAY_PAYH_ZFB_SM = "75_1702";

	/**
	 * 平安银行微信公众号
	 */
	public static final String GATEWAY_PAYH_WX_GZH = "73_1703";

	/**
	 * 平安微信条码支付(HV微信条码支付 )
	 */
	public static final String GATEWAY_PAYH_WX_TM = "92";

	/**
	 * 平安支付宝条码支付(HV支付宝条码支付)
	 */
	public static final String GATEWAY_PAYH_ZFB_TM = "93";

	/**
	 * 平安支付宝H5支付(HV支付宝H5支付)
	 */
	public static final String GATEWAY_PAYH_ZFB_H5 = "101";

	/**
	 * 平安银行微信APP
	 */
	public static final String GATEWAY_PAYH_WX_APP = "1704";

	/**
	 * 京东快捷支付(PC端)
	 */
	public static final String GATEWAY_JD_KJ_PC = "10_2001";

	/**
	 * 京东快捷支付(H5端)
	 */
	public static final String GATEWAY_JD_KJ_H5 = "2002";

	/**
	 * 京东网关支付(非直连)
	 */
	public static final String GATEWAY_JD_WG_FZL = "14_2003";

	/**
	 * 京东网关支付(直连)
	 */
	public static final String GATEWAY_JD_WG_ZL = "2004";

	/**
	 * 富友支付宝扫码支付
	 */
	public static final String GATEWAY_FY_ZFB_SM = "33_2101";

	/**
	 * 富友微信支付扫码支付
	 */
	public static final String GATEWAY_FY_WX_SM = "38_2102";

	/**
	 * 富友通用固定码支付
	 */
	@Deprecated
	public static final String GATEWAY_FY_GDM_ZF = "2103";

	/**
	 * 富友微信公众号支付
	 */
	public static final String GATEWAY_FY_WX_GZH = "31_2104";

	/**
	 * 富友支付宝服务窗支付
	 */
	public static final String GATEWAY_FY_ZFB_FWC = "70_2105";

	/**
	 * 富友支付宝条码支付
	 */
	public static final String GATEWAY_FY_ZFB_TM = "46_2106";

	/**
	 * 富友微信条码支付
	 */
	public static final String GATEWAY_FY_WX_TM = "45_2107";

	/**
	 * SC天下网关(银联)
	 */
	public static final String GATEWAY_TX_WG = "71_2301";

	/**
	 * SC天下网关(有融智付图标)
	 */
	public static final String GATEWAY_TX_ZITOPAY_WG = "72_2302";

	/**
	 * SC天下网关(无融智付图标)
	 */
	public static final String GATEWAY_TX_NO_ZITOPAY_WG = "79_2303";

	/**
	 * 易生华势微信扫码
	 */
	public static final String GATEWAY_YSHS_WX_SM = "2501";

	/**
	 * 易生华势支付宝扫码
	 */
	public static final String GATEWAY_YSHS_ZFB_SM = "2502";

	/**
	 * 易生华势微信条码
	 */
	public static final String GATEWAY_YSHS_WX_TM = "2503";

	/**
	 * 易生华势支付宝条码
	 */
	public static final String GATEWAY_YSHS_ZFB_TM = "2504";

	/**
	 * 易生华势微信公众号
	 */
	public static final String GATEWAY_YSHS_WX_GZH = "2505";

	/**
	 * 易生华势支付宝服务窗
	 */
	public static final String GATEWAY_YSHS_ZFB_FWC = "2506";

	/**
	 * 银联快捷支付
	 */
	public static final String GATEWAY_TL_KJ = "129_2601";

	/**
	 * ZR衫德网关(有融智付图标)
	 */
	public static final String GATEWAY_SD_ZITOPAY_WG = "2701";

	/**
	 * ZR衫德网关(无融智付图标)
	 */
	public static final String GATEWAY_SD_NO_ZITOPAY_WG = "2702";

	/**
	 * ZR衫德网关(第三方收银台)
	 */
	public static final String GATEWAY_SD_WG = "2703";

	/**
	 * 易生快捷支付(有积分API版)
	 */
	public static final String GATEWAY_YS_KJ_INTEGRAL_API = "116_2801";

	/**
	 * 智慧网关(有融智付图标)
	 */
	public static final String GATEWAY_ZH_ZITOPAY_WG = "2901";

	/**
	 * 智慧网关(无融智付图标)
	 */
	public static final String GATEWAY_ZH_NO_ZITOPAY_WG = "2902";

	/**
	 * 九派网关(无融智付logo收银台)
	 */
	public static final String GATEWAY_JP_NO_ZITOPAY_WG = "128_3001";
	
	/**
	 * 九派人脸快捷
	 */
	public static final String GATEWAY_JP_KJ = "3002";

	/**
	 * TM云E付网关(有融智付图标)
	 */
	public static final String GATEWAY_YEF_ZITOPAY_WG = "3101";

	/**
	 * TM云E付网关(无融智付图标)
	 */
	public static final String GATEWAY_YEF_NO_ZITOPAY_WG = "3102";
	
	/**
	 * TM云E付网关(第三方收银台)
	 */
	public static final String GATEWAY_YEF_WG = "3103";
	
	/**
	 * TM云E付快捷
	 */
	public static final String GATEWAY_YWF_KJ = "3104";
	
	/**
	 * TM云E付银联扫码
	 */
	public static final String GATEWAY_YEF_YL_SM = "3105";

	/**
	 * 光大QQ钱包扫码支付
	 */
	public static final String GATEWAY_GD_QQ_SM = "103";

	/**
	 * 光大QQ钱包H5支付
	 */
	public static final String GATEWAY_GD_QQ_H5 = "130";

	/**
	 * 光大支付宝扫码支付
	 */
	public static final String GATEWAY_GD_ZFB_SM = "107";

	/**
	 * 光大微信扫码支付
	 */
	public static final String GATEWAY_GD_WX_SM = "106";

	/**
	 * 光大支付宝条码支付
	 */
	public static final String GATEWAY_GD_ZFB_TM = "109";

	/**
	 * 光大GO支付宝条码支付
	 */
	public static final String GATEWAY_GD_WX_TM = "108";

	/**
	 * 光大微信公众号支付
	 */
	public static final String GATEWAY_GD_WX_GZH = "110";

	/**
	 * 统统付网关(融智付logo收银台)
	 */
	public static final String GATEWAY_TTF_THREE_WG = "117";

	/**
	 * 统统付网关(第三方收银台)
	 */
	public static final String GATEWAY_TTF_ZITOPAY_WG = "113";

	/**
	 * 统统付网关(无融智付logo收银台)
	 */
	public static final String GATEWAY_TTF_NO_ZITOPAY_WG = "118";

	/**
	 * 易势快捷
	 */
	public static final String GATEWAY_YS_KJ = "3201";
	
	/**
	 * 易收付网关(无融智付logo收银台)
	 */
	public static final String GATEWAY_YSF_NO_ICON_WG = "3301";
	
	/**
	 * 易收付网关2(无融智付logo收银台)
	 */
	public static final String GATEWAY_YSF_NO_ICON_WG_2 = "3302";
	
	/**
	 * 易收付网关3(无融智付logo收银台)
	 */
	public static final String GATEWAY_YSF_NO_ICON_WG_3 = "3303";

	/**
	 * 通道url枚举配置（用于收银台页面配置）
	 * 
	 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
	 * @since 2016年12月27日
	 */
	public static enum GatewayURL {
		/**
		 * 支付宝官方扫码通道-1001
		 */
		gateway_13_1001(ForwardConstant.CASHIER_URL_TYPE_ZFBQRCODE),
		/**
		 * 微信官方扫码通道-1011
		 */
		gateway_4_1011(ForwardConstant.CASHIER_URL_TYPE_WXQRCODE),
		/**
		 * 兴业银行支付宝扫码通道-1101
		 */
		gateway_56_1101(ForwardConstant.CASHIER_URL_TYPE_ZFBQRCODE),
		/**
		 * 兴业银行微信扫码通道-1102
		 */
		gateway_57_1102(ForwardConstant.CASHIER_URL_TYPE_WXQRCODE),
		/**
		 * 银生宝网关通道-1302
		 */
		gateway_36_1302(ForwardConstant.CASHIER_URL_TYPE_THREE),
		/**
		 * 盛付通网关通道-1402
		 */
		gateway_1402(ForwardConstant.CASHIER_URL_TYPE_THREE),
		/**
		 * 盛付通网关直联
		 */
		gateway_1404(ForwardConstant.CASHIER_URL_TYPE_THREE),
		/**
		 * 华势在线微信扫码
		 */
		gateway_68_1501(ForwardConstant.CASHIER_URL_TYPE_WXQRCODE),
		/**
		 * 华势在线支付宝扫码
		 */
		gateway_67_1502(ForwardConstant.CASHIER_URL_TYPE_ZFBQRCODE),
		/**
		 * 华势在线QQ扫码
		 */
		gateway_80(ForwardConstant.CASHIER_URL_TYPE_QQQRCODE),
		/**
		 * 华势在线京东扫码
		 */
		gateway_114(ForwardConstant.CASHIER_URL_TYPE_JDQRCODE),
		/**
		 * 华势在线网银
		 */
		gateway_66_1503(ForwardConstant.CASHIER_URL_TYPE_THREE),
		/**
		 * 民生支付宝扫码
		 */
		gateway_40_1601(ForwardConstant.CASHIER_URL_TYPE_ZFBQRCODE),
		/**
		 * 民生微信扫码
		 */
		gateway_41_1602(ForwardConstant.CASHIER_URL_TYPE_WXQRCODE),

		/**
		 * 平安微信扫码
		 */
		gateway_74_1701(ForwardConstant.CASHIER_URL_TYPE_WXQRCODE),

		/**
		 * 平安支付宝扫码
		 */
		gateway_75_1702(ForwardConstant.CASHIER_URL_TYPE_ZFBQRCODE),

		/**
		 * 富友支付宝扫码
		 */
		gateway_33_2101(ForwardConstant.CASHIER_URL_TYPE_ZFBQRCODE),

		/**
		 * 富友微信扫码
		 */
		gateway_38_2102(ForwardConstant.CASHIER_URL_TYPE_WXQRCODE),

		/**
		 * 天下网关
		 */
		gateway_71_2301(ForwardConstant.CASHIER_URL_TYPE_THREE),

		/**
		 * 天下网关(有融智付图标)
		 */
		gateway_72_2302(ForwardConstant.CASHIER_URL_TYPE_WG),

		/**
		 * 天下网关(无融智付图标)
		 */
		gateway_79_2303(ForwardConstant.CASHIER_URL_TYPE_WG_NOICON),

		/**
		 * 易生华势微信扫码
		 */
		gateway_2501(ForwardConstant.CASHIER_URL_TYPE_WXQRCODE),

		/**
		 * 易生华势支付宝扫码
		 */
		gateway_2502(ForwardConstant.CASHIER_URL_TYPE_ZFBQRCODE),

		/**
		 * 衫德网关(有融智付图标)
		 */
		gateway_2701(ForwardConstant.CASHIER_URL_TYPE_WG),

		/**
		 * 衫德网关(无融智付图标)
		 */
		gateway_2702(ForwardConstant.CASHIER_URL_TYPE_WG_NOICON),

		/**
		 * 衫德网关(第三方收银台)
		 */
		gateway_2703(ForwardConstant.CASHIER_URL_TYPE_THREE),

		/**
		 * 智慧网关(有融智付图标)
		 */
		gateway_2901(ForwardConstant.CASHIER_URL_TYPE_WG),

		/**
		 * 智慧网关(无融智付图标)
		 */
		gateway_2902(ForwardConstant.CASHIER_URL_TYPE_WG_NOICON),

		/**
		 * 九派网银
		 */
		gateway_128_3001(ForwardConstant.CASHIER_URL_TYPE_WG_NOICON),

		/**
		 * TM云E付网关(有融智付图标)
		 */
		gateway_3101(ForwardConstant.CASHIER_URL_TYPE_WG),

		/**
		 * TM云E付网关(无融智付图标)
		 */
		gateway_3102(ForwardConstant.CASHIER_URL_TYPE_WG_NOICON),

		/**
		 * TM云E付网关(第三方收银台)
		 */
		gateway_3103(ForwardConstant.CASHIER_URL_TYPE_THREE),
		
		/**
		 * TM云E付快捷
		 */
		gateway_3104(ForwardConstant.CASHIER_URL_TYPE_THREE),
		
		/**
		 * TM云E付银联扫码
		 */
		gateway_3105(ForwardConstant.CASHIER_URL_TYPE_YLQRCODE),

		/**
		 * 光大QQ钱包扫码
		 */
		gateway_103(ForwardConstant.CASHIER_URL_TYPE_WG_NOICON),
		/**
		 * 光大微信扫码
		 */
		gateway_106(ForwardConstant.CASHIER_URL_TYPE_WXQRCODE),
		/**
		 * 光大支付宝扫码
		 */
		gateway_107(ForwardConstant.CASHIER_URL_TYPE_ZFBQRCODE),

		/**
		 * 统统付网关(第三方收银台)
		 */
		gateway_113(ForwardConstant.CASHIER_URL_TYPE_THREE),

		/**
		 * 统统付网关(融智付logo收银台)
		 */
		gateway_117(ForwardConstant.CASHIER_URL_TYPE_WG),

		/**
		 * 统统付网关(无融智付logo收银台)
		 */
		gateway_118(ForwardConstant.CASHIER_URL_TYPE_WG_NOICON),
		
		/**
		 * 易收付网关(无融智付logo收银台)
		 */
		gateway_3301(ForwardConstant.CASHIER_URL_TYPE_WG_NOICON),
		
		/**
		 * 易收付网关2(无融智付logo收银台)
		 */
		gateway_3302(ForwardConstant.CASHIER_URL_TYPE_WG_NOICON),
		
		/**
		 * 易收付网关3(无融智付logo收银台)
		 */
		gateway_3303(ForwardConstant.CASHIER_URL_TYPE_WG_NOICON),

		;

		private String url;

		/**
		 * @return the url
		 */
		public String getUrl() {
			return url;
		}

		/**
		 * @param url
		 *            the url to set
		 */
		public void setUrl(String url) {
			this.url = url;
		}

		/**
		 * @param channelCode
		 * @param url
		 */
		private GatewayURL(String url) {
			this.url = url;
		}

		public static GatewayURL getEnum(String gatewayid) {
			for (GatewayURL v : values()) {
				String name = StringUtils.substringAfter(v.name(), "gateway_");
				String[] gatewayids = StringUtils.split(name, "_");
				if (gatewayids != null && gatewayids.length > 0) {
					for (String gid : gatewayids) {
						if (gid.equals(gatewayid)) {
							return v;
						}
					}
				}
			}
			return null;
		}
	}

	public static enum H5URL {
		/**
		 * 支付宝官方手机网站
		 */
		gateway_22_1002(ForwardConstant.CASHIER_URL_TYPE_THREE),

		/**
		 * 民生银行支付宝H5
		 */
		gateway_77_1603(ForwardConstant.CASHIER_URL_TYPE_THREE),

		/**
		 * 兴业银行支付宝H5
		 */
		gateway_78_1106(ForwardConstant.CASHIER_URL_TYPE_THREE),

		/**
		 * 京东快捷H5
		 */
		gateway_2002(ForwardConstant.CASHIER_URL_TYPE_THREE),
		/**
		 * 京东钱包H5
		 */
		gateway_1504(ForwardConstant.CASHIER_URL_TYPE_THREE),
		/**
		 * 华势在线QQH5
		 */
		gateway_132(ForwardConstant.CASHIER_URL_TYPE_THREE),

		/**
		 * 银联快捷智付
		 */
		gateway_129_2601(ForwardConstant.CASHIER_URL_TYPE_THREE),

		/**
		 * 平安银行微信公众号
		 */
		gateway_73_1703(ForwardConstant.CASHIER_URL_TYPE_THREE),

		/**
		 * 平安银行支付宝H5
		 */
		gateway_101(ForwardConstant.CASHIER_URL_TYPE_THREE),

		/**
		 * 微信官方公众号
		 */
		gateway_5_1013(ForwardConstant.CASHIER_URL_TYPE_WXJSAPI),

		/**
		 * 富友公众号
		 */
		gateway_31_2104(ForwardConstant.CASHIER_URL_TYPE_WXJSAPI),

		/**
		 * 民生银行微信公众号
		 */
		gateway_42_1604(ForwardConstant.CASHIER_URL_TYPE_WXJSAPI),

		/**
		 * 兴业银行微信公众号
		 */
		gateway_58_1103(ForwardConstant.CASHIER_URL_TYPE_WXJSAPI),

		/**
		 * 易生华势微信公众号
		 */
		gateway_2505(ForwardConstant.CASHIER_URL_TYPE_WXJSAPI),

		/**
		 * 民生银行支付宝服务窗
		 */
		gateway_52_1607(ForwardConstant.CASHIER_URL_TYPE_ZFBFWC),

		/**
		 * 富友支付宝服务窗
		 */
		gateway_70_2105(ForwardConstant.CASHIER_URL_TYPE_ZFBFWC),

		/**
		 * 易生华势支付宝服务窗
		 */
		gateway_2506(ForwardConstant.CASHIER_URL_TYPE_ZFBFWC),

		/**
		 * 兴业银行支付宝服务窗
		 */
		gateway_1108(ForwardConstant.CASHIER_URL_TYPE_ZFBFWC),

		/**
		 * 光大微信公众号
		 */
		gateway_110(ForwardConstant.CASHIER_URL_TYPE_WXJSAPI);

		private String url;

		/**
		 * @return the url
		 */
		public String getUrl() {
			return url;
		}

		/**
		 * @param url
		 *            the url to set
		 */
		public void setUrl(String url) {
			this.url = url;
		}

		/**
		 * @param channelCode
		 * @param url
		 */
		private H5URL(String url) {
			this.url = url;
		}

		public static H5URL getEnum(String value) {
			for (H5URL v : values()) {
				String name = StringUtils.substringAfter(v.name(), "gateway_");
				String[] gatewayids = StringUtils.split(name, "_");
				if (gatewayids != null && gatewayids.length > 0) {
					for (String gatewayid : gatewayids) {
						if (gatewayid.equals(value)) {
							return v;
						}
					}
				}
			}
			return null;
		}

	}
}
