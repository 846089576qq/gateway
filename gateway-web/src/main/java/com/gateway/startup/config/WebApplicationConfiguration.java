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
package com.gateway.startup.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.gateway.common.config.DubboClientAnnotaionCommonConfiguration;
import com.zitopay.foundation.common.env.DigestEnvironment;
import com.zitopay.foundation.web.config.RestTemplateConfig;

/**
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年4月20日
 */
@Configuration
@EnableWebMvc
@ComponentScan({ "com.zitopay" })
@PropertySource(value = { "classpath:prop/gateway-web.properties", "classpath:prop/gateway-env.properties","classpath:prop/gateway-extension.properties"})
@Import(value = { DubboClientAnnotaionCommonConfiguration.class,RestTemplateConfig.class })
public class WebApplicationConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/source/**").addResourceLocations("/source/");
		registry.addResourceHandler("/error/**").addResourceLocations("/error/");
	}

	@Bean
	public ViewResolver configureViewResolver() {
		InternalResourceViewResolver viewResolve = new InternalResourceViewResolver();
		viewResolve.setPrefix("/WEB-INF/views/");
		viewResolve.setSuffix(".jsp");
		return viewResolve;
	}

	@Bean
	public DigestEnvironment digesteEnvironment() {
		return new DigestEnvironment();
	}
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.mediaType("json", MediaType.valueOf("application/json"));
		configurer.mediaType("xml", MediaType.valueOf("application/xml"));
		configurer.mediaType("html", MediaType.valueOf("text/html"));
		configurer.mediaType("*", MediaType.valueOf("*/*"));
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
		List<MediaType> list = new ArrayList<MediaType>();
		list.add(new MediaType("text", "plain", Charset.forName("UTF-8")));
		list.add(new MediaType("*", "*", Charset.forName("UTF-8")));
		stringConverter.setSupportedMediaTypes(list);
		stringConverter.setWriteAcceptCharset(false);

		/*
		 * FastJsonHttpMessageConverter jsonConverter = new FastJsonHttpMessageConverter(); List<MediaType> jsonList = new
		 * ArrayList<MediaType>(); jsonList.add(MediaType.valueOf("application/json;charset=UTF-8"));
		 * jsonList.add(MediaType.valueOf("text/plain;charset=utf-8")); jsonList.add(MediaType.valueOf("text/html;charset=utf-8"));
		 * jsonConverter.setSupportedMediaTypes(jsonList); jsonConverter.setFeatures(new
		 * SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat});
		 */

		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		List<MediaType> jsonList = new ArrayList<MediaType>();
		jsonList.add(MediaType.valueOf("application/json;charset=UTF-8"));
		jsonList.add(MediaType.valueOf("text/plain;charset=UTF-8"));
		jsonList.add(MediaType.valueOf("text/html;charset=UTF-8"));
		jsonConverter.setSupportedMediaTypes(jsonList);
		converters.add(stringConverter);
		converters.add(jsonConverter);

	}
}
