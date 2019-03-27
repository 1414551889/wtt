package com.cmcc.report.model;

import com.cmcc.report.util.excel.annotations.ExcelField;
import com.cmcc.report.util.excel.annotations.ExcelObject;
import com.cmcc.report.util.excel.annotations.ParseType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author HF
 */
@ExcelObject(parseType = ParseType.ROW, start = 3)
public class OnlinePlanContentImport  {
	/**
	 *
	 */
	private static final long serialVersionUID = -3335041249422235702L;
	@ExcelField(position = 1)
	private String onlineTime;// 备注
	@ExcelField(position = 3)
	private String projectNumber;// 内容ID
	@ExcelField(position = 4)
	private String type;//分类
	@ExcelField(position = 5)
	private String content;// 任务
	@ExcelField(position = 6)
	private String project;// 所属项目
	@ExcelField(position = 7)
	private String testTime;//测试时间
	@ExcelField(position = 8)
	private String productStaff;// 产品人员
	@ExcelField(position = 9)
	private String developStaff;// 开发人员
	@ExcelField(position = 10)
	private String testStaff;//
	@ExcelField(position = 11)
	private String remark;// 备注
	public String getType() {
		return type;
	}

	public String getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(String onlineTime) {
		this.onlineTime = onlineTime;
	}

	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
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
