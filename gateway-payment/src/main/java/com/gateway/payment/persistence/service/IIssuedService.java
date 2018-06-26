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

import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.IssuedEntity;
import com.gateway.payment.entity.PersonEntity;

/**
 * 代付的接口
 */
public interface IIssuedService {
	
	public IssuedEntity getByIssuedid(String Issuedid);
	
	public IssuedEntity findByhreerderid(String threeorderid);
	
    /**
     * 更新转账流水信息
     *
     * @param amount 提现金额
     * @param fee 手续费
     * @param payNo 流水号
     * @param state 状态
     * @return
     */
    public int updateOrder(IssuedEntity IssuedEntity);

	public int countOrderByOrderidinf(String orderidinf);
	
	public List<IssuedEntity> queryOrderByStatus(Integer state);
	
	/**
	 * 创建转账订单
	 * 
	 * @param merchantId
	 * @param orderidinf
	 * @param posttime
	 * @param totalPrice
	 * @param orderTitle
	 * @param returnUrl
	 * @param bgRetUrl
	 * @return
	 */
	public IssuedEntity createIssuedOrder(PersonEntity zitopayPerson, GatewayEntity zitopayGeteway, String orderidinf, Map<String, String> paramMap);

}
