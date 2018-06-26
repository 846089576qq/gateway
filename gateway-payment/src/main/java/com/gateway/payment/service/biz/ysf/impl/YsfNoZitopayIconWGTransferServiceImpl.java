package com.gateway.payment.service.biz.ysf.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gateway.channel.entity.ysf.tansfer.YsfTranserInDto;
import com.gateway.channel.service.ysf.IYsfChannelService;
import com.gateway.common.annotaion.Gateway;
import com.gateway.common.constants.config.GatewayConstants;
import com.gateway.common.constants.param.TransferResultParamConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.gateway.payment.entity.TransferEntity;
import com.gateway.payment.service.IBaseService;
import com.gateway.payment.service.base.impl.BaseTransferServiceImpl;
import com.gateway.payment.service.biz.ysf.IYsfWGTransferService;
import com.zitopay.foundation.common.exception.ServiceException;

@Gateway(gatewayId = IBaseService.BEAN_PREFIX_TRANSFER + GatewayConstants.GATEWAY_YSF_NO_ICON_WG)
public class YsfNoZitopayIconWGTransferServiceImpl extends BaseTransferServiceImpl implements IYsfWGTransferService {

	@Reference
	private IYsfChannelService channelService;

	@Override
	public Map<String, Object> transfer(PersonEntity zitopayPerson, GatewayEntity zitopayGeteway, PersonGatewayEntity zitopayPersonGeteway, TransferEntity transferEntity, Map<String, String> paramMap, String serialNo) throws ServiceException {
		YsfTranserInDto in = new YsfTranserInDto();
		in.setMerCode(zitopayPersonGeteway.getGatewaypid());
		in.setMerBillNo(transferEntity.getThreeorderid());
		in.setCustomerCode(zitopayPerson.getPid());
		in.setMerAcctNo(zitopayPersonGeteway.getChannelid());
		in.setTransferAmount(transferEntity.getTotalprice().toString());
		in.setCollectionItemName("Alwaypay");
		logger.info("易收付转账请求：{}", in);
		Map<String, String> transferMap = channelService.transfer(in, zitopayPersonGeteway.getGatewaykey(), zitopayPersonGeteway.getExt1(), zitopayPersonGeteway.getExt2());
		logger.info("易收付转账响应：{}", transferMap);
		if(transferMap == null) {
			return ResponseInfoEnum.调用失败.getMap("跳转结果为空请联系管理员");
		}
		
		if(!"M000000".equals(transferMap.get("rspCode"))) {
			return ResponseInfoEnum.调用失败.getMap("上游返回状态为：" +  transferMap.get("rspCode"));
		}
		
		Integer state = 1;
		if(!"10".equals(transferMap.get("tradeState"))) {
			state = 2;
		}
		
		int count = transferService.updateOrder(transferEntity, new BigDecimal(transferMap.get("ipsFee")), transferMap.get("ipsBillNo"), transferMap.get("tradeId"), state);
		if(count != 1) {
			logger.info("更新转账记录失败 条数为：{}", count);
			return ResponseInfoEnum.调用失败.getMap("更新转账记录失败");
		}
		
		final String stateresult = state.toString();
		final String transferid = transferEntity.getTransferid();
		return ResponseInfoEnum.调用成功.getMap(new HashMap<String, Object>() {

			private static final long serialVersionUID = 1L;
			{
				put(TransferResultParamConstant.status, stateresult);
				put(TransferResultParamConstant.param_transferid, transferid);
			}
		});
	}
	
}
