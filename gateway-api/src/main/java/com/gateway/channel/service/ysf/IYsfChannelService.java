package com.gateway.channel.service.ysf;

import java.util.Map;

import com.gateway.channel.entity.ysf.pay.YsfPayInDto;
import com.gateway.channel.entity.ysf.query.YsfQueryInDto;
import com.gateway.channel.entity.ysf.tansfer.YsfTranserInDto;
import com.zitopay.foundation.common.exception.ServiceException;

public interface IYsfChannelService {
	
	/**
	 * 支付
	 * @param in
	 * @param key
	 * @return
	 */
	public String pay(YsfPayInDto in, String key) throws ServiceException;
	
	/**
	 * 通知
	 * @param params
	 * @return
	 */
	public Map<String, String> checkNotify(String xml, String merCode, String directStr, String ipsRsaPub);
	

	/**
	 * 查询接口
	 * @param ysfQueryInDto
	 * @param key
	 */
	public Map<String, String> query(YsfQueryInDto ysfQueryInDto, String key);
	
	/**
	 * 转账
	 * @param in
	 * @param key
	 * @return
	 */
	public Map<String, String> transfer(YsfTranserInDto in, String key, String deskey, String desiv) throws ServiceException;

	/**
	 * 代付
	 * @param params
	 * @param key
	 * @param directStr
	 * @param iv
	 * @return
	 */
	public Map<String, String> issued(Map<String, String> params, String key, String directStr, String iv);

	/**
	 * 代付查询接口
	 * @param params
	 * @param directStr
	 * @return
	 */
	public Map<String, String> issuedSearch(Map<String, String> params, String directStr);

	/**
	 * 获取商户订单号
	 * @param xml
	 * @return
	 */
	public String getMerBillNo(String xml);

	/**
	 * 代付退票查询
	 * @param params
	 * @param directStr
	 * @return
	 */
	public Map<String, String> issuedReturnSearch(Map<String, String> params, String directStr);

	/**
	 * 代付交易查询
	 * @param params
	 * @param directStr
	 * @return
	 */
	public Map<String, String> issuedTradeSearch(Map<String, String> params, String directStr);

}
