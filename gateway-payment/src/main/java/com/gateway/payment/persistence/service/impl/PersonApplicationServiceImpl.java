package com.gateway.payment.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.gateway.payment.entity.PersonApplicationEntity;
import com.gateway.payment.persistence.service.IPersonApplicationService;

/**
 * 商户应用实现类
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Service
public class PersonApplicationServiceImpl  extends BaseGenericServiceImpl<PersonApplicationEntity> implements IPersonApplicationService {

	@Override
	public PersonApplicationEntity getZitopayPersonApplicationByAppid(Integer personId, String appid) {
		PersonApplicationEntity entity=new PersonApplicationEntity();
		entity.setPersonId(personId.toString());
		entity.setAppId(appid);
		return queryOne(entity);
	}

	@Override
	public PersonApplicationEntity selectByApplicationId(Integer applicationId) {
		PersonApplicationEntity entity=new PersonApplicationEntity();
		entity.setApplicationId(applicationId);
		return queryOne(entity);
	}

	@Override
	public PersonApplicationEntity getZitopayPersonApplicationByAppid(String appid) {
		PersonApplicationEntity entity=new PersonApplicationEntity();
		entity.setAppId(appid);
		return queryOne(entity);
	}

}
