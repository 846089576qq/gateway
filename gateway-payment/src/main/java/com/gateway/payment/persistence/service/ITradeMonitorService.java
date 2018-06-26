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

import com.gateway.payment.entity.OrderEntity;
import com.gateway.payment.entity.TradeMonitorEntity;

/**
 * 交易监控接口
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
public interface ITradeMonitorService extends IBaseGenericService<TradeMonitorEntity> {
	
	/**
	 * 初始化交易监控表数据
	 * 
	 * @param orderEntity
	 */
	public void initTradeMonitor(OrderEntity orderEntity);

	int insertMonitor(TradeMonitorEntity entity);

	int updateByZorderId(TradeMonitorEntity entity);

	TradeMonitorEntity selectByZorderid(String zOrderid);

}
