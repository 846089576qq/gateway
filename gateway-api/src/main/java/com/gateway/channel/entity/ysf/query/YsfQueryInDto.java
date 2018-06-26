package com.gateway.channel.entity.ysf.query;

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
public class YsfQueryInDto extends BaseYsfDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String version = "v1.0.0";  //
	private String accCode;
	private String merCode;
	private String merName;
	private String merBillNo;
	private String amount;
	private String date;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getAccCode() {
		return accCode;
	}
	public void setAccCode(String accCode) {
		this.accCode = accCode;
	}
	public String getMerCode() {
		return merCode;
	}
	public void setMerCode(String merCode) {
		this.merCode = merCode;
	}
	public String getMerName() {
		return merName;
	}
	public void setMerName(String merName) {
		this.merName = merName;
	}
	public String getMerBillNo() {
		return merBillNo;
	}
	public void setMerBillNo(String merBillNo) {
		this.merBillNo = merBillNo;
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
}

