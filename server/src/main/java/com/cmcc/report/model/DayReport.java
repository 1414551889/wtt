package com.cmcc.report.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author HF
 */
@Entity
@Table(name = "dayReport")
public class DayReport implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 4371907265465657376L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;// 清单ID
	@Column(name = "title")
	private String title;// 标题
	@Column(name = "content")
	private String content;// 标题
	@Column(name = "centerId")
	private Integer centerId;// 中心Id
	@Column(name = "centerName")
	private String centerName;// 中心名称
	@Column(name = "isCheck")
	private Integer isCheck;// 审核状态  0 草稿  1 提交 2 发布  3 已阅
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
	@Column(name = "updater")
	private String updater;// 更新人ID
	@Column(name = "updateTime")
	private Date updateTime;// 提交日期

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getCreater() {
		return creater;
	}
	public void setCreater(Integer creater) {
		this.creater = creater;
	}

	public String getCheckerName() {
		return checkerName;
	}

	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	public Integer getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	

}
