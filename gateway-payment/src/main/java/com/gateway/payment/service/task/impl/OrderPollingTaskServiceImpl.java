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
package com.gateway.payment.service.task.impl;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.gateway.common.constants.SchedulerTaskConstant;
import com.gateway.common.constants.status.OrderStatusConstant;
import com.gateway.common.utils.DateUtil;
import com.gateway.common.utils.DateUtil.DateStyle;
import com.gateway.common.utils.StringUtils;
import com.gateway.payment.entity.IssuedEntity;
import com.gateway.payment.entity.OrderEntity;
import com.gateway.payment.entity.OrderRecheckEntity;
import com.gateway.payment.persistence.service.IOrderRecheckService;
import com.gateway.payment.persistence.service.IOrderService;
import com.gateway.payment.persistence.service.IPersonGatewayService;
import com.gateway.payment.persistence.service.IPersonService;
import com.gateway.payment.service.base.impl.BaseServiceImpl;
import com.gateway.payment.service.task.IOrderPollingTaskService;
import com.zitopay.foundation.common.exception.ServiceException;
import com.zitopay.foundation.common.util.PKUtils;

/**
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月15日
 */
@Service
public class OrderPollingTaskServiceImpl extends BaseServiceImpl implements IOrderPollingTaskService {

	public static final int multiple_task_cout = 10;// 并行任务数量

	public static final int max_sync_count = 4;// 最大查询次数(调用支付公司查询)

	public static final String serialNo = "query-serialno";

	private static final Logger logger = LoggerFactory.getLogger(OrderPollingTaskServiceImpl.class);

	@Autowired
	IOrderService orderService;
	
	@Autowired
	IPersonService personService;

	@Autowired
	IPersonGatewayService personGatewayService;

