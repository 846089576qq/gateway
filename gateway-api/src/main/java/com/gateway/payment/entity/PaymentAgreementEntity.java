package com.gateway.payment.entity;

import java.util.Date;

import com.zitopay.foundation.common.entity.AbstractEntity;
import com.zitopay.foundation.common.util.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="zitopay_payment_agreement")
public class PaymentAgreementEntity extends AbstractEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5290156524679807012L;
    @Id
	private String agreementId;

	private String merchantId;

	private String merCustId;

	private String custAgreementNo;

	private String custBizNo;

	private String custCardType;

	private String custCardNo;

	private String custBankCode;

	private String custContact;

	private String custIdentityCode;

	private String custName;

	private String creditValidDate;

	private String custCreditCvv2;
    @Column(name="STATUS")
	private Integer status;

	private Date createTime;

	public String getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId == null ? null : agreementId.trim();
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId == null ? null : merchantId.trim();
	}

	public String getMerCustId() {
		return merCustId;
	}

	public void setMerCustId(String merCustId) {
		this.merCustId = merCustId == null ? null : merCustId.trim();
	}

	public String getCustAgreementNo() {
		return custAgreementNo;
	}

	public void setCustAgreementNo(String custAgreementNo) {
		this.custAgreementNo = custAgreementNo == null ? null : custAgreementNo.trim();
	}

	public String getCustBizNo() {
		return custBizNo;
	}

	public void setCustBizNo(String custBizNo) {
		this.custBizNo = custBizNo == null ? null : custBizNo.trim();
	}

	public String getCustCardType() {
		return custCardType;
	}

	public void setCustCardType(String custCardType) {
		this.custCardType = custCardType == null ? null : custCardType.trim();
	}

	public String getCustCardNo() {
		return custCardNo;
	}

	public void setCustCardNo(String custCardNo) {
		this.custCardNo = custCardNo == null ? null : custCardNo.trim();
	}

	public String getCustBankCode() {
		return custBankCode;
	}

	public void setCustBankCode(String custBankCode) {
		this.custBankCode = custBankCode == null ? null : custBankCode.trim();
	}

	public String getCustContact() {
		return custContact;
	}

	public void setCustContact(String custContact) {
		this.custContact = custContact == null ? null : custContact.trim();
	}

	public String getCustIdentityCode() {
		return custIdentityCode;
	}

	public void setCustIdentityCode(String custIdentityCode) {
		this.custIdentityCode = custIdentityCode == null ? null : custIdentityCode.trim();
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName == null ? null : custName.trim();
	}

	public String getCreditValidDate() {
		return creditValidDate;
	}

	public void setCreditValidDate(String creditValidDate) {
		this.creditValidDate = creditValidDate == null ? null : creditValidDate.trim();
	}

	public String getCustCreditCvv2() {
		return custCreditCvv2;
	}

	public void setCustCreditCvv2(String custCreditCvv2) {
		this.custCreditCvv2 = custCreditCvv2 == null ? null : custCreditCvv2.trim();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return BeanUtils.bean2JSON(this);
	}
}