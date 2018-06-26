package com.gateway.payment.service.biz.ysf;

import java.util.Map;

import com.zitopay.foundation.common.exception.ServiceException;

public interface IYsfWGCallbackPamentService {

	public Map<String, Object> callbackCheck(Map<String, String> paramMap, String serialNo) throws ServiceException;

}
