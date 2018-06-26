package com.gateway.payment.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.gateway.payment.entity.OrderRecheckEntity;
import com.gateway.payment.persistence.service.IOrderRecheckService;

/**
 * 功能描述：
 * 创建人：licanhui.
 * 创建时间：2017/5/17.
 */
@Service
public class OrderRecheckServiceImpl  extends BaseGenericServiceImpl<OrderRecheckEntity> implements IOrderRecheckService{

    @Override
    public int saveRecord(OrderRecheckEntity entity) {
        return saveSelective(entity);
    }

    @Override
    public int updateByPrimaryKey(OrderRecheckEntity entity) {
        return updateSelective(entity);
    }


    @Override
    public OrderRecheckEntity selectByOrderId(String orderid) {
        OrderRecheckEntity entity=new OrderRecheckEntity();
        entity.setOrderid(orderid);
        return queryOne(entity);
    }
}
