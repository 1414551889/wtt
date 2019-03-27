package com.cmcc.report.model;

import com.cmcc.report.util.excel.annotations.ExcelField;
import com.cmcc.report.util.excel.annotations.ExcelObject;
import com.cmcc.report.util.excel.annotations.ParseType;

/**
 * @author HF
 */
@ExcelObject(parseType = ParseType.ROW, start = 3)
public class OnlineTraceContentImport {
	/**
	 *
	 */
	private static final long serialVersionUID = -3335041249422235702L;
	@ExcelField(position = 2)
	private String centerName;// 备注
	@ExcelField(position = 3)
	private String projectWork;
	@ExcelField(position = 4)
	private String content;
	@ExcelField(position = 5)
	private String status;
	@ExcelField(position = 6)
	private String projectManager;
	@ExcelField(position = 7)
	private String remark;// 备注

	public String getCenterName() {
		return centerName;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
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

	public String getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
