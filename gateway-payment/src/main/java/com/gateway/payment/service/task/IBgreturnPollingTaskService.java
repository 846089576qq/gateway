package com.gateway.payment.service.task;

import com.zitopay.foundation.common.exception.ServiceException;

public interface IBgreturnPollingTaskService {

	/**
	 * 在商户未给正确回调时重复对商户进行通知<br/>
	 * 每2分钟执行一次，处理所有类型为'支付'同时未给SUCCESS反馈且执行次数低于10次的信息
	 * 
	 * @throws ServiceException
	 */
	public void paymentNotifyScheduler() throws ServiceException;
}
