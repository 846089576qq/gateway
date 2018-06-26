package com.gateway.payment.persistence.service;

import com.gateway.payment.entity.TradeSecretConfigEntity;

/**
 * 平台系统设置
 * 作者：王政
 * 创建时间：2017年5月23日 下午12:01:40
 */
public interface ITradeSecretConfigService extends IBaseGenericService<TradeSecretConfigEntity> {
	
	/**
	 * 获取平台公钥
	 * 
	 * @return
	 */
	public String getRsaPublicKey();
	
	/**
	 * 获取平台私钥
	 * 
	 * @return
	 */
	public String getRsaPrivateKey();

}
