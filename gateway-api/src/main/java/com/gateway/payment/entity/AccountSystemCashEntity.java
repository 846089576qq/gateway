package com.gateway.payment.entity;

import java.io.Serializable;

import javax.persistence.Table;

import com.zitopay.foundation.common.util.BeanUtils;

/**
 * 提现实体
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Table(name = "zitopay_account_system_cash")
public class AccountSystemCashEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8016219630961012692L;

	private String name;

	private String tradeTime;// 交易日期

	private String orderNo;// '订单号',

	private String orderType;// '类型',

	private double amount;// '提现金额',

	private double poundage;// '手续费',

	private String state;// '状态',

	private double arriveAccount;// '到账金额'

	private String note;// '备注',

	private Integer id;// '主键ID',

	private String personId;// '商户ID',

	private String getewayId;// '通道ID',

	private String createTime;// '创建日期',

	private String sTrackingNo;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getsTrackingNo() {
		return sTrackingNo;
	}

	public void setsTrackingNo(String sTrackingNo) {
		this.sTrackingNo = sTrackingNo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public double getPoundage() {
		return poundage;
	}

	public void setPoundage(double poundage) {
		this.poundage = poundage;
	}

	public double getArriveAccount() {
		return arriveAccount;
	}

	public void setArriveAccount(double arriveAccount) {
		this.arriveAccount = arriveAccount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getGetewayId() {
		return getewayId;
	}

	public void setGetewayId(String getewayId) {
		this.getewayId = getewayId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return BeanUtils.bean2JSON(this);
	}
}
