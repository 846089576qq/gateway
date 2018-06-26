package com.gateway.payment.service.alive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.gateway.payment.entity.PaymentAliveEntity;
import com.gateway.payment.service.alive.IPaymentAliveService;
import com.zitopay.foundation.common.exception.ServiceException;

@Service
@org.springframework.stereotype.Service
public class PaymentAliveServiceImpl implements IPaymentAliveService {

	private static final Logger logger = LoggerFactory.getLogger(PaymentAliveServiceImpl.class);

	@Override
	public PaymentAliveEntity checkAlivePayment(PaymentAliveEntity entity) throws ServiceException {
		entity.setName("payment");
		logger.info("entity: {}", entity);
		return entity;
	}

}
