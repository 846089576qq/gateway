package com.gateway.payment.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.zitopay.foundation.common.entity.AbstractEntity;
import com.zitopay.foundation.common.util.BeanUtils;

@Table(name="zitopay_order_refund")
public class OrderRefundEntity extends AbstractEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7885302512640599203L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	private String orderid;

	private String threeid;

	private BigDecimal amount;

	private String reason;

	private String bgreturl;

	private Integer type;

	private Date createtime;

	private String orderidinf;

	private String appId;

	private String refundno;

	private String toprefundno;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid == null ? null : orderid.trim();
	}

	public String getThreeid() {
		return threeid;
	}

	public void setThreeid(String threeid) {
		this.threeid = threeid == null ? null : threeid.trim();
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason == null ? null : reason.trim();
	}

	public String getBgreturl() {
		return bgreturl;
	}

	public void setBgreturl(String bgreturl) {
		this.bgreturl = bgreturl == null ? null : bgreturl.trim();
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getOrderidinf() {
		return orderidinf;
	}

	public void setOrderidinf(String orderidinf) {
		this.orderidinf = orderidinf == null ? null : orderidinf.trim();
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId == null ? null : appId.trim();
	}

	public String getRefundno() {
		return refundno;
	}

	public void setRefundno(String refundno) {
		this.refundno = refundno;
	}

	public String getToprefundno() {
		return toprefundno;
	}

	public void setToprefundno(String toprefundno) {
		this.toprefundno = toprefundno;
	}

	@Override
	public String toString() {
		return BeanUtils.bean2JSON(this);
	}
}