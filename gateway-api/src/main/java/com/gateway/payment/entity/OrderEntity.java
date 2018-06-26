package com.gateway.payment.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.zitopay.foundation.common.entity.AbstractEntity;
import com.zitopay.foundation.common.util.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="alwaypay_order")
public class OrderEntity extends AbstractEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7309033861559034919L;
	@Id
	private String orderid;

	private Integer persionid;

	private String threeorderid;

	private String payno;
	
	private String toppayno;

	public String getToppayno() {
		return toppayno;
	}

	public void setToppayno(String toppayno) {
		this.toppayno = toppayno;
	}

	private Integer getewayid;

	private String ordertitle;

	private String goodsname;

	private String goodsdetail;

	/**
	 * 同步回调地址
	 */
	private String returl;

	/**
	 * 异步 后台回调url
	 */
	private String bgreturl;

	/**
	 * 返回商户url
	 */
	private String returnurl;

	private String accountname;

	private String posttime;

	private BigDecimal totalprice;

	private BigDecimal amount;

	private BigDecimal refundmoney;

	private BigDecimal ratemmoney;

	private BigDecimal rate;

	private Integer state;

	private String createdate;

	private Date createtime;

	private Date paysucctime;

	private String zongduanno;

	private String sign;

	private String orderidinf;

	private Integer applicationId;

	private String appId;
	
	private String min;

	private String fix;

	private String max;

	@Column(name="sessionToken")
	private String sessionToken;

	/**
	 * 协议表主键
	 */
	private String agreementId;
	
	private String paycardShopNo;
	
	private String paycardOperatorNo;

	private String settlementType;

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getFix() {
		return fix;
	}

	public void setFix(String fix) {
		this.fix = fix;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getPaycardShopNo() {
		return paycardShopNo;
	}

	public void setPaycardShopNo(String paycardShopNo) {
		this.paycardShopNo = paycardShopNo;
	}

	public String getPaycardOperatorNo() {
		return paycardOperatorNo;
	}

	public void setPaycardOperatorNo(String paycardOperatorNo) {
		this.paycardOperatorNo = paycardOperatorNo;
	}

	public String getSettlementType() {
		return settlementType;
	}

	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}

	public String getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid == null ? null : orderid.trim();
	}

	public Integer getPersionid() {
		return persionid;
	}

	public void setPersionid(Integer persionid) {
		this.persionid = persionid;
	}

	public String getThreeorderid() {
		return threeorderid;
	}

	public void setThreeorderid(String threeorderid) {
		this.threeorderid = threeorderid == null ? null : threeorderid.trim();
	}

	public String getPayno() {
		return payno;
	}

	public void setPayno(String payno) {
		this.payno = payno == null ? null : payno.trim();
	}

	public Integer getGetewayid() {
		return getewayid;
	}

	public void setGetewayid(Integer getewayid) {
		this.getewayid = getewayid;
	}

	public String getOrdertitle() {
		return ordertitle;
	}

	public void setOrdertitle(String ordertitle) {
		this.ordertitle = ordertitle == null ? null : ordertitle.trim();
	}

	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname == null ? null : goodsname.trim();
	}

	public String getGoodsdetail() {
		return goodsdetail;
	}

	public void setGoodsdetail(String goodsdetail) {
		this.goodsdetail = goodsdetail == null ? null : goodsdetail.trim();
	}

	public String getReturl() {
		return returl;
	}

	public void setReturl(String returl) {
		this.returl = returl == null ? null : returl.trim();
	}

	public String getBgreturl() {
		return bgreturl;
	}

	public void setBgreturl(String bgreturl) {
		this.bgreturl = bgreturl == null ? null : bgreturl.trim();
	}

	public String getReturnurl() {
		return returnurl;
	}

	public void setReturnurl(String returnurl) {
		this.returnurl = returnurl == null ? null : returnurl.trim();
	}

	public String getAccountname() {
		return accountname;
	}

	public void setAccountname(String accountname) {
		this.accountname = accountname == null ? null : accountname.trim();
	}

	public String getPosttime() {
		return posttime;
	}

	public void setPosttime(String posttime) {
		this.posttime = posttime == null ? null : posttime.trim();
	}

	public BigDecimal getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(BigDecimal totalprice) {
		this.totalprice = totalprice;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getRefundmoney() {
		return refundmoney;
	}

	public void setRefundmoney(BigDecimal refundmoney) {
		this.refundmoney = refundmoney;
	}

	public BigDecimal getRatemmoney() {
		return ratemmoney;
	}

	public void setRatemmoney(BigDecimal ratemmoney) {
		this.ratemmoney = ratemmoney;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate == null ? null : createdate.trim();
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getPaysucctime() {
		return paysucctime;
	}

	public void setPaysucctime(Date paysucctime) {
		this.paysucctime = paysucctime;
	}

	public String getZongduanno() {
		return zongduanno;
	}

	public void setZongduanno(String zongduanno) {
		this.zongduanno = zongduanno == null ? null : zongduanno.trim();
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign == null ? null : sign.trim();
	}

	public String getOrderidinf() {
		return orderidinf;
	}

	public void setOrderidinf(String orderidinf) {
		this.orderidinf = orderidinf == null ? null : orderidinf.trim();
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId == null ? null : appId.trim();
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	@Override
	public String toString() {
		return BeanUtils.bean2JSON(this);
	}
}