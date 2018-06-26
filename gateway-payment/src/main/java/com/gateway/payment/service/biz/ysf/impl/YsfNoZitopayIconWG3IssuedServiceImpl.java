package com.gateway.payment.service.biz.ysf.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gateway.channel.service.ysf.IYsfChannelService;
import com.gateway.common.annotaion.Gateway;
import com.gateway.common.constants.CommonConstant;
import com.gateway.common.constants.config.GatewayConstants;
import com.gateway.common.constants.param.IssuedParamConstant;
import com.gateway.common.constants.param.IssuedResultParamConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.IssuedEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.gateway.payment.service.IBaseService;
import com.gateway.payment.service.base.impl.BaseIssuedServiceImpl;
import com.gateway.payment.service.biz.ysf.IYsfWGIssuedService;
import com.zitopay.foundation.common.exception.ServiceException;

@Gateway(gatewayId = IBaseService.BEAN_PREFIX_ISSUED + GatewayConstants.GATEWAY_YSF_NO_ICON_WG_3)
public class YsfNoZitopayIconWG3IssuedServiceImpl extends BaseIssuedServiceImpl implements IYsfWGIssuedService {

	@Reference
	private IYsfChannelService channelService;

	@Override
	public Map<String, Object> issued(PersonEntity zitopayPerson, GatewayEntity zitopayGeteway, PersonGatewayEntity zitopayPersonGeteway, IssuedEntity issuedEntity, Map<String, String> paramMap, String serialNo) throws ServiceException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("MerBillno", issuedEntity.getThreeorderid());
		params.put("AccountName", paramMap.get(IssuedParamConstant.param_custRealName));
		params.put("AccountNumber", paramMap.get(IssuedParamConstant.param_custCardNo));
		params.put("BankName", paramMap.get(IssuedParamConstant.param_bankName));
		params.put("BranchBankName", paramMap.get(IssuedParamConstant.param_branchBankName));
		params.put("BankCity", paramMap.get(IssuedParamConstant.param_bankCity));
		params.put("BankProvince", paramMap.get(IssuedParamConstant.param_bankProvince));
		params.put("BillAmount", paramMap.get(IssuedParamConstant.param_orderAmount));
		params.put("IdCard", paramMap.get(IssuedParamConstant.param_custIdentityCode));
		params.put("MobilePhone", paramMap.get(IssuedParamConstant.param_custCardMobile));
		params.put("MerCode", zitopayPersonGeteway.getGatewaypid());
		params.put("MerName", zitopayPersonGeteway.getGatewayname());
		params.put("Account", zitopayPersonGeteway.getChannelid());
		logger.info("易收付代付请求：{}", params);
		Map<String, String> issuedMap = channelService.issued(params, zitopayPersonGeteway.getGatewaykey(), zitopayPersonGeteway.getExt1(), zitopayPersonGeteway.getExt2());
		logger.info("易收付代付响应：{}", issuedMap);
		if(issuedMap == null) {
			return ResponseInfoEnum.调用失败.getMap("跳转结果为空请联系管理员");
		}
		
		/*if(!"000000".equals(issuedMap.get("RspCode"))) {
			return ResponseInfoEnum.调用失败.getMap("上游返回请求状态为：" +  issuedMap.get("RspCode") + ",内容：" + issuedMap.get("RspMsg"));
		}*/
		
