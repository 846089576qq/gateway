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
package com.gateway.common.properties;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.zitopay.foundation.common.env.DigestEnvironment;

/**
 * spring容器默认singleton,成员变量则只初始化一次
 *
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月8日
 */
@Component
public class ChannelProperties {

	@Inject
	private DigestEnvironment environment;

	private ShengFuTongProperty shengFuTongProperty;

	public ShengFuTongProperty getShengFuTongProperty() {
		if (shengFuTongProperty == null) {
			shengFuTongProperty = new ShengFuTongProperty();
		}
		return shengFuTongProperty;
	}

	private XingyeProperty xingyeProperty;

	public XingyeProperty getXingyeProperty() {
		if (xingyeProperty == null) {
			xingyeProperty = new XingyeProperty();
		}
		return xingyeProperty;
	}

	private HszxProperty hszxProperty;

	public HszxProperty getHszxProperty() {
		if (hszxProperty == null) {
			hszxProperty = new HszxProperty();
		}
		return hszxProperty;
	}

	private MsyhProperty msyhProperty;

	public MsyhProperty getMsyhProperty() {
		if (msyhProperty == null) {
			msyhProperty = new MsyhProperty();
		}
		return msyhProperty;
	}

	private PayhProperty payhProperty;

	public PayhProperty getPayhProperty() {
		if (payhProperty == null) {
			payhProperty = new PayhProperty();
		}
		return payhProperty;
	}

	private WXQRcodeProperty wXQRcodeProperty;

	public WXQRcodeProperty getWXQRcodeProperty() {
		if (wXQRcodeProperty == null) {
			wXQRcodeProperty = new WXQRcodeProperty();
		}
		return wXQRcodeProperty;
	}

	private ZhifubaoProperty zhifubaoProperty;

	public ZhifubaoProperty getZfbProperty() {
		if (zhifubaoProperty == null) {
			zhifubaoProperty = new ZhifubaoProperty();
		}
		return zhifubaoProperty;
	}

	private FuYouProperty fuYouProperty;

	public FuYouProperty getfuyouProperty() {
		if (fuYouProperty == null) {
			fuYouProperty = new FuYouProperty();
		}
		return fuYouProperty;
	}

	private JDkjProperty jdkjProperty;

	public JDkjProperty getJDkjProperty() {
		if (jdkjProperty == null) {
			jdkjProperty = new JDkjProperty();
		}
		return jdkjProperty;
	}

	private JDwgProperty jDwgProperty;

	public JDwgProperty getJDwgProperty() {
		if (jDwgProperty == null) {
			jDwgProperty = new JDwgProperty();
		}
		return jDwgProperty;
	}

	private YSBProperty ySBProperty;

	public YSBProperty getYSBProperty() {
		if (ySBProperty == null) {
			ySBProperty = new YSBProperty();
		}
		return ySBProperty;
	}

	private TXProperty txProperty;

	public TXProperty getTXProperty() {
		if (txProperty == null) {
			txProperty = new TXProperty();
		}
		return txProperty;
	}

	private YLProperty ylProperty;

	public YLProperty getYLProperty() {
		if (ylProperty == null) {
			ylProperty = new YLProperty();
		}
		return ylProperty;
	}

	private YSkjProperty ysSkjProperty;

	public YSkjProperty getYSkjProperty() {
		if (ysSkjProperty == null) {
			ysSkjProperty = new YSkjProperty();
		}
		return ysSkjProperty;
	}

	private JiuPaiProperty jiuPaiProperty;

	public JiuPaiProperty getJiuPaiProperty() {
		if (jiuPaiProperty == null) {
			jiuPaiProperty = new JiuPaiProperty();
		}
		return jiuPaiProperty;
	}

	private GDProperty gDProperty;

	public GDProperty getGDProperty() {
		if (gDProperty == null) {
			gDProperty = new GDProperty();
		}
		return gDProperty;
	}

	private TongTongFuProperty tongTongFuProperty;

