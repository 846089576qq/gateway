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
package com.gateway.payment.service.general.impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gateway.payment.service.base.impl.BaseServiceImpl;
import com.gateway.payment.service.general.IInitializationService;
import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 服务初始化实现
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年8月10日
 */
@Service
public class InitializationServiceImpl extends BaseServiceImpl implements IInitializationService {

	private static final Logger logger = LoggerFactory.getLogger(InitializationServiceImpl.class);

	@PostConstruct
	@Override
	public void cleanDistributeKeys() throws ServiceException {
		/*try {
			redisTemplate.delete(SchedulerTaskConstant.EXPIREDORDERSCHEDULER);
			redisTemplate.delete(SchedulerTaskConstant.INITIAL2FAILUREORDERSCHEDULER);
			redisTemplate.delete(SchedulerTaskConstant.PAYMENTNOTIFY_SCHEDULER);
			redisTemplate.delete(SchedulerTaskConstant.UNPAYING2EXPIREDORDERSCHEDULER);
			redisTemplate.delete(SchedulerTaskConstant.UNPAYINGORDERSCHEDULER);
			logger.info("Distributed lock cleaning success!");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("Distributed lock cleaning failure!", e);
		}*/

	}
}
