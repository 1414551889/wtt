package com.cmcc.report.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author HF
 */
@Entity
@Table(name = "onlinetracecontent")
public class OnlineTraceContent implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -3335041249422235702L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;// ID
	@Column(name = "traceBillId")
	private Long traceBillId;// ID
	@Column(name = "projectWork")
	private String projectWork;
	@Column(name = "content")
	private String content;// 上线内容
	@Column(name = "status")
	private String status;// 状态
	@Column(name = "projectManager")
	private String projectManager;
	@Column(name = "centerName")
	private String centerName;
	@Column(name = "centerId")
	private Integer centerId;
	@Column(name = "remark")
	private String remark;// 备注
	@Column(name = "ischeck")
	private Integer ischeck;// 是否审核
	@Column(name = "updater")
	private Integer updater;
	@Column(name = "updateTime")
	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTraceBillId() {
		return traceBillId;
	}

	public void setTraceBillId(Long traceBillId) {
		this.traceBillId = traceBillId;
	}

	public String getProjectWork() {
		return projectWork;
	}

	public void setProjectWork(String projectWork) {
		this.projectWork = projectWork;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIscheck() {
		return ischeck;
	}

	public void setIscheck(Integer ischeck) {
		this.ischeck = ischeck;
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

	public String getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}
}
