/**
 * Copyright [2015-2017]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.gateway.payment.entity;

import java.math.BigDecimal;

/**
 * 商户通道和代理信息
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月16日
 */
public class PersonChannelEntity {

	/** 商户id */
	private Integer personId;

	/** 通道id */
	private Integer gatewayId;

	/** 通道名称 */
	private String channelName;

	/** 费率 */
	private BigDecimal rate;

	/** 运营成本 */
	private BigDecimal channelCost;

	/** 代理名称 */
	private String employeeName;

	/** 代理人id */
	private String employeeId;

	/** 结算模式 */
	private String clearform;

	public String getClearform() {
		return clearform;
	}

	public void setClearform(String clearform) {
		this.clearform = clearform;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public Integer getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(Integer gatewayId) {
		this.gatewayId = gatewayId;
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

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

}
