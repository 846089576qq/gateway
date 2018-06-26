package com.gateway.payment.service.adapter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.gateway.common.constants.CommonConstant;
import com.gateway.common.constants.param.IssuedParamConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.IssuedEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.gateway.payment.entity.TransferEntity;
import com.gateway.payment.service.base.IBaseIssuedService;
import com.zitopay.foundation.common.exception.ServiceException;
import com.zitopay.foundation.common.util.StringUtils;

/**
 * 订单代付服务实现
 * 
 * 作者：王政 创建时间：2017年3月6日 下午5:59:52
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class IssuedServiceImplAdapter extends BaseServiceImplAdapter implements IIssuedServiceAdapter {

	@Override
	public Map<String, Object> checkIssuedParameter(Map<String, String> paramMap, String serialNo) throws ServiceException {
		logger.info("开始验证代付参数,内容:{}.", paramMap);

		if (paramMap == null || paramMap.isEmpty()) {
			return ResponseInfoEnum.参数为空.getMap();
		}

		String merchantId = paramMap.get(IssuedParamConstant.param_merchantId);// 商户id，由融智付分配
		if (StringUtils.isEmpty(merchantId)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + IssuedParamConstant.param_merchantId + "]不能为空");
		}

		Integer gatewayId = Integer.valueOf(paramMap.get(IssuedParamConstant.param_gatewayId)); // 通道ID，由融智付分配
		if (null == gatewayId) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + IssuedParamConstant.param_gatewayId + "]不能为空");
		}

		String merchantTransferOrderId = paramMap.get(IssuedParamConstant.param_merchantOrderId); // 商户代付订单号
		if (StringUtils.isEmpty(merchantTransferOrderId)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + IssuedParamConstant.param_merchantOrderId + "]不能为空");
		}
		
		String totalPrice = paramMap.get(IssuedParamConstant.param_orderAmount);// 商品总价格，以元为单位，小数点后2位
		if (StringUtils.isEmpty(totalPrice)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + IssuedParamConstant.param_orderAmount + "]不能为空");
		}
		Pattern p = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))\\.(\\d){2}?$");
		Matcher m = p.matcher(totalPrice);
		if (!m.matches()) {
			return ResponseInfoEnum.参数有误.getMap("参数[" + IssuedParamConstant.param_orderAmount + "]格式不正确(0.00)");
		}
		
		String bankName = paramMap.get(IssuedParamConstant.param_bankName);
		if (StringUtils.isBlank(bankName)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + IssuedParamConstant.param_bankName + "]不能为空");
		}

		String branchBankName = paramMap.get(IssuedParamConstant.param_branchBankName);
		if (StringUtils.isBlank(branchBankName)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + IssuedParamConstant.param_branchBankName + "]不能为空");
		}
		
		String bankProvince = paramMap.get(IssuedParamConstant.param_bankProvince);
		if (StringUtils.isBlank(bankProvince)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + IssuedParamConstant.param_bankProvince + "]不能为空");
		}

		String bankCity = paramMap.get(IssuedParamConstant.param_bankCity);
		if (StringUtils.isBlank(bankCity)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + IssuedParamConstant.param_bankCity + "]不能为空");
		}
		
		String bankCode = paramMap.get(IssuedParamConstant.param_custCardNo);
		if (StringUtils.isBlank(bankCode)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + IssuedParamConstant.param_custCardNo + "]不能为空");
		}

		String cardMobile = paramMap.get(IssuedParamConstant.param_custCardMobile);
		if (StringUtils.isBlank(cardMobile)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + IssuedParamConstant.param_custCardMobile + "]不能为空");
		}

		String cardIdentityCode = paramMap.get(IssuedParamConstant.param_custIdentityCode);
		if (StringUtils.isBlank(cardIdentityCode)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + IssuedParamConstant.param_custIdentityCode + "]不能为空");
		}

		String cardRealName = paramMap.get(IssuedParamConstant.param_custRealName);
		if (StringUtils.isBlank(cardRealName)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + IssuedParamConstant.param_custRealName + "]不能为空");
		}
		
		return ResponseInfoEnum.调用成功.getMap();
	}

	@Override
	public Map<String, Object> checkIssuedPermission(String merchantId, String gatewayId, String serialNo) throws ServiceException {
		return checkPermission(merchantId, null, gatewayId, serialNo);
	}

	@Override
	public Map<String, Object> execIssued(PersonEntity personEntity, GatewayEntity gatewayEntity, PersonGatewayEntity zitopayPersonGeteway, Map<String, String> paramMap, String serialNo) throws ServiceException {
		try {
			String gatewayId = String.valueOf(gatewayEntity.getId());
			Map<String, Object> buildOrderMap = initIssued(personEntity, gatewayEntity, paramMap, serialNo);
			if (!ResponseInfoEnum.调用成功.equals(buildOrderMap)) {
				return buildOrderMap;
			}
			IssuedEntity issuedEntity = (IssuedEntity) buildOrderMap.get(CommonConstant.ZITOPAY_ENTITY_ORDER);
			
			if (issuedEntity == null) {
				return ResponseInfoEnum.参数组装失败.getMap("创建商户[" + personEntity.getPid() + "],代付订单失败");
			}

			IBaseIssuedService baseIssuedApiService = getIssuedService(gatewayId);
			if (null == baseIssuedApiService) {
				return ResponseInfoEnum.调用失败.getMap("未找到编号[" + gatewayId + "]的代付通道");
			}
			return baseIssuedApiService.issued(personEntity, gatewayEntity, zitopayPersonGeteway, issuedEntity, paramMap, serialNo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",代付异常,请检查!", e);
		}
	}
	
	/**
	 * 初始化代付数据
	 * 
	 * @param personApplicationEntity
	 * @param personEntity
	 * @param gatewayEntity
	 * @param paramMap
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> initIssued(PersonEntity personEntity, GatewayEntity gatewayEntity, Map<String, String> paramMap, String serialNo) throws ServiceException {
		try {
			logger.info("代付订单创建开始");
			String orderidinf = paramMap.get(IssuedParamConstant.param_merchantOrderId);
			Map<String, Object> map = new HashMap<String, Object>();
			int orderCount = issuedService.countOrderByOrderidinf(orderidinf);
			if (orderCount > 0) {
				logger.info("订单已存在,商户订单号是:{}", orderidinf);
				map.put(IssuedParamConstant.param_merchantOrderId, orderidinf);
				return ResponseInfoEnum.订单已存在.getMap(map);
			}
			IssuedEntity issuedEntity = issuedService.createIssuedOrder(personEntity, gatewayEntity, orderidinf, paramMap);
			logger.info("代付订单创建成功,订单信息:{}", issuedEntity);
			map.put(CommonConstant.ZITOPAY_ENTITY_ORDER, issuedEntity);
			return ResponseInfoEnum.调用成功.getMap(map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",初始化代付订单异常", e);
		}
	}

}