		Integer state = 2;
		String message = "代付申请成功";
		if(!"000000".equals(issuedMap.get("RspCode")) || "9".equals(issuedMap.get("BatchStatus"))) {
			state = 3;
			if(!"000000".equals(issuedMap.get("RspCode")) && StringUtils.isNotBlank(issuedMap.get("RspMsg"))) {
				message += issuedMap.get("RspMsg") + " ";
			}
			if(StringUtils.isNotBlank(issuedMap.get("BatchErrorMsg"))) {
				message += issuedMap.get("BatchErrorMsg") + " ";
			}
			if(StringUtils.isNotBlank(issuedMap.get("ErrorMsg"))) {
				message += issuedMap.get("ErrorMsg");
			}
			issuedEntity.setRemark(message);
		}
		issuedEntity.setState(state);
		issuedEntity.setToppayno(issuedMap.get("BatchBillno"));
		final String issuedid = issuedEntity.getIssuedid();
		int count = issuedService.updateOrder(issuedEntity);
		if(count != 1) {
			logger.info(issuedid + "更新代付状态失败  条数为：{}", count);
			return ResponseInfoEnum.调用失败.getMap("更新代付状态失败");
		}
		final String stateresult = state.toString();
		return ResponseInfoEnum.调用成功.getMap(message, new HashMap<String, Object>() {

			private static final long serialVersionUID = 1L;
			{
				put(IssuedResultParamConstant.status, stateresult);
				put(IssuedResultParamConstant.param_issuedid, issuedid);
			}
		});
	}

	@Override
	public Map<String, Object> queryOrder(IssuedEntity issuedEntity, PersonGatewayEntity personGatewayEntity, String serialNo) throws ServiceException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("merCode", personGatewayEntity.getGatewaypid());
		params.put("batchNo", issuedEntity.getToppayno());
		params.put("accCode", personGatewayEntity.getChannelid());
		params.put("MerBillNo", issuedEntity.getThreeorderid());
		
		Map<String, String> issuedMap = null;
		String state = issuedEntity.getState().toString();
		String threepoundage = null;
		
		// 代付退票查询
		if("1".equals(state)) {
			
			params.put("date", new SimpleDateFormat("yyyyMMdd").format(issuedEntity.getCreatetime()));
			logger.info("易收付代付退票查询请求：{}", params);
			issuedMap = channelService.issuedReturnSearch(params, personGatewayEntity.getGatewaykey());
			logger.info("易收付代付退票查询响应：{}", issuedMap);
			
			if(issuedMap == null) {
				return ResponseInfoEnum.调用失败.getMap("跳转结果为空请联系管理员");
			}
			
			if("4".equals(issuedMap.get("TrdStatus"))) {
				state = "4";
				threepoundage = (new BigDecimal(issuedMap.get("TrdOutCash")).subtract(new BigDecimal(issuedMap.get("TrdInCash")))).setScale(2, RoundingMode.HALF_UP).toString();
			}
			
		}else {
		// 代付订单查询	
			logger.info("易收付代付查询请求：{}", params);
			issuedMap = channelService.issuedSearch(params, personGatewayEntity.getGatewaykey());
			logger.info("易收付代付查询响应：{}", issuedMap);
			
			if(issuedMap == null) {
				return ResponseInfoEnum.调用失败.getMap("跳转结果为空请联系管理员");
			}
			
			String ordState = issuedMap.get("OrdStatus");
			if("000000".equals(issuedMap.get("RspCode")) && "10".equals(ordState)) {
				if("0".equals(issuedMap.get("TrdStatus"))) {
					state = "1";
				}else {
					state = "4";
				}
				threepoundage = (new BigDecimal(issuedMap.get("TrdOutCash")).subtract(new BigDecimal(issuedMap.get("TrdInCash")))).setScale(2, RoundingMode.HALF_UP).toString();
			}else if("9".equals(ordState)) {
				state = "3";
				issuedEntity.setRemark(issuedMap.get("ErrorMsg"));
				issuedService.updateOrder(issuedEntity);
			}
			
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(CommonConstant.ISSUED_STATUS, state);
		resultMap.put(CommonConstant.SUCCESSTIME, issuedMap.get("TrdDoTime"));
		resultMap.put(CommonConstant.TRADENO, issuedMap.get("IpsBillno"));
		resultMap.put(CommonConstant.TOPTRADENO, issuedMap.get("BatchNo"));
		resultMap.put(CommonConstant.POUNDAGE, threepoundage);
		return ResponseInfoEnum.调用成功.getMap(resultMap);
	}
	
}
