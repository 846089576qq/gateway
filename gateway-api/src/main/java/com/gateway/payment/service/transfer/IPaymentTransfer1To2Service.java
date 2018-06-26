package com.gateway.payment.service.transfer;

import java.util.Map;

import com.gateway.payment.entity.OrderEntity;

public interface IPaymentTransfer1To2Service {
	
	/**
	 * 去2.0版本发起支付
	 * @param publicKey
	 * @param privateKey
	 * @param params
	 * @return
	 */
	public Map<String, Object> toV2Pay(String publicKey, String privateKey, Map<String, String> params);
	
	/**
	 * 去2.0版本发起转账
	 * @param publicKey
	 * @param privateKey
	 * @param params
	 * @return
	 */
	public Map<String, Object> toV2Transfer(String publicKey, String privateKey, Map<String, String> params);
	
	/**
	 * 去2.0版本发起代付
	 * @param publicKey
	 * @param privateKey
	 * @param params
	 * @return
	 */
	public Map<String, Object> toV2Issued(String publicKey, String privateKey, Map<String, String> params);
	
	/**
	 * 去2.0版本发起订单查询
	 * @param publicKey
	 * @param privateKey
	 * @param params
	 * @return
	 */
	public Map<String, Object> toV2Query(String publicKey, String privateKey, Map<String, String> params);

	/**
	 * 去2.0版本发起退款
	 * @param publicKey
	 * @param privateKey
	 * @param params
	 * @return
	 */
	public Map<String, Object> toV2Refund(String publicKey, String privateKey, Map<String, String> params);
	
	/**
	 * 去2.0版本发起商户注册流程
	 * @param publicKey
	 * @param privateKey
	 * @param params
	 * @return
	 */
	public Map<String, Object> toV2Register(String publicKey, String privateKey, Map<String, String> params);
	
	/**
	 * 去2.0版本发起商户注册更新
	 * @param publicKey
	 * @param privateKey
	 * @param params
	 * @return
	 */
	public Map<String, Object> toV2RegisterUpdate(String publicKey, String privateKey, Map<String, String> params);
	
	/**
	 * 去2.0版本发起绑卡
	 * @param publicKey
	 * @param privateKey
	 * @param params
	 * @return
	 */
	public Map<String, Object> toV2BindCard(String publicKey, String privateKey, Map<String, String> params);
	
	/**
	 * 去2.0版本发起解绑
	 * @param publicKey
	 * @param privateKey
	 * @param params
	 * @return
	 */
	public Map<String, Object> toV2UnBindCard(String publicKey, String privateKey, Map<String, String> params);
	
	/**
	 * 去2.0版本查询绑定卡
	 * @param publicKey
	 * @param privateKey
	 * @param params
	 * @return
	 */
	public Map<String, Object> toV2FindBindCard(String publicKey, String privateKey, Map<String, String> params);

	/**
	 * 支付1.0参数转换成2.0参数
	 * @param params
	 * @return
	 */
	public Map<String, String> transferPayParamsV1ToV2(Map<String, String> params);

	/**
	 * 查询1.0参数转换成2.0参数
	 * @param params
	 * @return
	 */
	public Map<String, String> transferQueryParamsV1ToV2(Map<String, String> params);

	/**
	 * 退款1.0参数转换成2.0参数
	 * @param params
	 * @return
	 */
	public Map<String, String> transferRefundParamsV1ToV2(Map<String, String> params);
	
	/**
	 * 封装回调参数
	 * @param order
	 * @return
	 */
	public String noticeParams(OrderEntity order);

	/**
	 * 转账1.0参数转换成2.0参数
	 * @param params
	 * @return
	 */
	public Map<String, String> transferTransferParamsV1ToV2(Map<String, String> params);

	/**
	 * 代付1.0参数转换成2.0参数
	 * @param params
	 * @return
	 */
	public Map<String, String> transferIssuedParamsV1ToV2(Map<String, String> params);

}
