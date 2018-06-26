package com.gateway.payment.service.adapter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.gateway.common.constants.CommonConstant;
import com.gateway.common.constants.param.TransferParamConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.gateway.payment.entity.TransferEntity;
import com.gateway.payment.service.base.IBaseTransferService;
import com.zitopay.foundation.common.exception.ServiceException;
import com.zitopay.foundation.common.util.StringUtils;

/**
 * 融智付订单转账服务实现
 * 
 * 作者：王政 创建时间：2017年3月6日 下午5:59:52
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class TransferServiceImplAdapter extends BaseServiceImplAdapter implements ITransferServiceAdapter {

	@Override
	public Map<String, Object> checkTransferParameter(Map<String, String> paramMap, String serialNo) throws ServiceException {
		logger.info("开始验证转账参数,内容:{}.", paramMap);

		if (paramMap == null || paramMap.isEmpty()) {
			return ResponseInfoEnum.参数为空.getMap();
		}

		String merchantId = paramMap.get(TransferParamConstant.param_merchantId);// 商户id，由融智付分配
		if (StringUtils.isEmpty(merchantId)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + TransferParamConstant.param_merchantId + "]不能为空");
		}

		Integer gatewayId = Integer.valueOf(paramMap.get(TransferParamConstant.param_gatewayId)); // 通道ID，由融智付分配
		if (null == gatewayId) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + TransferParamConstant.param_gatewayId + "]不能为空");
		}

		String merchantTransferOrderId = paramMap.get(TransferParamConstant.param_merchantTransferOrderId); // 商户转账订单号
		if (StringUtils.isEmpty(merchantTransferOrderId)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + TransferParamConstant.param_merchantTransferOrderId + "]不能为空");
		}
		
		String totalPrice = paramMap.get(TransferParamConstant.param_orderAmount);// 商品总价格，以元为单位，小数点后2位
		if (StringUtils.isEmpty(totalPrice)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + TransferParamConstant.param_orderAmount + "]不能为空");
		}
		Pattern p = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))\\.(\\d){2}?$");
		Matcher m = p.matcher(totalPrice);
		if (!m.matches()) {
			return ResponseInfoEnum.参数有误.getMap("参数[" + TransferParamConstant.param_orderAmount + "]格式不正确(0.00)");
		}
		
		String ordertitle = paramMap.get(TransferParamConstant.param_orderTitle);// 订单标题
		if (StringUtils.isEmpty(ordertitle)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + TransferParamConstant.param_orderTitle + "]不能为空");
		}

		return ResponseInfoEnum.调用成功.getMap();
	}

	@Override
	public Map<String, Object> checkTransferPermission(String merchantId, String gatewayId, String serialNo) throws ServiceException {
		return checkPermission(merchantId, null, gatewayId, serialNo);
	}

	@Override
	public Map<String, Object> execTransfer(PersonEntity personEntity, GatewayEntity gatewayEntity, PersonGatewayEntity zitopayPersonGeteway, Map<String, String> paramMap, String serialNo) throws ServiceException {
		try {
			String gatewayId = String.valueOf(gatewayEntity.getId());
			Map<String, Object> buildOrderMap = initTransfer(personEntity, gatewayEntity, paramMap, serialNo);
			if (!ResponseInfoEnum.调用成功.equals(buildOrderMap)) {
				return buildOrderMap;
			}
			TransferEntity transferEntity = (TransferEntity) buildOrderMap.get(CommonConstant.ZITOPAY_ENTITY_ORDER);
			
			if (transferEntity == null) {
				return ResponseInfoEnum.参数组装失败.getMap("创建商户[" + personEntity.getPid() + "],转账订单失败");
			}

			IBaseTransferService baseTransferApiService = getTransferService(gatewayId);
			if (null == baseTransferApiService) {
				return ResponseInfoEnum.调用失败.getMap("未找到编号[" + gatewayId + "]的转账通道");
			}
			return baseTransferApiService.transfer(personEntity, gatewayEntity, zitopayPersonGeteway, transferEntity, paramMap, serialNo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",转账异常,请检查!", e);
		}
	}
	
	/**
	 * 初始化转账数据
	 * 
	 * @param personApplicationEntity
	 * @param personEntity
	 * @param gatewayEntity
	 * @param paramMap
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> initTransfer(PersonEntity personEntity, GatewayEntity gatewayEntity, Map<String, String> paramMap, String serialNo) throws ServiceException {
		try {
			logger.info("转账订单创建开始");
			String orderidinf = paramMap.get(TransferParamConstant.param_merchantTransferOrderId);
			Map<String, Object> map = new HashMap<String, Object>();
			int orderCount = transferService.countOrderByOrderidinf(orderidinf);
			if (orderCount > 0) {
				logger.info("订单已存在,商户订单号是:{}", orderidinf);
				map.put(TransferParamConstant.param_merchantTransferOrderId, orderidinf);
				return ResponseInfoEnum.订单已存在.getMap(map);
			}
			TransferEntity transferEntity = transferService.createTransferOrder(personEntity, gatewayEntity, orderidinf, paramMap);
			logger.info("转账订单创建成功,订单信息:{}", transferEntity);
			map.put(CommonConstant.ZITOPAY_ENTITY_ORDER, transferEntity);
			return ResponseInfoEnum.调用成功.getMap(map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",初始化转账订单异常", e);
		}
	}

}