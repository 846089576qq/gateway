package com.gateway.common.context;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 取得spring上下文，获取bean对象以及动态增加配置文件到spring容器
 * 
 * @author xiaosw 2014-9-3
 */
public class BeanContextUtil implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(BeanContextUtil.class);

	private static ConfigurableApplicationContext applicationContext = null;

	private static final Map<String, Object> gatewayBeanMap = new ConcurrentHashMap<String, Object>();
	
	private static final Map<String, Object> MerchantRegisterBeanMap = new ConcurrentHashMap<String, Object>();

	/**
	 * 根据gatewayid获取对应配置的serviceBean
	 * 
	 * @param gatewayId
	 * @return
	 */
	public static <T> T getGatewayBean(String gatewayId) {
		return (T) gatewayBeanMap.get(gatewayId);
	}

	public static Map<String, Object> getGatewayBeanMap() {
		return gatewayBeanMap;
	}

	/**
	 * 根据gatewayid获取对应配置的serviceBean
	 * 
	 * @param gatewayId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getMerchantRegisterBean(String gatewayId) {
		return (T) MerchantRegisterBeanMap.get(gatewayId);
	}

	public static Map<String, Object> getMerchantRegisterBeanMap() {
		return MerchantRegisterBeanMap;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		BeanContextUtil.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}

	/**
	 * 从spring上下文获取bean
	 * 
	 * @author xiaosw 2014-9-2
	 * @param beanName
	 * @return
	 */
	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	/**
	 * 从spring上下文获取beanMap
	 * 
	 * @param clz
	 * @return
	 */
	public static Map<String, Object> getBeanMapWithAnnotation(Class<? extends Annotation> clz) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		if(applicationContext != null) {
			Map<String, Object> proxyMap = applicationContext.getBeansWithAnnotation(clz);
			if(proxyMap != null && proxyMap.size() > 0) {
				for (Iterator<String> iterator = proxyMap.keySet().iterator(); iterator.hasNext();) {
					String key = iterator.next();
					Object obj = getTargetObject(proxyMap.get(key));
					objMap.put(key, obj);
				}
			}
		}
		return objMap;
	}

	private static Object getTargetObject(Object proxy) {
		try {
			if (!AopUtils.isAopProxy(proxy)) {// 不是代理对象
				return proxy;
			}
			if (AopUtils.isCglibProxy(proxy)) {// cglib代理对象
				return getCglibProxyTargetObject(proxy);
			}
			if (AopUtils.isJdkDynamicProxy(proxy)) {// jdk动态代理
				return getJdkDynamicProxyTargetObject(proxy);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 获取cglib代理目标对象
	 * 
	 * @param proxy
	 * @return
	 * @throws Exception
	 */
	private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
		try {
			Field field = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
			field.setAccessible(true);
			Object dynamicAdvisedInterceptor = field.get(proxy);
			Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
			advised.setAccessible(true);
			return ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
		} catch (SecurityException e) {
			logger.error(e.getMessage(), e);
			throw new Exception("The method of object with cglib can't read", e);
		} catch (NoSuchFieldException e) {
			logger.error(e.getMessage(), e);
			throw new Exception("Find filed in object of cglib has error", e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
			throw new Exception("Inject parameter to object of cglib has error", e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			throw new Exception("Access object of cglib has error", e);
		}
	}

	/**
	 * 获取JDK动态代理目标对象
	 * 
	 * @param proxy
	 * @return
	 * @throws Exception
	 */
	private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
		try {
			Field field = proxy.getClass().getSuperclass().getDeclaredField("h");
			field.setAccessible(true);
			AopProxy aopProxy = (AopProxy) field.get(proxy);
			Field advised = aopProxy.getClass().getDeclaredField("advised");
			advised.setAccessible(true);
			Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
			return target;
		} catch (SecurityException e) {
			logger.error(e.getMessage(), e);
			throw new Exception("The method of object with jdk dynamic can't read", e);
		} catch (NoSuchFieldException e) {
			logger.error(e.getMessage(), e);
			throw new Exception("Find filed in object of jdk dynamic has error", e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
			throw new Exception("Inject parameter to object of jdk dynamic has error", e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			throw new Exception("Access object of jdk dynamic has error", e);
		}

	}

	/**
	 * 从spring上下文获取bean
	 * 
	 * @author xiaosw 2014-9-3
	 * @param clz
	 * @return
	 */
	public static <T> T getBean(Class<T> clz) {
		return (T) applicationContext.getBean(clz);
	}

}
