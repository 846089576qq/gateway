/**
 * Copyright [2015-2017]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.gateway.payment.persistence.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gateway.payment.entity.PersonGatewayEntity;
import com.gateway.payment.persistence.mapper.IPersonGatewayMapper;
import com.gateway.payment.persistence.service.IPersonGatewayService;

/**
 * 商户通道实现类
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Service
public class PersonGatewayServiceImpl  extends BaseGenericServiceImpl<PersonGatewayEntity> implements IPersonGatewayService {

	@Autowired
	private IPersonGatewayMapper iPersonGatewayMapper;

	@Override
	public PersonGatewayEntity getZitopayPersonGateway(Integer personId, Integer gatewayId) {
		PersonGatewayEntity entity=new PersonGatewayEntity();
		entity.setPersonid(personId);
		entity.setGetewayid(gatewayId);
		entity.setDisable(0);
		return queryOne(entity);
	}

	@Override
	public List<Map<String, Object>> findIndividualPayChannel(String appId, int personId) {
		return iPersonGatewayMapper.findIndividualPayChannel(appId,personId);
	}

}
