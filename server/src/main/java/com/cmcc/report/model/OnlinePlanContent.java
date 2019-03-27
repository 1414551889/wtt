package com.cmcc.report.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author HF
 */
@Entity
@Table(name = "onlinePlanContent")
public class OnlinePlanContent implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -3335041249422235702L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;// ID
	@Column(name = "projectNumber")
	private String projectNumber;
	@Column(name = "type")
	private String type;//分类
	@Column(name = "content")
	private String content;// 任务
	@Column(name = "project")
	private String project;// 所属项目
	@Column(name = "centerName")
	private String centerName;
	@Column(name = "centerId")
	private Integer centerId;
	@Column(name = "testTime")
	private String testTime;//测试时间
	@Column(name = "productStaff")
	private String productStaff;// 产品人员
	@Column(name = "developStaff")
	private String developStaff;// 开发人员
	@Column(name = "testStaff")
	private String testStaff;//
	@Column(name = "onlineTime")
	private String onlineTime;// 备注
	@Column(name = "remark")
	private String remark;// 备注
	@Column(name = "onlinePlanBillId")
	private Long onlinePlanBillId;
	@Column(name = "ischeck")
	private Integer ischeck;// 是否审核
	@Column(name = "updater")
	private Integer updater;
	@Column(name = "updateTime")
	private Date updateTime;

	public Integer getIscheck() {
		return ischeck;
	}

	public void setIscheck(Integer ischeck) {
		this.ischeck = ischeck;
	}

	public String getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(String onlineTime) {
		this.onlineTime = onlineTime;
	}

	public String getCenterName() {
		return centerName;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

	public Integer getCenterId() {
		return centerId;
	}

	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}

	public Long getId() {
		return id;
	}

	public Long getOnlinePlanBillId() {
		return onlinePlanBillId;
	}

	public void setOnlinePlanBillId(Long onlinePlanBillId) {
		this.onlinePlanBillId = onlinePlanBillId;
	}

	public Integer getUpdater() {
		return updater;
	}

	public void setUpdater(Integer updater) {
		this.updater = updater;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getTestTime() {
		return testTime;
	}

	public void setTestTime(String testTime) {
		this.testTime = testTime;
	}

	public String getProductStaff() {
		return productStaff;
	}

	public void setProductStaff(String productStaff) {
		this.productStaff = productStaff;
	}

	public String getDevelopStaff() {
		return developStaff;
	}

	public void setDevelopStaff(String developStaff) {
		this.developStaff = developStaff;
	}

	public String getTestStaff() {
		return testStaff;
	}

	public void setTestStaff(String testStaff) {
		this.testStaff = testStaff;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
