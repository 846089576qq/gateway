//package com.gateway.controller.gateway.util;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.alipay.api.AlipayApiException;
//import com.alipay.api.AlipayClient;
//import com.alipay.api.DefaultAlipayClient;
//import com.alipay.api.request.AlipaySystemOauthTokenRequest;
//import com.alipay.api.response.AlipaySystemOauthTokenResponse;
//import com.gateway.common.properties.EnvProperties;
//import com.gateway.common.properties.ExtensionProperties;
//
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.net.URLEncoder;
//import java.util.*;
//
///**
// * 微信OAuth2.0官方授权工具类
// *
// * 作者：田萌  创建时间：2017年6月21日 15:37:46
// */
//@Component
//public class WechatOAuthUtil {
//
//	private static final Logger logger = LoggerFactory.getLogger(WechatOAuthUtil.class);
//
//	/** 请求模式 - 微信 */
//	public static final String REQUEST_TYPE_WX = "wechat";
//
//	/** 请求模式 - 支付宝 */
//	public static final String REQUEST_TYPE_ZFB = "alipay";
//
//	/** 微信获取access_token地址 */
//	private static final String WINXIN_OAUTH_ACCESS_TOKENURL = "https://api.weixin.qq.com/sns/oauth2/access_token";
//
//	/** 微信授权地址 */
//	private static final String WINXIN_OAUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
//
//	/** 支付宝授权地址 */
//	private static final String ALIPAY_OAUTH_URL = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm";
//
//	/** 支付宝请求获取OPENID地址 */
//	private static final String ALIPAY_GETOPENID_URL = "https://openapi.alipay.com/gateway.do";
//
//	/** 支付宝服务平台 - 融智付服务窗 - 应用id */
//	private static final String APP_ID = "2016101102094232";
//
//	/** 支付宝服务平台 - 融智付服务窗 - 私钥 */
//	private static final String APP_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMC+VKLck8g6NoGZ9ak/hq679jZGJlLKJSD76owpC7wqO4o9BjOWa+ezi2coiXR7VjUZ072VzhwGAj9s+gsEY0SrCdTp+smzGRLPxw0IzavKrM5CusX5mM32q1UxVoz1scjApGlsCedEud/9g71sMlmMgm175CjhLwlfEsdzUXwlAgMBAAECgYAZliquJBIKNpAdzE5sVV6Pu6wHsHBN9T/QjSCp0pkYNZMJ2ugD2Sgh7hfHbf4xVF4xVmhhdo2R8spBmTnoVIDI1cWTnbR7vREtDkHN3CcdM8ejtvt2VzTfeLELgXGdeKJblGbrzJTvutahunFciQOfJlHfQ2QwC1My8j+SYz66AQJBAOwjLRcAjN0lQvIWhoXVQNd+h/GDpdv6LFy7JU5xkvDKR/pj/eMl9Guf024xR4H+JIpOepguZzg8NwckIRtMrmECQQDQ9L0mMHSUmfGLkVTIvQ54KIWuV1YuANU54P9wGlPCSl2eRwFiBwOvmfk5qZbrQjVsGpixd76dM19ssPSmX/xFAkArSMNiHQK1IrhjwcdEzvNEzPfESHplmTT6hn9vIphptNp+xkdqlLF57OEHqNbPuDMgewQz0wWupDL+Bxxeca7hAkADO6YpjxpeqjsYg7kiGfq9VTMsTWGh+JVT/e012NJu8SOdrU+SKrWd4+39PGh/X1jgQVEXfdjPKdpSHjLK2DVZAkEAhgNPgkRka93BbE3zbUF0z92RYe9lwG5iK3HLmtI6vQpVK0beHvKavcTe7XTc1nBsACiXJ6psLDzUiUjTx5/8wA==";
//
//	/** 支付宝服务平台 - 融智付服务窗 - 公钥 */
//	private static final String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
//
//	@Autowired
//	protected EnvProperties properties;
//
//	@Autowired
//	protected ExtensionProperties extensionProperties;
//
//
//
//	/**
//	 * 判断请求来源
//	 */
//	public static String getRequestType(String userAgent){
//		if (StringUtils.indexOf(userAgent, "micromessenger") > 0) {// 请求来自微信
//			return REQUEST_TYPE_WX;
//		}else if (StringUtils.indexOf(userAgent, "alipayclient") > 0) {// 请求来自支付宝
//			return REQUEST_TYPE_ZFB;
//		}
//		return null;
//	}
//
//
//	/**
//	 * 拼装授权地址
//	 */
//	public static String createOAuthUrl(String type, String url, Map<String, String> params){
//		String result = null;
//		try {
//			//拼装授权回调地址
//			String redirect_uri = URLEncoder.encode(url + "/payment/getOpenid?" + createLinkString(params), "GBK");
//			Map<String, String> OAuth_Param_Map = new HashMap<>();// 授权请求参数集合
//
//			//拼装微信授权地址
//			if (type.equals(REQUEST_TYPE_WX)) {
//				OAuth_Param_Map.put("appid", params.get("appid"));// 公众号平台应用ID
//				OAuth_Param_Map.put("redirect_uri", redirect_uri);// 回调本平台地址
//				OAuth_Param_Map.put("response_type", "code");// 返回类型，请填写code
//				OAuth_Param_Map.put("scope", "snsapi_base");// snsapi_userinfo:非静默 - snsapi_base:静默
//				String OAuth_Param = createLinkString(OAuth_Param_Map);
//				result = WINXIN_OAUTH_URL +"?"+ OAuth_Param + "#wechat_redirect";
//			}
//			//拼装支付宝授权地址
//			if (type.equals(REQUEST_TYPE_ZFB)) {
//				OAuth_Param_Map.put("app_id", APP_ID);// 服务窗平台应用ID
//				OAuth_Param_Map.put("scope", "auth_base");// auth_userinfo:非静默 - auth_base:静默
//				OAuth_Param_Map.put("redirect_uri", redirect_uri);// 回调本平台地址
//				String OAuth_Param = createLinkString(OAuth_Param_Map);
//				result = ALIPAY_OAUTH_URL +"?"+ OAuth_Param;
//			}
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//			logger.error("拼装授权地址异常,错误信息：{}", e.getLocalizedMessage());
//		}
//		logger.info("授权地址：{}", result);
//		return result;
//	}
//
//
//	/**
//	 * 接收微信/支付宝授权回调后换取openid
//	 */
//	public static String exchangeOpendId(String type, String code, Map<String,String> params){
//		String openid = null;
//		// ==========微信授权==========
//		if (type.equals(REQUEST_TYPE_WX)) {
//			String url = WINXIN_OAUTH_ACCESS_TOKENURL+"?appid=AppId&secret=AppSecret&code=CODE&grant_type=authorization_code";
//			url = url.replace("AppId", params.get("appid")).replace("AppSecret", params.get("appsecret")).replace("CODE", code);
//			String jsonResult = sendGet(url);
//			JSONObject jsonTexts = (JSONObject) JSON.parse(jsonResult);
//			logger.info("获取openid 请求结果：{}", jsonTexts.toString());
//			if (null == jsonTexts.get("openid")) {
//				return null;
//			}
//			openid = jsonTexts.get("openid").toString();
//		}
//		// ==========支付宝授权==========
//		if (type.equals(REQUEST_TYPE_ZFB)) {
//			AlipayClient alipayClient = new DefaultAlipayClient(ALIPAY_GETOPENID_URL, APP_ID, APP_PRIVATE_KEY, "json", "UTF-8", ALIPAY_PUBLIC_KEY, "RSA");
//			AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
//			request.setCode(code);
//			request.setGrantType("authorization_code");
//			try {
//				AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
//				logger.info("获取openid 请求结果：{}", JSON.toJSONString(oauthTokenResponse));
//				if (null == oauthTokenResponse) {
//					return null;
//				}
//				openid = oauthTokenResponse.getUserId();
//			} catch (AlipayApiException e) {
//				e.printStackTrace();
//			}
//		}
//		return openid;
//	}
//
//
//
//	/**
//	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
//	 * @param params 需要排序并参与字符拼接的参数组
//	 * @return 拼接后字符串
//	 */
//	public static String createLinkString(Map<String, String> params) {
//		List<String> keys = new ArrayList<String>(params.keySet());
//		Collections.sort(keys);
//		String prestr = "";
//		for (int i = 0; i < keys.size(); i++) {
//			String key = keys.get(i);
//			String value = params.get(key);
//			if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
//				prestr = prestr + key + "=" + value;
//			} else {
//				prestr = prestr + key + "=" + value + "&";
//			}
//		}
//		return prestr;
//	}
//
//	/**
//	 * Get请求方法
//	 */
//	public static String sendGet(String url) {
//		StringBuffer result=new StringBuffer("");
//		BufferedReader in = null;
//		try {
//			URL realUrl = new URL(url);
//			// 打URL间连接
//			URLConnection connection = realUrl.openConnection();
//			// 设置通用请求属性
//			connection.setRequestProperty("accept", "*/*");
//			connection.setRequestProperty("connection", "Keep-Alive");
//			connection.setRequestProperty("user-agent", "mozilla/5.0 (linux; android 4.4.4; hm note 1lte build/ktu84p; wv) applewebkit/537.36 (khtml, like gecko) version/4.0 chrome/53.0.2785.49 mobile mqqbrowser/6.2 tbs/043305 safari/537.36 micromessenger/6.5.4.1000 nettype/wifi language/zh_cn");
//			connection.setRequestProperty("Accept-Charset", "GBK");
//			// 建立实际连接
//			connection.connect();
//			// 遍历所响应字段
//			in = new BufferedReader(
//					new InputStreamReader(connection.getInputStream(),"GBK"));// 定义BufferedReader输入流读取URL响应
//			String line;
//			while ((line = in.readLine()) != null) {
//				result.append(line);
//			}
//		} catch (Exception e) {
//			logger.error("发送GET请求异常" + e);
//			e.printStackTrace();
//		}finally {
//			try {
//				if (in != null) in.close();
//			} catch (Exception e2) { e2.printStackTrace();}
//		}
//		String resultStr=result.toString();
//		return resultStr;
//	}
//}
