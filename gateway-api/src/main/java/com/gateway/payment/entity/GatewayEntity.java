package com.gateway.payment.entity;

import java.math.BigDecimal;

import com.zitopay.foundation.common.entity.AbstractEntity;
import com.zitopay.foundation.common.util.BeanUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "alwaypay_geteway")
public class GatewayEntity extends AbstractEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6567248313266403091L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer gid;// 通道表主键

	private Integer id;// 通道业务主键

	private String cid;

	private String name;

	private String subChannelCode;

	private Integer nature;

	private BigDecimal rate;

	private String cashiername;

	private String payproduct;

	private String appliscenarios;

	private String accessParty;

	private String logoUrl;

	private String logoUrlApp;

	private Integer disable;

	private String clearform;

	private String priority;

	private String remarks;

	private Integer connectedWay;

	// private String settlementMode;

	private Integer state;

	private String instructions;

	private String ext;

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid == null ? null : cid.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getSubChannelCode() {
		return subChannelCode;
	}

	public void setSubChannelCode(String subChannelCode) {
		this.subChannelCode = subChannelCode == null ? null : subChannelCode.trim();
	}

	public Integer getNature() {
		return nature;
	}

	public void setNature(Integer nature) {
		this.nature = nature;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public String getCashiername() {
		return cashiername;
	}

	public void setCashiername(String cashiername) {
		this.cashiername = cashiername == null ? null : cashiername.trim();
	}

	public String getPayproduct() {
		return payproduct;
	}

	public void setPayproduct(String payproduct) {
		this.payproduct = payproduct == null ? null : payproduct.trim();
	}

	public String getAppliscenarios() {
		return appliscenarios;
	}

	public void setAppliscenarios(String appliscenarios) {
		this.appliscenarios = appliscenarios == null ? null : appliscenarios.trim();
	}

	public String getAccessParty() {
		return accessParty;
	}

	public void setAccessParty(String accessParty) {
		this.accessParty = accessParty == null ? null : accessParty.trim();
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl == null ? null : logoUrl.trim();
	}

	public String getLogoUrlApp() {
		return logoUrlApp;
	}

	public void setLogoUrlApp(String logoUrlApp) {
		this.logoUrlApp = logoUrlApp == null ? null : logoUrlApp.trim();
	}

	public Integer getDisable() {
		return disable;
	}

	public void setDisable(Integer disable) {
		this.disable = disable;
	}

	public String getClearform() {
		return clearform;
	}

	public void setClearform(String clearform) {
		this.clearform = clearform == null ? null : clearform.trim();
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority == null ? null : priority.trim();
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks == null ? null : remarks.trim();
	}

	public Integer getConnectedWay() {
		return connectedWay;
	}

	public void setConnectedWay(Integer connectedWay) {
		this.connectedWay = connectedWay;
	}

	// public String getSettlementMode() {
	// return settlementMode;
	// }
	//
	// public void setSettlementMode(String settlementMode) {
	// this.settlementMode = settlementMode == null ? null : settlementMode.trim();
	// }

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	public String getExt() {
		return ext;
	}

	
	public void setExt(String ext) {
		this.ext = ext;
	}

	@Override
	public String toString() {
		return BeanUtils.bean2JSON(this);
	}
}