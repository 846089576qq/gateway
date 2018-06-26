package com.gateway.payment.persistence.service;

import com.gateway.payment.entity.OrderRecheckEntity;

/**
 * 功能描述：
 * 创建人：licanhui.
 * 创建时间：2017/5/17.
 */
public interface IOrderRecheckService extends IBaseGenericService<OrderRecheckEntity> {
    int saveRecord(OrderRecheckEntity entity);

    int updateByPrimaryKey(OrderRecheckEntity entity);

    OrderRecheckEntity selectByOrderId(String orderid);
}
