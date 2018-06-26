package com.gateway.common.properties;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 此方法已弃用，请参考：{@link com.zitopay.foundation.common.env.DigestEnvironment}
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年4月20日
 */
@Deprecated
public class CustomizedPropertyPlaceHolder extends PropertyPlaceholderConfigurer {

	private static final Map<String, String> ctxPropertiesMap = new HashMap<String, String>();

	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
		super.processProperties(beanFactory, props);
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = props.getProperty(keyStr);
			ctxPropertiesMap.put(keyStr, value);
		}
	}

	public static Map<String, String> getPropertiesMap() {
		return Collections.unmodifiableMap(ctxPropertiesMap);
	}

	public static String getProperty(String key) {
		return ctxPropertiesMap.get(key);
	}
}
