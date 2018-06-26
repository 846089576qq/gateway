package com.gateway.payment.persistence.service;

import com.gateway.payment.entity.GatewayEntity;

/**
 * 通道接口
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
public interface IGatewayService extends IBaseGenericService<GatewayEntity> {

	/**
	 * 获取通道
	 * 
	 * @param gatewayId
	 *            融智付业务通道号
	 * @return
	 */
	public GatewayEntity getGateway(Integer gatewayId);

	/**
	 * 是否指定通道
	 * 
	 * @param gatewayId
	 * @param categoryCode
	 * @return
	 */
	public boolean isCategoryGateway(String gatewayId, String categoryCode);
	
	/**
	 * 获取指定商户指定通道类型的通道编号
	 * 
	 * @return
	 */
	public String getGatewayIdByPersonidAndCategoryCode(Integer personId, String categoryCode);

}