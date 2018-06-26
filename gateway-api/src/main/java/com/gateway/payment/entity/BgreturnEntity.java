package com.gateway.payment.entity;

import java.util.Date;

import com.zitopay.foundation.common.entity.AbstractEntity;
import com.zitopay.foundation.common.util.BeanUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="alwaypay_bgreturn")
public class BgreturnEntity extends AbstractEntity {


	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6628095440767829033L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	private Integer type;

	private String bgreturl;

	private Integer isfeedback;

	private Integer num = 0;

	private Date resulttime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getBgreturl() {
		return bgreturl;
	}

	public void setBgreturl(String bgreturl) {
		this.bgreturl = bgreturl == null ? null : bgreturl.trim();
	}

	public Integer getIsfeedback() {
		return isfeedback;
	}

	public void setIsfeedback(Integer isfeedback) {
		this.isfeedback = isfeedback;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Date getResulttime() {
		return resulttime;
	}

	public void setResulttime(Date resulttime) {
		this.resulttime = resulttime;
	}

	@Override
	public String toString() {
		return BeanUtils.bean2JSON(this);
	}

}