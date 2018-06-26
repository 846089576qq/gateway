package com.gateway.payment.service.base.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gateway.common.constants.param.RefundNotifyConstant;
import com.gateway.common.constants.status.OrderStatusConstant;
import com.gateway.common.constants.status.RefundOrderStatusConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.entity.OrderEntity;
import com.gateway.payment.entity.OrderRefundEntity;
import com.gateway.payment.service.base.IBaseRefundService;
import com.zitopay.foundation.common.exception.ServiceException;

public abstract class BaseRefundServiceImpl extends BaseServiceImpl implements IBaseRefundService {

	@Override
	public Map<String, Object> checkParam(Map<String, String> paramMap) throws ServiceException {
		return ResponseInfoEnum.调用成功.getMap();
	}
	
	public Map<String, Object> refunding(String merchantId, String merchantPublicKey, String threeid,String refundNo,String topRefundNo,String serialNo) {
		final OrderRefundEntity orderRefundEntity = orderRefundService.findBythreeid(threeid);
		if (null == orderRefundEntity) {
			return ResponseInfoEnum.退款订单不存在.getMap();
		}
		orderRefundEntity.setType(RefundOrderStatusConstant.STATUS_PROCESSING);// 退款状态(0:退款失败，1:退款成功,
																				// 2初始状态)
		if(!StringUtils.isEmpty(refundNo)){// 上游通道退款流水号,有则保存
			orderRefundEntity.setRefundno(refundNo);
		}
		if(!StringUtils.isEmpty(topRefundNo)){// 支付公司上游退款流水号,有则保存
			orderRefundEntity.setToprefundno(topRefundNo);
		}
		orderRefundService.updateSelective(orderRefundEntity);// 修改退款表
		logger.info("退款中，融智付订单号：" + orderRefundEntity.getOrderid());
		return ResponseInfoEnum.调用成功.getMap(new HashMap<String, Object>() {

			private static final long serialVersionUID = 1L;
			{
				put(RefundNotifyConstant.param_refund_id, orderRefundEntity.getId());
				put(RefundNotifyConstant.param_merchantRefundOrderId, orderRefundEntity.getOrderidinf());
				put(RefundNotifyConstant.param_refundStatus, orderRefundEntity.getType() == null ? null : orderRefundEntity.getType());
			}
		});
	}

	public Map<String, Object> refundSuccess(String merchantId, String merchantPublicKey, String threeid, String refundNo, String topRefundNo, String serialNo) {
		final OrderRefundEntity orderRefundEntity = orderRefundService.findBythreeid(threeid);
		if (null == orderRefundEntity) {
			return ResponseInfoEnum.退款订单不存在.getMap();
		}
		final String orderid = orderRefundEntity.getOrderid();
		OrderEntity orderEntity = orderService.findByOrderId(orderid);
		if (null == orderEntity) {
			return ResponseInfoEnum.订单不存在.getMap();
		}
		// 退款总金额 = 退款总金额+退款申请金额
		if(null == orderEntity.getRefundmoney()){
			orderEntity.setRefundmoney(new BigDecimal(0));
		}
		BigDecimal refundmoney = orderEntity.getRefundmoney().add(orderRefundEntity.getAmount());
		orderEntity.setRefundmoney(refundmoney);// 退款总金额
		if (orderEntity.getTotalprice().equals(refundmoney)) { // 如果退款总金额==用户实际支付金额，状态改为已退款
			orderEntity.setState(OrderStatusConstant.STATUS_REFUNDED); // 订单状态（0初始状态,1成功，2未支付，3失败，4失效，5已退款,6部分退款）
			// 全部退款时,不收手续费, 所以这里要把实际到账金额[amount]变为0和手续费[ratemmoney]变为0
			orderEntity.setAmount(new BigDecimal(0));// 实际到账金额
			orderEntity.setRatemmoney(new BigDecimal(0));// 手续费[ratemmoney]
		} else {
			// 实际到账金额 = 支付总金额-退款总金额-手续费
			BigDecimal amount = orderEntity.getTotalprice().subtract(refundmoney).subtract(orderEntity.getRatemmoney());
			orderEntity.setAmount(amount);// 实际到账金额
			orderEntity.setState(OrderStatusConstant.STATUS_REFUNDEDPART);// 订单状态（0初始状态,1成功，2未支付，3失败，4失效，5已退款,6部分退款）
		}
		orderService.updateSelective(orderEntity);// 更新
		logger.info("退款更新本地数据库成功，融智付订单号：" + orderEntity.getOrderid());
		orderRefundEntity.setType(RefundOrderStatusConstant.STATUS_REFUNDED);// 退款状态(0:退款失败，1:退款成功,
																				// 2初始状态)
		if(!StringUtils.isEmpty(refundNo)){// 上游通道退款流水号,有则保存
			orderRefundEntity.setRefundno(refundNo);
		}
		if(!StringUtils.isEmpty(topRefundNo)){// 支付公司上游退款流水号,有则保存
			orderRefundEntity.setToprefundno(topRefundNo);
		}
		orderRefundService.updateSelective(orderRefundEntity);// 修改退款表
		logger.info("修改退款表成功，融智付订单号：" + orderEntity.getOrderid());
		// 异步回调地址为terminal-client时,不发送异步通知
		/*if (!orderRefundEntity.getBgreturl().equals("terminal-client")) {
			String notifyParam = refundCallbackParams(merchantId, merchantPublicKey, orderEntity, orderRefundEntity, serialNo);
			sendNotify(orderRefundEntity, orderRefundEntity.getBgreturl(), notifyParam);
			logger.info("参数通知商户成功");
			return ResponseInfoEnum.调用成功.getMap();
		} else {
			return ResponseInfoEnum.调用成功.getMap(new HashMap<String, Object>() {

				*//**
				 * 
				 *//*
				private static final long serialVersionUID = 1L;

				{
					put(RefundNotifyConstant.param_order_id, orderid);
					put(RefundNotifyConstant.param_refundStatus, "success");
				}
			});
		}*/
		return ResponseInfoEnum.退款成功.getMap(new HashMap<String, Object>() {

			private static final long serialVersionUID = 1L;

			{
				put(RefundNotifyConstant.param_refund_id, orderRefundEntity.getId());
				put(RefundNotifyConstant.param_merchantRefundOrderId, orderRefundEntity.getOrderidinf());
				put(RefundNotifyConstant.param_refundStatus, orderRefundEntity.getType() == null ? null : orderRefundEntity.getType());
			}
		});
	}
	
	public Map<String, Object> refundFailed(String merchantId, String merchantPublicKey, String threeid, String desc, String serialNo) {
		OrderRefundEntity orderRefundEntity = orderRefundService.findBythreeid(threeid);
		if (null == orderRefundEntity) {
			return ResponseInfoEnum.退款订单不存在.getMap();
		}
		orderRefundEntity.setType(RefundOrderStatusConstant.STATUS_REFUNDFAILED);// 退款状态(0:退款失败，1:退款成功,
																				// 2初始状态)
		orderRefundService.updateSelective(orderRefundEntity);// 修改退款表
		logger.info("退款失败，融智付订单号：" + orderRefundEntity.getOrderid());
		return ResponseInfoEnum.退款失败.getMap(desc);
	}

}
