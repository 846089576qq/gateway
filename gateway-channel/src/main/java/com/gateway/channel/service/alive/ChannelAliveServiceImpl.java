package com.gateway.channel.service.alive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gateway.channel.entity.ChannelAliveEntity;
import com.gateway.channel.service.BaseChannelServiceImpl;
import com.zitopay.foundation.common.exception.ServiceException;

@Service
@com.alibaba.dubbo.config.annotation.Service
public class ChannelAliveServiceImpl extends BaseChannelServiceImpl implements IChannelAliveService {

	private static final Logger logger = LoggerFactory.getLogger(ChannelAliveServiceImpl.class);

	@Override
	public ChannelAliveEntity checkChannelAlive(ChannelAliveEntity entity) throws ServiceException {
		entity.setName("channel");
		logger.info("say: {}", entity);
		return entity;
	}

}
