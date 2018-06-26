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
package com.gateway.register.service.biz.yishi.impl;

import java.util.HashMap;
import java.util.Map;

import com.gateway.common.annotaion.MerchantRegisterChannel;
import com.gateway.common.constants.config.GatewayConstants;
import com.gateway.common.constants.param.MerchantRegisterResultParamConstant;
import com.gateway.common.constants.param.ZitopayMerchantRegisterParamConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.register.service.biz.yishi.IYiShiQuickRegisterService;
import com.zitopay.foundation.common.exception.ServiceException;
import com.zitopay.foundation.common.util.BeanUtils;

/**
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2018年3月14日
 */
@MerchantRegisterChannel(channelId = GatewayConstants.GATEWAY_YS_KJ)
public class YiShiQuickRegisterServiceImpl implements IYiShiQuickRegisterService {

	/*
	 * (non-Javadoc)
	 * @see com.zitopay.gateway.register.service.base.IBaseRegisterService#register(java.util.Map, java.lang.String)
	 */
	@Override
	public Map<String, Object> register(Map<String, String> paramMap, String serialNo) throws ServiceException {
		// 存储协议信息
		final Map map = BeanUtils.json2Bean(paramMap.get(ZitopayMerchantRegisterParamConstant.param_emplConfig), Map.class);

		return ResponseInfoEnum.调用成功.getMap(new HashMap<String, Object>() {

			private static final long serialVersionUID = 1L;
			{
				/* 参数格式:{ "privateKeyFile":"", "privateKeyPwd":"", "publicKeyFile":"", "merchantNo":""} */
				put(MerchantRegisterResultParamConstant.result_map, map);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see com.zitopay.gateway.register.service.base.IBaseRegisterService#update(java.util.Map, java.lang.String)
	 */
	@Override
	public Map<String, Object> update(Map<String, String> paramMap, String serialNo) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.zitopay.gateway.register.service.base.IBaseRegisterService#query(java.util.Map, java.lang.String)
	 */
	@Override
	public Map<String, Object> query(Map<String, String> paramMap, String serialNo) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
