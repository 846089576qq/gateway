package com.gateway.payment.service.base.impl;

import java.util.Map;

import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.service.base.IBaseTransferService;
import com.zitopay.foundation.common.exception.ServiceException;

public abstract class BaseTransferServiceImpl extends BaseServiceImpl implements IBaseTransferService {

	@Override
	public Map<String, Object> checkParam(Map<String, String> paramMap) throws ServiceException {
		return ResponseInfoEnum.调用成功.getMap();
	}
		
}
