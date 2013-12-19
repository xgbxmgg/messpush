package com.android.shop.sys;

public enum Command {
	LINK("连接", 1), LINKRES("连接响应", 2), SENDMES("发送消息", 3);

	private String name;
	private int index;

	private Command(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public String getName(int index) {
		for(Command c:Command.values()){
			if(c.getIndex()==index){
				return c.name;
			}
		}
		return null;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
