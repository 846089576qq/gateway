package com.gateway.payment.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 
 */
@Table(name = "alwaypay_transfer")
public class TransferEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    /**
     * 转账单号
     */
	@Id
    private String transferid;

    /**
     * 商户编号
     */
    private Integer personid;

    /**
     * 通道编号
     */
    private Integer gatewayid;

    /**
     * 第三方订单号
     */
    private String threeorderid;
    
    /**
     * 商户订单号
     */
    private String orderidinf;

    /**
     * 上游交易号
     */
    private String payno;

    /**
     * 上游流水号
     */
    private String toppayno;

    /**
     * 转账金额
     */
    private BigDecimal totalprice;

    /**
     * 手续费
     */
    private BigDecimal poundage;

    /**
     * 转账描述
     */
    private String goodsname;

    /**
     * 转账状态
     */
    private Integer state;

    /**
     * 备注
     */
    private String remark;

    public String getOrderidinf() {
		return orderidinf;
	}

	public void setOrderidinf(String orderidinf) {
		this.orderidinf = orderidinf;
	}

	public String getTransferid() {
        return transferid;
    }

    public void setTransferid(String transferid) {
        this.transferid = transferid;
    }

    public Integer getPersonid() {
        return personid;
    }

    public void setPersonid(Integer personid) {
        this.personid = personid;
    }

    public Integer getGatewayid() {
        return gatewayid;
    }

    public void setGatewayid(Integer gatewayid) {
        this.gatewayid = gatewayid;
    }

    public String getThreeorderid() {
        return threeorderid;
    }

    public void setThreeorderid(String threeorderid) {
        this.threeorderid = threeorderid;
    }

    public String getPayno() {
        return payno;
    }

    public void setPayno(String payno) {
        this.payno = payno;
    }

    public String getToppayno() {
        return toppayno;
    }

    public void setToppayno(String toppayno) {
        this.toppayno = toppayno;
    }

    public BigDecimal getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(BigDecimal totalprice) {
        this.totalprice = totalprice;
    }

    public BigDecimal getPoundage() {
        return poundage;
    }

    public void setPoundage(BigDecimal poundage) {
        this.poundage = poundage;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}