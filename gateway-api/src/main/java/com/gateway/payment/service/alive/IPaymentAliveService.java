package com.gateway.payment.service.alive;

import com.gateway.payment.entity.PaymentAliveEntity;
import com.zitopay.foundation.common.exception.ServiceException;

public interface IPaymentAliveService {

	public PaymentAliveEntity checkAlivePayment(PaymentAliveEntity entity) throws ServiceException;

}
