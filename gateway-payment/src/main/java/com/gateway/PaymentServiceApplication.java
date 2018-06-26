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
package com.gateway;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gateway.common.config.DubboAnnotationConfiguration;
import com.gateway.common.context.BeanContextUtil;
import com.gateway.config.PersistenceEngine;
import com.zitopay.foundation.config.springboot.SpringBootApplicationConfiguration;
import com.zitopay.foundation.persistence.config.RedisConfig;
import com.zitopay.foundation.web.config.RestTemplateConfig;

/**
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年4月20日
 */
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableAutoConfiguration
@EnableScheduling
@PropertySource({ "classpath:prop/gateway-env.properties", "classpath:prop/gateway-payment.properties", "classpath:prop/gateway-extension.properties"})
@Import({ SpringBootApplicationConfiguration.class, PersistenceEngine.class, DubboAnnotationConfiguration.class, RedisConfig.class,
		RestTemplateConfig.class})
public class PaymentServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(PaymentServiceApplication.class);

	@Bean
	public CountDownLatch countDownLatch() {
		return new CountDownLatch(1);

	}

	@Bean
	public BeanContextUtil beanContextUtil() {
		BeanContextUtil util = new BeanContextUtil();
		util.setApplicationContext(ctx);
		return util;
	}

	@Value("${threadPool.corePoolSize}")
	private int corePoolSize;

	@Value("${threadPool.keepAliveSeconds}")
	private int keepAliveSeconds;

	@Value("${threadPool.maxPoolSize}")
	private int maxPoolSize;

	@Value("${threadPool.queueCapacity}")
	private int queueCapacity;

	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setKeepAliveSeconds(keepAliveSeconds);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setRejectedExecutionHandler(new CallerRunsPolicy());
		return executor;
	}

	private static CountDownLatch closeLatch;

	private static ApplicationContext ctx;

	public static void main(String[] args) {
		try {
			ctx = new SpringApplicationBuilder(PaymentServiceApplication.class).web(true).run(args);
			closeLatch = ctx.getBean(CountDownLatch.class);
			logger.info("Spring boot start success!");
			closeLatch.await();
		} catch (Exception e) {
			closeLatch.countDown();
			((AbstractApplicationContext) ctx).registerShutdownHook();
			logger.warn("Spring boot start failure!");
			logger.error(e.getMessage(), e);
		}
	}
}