	public TongTongFuProperty getTongTongFuProperty() {
		if (tongTongFuProperty == null) {
			tongTongFuProperty = new TongTongFuProperty();
		}
		return tongTongFuProperty;
	}

	private YiShiProperty yiShiProperty;

	public YiShiProperty getYiShiProperty() {
		if (yiShiProperty == null)
			yiShiProperty = new YiShiProperty();
		return yiShiProperty;
	}

	/**
	 * 盛付通通道配置文件
	 *
	 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
	 * @since 2017年5月8日
	 */
	public class ShengFuTongProperty {

		/**
		 * 盛付通快捷api下订单请求url
		 */
		public final String ShengFutongApikjOrderUrl = environment.getProperty("shengfutong_apikj_order_url");

		/**
		 * 盛付通快捷api预校验url
		 */
		public final String ShengFutongApikjPayPrecheckUrl = environment.getProperty("shengfutong_apikj_payprecheck_url");

		/**
		 * 盛付通快捷api确认支付url
		 */
		public final String ShengFutongApikjConfirmpayUrl = environment.getProperty("shengfutong_apikj_confirmpay_url");

		/**
		 * 盛付通快捷api查询wsdl地址
		 */
		public final String ShengFutongApikjQueryorderWsdl = environment.getProperty("shengfutong_apikj_queryorder_wsdl");

		/**
		 * 盛付通网关api下单请求url
		 */
		public final String ShengFutongApiwgOrderUrl = environment.getProperty("shengfutong_apiwg_order_url");

		/**
		 * 盛付通网关下单前请求上游时间戳
		 */
		public final String shengfutongApiwgQueryorderWsdl = environment.getProperty("shengfutong_apiwg_queryorder_wsdl");

		/**
		 * 盛付通网关下单前请求上游时间戳
		 */
		public final String ShengFutongApiwgsendTimeUrl = environment.getProperty("shengfutong_apiwg_sendTime_url");

		/**
		 * 盛付通网关退款
		 */
		public final String shengfutongApiwgRefundUrl = environment.getProperty("shengfutong_apiwg_refund_url");

		/**
		 * 盛付通网关退款查询
		 */
		public final String shengfutongApiwgQueryRefundUrl = environment.getProperty("shengfutong_apiwg_queryrefund_url");

		/**
		 * 盛付通代扣请求url
		 */
		public final String ShengFutongApidkOrderUrl = environment.getProperty("shengfutong_api_order_url");

		/**
		 * 盛付通代扣查询请求url
		 */
		public final String ShengFutongApidkOrderQueryUrl = environment.getProperty("shengfutong_api_order_query_url");

		/**
		 * 盛付通代扣退款请求url
		 */
		public final String shengfutongApiRefundUrl = environment.getProperty("shengfutong_api_refund_url");

		/**
		 * 盛付通代扣退款查询请求url
		 */
		public final String shengfutongApiRefundQueryUrl = environment.getProperty("shengfutong_api_refund_query_url");

	}

	/**
	 * 兴业银行配置文件
	 *
	 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
	 * @since 2017年5月8日
	 */
	public class XingyeProperty {

		/**
		 * 兴业支付宝扫码url
		 */
		public final String xingyeApiZhifubaoSmUrl = environment.getProperty("xingye_api_zhifubao_sm_url");

		/**
		 * 兴业微信扫码url
		 */
		public final String XingYeWeiXinSmOrderUrl = environment.getProperty("xingye_wxsm_order_url");

		/**
		 * 兴业微信主扫url
		 */
		public final String XingYeWeiXinTmOrderUrl = environment.getProperty("xingye_wxtm_order_url");

		/**
		 * 兴业微信扫码查询url
		 */
		public final String XingYeWeiXinSmOrderQueryUrl = environment.getProperty("xingye_wxsm_order_query_url");

		/**
		 * 兴业微信退款url
		 */
		public final String XingYeWeiXinRefundOrderUrl = environment.getProperty("xingye_wx_refundorder_url");