	@Autowired
	IOrderRecheckService orderRecheckService;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Scheduled(cron = "0 0/15 * * * ?")
	@Override
	public void unpayingOrderScheduler() {
		try {
			boolean flag = redisTemplate.getConnectionFactory().getConnection().setNX(SchedulerTaskConstant.UNPAYINGORDERSCHEDULER.getBytes(), "lock".getBytes());
			if (!flag)
				return;
			try {
				logger.info("开始未支付订单查询上游任务");
				String startOrderId = "R" + DateUtil.DateToString(DateUtil.addMinute(DateUtil.getDate(), -30), DateStyle.YYYYMMDDHHMMSS) + "00000";
				String endOrderId = "R" + DateUtil.DateToString(DateUtil.addMinute(DateUtil.getDate(), -15), DateStyle.YYYYMMDDHHMMSS) + "99999";
				List<OrderEntity> orderList = orderService.queryOrderByStatus(startOrderId, endOrderId, OrderStatusConstant.STATUS_UNPAYING);
				logger.info("未支付订单数:{}", orderList.size());
				// 请求第三方接口查询订单最新状态
				final Queue<OrderEntity> queue = new ConcurrentLinkedQueue<>(orderList);
				ExecutorService executorService = Executors.newFixedThreadPool(multiple_task_cout);
				final CountDownLatch latch = new CountDownLatch(multiple_task_cout);
				for (int i = 0; i < multiple_task_cout; i++) {
					executorService.submit(new Runnable() {

						@Override
						public void run() {
							while (!queue.isEmpty()) {
								try {
									OrderEntity zitopayOrder = queue.poll();
									executeTask(zitopayOrder, 15);
								} catch (Exception e) {
									logger.error("订单补单处理异常\n" + e.getMessage(), e);
								}
							}
							latch.countDown();
						}
					});
				}
				latch.await();
				executorService.shutdown();
				logger.info("未支付订单查询上游任务处理结束!");
			} catch (Exception e) {
				logger.warn("未支付订单查询上游任务异常!");
				logger.error(e.getMessage(), e);
			}
			redisTemplate.delete(SchedulerTaskConstant.UNPAYINGORDERSCHEDULER);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@Scheduled(cron = "0 0/120 * * * ?")
	@Override
	public void issuedingOrderScheduler() {
		try {
			boolean flag = redisTemplate.getConnectionFactory().getConnection().setNX(SchedulerTaskConstant.ISSUEDINGSCHEDULER.getBytes(), "lock".getBytes());
			if (!flag)
				return;
			try {
				logger.info("开始代付审核中查询上游任务");
				List<IssuedEntity> orderList = issuedService.queryOrderByStatus(2);
				logger.info("代付审核中订单数:{}", orderList.size());
				// 请求第三方接口查询订单最新状态
				final Queue<IssuedEntity> queue = new ConcurrentLinkedQueue<>(orderList);
				ExecutorService executorService = Executors.newFixedThreadPool(multiple_task_cout);
				final CountDownLatch latch = new CountDownLatch(multiple_task_cout);
				for (int i = 0; i < multiple_task_cout; i++) {
					executorService.submit(new Runnable() {

						@Override
						public void run() {
							while (!queue.isEmpty()) {
								try {
									IssuedEntity issuedEntity = queue.poll();
									executeQuery(issuedEntity, serialNo);
								} catch (Exception e) {
									logger.error("代付轮循处理异常\n" + e.getMessage(), e);
								}
							}
							latch.countDown();
						}
					});
				}
				latch.await();
				executorService.shutdown();
				logger.info("代付审核中查询上游任务处理结束!");
			} catch (Exception e) {
				logger.warn("代付审核中查询上游任务异常!");
				logger.error(e.getMessage(), e);
			}
			redisTemplate.delete(SchedulerTaskConstant.ISSUEDINGSCHEDULER);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@Scheduled(cron = "0 0/30 * * * ?")
	@Override
	public void expiredOrderScheduler() {
		try {
			boolean flag = redisTemplate.getConnectionFactory().getConnection().setNX(SchedulerTaskConstant.EXPIREDORDERSCHEDULER.getBytes(), "lock".getBytes());
			if (!flag)
				return;
			try {
				logger.info("开始支付失效订单查询上游任务");
				String startOrderId = "R" + DateUtil.DateToString(DateUtil.addHour(DateUtil.getDate(), -2), DateStyle.YYYYMMDDHHMMSS) + "00000";
				String endOrderId = "R" + DateUtil.DateToString(DateUtil.addMinute(DateUtil.getDate(), -30), DateStyle.YYYYMMDDHHMMSS) + "99999";
				List<OrderEntity> orderList = orderService.queryOrderByStatus(startOrderId, endOrderId, OrderStatusConstant.STATUS_EXPIRED);
				logger.info("失效订单数:{}", orderList.size());
				final Queue<OrderEntity> queue = new ConcurrentLinkedQueue<>(orderList);
				ExecutorService executorService = Executors.newFixedThreadPool(multiple_task_cout);
				final CountDownLatch latch = new CountDownLatch(multiple_task_cout);
				for (int i = 0; i < multiple_task_cout; i++) {
					executorService.submit(new Runnable() {

						@Override
						public void run() {
							while (!queue.isEmpty()) {
								try {
									OrderEntity zitopayOrder = queue.poll();
									executeTask(zitopayOrder, 30);
								} catch (Exception e) {
									logger.error("订单补单处理异常\n" + e.getMessage(), e);
								}
							}
							latch.countDown();
						}
					});
				}
				latch.await();
				executorService.shutdown();
				logger.info("支付失效订单查询上游任务处理结束!");
			} catch (Exception e) {
				logger.warn("支付失效订单查询上游任务异常!");
				logger.error(e.getMessage(), e);
			}
			redisTemplate.delete(SchedulerTaskConstant.EXPIREDORDERSCHEDULER);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Scheduled(cron = "0 0/2 * * * ?")
	@Override
	public void unpaying2expiredOrderScheduler() {
		try {
			boolean flag = redisTemplate.getConnectionFactory().getConnection().setNX(SchedulerTaskConstant.UNPAYING2EXPIREDORDERSCHEDULER.getBytes(), "lock".getBytes());
			if (!flag)
				return;
			try {
				logger.info("开始未支付订单修改为支付失效任务");
				String startOrderId = "R" + DateUtil.DateToString(DateUtil.addHour(DateUtil.getDate(), -1), DateStyle.YYYYMMDDHHMMSS) + "00000";
				String endOrderId = "R" + DateUtil.DateToString(DateUtil.addMinute(DateUtil.getDate(), -30), DateStyle.YYYYMMDDHHMMSS) + "99999";
				orderService.updateOrder(startOrderId, endOrderId, OrderStatusConstant.STATUS_UNPAYING, OrderStatusConstant.STATUS_EXPIRED);
				logger.info("未支付订单修改为支付失效任务处理结束!");
			} catch (Exception e) {
				logger.warn("初始订单变为支付失败订单任务处理失败!");
				logger.error(e.getMessage(), e);
			}
			redisTemplate.delete(SchedulerTaskConstant.UNPAYING2EXPIREDORDERSCHEDULER);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Scheduled(cron = "0 0/2 * * * ?")
	@Override
	public void initial2failureOrderScheduler() {
		try {
			boolean flag = redisTemplate.getConnectionFactory().getConnection().setNX(SchedulerTaskConstant.INITIAL2FAILUREORDERSCHEDULER.getBytes(), "lock".getBytes());
			if (!flag)
				return;
			try {
				logger.info("开始初始订单修改为支付失败任务");
				String startOrderId = "R" + DateUtil.DateToString(DateUtil.addHour(DateUtil.getDate(), -1), DateStyle.YYYYMMDDHHMMSS) + "00000";
				String endOrderId = "R" + DateUtil.DateToString(DateUtil.addMinute(DateUtil.getDate(), -30), DateStyle.YYYYMMDDHHMMSS) + "99999";
				orderService.updateOrder(startOrderId, endOrderId, OrderStatusConstant.STATUS_INITIALIZED, OrderStatusConstant.STATUS_PAYFAILED);
				logger.info("初始订单修改为支付失败任务处理结束!");
			} catch (Exception e) {
				logger.warn("初始订单变为支付失败订单任务处理失败");
				logger.error(e.getMessage(), e);
			}
			redisTemplate.delete(SchedulerTaskConstant.INITIAL2FAILUREORDERSCHEDULER);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void executeTask(OrderEntity entity, int minuteInterval) throws ServiceException {
		OrderRecheckEntity orderRecheck = orderRecheckService.selectByOrderId(entity.getOrderid());
		if (orderRecheck == null) {
			orderRecheck = new OrderRecheckEntity();
			orderRecheck.setId(PKUtils.getInstance().longPK());
			orderRecheck.setCount(0);
			orderRecheck.setOrderid(entity.getOrderid());
			orderRecheck.setCreatetime(DateUtil.getDate());
			orderRecheckService.saveRecord(orderRecheck);
			return;
		}
		// 执行条件：执行次数小于最大查询次数,定时任务执行间隔内只执行过一次
		if (orderRecheck.getCount() < max_sync_count || DateUtil.addMinute(DateUtil.getDate(), -minuteInterval).after(orderRecheck.getCreatetime())) {
			String gatewayId = String.valueOf(entity.getGetewayid());
			if (StringUtils.isNull(gatewayId)) {
				return;
			}
			executeQuery(entity, serialNo);// 处理查询、通知商户、分润等
			orderRecheck.setCount(orderRecheck.getCount() + 1);
			orderRecheck.setCreatetime(DateUtil.getDate());
			orderRecheckService.updateByPrimaryKey(orderRecheck);
		}
	}

}
