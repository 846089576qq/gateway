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

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gateway.PaymentServiceApplication;
import com.gateway.common.properties.ExtensionProperties;
import com.zitopay.foundation.common.util.BeanUtils;
import com.zitopay.foundation.config.dubbo.DubboAnnotationConfiguration;
import com.zitopay.foundation.config.springboot.SpringBootApplicationConfiguration;

/**
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2018年3月12日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource({ "classpath:prop/gateway-env.properties", "classpath:prop/gateway-payment.properties", "classpath:prop/gateway-extension.properties" })
@SpringBootTest(classes = PaymentServiceApplication.class)
@Import({ SpringBootApplicationConfiguration.class, DubboAnnotationConfiguration.class })
public class PropertiesTest {

	@Autowired
	ExtensionProperties extensionProperties;

	@Test
	public void testBanklist() {
		System.out.println(extensionProperties.getBankList());
		Map bankMap = BeanUtils.json2Bean(extensionProperties.getBankList(), Map.class);
		System.out.println(bankMap);
	}
}
