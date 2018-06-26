package com.gateway.channel.entity;

import com.zitopay.foundation.common.entity.AbstractEntity;
import com.zitopay.foundation.common.util.BeanUtils;

public class ChannelAliveEntity extends AbstractEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6477971830870017208L;

	private String name = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return BeanUtils.bean2JSON(this);
	}
}
