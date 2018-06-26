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
package com.gateway.payment.persistence.service;

import java.util.List;
import java.util.Map;

import com.gateway.payment.entity.PersonGatewayEntity;

/**
 * 商户通道接口
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
public interface IPersonGatewayService  extends IBaseGenericService<PersonGatewayEntity>{

	/**
	 * 获取商户指定通道配置
	 * @param personId
	 * @param gatewayId
	 * @return
	 */
	public PersonGatewayEntity getZitopayPersonGateway(Integer personId, Integer gatewayId);

	/**
	 * 查询商户应用开通的通道
	 * @param appId 融智付配置应用ID
	 * @param personId 商户ID
	 * @return
	 */
	public List<Map<String, Object>> findIndividualPayChannel(String appId, int personId);

}
