package com.gateway.payment.persistence.service;

import com.gateway.payment.entity.PersonApplicationEntity;


/**
 * 商户应用接口
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
public interface IPersonApplicationService extends IBaseGenericService<PersonApplicationEntity>{
	
	/**
	 * 获取商户应用
	 * @param appid
	 * @return
	 */
	public PersonApplicationEntity getZitopayPersonApplicationByAppid(String appid);

	/**
	 * 获取商户应用
	 * @param personId
	 * @param appid
	 * @return
	 */
	public PersonApplicationEntity getZitopayPersonApplicationByAppid(Integer personId, String appid);

	public PersonApplicationEntity selectByApplicationId(Integer applicationId);

}