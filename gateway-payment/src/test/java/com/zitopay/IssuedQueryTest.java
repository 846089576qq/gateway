package com.zitopay;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gateway.PaymentServiceApplication;
import com.gateway.payment.persistence.service.IIssuedService;
import com.gateway.payment.service.task.impl.OrderPollingTaskServiceImpl;
import com.zitopay.foundation.common.exception.ServiceException;
import com.zitopay.foundation.config.dubbo.DubboAnnotationConfiguration;
import com.zitopay.foundation.config.springboot.SpringBootApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource({ "classpath:prop/gateway-env.properties", "classpath:prop/gateway-payment.properties" })
@SpringBootTest(classes=PaymentServiceApplication.class)
@Import({ SpringBootApplicationConfiguration.class, DubboAnnotationConfiguration.class })
public class IssuedQueryTest {

    @Autowired
    public OrderPollingTaskServiceImpl orderPollingTaskServiceImpl;
    
    @Autowired
    public IIssuedService iIssuedService;

    @Test
    public void query() throws ServiceException{
    	System.out.println("结果：" + orderPollingTaskServiceImpl.executeQuery(iIssuedService.getByIssuedid("I2018050919095767902"), "test"));
    }


}
