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
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gateway.common.constants.param.TransferParamConstant;
import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.TransferEntity;
import com.gateway.payment.persistence.mapper.ITransferMapper;
import com.gateway.payment.persistence.service.ITransferService;
import com.zitopay.foundation.common.util.PKUtils;

/**
 * 转账的实现类
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Service
public class TransferServiceImpl extends BaseGenericServiceImpl<TransferEntity> implements ITransferService {

	@Autowired
	private ITransferMapper transferMapper;
	
	@Override
	public int countOrderByOrderidinf(String orderidinf) {
		return transferMapper.countOrderByOrderidinf(orderidinf);
	}

	@Override
	public int updateOrder(TransferEntity transferEntity, BigDecimal fee, String payNo, String topPayNo, Integer state) {
		transferEntity.setPayno(payNo);
		transferEntity.setToppayno(topPayNo);
		transferEntity.setState(state);
		transferEntity.setPoundage(fee);
		return updateSelective(transferEntity);
	}

	@Override
	public TransferEntity createTransferOrder(PersonEntity zitopayPerson, GatewayEntity zitopayGeteway, String orderidinf, Map<String, String> paramMap) {
		String totalPrice = paramMap.get(TransferParamConstant.param_orderAmount);
		String orderTitle = paramMap.get(TransferParamConstant.param_orderTitle);
		TransferEntity transferEntity = new TransferEntity();
		transferEntity.setTransferid("T" + String.valueOf(PKUtils.getInstance().longPK()));// 融智付订单号
		transferEntity.setPersonid(zitopayPerson.getId());// 商户id（商户表主键）
		transferEntity.setThreeorderid(String.valueOf(PKUtils.getInstance().longPK()));// 下单时创建第三方订单号
		BigDecimal bg = new BigDecimal(totalPrice);
		transferEntity.setTotalprice(bg);// 支付金额
		// 商品参数
		transferEntity.setGoodsname(orderTitle);// 商户订单标题
		// 订单类参数
		transferEntity.setState(0);// 订单状态 0：初始状态
		transferEntity.setOrderidinf(orderidinf);// 商户传过来的订单号
		if (zitopayGeteway != null) {
			transferEntity.setGatewayid(zitopayGeteway.getId());
		}
		
		saveSelective(transferEntity);
		return transferEntity;
	}

	@Override
	public TransferEntity getByTransferid(String transferid) {
		return queryById(transferid);
	}

}