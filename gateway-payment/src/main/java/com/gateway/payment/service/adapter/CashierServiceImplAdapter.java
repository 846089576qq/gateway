package com.gateway.payment.service.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gateway.common.constants.CashwayTypeConstant;
import com.gateway.common.constants.CommonConstant;
import com.gateway.common.constants.config.GatewayConstants;
import com.gateway.common.constants.param.PaymentResultParamConstant;
import com.gateway.common.constants.param.QueryChannelParamConstant;
import com.gateway.common.constants.status.OrderStatusConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.entity.OrderEntity;
import com.gateway.payment.entity.PersonApplicationEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.service.adapter.ICashierServiceAdapter;
import com.zitopay.foundation.common.exception.ServiceException;
import com.zitopay.foundation.common.util.StringUtils;

/**
 * 融智付收银台服务实现
 * 
 * 作者：王政 创建时间：2017年3月6日 下午5:58:58
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class CashierServiceImplAdapter extends BaseServiceImplAdapter implements ICashierServiceAdapter {

	private static final Logger logger = LoggerFactory.getLogger(CashierServiceImplAdapter.class);

	@Override
	public Map<String, Object> checkCashierParameter(Map<String, String> paramMap, String serialNo) throws ServiceException {
		return checkPaymentParam(paramMap, serialNo);
	}

	@Override
	public Map<String, Object> checkCashierPermission(String merchantId, String applicationId, String serialNo) throws ServiceException {
		return checkPermission(merchantId, applicationId, null, serialNo);
	}

	@Override
	public Map<String, Object> execCashier(PersonEntity zitopayPerson, PersonApplicationEntity zitopayPersonApplication, Map<String, String> paramMap, String type, String serialNo) throws ServiceException {
		try {
			// 生成订单
			Map<String, Object> buildOrderMap = initPaymentTrade(zitopayPersonApplication, zitopayPerson, null, paramMap, serialNo);
			if(!ResponseInfoEnum.调用成功.equals(buildOrderMap)) {
				return buildOrderMap;
			}
			Object o = buildOrderMap.get(CommonConstant.ZITOPAY_ENTITY_ORDER);
			OrderEntity zitopayOrder = null;
			if (null != o) {
				zitopayOrder = (OrderEntity) o;
			}
			if (zitopayOrder == null) {
				return ResponseInfoEnum.参数组装失败.getMap("创建商户[" + zitopayPerson.getId() + "],支付订单失败");
			}
			// 订单状态是初始状态[0]或未支付状态[2]才可以操作
			if (OrderStatusConstant.STATUS_INITIALIZED != zitopayOrder.getState() && OrderStatusConstant.STATUS_UNPAYING != zitopayOrder.getState()) {
				return ResponseInfoEnum.订单状态失败.getMap();
			}
			// 获取商户配置开通的渠道列表
			List<Map<String, Object>> channelList = personGatewayService.findIndividualPayChannel(zitopayPersonApplication.getAppId(), zitopayPerson.getId());
			if (channelList == null || channelList.isEmpty()) {
				return ResponseInfoEnum.商户通道不存在.getMap();
			}
			for (Iterator<Map<String, Object>> itor = channelList.iterator(); itor.hasNext();) {
				Map<String, Object> channelMap = itor.next();
				String channelId = String.valueOf(channelMap.get(QueryChannelParamConstant.cashier_param_gatewayId));
				String channelUrl = null;
				try {
					if (CashwayTypeConstant.CASHIER_TYPE_PC.equals(type)) {
						channelUrl = GatewayConstants.GatewayURL.getEnum(channelId).getUrl();
					} else if (CashwayTypeConstant.CASHIER_TYPE_H5.equals(type)) {
						channelUrl = GatewayConstants.H5URL.getEnum(channelId).getUrl();
					}
				} catch (Exception ee) {
					logger.error(ee.getMessage(), ee);
				}
				if (StringUtils.isEmpty(channelUrl)) {
					itor.remove();
				}else {
					channelMap.put(PaymentResultParamConstant.param_url, channelUrl);
				}
			}

			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put(CommonConstant.ZITOPAY_ENTITY_ORDER, zitopayOrder);
			returnMap.put(CommonConstant.ZITOPAY_CHANNEL_LIST, channelList);
			return ResponseInfoEnum.调用成功.getMap(returnMap);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof ServiceException) {
				throw e;
			} else {
				throw new ServiceException("报文序列号:" + serialNo + ",收银台异常,请检查!", e);
			}
		}
	}

	@Override
	public Map<String, Object> payResult(String orderid) {
		OrderEntity zitopayOrder = orderService.findByOrderId(orderid);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put(CommonConstant.ZITOPAY_ENTITY_ORDER, zitopayOrder);
		return ResponseInfoEnum.调用成功.getMap(returnMap);
	}

}