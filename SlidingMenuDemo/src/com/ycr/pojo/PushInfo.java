package com.ycr.pojo;

import java.io.Serializable;

public class PushInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -852119845218829422L;
	private String type;
	private String content;
	private String param;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	
}
