package com.gateway;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gateway.common.config.DubboAnnotationConfiguration;
import com.gateway.common.context.BeanContextUtil;
import com.zitopay.foundation.config.springboot.SpringBootApplicationConfiguration;

/**
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年4月19日
 */
@Configuration
@EnableAutoConfiguration //(exclude={DataSourceAutoConfiguration.class})
@EnableAspectJAutoProxy
@EnableTransactionManagement
@PropertySource({ "classpath:prop/gateway-env.properties", "classpath:prop/gateway-channel.properties" })
@Import({ SpringBootApplicationConfiguration.class, DubboAnnotationConfiguration.class })
public class ChannelServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(ChannelServiceApplication.class);

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

	private static CountDownLatch closeLatch;

	private static ApplicationContext ctx;

	public static void main(String[] args) {
		try {
			ctx = new SpringApplicationBuilder(ChannelServiceApplication.class).web(true).run(args);
			closeLatch = ctx.getBean(CountDownLatch.class);
			logger.info("Spring boot start success!");
			closeLatch.await();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			((AbstractApplicationContext) ctx).registerShutdownHook();
			closeLatch.countDown();
			logger.warn("Spring boot start failure!");
		}
	}
}
