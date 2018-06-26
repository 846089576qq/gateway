package com.gateway.payment.service.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.gateway.common.constants.CommonConstant;
import com.gateway.common.constants.param.BaseParamConstant;
import com.gateway.common.constants.param.QueryParamConstant;
import com.gateway.common.constants.status.OrderStatusConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.entity.OrderEntity;
import com.gateway.payment.entity.PersonApplicationEntity;
import com.gateway.payment.entity.PersonEntity;
import com.zitopay.foundation.common.exception.ServiceException;
import com.zitopay.foundation.common.util.DateUtils;

/**
 * 融智付订单查询服务实现
 * 
 * 作者：王政 创建时间：2017年3月6日 下午5:59:52
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class QueryServiceImplAdapter extends BaseServiceImplAdapter implements IQueryServiceAdapter {

	@Override
	public Map<String, Object> checkQueryParameter(Map<String, String> paramMap, String serialNo) throws ServiceException {
		try {
			Map<String, Object> map = super.checkParameter(paramMap, serialNo);
			if (!ResponseInfoEnum.调用成功.equals(map)) {
				return map;
			}

			String orderId = paramMap.get(QueryParamConstant.param_order_id); // 融智付订单号
			String merchantOrderId = paramMap.get(QueryParamConstant.param_merchantOrderId); // 商户订单号
			if (StringUtils.isBlank(orderId) && StringUtils.isBlank(merchantOrderId)) {
				return ResponseInfoEnum.参数为空.getMap("参数[" + QueryParamConstant.param_order_id + "]和参数[" + QueryParamConstant.param_merchantOrderId + "]不能同时为空");
			}

			return ResponseInfoEnum.调用成功.getMap();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",查询参数校验异常,请检查!", e);
		}
	}

	@Override
	public Map<String, Object> checkQueryPermission(String merchantId, String applicationId, String serialNo) throws ServiceException {
		try {
			return checkPermission(merchantId, applicationId, null, serialNo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",查询权限校验异常,请检查!", e);
		}
	}

	@Override
	public Map<String, Object> execQuery(PersonEntity zitopayPerson, PersonApplicationEntity zitopayPersonApplication, Map<String, String> paramMap, String serialNo) throws ServiceException {
		try {
			String orderId = paramMap.get(QueryParamConstant.param_order_id); // 融智付订单号
			Integer merchantId = zitopayPerson.getId();
			String appId = paramMap.get(BaseParamConstant.param_appId);
			String merchantOrderId = paramMap.get(QueryParamConstant.param_merchantOrderId);
			// 查询订单信息
			List<OrderEntity> orders = null;
			if (!StringUtils.isBlank(orderId)) {
				String[] orderIds = StringUtils.split(orderId, ",");
				if(orderIds == null || orderIds.length < 1 || orderIds.length > 10) {
					return ResponseInfoEnum.内容为空.getMap("orderId个数必须大于1个且小于等于10个");
				}
				
				orders = orderService.findByOrderIds(merchantId, appId, orderIds);
			}

			if (!StringUtils.isBlank(merchantOrderId)) {
				String[] merchantOrderIds = StringUtils.split(merchantOrderId, ",");
				if(merchantOrderIds == null || merchantOrderIds.length < 1 || merchantOrderIds.length > 10) {
					return ResponseInfoEnum.内容为空.getMap("merchantOrderId个数必须大于1个且小于等于10个");
				}
				orders = orderService.findByMerchantOrderIds(merchantId, appId, merchantOrderIds);
			}
			
			if (orders == null || orders.size() < 1) {
				return ResponseInfoEnum.内容为空.getMap();
			}
			List<OrderEntity> orderEntitys = new ArrayList<OrderEntity>();
			for(OrderEntity order : orders) {
				if ((OrderStatusConstant.STATUS_UNPAYING == order.getState()) || (OrderStatusConstant.STATUS_EXPIRED == order.getState())) {
					// 如果订单在redis里面和数据库里取出来的结果是初始化,或者已经关闭的状态,就去请求查询接口,重新更新一下订单情况
					order = requestQuery(order, serialNo);
				}
				orderEntitys.add(order);
			}

			Map<String, Object> resultMap = new HashMap<String, Object>();
			if(orderEntitys.size() > 1) {
				List<Map<String, Object>> orderlist = new ArrayList<Map<String, Object>>();
				for(OrderEntity order : orderEntitys) {
					orderlist.add(buildResult(order));
				}
				resultMap.put(QueryParamConstant.param_order_list, orderlist);// 查询到订单记录,订单集合
				resultMap.put(QueryParamConstant.param_order_num, orderlist.size());// 总条数
			}else {
				resultMap.putAll(buildResult(orderEntitys.get(0)));
			}
			return ResponseInfoEnum.调用成功.getMap(resultMap);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",查询调用异常,请检查!", e);
		}
	}

	private Map<String, Object> buildResult(OrderEntity order) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(QueryParamConstant.param_gatewayId, order.getGetewayid());
		resultMap.put(QueryParamConstant.param_appId, order.getAppId());
		resultMap.put(QueryParamConstant.param_order_id, order.getOrderid());
		resultMap.put(QueryParamConstant.param_merchantOrderId, order.getOrderidinf());
		resultMap.put(QueryParamConstant.param_orderTitle, order.getOrdertitle());
		resultMap.put(QueryParamConstant.param_payTime, order.getPaysucctime());
		resultMap.put(QueryParamConstant.param_orderTime, order.getPosttime());
		resultMap.put(QueryParamConstant.param_orderAmount, order.getTotalprice());
		resultMap.put(QueryParamConstant.param_actualAmount, order.getAmount());
		resultMap.put(QueryParamConstant.param_fee, order.getRatemmoney());
		resultMap.put(QueryParamConstant.param_state, order.getState());
		resultMap.put(QueryParamConstant.param_payNo, order.getPayno());
		resultMap.put(QueryParamConstant.param_topPayNo, order.getToppayno());
		resultMap.put(QueryParamConstant.param_terminal_no, order.getZongduanno());
		return resultMap;
	}

	private OrderEntity requestQuery(OrderEntity order, String serialNo) throws ServiceException {
		executeQuery(order, serialNo);
		return orderService.findByOrderId(order.getOrderid());
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
	
	public void allunpayingOrderScheduler(String threeorderid) {
		try {
			logger.info("开始未支付订单查询上游任务");
			/*List<OrderEntity> orderList = orderService.queryAll();
			logger.info("未支付订单数:{}", orderList.size());
			// 请求第三方接口查询订单最新状态
			for(OrderEntity zitopayOrder : orderList) {*/
			OrderEntity zitopayOrder = orderService.findByhreerderid(threeorderid);
				if(zitopayOrder.getCreatetime().after(DateUtils.parseDate("20180421", "YYYYMMdd")) && !zitopayOrder.getState().equals(0) && !zitopayOrder.getState().equals(1)) {
					executeQuery(zitopayOrder, "checkOrder");
					Thread.sleep(5000);
				}
			/*}*/
			logger.info("未支付订单查询上游任务处理结束!");
		} catch (Exception e) {
			logger.warn("未支付订单查询上游任务异常!");
			logger.error(e.getMessage(), e);
		}
}

}