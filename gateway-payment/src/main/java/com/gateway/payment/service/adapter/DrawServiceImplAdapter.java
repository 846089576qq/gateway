package com.gateway.payment.service.adapter;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.gateway.common.constants.param.DrawParamConstant;
import com.gateway.common.constants.status.WithdrawStatusConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.entity.AccountSystemCashEntity;
import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.gateway.payment.service.adapter.IDrawServiceAdapter;
import com.gateway.payment.service.base.IBaseDrawService;
import com.zitopay.foundation.common.exception.ServiceException;
import com.zitopay.foundation.common.util.StringUtils;

/**
 * 融智付订单提现服务实现
 * 
 * 作者：王政 创建时间：2017年3月6日 下午5:59:52
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class DrawServiceImplAdapter extends BaseServiceImplAdapter implements IDrawServiceAdapter {

	@Override
	public Map<String, Object> checkDrawParameter(Map<String, String> paramMap, String serialNo) throws ServiceException {
		logger.info("开始验证支付参数,内容:{}.", paramMap);

		if (paramMap == null || paramMap.isEmpty()) {
			return ResponseInfoEnum.参数为空.getMap();
		}

		String merchantId = paramMap.get(DrawParamConstant.param_merchantId);// 商户id，由融智付分配
		if (StringUtils.isEmpty(merchantId)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + DrawParamConstant.param_merchantId + "]不能为空");
		}

		Integer gatewayId = Integer.valueOf(paramMap.get(DrawParamConstant.param_gatewayId)); // 通道ID，由融智付分配
		if (null == gatewayId) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + DrawParamConstant.param_gatewayId + "]不能为空");
		}

		String merchantDrawOrderId = paramMap.get(DrawParamConstant.param_merchantDrawOrderId); // 商户提现订单号
		if (StringUtils.isEmpty(merchantDrawOrderId)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + DrawParamConstant.param_merchantDrawOrderId + "]不能为空");
		}

		return ResponseInfoEnum.调用成功.getMap();
	}

	@Override
	public Map<String, Object> checkDrawPermission(String merchantId, String gatewayId, String serialNo) throws ServiceException {
		return checkPermission(merchantId, null, gatewayId, serialNo);
	}

	@Override
	public Map<String, Object> execDraw(PersonEntity zitopayPerson, GatewayEntity zitopayGeteway, PersonGatewayEntity zitopayPersonGeteway, Map<String, String> paramMap, String serialNo) throws ServiceException {
		try {
			String gatewayId = String.valueOf(zitopayGeteway.getId());
			String merchantDrawOrderId = paramMap.get(DrawParamConstant.param_merchantDrawOrderId);
			AccountSystemCashEntity accountSystemCash = accountSystemCashService.getZitopayAccountSystemCash(zitopayPerson.getId().toString(), gatewayId, merchantDrawOrderId);
			if (null == accountSystemCash) {
				return ResponseInfoEnum.参数有误.getMap("未找到该提现订单：" + merchantDrawOrderId);
			}

			if (!WithdrawStatusConstant.STATUS_WITHDRAWING.equals(accountSystemCash.getState())) {
				return ResponseInfoEnum.调用失败.getMap("该提现订单已处理完成，状态为：" + accountSystemCash.getState());
			}

			IBaseDrawService baseDrawApiService = getDrawService(gatewayId);
			if (null == baseDrawApiService) {
				return ResponseInfoEnum.调用失败.getMap("未找到编号[" + gatewayId + "]的提现通道");
			}
			return baseDrawApiService.draw(zitopayPerson, zitopayGeteway, zitopayPersonGeteway, accountSystemCash, paramMap, serialNo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",提现异常,请检查!", e);
		}
	}

	@Override
	public Map<String, Object> execCallback(PersonEntity personEntity, GatewayEntity zitopayGeteway, PersonGatewayEntity zitopayPersonGeteway, Map<String, String> paramMap, String serialNo) throws ServiceException {
		try {
			Integer gatewayId = zitopayGeteway.getId();
			IBaseDrawService baseDrawApiService = getDrawService(gatewayId.toString());
			if (baseDrawApiService == null) {
				return ResponseInfoEnum.提现回调失败.getMap("未找到编号[" + gatewayId + "]的支付通道");
			}
			String merchantKey = getMerchantPublicKey(personEntity.getPid(), serialNo);
			return baseDrawApiService.drawCallback(personEntity, zitopayGeteway, zitopayPersonGeteway, merchantKey, paramMap);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",提现回调异常,请检查!", e);
		}
	}

}