		/**
		 * 兴业微信退款查询url
		 */
		public final String XingYeWeiXinRefundOrderQueryUrl = environment.getProperty("xingye_wx_refundorder_query_url");

	}

	/**
	 * 支付宝官方配置文件
	 *
	 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
	 * @since 2017年5月8日
	 */
	public class ZhifubaoProperty {

		/**
		 * 支付宝官方扫码/退款/退款查询url
		 */
		public final String ZhiFuBaoSmsendUrl = environment.getProperty("zfb_smzf_url");

	}

	/**
	 * 华势在线配置文件
	 *
	 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
	 * @since 2017年5月8日
	 */
	public class HszxProperty {

		public final String HuaShiZxOrderUrl = environment.getProperty("hszx_order_url");

		public final String HuaShiZxWeiXinSmOrderQueryUrl = environment.getProperty("hszx_wxsm_order_query_url");

		public final String HuaShiZxOrderRefund = environment.getProperty("hszx_order_refund");

	}

	/**
	 * 民生银行 配置文件
	 *
	 * @author zhangxinyuan
	 * @since 2017年6月14日
	 */
	public class MsyhProperty {

		public final String MsyhOrderUrl = environment.getProperty("ms_smzf_url");

	}

	/**
	 * 平安银行 配置文件
	 *
	 * @author zhangxinyuan
	 * @since 2017年6月14日
	 */
	public class PayhProperty {

		public final String PayhOrderUrl = environment.getProperty("pinganbank_trade_url");

		public final String PmtTagWeixin = environment.getProperty("pinganbank_trade_pmt_tag_weixin");

		public final String PmtTagAlipay = environment.getProperty("pinganbank_trade_pmt_tag_alipay");

	}

	/**
	 * 微信官方扫码
	 *
	 * @author ryw
	 * @since 2017年6月14日
	 */
	public class WXQRcodeProperty {

		/** 统一下单 */
		public final String WXUnifiedUrl = environment.getProperty("wx_unified_pay_url");

		/** 条码支付 */
		public final String WXBARPayUrl = environment.getProperty("wx_bar_pay_url");

		/** 订单查询 */
		public final String WXQRQuerycodeUrl = environment.getProperty("wx_qr_quey_url");

		/** 微信退款 */
		public final String WXRefundUrl = environment.getProperty("wx_refund_url");

		/** 退款查询 */
		public final String WXFindRefundUrl = environment.getProperty("wx_find_refund_url");

	}

	/**
	 * 富友请求地址
	 *
	 * @author Zxy
	 * @since 2017年7月20日
	 */
	public class FuYouProperty {

		public final String fyPublickey = environment.getProperty("fy_publickey");

		public final String fyPrivatekey = environment.getProperty("fy_privatekey");

		public final String fyOrgno = environment.getProperty("fy_orgno");

		public final String fySmPayUrl = environment.getProperty("fy_sm_pay_url");

		public final String fyTmPayUrl = environment.getProperty("fy_tm_pay_url");

		public final String fyWxprePayUrl = environment.getProperty("fy_wxpre_pay_url");

		public final String fyCommonQueryUrl = environment.getProperty("fy_common_query_url");

		public final String fyCommonRefundUrl = environment.getProperty("fy_common_refund_url");
	}

	/**
	 * 京东快捷配置文件
	 *
	 * @author tianmeng
	 * @since 2017年7月20日
	 */
	public class JDkjProperty {

		// 支付 pc
		public final String JDkjPayPcUrl = environment.getProperty("jdkj_pay_pc_url");

		// 支付 h5
		public final String JDkjPayH5Url = environment.getProperty("jdkj_pay_h5_url");

		// 查询
		public final String JDkjQueryUrl = environment.getProperty("jdkj_query_url");

		// 退款
		public final String JDkjRefundUrl = environment.getProperty("jdkj_refund_url");

	}

	/**
	 * 京东网关配置文件
	 *
	 * @author tianmeng
	 * @since 2017年7月26日
	 */
	public class JDwgProperty {

