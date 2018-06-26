package com.gateway.channel.service.alive;

import com.gateway.channel.entity.ChannelAliveEntity;
import com.zitopay.foundation.common.exception.ServiceException;

public interface IChannelAliveService {

	public ChannelAliveEntity checkChannelAlive(ChannelAliveEntity entity) throws ServiceException;

}
