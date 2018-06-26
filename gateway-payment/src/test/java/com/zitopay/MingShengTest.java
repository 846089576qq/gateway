package com.zitopay;

import com.alibaba.fastjson.JSON;
import com.gateway.PaymentServiceApplication;
import com.gateway.common.constants.status.OrderStatusConstant;
import com.gateway.payment.entity.OrderEntity;
import com.gateway.payment.persistence.service.IOrderService;
import com.zitopay.foundation.config.dubbo.DubboAnnotationConfiguration;
import com.zitopay.foundation.config.springboot.SpringBootApplicationConfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 项目名称:zitopay-gateway-2.0
 * 描述:
 * 创建人:ryw
 * 创建时间:2017/7/11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource({ "classpath:prop/gateway-env.properties", "classpath:prop/gateway-payment.properties" })
@SpringBootTest(classes=PaymentServiceApplication.class)
@Import({ SpringBootApplicationConfiguration.class, DubboAnnotationConfiguration.class })
public class MingShengTest {

    @Autowired
    public IOrderService orderService;

    /**手动更改订单支付完成后reids/数据库状态 针对民生测试时没有回调*/
    @Test
    public void changeState(){
        OrderEntity orderEntity = orderService.findByhreerderid("2017071416185373687");
        orderEntity.setState(OrderStatusConstant.STATUS_PAYED);
        BigDecimal ratemmoney = orderEntity.getTotalprice().multiply(orderEntity.getRate());
        orderEntity.setRatemmoney(ratemmoney);
        orderEntity.setAmount(orderEntity.getTotalprice().subtract(orderEntity.getRatemmoney()));
        orderService.updateByPrimaryKeySelective(orderEntity);
    }


