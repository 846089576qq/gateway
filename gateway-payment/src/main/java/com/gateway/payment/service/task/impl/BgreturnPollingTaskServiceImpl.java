package com.gateway.payment.service.task.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.gateway.common.constants.SchedulerTaskConstant;
import com.gateway.common.constants.status.BgReturnConstant;
import com.gateway.payment.entity.BgreturnEntity;
import com.gateway.payment.entity.OrderEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.persistence.service.IBgreturnService;
import com.gateway.payment.persistence.service.IOrderService;
import com.gateway.payment.persistence.service.IPersonService;
import com.gateway.payment.service.base.IBasePaymentService;
import com.gateway.payment.service.base.impl.BaseServiceImpl;
import com.gateway.payment.service.task.IBgreturnPollingTaskService;
import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 路径:com.zitopay.gateway.payment.service.task.impl 类名称:BgreturnPollingTaskService 描述:回调商户定时器 创建人:sms 创建时间:2017-05-16 13:37 版本：1.0.0
 */
@Service
public class BgreturnPollingTaskServiceImpl extends BaseServiceImpl implements IBgreturnPollingTaskService {

	private static final Logger logger = LoggerFactory.getLogger(BgreturnPollingTaskServiceImpl.class);

	private static final String serialNo = "notify-serialno";

	@Autowired
	IBgreturnService bgreturnService;

	@Autowired
	IPersonService personService;

	@Autowired
	IOrderService orderService;

	@Scheduled(cron = "0 0/2 * * * ?")
	public void paymentNotifyScheduler() {
		try {
			/*boolean flag = redisTemplate.getConnectionFactory().getConnection().setNX(SchedulerTaskConstant.PAYMENTNOTIFY_SCHEDULER.getBytes(), "lock".getBytes());
			if (!flag)
				return;*/
			try {
				/* 获取回调表中所有支付成功未反馈的回调信息（支付成功回调） */
				logger.info("开始支付交易异步通知商户任务");
				List<BgreturnEntity> bgreturnEntityList = bgreturnService.findByList(BgReturnConstant.BGRETURN_TYPE_PAYMENT,BgReturnConstant.UNFEEDBACK, BgReturnConstant.NOTIFY_LIMIT);
				logger.info("未对商户进行异步通知订单数:{}", bgreturnEntityList.size());
				for (BgreturnEntity zitopayBgreturn : bgreturnEntityList) {
					try {
						callbackMerchantsParameter(zitopayBgreturn);// 执行组装支付参数并请求
					} catch (Exception e) {
						logger.error("支付交易通知商户任务异常", e);
					}
				}
				logger.info("支付交易异步通知商户任务结束");
			} catch (Exception e) {
				logger.warn("初始订单变为支付失败订单任务处理失败");
				logger.error(e.getMessage(), e);
			}
			/*redisTemplate.delete(SchedulerTaskConstant.PAYMENTNOTIFY_SCHEDULER);*/
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 方法描述: 组装支付参数并请求<br>
	 * 
	 * @return void <br>
	 *         作者： sms <br>
	 *         创建时间： 2017年5月16日09:46:34
	 * @throws ServiceException
	 */
	private void callbackMerchantsParameter(BgreturnEntity zitopayBgreturn) throws ServiceException {
		logger.debug("支付交易异步通知商户任务订单号:{}.", zitopayBgreturn.getId());
		OrderEntity orderEntity = orderService.findByOrderId(zitopayBgreturn.getId());
		if (null != orderEntity) {
			PersonEntity zitopayPerson = personService.queryByPersonId(orderEntity.getPersionid());// 查询商户表
			String merchantId = zitopayPerson.getPid();
			String merchantPublicKey = getMerchantPublicKey(merchantId, serialNo);
			IBasePaymentService basePaymentService = getPaymentService(String.valueOf(orderEntity.getGetewayid()));
			if (basePaymentService == null) {
				logger.debug("未找到通道[{}]的实例,停止轮询!", orderEntity.getGetewayid());
				return;
			}
			String notifyParam = basePaymentService.assemblyNotifyParam(merchantId, merchantPublicKey, serialNo, orderEntity);
			basePaymentService.sendNotify(orderEntity, notifyParam);
		} else {// 如果不存在订单则删除
			logger.debug("订单[{}]不存在，修改回调次数为-1.", zitopayBgreturn.getId());
			zitopayBgreturn.setNum(-1);
			bgreturnService.update(zitopayBgreturn);
		}
	}
}
