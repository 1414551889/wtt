package com.cmcc.report.util;
import java.io.Serializable;
import java.util.List;



public class Page<T> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3488717183867777303L;
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
	//结果集
	private List<T> list;
	public Page() {
	}
	public Page(int pageCount, int pageIndex, int pageSize, List<T> list) {
		this.pageCount = pageCount;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.list = list;
	}
	public int getPageCount() {
		int count=0;
		if(reultAllCount !=0){
			if(reultAllCount % pageSize==0){
				count=reultAllCount/pageSize;
			}else{
				
				count=reultAllCount/pageSize+1;
			}
		}
		return count;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public int getReultCount() {
		return this.list.size();
	}
	public int getReultAllCount() {
		return reultAllCount;
	}
	public void setReultAllCount(int reultAllCount) {
		this.reultAllCount = reultAllCount;
	}
	public void setReultCount(int reultCount) {
		this.reultCount = reultCount;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
