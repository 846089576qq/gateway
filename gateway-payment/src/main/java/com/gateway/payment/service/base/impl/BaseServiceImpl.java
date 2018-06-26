package com.gateway.payment.service.base.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import com.gateway.common.constants.CommonConstant;
import com.gateway.common.constants.param.BaseParamConstant;
import com.gateway.common.constants.param.PaymentAgreementParamConstant;
import com.gateway.common.constants.param.PaymentParamConstant;
import com.gateway.common.constants.status.DisabledStatusConstant;
import com.gateway.common.context.BeanContextUtil;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.common.properties.EnvProperties;
import com.gateway.common.properties.ExtensionProperties;
import com.gateway.common.utils.DateUtil;
import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.IssuedEntity;
import com.gateway.payment.entity.OrderEntity;
import com.gateway.payment.entity.PersonApplicationEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.gateway.payment.persistence.service.IAccountSystemCashService;
import com.gateway.payment.persistence.service.IBgreturnService;
import com.gateway.payment.persistence.service.IEmployeeGatewayService;
import com.gateway.payment.persistence.service.IEmployeeProfitService;
import com.gateway.payment.persistence.service.IEmployeeService;
import com.gateway.payment.persistence.service.IGatewayService;
import com.gateway.payment.persistence.service.IIssuedService;
import com.gateway.payment.persistence.service.IOrderRefundService;
import com.gateway.payment.persistence.service.IOrderService;
import com.gateway.payment.persistence.service.IPaymentAgreementService;
import com.gateway.payment.persistence.service.IPersonAppGatewayService;
import com.gateway.payment.persistence.service.IPersonApplicationService;
import com.gateway.payment.persistence.service.IPersonAuthenticationBaseService;
import com.gateway.payment.persistence.service.IPersonAuthenticationStreamService;
import com.gateway.payment.persistence.service.IPersonGatewayService;
import com.gateway.payment.persistence.service.IPersonService;
import com.gateway.payment.persistence.service.ITradeMonitorService;
import com.gateway.payment.persistence.service.ITradeSecretConfigService;
import com.gateway.payment.persistence.service.ITransferService;
import com.gateway.payment.service.IBaseService;
import com.gateway.payment.service.base.IBaseIssuedService;
import com.gateway.payment.service.base.IBasePaymentService;
import com.gateway.payment.service.general.ISecretService;
import com.zitopay.foundation.common.exception.ServiceException;
import com.zitopay.foundation.common.util.BeanUtils;
import com.zitopay.foundation.common.util.StringUtils;

public class BaseServiceImpl implements IBaseService {

	protected static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

	@Autowired
	protected IPersonService personService;

	@Autowired
	protected IEmployeeService employeeService;

	@Autowired
	protected IEmployeeGatewayService employeeGatewayService;

	@Autowired
	protected IPersonApplicationService personApplicationService;

	@Autowired
	protected IPersonAppGatewayService personAppGatewayService;

	@Autowired
	protected IPersonAuthenticationStreamService personAuthenticationStreamService;

	@Autowired
	protected IPersonAuthenticationBaseService personAuthenticationBaseService;

	@Autowired
	protected IGatewayService gatewayService;

	@Autowired
	protected IPersonGatewayService personGatewayService;

	@Autowired
	protected IOrderService orderService;

	@Autowired
	protected IOrderRefundService orderRefundService;

	@Autowired
	protected IPaymentAgreementService paymentAgreementService;

	@Autowired
	protected IAccountSystemCashService accountSystemCashService;
	
	@Autowired
	protected IIssuedService issuedService;
	
	@Autowired
	protected ITransferService transferService;

	@Autowired
	protected ITradeMonitorService tradeMonitorService;

	@Autowired
	protected IBgreturnService bgreturnService;

	@Autowired
	protected ITradeSecretConfigService tradeSecretConfigService;

	@Autowired
	protected IEmployeeProfitService iEmployeeProfitService;

	/*@Autowired
	protected OrderProducerService orderProducerService;*/

	@Autowired
	protected ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	protected RestTemplate restTemplate;

	@Autowired
	protected EnvProperties envProperties;

	@Autowired
	protected ISecretService secretService;

	@Autowired
	private ExtensionProperties extensionProperties;

	@Override
	public String getMerchantPublicKey(String merchantId, String serialNo) throws ServiceException {
		PersonEntity zitopayPerson = personService.getZitopayPersonByPid(merchantId);
		if (zitopayPerson == null) {
			throw new ServiceException(new NullPointerException("商户公钥获取失败."));
		}
		return zitopayPerson.getRsapublickey();
	}

