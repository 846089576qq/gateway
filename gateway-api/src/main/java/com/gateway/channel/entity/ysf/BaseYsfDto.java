package com.gateway.channel.entity.ysf;

import java.io.Serializable;

import com.zitopay.foundation.common.util.BeanUtils;

public class BaseYsfDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString() {
		return BeanUtils.bean2JSON(this);
	}

}
