package com.gateway.payment.entity;

import com.zitopay.foundation.common.entity.AbstractEntity;
import com.zitopay.foundation.common.util.BeanUtils;

import javax.persistence.Id;
import javax.persistence.Table;
@Table(name="alwaypay_trade_monitor")
public class TradeMonitorEntity extends AbstractEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1050082048713931538L;
	
    @Id
	private String zOrderid;

	private Integer zOrderState;

	private String sOrderState;

	private Integer sIsNotify;

	private String sNotifyTime;

	private String sNotifyValue;

	private Integer cIsNotify;

	private String cNotifyTime;

	private String cNotifyValue;

	private Integer sIsCheck;

	public String getzOrderid() {
		return zOrderid;
	}

	public void setzOrderid(String zOrderid) {
		this.zOrderid = zOrderid == null ? null : zOrderid.trim();
	}

	public Integer getzOrderState() {
		return zOrderState;
	}

	public void setzOrderState(Integer zOrderState) {
		this.zOrderState = zOrderState;
	}

	public String getsOrderState() {
		return sOrderState;
	}

	public void setsOrderState(String sOrderState) {
		this.sOrderState = sOrderState == null ? null : sOrderState.trim();
	}

	public Integer getsIsNotify() {
		return sIsNotify;
	}

	public void setsIsNotify(Integer sIsNotify) {
		this.sIsNotify = sIsNotify;
	}

	public String getsNotifyTime() {
		return sNotifyTime;
	}

	public void setsNotifyTime(String sNotifyTime) {
		this.sNotifyTime = sNotifyTime == null ? null : sNotifyTime.trim();
	}

	public String getsNotifyValue() {
		return sNotifyValue;
	}

	public void setsNotifyValue(String sNotifyValue) {
		this.sNotifyValue = sNotifyValue == null ? null : sNotifyValue.trim();
	}

	public Integer getcIsNotify() {
		return cIsNotify;
	}

	public void setcIsNotify(Integer cIsNotify) {
		this.cIsNotify = cIsNotify;
	}

	public String getcNotifyTime() {
		return cNotifyTime;
	}

	public void setcNotifyTime(String cNotifyTime) {
		this.cNotifyTime = cNotifyTime == null ? null : cNotifyTime.trim();
	}

	public String getcNotifyValue() {
		return cNotifyValue;
	}

	public void setcNotifyValue(String cNotifyValue) {
		this.cNotifyValue = cNotifyValue == null ? null : cNotifyValue.trim();
	}

	public Integer getsIsCheck() {
		return sIsCheck;
	}

	public void setsIsCheck(Integer sIsCheck) {
		this.sIsCheck = sIsCheck;
	}

	@Override
	public String toString() {
		return BeanUtils.bean2JSON(this);
	}
}