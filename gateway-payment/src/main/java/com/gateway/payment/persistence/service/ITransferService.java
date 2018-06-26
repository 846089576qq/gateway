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


import java.math.BigDecimal;
import java.util.Map;

import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.TransferEntity;

/**
 * 转账的接口
 */
public interface ITransferService {
	
	public TransferEntity getByTransferid(String transferid);
	
    /**
     * 更新转账流水信息
     *
     * @param amount 提现金额
     * @param fee 手续费
     * @param payNo 流水号
     * @param state 状态
     * @return
     */
    public int updateOrder(TransferEntity transferEntity, BigDecimal fee, String payNo, String topPayNo, Integer state);

	public int countOrderByOrderidinf(String orderidinf);
	
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
	public TransferEntity createTransferOrder(PersonEntity zitopayPerson, GatewayEntity zitopayGeteway, String orderidinf, Map<String, String> paramMap);

}
