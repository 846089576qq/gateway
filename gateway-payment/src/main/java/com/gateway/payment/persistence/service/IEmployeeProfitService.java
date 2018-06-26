package com.gateway.payment.persistence.service;

import java.math.BigDecimal;

/**
 * 分润计算服务
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月12日
 */
public interface IEmployeeProfitService {

	/**
	 * methodName:countAndSaveProfit description: 计算不同层级的代理利润并存入数据库 param:orderId return:Map Author:王洪滨 Date:2016/10/25 10:15
	 */
	void countAndSaveProfit(String getewayId, String personId, String orderId, BigDecimal totalPrice, BigDecimal rateMoney) throws Exception;

    /**
     * 计算手续费
     * 
     * @param totalPrice
     * @param min
     * @param max
     * @param fix
     * @param rate
     * @return
     */
    public BigDecimal cacuPoundage(BigDecimal totalPrice, BigDecimal min, BigDecimal max, BigDecimal fix, BigDecimal rate);

}