		// 支付
		public final String JDwgPayProperty = environment.getProperty("jdwg_pay_url");

		// 查询、退款
		public final String JDwgQueryAndRefudnUrl = environment.getProperty("jdwg_query_refund_url");

	}

	/**
	 * 银生宝配置文件
	 *
	 * @author zhangxinyuan
	 * @since 2017年8月2日
	 */
	public class YSBProperty {

		// 支付
		public final String YSBPayUrl = environment.getProperty("yinshengbao_pay_url");

		// 查询
		public final String YSBQueryUrl = environment.getProperty("yinshengbao_query_url");

		// 退款
		public final String YSBRefundUrl = environment.getProperty("yinshengbao_refund_url");

		// 实时代扣支付
		public final String YSBSSDKPayUrl = environment.getProperty("yinshengbao_ss_dk_pay_url");

		// 实时代扣查询
		public final String YSBSSDKQueryUrl = environment.getProperty("yinshengbao_ss_dk_query_url");

		// 委托代扣录入
		public final String YSBWTDKEnteringUrl = environment.getProperty("yinshengbao_wt_dk_entering_url");

		// 委托代扣查询
		public final String YSBWTDKQueryUrl = environment.getProperty("yinshengbao_wt_dk_query_url");

		// 委托代扣支付
		public final String YSBWTDKPayUrl = environment.getProperty("yinshengbao_wt_dk_pay_url");

		// 委托代扣子协议号查询接口
		public final String YSBWTDProtocolQueryUrl = environment.getProperty("yinshengbao_wt_dk_protocol_query_url");

		// 委托代扣子协议延期接口
		public final String YSBWTDProtocolDelayUrl = environment.getProperty("yinshengbao_wt_dk_protocol_delay_url");
	}

	/**
	 * 天下配置文件
	 *
	 * @author zhangxinyuan
	 * @since 2017年8月7日
	 */
	public class TXProperty {

		// 支付
		public final String TXpayUrl = environment.getProperty("TIANXIAPAY_WGPAY_URL");

		// 查询
		public final String TXqueryUrl = environment.getProperty("TIANXIAPAY_WGQUERY_URL");
	}

	/**
	 * 银联配置文件
	 *
	 * @author tianmeng
	 * @since 2017年12月21日
	 */
	public class YLProperty {

		// 通用地址
		public final String YinLianUrl = environment.getProperty("YINLIAN_URL");

	}

	/**
	 * 易生快捷配置文件
	 *
	 * @author tianmeng
	 * @since 2018年1月9日
	 */
	public class YSkjProperty {

		// 通用地址
		public final String YiShengQuickUrl = environment.getProperty("YISHENG_QUICK_URL");

	}

	/**
	 * 九派配置文件
	 *
	 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
	 * @since 2018年1月17日
	 */
	public class JiuPaiProperty {

		/**
		 * JiuPayWGUrl 支付&查询
		 */
		public final String JiuPaiWGUrl = environment.getProperty("JIUPAI_WG_URL");

	}

	/**
	 * description:光大银行配置文件 Author:Evelyn Date:2018/1/17 15:47
	 */
	public class GDProperty {

		// 通用地址
		public final String GuangDaUrl = environment.getProperty("GUANG_DA_URL");

	}

	/**
	 * 统统付配置文件
	 *
	 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
	 * @since 2018年1月22日
	 */
	public class TongTongFuProperty {

		/**
		 * TongTongFuWGUrl 支付
		 */
		public final String TongTongFuWGUrl = environment.getProperty("TONGTONGFU_WG_URL");

		/**
		 * TongTongFuQueryUrl 查询
		 */
		public final String TongTongFuQueryUrl = environment.getProperty("TONGTONGFU_QUERY_URL");

	}

	/**
	 * 易势配置文件
	 *
	 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
	 * @since 2018年3月19日
	 */
	public class YiShiProperty {

		/**
		 * YiShiUrl 易势地址
		 */
		public final String yiShiUrl = environment.getProperty("yishi_url");

	}
}
