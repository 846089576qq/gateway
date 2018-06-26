package com.gateway.payment.persistence.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gateway.payment.entity.EmployeeGatewayEntity;
import com.gateway.payment.entity.EmployeeProfitEntity;
import com.gateway.payment.entity.PersonChannelEntity;
import com.gateway.payment.persistence.mapper.IEmployeeProfitMapper;
import com.gateway.payment.persistence.service.IEmployeeProfitService;

/**
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月12日
 */
@Service
public class EmployeeProfitServiceImpl implements IEmployeeProfitService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeProfitServiceImpl.class);

	@Autowired
	private IEmployeeProfitMapper employeeProfitMapper;

	@Override
	public void countAndSaveProfit(String getewayId, String personId, String orderId, BigDecimal totalPrice, BigDecimal rateMoney) throws Exception {
		logger.info("分润开始,[订单号:{}],[商户号:{}],[通道号:{}],[订单金额:{}].", orderId, personId, getewayId, totalPrice);
		List<EmployeeGatewayEntity> employeeGetewayDtos;
		// 判断订单id是否存在，存在则表示已经进行过分润处理，不重复进行 (add by xiaoshiwen 2017-01-16)
		int profitCount = employeeProfitMapper.existProfitByOrderId(orderId);
		if (profitCount > 0) {// 订单已经进行过分润，不再次进行分润
			logger.info("订单{}已经进行过分润处理，退出方法!", orderId);
			return;
		}
		/** 根据商户id获取商户通道信息。以及所属代理id */
		PersonChannelEntity personGetewayDto = employeeProfitMapper.queryPersonChannelInfo(personId, getewayId);
		logger.info("手续费总收益total:" + rateMoney);
		/** 获取订单所属商户的所有上层代理信息：代理id、成本、费率、折扣比例、 */
		if (personGetewayDto.getEmployeeId().trim().length() > 0) {// 代理人id存在
			employeeGetewayDtos = employeeProfitMapper.queryEmployeeChannelList(personGetewayDto.getEmployeeId(), getewayId, personGetewayDto.getClearform());
			if (employeeGetewayDtos != null) {
				/** 计算各级代理总收益、净收益 */
				/**
				 * 规则： 1.上一级的折扣比例必须大于等于下一级；大于0小于1，百分比显示
				 *  	2.上一级的分润费率必须小于等于下一级； 费率为千分之几，小数点后4位，大于0小于1，千分比显示；
				 * 		3.每产生1次成功的交易，需要实时运算各级费率并存储（存储当时分润费率/折扣比例及分润金额）； 
				 * 		4.分润原则都是上级分给下级。
				 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 计算公式：
				 * A0总收益=（MR-A0P）*10000*A0D,即（5‰-1‰）*10000*90%=36 ，注明：代表通道商华势收入50元，分给了顶级代理36元，通道商华势净收益14元
				 * A1总收益=（MR-A1P）*10000*A1D,即（5‰-2‰）*10000*90%=27 
				 * A2总收益=（MR-A2P）*10000*A2D,即（5‰-3‰）*10000*80%=16
				 * A3总收益=（MR-A3P）*10000*A3D,即（5‰-4‰）*10000*50%=5
				 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
				 * A0净收益=A0总收益-A1总收益=36-27=9元 
				 * A1净收益=A1总收益-A2总收益=27-16=11元 
				 * A2净收益=A2总收益-A3总收益=16-5=11元 
				 * A3净收益=A3总收益-下面已无代理=5-0=5元
				 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
				 * 各级代理收单净收益=36元=顶级代理净收益+1级代理净收益+2级代理净收益+3级代理净收益=9+11+11+5
				 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MR：商户通道费率
				 */
				BigDecimal profitTotal = new BigDecimal(0);// 各种代理净收益之和
				BigDecimal topProfit = BigDecimal.ZERO;
				for (int index = 0; index < employeeGetewayDtos.size(); index++) {
					EmployeeGatewayEntity employeeGetewayDto = employeeGetewayDtos.get(index);
					BigDecimal min = employeeGetewayDto.getMin(); // 下限
					BigDecimal max = employeeGetewayDto.getMax(); // 上限
					BigDecimal singleSum = employeeGetewayDto.getFix(); // 单笔
					BigDecimal discount = employeeGetewayDto.getDiscountRate(); // 折扣
					BigDecimal channelCostRate = employeeGetewayDto.getChannelCost(); // 运营成本费率
					BigDecimal totalProfit = totalProfit(rateMoney, totalPrice, min, max, singleSum, discount, channelCostRate);
					
					if (index == 0) {
						topProfit = totalProfit;
					}
					logger.debug("第===" + (index + 1) + "====级计算总收益totalProfit:" + totalProfit);
					BigDecimal nextTotalPrice = new BigDecimal(0);// 下一级代理总收益
					if (index + 1 < employeeGetewayDtos.size()) {// 不是list的最后一个
						EmployeeGatewayEntity nextEmployeeGetewayDto = employeeGetewayDtos.get(index + 1);
						BigDecimal nextMin = nextEmployeeGetewayDto.getMin(); // 下限
						BigDecimal nextMax = nextEmployeeGetewayDto.getMax(); // 上限
						BigDecimal nextSingleSum = nextEmployeeGetewayDto.getFix(); // 单笔
						BigDecimal nextDiscount = nextEmployeeGetewayDto.getDiscountRate(); // 折扣
						BigDecimal nextChannelCostRate = nextEmployeeGetewayDto.getChannelCost(); // 运营成本费率
						nextTotalPrice = totalProfit(rateMoney, totalPrice, nextMin, nextMax, nextSingleSum, nextDiscount, nextChannelCostRate);
					}
					logger.debug("下一级代理总收益nextTotalPrice=" + nextTotalPrice);
					/** 计算净收益 */
					BigDecimal netProceeds = totalProfit.subtract(nextTotalPrice);
					logger.debug("第===" + (index + 1) + "====级计算netProceeds" + nextTotalPrice);
					profitTotal = profitTotal.add(netProceeds);
					// 存储计算结果
					EmployeeProfitEntity profit = new EmployeeProfitEntity();
					SimpleDateFormat dtFormatdB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					// profit.setCreateTime(DateTimeUtil.getFormatDateTime(new
					// Date()));
					profit.setCreateTime(dtFormatdB.format(new Date()));
					profit.setEmployeeId(employeeGetewayDto.getEmployeeId().toString());
					profit.setOrderId(orderId);
					profit.setDiscountRate(discount);
					profit.setNetProceeds(netProceeds);
					profit.setProfitRate(channelCostRate);
					profit.setTotalRevenue(totalProfit);
					profit.setFix(singleSum);
					int result = employeeProfitMapper.insertSelective(profit);
					logger.debug("第===" + index + "====保存结果:" + result);
				}
				logger.info("顶级代理分润 :{},各级代理分润总和:{},计算结果:{}.", topProfit, profitTotal, topProfit.equals(profitTotal));
			}
		}
		logger.info("分润结束,[订单号:{}],[商户号:{}],[通道号:{}],[订单金额:{}].", orderId, personId, getewayId, totalPrice);
	}
	
	/**
	 * 计算总收益
	 * 
	 * @param poundage 商户手续费
	 * @param totalPrice 交易金额
	 * @param min 代理手续费下限
	 * @param max 代理手续费上限
	 * @param singleSum 代理单笔手续费
	 * @param discount 代理折扣
	 * @param channelCostRate 通道成本费率
	 * @return
	 */
	private BigDecimal totalProfit(BigDecimal poundage, BigDecimal totalPrice, BigDecimal min, BigDecimal max, BigDecimal singleSum, BigDecimal discount, BigDecimal channelCostRate) {
		BigDecimal channelMoney = cacuPoundage(totalPrice, min, max, singleSum, channelCostRate);
		discount = discount!= null ? discount : new BigDecimal(0);
		
		BigDecimal totalProfit = (poundage.subtract(channelMoney)).multiply(discount); // 总收益 = （手续费 - 成本） * 折扣 
		return totalProfit;
	}
	
	public BigDecimal cacuPoundage(BigDecimal totalPrice, BigDecimal min, BigDecimal max, BigDecimal fix, BigDecimal rate) {
		min = min!= null ? min : new BigDecimal(0);
		max = max!= null ? max : new BigDecimal(0);
		fix = fix!= null ? fix : new BigDecimal(0);
		rate = rate!= null ? rate : new BigDecimal(0);
		
		BigDecimal poundage = rate.multiply(totalPrice).add(fix);
		
		if(poundage.doubleValue() < min.doubleValue()) { // 手续费小于下限取下限
			poundage = min;
		}else if(poundage.doubleValue() > max.doubleValue()) { // 手续费大于上限取上限
			poundage = max;
		}
		return poundage.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
}
