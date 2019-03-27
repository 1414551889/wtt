package com.cmcc.report.model;

public class User {
	private int userId;// 用户Id
	private String account;// 用户账号
	private String password;// 账号密码
	private int centerId;// 用户所属中心Id
	private String center;// 用户所属中心
	private String name;// 用户所属中心
	private int role;// 用户角色 1 录入人 2 上报人 3 领导

	public User(int userId, String account, String password,int centerId, String center, int role,String name) {
		super();
		this.userId = userId;
		this.name = name;
		this.account = account;
		this.password = password;
		this.center = center;
		this.role = role;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getCenterId() {
		return centerId;
	}

	public void setCenterId(int centerId) {
		this.centerId = centerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
