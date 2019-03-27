package com.cmcc.report.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author HF
 */
@Entity
@Table(name = "bill")
public class Bill implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4371907265465657376L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "billId")
	private Long billId;// 清单ID
	@Column(name = "title")
	private String title;// 清单标题
	@Column(name = "centerId")
	private Integer centerId;// 中心Id
	@Column(name = "centerName")
	private String centerName;// 中心名称
	@Column(name = "isCheck")
	private Integer isCheck;// 清单审核状态  0 草稿  1 提交 2 发布  3 已阅
	@Column(name = "subTime")
	private Date subTime;// 提交日期
	@Column(name = "checkerId")
	private String checkerId;// 审查人Id
	@Column(name = "checkerName")
	private String checkerName;// 审查人名称
	@Column(name = "isDelete")
	private Integer isDelete;// 删除标识 0 未删除 1 删除
	@Column(name = "creater")
	private Integer creater;// 创建人ID 
	
	public Bill(Long billId, String title, Integer centerId, String centerName, Integer isCheck, Date subTime,
			String checkerId, String checkerName, Integer isDelete,Integer creater) {
		super();
		this.billId = billId;
		this.title = title;
		this.centerId = centerId;
		this.centerName = centerName;
		this.isCheck = isCheck;
		this.subTime = subTime;
		this.checkerId = checkerId;
		this.checkerName = checkerName;
		this.isDelete = isDelete;
		this.creater = creater;
	}
	public Bill() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Integer getCreater() {
		return creater;
	}
	public void setCreater(Integer creater) {
		this.creater = creater;
	}
	public Long getBillId() {
		return billId;
	}
	public void setBillId(Long billId) {
		this.billId = billId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getCenterId() {
		return centerId;
	}
	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}
	public String getCenterName() {
		return centerName;
	}
	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}
	public Integer getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(Integer isCheck) {
		this.isCheck = isCheck;
	}
	public Date getSubTime() {
		return subTime;
	}
	public void setSubTime(Date subTime) {
		this.subTime = subTime;
	}
	public String getCheckerId() {
		return checkerId;
	}
	public void setCheckerId(String checkerId) {
		this.checkerId = checkerId;
	}
	public String getCheckerName() {
		return checkerName;
	}
	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}
	public Integer getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	

}
