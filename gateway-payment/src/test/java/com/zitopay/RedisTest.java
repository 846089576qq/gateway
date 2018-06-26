package com.zitopay;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gateway.PaymentServiceApplication;
import com.zitopay.foundation.common.exception.ServiceException;
import com.zitopay.foundation.config.dubbo.DubboAnnotationConfiguration;
import com.zitopay.foundation.config.springboot.SpringBootApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource({ "classpath:prop/gateway-env.properties", "classpath:prop/gateway-payment.properties" })
@SpringBootTest(classes = PaymentServiceApplication.class)
@Import({ SpringBootApplicationConfiguration.class, DubboAnnotationConfiguration.class })
public class RedisTest {

	/*@Autowired
	private RedisTemplate<String, String> redisTemplate;*/

	@Test
	public void clearRedisOrder() throws ServiceException {
		/*String orderid = "R2018012517565489234";
		redisTemplate.delete(String.format(CommonConstant.ZITOPAYORDER_REDIS_KEY, orderid));
		
		String value = redisTemplate.opsForValue().get(String.format(CommonConstant.ZITOPAYORDER_REDIS_KEY, orderid));
		System.out.println(value);*/
	}
}
