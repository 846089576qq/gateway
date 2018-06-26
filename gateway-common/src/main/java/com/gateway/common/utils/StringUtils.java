package com.gateway.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import org.apache.commons.httpclient.NameValuePair;

/**
 * 字符串工具类
 * 
 * @author sunaolin
 * 
 */
public class StringUtils extends com.zitopay.foundation.common.util.StringUtils {

	public StringUtils() {
		super();
	}

	/**
	 * 使用给定的 charset 将此 String 编码到 byte 序列，并将结果存储到新的 byte 数组。
	 * 
	 * @param content
	 *            字符串对象
	 * 
	 * @param charset
	 *            编码方式
	 * 
	 * @return 所得 byte 数组
	 */
	public static byte[] getContentBytes(String content, String charset) {
		if (charset == null || "".equals(charset)) {
			return content.getBytes();
		}

		try {
			return content.getBytes(charset);
		} catch (UnsupportedEncodingException ex) {
			throw new IllegalArgumentException("Not support:" + charset, ex);
		}
	}

	/**
	 * 构建键值对
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static NameValuePair createNameValuePair(String key, String value) throws UnsupportedEncodingException {
		if (null == key || "".equals(key) || null == value || "".equals(value)) {
			return null;
		}
		return new NameValuePair(key, URLEncoder.encode(value, "utf-8"));
	}

	/**
	 * 构建键值对
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static NameValuePair createNameValuePairSupportBlankValue(String key, String value) throws UnsupportedEncodingException {
		if (null == key || "".equals(key)) {
			return null;
		}
		return new NameValuePair(key, URLEncoder.encode(value, "utf-8"));
	}

	/**
	 * 获取UUID
	 */
	public static String getUUIDString() {
		return UUID.randomUUID().toString().substring(0, 30);
	}

	/**
	 * 判断字符串是否为空或者null
	 * 
	 * @param charsequence
	 * @return
	 */
	public static boolean isNull(final CharSequence charsequence) {
		return charsequence == null || charsequence.length() <= 0 || "null".equals(charsequence);
	}
	


}