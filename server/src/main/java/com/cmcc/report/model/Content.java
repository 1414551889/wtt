package com.cmcc.report.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author HF
 */
@Entity
@Table(name = "content")
public class Content implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3335041249422235702L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "contentId")
	private Long contentId;// 内容ID
	@Column(name = "billId")
	private Integer billId;// 清单ID
	@Column(name = "isImportant")
	private Integer isImportant;// 是否重要  1：重要  0：不重要
	@Column(name = "projectName")
	private String projectName;// 项目名称（日报，半周报标题）
	@Column(name = "workProject")
	private String workProject;// 工作项目
	@Column(name = "workContent")
	private String workContent;// 工作内容
	@Column(name = "overState")
	private String overState;// 完成状况（日报，半周报内容）
	@Column(name = "responsible")
	private String responsible;// 责任人
	@Column(name = "cooperator")
	private String cooperator;// 配合人
	@Column(name = "timeLimit")
	private String timeLimit;// 完成时限
	@Column(name = "isRisk")
	private Integer isRisk;// 是否有风险  1 有  0没有(日报，半周报默认为0)
	@Column(name = "riskSituation")
	private String riskSituation;// 风险情况
	@Column(name = "remark")
	private String remark;// 备注
	@Column(name = "centerName")
	private String centerName;// 所属中心名称
	@Column(name = "centerId")
	private Integer centerId;// 所属中心Id
	@Column(name = "ischeck")
	private Integer ischeck;// 是否审核
	@Column(name = "updater")
	private Integer updater;// 修改人（最后）
	@Column(name = "updateTime")
	private Date updateTime;// 修改时间
	
	@Column(name = "isDelete")
	private Integer isDelete;// 是否删除   0 未删除  1删除
	
	@Column(name = "isFocused")
	private Integer isFocused;// 是否关注   0未关注  1关注
	
	@Column(name = "sum")
	private Integer sum;  //重点*8+风险*4+关注*2
	
	@Column(name = "isPublish")
	private Integer isPublish;//是否发布    1发布   其他未发布
	
	@Column(name = "publishTime")
	private Date publishTime;// 发布时间

	@Column(name = "selecttime")
	private String selecttime;// 填写日期
	
	@Column(name = "leaders")
	private String leaders;//@领导人
	
	@Column(name = "projectType")
	private int projectType;	//类型：1项目推进2日报3半周报
	
	@Column(name = "picUrl")
	private String picUrl;//项目推进清单_缩略图
	
	@Transient
	private String completionText;//完成情况，纯文本
	
	@Transient
	private String projectImage;//项目图片
	@Transient
	private String picture;//项目图片
	
	@Transient
	private Integer isRead;//是否已读  0:未读 1：已读
	
	public int getProjectType() {
		return projectType;
	}
	public void setProjectType(int projectType) {
		this.projectType = projectType;
	}
	public Integer getIsRead() {
		return isRead;
	}
	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}
	public String getLeaders() {
		return leaders;
	}
	public void setLeaders(String leaders) {
		this.leaders = leaders;
	}
	public Integer getIsPublish() {
		return isPublish;
	}
	public void setIsPublish(Integer isPublish) {
		this.isPublish = isPublish;
	}
	public Date getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
	public Integer getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	public Integer getIsFocused() {
		return isFocused;
	}
	public void setIsFocused(Integer isFocused) {
		this.isFocused = isFocused;
	}
	public Integer getSum() {
		return sum;
	}
	public void setSum(Integer sum) {
		this.sum = sum;
	}
	public Long getContentId() {
		return contentId;
	}
	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public Integer getBillId() {
		return billId;
	}
	public void setBillId(Integer billId) {
		this.billId = billId;
	}
	public Integer getIsImportant() {
		return isImportant;
	}
	public void setIsImportant(Integer isImportant) {
		this.isImportant = isImportant;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getWorkProject() {
		return workProject;
	}
	public void setWorkProject(String workProject) {
		this.workProject = workProject;
	}
	public String getWorkContent() {
		return workContent;
	}
	public void setWorkContent(String workContent) {
		this.workContent = workContent;
	}
	public String getOverState() {
		return overState;
	}
	public void setOverState(String overState) {
		this.overState = overState;
	}
	public String getResponsible() {
		return responsible;
	}
	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}
	public String getCooperator() {
		return cooperator;
	}
	public void setCooperator(String cooperator) {
		this.cooperator = cooperator;
	}
	public String getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(String timeLimit) {
		this.timeLimit = timeLimit;
	}
	public Integer getIsRisk() {
		return isRisk;
	}
	public void setIsRisk(Integer isRisk) {
		this.isRisk = isRisk;
	}
	public String getRiskSituation() {
		return riskSituation;
	}
	public void setRiskSituation(String riskSituation) {
		this.riskSituation = riskSituation;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getSelecttime() {
		return selecttime;
	}
	public void setSelecttime(String selecttime) {
		this.selecttime = selecttime;
	}
	public String getCompletionText() {
		return completionText;
	}
	public void setCompletionText(String completionText) {
		this.completionText = completionText;
	}
	public String getProjectImage() {
		return projectImage;
	}
	public void setProjectImage(String projectImage) {
		this.projectImage = projectImage;
	}
	
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	public Content(Long contentId, Integer billId, Integer isImportant, String projectName, String workProject,
			String workContent, String overState, String responsible, String cooperator, String timeLimit,
			Integer isRisk, String riskSituation, String remark, String centerName, Integer centerId, Integer ischeck,
			Integer updater, Date updateTime, Integer isDelete, Integer isFocused, Integer sum, Integer isPublish,
			Date publishTime, String selecttime, String leaders, int projectType, String picUrl, String completionText,
			String projectImage, String picture, Integer isRead) {
		super();
		this.contentId = contentId;
		this.billId = billId;
		this.isImportant = isImportant;
		this.projectName = projectName;
		this.workProject = workProject;
		this.workContent = workContent;
		this.overState = overState;
		this.responsible = responsible;
		this.cooperator = cooperator;
		this.timeLimit = timeLimit;
		this.isRisk = isRisk;
		this.riskSituation = riskSituation;
		this.remark = remark;
		this.centerName = centerName;
		this.centerId = centerId;
		this.ischeck = ischeck;
		this.updater = updater;
		this.updateTime = updateTime;
		this.isDelete = isDelete;
		this.isFocused = isFocused;
		this.sum = sum;
		this.isPublish = isPublish;
		this.publishTime = publishTime;
		this.selecttime = selecttime;
		this.leaders = leaders;
		this.projectType = projectType;
		this.picUrl = picUrl;
		this.completionText = completionText;
		this.projectImage = projectImage;
		this.picture = picture;
		this.isRead = isRead;
	}
	
	public Content() {
		super();
	}

}
