package com.gateway.payment.persistence.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gateway.common.constants.param.PaymentParamConstant;
import com.gateway.common.constants.status.OrderStatusConstant;
import com.gateway.common.utils.StringUtils;
import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.OrderEntity;
import com.gateway.payment.entity.PersonApplicationEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.gateway.payment.persistence.mapper.IOrderMapper;
import com.gateway.payment.persistence.service.IOrderService;
import com.zitopay.foundation.common.util.PKUtils;

/**
 * 订单接口的实现类
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Service
public class OrderServiceImpl extends BaseGenericServiceImpl<OrderEntity> implements IOrderService {

	@Autowired
	private IOrderMapper orderMapper;

	/**
	 * 创建支付订单
	 * 
	 * @param merchantId
	 * @param appId
	 * @param orderidinf
	 * @param posttime
	 * @param totalPrice
	 * @param orderTitle
	 * @param returnUrl
	 * @param bgRetUrl
	 * @return
	 */
	public OrderEntity createOrder(PersonApplicationEntity zitopayPersonApplication, PersonEntity zitopayPerson, GatewayEntity zitopayGeteway, String orderidinf, Map<String, String> paramMap) {
		String orderTime = paramMap.get(PaymentParamConstant.param_orderTime);
		String totalPrice = paramMap.get(PaymentParamConstant.param_orderAmount);
		String orderTitle = paramMap.get(PaymentParamConstant.param_orderTitle);
		String bgRetUrl = paramMap.get(PaymentParamConstant.param_merchantNotifyUrl);
		String returnUrl = paramMap.get(PaymentParamConstant.param_merchantUrl);
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setOrderid("R" + String.valueOf(PKUtils.getInstance().longPK()));// 融智付订单号
		orderEntity.setPersionid(zitopayPerson.getId());// 商户id（商户表主键）
		orderEntity.setPosttime(orderTime);// 请求时间
		BigDecimal bg = new BigDecimal(totalPrice);
		orderEntity.setTotalprice(bg);// 支付金额
		// 商品参数
		orderEntity.setOrdertitle(orderTitle);// 商户订单标题
		// 回调地址
		orderEntity.setBgreturl(bgRetUrl);// 异步回调地址
		orderEntity.setReturnurl(returnUrl);// 返回商户url
		// 订单类参数
		orderEntity.setState(OrderStatusConstant.STATUS_INITIALIZED);// 订单状态
																		// 0：初始状态
		orderEntity.setCreatetime(new Date());// 创建时间
		orderEntity.setOrderidinf(orderidinf);// 商户传过来的订单号
		orderEntity.setCreatedate(new SimpleDateFormat("yyyyMMdd").format(new Date()));// 订单创建时间[字符串]
		orderEntity.setApplicationId(zitopayPersonApplication.getApplicationId());// 商户应用信息信息
		orderEntity.setAppId(zitopayPersonApplication.getAppId());// 应用id
		if (zitopayGeteway != null) {
			orderEntity.setGetewayid(zitopayGeteway.getId());
		}
		
		String terminalNo = paramMap.get(PaymentParamConstant.param_terminal_no);
		if(StringUtils.isNotBlank(terminalNo)) {
			orderEntity.setZongduanno(terminalNo);
		}
		String shopNo = paramMap.get(PaymentParamConstant.param_shop_no);
		String operatorNo = paramMap.get(PaymentParamConstant.param_cashier_no);
		
		if(StringUtils.isNotBlank(shopNo)) {
			orderEntity.setPaycardShopNo(shopNo);
		}
		
		if(StringUtils.isNotBlank(operatorNo)) {
			orderEntity.setPaycardOperatorNo(operatorNo);
		}
		
		createOrder(orderEntity);
		return orderEntity;
	}

	public void payingPayment(OrderEntity order, String threeorderid, PersonGatewayEntity personGeteway) {
		order.setThreeorderid(threeorderid);// 第三方订单号
		order.setRate(personGeteway.getRate());// 商户通道表的费率
		order.setState(OrderStatusConstant.STATUS_UNPAYING);// //订单表
															// 订单状态（0初始状态,1成功，2未支付，3失败，4失效，5已退款,6部分退款）
		updateByPrimaryKeySelective(order);// 根据主键修改订单表
	}

	@Override
	public OrderEntity getPersonApplicationOrder(Integer personId, String appId, String orderidinf) {
		OrderEntity entity = new OrderEntity();
		entity.setPersionid(personId);
		entity.setAppId(appId);
		entity.setOrderidinf(orderidinf);
		return queryOne(entity);
	}

	@Override
	public int updateByPrimaryKeySelective(OrderEntity orderEntity) {
		// setToRedis(orderEntity); // 把订单更新到redis
		return updateSelective(orderEntity);
	}

	@Override
	public int createOrder(OrderEntity orderEntity) {
		// setToRedis(orderEntity);// 把订单更新到redis
		return saveSelective(orderEntity);
	}

	@Override
	public OrderEntity findByOrderId(String orderId) {
		OrderEntity redisOrder = getFromRedis(orderId);
		if (null == redisOrder) {
			OrderEntity entity = new OrderEntity();
			entity.setOrderid(orderId);
			return queryOne(entity);
		} else {
			return redisOrder;
		}

	}
	
	@Override
	public List<OrderEntity> findByOrderIds(String[] orderIds) {
		List<OrderEntity> orders = new ArrayList<OrderEntity>();
		if(orderIds != null) {
			for(String orderId : orderIds) {
				OrderEntity order = findByOrderId(orderId);
				if(order != null) {
					orders.add(order);
				}
			}
		}
		return orders;
	}

	@Override
	public OrderEntity findByhreerderid(String threeorderid) {
		OrderEntity redisOrder = getRedisByGatewayOrderId(threeorderid);
		if (null == redisOrder) {
			OrderEntity entity = new OrderEntity();
			entity.setThreeorderid(threeorderid);
			return queryOne(entity);
		} else {
			return redisOrder;
		}

	}

	@Override
	public OrderEntity findByMerchantOrderId(String merchantOrderId) {
		OrderEntity redisOrder = getRedisByMerchantId(merchantOrderId);
		if (null == redisOrder) {
			OrderEntity entity = new OrderEntity();
			entity.setOrderidinf(merchantOrderId);
			return queryOne(entity);
		} else {
			return redisOrder;
		}

	}
	
	@Override
	public List<OrderEntity> findByMerchantOrderIds(String[] merchantOrderIds) {
		List<OrderEntity> orders = new ArrayList<OrderEntity>();
		if(merchantOrderIds != null) {
			for(String merchantOrderId : merchantOrderIds) {
				OrderEntity order = findByMerchantOrderId(merchantOrderId);
				if(order != null) {
					orders.add(order);
				}
			}
		}
		return orders;
	}

	@Override
	public List<OrderEntity> queryOrder(Integer personId, String appId, Integer gatewayid, String startDate, String endDate, Integer pageNo, Integer pagesize) {
		return orderMapper.queryOrder(personId, appId, gatewayid, startDate, endDate, pageNo, pagesize);
	}

	@Override
	public Integer queryOrderCount(Integer personId, String appId, Integer gatewayid, String startDate, String endDate) {
		return orderMapper.queryOrderCount(personId, appId, gatewayid, startDate, endDate);
	}

	@Override
	public int countOrderByOrderidinf(String orderidinf) {
		return orderMapper.countOrderByOrderidinf(orderidinf);
	}
	
	@Override
	public Integer updateSelective(OrderEntity record) {
		Integer size = super.updateSelective(record);
		if(size > 0) {
			// setToRedis(record);
		}
		return size;
	}

	@Override
	public void updateOrder(String startOrderId, String endOrderId, int fromStatus, int toStatus) {
		List<OrderEntity> orderEntities = orderMapper.selectOrderStartToEnd(startOrderId, endOrderId, fromStatus);
		for(OrderEntity entity : orderEntities) {
			entity.setState(toStatus);
			updateSelective(entity);
		}
	}

	@Override
	public List<OrderEntity> queryOrderByStatus(String startOrderId, String endOrderId, int status) {
		return orderMapper.queryOrdersByStatus(startOrderId, endOrderId, status);
	}

	/*public void setToRedis(OrderEntity orderEntity) {
		if (orderEntity == null)
			return;
		if (!StringUtils.isNull(orderEntity.getOrderid()))
			redisTemplate.opsForValue().set(String.format(CommonConstant.ZITOPAYORDER_REDIS_KEY, orderEntity.getOrderid()), BeanUtils.bean2JSON(orderEntity), 1, TimeUnit.DAYS);
		if (!StringUtils.isNull(orderEntity.getOrderidinf()))
			redisTemplate.opsForValue().set(String.format(CommonConstant.ZITOPAYORDER_REDIS_KEY, orderEntity.getOrderidinf()), orderEntity.getOrderid(), 1, TimeUnit.DAYS);
		if (!StringUtils.isNull(orderEntity.getThreeorderid()))
			redisTemplate.opsForValue().set(String.format(CommonConstant.ZITOPAYORDER_REDIS_KEY, orderEntity.getThreeorderid()), orderEntity.getOrderid(), 45, TimeUnit.MINUTES);
	}*/

	private OrderEntity getRedisByMerchantId(String merchantId) {
		/*String orderId = String.valueOf(redisTemplate.opsForValue().get(String.format(CommonConstant.ZITOPAYORDER_REDIS_KEY, merchantId)));
		if (!StringUtils.isNull(orderId)) {
			return getFromRedis(orderId);
		}*/
		return null;
	}

	private OrderEntity getRedisByGatewayOrderId(String gatewayOrderId) {
		/*String orderId = String.valueOf(redisTemplate.opsForValue().get(String.format(CommonConstant.ZITOPAYORDER_REDIS_KEY, gatewayOrderId)));
		if (!StringUtils.isNull(orderId)) {
			return getFromRedis(orderId);
		}*/
		return null;
	}

	public OrderEntity getFromRedis(String orderid) {
		/*String orderJson = String.valueOf(redisTemplate.opsForValue().get(String.format(CommonConstant.ZITOPAYORDER_REDIS_KEY, orderid)));
		if (!StringUtils.isNull(orderJson)) {
			OrderEntity entity = BeanUtils.json2Bean(orderJson, OrderEntity.class);
			return entity;
		}*/
		return null;
	}

	public void delFromRedis(String orderid) {
		// redisTemplate.delete(String.format(CommonConstant.ZITOPAYORDER_REDIS_KEY, orderid));
	}

	@Override
	public OrderEntity findByOrderId(Integer personid, String appId, String orderId) {
		OrderEntity redisOrder = getFromRedis(orderId);
		if (null == redisOrder) {
			OrderEntity entity = new OrderEntity();
			entity.setOrderid(orderId);
			entity.setPersionid(personid);
			entity.setAppId(appId);
			return queryOne(entity);
		} else {
			return redisOrder;
		}
	}

	@Override
	public List<OrderEntity> findByOrderIds(Integer personid, String appId, String[] orderIds) {
		List<OrderEntity> orders = new ArrayList<OrderEntity>();
		if(orderIds != null) {
			for(String orderId : orderIds) {
				OrderEntity order = findByOrderId(personid, appId, orderId);
				if(order != null) {
					orders.add(order);
				}
			}
		}
		return orders;
	}

	@Override
	public OrderEntity findByMerchantOrderId(Integer personid, String appId, String merchantOrderId) {
		OrderEntity redisOrder = getRedisByMerchantId(merchantOrderId);
		if (null == redisOrder) {
			OrderEntity entity = new OrderEntity();
			entity.setOrderidinf(merchantOrderId);
			entity.setPersionid(personid);
			entity.setAppId(appId);
			return queryOne(entity);
		} else {
			return redisOrder;
		}
	}

	@Override
	public List<OrderEntity> findByMerchantOrderIds(Integer personid, String appId, String[] merchantOrderIds) {
		List<OrderEntity> orders = new ArrayList<OrderEntity>();
		if(merchantOrderIds != null) {
			for(String merchantOrderId : merchantOrderIds) {
				OrderEntity order = findByMerchantOrderId(personid, appId, merchantOrderId);
				if(order != null) {
					orders.add(order);
				}
			}
		}
		return orders;
	}

}