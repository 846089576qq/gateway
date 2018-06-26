package com.gateway.payment.service.adapter;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.gateway.common.constants.CommonConstant;
import com.gateway.common.constants.param.QueryRefundParamConstant;
import com.gateway.common.constants.param.RefundNotifyConstant;
import com.gateway.common.constants.status.RefundOrderStatusConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.OrderRefundEntity;
import com.gateway.payment.entity.PersonApplicationEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.gateway.payment.service.adapter.IRefundQueryServiceAdapter;
import com.gateway.payment.service.base.IBaseRefundService;
import com.zitopay.foundation.common.exception.ServiceException;

/**
 * 融智付退款订单查询服务实现
 * 
 * 作者：王政 创建时间：2017年3月6日 下午5:59:52
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class RefundQueryServiceImplAdapter extends BaseServiceImplAdapter implements IRefundQueryServiceAdapter {

	@Override
	public Map<String, Object> checkQueryParameter(Map<String, String> paramMap, String serialNo) throws ServiceException {
		try {
			Map<String, Object> map = super.checkParameter(paramMap, serialNo);
			if (!ResponseInfoEnum.调用成功.equals(map)) {
				return map;
			}

			Integer gatewayId = Integer.valueOf(paramMap.get(QueryRefundParamConstant.param_gatewayId)); // 通道ID，由融智付分配
			if (null == gatewayId) {
				return ResponseInfoEnum.参数为空.getMap("参数[" + QueryRefundParamConstant.param_gatewayId + "]不能为空");
			}

			String merchantRefundOrderId = paramMap.get(QueryRefundParamConstant.param_merchantRefundOrderId); // 商户订单号
			if (StringUtils.isBlank(merchantRefundOrderId)) {
				return ResponseInfoEnum.参数为空.getMap("参数[" + QueryRefundParamConstant.param_merchantRefundOrderId + "]不能为空");
			}

			return ResponseInfoEnum.调用成功.getMap();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",查询参数校验异常,请检查!", e);
		}
	}

	@Override
	public Map<String, Object> checkQueryPermission(String merchantId, String applicationId, String gatewayId, String serialNo)
			throws ServiceException {
		try {
			return checkPermission(merchantId, applicationId, gatewayId, serialNo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",查询权限校验异常,请检查!", e);
		}
	}

	@Override
	public Map<String, Object> execQuery(PersonEntity zitopayPerson, GatewayEntity zitopayGeteway,
			PersonApplicationEntity zitopayPersonApplication, PersonGatewayEntity zitopayPersonGeteway, Map<String, String> paramMap,
			String serialNo) throws ServiceException {
		try {
			String appid = paramMap.get(QueryRefundParamConstant.param_appId);// 商户的应用id，由融智付分配
			String merchantRefundOrderId = paramMap.get(QueryRefundParamConstant.param_merchantRefundOrderId);
			OrderRefundEntity refundOrder = null;
			// 查询退款订单信息
			if (!StringUtils.isBlank(merchantRefundOrderId)) {// 如果传入订单号是空,就是批量查询
				refundOrder = orderRefundService.getZitopayOrderRefund(appid, merchantRefundOrderId);
				if ((RefundOrderStatusConstant.STATUS_PROCESSING == refundOrder.getType())) {
					OrderRefundEntity requestOrder = requestQuery(refundOrder, zitopayPersonGeteway, zitopayPerson, serialNo);
					if (requestOrder != null) {
						refundOrder = requestOrder;
					}
				}
			}

			if (refundOrder == null) {
				return ResponseInfoEnum.内容为空.getMap();
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put(RefundNotifyConstant.param_refund_id, refundOrder.getId());
			resultMap.put(RefundNotifyConstant.param_order_id, refundOrder.getOrderid());
			resultMap.put(RefundNotifyConstant.param_merchantRefundOrderId, refundOrder.getOrderidinf());
			resultMap.put(RefundNotifyConstant.param_refundAmount, refundOrder.getAmount() == null ? null : refundOrder.getAmount().toString());
			resultMap.put(RefundNotifyConstant.param_refundStatus, refundOrder.getType() == null ? null : refundOrder.getType().toString());
			resultMap.put(RefundNotifyConstant.param_refundNo, refundOrder.getRefundno());
			resultMap.put(RefundNotifyConstant.param_topRefundNo, refundOrder.getToprefundno());

			return ResponseInfoEnum.调用成功.getMap(resultMap);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",查询调用异常,请检查!", e);
		}
	}

	private OrderRefundEntity requestQuery(OrderRefundEntity orderRefund, PersonGatewayEntity zitopayPersonGeteway,
			PersonEntity personEntity, String serialNo) throws ServiceException {
		IBaseRefundService baseRefundApiService = getRefundService(String.valueOf(zitopayPersonGeteway.getGetewayid()));
		if (null == baseRefundApiService) {
			return null;
		}
		Map<String, Object> map = baseRefundApiService.queryOrder(orderRefund, orderService.findByOrderId(orderRefund.getOrderid()),
				zitopayPersonGeteway, serialNo);
		if (!ResponseInfoEnum.调用成功.equals(map)) {
			logger.info("手动查询第三方公司订单失败,订单号:{},失败原因:{}", orderRefund.getOrderid(), map);
			return null;
		}
		String merchantId = personEntity.getPid();
		String status = String.valueOf(map.get(CommonConstant.TRADESTATUS)); // 退款状态
		String payNo = String.valueOf(map.get(CommonConstant.TRADENO)); // 上游交易流水号
		String toPayNo = String.valueOf(map.get(CommonConstant.TOPTRADENO)); // 顶级交易流水号
		String desc = String.valueOf(map.get(CommonConstant.REFUNDDESC)); // 退款描述
		String threeid = orderRefund.getThreeid();
		String merchantPublicKey = getMerchantPublicKey(merchantId, serialNo);
		if (status.equals(String.valueOf(RefundOrderStatusConstant.STATUS_REFUNDED))) {
			baseRefundApiService.refundSuccess(merchantId, merchantPublicKey, threeid, payNo, toPayNo, serialNo);
		} else if (status.equals(String.valueOf(RefundOrderStatusConstant.STATUS_REFUNDFAILED))) {
			baseRefundApiService.refundFailed(merchantId, merchantPublicKey, threeid, desc, serialNo);
		}
		return orderRefundService.findBythreeid(threeid);
	}

	@Override
	public Map<String, Object> getMerchant(String merchantId, String serialNo) throws ServiceException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		PersonEntity zitopayPerson = personService.getZitopayPersonByPid(merchantId);
		if (zitopayPerson == null) {
			return ResponseInfoEnum.商户不存在.getMap();
		}
		returnMap.put(CommonConstant.ZITOPAY_ENTITY_PERSON, zitopayPerson);
		return ResponseInfoEnum.调用成功.getMap(returnMap);
	}

}