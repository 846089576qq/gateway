package com.gateway.payment.service.general;

/**
 * 融智付秘钥接口
 * 作者：王政
 * 创建时间：2017年5月23日 下午12:55:19
 */
public interface ISecretService {
	
	public String getRsaPublicKey();
	
	public String getRsaPrivateKey();

}
