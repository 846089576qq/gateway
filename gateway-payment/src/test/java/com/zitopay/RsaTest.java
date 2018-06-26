package com.zitopay;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import com.gateway.common.algorithm.Base64;
import com.zitopay.datagram.utils.AesRsaUtil;

public class RsaTest {
	
	/**
     * 加密算法RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";
	
	public static final String PUBLIC_KEY = "publicKey";
	public static final String PRIVATE_KEY = "privateKey";

	/** RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024 */
	public static final int KEY_SIZE = 2048;

	public static final String PLAIN_TEXT = "加密原文内容测试";

	public static void main(String[] args) {
		Map<String, String> keyMap = generateKeyBytes();

		String publicKey = keyMap.get(PUBLIC_KEY);
		System.out.println("公钥：" + publicKey);
		String privateKey = keyMap.get(PRIVATE_KEY);
		System.out.println("私钥：" + privateKey);

		try {
			String encodedData = AesRsaUtil.encrtptKey(publicKey, PLAIN_TEXT, "UTF-8");
			System.out.println("加密：" + encodedData);
			byte[] decodedData = AesRsaUtil.decryptKey(privateKey, encodedData, "UTF-8");
			System.out.println("解密：" + new String(decodedData, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 生成密钥对。注意这里是生成密钥对KeyPair，再由密钥对获取公私钥
	 * 
	 * @return
	 */
	public static Map<String, String> generateKeyBytes() {

		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			keyPairGenerator.initialize(KEY_SIZE);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

			Map<String, String> keyMap = new HashMap<String, String>();
			keyMap.put(PUBLIC_KEY, Base64.encode(publicKey.getEncoded()));
			keyMap.put(PRIVATE_KEY, Base64.encode(privateKey.getEncoded()));
			return keyMap;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

}