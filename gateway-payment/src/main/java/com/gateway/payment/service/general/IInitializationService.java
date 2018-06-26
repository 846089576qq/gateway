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
package com.gateway.payment.service.general;

import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 服务初始化接口
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年8月10日
 */
public interface IInitializationService {

	/**
	 * 清理分布式锁key
	 * 
	 * @throws ServiceException
	 */
	public void cleanDistributeKeys() throws ServiceException;
}
