package com.gateway.payment.entity;

import com.zitopay.foundation.common.util.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 项目名称:com.zitopay.order.dto
 * 类名称:EmployeeGetewayDto
 * 描述:
 * 创建人:王洪滨
 * 创建时间:2016-10-25 14:22
 */
@Table(name = "zitopay_employee_geteway")
public class EmployeeGatewayEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**代理id*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer employeeId;
	/**代理名称*/
	@Transient
	private String employeeName;
	/**通道id*/
	private Integer getewayid;
	/**0是显示  1是隐藏*/
	private Integer disable;
	/**通道名称*/
	@Transient
	private String channelName;
	/**费率*/
	private BigDecimal rate;
	/**运营成本*/
	private BigDecimal channelCost;
	/**折扣比例*/
	private BigDecimal discountRate;
	/**提现手续费百分比部分*/
	private BigDecimal withdraw_rate;
	/**提现手续费固定部分*/
	private BigDecimal withdraw_fee;
	/**结算方式*/
	private String clearform;
	/**分润固定收取*/
	private BigDecimal fix;
	
	private String employeeConfig;

	public String getEmployeeConfig() {
		return employeeConfig;
	}

	public void setEmployeeConfig(String employeeConfig) {
		this.employeeConfig = employeeConfig;
	}

	public BigDecimal getWithdraw_rate() {
		return withdraw_rate;
	}

	public void setWithdraw_rate(BigDecimal withdraw_rate) {
		this.withdraw_rate = withdraw_rate;
	}

	public BigDecimal getWithdraw_fee() {
		return withdraw_fee;
	}

	public void setWithdraw_fee(BigDecimal withdraw_fee) {
		this.withdraw_fee = withdraw_fee;
	}

	public String getClearform() {
		return clearform;
	}

	public void setClearform(String clearform) {
		this.clearform = clearform;
	}

	/**分润下限*/
	private BigDecimal min;
	/**分润上限*/
	private BigDecimal max;

	public BigDecimal getFix() {
		return fix;
	}

	public void setFix(BigDecimal fix) {
		this.fix = fix;
	}

	public BigDecimal getMin() {
		return min;
	}

	public void setMin(BigDecimal min) {
		this.min = min;
	}

	public BigDecimal getMax() {
		return max;
	}

	public void setMax(BigDecimal max) {
		this.max = max;
	}

	public Integer getDisable() {
		return disable;
	}

	public void setDisable(Integer disable) {
		this.disable = disable;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Integer getGetewayid() {
		return getewayid;
	}

	public void setGetewayid(Integer getewayid) {
		this.getewayid = getewayid;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getChannelCost() {
		return channelCost;
	}

	public void setChannelCost(BigDecimal channelCost) {
		this.channelCost = channelCost;
	}

	public BigDecimal getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}

	@Override
	public String toString() {
		return BeanUtils.bean2JSON(this);
	}
}
