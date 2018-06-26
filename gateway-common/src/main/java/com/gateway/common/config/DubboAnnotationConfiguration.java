package com.gateway.common.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.zitopay.foundation.config.dubbo.consumer.DubboConsumerConfiguration;
import com.zitopay.foundation.config.dubbo.provider.DubboProviderConfiguration;

/**
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年1月18日
 */
@Configurable
@Import(value = { DubboProviderConfiguration.class, DubboConsumerConfiguration.class })
public class DubboAnnotationConfiguration {

	/**
	 * 启动dubbo
	 * 
	 * @return
	 */
	@Bean
	public AnnotationBean annotationBean() {
		AnnotationBean bean = new AnnotationBean();
		bean.setPackage("com.gateway");
		return bean;
	}

}
