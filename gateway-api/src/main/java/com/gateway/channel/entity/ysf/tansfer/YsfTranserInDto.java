package com.gateway.channel.entity.ysf.tansfer;

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
public class YsfTranserInDto extends BaseYsfDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String merBillNo;
	private String transferType = "2";
	private String merCode;
	private String merAcctNo;
	private String customerCode;
	private String transferAmount;
	private String collectionItemName;
	private String remark;
	public String getMerCode() {
		return merCode;
	}
	public void setMerCode(String merCode) {
		this.merCode = merCode;
	}
	public String getMerBillNo() {
		return merBillNo;
	}
	public void setMerBillNo(String merBillNo) {
		this.merBillNo = merBillNo;
	}
	public String getTransferType() {
		return transferType;
	}
	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}
	public String getMerAcctNo() {
		return merAcctNo;
	}
	public void setMerAcctNo(String merAcctNo) {
		this.merAcctNo = merAcctNo;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getTransferAmount() {
		return transferAmount;
	}
	public void setTransferAmount(String transferAmount) {
		this.transferAmount = transferAmount;
	}
	public String getCollectionItemName() {
		return collectionItemName;
	}
	public void setCollectionItemName(String collectionItemName) {
		this.collectionItemName = collectionItemName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}

