package com.gateway.payment.persistence.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.gateway.payment.entity.PersonGatewayEntity;
import com.github.abel533.mapper.Mapper;

/**
 * 商户通道mapper
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Repository
public interface IPersonGatewayMapper extends Mapper<PersonGatewayEntity> {

	/**
	 * 查询商户对应开通的渠道
	 * @param appId 融智付配置应用ID
	 * @param merchantId 商户ID
	 * @return
	 */
	List<Map<String, Object>> findIndividualPayChannel(@Param("appId")String appId,@Param("merchantId") int merchantId);
}