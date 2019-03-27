package com.cmcc.report.model;

import java.sql.Timestamp;
import java.util.Date;

public class VersionInfo {
	private Integer Id;// Id
	private String update;//是否更新(1-更新;2-强制更新;其他-不更新)
	private String versionCode;//版本号(例：1.1.1)
	private String updateInfo;// 用户所属中心Id
	private String updateUrl;// 下载地址
	private Timestamp updateTime;// 更新时间
	private Integer os;// 系统
	public VersionInfo() {
		super();
	}
	public VersionInfo(Integer id, String update, String versionCode, String updateInfo, String updateUrl, Timestamp updateTime,
			Integer os) {
		super();
		this.Id = id;
		this.update = update;
		this.versionCode = versionCode;
		this.updateInfo = updateInfo;
		this.updateUrl = updateUrl;
		this.updateTime = updateTime;
		this.os = os;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getUpdate() {
		return update;
	}
	public void setUpdate(String update) {
		this.update = update;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getUpdateInfo() {
		return updateInfo;
	}
	public void setUpdateInfo(String updateInfo) {
		this.updateInfo = updateInfo;
	}
	public String getUpdateUrl() {
		return updateUrl;
	}
	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getOs() {
		return os;
	}
	public void setOs(Integer os) {
		this.os = os;
	}
}
