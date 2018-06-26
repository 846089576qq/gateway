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
package com.gateway.common.algorithm;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import org.apache.commons.codec.digest.DigestUtils;

import com.gateway.common.utils.HexUtil;
import com.gateway.common.utils.StringUtils;

/**
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月8日
 */
public class MD5 {

	public static String MD5(String text, String key, String charset) {
		String message = text + key;
		MessageDigest digest = getDigest("MD5");
		digest.update(StringUtils.getContentBytes(message, charset));
		byte[] signed = digest.digest();
		return HexUtil.toHexString(signed);
	}

	private static MessageDigest getDigest(String algorithm) {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (final NoSuchAlgorithmException ex) {
			throw new IllegalArgumentException("Not support:" + algorithm, ex);
		}
	}

	/**
	 *
	 *
	 * 方法描述:利用java自带的MD5加密，生成32个字符的16进制(utf-8编码) <br>
	 *
	 * @return String <br>
	 *         作者： 徐诚 <br>
	 *         创建时间： 2016-6-14 下午01:43:33
	 */
	public final static String encode(String s) {
		// 16进制字符
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		try {
			if (s == null) {
				return null;
			}
			byte[] btInput = s.getBytes("utf-8");
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			String jm = new String(str);
			return jm;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 签名字符
	 *
	 * @param text
	 *            要签名的字符
	 * @param sign
	 *            签名结果
	 * @param key
	 *            密钥
	 * @param input_charset
	 *            编码格式
	 * @return 签名结果
	 */
	public static boolean verify(String text, String sign, String key, String charset) throws Exception {
		text = text + key;
		String mysign = DigestUtils.md5Hex(getContentBytes(text, charset));
		if (mysign.equals(sign)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("签名过程中出现错,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }


}
