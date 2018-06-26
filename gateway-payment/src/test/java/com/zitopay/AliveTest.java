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
package com.zitopay;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gateway.PaymentServiceApplication;
import com.gateway.channel.entity.ChannelAliveEntity;
import com.gateway.channel.service.alive.IChannelAliveService;
import com.zitopay.foundation.common.exception.ServiceException;
import com.zitopay.foundation.config.dubbo.DubboAnnotationConfiguration;
import com.zitopay.foundation.config.springboot.SpringBootApplicationConfiguration;

/**
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2018年3月13日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource({ "classpath:prop/gateway-env.properties", "classpath:prop/gateway-payment.properties", "classpath:prop/gateway-extension.properties" })
@SpringBootTest(classes = PaymentServiceApplication.class)
@Import({ SpringBootApplicationConfiguration.class, DubboAnnotationConfiguration.class })
public class AliveTest {

	@Reference
	private IChannelAliveService channelAliveService;

	@Test
	public void testAliveTimeOut() {
		try {
			ChannelAliveEntity entity = channelAliveService.checkChannelAlive(new ChannelAliveEntity());
			System.out.println(entity);
			System.out.println("========================================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
