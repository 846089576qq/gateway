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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Description: (这里用一句话描述这个类的作用)
 * @Author QULIPENG
 * @Version 1.0 2015年12月10日 上午9:05:54
 */

public final class IPSmd5 {

	// 全局数组
	private final static String[] strDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	private IPSmd5() {
	}

	/**
	 * 
	 * @Title: createSignature
	 * @Description: 生产证书
	 * @param body  xml中的body节点
	 * @param platForm 平台商户号
	 * @param platFormCertificate 平台商户号证书
	 * @return
	 */
	public static String createSignature (String body, String platForm, String platFormCertificate) {
		return GetMD5Code(body + platForm + platFormCertificate);
	}
	
	public static String createSignature (String body, String platFormCertificate) {
		return GetMD5Code(body + platFormCertificate);
	}

	// 返回形式为数字跟字符串
	private static String byteToArrayString (byte bByte) {
		int iRet = bByte;
		if (iRet < 0) {
			iRet += 256;
		}
		int iD1 = iRet / 16;
		int iD2 = iRet % 16;
		return strDigits[iD1] + strDigits[iD2];
	}

	// 转换字节数组为16进制字串
	private static String byteToString (byte[] bByte) {
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < bByte.length; i++) {
			sBuffer.append(byteToArrayString(bByte[i]));
		}
		return sBuffer.toString();
	}

	private static String GetMD5Code (String strObj) {
		String resultString = null;
		try {
			resultString = new String(strObj);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteToString(md.digest(strObj.getBytes()));
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		System.out.println("md5 == " + resultString);
		return resultString;
	}

}
