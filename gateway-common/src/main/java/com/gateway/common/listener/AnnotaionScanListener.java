package com.gateway.common.listener;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.gateway.common.annotaion.Gateway;
import com.gateway.common.annotaion.MerchantRegisterChannel;
import com.gateway.common.context.BeanContextUtil;

/**
 * 监听支付通道注解
 * 
 * 作者：王政 创建时间：2017年3月9日 下午2:09:02
 */
@Component
public class AnnotaionScanListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			Map<String, Object> beans = BeanContextUtil.getBeanMapWithAnnotation(Gateway.class);
			if(beans != null && beans.size() >0) {
				for (Object bean : beans.values()) {
					try {
						String gatewayid = parse(bean.getClass());
						if (StringUtils.isNotBlank(gatewayid)) {
							String prefix = StringUtils.substringBefore(gatewayid, "_");
							String suffix = StringUtils.substringAfter(gatewayid, "_");
							String[] gatewayids = StringUtils.split(suffix, "_");
							if (gatewayids != null && gatewayids.length > 0) {
								for (String gid : gatewayids) {
									BeanContextUtil.getGatewayBeanMap().put(prefix + "_" + gid, bean);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			beans = BeanContextUtil.getBeanMapWithAnnotation(MerchantRegisterChannel.class);
			if(beans != null && beans.size() >0) {
				for (Object bean : beans.values()) {
					try {
						String gatewayid = parse(bean.getClass());
						if (StringUtils.isNotBlank(gatewayid)) {
							String[] gatewayids = StringUtils.split(gatewayid, "_");
							if (gatewayids != null && gatewayids.length > 0) {
								for (String gid : gatewayids) {
									BeanContextUtil.getMerchantRegisterBeanMap().put(gid, bean);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}

	}

	public static String parse(Class<?> targetClass) throws Exception {
		if (targetClass.isAnnotationPresent(Gateway.class)) {
			Gateway gateway = (Gateway) targetClass.getAnnotation(Gateway.class);
			return gateway.gatewayId();
		}
		
		if (targetClass.isAnnotationPresent(MerchantRegisterChannel.class)) {
			MerchantRegisterChannel merchantRegisterChannel = (MerchantRegisterChannel) targetClass.getAnnotation(MerchantRegisterChannel.class);
			return merchantRegisterChannel.channelId();
		}
		return null;
	}
}
