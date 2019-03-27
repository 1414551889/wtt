package com.cmcc.report.model;


import com.cmcc.report.util.excel.annotations.ExcelField;
import com.cmcc.report.util.excel.annotations.ExcelObject;
import com.cmcc.report.util.excel.annotations.ParseType;

/**
 * @author huwenli
 * @time:
 * @describe:用户导入模板
 */
@ExcelObject(parseType = ParseType.ROW, start = 2)
public class HalfWeekContentImport {
	@ExcelField(position = 1)
	private Long id;// ID
	@ExcelField(position = 2)
	private String projectName;
	@ExcelField(position = 3)
	private String subProject;
	@ExcelField(position = 4)
	private String workContent;
	@ExcelField(position = 5)
	private String journalists_b;
	@ExcelField(position = 6)
	private String writeTime;
	@ExcelField(position = 7)
	private String aggregated;
	public HalfWeekContentImport() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getJournalists_b() {
		return journalists_b;
	}

	public void setJournalists_b(String journalists_b) {
		this.journalists_b = journalists_b;
	}

	public String getWriteTime() {
		return writeTime;
	}

	public void setWriteTime(String writeTime) {
		this.writeTime = writeTime;
	}

	public String getAggregated() {
		return aggregated;
	}

	public void setAggregated(String aggregated) {
		this.aggregated = aggregated;
	}
}
