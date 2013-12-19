package com.android.shop.sys;

import java.io.Serializable;

public class Information implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int command;
	private int result;
	private Object object;

	public Information(int command, int result, Object object) {
		this.command = command;
		this.result = result;
		this.object = object;
	}

	public Integer getCommand() {
		return command;
	}

	public void setCommand(Integer command) {
		this.command = command;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