    @Test
    public void testJson(){
        String json="{\"encryptData\":\"IHSynWKRn5lswJx8+6VEtrm4/m9Ke7LVcM6sLbxN+OQkeMeTfvLg4dmI7IoxLGGmNR3fr+oVhTg5Suyjt7+CDSbM+nibdHtHKp/T3uy2N1MGggITq9d5c9fxTmgBTdg27NdIQQ0QK7wFxOX85geWpbkrybf7dmLQ+KAoNfC1Amv4LVIrzoL8dcQPAsSyeumi2B0XqIRKP9B2QEVK277Z7gusL6W35i/lujdgQttBVRiCmFqFRAsJBkqTtWb8U7xgkS4L0hgZMywBLnUp1XGRIPTjPjy36Vyh5ZaIXfAokWE7QO82Dbw0cyTjXgXDLV4MPeoiqGCLC9pBHtYBNPWWjiC7bh3b85j0vkHHf6T7UQ4/6Hd1sIyJX/9ou3jJgZ83vbe9VUHttlGBnqNVKHdqyG7qomW0Kf/VEudjO1MlPmpfB3ojUnsFbDimLHy/vr0Yo5RiZm66wyqMRGgCoGOcmK1+WCArREvcrC+midbuSKuC6uIDFljxYX9KGU5Bf8tLgbfldxzTydKV+O6CmD2m+rUJL4dWUulBpUFePHPMlO3bu+VgaQ5EjzexCzrct3SC5Dyq2xGsRKVDWoq9tldeGPp27DSTb8NEcRD92XHFqSYJDp1sablB1UcoOLjKhmUMsznOHdtWM43nYnBurgLWl2kRkWoUtGLuxF0LOp72P4M4Jiy4ZWOsPx3Z2b36NKedZiijDeSZCm0mwFuWY0IfgwI19sojdye2zFazUl0fv3PakPllAOYfhI0RasfX2GzUwYaWeRWbJPowiLMz4+6JJYPwh07+0peK3AxPwSjtOzDUlYy3sHbEReRlGCFppVhQbUyCtsB2c7vpfW7oDjc8ZzJgzpO8esm1VfjrXmHM22DCLHQ3+DgxhzSFtgBJhzsuIhPv2hqfHLi5YkcrRjjqcyuZD37Jp0tZy3bkprvFv+f3fDw5WPRSZD48ggVumcLrp2l2hgi4Bywou38o2DnEW6FmGYs+owwJTKXHTxAKCGw=\",\"encryptKey\":\"jjBD86HUE0VNBcS+Zz4OcAtLX7CRc93nvh7xvH0cTc/gUC1N3FHMxOJQ+cXUr9DE4aEgDuOK70Z/pBQBnf4Q15NpE9UYtxckg4JAN+V5iBykZJpd7jTV8TCB5NObdD3GAUBW7SCL0vrPJx4f7gKwwx2938j5n7EPuMsxTVbdwqTSXofcnulQRpivGWFo9RMjdLGnc44ELucchAjYL2Ltsv2IWZWXcjbkNxMbRvyKUM/TFVjG4pcs0wjOgevQmHZYeJh8ZyT80dDh88F3vZDE82x2ezUCCzutu81VnDlLjHTO+tx5VywOa1ryfUbEfP4hQTKhCoZnQu80sLz/ekelCg==\",\"tranCode\":\"SMZF008\",\"reqMsgId\":\"2017072710592918879\",\"cooperator\":\"SMZF_SHHX_HD_T1\",\"signData\":\"EveyI2OL31+2qzN+9BJIc9fS7hW8pa8yX1rS/s8T2FdwshlleMKvPWOILJunE+eLJhcxmI5glEOo7Q6XGDt9/YK6KYFQRBokwFaTwFxHCJ2neQSEXuU3zXMmD6OCAgKYBQAQf2sQZBpbW/jVvA++jqC1eYojw051tThYm2snrUFOOJhf+GCJrq6YsuNHjxJtTmtDUDlrUpz/OwpZWJFLYIS/WmDUpGrmVYBYg9zPJoqsdOsLk2z7RKl22KtwTu2SmSeOo2mPstkRSSn6rk24ncPIR2k5V+xWBXHkMsh+WqS1jY+76gfnsDtyN2yapqh2LtLat12YqnuF+4yYYPICPQ==\",\"requestContent\":\"encryptData=IHSynWKRn5lswJx8%2B6VEtrm4%2Fm9Ke7LVcM6sLbxN%2BOQkeMeTfvLg4dmI7IoxLGGmNR3fr%2BoVhTg5Suyjt7%2BCDSbM%2BnibdHtHKp%2FT3uy2N1MGggITq9d5c9fxTmgBTdg27NdIQQ0QK7wFxOX85geWpbkrybf7dmLQ%2BKAoNfC1Amv4LVIrzoL8dcQPAsSyeumi2B0XqIRKP9B2QEVK277Z7gusL6W35i%2FlujdgQttBVRiCmFqFRAsJBkqTtWb8U7xgkS4L0hgZMywBLnUp1XGRIPTjPjy36Vyh5ZaIXfAokWE7QO82Dbw0cyTjXgXDLV4MPeoiqGCLC9pBHtYBNPWWjiC7bh3b85j0vkHHf6T7UQ4%2F6Hd1sIyJX%2F9ou3jJgZ83vbe9VUHttlGBnqNVKHdqyG7qomW0Kf%2FVEudjO1MlPmpfB3ojUnsFbDimLHy%2Fvr0Yo5RiZm66wyqMRGgCoGOcmK1%2BWCArREvcrC%2BmidbuSKuC6uIDFljxYX9KGU5Bf8tLgbfldxzTydKV%2BO6CmD2m%2BrUJL4dWUulBpUFePHPMlO3bu%2BVgaQ5EjzexCzrct3SC5Dyq2xGsRKVDWoq9tldeGPp27DSTb8NEcRD92XHFqSYJDp1sablB1UcoOLjKhmUMsznOHdtWM43nYnBurgLWl2kRkWoUtGLuxF0LOp72P4M4Jiy4ZWOsPx3Z2b36NKedZiijDeSZCm0mwFuWY0IfgwI19sojdye2zFazUl0fv3PakPllAOYfhI0RasfX2GzUwYaWeRWbJPowiLMz4%2B6JJYPwh07%2B0peK3AxPwSjtOzDUlYy3sHbEReRlGCFppVhQbUyCtsB2c7vpfW7oDjc8ZzJgzpO8esm1VfjrXmHM22DCLHQ3%2BDgxhzSFtgBJhzsuIhPv2hqfHLi5YkcrRjjqcyuZD37Jp0tZy3bkprvFv%2Bf3fDw5WPRSZD48ggVumcLrp2l2hgi4Bywou38o2DnEW6FmGYs%2BowwJTKXHTxAKCGw%3D&encryptKey=jjBD86HUE0VNBcS%2BZz4OcAtLX7CRc93nvh7xvH0cTc%2FgUC1N3FHMxOJQ%2BcXUr9DE4aEgDuOK70Z%2FpBQBnf4Q15NpE9UYtxckg4JAN%2BV5iBykZJpd7jTV8TCB5NObdD3GAUBW7SCL0vrPJx4f7gKwwx2938j5n7EPuMsxTVbdwqTSXofcnulQRpivGWFo9RMjdLGnc44ELucchAjYL2Ltsv2IWZWXcjbkNxMbRvyKUM%2FTFVjG4pcs0wjOgevQmHZYeJh8ZyT80dDh88F3vZDE82x2ezUCCzutu81VnDlLjHTO%2Btx5VywOa1ryfUbEfP4hQTKhCoZnQu80sLz%2FekelCg%3D%3D&tranCode=SMZF008&reqMsgId=2017072710592918879&cooperator=SMZF_SHHX_HD_T1&ext=&signData=EveyI2OL31%2B2qzN%2B9BJIc9fS7hW8pa8yX1rS%2Fs8T2FdwshlleMKvPWOILJunE%2BeLJhcxmI5glEOo7Q6XGDt9%2FYK6KYFQRBokwFaTwFxHCJ2neQSEXuU3zXMmD6OCAgKYBQAQf2sQZBpbW%2FjVvA%2B%2BjqC1eYojw051tThYm2snrUFOOJhf%2BGCJrq6YsuNHjxJtTmtDUDlrUpz%2FOwpZWJFLYIS%2FWmDUpGrmVYBYg9zPJoqsdOsLk2z7RKl22KtwTu2SmSeOo2mPstkRSSn6rk24ncPIR2k5V%2BxWBXHkMsh%2BWqS1jY%2B76gfnsDtyN2yapqh2LtLat12YqnuF%2B4yYYPICPQ%3D%3D\"}";
        Map map = JSON.parseObject(json, Map.class);
        System.out.println(map);
    }

}
