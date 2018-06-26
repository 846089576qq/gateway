package com.gateway.payment.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "zitopay_person_authentication_base")
public class PersonAuthenticationBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer streamId;

    private String userName;

    private String phone;

    private String qq;

    private String email;

    private String companyName;

    private String registeredAddress;

    private String contactAddress;

    private String subordinateIndustry;

    private String website;

    private String preparedNo;

    private String bankCompanyName;

    private String bankNo;

    private Integer bankAddrProvince;

    private Integer bankAddrCity;

    private String bankName;

    private String bankSubName;

    private String bankSubCode;

    private String createtime;

    private String modtime;

    private String instructions;

    private String platformname;

    public Integer getStreamId() {
        return streamId;
    }

    public void setStreamId(Integer streamId) {
        this.streamId = streamId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getRegisteredAddress() {
        return registeredAddress;
    }

    public void setRegisteredAddress(String registeredAddress) {
        this.registeredAddress = registeredAddress == null ? null : registeredAddress.trim();
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress == null ? null : contactAddress.trim();
    }

    public String getSubordinateIndustry() {
        return subordinateIndustry;
    }

    public void setSubordinateIndustry(String subordinateIndustry) {
        this.subordinateIndustry = subordinateIndustry == null ? null : subordinateIndustry.trim();
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website == null ? null : website.trim();
    }

    public String getPreparedNo() {
        return preparedNo;
    }

    public void setPreparedNo(String preparedNo) {
        this.preparedNo = preparedNo == null ? null : preparedNo.trim();
    }

    public String getBankCompanyName() {
        return bankCompanyName;
    }

    public void setBankCompanyName(String bankCompanyName) {
        this.bankCompanyName = bankCompanyName == null ? null : bankCompanyName.trim();
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo == null ? null : bankNo.trim();
    }

    public Integer getBankAddrProvince() {
        return bankAddrProvince;
    }

    public void setBankAddrProvince(Integer bankAddrProvince) {
        this.bankAddrProvince = bankAddrProvince;
    }

    public Integer getBankAddrCity() {
        return bankAddrCity;
    }

    public void setBankAddrCity(Integer bankAddrCity) {
        this.bankAddrCity = bankAddrCity;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankSubName() {
        return bankSubName;
    }

    public void setBankSubName(String bankSubName) {
        this.bankSubName = bankSubName == null ? null : bankSubName.trim();
    }

    public String getBankSubCode() {
        return bankSubCode;
    }

    public void setBankSubCode(String bankSubCode) {
        this.bankSubCode = bankSubCode == null ? null : bankSubCode.trim();
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }

    public String getModtime() {
        return modtime;
    }

    public void setModtime(String modtime) {
        this.modtime = modtime == null ? null : modtime.trim();
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions == null ? null : instructions.trim();
    }

    public String getPlatformname() {
        return platformname;
    }

    public void setPlatformname(String platformname) {
        this.platformname = platformname == null ? null : platformname.trim();
    }
}