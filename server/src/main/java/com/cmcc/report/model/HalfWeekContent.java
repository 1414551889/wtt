package com.cmcc.report.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author HF
 */
@Entity
@Table(name = "halfWeekContent")
public class HalfWeekContent implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -3335041249422235702L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;// 内容ID
	@Column(name = "billId")
	private Long billId;// 内容ID
	@Column(name = "projectName")
	private String projectName;// 项目名称
	@Column(name = "subProject")
	private String subProject;// 工作项目
	@Column(name = "workContent")
	private String workContent;// 工作内容
	@Column(name = "centerName")
	private String centerName;
	@Column(name = "centerId")
	private Integer centerId;
	@Column(name = "creater")
	private Integer creater;
	@Column(name = "ischeck")
	private Integer ischeck;// 是否审核
	@Column(name = "updater")
	private Integer updater;// 修改人（最后）
	@Column(name = "updateTime")
	private Date updateTime;// 修改时间
	@Column(name = "subTime")
	private Date subTime;// 修改时间

	public Date getSubTime() {
		return subTime;
	}

	public void setSubTime(Date subTime) {
		this.subTime = subTime;
	}

	public Integer getCreater() {
		return creater;
	}

	public void setCreater(Integer creater) {
		this.creater = creater;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getSubProject() {
		return subProject;
	}

	public void setSubProject(String subProject) {
		this.subProject = subProject;
	}

	public String getWorkContent() {
		return workContent;
	}

	public void setWorkContent(String workContent) {
		this.workContent = workContent;
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


}
