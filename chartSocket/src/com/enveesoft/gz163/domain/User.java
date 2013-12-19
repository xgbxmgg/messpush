package com.enveesoft.gz163.domain;

import java.io.Serializable;

public class User implements Serializable {
	private String userId;
	private String userName;
	private String passwd;

	public User() {
	}

	public User(String userId, String userName, String passwd) {
		this.userId = userId;
		this.userName = userName;
		this.passwd = passwd;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

}
