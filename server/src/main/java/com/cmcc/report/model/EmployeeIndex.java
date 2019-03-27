package com.cmcc.report.model;

import java.util.List;

/**
 *	微头条二期_客户端普通首页（中心项目列表）
 */
public class EmployeeIndex {

	//总页数  
	private int pageCount;
	//当前第几页
	private int pageIndex;
	//每页显示数
	private int pageSize;
	//当前页返回条数
	private int reultCount;
	//总数据条数
	private int reultAllCount;
	//中心内容
	private List<Content> projects;
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getReultCount() {
		return reultCount;
	}
	public void setReultCount(int reultCount) {
		this.reultCount = reultCount;
	}
	public int getReultAllCount() {
		return reultAllCount;
	}
	public void setReultAllCount(int reultAllCount) {
		this.reultAllCount = reultAllCount;
	}
	public List<Content> getProjects() {
		return projects;
	}
	public void setProjects(List<Content> projects) {
		this.projects = projects;
	}
	
}
