package com.cmcc.report.model;

import java.util.Date;
import java.util.List;

/**
 *	微头条二期_客户端领导首页
 */
public class LeaderIndex {
	
	private Date recentTime;//中心下内容最新发布时间
	
	private int  centerId;//中心ID
	
	private String centerName;//中心名称
	
	private int hasUnRead;//是否有未读消息 0：无   1：有
	
	private List<Content> projects;//中心下内容

	public Date getRecentTime() {
		return recentTime;
	}

	public void setRecentTime(Date recentTime) {
		this.recentTime = recentTime;
	}

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

	public int getHasUnRead() {
		return hasUnRead;
	}

	public void setHasUnRead(int hasUnRead) {
		this.hasUnRead = hasUnRead;
	}

	public List<Content> getProjects() {
		return projects;
	}

	public void setProjects(List<Content> projects) {
		this.projects = projects;
	}
	
}
