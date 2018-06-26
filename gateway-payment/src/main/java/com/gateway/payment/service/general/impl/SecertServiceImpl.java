package com.gateway.payment.service.general.impl;

import org.springframework.stereotype.Service;

import com.gateway.payment.service.base.impl.BaseServiceImpl;
import com.gateway.payment.service.general.ISecretService;

/**
 * 融智付秘钥接口实现
 * 
 * 作者：王政 创建时间：2017年3月6日 下午5:59:13
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class SecertServiceImpl extends BaseServiceImpl implements ISecretService {

	@Override
	public String getRsaPublicKey() {
		return tradeSecretConfigService.getRsaPublicKey();
	}

	@Override
	public String getRsaPrivateKey() {
		return tradeSecretConfigService.getRsaPrivateKey();
	}

}