package com.gateway.payment.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gateway.channel.entity.ChannelAliveEntity;
import com.gateway.channel.service.alive.IChannelAliveService;
import com.gateway.payment.entity.PaymentAliveEntity;
import com.gateway.payment.service.alive.IPaymentAliveService;
import com.zitopay.foundation.common.exception.ServiceException;

/**
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年4月19日
 */
@CrossOrigin
@RestController
@RequestMapping("/payment")
public class PaymentAliveController {

	private static final Logger logger = LoggerFactory.getLogger(PaymentAliveController.class);

	@Reference
	private IChannelAliveService channelAliveService;

	@Reference
	private IPaymentAliveService paymentAliveService;

	@RequestMapping("/alive")
	public List channel(final HttpServletRequest request) throws ServiceException {
		List list = new ArrayList();
		try {
			list.add(channelAliveService.checkChannelAlive(new ChannelAliveEntity()));
			list.add(paymentAliveService.checkAlivePayment(new PaymentAliveEntity()));
			list.add(new HashMap() {

				{
					put("clientIP", request.getRemoteAddr());
					put("serverIP", request.getLocalAddr());
				}
			});
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return list;
	}
}