	@Override
	public Map<String, Object> checkParameter(Map<String, String> paramMap, String serialNo) throws ServiceException {
		logger.info("开始验证参数,内容:{}.", paramMap);
		if (paramMap == null || paramMap.isEmpty()) {
			return ResponseInfoEnum.参数为空.getMap();
		}

		String merchantId = paramMap.get(BaseParamConstant.param_merchantId);// 商户id，由融智付分配
		if (StringUtils.isEmpty(merchantId)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + BaseParamConstant.param_merchantId + "]不能为空");
		}

		String applicationId = paramMap.get(BaseParamConstant.param_appId);// 商户的应用id，由融智付分配
		if (StringUtils.isEmpty(applicationId)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + BaseParamConstant.param_appId + "]不能为空");
		}
		return ResponseInfoEnum.调用成功.getMap();
	}

	/**
	 * 非收银台/收银台公用方法
	 * 
	 * @param paramMap
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> checkPaymentParam(Map<String, String> paramMap, String serialNo) throws ServiceException {
		Map<String, Object> map = checkParameter(paramMap, serialNo);
		if (!ResponseInfoEnum.调用成功.equals(map)) {
			return map;
		}
		String ordertitle = paramMap.get(PaymentParamConstant.param_orderTitle);// 订单标题
		if (StringUtils.isEmpty(ordertitle)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + PaymentParamConstant.param_orderTitle + "]不能为空");
		}
		String posttime = paramMap.get(PaymentParamConstant.param_orderTime);// 请求时间，格式为：yyyyMMddhhmmssSSS
		if (StringUtils.isEmpty(posttime)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + PaymentParamConstant.param_orderTime + "]不能为空");
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			sdf.parse(posttime);
			if (posttime.length() != 17) {
				return ResponseInfoEnum.参数有误.getMap("参数[" + PaymentParamConstant.param_orderTime + "]格式不正确(yyyyMMddHHmmssSSS)");
			}
		} catch (Exception e) {
			return ResponseInfoEnum.参数有误.getMap("参数[" + PaymentParamConstant.param_orderTime + "]格式不正确(yyyyMMddHHmmssSSS)");
		}

		String totalPrice = paramMap.get(PaymentParamConstant.param_orderAmount);// 商品总价格，以元为单位，小数点后2位
		if (StringUtils.isEmpty(totalPrice)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + PaymentParamConstant.param_orderAmount + "]不能为空");
		}
		Pattern p = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))\\.(\\d){2}?$");
		Matcher m = p.matcher(totalPrice);
		if (!m.matches()) {
			return ResponseInfoEnum.参数有误.getMap("参数[" + PaymentParamConstant.param_orderAmount + "]格式不正确(0.00)");
		}

		if (Double.valueOf(totalPrice) > 1000000.00 || Double.valueOf(totalPrice) < 0.00) {
			return ResponseInfoEnum.参数有误.getMap("参数[" + PaymentParamConstant.param_orderAmount + "]范围超出(0.00~1000000.00)");
		}

		String merchantOrderId = paramMap.get(PaymentParamConstant.param_merchantOrderId);// 商户传入订单号
		if (StringUtils.isEmpty(merchantOrderId)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + PaymentParamConstant.param_merchantOrderId + "]不能为空");
		}
		String regex = "(^[a-z0-9A-Z])[a-z0-9A-Z_]+([a-z0-9-A-Z])";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(merchantOrderId);
		if (!matcher.matches()) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + PaymentParamConstant.param_merchantOrderId + "]格式非法,必须由字母、数字、下划线组成,且开头和结尾不能有下划线");
		}
		String merchantAsyncCallbackURL = paramMap.get(PaymentParamConstant.param_merchantNotifyUrl);// 返回商户的异步回调url
		if (StringUtils.isEmpty(merchantAsyncCallbackURL)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + PaymentParamConstant.param_merchantNotifyUrl + "]不能为空");
		}
		Map<String, Object> gatewayMap = checkGatewayParameter(paramMap, serialNo);
		if (!ResponseInfoEnum.调用成功.equals(gatewayMap)) {
			return gatewayMap;
		}
		return ResponseInfoEnum.调用成功.getMap();
	}

	/**
	 * 用于自定义通道参数检查（如果通道没有自定义参数检查，此方法可不实现）
	 * 
	 * @param paramMap
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> checkGatewayParameter(Map<String, String> paramMap, String serialNo) throws ServiceException {
		return ResponseInfoEnum.调用成功.getMap();
	}

	/**
	 * 验证支付协议
	 * 
	 * @param paramMap
	 * @return
	 */
	protected Map<String, Object> checkAgreementParam(Map<String, String> paramMap) {
		String merCustId = paramMap.get(PaymentAgreementParamConstant.param_merchantCustomerId);
		if (StringUtils.isEmpty(merCustId)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + PaymentAgreementParamConstant.param_merchantCustomerId + "]不能为空");
		}

		String custContact = paramMap.get(PaymentAgreementParamConstant.param_customerContact);
		if (StringUtils.isEmpty(custContact)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + PaymentAgreementParamConstant.param_customerContact + "]不能为空");
		}

		String custIdentityCode = paramMap.get(PaymentAgreementParamConstant.param_customerIdentityCode);
		if (StringUtils.isEmpty(custIdentityCode)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + PaymentAgreementParamConstant.param_customerIdentityCode + "]不能为空");
		}

		String custName = paramMap.get(PaymentAgreementParamConstant.param_customerRealName);
		if (StringUtils.isEmpty(custName)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + PaymentAgreementParamConstant.param_customerRealName + "]不能为空");
		}

		String custBankCode = paramMap.get(PaymentAgreementParamConstant.param_bankCode);
		if (StringUtils.isEmpty(custBankCode)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + PaymentAgreementParamConstant.param_bankCode + "]不能为空");
		}

		Map<?, ?> bankMap = BeanUtils.json2Bean(extensionProperties.getBankList(), Map.class);
		if (!bankMap.containsKey(custBankCode)) {
			return ResponseInfoEnum.参数有误.getMap("参数[" + PaymentAgreementParamConstant.param_bankCode + "],不支持");
		}
		String custCardNO = paramMap.get(PaymentAgreementParamConstant.param_bankCardNo);
		if (StringUtils.isEmpty(custCardNO)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + PaymentAgreementParamConstant.param_bankCardNo + "]不能为空");
		}

		String custCardType = paramMap.get(PaymentAgreementParamConstant.param_customerCardType);
		if (StringUtils.isEmpty(custCardType)) {
			return ResponseInfoEnum.参数为空.getMap("参数[" + PaymentAgreementParamConstant.param_customerCardType + "]不能为空");
		} else if ("1".equals(custCardType) || "0".equals(custCardType)) {

			if ("1".equals(custCardType)) {
				String custCvv2 = paramMap.get(PaymentAgreementParamConstant.param_bankCardCVV2);
				if (StringUtils.isEmpty(custCvv2)) {
					return ResponseInfoEnum.参数为空.getMap("参数[" + PaymentAgreementParamConstant.param_bankCardCVV2 + "]不能为空");
				}

				String custValidate = paramMap.get(PaymentAgreementParamConstant.param_bankCardValidDate);
				if (StringUtils.isEmpty(custValidate)) {
					return ResponseInfoEnum.参数为空.getMap("参数[" + PaymentAgreementParamConstant.param_bankCardValidDate + "]不能为空");
				}
			}

		} else {
			return ResponseInfoEnum.参数为空.getMap("参数[" + PaymentAgreementParamConstant.param_customerCardType + "]传值错误");
		}
		return ResponseInfoEnum.调用成功.getMap();
	}

	/**
	 * 初始化交易数据
	 * 
	 * @param personApplicationEntity
	 * @param personEntity
	 * @param gatewayEntity
	 * @param paramMap
	 * @param serialNo
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> initPaymentTrade(PersonApplicationEntity personApplicationEntity, PersonEntity personEntity, GatewayEntity gatewayEntity, Map<String, String> paramMap, String serialNo) throws ServiceException {
		try {
			logger.info("支付订单创建开始");
			String orderidinf = paramMap.get(PaymentParamConstant.param_merchantOrderId);
			Map<String, Object> map = new HashMap<String, Object>();
			int orderCount = orderService.countOrderByOrderidinf(orderidinf);
			if (orderCount > 0) {
				logger.info("订单已存在,商户订单号是:{}", orderidinf);
				map.put(PaymentParamConstant.param_merchantOrderId, orderidinf);
				return ResponseInfoEnum.订单已存在.getMap(map);
			}
			OrderEntity orderEntity = orderService.createOrder(personApplicationEntity, personEntity, gatewayEntity, orderidinf, paramMap);
			logger.info("支付订单创建成功,订单信息:{}", orderEntity);
			tradeMonitorService.initTradeMonitor(orderEntity);
			map.put(CommonConstant.ZITOPAY_ENTITY_ORDER, orderEntity);
			return ResponseInfoEnum.调用成功.getMap(map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",初始化支付订单异常", e);
		}
	}

	/**
	 * 往消息队列放订单信息
	 */
	protected void putmq(final OrderEntity orderEntity) {
		/*try {
			 mq处理 
			taskExecutor.execute(new Runnable() {

				@Override
				public void run() {
					try {
						logger.info("开始往消息队列放订单信息" + orderEntity.getOrderid());
						MessageSender messageSender = new MessageSender();
						messageSender.setContent(JSON.toJSONString(orderEntity));
						messageSender.setRoutingKey(CommonConstant.ZITOPAYORDER_MQ_KEY);
						orderProducerService.sendOrderDataToQueue(messageSender);
						logger.info("完成往消息队列放订单信息" + orderEntity.getOrderid());
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			});
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}*/
	}

	@Override
	public Map<String, Object> checkPermission(String merchantId, String applicationId, String gatewayId, String serialNo) throws ServiceException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 验证商户是否存在以及启用
		PersonEntity zitopayPerson = personService.getZitopayPersonByPid(merchantId);
		if (null == zitopayPerson) {
			return ResponseInfoEnum.商户不存在.getMap();
		}
		if (DisabledStatusConstant.STATUS_DISABLED.equals(zitopayPerson.getStatus())) {
			return ResponseInfoEnum.商户已禁用.getMap();
		}

		// 验证商户应用是否存在以及启用
		if (StringUtils.isNotBlank(applicationId)) {
			PersonApplicationEntity zitopayPersonApplication = personApplicationService.getZitopayPersonApplicationByAppid(zitopayPerson.getId(), applicationId);

			if (null == zitopayPersonApplication) {
				return ResponseInfoEnum.商户应用不存在.getMap();
			}
			if (DisabledStatusConstant.STATUS_DISABLED.equals(zitopayPersonApplication.getDisabled())) {
				return ResponseInfoEnum.商户应用被禁用.getMap();
			}
			returnMap.put(CommonConstant.ZITOPAY_ENTITY_PERSON_APPLICATION, zitopayPersonApplication);
		}

		if (StringUtils.isNotBlank(gatewayId)) {
			if (BasePaymentServiceImpl.payment_verson2.equals(zitopayPerson.getCallVersion()) && com.gateway.common.utils.StringUtils.isEmpty(zitopayPerson.getRsapublickey())) {
				return ResponseInfoEnum.商户公钥不存在.getMap();
			}
			// 验证通道是否存在以及启用
			final GatewayEntity zitopayGeteway = gatewayService.getGateway(Integer.parseInt(gatewayId)); // 商户通道表
			if (zitopayGeteway == null) {
				return ResponseInfoEnum.通道不存在.getMap();
			}
			if (DisabledStatusConstant.STATUS_DISABLED.equals(zitopayGeteway.getDisable())) {
				return ResponseInfoEnum.通道已禁用.getMap();
			}

			// 验证该商户是否配置该通道配置启用
			PersonGatewayEntity zitopayPersonGeteway = personGatewayService.getZitopayPersonGateway(zitopayPerson.getId(), Integer.valueOf(gatewayId));
			if (null == zitopayPersonGeteway) {
				return ResponseInfoEnum.商户通道不存在.getMap();
			}
			if (DisabledStatusConstant.STATUS_DISABLED.equals(zitopayPersonGeteway.getDisable())) {
				return ResponseInfoEnum.商户通道被禁用.getMap();
			}
			returnMap.put(CommonConstant.ZITOPAY_ENTITY_GATEWAY, zitopayGeteway);
			returnMap.put(CommonConstant.ZITOPAY_ENTITY_PERSON_GATEWAY, zitopayPersonGeteway);
		}

		returnMap.put(CommonConstant.ZITOPAY_ENTITY_PERSON, zitopayPerson);
		return ResponseInfoEnum.调用成功.getMap(returnMap);
	}

	/**
	 * 查询上游通道和回调商户操作方法
	 * 
	 * @param entity
	 * @throws ServiceException
	 */
	public Map<String, Object> executeQuery(OrderEntity entity, String serialNo) throws ServiceException {
		String gatewayId = String.valueOf(entity.getGetewayid());
		IBasePaymentService basePaymentService = getPaymentService(gatewayId);
		if (basePaymentService == null) {
			logger.info("未找到通道[{}]的实例!", gatewayId);
			return ResponseInfoEnum.调用失败.getMap();
		}
		PersonGatewayEntity personGatewayEntity = personGatewayService.getZitopayPersonGateway(entity.getPersionid(), entity.getGetewayid());
		Map<String, Object> map = basePaymentService.queryOrder(entity, personGatewayEntity, serialNo);
		if (!ResponseInfoEnum.调用成功.equals(map)) {
			logger.info("查询第三方公司订单失败,失败原因:{}", entity.getOrderid(), map);
			return ResponseInfoEnum.调用失败.getMap();
		}
		PersonEntity personEntity = personService.queryByPersonId(entity.getPersionid());
		String merchantId = personEntity.getPid();
		// 查询上游返回第三方支付状态通过CommonConstant.TRADESTATUS键取得，值需为true/false字符串
		String status = String.valueOf(map.get(CommonConstant.TRADESTATUS));
		String channelReturnValue = String.valueOf(map.get(CommonConstant.RESULT));
		String payNo = String.valueOf(map.get(CommonConstant.TRADENO));
		String topPayNo = String.valueOf(map.get(CommonConstant.TOPTRADENO));
		String threeOrderId = entity.getThreeorderid();
		String merchantPublicKey = getMerchantPublicKey(merchantId, serialNo);
		basePaymentService.afterPayment(merchantId, merchantPublicKey, threeOrderId, DateUtil.getDate(), payNo, topPayNo, channelReturnValue, status, serialNo);
		return map;
	}
	
	/**
	 * 查询上游通道和回调商户操作方法
	 * 
	 * @param entity
	 * @throws ServiceException
	 */
	public Map<String, Object> executeQuery(IssuedEntity entity, String serialNo) throws ServiceException {
		String gatewayId = String.valueOf(entity.getGatewayid());
		IBaseIssuedService baseIssuedService = getIssuedService(gatewayId);
		if (baseIssuedService == null) {
			logger.info("未找到通道[{}]的实例!", gatewayId);
			return ResponseInfoEnum.调用失败.getMap();
		}
		PersonGatewayEntity personGatewayEntity = personGatewayService.getZitopayPersonGateway(entity.getPersonid(), entity.getGatewayid());
		Map<String, Object> map = baseIssuedService.queryOrder(entity, personGatewayEntity, serialNo);
		if (!ResponseInfoEnum.调用成功.equals(map)) {
			logger.info("查询第三方公司订单失败,失败原因:{}", entity.getIssuedid(), map);
			return ResponseInfoEnum.调用失败.getMap();
		}
		PersonEntity personEntity = personService.queryByPersonId(entity.getPersonid());
		String merchantId = personEntity.getPid();
		// 查询上游返回第三方支付状态通过CommonConstant.TRADESTATUS键取得，值需为true/false字符串
		String status = String.valueOf(map.get(CommonConstant.ISSUED_STATUS));
		String successTime = String.valueOf(map.get(CommonConstant.SUCCESSTIME));
		String payNo = String.valueOf(map.get(CommonConstant.TRADENO));
		String topPayNo = String.valueOf(map.get(CommonConstant.TOPTRADENO));
		String poundage = String.valueOf(map.get(CommonConstant.POUNDAGE));
		String threeOrderId = entity.getThreeorderid();
		String merchantPublicKey = getMerchantPublicKey(merchantId, serialNo);
		return baseIssuedService.afterIssued(merchantId, merchantPublicKey, threeOrderId, successTime, payNo, topPayNo, poundage, status, serialNo);
	}

	@SuppressWarnings({ "unchecked" })
	protected <T> T getPaymentService(String gatewayId) throws ServiceException {
		try {
			return (T) BeanContextUtil.getGatewayBean(BEAN_PREFIX_PAY + gatewayId);
		} catch (BeansException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings({ "unchecked" })
	protected <T> T getDrawService(String gatewayId) throws ServiceException {
		try {
			return (T) BeanContextUtil.getGatewayBean(BEAN_PREFIX_DRAW + gatewayId);
		} catch (BeansException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e);
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	protected <T> T getTransferService(String gatewayId) throws ServiceException {
		try {
			return (T) BeanContextUtil.getGatewayBean(BEAN_PREFIX_TRANSFER + gatewayId);
		} catch (BeansException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e);
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	protected <T> T getIssuedService(String gatewayId) throws ServiceException {
		try {
			return (T) BeanContextUtil.getGatewayBean(BEAN_PREFIX_ISSUED + gatewayId);
		} catch (BeansException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings({ "unchecked" })
	protected <T> T getRefundService(String gatewayId) throws ServiceException {
		try {
			return (T) BeanContextUtil.getGatewayBean(BEAN_PREFIX_REFUND + gatewayId);
		} catch (BeansException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings({ "unchecked" })
	protected <T> T getRegisterService(String channelId) throws ServiceException {
		try {
			return (T) BeanContextUtil.getMerchantRegisterBean(channelId);
		} catch (BeansException e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e);
		}
	}
}
