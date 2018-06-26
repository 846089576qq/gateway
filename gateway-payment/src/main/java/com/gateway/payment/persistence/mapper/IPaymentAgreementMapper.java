package com.gateway.payment.persistence.mapper;

import org.springframework.stereotype.Repository;

import com.gateway.payment.entity.PaymentAgreementEntity;
import com.github.abel533.mapper.Mapper;

/**
 * 协议mapper
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Repository
public interface IPaymentAgreementMapper extends Mapper<PaymentAgreementEntity> {
}