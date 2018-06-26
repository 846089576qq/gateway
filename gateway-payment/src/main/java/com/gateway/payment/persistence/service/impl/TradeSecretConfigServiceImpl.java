package com.gateway.payment.persistence.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gateway.payment.entity.TradeSecretConfigEntity;
import com.gateway.payment.persistence.service.ITradeSecretConfigService;

@Service
public class TradeSecretConfigServiceImpl extends BaseGenericServiceImpl<TradeSecretConfigEntity> implements ITradeSecretConfigService {

	@Override
	public String getRsaPublicKey() {
		List<TradeSecretConfigEntity> tradeSecretConfigs = queryAll();
		if (tradeSecretConfigs != null && tradeSecretConfigs.size() > 0) {
			return tradeSecretConfigs.get(0).getPlantformRsaPublicKey();
		}
		return null;
	}

	@Override
	public String getRsaPrivateKey() {
		List<TradeSecretConfigEntity> tradeSecretConfigs = queryAll();
		if (tradeSecretConfigs != null && tradeSecretConfigs.size() > 0) {
			return tradeSecretConfigs.get(0).getPlantformRsaPrivateKey();
		}
		return null;
	}

}
