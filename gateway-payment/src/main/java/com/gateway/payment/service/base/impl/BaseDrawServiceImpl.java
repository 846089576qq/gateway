package com.gateway.payment.service.base.impl;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.service.base.IBaseDrawService;
import com.zitopay.foundation.common.exception.ServiceException;

public abstract class BaseDrawServiceImpl extends BaseServiceImpl implements IBaseDrawService {

	@Override
	public Map<String, Object> checkParam(Map<String, String> paramMap) throws ServiceException {
		return ResponseInfoEnum.调用成功.getMap();
	}
		
	
	@Transactional
	public Map<String, Object> drawSuccess(String drawAmount, String drawFee, String settleDate, String orderNo, String state, String note) {
		accountSystemCashService.updateOrder(drawAmount, drawFee, settleDate, orderNo, state, note);// 更新提现流水信息
		return ResponseInfoEnum.调用成功.getMap();
	}
	
}
