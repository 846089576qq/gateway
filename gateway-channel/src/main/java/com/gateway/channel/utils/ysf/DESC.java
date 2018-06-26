package com.gateway.channel.utils.ysf;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


public class DESC {
	/**
	 * 加密
	 * encryptString 明文
	 * encryptKey 向量
	 * iv 密钥
	 */
	public static String encrypt3DES(String encryptString, String encryptKey,
			String iv) {
		try {
			IvParameterSpec zeroIv = new IvParameterSpec(iv.getBytes("UTF-8"));
			SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes("UTF-8"),
					"DESede");
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			cipher.init(1, key, zeroIv);
			byte[] encryptedData = cipher.doFinal(encryptString
					.getBytes("UTF-8"));
			return new String(Base64.encodeBase64(encryptedData));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
	}

	/**
	 * 解密
	 * decryptString 密文
	 * decryptKey 向量
	 * iv 密钥
	 */
	public static String decrypt3DES(String decryptString, String decryptKey,
			String iv) {
		try {
			byte[] byteMi = Base64.decodeBase64(decryptString.getBytes("UTF-8"));
			IvParameterSpec zeroIv = new IvParameterSpec(iv.getBytes("UTF-8"));
			SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes("UTF-8"),
					"DESede");
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			cipher.init(2, key, zeroIv);
			byte[] decryptedData = cipher.doFinal(byteMi);
			return new String(decryptedData, "UTF-8");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
	}
}
