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
package com.gateway.payment.service.adapter;

import java.util.HashMap;
import java.util.Map;

import com.gateway.common.constants.CommonConstant;
import com.gateway.common.constants.param.PaymentParamConstant;
import com.gateway.common.constants.status.OrderStatusConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.entity.OrderEntity;
import com.gateway.payment.service.adapter.IBaseServiceAdapter;
import com.gateway.payment.service.base.impl.BaseServiceImpl;

/**
 * BaseService 适配器
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月22日
 */
public class BaseServiceImplAdapter extends BaseServiceImpl implements IBaseServiceAdapter {
	

	public Map<String, Object> findOrder(Map<String, String> params) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		final String merorderid = params.get(PaymentParamConstant.param_merchantOrderId);// 订单信息
		OrderEntity orderEntity = orderService.findByMerchantOrderId(merorderid);
		if (orderEntity == null) {// 订单信息为null
			return ResponseInfoEnum.订单不存在.getMap(new HashMap<String, Object>() {

				private static final long serialVersionUID = 1L;
				{
					put(CommonConstant.CASHIER_POLLING_MSG, "根据商户订单id" + merorderid + "没有查询到信息");
				}
			});
		}
		// 取有用数据返回,为了安全性考虑,建议Controller不要全部返回
		dataMap.put(CommonConstant.CASHIER_ZITOPA_ORDER, orderEntity);// 订单表
		int state = orderEntity.getState();// 订单表
											// 订单状态（0初始状态,1成功，2未支付，3失败，4失效，5已退款,6部分退款）
		if (OrderStatusConstant.STATUS_PAYED == state) {
			dataMap.put(CommonConstant.CASHIER_POLLING_MSG, "订单支付成功");
		} else if (OrderStatusConstant.STATUS_UNPAYING == state) {
			dataMap.put(CommonConstant.CASHIER_POLLING_MSG, "等待订单支付");
			return ResponseInfoEnum.订单未支付.getMap(dataMap);
		} else {
			dataMap.put(CommonConstant.CASHIER_POLLING_MSG, "订单过期/失败");
			return ResponseInfoEnum.订单状态失败.getMap(dataMap);
		}
		return ResponseInfoEnum.调用成功.getMap(dataMap);
	}

}
