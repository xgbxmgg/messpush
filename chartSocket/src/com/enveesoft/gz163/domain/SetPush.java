package com.enveesoft.gz163.domain;

import java.io.Serializable;

public class SetPush implements Serializable {
	private static final long serialVersionUID = 5821363072659777665L;
	private String imsi;

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
}
