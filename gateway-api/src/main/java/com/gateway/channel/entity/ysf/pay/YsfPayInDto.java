package com.gateway.channel.entity.ysf.pay;

import com.gateway.channel.entity.ysf.BaseYsfDto;

/**
 *
 * 类   描  述：人民币卡收单请求表单实体类
 *          对应网银收单请求的form表单
 * 作          者：fengxueyong
 * 创建日期：2013-5-15
 * 版          本：1.0
 * 修改历史：
 */
public class YsfPayInDto extends BaseYsfDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String version = "v1.0.0";  //
	private String merCode;
	private String merName;
	private String merBillNo;
	private String amount;
	private String date;
	private String currencyType = "156";
	private String gatewayType;
	private String lang = "gb";
	private String merchantUrl;
	private String failUrl;
	private String attach;
	private String orderEncodeType = "5";
	private String retEncodeType = "17";
	private String rettype = "1";
	private String serverUrl;
	private String signMD5;
	private String doCredit;
	private String bankcode;
	private String merAcccode;
	private String goodsName;
	private String payDeadlineDateUnit;
	private String payDeadlineDate;
	private String merBillIp;
	private String billExp = "96";
	private String productType;
	private String isCredit;
	private String sysCode;
	private String whoFee;
	private String userId;
	private String feeType;
	private String bizType;
	private String userRealName;
	public String getBillExp() {
		return billExp;
	}
	public void setBillExp(String billExp) {
		this.billExp = billExp;
	}
	public String getMerName() {
		return merName;
	}
	public void setMerName(String merName) {
		this.merName = merName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getMerCode() {
		return merCode;
	}
	public void setMerCode(String merCode) {
		this.merCode = merCode;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public String getGatewayType() {
		return gatewayType;
	}
	public void setGatewayType(String gatewayType) {
		this.gatewayType = gatewayType;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getMerchantUrl() {
		return merchantUrl;
	}
	public void setMerchantUrl(String merchantUrl) {
		this.merchantUrl = merchantUrl;
	}
	public String getFailUrl() {
		return failUrl;
	}
	public void setFailUrl(String failUrl) {
		this.failUrl = failUrl;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getOrderEncodeType() {
		return orderEncodeType;
	}
	public void setOrderEncodeType(String orderEncodeType) {
		this.orderEncodeType = orderEncodeType;
	}
	public String getRetEncodeType() {
		return retEncodeType;
	}
	public void setRetEncodeType(String retEncodeType) {
		this.retEncodeType = retEncodeType;
	}
	public String getRettype() {
		return rettype;
	}
	public void setRettype(String rettype) {
		this.rettype = rettype;
	}
	public String getServerUrl() {
		return serverUrl;
	}
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	public String getSignMD5() {
		return signMD5;
	}
	public void setSignMD5(String signMD5) {
		this.signMD5 = signMD5;
	}
	public String getDoCredit() {
		return doCredit;
	}
	public void setDoCredit(String doCredit) {
		this.doCredit = doCredit;
	}
	public String getMerAcccode() {
		return merAcccode;
	}
	public void setMerAcccode(String merAcccode) {
		this.merAcccode = merAcccode;
	}
	public String getPayDeadlineDateUnit() {
		return payDeadlineDateUnit;
	}
	public void setPayDeadlineDateUnit(String payDeadlineDateUnit) {
		this.payDeadlineDateUnit = payDeadlineDateUnit;
	}
	public String getPayDeadlineDate() {
		return payDeadlineDate;
	}
	public void setPayDeadlineDate(String payDeadlineDate) {
		this.payDeadlineDate = payDeadlineDate;
	}
	public String getMerBillIp() {
		return merBillIp;
	}
	public void setMerBillIp(String merBillIp) {
		this.merBillIp = merBillIp;
	}

	public String getMerBillNo() {
		return merBillNo;
	}

	public void setMerBillNo(String merBillNo) {
		this.merBillNo = merBillNo;
	}

	public String getBankcode() {
		return bankcode;
	}

	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getIsCredit() {
		return isCredit;
	}

	public void setIsCredit(String isCredit) {
		this.isCredit = isCredit;
	}

	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public String getWhoFee() {
		return whoFee;
	}

	public void setWhoFee(String whoFee) {
		this.whoFee = whoFee;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getUserRealName() {
		return userRealName;
	}
	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}
}

