package com.gateway.payment.entity;

import com.zitopay.foundation.common.entity.AbstractEntity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name="zitopay_order_extend")
public class OrderExtendEntity extends AbstractEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderid;

    private Integer dfStatus;

    private BigDecimal dfAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public Integer getDfStatus() {
        return dfStatus;
    }

    public void setDfStatus(Integer dfStatus) {
        this.dfStatus = dfStatus;
    }

    public BigDecimal getDfAmount() {
        return dfAmount;
    }

    public void setDfAmount(BigDecimal dfAmount) {
        this.dfAmount = dfAmount;
    }
}