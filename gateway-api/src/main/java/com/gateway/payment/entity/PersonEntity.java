package com.gateway.payment.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.zitopay.foundation.common.entity.AbstractEntity;
import com.zitopay.foundation.common.util.BeanUtils;

/**
 * 商户实体
 * 
 * @author xiaoshiwen<xiaoshiwen@zitopay.com>
 * @since 2017年5月9日
 */
@Table(name = "alwaypay_person")
public class PersonEntity extends AbstractEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1087581145932539069L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String pid;

	private String username;

	private String personkey;

	private String loginname;

	private String password;

	private Integer status;

	private String contacts;

	private String phone;

	private String address;

	private String legalperson;

	private String idcard;

	private Date creationtime;

	private String createtime;

	private String imageid;

	private Integer photoid;

	private Integer auStatus;

	private String inviteCode;

	private Integer loginId;
	@Column(name = "parentId")
	private Integer parentid;

	private String businessName;

	private String businessPhone;

	private String businessEmail;

	private String businessQq;

	private String industry;

	private String rsapublickey;
	
	private String level;
	
	private Integer personType;

    private String merchantsTag;
    
    private Boolean settlementType;

    public String getCallVersion() {
		return callVersion;
	}

	public void setCallVersion(String callVersion) {
		this.callVersion = callVersion;
	}

	private Boolean source;

    private String openid;
    
    private String callVersion;

	public String getMerchantsTag() {
		return merchantsTag;
	}

	public void setMerchantsTag(String merchantsTag) {
		this.merchantsTag = merchantsTag;
	}

	public Boolean getSettlementType() {
		return settlementType;
	}

	public void setSettlementType(Boolean settlementType) {
		this.settlementType = settlementType;
	}

	public Boolean getSource() {
		return source;
	}

	public void setSource(Boolean source) {
		this.source = source;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Integer getPersonType() {
		return personType;
	}

	public void setPersonType(Integer personType) {
		this.personType = personType;
	}

	public String getRsapublickey() {
		return rsapublickey;
	}

	public void setRsapublickey(String rsapublickey) {
		this.rsapublickey = rsapublickey;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid == null ? null : pid.trim();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}

	public String getPersonkey() {
		return personkey;
	}

	public void setPersonkey(String personkey) {
		this.personkey = personkey == null ? null : personkey.trim();
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname == null ? null : loginname.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts == null ? null : contacts.trim();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone == null ? null : phone.trim();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address == null ? null : address.trim();
	}

	public String getLegalperson() {
		return legalperson;
	}

	public void setLegalperson(String legalperson) {
		this.legalperson = legalperson == null ? null : legalperson.trim();
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard == null ? null : idcard.trim();
	}

	public Date getCreationtime() {
		return creationtime;
	}

	public void setCreationtime(Date creationtime) {
		this.creationtime = creationtime;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime == null ? null : createtime.trim();
	}

	public String getImageid() {
		return imageid;
	}

	public void setImageid(String imageid) {
		this.imageid = imageid == null ? null : imageid.trim();
	}

	public Integer getPhotoid() {
		return photoid;
	}

	public void setPhotoid(Integer photoid) {
		this.photoid = photoid;
	}

	public Integer getAuStatus() {
		return auStatus;
	}

	public void setAuStatus(Integer auStatus) {
		this.auStatus = auStatus;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode == null ? null : inviteCode.trim();
	}

	public Integer getLoginId() {
		return loginId;
	}

	public void setLoginId(Integer loginId) {
		this.loginId = loginId;
	}

	public Integer getParentid() {
		return parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName == null ? null : businessName.trim();
	}

	public String getBusinessPhone() {
		return businessPhone;
	}

	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone == null ? null : businessPhone
				.trim();
	}

	public String getBusinessEmail() {
		return businessEmail;
	}

	public void setBusinessEmail(String businessEmail) {
		this.businessEmail = businessEmail == null ? null : businessEmail
				.trim();
	}

	public String getBusinessQq() {
		return businessQq;
	}

	public void setBusinessQq(String businessQq) {
		this.businessQq = businessQq == null ? null : businessQq.trim();
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry == null ? null : industry.trim();
	}

	@Override
	public String toString() {
		return BeanUtils.bean2JSON(this);
	}
}