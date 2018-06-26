package com.gateway.payment.entity;

import java.math.BigDecimal;

import com.zitopay.foundation.common.entity.AbstractEntity;
import com.zitopay.foundation.common.util.BeanUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商户通道实体
 */
@Table(name = "alwaypay_person_geteway")
public class PersonGatewayEntity extends AbstractEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5419669846304370542L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer personid;

	private Integer getewayid;

	private String gatewaykey;

	private String gatewaypid;

	private Integer disable;

	private Integer priority;

	private String biztype;

	private BigDecimal rate;

	private String clearform;

	private String channelid;

	private String termid;

	private String hsinfkey;

	private String gatewayname;

	private String isShow;

	private Integer isOpen;

	private Integer applicationId;

	private String pid;

	private String zkey;

	private String appid;

	private String accountNo;

	private String rsa;

	private BigDecimal channelCost;

	private String isChecked;

	private String ext1;

	private String ext2;

	private String ext3;

	private String bankName;

	private String bankNo;

	private String bankCompanyName;

	private String bankAddrProvince;

	private String bankAddrCity;

	private String bankSubName;

	private String bankSubCode;

	private BigDecimal withdrawRate;

	private BigDecimal withdrawFee;

	/** 折扣比例 */
	private BigDecimal discountRate;

	/** 分润固定收取 */
	private BigDecimal fix;

	/** 分润下限 */
	private BigDecimal min;

	/** 分润上限 */
	private BigDecimal max;

	public BigDecimal getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPersonid() {
		return personid;
	}

	public void setPersonid(Integer personid) {
		this.personid = personid;
	}

	public Integer getGetewayid() {
		return getewayid;
	}

	public void setGetewayid(Integer getewayid) {
		this.getewayid = getewayid;
	}

	public String getGatewaykey() {
		return gatewaykey;
	}

	public void setGatewaykey(String gatewaykey) {
		this.gatewaykey = gatewaykey == null ? null : gatewaykey.trim();
	}

	public String getGatewaypid() {
		return gatewaypid;
	}

	public void setGatewaypid(String gatewaypid) {
		this.gatewaypid = gatewaypid == null ? null : gatewaypid.trim();
	}

	public Integer getDisable() {
		return disable;
	}

	public void setDisable(Integer disable) {
		this.disable = disable;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getBiztype() {
		return biztype;
	}

	public void setBiztype(String biztype) {
		this.biztype = biztype == null ? null : biztype.trim();
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public String getClearform() {
		return clearform;
	}

	public void setClearform(String clearform) {
		this.clearform = clearform == null ? null : clearform.trim();
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid == null ? null : channelid.trim();
	}

	public String getTermid() {
		return termid;
	}

	public void setTermid(String termid) {
		this.termid = termid == null ? null : termid.trim();
	}

	public String getHsinfkey() {
		return hsinfkey;
	}

	public void setHsinfkey(String hsinfkey) {
		this.hsinfkey = hsinfkey == null ? null : hsinfkey.trim();
	}

	public String getGatewayname() {
		return gatewayname;
	}

	public void setGatewayname(String gatewayname) {
		this.gatewayname = gatewayname == null ? null : gatewayname.trim();
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow == null ? null : isShow.trim();
	}

	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid == null ? null : pid.trim();
	}

	public String getZkey() {
		return zkey;
	}

	public void setZkey(String zkey) {
		this.zkey = zkey == null ? null : zkey.trim();
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid == null ? null : appid.trim();
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo == null ? null : accountNo.trim();
	}

	public String getRsa() {
		return rsa;
	}

	public void setRsa(String rsa) {
		this.rsa = rsa == null ? null : rsa.trim();
	}

	public BigDecimal getChannelCost() {
		return channelCost;
	}

	public void setChannelCost(BigDecimal channelCost) {
		this.channelCost = channelCost;
	}

	public String getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked == null ? null : isChecked.trim();
	}

	/**
	 * @return the ext1
	 */
	public String getExt1() {
		return ext1;
	}

	/**
	 * @param ext1
	 *            the ext1 to set
	 */
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	/**
	 * @return the ext2
	 */
	public String getExt2() {
		return ext2;
	}

	/**
	 * @param ext2
	 *            the ext2 to set
	 */
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	/**
	 * @return the ext3
	 */
	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getBankCompanyName() {
		return bankCompanyName;
	}

	public void setBankCompanyName(String bankCompanyName) {
		this.bankCompanyName = bankCompanyName;
	}

	public String getBankAddrProvince() {
		return bankAddrProvince;
	}

	public void setBankAddrProvince(String bankAddrProvince) {
		this.bankAddrProvince = bankAddrProvince;
	}

	public String getBankAddrCity() {
		return bankAddrCity;
	}

	public void setBankAddrCity(String bankAddrCity) {
		this.bankAddrCity = bankAddrCity;
	}

	public String getBankSubName() {
		return bankSubName;
	}

	public void setBankSubName(String bankSubName) {
		this.bankSubName = bankSubName;
	}

	public String getBankSubCode() {
		return bankSubCode;
	}

	public void setBankSubCode(String bankSubCode) {
		this.bankSubCode = bankSubCode;
	}

	public BigDecimal getWithdrawRate() {
		return withdrawRate;
	}

	public void setWithdrawRate(BigDecimal withdrawRate) {
		this.withdrawRate = withdrawRate;
	}

	public BigDecimal getWithdrawFee() {
		return withdrawFee;
	}

	public void setWithdrawFee(BigDecimal withdrawFee) {
		this.withdrawFee = withdrawFee;
	}

	@Override
	public String toString() {
		return BeanUtils.bean2JSON(this);
	}

}