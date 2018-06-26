package com.gateway.payment.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.zitopay.foundation.common.entity.AbstractEntity;
import com.zitopay.foundation.common.util.BeanUtils;

/**
 * 平台系统设置
 * 作者：王政
 * 创建时间：2017年5月23日 上午11:57:56
 */
@Table(name="alwaypay_trade_secret_config")
public class TradeSecretConfigEntity extends AbstractEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7309033861559034919L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 平台公钥
	 */
	private String plantformRsaPublicKey;

	/**
	 * 平台私钥
	 */
	private String plantformRsaPrivateKey;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPlantformRsaPublicKey() {
		return plantformRsaPublicKey;
	}

	public void setPlantformRsaPublicKey(String plantformRsaPublicKey) {
		this.plantformRsaPublicKey = plantformRsaPublicKey;
	}

	public String getPlantformRsaPrivateKey() {
		return plantformRsaPrivateKey;
	}

	public void setPlantformRsaPrivateKey(String plantformRsaPrivateKey) {
		this.plantformRsaPrivateKey = plantformRsaPrivateKey;
	}

	@Override
	public String toString() {
		return BeanUtils.bean2JSON(this);
	}
}