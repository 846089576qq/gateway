package com.gateway.common.constants;

/**
 * 通用常量类 作者：王政 创建时间：2017年3月3日 上午11:25:59
 */
public class CommonConstant {

	/**
	 * 实体标识（employee 代理）
	 */
	public final static String ZITOPAY_ENTITY_EMPLOYEE = "employee";
	
	/**
	 * 实体标识（person 商户）
	 */
	public final static String ZITOPAY_ENTITY_PERSON = "person";
	
	/**
	 * 实体标识（employeeGateway 代理通道配置）
	 */
	public final static String ZITOPAY_ENTITY_EMPLOYEE_GATEWAY = "employeeGateway";

	/**
	 * 实体标识（personAuthenticationStream 商户认证）
	 */
	public final static String ZITOPAY_ENTITY_PERSSON_AUTHENTICATION_STREAM = "personAuthenticationStream";

	/**
	 * 实体标识（gateway 通道）
	 */
	public final static String ZITOPAY_ENTITY_GATEWAY = "gateway";

	/**
	 * 实体标识（personApplication 商户应用）
	 */
	public final static String ZITOPAY_ENTITY_PERSON_APPLICATION = "personApplication";

	/**
	 * 实体标识（personGateway 商户通道）
	 */
	public final static String ZITOPAY_ENTITY_PERSON_GATEWAY = "personGateway";

	/**
	 * 实体标识（order 订单）
	 */
	public final static String ZITOPAY_ENTITY_ORDER = "order";

	/**
	 * channelList 通道列表
	 */
	public final static String ZITOPAY_CHANNEL_LIST = "channelList";

	/**
	 * 通道查询标识（退款描述）
	 */
	public static final String REFUNDDESC = "refundDesc";

	/**
	 * 通道查询标识（交易状态）
	 */
	public static final String TRADESTATUS = "tradeStatus";

	/**
	 * 通道查询标识（交易流水号）
	 */
	public static final String TRADENO = "tradeNo";

	/**
	 * 通道查询标识（顶级交易流水号）
	 */
	public static final String TOPTRADENO = "toptradeNo";

	/**
	 * 通道查询标识（查询返回结果）
	 */
	public final static String RESULT = "result";

	/**
	 * 请求流内容
	 */
	public final static String REQUEST_CONTENT = "requestContent";

	/**
	 * 订单存redis对应的key
	 */
	public static final String ZITOPAYORDER_REDIS_KEY = "zitopayOrder%s";
	
	/**
	 * mq key
	 */
	public static final String ZITOPAYORDER_MQ_KEY = "orderQueueKey";

	/**
	 * 收银台二维码支付轮询返回信息
	 */
	public static final String CASHIER_POLLING_MSG = "msgInfo";

	/**
	 * 收银台返回智付订单表数据
	 */
	public static final String CASHIER_ZITOPA_ORDER = "zitopayOrder";

	/**
	 * 收银台返回商家返回地址
	 */
	public static final String CASHIER_RETURNURL = "returnUrl";

	/**
	 * 收银台处理结果回传
	 */
	public static final String CASHIER_CALLBACK = "callback";

	/**
	 * 代付状态（0 : 初始化, 1 : 代付成功, 2 : 审核中, 3 : 代付失败 , 4 : 退票  ）
	 */
	public final static String ISSUED_STATUS = "issuedStatus";

	/**
	 * 代付金额
	 */
	public final static String ISSUED_AMOUNT = "dfAmount";
	
	/**
	 * 交易成功时间
	 */
	public final static String SUCCESSTIME = "successTime";
	
	/**
	 * 手续费
	 */
	public final static String POUNDAGE = "poundage";

}
