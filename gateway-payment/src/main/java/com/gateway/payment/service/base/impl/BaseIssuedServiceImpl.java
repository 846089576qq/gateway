package com.gateway.payment.service.base.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.transaction.annotation.Transactional;

import com.gateway.common.constants.CommonConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.common.utils.DateUtil;
import com.gateway.common.utils.StringUtils;
import com.gateway.payment.entity.IssuedEntity;
import com.gateway.payment.service.base.IBaseIssuedService;
import com.zitopay.foundation.common.exception.ServiceException;
import com.zitopay.foundation.common.util.BeanUtils;

public abstract class BaseIssuedServiceImpl extends BaseServiceImpl implements IBaseIssuedService {

	@Override
	public Map<String, Object> checkParam(Map<String, String> paramMap) throws ServiceException {
		return ResponseInfoEnum.调用成功.getMap();
	}
	
	@Transactional
	@Override
	public Map<String, Object> afterIssued(String merchantId, String merchantPublicKey, String threeorderid, String successTime, String payno, String toppayno, String threepoundage, String state, String serialNo) throws ServiceException {
		logger.info("代付后操作-开始,代付状态:{}", state);
		try {
			IssuedEntity issuedEntity = issuedService.findByhreerderid(threeorderid);
			logger.info("支付回调处理-orderEntity{}", BeanUtils.bean2JSON(issuedEntity));
			if (null == issuedEntity) {
				String msg = "未找到对应订单,订单号[" + threeorderid + "]不存在.";
				logger.warn(msg);
				return ResponseInfoEnum.订单不存在.getMap(msg);
			}
			if (!StringUtils.isNull(payno)) {
				issuedEntity.setPayno(payno);// 支付流水号
			}
			if (!StringUtils.isNull(toppayno)) {
				issuedEntity.setToppayno(toppayno);// 支付流水号
			}
			if ("1".equals(state) || "4".equals(state)) {
				if(!StringUtils.isNull(successTime)) {
					issuedEntity.setSuccesstime(DateUtil.parseDate(successTime, "yyyyMMddHHmmss"));// 交易结束时间
				}
				if(!StringUtils.isNull(threepoundage)) {
					issuedEntity.setThreepoundage(new BigDecimal(threepoundage));
				}
			}
			issuedEntity.setState(Integer.parseInt(state));
			issuedService.updateOrder(issuedEntity); // 更新订单信息
			logger.info("代付后操作-成功.");
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put(CommonConstant.ISSUED_STATUS, state);
			resultMap.put(CommonConstant.SUCCESSTIME, issuedEntity.getSuccesstime() != null ? DateFormatUtils.format(issuedEntity.getSuccesstime(), "yyyyMMddHHmmss") : null);
			resultMap.put(CommonConstant.TRADENO, issuedEntity.getPayno());
			resultMap.put(CommonConstant.TOPTRADENO, issuedEntity.getToppayno());
			resultMap.put(CommonConstant.POUNDAGE, issuedEntity.getThreepoundage());
			return ResponseInfoEnum.调用成功.getMap(resultMap);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",代付后-处理异常", e);
		}

	}
		
}
