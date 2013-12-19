package com.enveesoft.gz163.domain;

import java.io.Serializable;

public class GetPush implements Serializable {

	private static final long serialVersionUID = 7986952784944740627L;
	// private String imsi;
	private int pushId;
	private int type;
	private String context;
	private String flag;
	private int adid;

	public GetPush() {
	}

	public GetPush(int pushId, int type, String context, String flag, int adid) {
		this.pushId = pushId;
		this.type = type;
		this.context = context;
		this.flag = flag;
		this.adid = adid;
	}

	/*
	 * public String getImsi() { return imsi; }
	 * 
	 * public void setImsi(String imsi) { this.imsi = imsi; }
	 */
	public int getPushId() {
		return pushId;
	}

	public void setPushId(int pushId) {
		this.pushId = pushId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public int getAdid() {
		return adid;
	}

	public void setAdid(int adid) {
		this.adid = adid;
	}

}
