package com.gateway.payment.service.task;

import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 订单轮询定时器接口
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月15日
 */
public interface IOrderPollingTaskService {

	/**
	 * 处理未支付订单<br/>
	 * 每15分钟执行一次，处理当前时间前30分钟到前15分钟内状态为'未支付'的订单<br/>
	 * 处理逻辑：针对每笔订单到支付公司进行查询，将查询到的结果更新订单状态和分润处理完毕后对商户进行异步通知。
	 * 
	 * @throws ServiceException
	 */
	void unpayingOrderScheduler() throws ServiceException;

	/**
	 * 处理超时订单<br/>
	 * 每30分钟执行一次，处理当前时间前2小时到30分钟区间内状态为'支付失效'的订单<br/>
	 * 处理逻辑：针对每笔订单到支付公司进行查询，将查询到的结果更新订单状态和分润处理完毕后对商户进行异步通知。
	 * 
	 * @throws ServiceException
	 */
	void expiredOrderScheduler() throws ServiceException;

	/**
	 * 超过30分钟未支付的订单状态修改为支付超时<br/>
	 * 每2分钟执行一次，处理当前时间前60分钟到30分钟区间状态为'未支付'订单，将其状态修改'支付失效',对于未选择支付通道的订单修改为'支付失败'。
	 * 
	 * @throws ServiceException
	 */
	void unpaying2expiredOrderScheduler() throws ServiceException;

	/**
	 * 超过30分钟未选择收银台的订单状态修改为支付失败<br/>
	 * 每2分钟执行一次，处理当前时间前60分钟到30分钟区间状态为'初始状态'订单，将其状态修改'支付失败'。
	 * 
	 * @throws ServiceException
	 */
	void initial2failureOrderScheduler() throws ServiceException;

	/**
	 * 处理审核中代付订单<br/>
	 * 每120分钟执行一次s
	 * 
	 * @throws ServiceException
	 */
	void issuedingOrderScheduler();

}
