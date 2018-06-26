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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gateway.payment.entity.AccountSystemCashEntity;
import com.gateway.payment.persistence.mapper.IAccountSystemCashMapper;
import com.gateway.payment.persistence.service.IAccountSystemCashService;

/**
 * 提现的实现类
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Service
public class AccountSystemCashServiceImpl extends BaseGenericServiceImpl<AccountSystemCashEntity> implements IAccountSystemCashService {

	@Autowired
	private IAccountSystemCashMapper iAccountSystemCashMapper;

	@Override
	public AccountSystemCashEntity getZitopayAccountSystemCash(String personId, String gatewayId, String orderNo) {
		return null;
	}

	@Override
	public int updateOrder(String drawAmount, String drawFee, String settleDate, String orderNo, String state, String note) {
		return 0;
	}
}