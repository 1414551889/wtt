package com.cmcc.report.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 微头条二期-用户_推进内容_是否已读关系表
 */
@Entity
@Table(name = "userAndContent")
public class UserAndContent implements Serializable{

	private static final long serialVersionUID = 1255098740861080485L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;// 主键ID
	
	@Column(name = "userId")
	private Integer userId;//用户ID
	
	@Column(name = "centerId")
	private Integer centerId;//中心ID
	
	@Column(name = "contentId")
	private Integer contentId;//项目ID(内容ID)

	@Column(name = "isRead")
	private Integer isRead;//是否已读  0:未读 1：已读

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getCenterId() {
		return centerId;
	}

	public void setCenterId(Integer centerId) {
		this.centerId = centerId;
	}

	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	public UserAndContent(Integer id, Integer userId, Integer centerId, Integer contentId, Integer isRead) {
		super();
		this.id = id;
		this.userId = userId;
		this.centerId = centerId;
		this.contentId = contentId;
		this.isRead = isRead;
	}

	public UserAndContent() {
		super();
	}
	
	
}
