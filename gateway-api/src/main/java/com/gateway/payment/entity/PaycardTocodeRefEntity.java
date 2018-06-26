package com.gateway.payment.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 *  码跳码 商户入驻中间信息
 */
@Table(name="zitopay_paycard_tocode_ref")
public class PaycardTocodeRefEntity implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    private Long id;

    private String personId;//商户ID

    private String md;//台牌号

    private String cd;//商户唯一标识

    private Date createTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMd() {
        return md;
    }

    public void setMd(String md) {
        this.md = md;
    }

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    @Override
    public String toString() {
        return "ZitopayPaycardTocodeRef{" +
                "id=" + id +
                ", personId='" + personId + '\'' +
                ", md='" + md + '\'' +
                ", cd='" + cd + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
