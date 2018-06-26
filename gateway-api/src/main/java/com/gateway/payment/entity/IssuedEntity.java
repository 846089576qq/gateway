package com.gateway.payment.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 代付
 * @author 
 */
@Table(name="alwaypay_issued")
public class IssuedEntity implements Serializable {
    /**
     * 代付订单号
     */
    @Id
    private String issuedid;

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
     * 顶级交易号
     */
    private String toppayno;

    /**
     * 代付金额
     */
    private BigDecimal totalprice;

    /**
     * 第三方支付公司手续费
     */
    private BigDecimal threepoundage;

    /**
     * 收款人账户姓名
     */
    private String accountname;

    /**
     * 收款人账户卡号
     */
    private String accountno;

    /**
     * 手机号
     */
    private String contact;

    /**
     * 身份证号
     */
    private String identitycode;

    /**
     * 银行名称
     */
    private String bankname;

    /**
     * 支行名称
     */
    private String branchbankname;

    /**
     * 开户行城市
     */
    private String bankcity;

    /**
     * 开户行省份
     */
    private String bankprovince;

    /**
     * 转账状态(0 : 初始化, 1 : 代付成功, 2 : 审核中, 3 : 代付失败)
     */
    private Integer state;

    /**
     * 备注
     */
    private String remark;
    
    private Date createtime;

	private Date successtime;

    public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getSuccesstime() {
		return successtime;
	}

	public void setSuccesstime(Date successtime) {
		this.successtime = successtime;
	}

	private static final long serialVersionUID = 1L;

    public String getIssuedid() {
        return issuedid;
    }

    public void setIssuedid(String issuedid) {
        this.issuedid = issuedid;
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

    public String getOrderidinf() {
        return orderidinf;
    }

    public void setOrderidinf(String orderidinf) {
        this.orderidinf = orderidinf;
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

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getIdentitycode() {
        return identitycode;
    }

    public void setIdentitycode(String identitycode) {
        this.identitycode = identitycode;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBranchbankname() {
        return branchbankname;
    }

    public void setBranchbankname(String branchbankname) {
        this.branchbankname = branchbankname;
    }

    public String getBankcity() {
        return bankcity;
    }

    public BigDecimal getThreepoundage() {
		return threepoundage;
	}

	public void setThreepoundage(BigDecimal threepoundage) {
		this.threepoundage = threepoundage;
	}

	public void setBankcity(String bankcity) {
        this.bankcity = bankcity;
    }

    public String getBankprovince() {
        return bankprovince;
    }

    public void setBankprovince(String bankprovince) {
        this.bankprovince = bankprovince;
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