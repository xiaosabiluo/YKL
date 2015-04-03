package com.ycr.pojo;

import java.io.Serializable;

public class MesCountInfo implements Serializable{
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 4258218420053926920L;
	private int count;
	private int type;
}
