/**
 * Copyright 2015 by IPS. Floor 3,Universal Industrial Building, 
 * Tian Yaoqiao Road 1178,Shanghai, P.R. China，200300. All rights reserved.
 *
 * This software is the confidential and proprietary information of IPS
 * ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with IPS.
 */
package com.gateway.channel.utils.ysf;


/**
 * @Description: 组装xml类
 * @Author QULIPENG
 * @Version 1.0 2015年12月10日 上午11:06:24
 */

public class XMLFactory {

	
	public static String createTransXML (String body, String md5) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(XML_TRANS_START);
		buffer.append(XML_REQ_HEAD);
		buffer.append(body);
		buffer.append(XML_TRANS_END);
		// 生成密钥
		String signature = IPSmd5.createSignature(body, md5);
		return buffer.toString().replace(MY_SIGNATURE, signature);
	}
	
	/**
	 *
	 * @Title: createXML
	 * @Description: 生成xml
	 * @return
	 */
	public static String createDesXML (String body, String merCode, String deskey, String desiv) {
		//
		String encrypt3des = DESC.encrypt3DES(body, deskey, desiv);
		// StringBuffer buffer = new StringBuffer(XML_VERSION);
		StringBuffer buffer = new StringBuffer();
		buffer.append("<ipsRequest><argMerCode>" + merCode + "</argMerCode><arg3DesXmlPara>");
		buffer.append(encrypt3des);
		buffer.append(XML_ROOT_CODE_END);
		
		return buffer.toString();//
	}

	
	private static final String XML_ROOT_CODE = "<ipsRequest><argMerCode>218415</argMerCode><arg3DesXmlPara>";
	private static final String XML_ROOT_CODE_END = "</arg3DesXmlPara></ipsRequest>";
	private static final String XML_OPEN_START = "<openUserReqXml>";
	private static final String XML_UPDATE_START = "<updateUserReqXml>";
	private static final String XML_QUERY_START = "<queryUserReqXml>";
	private static final String XML_WITH_START = "<withdrawalReqXml>";
	private static final String XML_TRANS_START = "<transferReqXml>";

	/**
	 * 请求节点
	 */
	private static final String XML_OPEN_END = "</openUserReqXml>";
	private static final String XML_UPDATE_END = "</updateUserReqXml>";
	private static final String XML_QUERY_END = "</queryUserReqXml>";
	private static final String XML_WITH_END = "</withdrawalReqXml>";
	private static final String XML_TRANS_END = "</transferReqXml>";

	/** XML报文头 **/
	private static final String XML_VERSION = "<?xml version='1.0' encoding=''?>";

	/**
	 * 默认认证my_signature
	 */
	private static final String MY_SIGNATURE = "my_signature";

	/**
	 * 请求头部文件
	 */
	private static final String XML_REQ_HEAD = "<head><version>v1.0.1</version><reqIp>192.168.0.66</reqIp><reqDate>2016-11-26 10:48:39</reqDate><signature>" + MY_SIGNATURE + "</signature></head>";
}
