package com.gateway.payment.entity;

import com.zitopay.foundation.common.util.BeanUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 分润实体
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月16日
 */
@Table(name = "alwaypay_employee_profit")
public class EmployeeProfitEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 主键id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/** 订单号 */
	private String orderId;

	/** 代理人id */
	private String employeeId;

	/** 总收益 */
	private BigDecimal totalRevenue;

	/** 净收益 */
	private BigDecimal netProceeds;

	/** 分润比例：通道费率 */
	private BigDecimal profitRate;

	/** 折扣比例 */
	private BigDecimal discountRate;

	/** 创建时间 */
	private String createTime;
	
	private BigDecimal fix;

	public BigDecimal getFix() {
		return fix;
	}

	public void setFix(BigDecimal fix) {
		this.fix = fix;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public BigDecimal getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(BigDecimal totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public BigDecimal getNetProceeds() {
		return netProceeds;
	}

	public void setNetProceeds(BigDecimal netProceeds) {
		this.netProceeds = netProceeds;
	}

	public BigDecimal getProfitRate() {
		return profitRate;
	}

	public void setProfitRate(BigDecimal profitRate) {
		this.profitRate = profitRate;
	}

	public BigDecimal getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
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
