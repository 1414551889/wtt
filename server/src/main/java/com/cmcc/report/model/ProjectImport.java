package com.cmcc.report.model;


import com.cmcc.report.util.excel.annotations.ExcelField;
import com.cmcc.report.util.excel.annotations.ExcelObject;
import com.cmcc.report.util.excel.annotations.ParseType;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author huwenli
 * @time:
 * @describe:用户导入模板
 */
@ExcelObject(parseType = ParseType.ROW, start = 3)
public class ProjectImport {
	@ExcelField(position = 1)
	private Long id;// Id
	@ExcelField(position = 2)
	private String selecttime;// 填写日期
	@ExcelField(position = 3)
	private String isImportant;// 是否重要 1重要 0不重要
	@ExcelField(position = 4)
	private String centerName;// 所属中心名称
	@ExcelField(position = 5)
	private String projectName;// 项目名称
	@ExcelField(position = 6)
	private String workProject;// 工作项目
	@ExcelField(position = 7)
	private String workContent;// 工作内容
	@ExcelField(position = 8)
	private String overState;// 完成状况
	@ExcelField(position = 9)
	private String responsible;// 责任人
	@ExcelField(position = 10)
	private String cooperator;// 配合人
	@ExcelField(position = 11)
	private String timeLimit;// 完成时限
	@ExcelField(position = 12)
	private String isRisk;// 是否存在风险 1是 0否
	@ExcelField(position = 13)
	private String riskSituation;// 风险情况
	@ExcelField(position = 14)
	private String remark;// 备注
	public ProjectImport() {
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSelecttime() {
		return selecttime;
	}
	public void setSelecttime(String selecttime) {
		this.selecttime = selecttime;
	}
	public String getIsImportant() {
		return isImportant;
	}
	public void setIsImportant(String isImportant) {
		this.isImportant = isImportant;
	}
	public String getCenterName() {
		return centerName;
	}
	public void setCenterName(String centerName) {
		this.centerName = centerName;
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
	public String getIsRisk() {
		return isRisk;
	}
	public void setIsRisk(String isRisk) {
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
}
