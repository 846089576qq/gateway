package com.gateway.common.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.zitopay.foundation.config.dubbo.consumer.DubboConsumerConfiguration;

/**
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年1月17日
 */
@Configurable
@Import(value = { DubboConsumerConfiguration.class })
public class DubboClientAnnotaionCommonConfiguration {

	/**
	 * 启动motan
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
