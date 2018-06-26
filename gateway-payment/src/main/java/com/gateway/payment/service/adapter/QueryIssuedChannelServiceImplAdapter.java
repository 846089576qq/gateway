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

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.gateway.common.constants.CommonConstant;
import com.gateway.common.constants.param.IssuedParamConstant;
import com.gateway.common.constants.status.DisabledStatusConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.IssuedEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 代付通道查询服务实现
 * 
 * 作者：王政 创建时间：2017年3月6日 下午5:59:29
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class QueryIssuedChannelServiceImplAdapter extends BaseServiceImplAdapter implements IQueryIssuedChannelServiceAdapter {

	@Override
	public Map<String, Object> checkQueryChannelParameter(Map<String, String> paramMap, String serialNo) throws ServiceException {
		try {
			logger.info("开始验证参数,内容:{}.", paramMap);
			if (paramMap == null || paramMap.isEmpty()) {
				return ResponseInfoEnum.参数为空.getMap();
			}

			String merchantId = paramMap.get(IssuedParamConstant.param_merchantId);// 商户id，由融智付分配
			if (StringUtils.isEmpty(merchantId)) {
				return ResponseInfoEnum.参数为空.getMap("参数[" + IssuedParamConstant.param_merchantId + "]不能为空");
			}

			Integer gatewayId = Integer.valueOf(paramMap.get(IssuedParamConstant.param_gatewayId)); // 通道ID，由融智付分配
			if (null == gatewayId) {
				return ResponseInfoEnum.参数为空.getMap("参数[" + IssuedParamConstant.param_gatewayId + "]不能为空");
			}

			String orderId = paramMap.get(IssuedParamConstant.param_issued_id); // 订单号
			if (StringUtils.isBlank(orderId)) {
				return ResponseInfoEnum.参数为空.getMap("参数[" + IssuedParamConstant.param_issued_id + "]不能为空");
			}
			return ResponseInfoEnum.调用成功.getMap();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",查询参数校验异常,请检查!", e);
		}
	}

	@Override
	public Map<String, Object> checkQueryChannelPermission(String merchantId, String gatewayId, String serialNo) throws ServiceException {
		try {
			Map<String, Object> returnMap = new HashMap<String, Object>();
			// 验证商户是否存在以及启用
			PersonEntity zitopayPerson = personService.getZitopayPersonByPid(merchantId);
			if (null == zitopayPerson) {
				return ResponseInfoEnum.商户不存在.getMap();
			}
			if (DisabledStatusConstant.STATUS_DISABLED.equals(zitopayPerson.getStatus())) {
				return ResponseInfoEnum.商户已禁用.getMap();
			}

			if (StringUtils.isNotBlank(gatewayId)) {
				// 验证通道是否存在以及启用
				final GatewayEntity zitopayGeteway = gatewayService.getGateway(Integer.parseInt(gatewayId)); // 商户通道表
				if (zitopayGeteway == null) {
					return ResponseInfoEnum.通道不存在.getMap();
				}
				if (DisabledStatusConstant.STATUS_DISABLED.equals(zitopayGeteway.getDisable())) {
					return ResponseInfoEnum.通道已禁用.getMap();
				}

				// 验证该商户是否配置该通道配置启用
				PersonGatewayEntity zitopayPersonGeteway = personGatewayService.getZitopayPersonGateway(zitopayPerson.getId(), Integer.parseInt(gatewayId));
				if (null == zitopayPersonGeteway) {
					return ResponseInfoEnum.商户通道不存在.getMap();
				}
				if (DisabledStatusConstant.STATUS_DISABLED.equals(zitopayPersonGeteway.getDisable())) {
					return ResponseInfoEnum.商户通道被禁用.getMap();
				}
				returnMap.put(CommonConstant.ZITOPAY_ENTITY_GATEWAY, zitopayGeteway);
				returnMap.put(CommonConstant.ZITOPAY_ENTITY_PERSON_GATEWAY, zitopayPersonGeteway);
			}

			returnMap.put(CommonConstant.ZITOPAY_ENTITY_PERSON, zitopayPerson);
			return ResponseInfoEnum.调用成功.getMap(returnMap);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",查询通道权限校验异常,请检查!", e);
		}
	}

	@Override
	public Map<String, Object> execChannelQuery(PersonEntity zitopayPerson, GatewayEntity zitopayGeteway, PersonGatewayEntity zitopayPersonGeteway, Map<String, String> paramMap, String serialNo) throws ServiceException {
		try {
			String issuedid = paramMap.get(IssuedParamConstant.param_issued_id); // 融智付订单号
			IssuedEntity issuedEntity = issuedService.getByIssuedid(issuedid);
			if (null == issuedEntity) {
				return ResponseInfoEnum.订单不存在.getMap();
			}
			return executeQuery(issuedEntity, serialNo);// 处理查询、通知商户、分润等;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",查询通道异常,请检查!", e);
		}
	}
}