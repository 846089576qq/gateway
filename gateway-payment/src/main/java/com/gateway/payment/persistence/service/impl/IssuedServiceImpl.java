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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gateway.common.constants.param.IssuedParamConstant;
import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.IssuedEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.persistence.mapper.IIssuedMapper;
import com.gateway.payment.persistence.service.IIssuedService;
import com.zitopay.foundation.common.util.PKUtils;

/**
 * 代付的实现类
 * 
 */
@Service
public class IssuedServiceImpl extends BaseGenericServiceImpl<IssuedEntity> implements IIssuedService {

	@Autowired
	private IIssuedMapper IssuedMapper;
	
	@Override
	public int countOrderByOrderidinf(String orderidinf) {
		return IssuedMapper.countOrderByOrderidinf(orderidinf);
	}

	@Override
	public int updateOrder(IssuedEntity IssuedEntity) {
		return updateSelective(IssuedEntity);
	}

	@Override
	public IssuedEntity createIssuedOrder(PersonEntity zitopayPerson, GatewayEntity zitopayGeteway, String orderidinf, Map<String, String> paramMap) {
		String totalPrice = paramMap.get(IssuedParamConstant.param_orderAmount);
		IssuedEntity IssuedEntity = new IssuedEntity();
		IssuedEntity.setIssuedid("I" + String.valueOf(PKUtils.getInstance().longPK()));// 融智付订单号
		IssuedEntity.setPersonid(zitopayPerson.getId());// 商户id（商户表主键）
		IssuedEntity.setThreeorderid(String.valueOf(PKUtils.getInstance().longPK()));// 下单时创建第三方订单号
		BigDecimal bg = new BigDecimal(totalPrice);
		IssuedEntity.setTotalprice(bg);// 支付金额
		IssuedEntity.setAccountname(paramMap.get(IssuedParamConstant.param_custRealName));
		IssuedEntity.setAccountno(paramMap.get(IssuedParamConstant.param_custCardNo));
		IssuedEntity.setBankname(paramMap.get(IssuedParamConstant.param_bankName));
		IssuedEntity.setBranchbankname(paramMap.get(IssuedParamConstant.param_branchBankName));
		IssuedEntity.setIdentitycode(paramMap.get(IssuedParamConstant.param_custIdentityCode));
		IssuedEntity.setContact(paramMap.get(IssuedParamConstant.param_custCardMobile));
		IssuedEntity.setBankcity(paramMap.get(IssuedParamConstant.param_bankCity));
		IssuedEntity.setBankprovince(paramMap.get(IssuedParamConstant.param_bankProvince));
		IssuedEntity.setCreatetime(new Date());
		// 订单类参数
		IssuedEntity.setState(0);// 订单状态 0：初始状态
		IssuedEntity.setOrderidinf(orderidinf);// 商户传过来的订单号
		if (zitopayGeteway != null) {
			IssuedEntity.setGatewayid(zitopayGeteway.getId());
		}
		
		saveSelective(IssuedEntity);
		return IssuedEntity;
	}

	@Override
	public IssuedEntity getByIssuedid(String Issuedid) {
		return queryById(Issuedid);
	}

	@Override
	public IssuedEntity findByhreerderid(String threeorderid) {
		IssuedEntity issuedEntity = new IssuedEntity();
		issuedEntity.setThreeorderid(threeorderid);
		return queryOne(issuedEntity);
	}

	@Override
	public List<IssuedEntity> queryOrderByStatus(Integer state) {
		IssuedEntity issuedEntity = new IssuedEntity();
		issuedEntity.setState(state);
		return queryListByWhere(issuedEntity);
	}

}