package com.gateway.payment.persistence.mapper;

import org.springframework.stereotype.Repository;

import com.gateway.payment.entity.TradeSecretConfigEntity;
import com.github.abel533.mapper.Mapper;

/**
 * 平台系统设置mapper
 * 作者：王政
 * 创建时间：2017年5月23日 上午11:59:40
 */
@Repository
public interface ITradeSecretConfigMapper extends Mapper<TradeSecretConfigEntity> {
	
}
