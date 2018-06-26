package com.gateway.payment.persistence.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.gateway.payment.entity.GatewayEntity;
import com.github.abel533.mapper.Mapper;

/**
 * 通道mapper
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Repository
public interface IGatewayMapper extends Mapper<GatewayEntity> {
	
	public boolean isCategoryGateway(@Param("gatewayId") String gatewayId, @Param("categoryCode") String categoryCode);
	
	public String getGatewayIdByPersonidAndCategoryCode(@Param("merchantId") Integer merchantId, @Param("categoryCode") String categoryCode);
	
	public String getCategoryCodeByGatewayId(@Param("gatewayId") String gatewayId);
	
}