package com.gateway.payment.persistence.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gateway.payment.entity.PaymentAgreementEntity;
import com.gateway.payment.persistence.mapper.IPaymentAgreementMapper;
import com.gateway.payment.persistence.service.IPaymentAgreementService;
import com.zitopay.foundation.common.util.PKUtils;

/**
 * 协议的实现类
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Service
public class PaymentAgreementServiceImpl extends BaseGenericServiceImpl<PaymentAgreementEntity> implements IPaymentAgreementService {

	private static final Logger logger = LoggerFactory.getLogger(PaymentAgreementServiceImpl.class);

	@Autowired
	private IPaymentAgreementMapper iPaymentAgreementMapper;

	@Override
	public PaymentAgreementEntity getPaymentAgreementByCustAgreementNo(String custAgreementNo) {
		PaymentAgreementEntity entity = new PaymentAgreementEntity();
		entity.setCustAgreementNo(custAgreementNo);
		return queryOne(entity);
	}

	@Override
	public PaymentAgreementEntity getPaymentAgreementByAgreementId(String agreementId) {
		PaymentAgreementEntity entity = new PaymentAgreementEntity();
		entity.setAgreementId(agreementId);
		return queryOne(entity);
	}

	@Override
	public PaymentAgreementEntity getPaymentAgreement(String merchantId, String custCardType, String custCardNo, String custIdentityCode, String custName, String creditValidDate, String custCreditCvv2, Integer status) {
		PaymentAgreementEntity zitopayPaymentAgreement = new PaymentAgreementEntity();
		zitopayPaymentAgreement.setMerchantId(merchantId);
		zitopayPaymentAgreement.setCustCardNo(custCardNo);
		zitopayPaymentAgreement.setCustIdentityCode(custIdentityCode);
		zitopayPaymentAgreement.setCustName(custName);
		zitopayPaymentAgreement.setCustCardType(custCardType);
		if ("1".equals(custCardType)) {
			// 信用卡添加匹配条件：有效期、cvv2
			zitopayPaymentAgreement.setCreditValidDate(creditValidDate);
			zitopayPaymentAgreement.setCustCreditCvv2(custCreditCvv2);
		}
		return queryOne(zitopayPaymentAgreement);
	}

	@Override
	public PaymentAgreementEntity createPaymentAgreement(String merchantId, String merchantCustomerId, String customerCardType, String bankCardNo, String customerIdentityCode, String customerContact, String customerRealName, String bankCardValidDate, String bankCardCVV2, String bankCode) {
		PaymentAgreementEntity zitopayPaymentAgreement = getPaymentAgreement(merchantId, customerCardType, bankCardNo, customerIdentityCode, customerRealName, bankCardValidDate, bankCardCVV2, 0);
		if (zitopayPaymentAgreement == null) {
			zitopayPaymentAgreement = new PaymentAgreementEntity();
			zitopayPaymentAgreement.setAgreementId(String.valueOf(PKUtils.getInstance().longPK()));
			zitopayPaymentAgreement.setMerchantId(merchantId);
			zitopayPaymentAgreement.setMerCustId(merchantCustomerId);
			zitopayPaymentAgreement.setCustCardType(customerCardType);
			zitopayPaymentAgreement.setCustCardNo(bankCardNo);
			zitopayPaymentAgreement.setCustBankCode(bankCode);
			zitopayPaymentAgreement.setCustContact(customerContact);
			zitopayPaymentAgreement.setCustIdentityCode(customerIdentityCode);
			zitopayPaymentAgreement.setCustName(customerRealName);
			zitopayPaymentAgreement.setCreditValidDate(bankCardValidDate);
			zitopayPaymentAgreement.setCustCreditCvv2(bankCardCVV2);
			zitopayPaymentAgreement.setCreateTime(new Date());
			zitopayPaymentAgreement.setStatus(1);
			int i = insertRecord(zitopayPaymentAgreement);
			logger.info("用户支付协议创建成功，协议信息:{}", zitopayPaymentAgreement);
		}
		return zitopayPaymentAgreement;
	}

	@Override
	public int insertRecord(PaymentAgreementEntity record) {
		return saveSelective(record);
	}

	@Override
	public PaymentAgreementEntity getPaymentAgreementByMerCustId(String merchantId, String merCustId) {
		PaymentAgreementEntity entity = new PaymentAgreementEntity();
		entity.setMerchantId(merchantId);
		entity.setMerCustId(merCustId);
		return queryOne(entity);
	}
}