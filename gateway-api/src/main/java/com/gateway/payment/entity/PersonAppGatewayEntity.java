package com.gateway.payment.entity;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "zitopay_person_app_geteway")
public class PersonAppGatewayEntity {
	
	@Column(name = "`order`")
    private Integer order;

    private Integer perGetewayId;

    private Integer applicationId;

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getPerGetewayId() {
        return perGetewayId;
    }

    public void setPerGetewayId(Integer perGetewayId) {
        this.perGetewayId = perGetewayId;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }
}