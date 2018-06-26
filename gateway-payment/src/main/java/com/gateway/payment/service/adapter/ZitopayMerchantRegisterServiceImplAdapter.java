/**
 * Copyright [2015-2017]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.gateway.payment.service.adapter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gateway.common.algorithm.MD5;
import com.gateway.common.constants.CommonConstant;
import com.gateway.common.constants.param.MerchantRegisterMethodConstant;
import com.gateway.common.constants.param.MerchantRegisterParamConstant;
import com.gateway.common.constants.param.MerchantRegisterResultParamConstant;
import com.gateway.common.constants.param.ZitopayMerchantRegisterParamConstant;
import com.gateway.common.constants.param.ZitopayMerchantRegisterResultParamConstant;
import com.gateway.common.constants.param.ZitopayMerchantRegisterUpdateParamConstant;
import com.gateway.common.constants.status.DisabledStatusConstant;
import com.gateway.common.message.ResponseInfoEnum;
import com.gateway.payment.entity.EmployeeEntity;
import com.gateway.payment.entity.EmployeeGatewayEntity;
import com.gateway.payment.entity.GatewayEntity;
import com.gateway.payment.entity.PersonAppGatewayEntity;
import com.gateway.payment.entity.PersonApplicationEntity;
import com.gateway.payment.entity.PersonAuthenticationBaseEntity;
import com.gateway.payment.entity.PersonAuthenticationStreamEntity;
import com.gateway.payment.entity.PersonEntity;
import com.gateway.payment.entity.PersonGatewayEntity;
import com.gateway.payment.service.util.DigestUtils;
import com.gateway.payment.service.util.HashUtil;
import com.gateway.payment.service.util.IDGenerator;
import com.zitopay.foundation.common.exception.ServiceException;
import com.zitopay.foundation.common.util.BeanUtils;

/**
 * 融智付非收银台服务实现
 * 
 * 作者：王政 创建时间：2017年3月6日 下午5:59:13
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class ZitopayMerchantRegisterServiceImplAdapter extends BaseServiceImplAdapter implements IZitopayMerchantRegisterServiceAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ZitopayMerchantRegisterServiceImplAdapter.class);

	@Reference
	private IMerchantRegisterServiceAdapter merchantRegisterServiceAdapter;

	@Override
	public Map<String, Object> checkRegisterParameter(String method, Map<String, String> paramMap, String serialNo) throws ServiceException {
		try {
			if (MerchantRegisterMethodConstant.method_register.equals(method)) {

				String account = paramMap.get(ZitopayMerchantRegisterParamConstant.param_account);
				if (StringUtils.isBlank(account)) {
					return ResponseInfoEnum.参数为空.getMap("参数[" + ZitopayMerchantRegisterParamConstant.param_account + "]不能为空");
				}

				String password = paramMap.get(ZitopayMerchantRegisterParamConstant.param_password);
				if (StringUtils.isBlank(password)) {
					return ResponseInfoEnum.参数为空.getMap("参数[" + ZitopayMerchantRegisterParamConstant.param_password + "]不能为空");
				}

			} else if (MerchantRegisterMethodConstant.method_update.equals(method)) {

				String merId = paramMap.get(ZitopayMerchantRegisterUpdateParamConstant.param_merchantId);
				if (StringUtils.isBlank(merId)) {
					return ResponseInfoEnum.参数为空.getMap("参数[" + ZitopayMerchantRegisterUpdateParamConstant.param_merchantId + "]不能为空");
				}

				String appId = paramMap.get(ZitopayMerchantRegisterUpdateParamConstant.param_appId);
				if (StringUtils.isBlank(appId)) {
					return ResponseInfoEnum.参数为空.getMap("参数[" + ZitopayMerchantRegisterUpdateParamConstant.param_appId + "]不能为空");
				}

			} else if (MerchantRegisterMethodConstant.method_query.equals(method)) {

			}
			paramMap.put(MerchantRegisterParamConstant.param_emplConfig, "no_check");
			return merchantRegisterServiceAdapter.checkRegisterParameter(method, paramMap, serialNo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("报文序列号:" + serialNo + ",融智付商户入驻参数校验异常,请检查!", e);
		}
	}

	@Override
	public Map<String, Object> checkRegisterPermission(String method, Map<String, String> paramMap, String serialNo) throws ServiceException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if (MerchantRegisterMethodConstant.method_register.equals(method)) {
			// 验证代理信息是否正确
			String account = paramMap.get(ZitopayMerchantRegisterParamConstant.param_account);
			String password = paramMap.get(ZitopayMerchantRegisterParamConstant.param_password);
			EmployeeEntity employeeEntity = employeeService.getEmployee(account, MD5.encode(password));
			if (null == employeeEntity) {
				logger.info("融智付【商户入驻】接口 未找到该代理，账号：{}", account);
				return ResponseInfoEnum.调用失败.getMap("未找到该代理，请核对代理账号");
			}
			returnMap.put(CommonConstant.ZITOPAY_ENTITY_EMPLOYEE, employeeEntity);

			// 验证通道是否存在以及启用
			Integer gatewayId = Integer.parseInt(paramMap.get(ZitopayMerchantRegisterParamConstant.param_gatewayId));
			final GatewayEntity zitopayGeteway = gatewayService.getGateway(gatewayId); // 商户通道表
			if (zitopayGeteway == null) {
				return ResponseInfoEnum.通道不存在.getMap();
			}
			if (DisabledStatusConstant.STATUS_DISABLED.equals(zitopayGeteway.getDisable())) {
				return ResponseInfoEnum.通道已禁用.getMap();
			}

			// 验证商户是否已经存在
			String cardNo = paramMap.get(ZitopayMerchantRegisterParamConstant.param_cardNo);
			PersonEntity personEntity = null;
			try {
				personEntity = personService.getZitopayPersonByLoginname(DigestUtils.encodeBase64(cardNo + gatewayId + account));
			} catch (UnsupportedEncodingException e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException("报文序列号:" + serialNo + ",查询商户异常,请检查!", e);
			}
			if (null != personEntity) {
				logger.info("融智付【商户入驻】接口 该商户已存在，商户号：{}", cardNo + gatewayId + account);
				return ResponseInfoEnum.调用失败.getMap("商户已存在");
			}

			// 验证费率配置是否正确
			BigDecimal settleRate = new BigDecimal(paramMap.get(ZitopayMerchantRegisterParamConstant.param_settleRate));
			BigDecimal fixFee = new BigDecimal(paramMap.get(ZitopayMerchantRegisterParamConstant.param_fixFee));
			final Map<String, Object> checkRateMap = checkRate(employeeEntity.getEmployeeid(), gatewayId, settleRate, fixFee);
			if (!ResponseInfoEnum.调用成功.equals(checkRateMap)) {
				return checkRateMap;
			}
			returnMap.put(CommonConstant.ZITOPAY_ENTITY_EMPLOYEE_GATEWAY, checkRateMap.get(CommonConstant.ZITOPAY_ENTITY_EMPLOYEE_GATEWAY));

		} else if (MerchantRegisterMethodConstant.method_update.equals(method)) {
			String merchantId = paramMap.get(ZitopayMerchantRegisterUpdateParamConstant.param_merchantId);
			String applicationId = paramMap.get(ZitopayMerchantRegisterUpdateParamConstant.param_appId);
			String gatewayId = paramMap.get(ZitopayMerchantRegisterParamConstant.param_gatewayId);
			String cardNo = paramMap.get(ZitopayMerchantRegisterParamConstant.param_cardNo);

			PersonEntity personEntity = personService.getZitopayPersonByLoginname(cardNo + gatewayId);
			if (null != personEntity && !personEntity.getPid().equals(merchantId)) {
				logger.info("融智付【商户入驻】接口 该卡号已与其他商户绑定，请换卡重试，商户号：{}", cardNo + gatewayId);
				return ResponseInfoEnum.调用失败.getMap("该卡号已与其他商户绑定，请换卡重试");
			}

			try {
				Map<String, Object> checkPermission = checkPermission(merchantId, applicationId, gatewayId, serialNo);
				returnMap.put(CommonConstant.ZITOPAY_ENTITY_PERSON_APPLICATION, checkPermission.get(CommonConstant.ZITOPAY_ENTITY_PERSON_APPLICATION));
				returnMap.put(CommonConstant.ZITOPAY_ENTITY_GATEWAY, checkPermission.get(CommonConstant.ZITOPAY_ENTITY_GATEWAY));
				returnMap.put(CommonConstant.ZITOPAY_ENTITY_PERSON_GATEWAY, checkPermission.get(CommonConstant.ZITOPAY_ENTITY_PERSON_GATEWAY));
				returnMap.put(CommonConstant.ZITOPAY_ENTITY_PERSON, checkPermission.get(CommonConstant.ZITOPAY_ENTITY_PERSON));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException("报文序列号:" + serialNo + ",权限校验异常,请检查!", e);
			}

			// 验证费率配置是否正确
			BigDecimal settleRate = new BigDecimal(paramMap.get(ZitopayMerchantRegisterParamConstant.param_settleRate));
			BigDecimal fixFee = new BigDecimal(paramMap.get(ZitopayMerchantRegisterParamConstant.param_fixFee));
			final Map<String, Object> checkRateMap = checkRate(personEntity.getParentid(), Integer.parseInt(gatewayId), settleRate, fixFee);
			if (!ResponseInfoEnum.调用成功.equals(checkRateMap)) {
				return checkRateMap;
			}
			returnMap.put(CommonConstant.ZITOPAY_ENTITY_EMPLOYEE_GATEWAY, checkRateMap.get(CommonConstant.ZITOPAY_ENTITY_EMPLOYEE_GATEWAY));

		} else if (MerchantRegisterMethodConstant.method_query.equals(method)) {

		}
		return ResponseInfoEnum.调用成功.getMap(returnMap);
	}

	@Override
	public Map<String, Object> execRegister(String method, Map<String, String> paramMap, Map<String, Object> permissionMap, String serialNo) throws ServiceException {
		Map<String, Object> resultMap = new HashMap<>();
		if (MerchantRegisterMethodConstant.method_register.equals(method)) {

			EmployeeEntity employeeEntity = (EmployeeEntity) permissionMap.get(CommonConstant.ZITOPAY_ENTITY_EMPLOYEE);
			EmployeeGatewayEntity employeeGateway = (EmployeeGatewayEntity) permissionMap.get(CommonConstant.ZITOPAY_ENTITY_EMPLOYEE_GATEWAY);

			// 调用商户入驻通道
			paramMap.put(ZitopayMerchantRegisterParamConstant.param_emplConfig, employeeGateway.getEmployeeConfig());
			Map<String, Object> registerMap = merchantRegisterServiceAdapter.execRegister(method, paramMap, serialNo);
			if (!ResponseInfoEnum.调用成功.equals(registerMap)) {
				return registerMap;
			}

			/** 创建【商户】信息 */
			final Map<String, Object> personMap = createPerson(paramMap, employeeEntity.getEmployeeid());
			if (!ResponseInfoEnum.调用成功.equals(personMap)) {
				return personMap;
			}
			PersonEntity personEntity = (PersonEntity) personMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON);

			/** 创建【商户认证记录】信息 */
			final Map<String, Object> streamMap = createPersonAuthenticationStream(personEntity.getId());
			if (!ResponseInfoEnum.调用成功.equals(streamMap)) {
				return streamMap;
			}
			PersonAuthenticationStreamEntity stream = (PersonAuthenticationStreamEntity) streamMap.get(CommonConstant.ZITOPAY_ENTITY_PERSSON_AUTHENTICATION_STREAM);

			/** 创建【商户认证】信息 */
			final Map<String, Object> baseMap = createPersonAuthenticationBase(paramMap, stream.getId());
			if (!ResponseInfoEnum.调用成功.equals(baseMap)) {
				return baseMap;
			}

			/** 创建【商户应用】信息 */
			final Map<String, Object> appMap = createPersonApplication(personEntity.getId());
			if (!ResponseInfoEnum.调用成功.equals(appMap)) {
				return appMap;
			}
			PersonApplicationEntity personApplication = (PersonApplicationEntity) appMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON_APPLICATION);
			String appId = personApplication.getAppId();
			String appKey = personApplication.getApplicationKey();

			BigDecimal payRate = new BigDecimal(paramMap.get(ZitopayMerchantRegisterParamConstant.param_settleRate));
			BigDecimal payFixFee = new BigDecimal(paramMap.get(ZitopayMerchantRegisterParamConstant.param_fixFee));
			BigDecimal withdrawRate = new BigDecimal(paramMap.get(ZitopayMerchantRegisterParamConstant.param_withdrawRate));
			BigDecimal withdrawFee = new BigDecimal(paramMap.get(ZitopayMerchantRegisterParamConstant.param_withdrawFixFee));

			Map<?, ?> registryResult = (Map<?, ?>) registerMap.get(MerchantRegisterResultParamConstant.result_map);
			/** 创建【商户通道】信息 设置商户参数 */
			final Map<String, Object> gatewayMap = createPersonGateway(personEntity.getId(), employeeGateway.getGetewayid(), payRate, payFixFee, withdrawRate, withdrawFee, employeeGateway.getMin(), employeeGateway.getMax(), employeeGateway.getClearform(), registryResult);
			if (!ResponseInfoEnum.调用成功.equals(gatewayMap)) {
				return gatewayMap;
			}
			PersonGatewayEntity zitopayPersonGeteway = (PersonGatewayEntity) gatewayMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON_GATEWAY);

			/** 创建【商户通道】-【商户应用】关联信息 */
			final Map<String, Object> appGatewayMap = createPersonAppGeteway(personApplication.getApplicationId(), zitopayPersonGeteway.getId());
			if (!ResponseInfoEnum.调用成功.equals(appGatewayMap)) {
				return appGatewayMap;
			}

			// 返回商户ID、应用ID、应用KEY
			resultMap.put(ZitopayMerchantRegisterResultParamConstant.param_merchantId, personEntity.getPid());
			resultMap.put(ZitopayMerchantRegisterResultParamConstant.param_appId, appId);
			resultMap.put(ZitopayMerchantRegisterResultParamConstant.param_appKey, appKey);
		} else if (MerchantRegisterMethodConstant.method_update.equals(method)) {
			PersonGatewayEntity personGatewayEntity = (PersonGatewayEntity) permissionMap.get(CommonConstant.ZITOPAY_ENTITY_PERSON_GATEWAY);
			// 调用商户入驻通道
			paramMap.put(ZitopayMerchantRegisterUpdateParamConstant.param_merConfig, personGatewayEntity.getExt1());
			return merchantRegisterServiceAdapter.execRegister(method, paramMap, serialNo);
		} else if (MerchantRegisterMethodConstant.method_query.equals(method)) {

		}

		return ResponseInfoEnum.调用成功.getMap(resultMap);
	}

	/**
	 * 创建商户信息
	 */
	protected Map<String, Object> createPerson(Map<String, String> paramMap, Integer employeeId) throws ServiceException {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			// 创建商户
			String pid = getUnRepeatPid();
			String cardRealName = paramMap.get(ZitopayMerchantRegisterParamConstant.param_cardRealName);
			String channelId = paramMap.get(ZitopayMerchantRegisterParamConstant.param_gatewayId);
			String cardNo = paramMap.get(ZitopayMerchantRegisterParamConstant.param_cardNo);
			String password = paramMap.get(ZitopayMerchantRegisterParamConstant.param_password);
			String cardMobile = paramMap.get(ZitopayMerchantRegisterParamConstant.param_cardMobile);
			String merEmail = paramMap.get(ZitopayMerchantRegisterParamConstant.param_merEmail);
			String account = paramMap.get(ZitopayMerchantRegisterParamConstant.param_account);

			PersonEntity zitopayPerson = new PersonEntity();
			zitopayPerson.setParentid(employeeId);
			zitopayPerson.setPid(pid);
			zitopayPerson.setUsername(cardRealName);
			zitopayPerson.setLoginname(DigestUtils.encodeBase64(cardNo + channelId + account));
			zitopayPerson.setPassword(MD5.encode(password));
			zitopayPerson.setStatus(0);
			zitopayPerson.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			zitopayPerson.setCreationtime(new Date());
			zitopayPerson.setBusinessName(cardRealName);
			zitopayPerson.setBusinessPhone(cardMobile);
			zitopayPerson.setBusinessEmail(merEmail);
			zitopayPerson.setPersonType(0);
			zitopayPerson.setMerchantsTag("H类");
			int result = personService.saveSelective(zitopayPerson);
			if (result <= 0) {
				return ResponseInfoEnum.调用失败.getMap("创建商户失败");
			}
			resultMap.put(CommonConstant.ZITOPAY_ENTITY_PERSON, zitopayPerson);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("融智付【商户入驻】接口 创建商户失败：{}", e.getLocalizedMessage());
			return ResponseInfoEnum.系统异常.getMap();
		}
		return ResponseInfoEnum.调用成功.getMap(resultMap);
	}

	/**
	 * 创建商户认证记录
	 */
	protected Map<String, Object> createPersonAuthenticationStream(Integer personId) throws ServiceException {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			PersonAuthenticationStreamEntity stream = new PersonAuthenticationStreamEntity();
			stream.setPersonId(personId);
			stream.setState(0);
			stream.setApplyType(0);
			stream.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			stream.setModTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			int result = personAuthenticationStreamService.saveSelective(stream);
			if (result <= 0) {
				logger.info("融智付【商户入驻】接口 商户认证记录入库失败");
				return ResponseInfoEnum.调用失败.getMap("融智付【商户入驻】接口 商户认证记录入库失败");
			}
			resultMap.put(CommonConstant.ZITOPAY_ENTITY_PERSSON_AUTHENTICATION_STREAM, stream);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("融智付【商户入驻】接口 创建商户认证记录失败：{}", e.getLocalizedMessage());
			return ResponseInfoEnum.系统异常.getMap("融智付【商户入驻】接口 创建商户认证记录失败");
		}
		return ResponseInfoEnum.调用成功.getMap(resultMap);
	}

	/**
	 * 创建商户认证信息
	 */
	protected Map<String, Object> createPersonAuthenticationBase(Map<String, String> paramMap, Integer streamId) throws ServiceException {
		try {
			String cardRealName = paramMap.get(ZitopayMerchantRegisterParamConstant.param_cardRealName);
			String cardNo = paramMap.get(ZitopayMerchantRegisterParamConstant.param_cardNo);
			String bankName = paramMap.get(ZitopayMerchantRegisterParamConstant.param_bankName);
			String bankCode = paramMap.get(ZitopayMerchantRegisterParamConstant.param_bankNo);
			String cardMobile = paramMap.get(ZitopayMerchantRegisterParamConstant.param_cardMobile);
			String merEmail = paramMap.get(ZitopayMerchantRegisterParamConstant.param_merEmail);
			String merName = paramMap.get(ZitopayMerchantRegisterParamConstant.param_merName);
			String merAddress = paramMap.get(ZitopayMerchantRegisterParamConstant.param_merAddress);
			String province = paramMap.get(ZitopayMerchantRegisterParamConstant.param_bankProvince);
			String city = paramMap.get(ZitopayMerchantRegisterParamConstant.param_bankCity);

			PersonAuthenticationBaseEntity base = new PersonAuthenticationBaseEntity();
			base.setStreamId(streamId);
			base.setUserName(cardRealName);
			base.setPhone(cardMobile);
			base.setEmail(merEmail);
			base.setBankCompanyName(cardRealName);
			base.setBankNo(cardNo);
			base.setBankName(bankName);
			base.setCompanyName(merName);
			base.setContactAddress(merAddress);
			if (!com.gateway.common.utils.StringUtils.isEmpty(province))
				base.setBankAddrProvince(Integer.valueOf(province));
			if (!com.gateway.common.utils.StringUtils.isEmpty(city))
				base.setBankAddrCity(Integer.valueOf(city));
			if (!com.gateway.common.utils.StringUtils.isEmpty(bankCode))
				base.setBankSubCode(bankCode);

			base.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			base.setModtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			int result = personAuthenticationBaseService.saveSelective(base);
			if (result <= 0) {
				logger.info("融智付【商户入驻】接口 商户认证信息入库失败");
				return ResponseInfoEnum.调用失败.getMap("融智付【商户入驻】接口 商户认证信息入库失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("融智付【商户入驻】接口 创建商户认证信息失败");
			return ResponseInfoEnum.系统异常.getMap("融智付【商户入驻】接口 创建商户认证信息失败");
		}
		return ResponseInfoEnum.调用成功.getMap();
	}

	/**
	 * 创建商户应用
	 */
	protected Map<String, Object> createPersonApplication(Integer personId) throws ServiceException {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			String appId = getUnRepeatAppId();
			String key = MD5.encode(HashUtil.encrypt(appId));
			PersonApplicationEntity zitopayPersonApplication = new PersonApplicationEntity();
			zitopayPersonApplication.setPersonId(String.valueOf(personId));
			zitopayPersonApplication.setAppScenarios("4");
			zitopayPersonApplication.setApplicationName("线下收款");
			zitopayPersonApplication.setAppId(appId);
			zitopayPersonApplication.setDisabled(0);
			zitopayPersonApplication.setApplicationKey(key);
			zitopayPersonApplication.setApplicationDesc("快捷线下收款专用");
			zitopayPersonApplication.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			zitopayPersonApplication.setModTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			int result = personApplicationService.saveSelective(zitopayPersonApplication);
			if (result <= 0) {
				logger.info("融智付【商户入驻】接口 商户应用信息入库失败");
				return ResponseInfoEnum.调用失败.getMap("融智付【商户入驻】接口 商户应用信息入库失败");
			}
			resultMap.put(CommonConstant.ZITOPAY_ENTITY_PERSON_APPLICATION, zitopayPersonApplication);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("融智付【商户入驻】接口 创建商户应用失败：{}", e.getLocalizedMessage());
			return ResponseInfoEnum.系统异常.getMap("融智付【商户入驻】接口 创建商户应用失败");
		}
		return ResponseInfoEnum.调用成功.getMap(resultMap);
	}

	/**
	 * 创建商户通道
	 */
	protected Map<String, Object> createPersonGateway(Integer personId, Integer gatewayId, BigDecimal paymentRate, BigDecimal fix, BigDecimal withDrawRate, BigDecimal withdrawFee, BigDecimal min, BigDecimal max, String clearform, Map<?, ?> param) throws ServiceException {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			PersonGatewayEntity zitopayPersonGeteway = new PersonGatewayEntity();
			zitopayPersonGeteway.setPersonid(personId);
			zitopayPersonGeteway.setGetewayid(gatewayId);
			zitopayPersonGeteway.setDisable(0);

			// 费率相关
			zitopayPersonGeteway.setRate(paymentRate);
			zitopayPersonGeteway.setMin(min);
			zitopayPersonGeteway.setMax(max);
			zitopayPersonGeteway.setFix(fix);
			zitopayPersonGeteway.setClearform(clearform);
			zitopayPersonGeteway.setWithdrawRate(withDrawRate);
			zitopayPersonGeteway.setWithdrawFee(withdrawFee);
			zitopayPersonGeteway.setExt3(BeanUtils.bean2JSON(param));

			int result = personGatewayService.saveSelective(zitopayPersonGeteway);
			if (result <= 0) {
				logger.info("融智付【商户入驻】接口 商户通道信息入库失败");
				return ResponseInfoEnum.调用失败.getMap("融智付【商户入驻】接口 商户通道信息入库失败");
			}
			resultMap.put(CommonConstant.ZITOPAY_ENTITY_PERSON_GATEWAY, zitopayPersonGeteway);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("融智付【商户入驻】接口 创建商户通道失败：{}", e.getLocalizedMessage());
			return ResponseInfoEnum.系统异常.getMap("融智付【商户入驻】接口 创建商户通道失败");
		}
		return ResponseInfoEnum.调用成功.getMap(resultMap);
	}

	/**
	 * 创建【商户通道】-【商户应用】关联关系
	 */
	protected Map<String, Object> createPersonAppGeteway(Integer personApplicationId, Integer personGetewayId) throws ServiceException {
		try {
			PersonAppGatewayEntity zitopayPersonAppGeteway = new PersonAppGatewayEntity();
			zitopayPersonAppGeteway.setApplicationId(personApplicationId);
			zitopayPersonAppGeteway.setPerGetewayId(personGetewayId);
			zitopayPersonAppGeteway.setOrder(0);
			int result = personAppGatewayService.saveSelective(zitopayPersonAppGeteway);
			if (result <= 0) {
				logger.info("融智付【商户入驻】接口 商户应用通道信息入库失败");
				return ResponseInfoEnum.调用失败.getMap("融智付【商户入驻】接口 商户应用通道信息入库失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("融智付【商户入驻】接口 创建商户应用通道失败：{}", e.getLocalizedMessage());
			return ResponseInfoEnum.系统异常.getMap("融智付【商户入驻】接口 创建商户应用通道失败");
		}
		return ResponseInfoEnum.调用成功.getMap();
	}

	/**
	 * 验证费率配置
	 */
	protected Map<String, Object> checkRate(Integer employeeId, Integer gatewayId, BigDecimal settleRate, BigDecimal extraRate) throws ServiceException {
		Map<String, Object> returnMap = new HashMap<>();
		try {
			EmployeeGatewayEntity employeeGatewayEntity = employeeGatewayService.getEmployeeGateway(employeeId, gatewayId);
			if (employeeGatewayEntity == null) {
				logger.info("融智付【商户入驻】接口 代理通道不存在，代理id：{}， 通道id：{}", employeeId, gatewayId);
				return ResponseInfoEnum.调用失败.getMap("代理通道费率不存在");
			}
			BigDecimal channelCost = employeeGatewayEntity.getChannelCost();
			BigDecimal fix = employeeGatewayEntity.getFix();
			BigDecimal min = employeeGatewayEntity.getMin();
			BigDecimal max = employeeGatewayEntity.getMax();
			String clearform = employeeGatewayEntity.getClearform();
			if (channelCost == null || fix == null || min == null || max == null || StringUtils.isBlank(clearform)) {
				return ResponseInfoEnum.参数为空.getMap("代理通道费率相关参数为空");
			}
			if (settleRate.doubleValue() < channelCost.doubleValue()) {
				logger.info("融智付【商户入驻】接口 用户通道费率不得低于代理费率");
				return ResponseInfoEnum.调用失败.getMap("用户通道费率不得低于代理费率");
			}
			if (extraRate.doubleValue() < fix.doubleValue()) {
				logger.info("融智付【商户入驻】接口 用户单笔手续费不得低于代理单笔手续费");
				return ResponseInfoEnum.调用失败.getMap("用户单笔手续费不得低于代理单笔手续费");
			}
			if (StringUtils.isBlank(employeeGatewayEntity.getEmployeeConfig())) {
				logger.info("融智付【商户入驻】接口 代理通道配置不能为空");
				return ResponseInfoEnum.调用失败.getMap("代理通道配置不能为空");
			}
			returnMap.put(CommonConstant.ZITOPAY_ENTITY_EMPLOYEE_GATEWAY, employeeGatewayEntity);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("融智付【商户入驻】接口 费率校验失败，原因：{}", e.getLocalizedMessage());
			return ResponseInfoEnum.系统异常.getMap();
		}
		return ResponseInfoEnum.调用成功.getMap(returnMap);
	}

	/**
	 * 生成商户pid
	 */
	private String getUnRepeatPid() {
		String pid = IDGenerator.newPid();
		PersonEntity personEntity = personService.getZitopayPersonByPid(pid);
		if (personEntity != null) {
			pid = getUnRepeatPid();
		}
		return pid;
	}

	/**
	 * 生成应用appid
	 */
	private String getUnRepeatAppId() {
		String appid = IDGenerator.newPid();
		PersonApplicationEntity personApplicationEntity = personApplicationService.getZitopayPersonApplicationByAppid(appid);
		if (personApplicationEntity != null) {
			appid = getUnRepeatAppId();
		}
		return appid;
	}

}