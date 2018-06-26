package com.gateway.payment.persistence.service;

import com.gateway.payment.entity.PaymentAgreementEntity;

/**
 * 协议接口
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
public interface IPaymentAgreementService extends IBaseGenericService<PaymentAgreementEntity> {

	/**
	 * 根据客户协议号获取协议
	 * 
	 * @param custAgreementNo
	 * @return
	 */
	public PaymentAgreementEntity getPaymentAgreementByCustAgreementNo(String custAgreementNo);

	/**
	 * 根据协议表Id获取协议
	 * @param agreementId
	 * @return
	 */
	public PaymentAgreementEntity getPaymentAgreementByAgreementId(String agreementId);
	
	/**
	 * 根据用户自定义Id获取协议
	 * 
	 * @param merchantId
	 * @param merCustId
	 * @return
	 */
	public PaymentAgreementEntity getPaymentAgreementByMerCustId(String merchantId, String merCustId);

	/**
	 * 根据商户号获取协议
	 * 
	 * @param merchantId
	 * @param custCardType
	 * @param custCardNo
	 * @param custIdentityCode
	 * @param custName
	 * @param creditValidDate
	 * @param custCreditCvv2
	 * @return
	 */
	public PaymentAgreementEntity getPaymentAgreement(String merchantId, String custCardType, String custCardNo, String custIdentityCode, String custName, String creditValidDate, String custCreditCvv2,Integer status);

	int insertRecord(PaymentAgreementEntity record);

	/**
	 * 构建支付协议
	 * @param merchantId
	 * @param merchantCustomerId
	 * @param customerCardType
	 * @param bankCardNo
	 * @param customerIdentityCode
	 * @param customerContact
	 * @param customerRealName
	 * @param bankCardValidDate
	 * @param bankCardCVV2
	 * @param bankCode
	 * @return
	 */
	public PaymentAgreementEntity createPaymentAgreement(String merchantId, String merchantCustomerId, String customerCardType, String bankCardNo, String customerIdentityCode, String customerContact, String customerRealName, String bankCardValidDate, String bankCardCVV2, String bankCode);

}