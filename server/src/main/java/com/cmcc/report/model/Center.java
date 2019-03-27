package com.cmcc.report.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "center")
public class Center {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "centerId")
	private int centerId;// 中心ID
	@Column(name = "centerName")
	private String centerName;// 中心名称
	@Column(name = "publishTime")
	private Date publishTime;//中心下项目发布最新时间
	@Transient
	private Integer hasUnRead;//是否有未读消息 0：无   1：有
	public int getCenterId() {
		return centerId;
	}
	public void setCenterId(int centerId) {
		this.centerId = centerId;
	}
	public String getCenterName() {
		return centerName;
	}
	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}
	public Date getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
	public Integer getHasUnRead() {
		return hasUnRead;
	}
	public void setHasUnRead(Integer hasUnRead) {
		this.hasUnRead = hasUnRead;
	}
	public Center(int centerId, String centerName, Date publishTime, Integer hasUnRead) {
		super();
		this.centerId = centerId;
		this.centerName = centerName;
		this.publishTime = publishTime;
		this.hasUnRead = hasUnRead;
	}
	public Center() {
		super();
	}
	@Override
	public String toString() {
		return "Center [centerId=" + centerId + ", centerName=" + centerName + ", publishTime=" + publishTime
				+ ", hasUnRead=" + hasUnRead + "]";
	}
	

}
