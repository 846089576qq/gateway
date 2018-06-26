/**
 * Copyright [2015-2017]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.gateway.channel.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/channel")
public class ChannelAliveController {

	private static final Logger logger = LoggerFactory.getLogger(ChannelAliveController.class);

	@Autowired
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
