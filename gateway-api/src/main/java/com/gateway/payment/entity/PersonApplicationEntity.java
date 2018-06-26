package com.gateway.payment.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.zitopay.foundation.common.entity.AbstractEntity;
import com.zitopay.foundation.common.util.BeanUtils;

/**
 * 商户应用表实体
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Table(name = "alwaypay_person_application")
public class PersonApplicationEntity extends AbstractEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2297608983180200917L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer applicationId;

	private String personId;

	private String appScenarios;

	private String applicationName;

	private String appId;

	private Integer disabled;

	private String applicationKey;

	private String applicationDesc;

	private String createTime;

	private String modTime;
    
	private String device;
	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId == null ? null : personId.trim();
	}

	public String getAppScenarios() {
		return appScenarios;
	}

	public void setAppScenarios(String appScenarios) {
		this.appScenarios = appScenarios == null ? null : appScenarios.trim();
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName == null ? null : applicationName.trim();
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId == null ? null : appId.trim();
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public String getApplicationKey() {
		return applicationKey;
	}

	public void setApplicationKey(String applicationKey) {
		this.applicationKey = applicationKey == null ? null : applicationKey.trim();
	}

	public String getApplicationDesc() {
		return applicationDesc;
	}

	public void setApplicationDesc(String applicationDesc) {
		this.applicationDesc = applicationDesc == null ? null : applicationDesc.trim();
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime == null ? null : createTime.trim();
	}

	public String getModTime() {
		return modTime;
	}

	public void setModTime(String modTime) {
		this.modTime = modTime == null ? null : modTime.trim();
	}

	@Override
	public String toString() {
		return BeanUtils.bean2JSON(this);
	}
}