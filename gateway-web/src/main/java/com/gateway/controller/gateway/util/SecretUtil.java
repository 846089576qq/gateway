package com.gateway.controller.gateway.util;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gateway.payment.service.general.ISecretService;

@Component
public class SecretUtil {
	
	@Reference
	private ISecretService secretService;
	
	public static String publicKey;
	
	public static String privateKey;
	
	@PostConstruct
	public void setZitopayPublicKey() {
		publicKey = secretService.getRsaPublicKey();
		privateKey = secretService.getRsaPrivateKey();
	}

}
