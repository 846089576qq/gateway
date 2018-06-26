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

import org.springframework.stereotype.Service;

import com.gateway.payment.entity.OrderEntity;
import com.gateway.payment.entity.TradeMonitorEntity;
import com.gateway.payment.persistence.service.ITradeMonitorService;

/**
 * 交易监控实现类
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Service
public class TradeMonitorServiceImpl  extends BaseGenericServiceImpl<TradeMonitorEntity> implements ITradeMonitorService {

	/**
	 * 初始化交易监控表数据
	 * 
	 * @param orderEntity
	 */
	public void initTradeMonitor(OrderEntity orderEntity) {
		TradeMonitorEntity zitopayTradeMonitor = new TradeMonitorEntity();
		zitopayTradeMonitor.setzOrderid(orderEntity.getOrderid());// 订单号
		zitopayTradeMonitor.setzOrderState(orderEntity.getState());// 订单状态
		zitopayTradeMonitor.setcIsNotify(0);// 是否回调商户
		zitopayTradeMonitor.setsIsNotify(0);// 是否回调
		zitopayTradeMonitor.setsIsCheck(0);// 是否检查
		saveSelective(zitopayTradeMonitor);
	}

	@Override
	public int insertMonitor(TradeMonitorEntity entity) {
		return saveSelective(entity);
	}

	@Override
	public int updateByZorderId(TradeMonitorEntity entity) {
		return updateSelective(entity);
	}

	@Override
	public TradeMonitorEntity selectByZorderid(String zOrderid) {
		TradeMonitorEntity entity=new TradeMonitorEntity();
		entity.setzOrderid(zOrderid);
		return queryOne(entity);
	}
}
