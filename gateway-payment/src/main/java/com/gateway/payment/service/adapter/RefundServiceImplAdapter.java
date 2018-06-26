package com.gateway.payment.service.adapter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.gateway.common.constants.param.RefundParamConstant;
import com.gateway.common.constants.status.DisabledStatusConstant;
import com.gateway.common.constants.status.OrderStatusConstant;
import com.gateway.common.constants.status.RefundOrderStatusConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.OrderEntity;
import com.gateway.payment.entity.OrderRefundEntity;
import com.gateway.payment.entity.PersonApplicationEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.gateway.payment.service.adapter.IRefundServiceAdapter;
import com.gateway.payment.service.base.IBaseRefundService;
import com.zitopay.foundation.common.exception.ServiceException;
import com.zitopay.foundation.common.util.PKUtils;
import com.zitopay.foundation.common.util.StringUtils;

/**
 * 融智付退款服务实现
 * 
 * 作者：王政 创建时间：2017年3月6日 下午6:00:21
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class RefundServiceImplAdapter extends BaseServiceImplAdapter implements IRefundServiceAdapter {

	@Override
	public Map<String, Object> checkRefundParameter(Map<String, String> paramMap, String serialNo) throws ServiceException {
		try {
			Map<String, Object> map = super.checkParameter(paramMap, serialNo);
			if (!ResponseInfoEnum.调用成功.equals(map)) {
				return map;
			}

			String orderId = String.valueOf(paramMap.get(RefundParamConstant.param_order_id));// 商户传入融智付订单号
			if (StringUtils.isEmpty(orderId)) {
				return ResponseInfoEnum.参数为空.getMap("参数[" + RefundParamConstant.param_order_id + "]不能为空");
			}

			String totalPrice = String.valueOf(paramMap.get(RefundParamConstant.param_refundAmount));// 商品总价格，以元为单位，小数点后2位
			if (StringUtils.isEmpty(totalPrice)) {
				return ResponseInfoEnum.参数为空.getMap("参数[" + RefundParamConstant.param_refundAmount + "]不能为空");
			}
			Pattern p = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))\\.(\\d){2}?$");
			Matcher m = p.matcher(totalPrice);
			if (!m.matches()) {
				return ResponseInfoEnum.参数有误.getMap("参数[" + RefundParamConstant.param_refundAmount + "]格式不正确(0.00)");
			}

			String merchantRefundOrderId = String.valueOf(paramMap.get(RefundParamConstant.param_merchantRefundOrderId));// 商品总价格，以元为单位，小数点后2位
			if (StringUtils.isEmpty(merchantRefundOrderId)) {
				return ResponseInfoEnum.参数为空.getMap("参数[" + RefundParamConstant.param_merchantRefundOrderId + "]不能为空");
			}

			/*
			 * String merchantNotifyUrl = String.valueOf(paramMap.get(RefundParamConstant. param_merchantNotifyUrl));// 商品总价格，以元为单位，小数点后2位
			 * if (StringUtils.isEmpty(merchantNotifyUrl)) { return ResponseInfoEnum.参数为空.getMap("参数[" +
			 * RefundParamConstant.param_merchantNotifyUrl + "]不能为空"); }
			 */
			return ResponseInfoEnum.调用成功.getMap();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",退款参数校验异常,请检查!", e);
		}
	}

	@Override
	public Map<String, Object> checkRefundPermission(String merchantId, String applicationId, String gatewayId, String serialNo) throws ServiceException {
		try {
			return checkPermission(merchantId, applicationId, gatewayId, serialNo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",退款权限校验异常,请检查!", e);
		}
	}

	@Override
	public Map<String, Object> execRefund(PersonEntity zitopayPerson, PersonApplicationEntity zitopayPersonApplication, Map<String, String> paramMap, String serialNo) throws ServiceException {
		String refundOrderid = paramMap.get(RefundParamConstant.param_merchantRefundOrderId);
		String orderid = paramMap.get(RefundParamConstant.param_order_id);
		String refundAmount = paramMap.get(RefundParamConstant.param_refundAmount);
		/*
		 * String bgreturl = paramMap.get(RefundParamConstant.param_merchantNotifyUrl);
		 */
		String reason = paramMap.get(RefundParamConstant.param_reason);
		OrderRefundEntity zitopayOrderRefund = orderRefundService.getZitopayOrderRefund(zitopayPersonApplication.getAppId(), refundOrderid);
		if (null != zitopayOrderRefund) {
			return ResponseInfoEnum.退款订单已存在.getMap();
		}

		OrderEntity orderEntity = orderService.findByOrderId(zitopayPerson.getId(), zitopayPersonApplication.getAppId(), orderid);
		if (orderEntity == null) {
			return ResponseInfoEnum.订单不存在.getMap();
		}

		int state = orderEntity.getState();// 订单状态
		if (!(state == OrderStatusConstant.STATUS_PAYED || state == OrderStatusConstant.STATUS_REFUNDEDPART)) {
			return ResponseInfoEnum.订单状态失败.getMap("只有是部分退款或支付成功的才可以修改退款");
		}

		if (!orderEntity.getTotalprice().toString().equals(refundAmount)) {
			return ResponseInfoEnum.参数有误.getMap("暂时不支持部分退款，请确认退款金额！");
		}

		Integer gatewayId = orderEntity.getGetewayid();
		// 验证通道是否存在以及启用
		final GatewayEntity zitopayGeteway = gatewayService.getGateway(gatewayId); // 商户通道表
		if (zitopayGeteway == null) {
			return ResponseInfoEnum.通道不存在.getMap();
		}
		if (DisabledStatusConstant.STATUS_DISABLED.equals(zitopayGeteway.getDisable())) {
			return ResponseInfoEnum.通道已禁用.getMap();
		}

		// 验证该商户是否配置该通道配置启用
		PersonGatewayEntity zitopayPersonGeteway = personGatewayService.getZitopayPersonGateway(zitopayPerson.getId(), Integer.valueOf(gatewayId));
		if (null == zitopayPersonGeteway) {
			return ResponseInfoEnum.商户通道不存在.getMap();
		}
		if (DisabledStatusConstant.STATUS_DISABLED.equals(zitopayPersonGeteway.getDisable())) {
			return ResponseInfoEnum.商户通道被禁用.getMap();
		}

		zitopayOrderRefund = createRefundOrder(refundOrderid, refundAmount, null, reason, orderEntity);

		IBaseRefundService basePayApiService = getRefundService(gatewayId.toString());
		if (null != basePayApiService) {
			return basePayApiService.refund(zitopayPerson, zitopayGeteway, zitopayPersonApplication, zitopayPersonGeteway, orderEntity, zitopayOrderRefund, paramMap, serialNo);
		}
		return ResponseInfoEnum.调用失败.getMap("未找到编号[" + gatewayId + "]的支付通道");
	}

	private OrderRefundEntity createRefundOrder(String refundOrderid, String refundAmount, String bgreturl, String reason, OrderEntity zitopayOrder) {
		OrderRefundEntity zitopayOrderRefund = new OrderRefundEntity();
		zitopayOrderRefund.setId("T" + PKUtils.getInstance().longPK());
		zitopayOrderRefund.setOrderidinf(refundOrderid);// 退款流水号
		zitopayOrderRefund.setAppId(zitopayOrder.getAppId());// 应用标识
		zitopayOrderRefund.setOrderid(zitopayOrder.getOrderid());// 订单主键
		zitopayOrderRefund.setAmount(new BigDecimal(refundAmount));// 退款申请金额
		zitopayOrderRefund.setReason(reason);// 退款原因
		zitopayOrderRefund.setBgreturl(bgreturl);// 退款异步回调
		zitopayOrderRefund.setThreeid(String.valueOf(PKUtils.getInstance().longPK()));

		zitopayOrderRefund.setCreatetime(new Date());// 创建时间

		zitopayOrderRefund.setType(RefundOrderStatusConstant.STATUS_INITIALIZED);// 退款状态(0:退款失败，1:退款成功,2初始状态)
		orderRefundService.saveSelective(zitopayOrderRefund);// 新增退款表
		logger.info("退款更新退款表成功，融智付订单号：" + zitopayOrder.getOrderid());
		return zitopayOrderRefund;
	}

	@Override
	public Map<String, Object> execCallback(PersonEntity zitopayPerson, GatewayEntity zitopayGeteway, PersonApplicationEntity zitopayPersonApplication, PersonGatewayEntity zitopayPersonGeteway, Map<String, String> paramMap, String serialNo) throws ServiceException {
		String gatewayId = String.valueOf(zitopayGeteway.getId());
		IBaseRefundService basePayApiService = getRefundService(gatewayId.toString());
		if (null == basePayApiService) {
			return ResponseInfoEnum.退款回调失败.getMap("未找到编号[" + gatewayId + "]的支付通道");
		}
		String merchantPublicKey = getMerchantPublicKey(zitopayPerson.getPid(), serialNo);
		return basePayApiService.refundCallback(zitopayPerson, zitopayGeteway, zitopayPersonApplication, zitopayPersonGeteway, paramMap, merchantPublicKey, serialNo);
	}

}