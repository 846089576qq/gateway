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

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.persistence.service.IPersonService;

/**
 * 商户信息操作实现类
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Service
public class PersonServiceImpl extends BaseGenericServiceImpl<PersonEntity> implements IPersonService {

	@Override
	public PersonEntity getZitopayPersonByPid(String pid) {
		if(StringUtils.isNotBlank(pid)) {
			PersonEntity entity=new PersonEntity();
			entity.setPid(pid);
			return queryOne(entity);
		}else {
			return null;
		}
	}
	
	@Override
	public PersonEntity getZitopayPersonByLoginname(String loginname) {
		if(StringUtils.isNotBlank(loginname)) {
			PersonEntity entity=new PersonEntity();
			entity.setLoginname(loginname);
			return queryOne(entity);
		}else {
			return null;
		}
	}

	@Override
	public PersonEntity queryByPersonId(int personId) {
		return queryById(personId);
	}

}
