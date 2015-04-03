package com.ycr.pojo;

import java.io.Serializable;
import java.util.List;

public class HttpResultList<T> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3409879834024494504L;
	private boolean result;
	private String error;
	private List<T> data;
	private String msgname;
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getMsgname() {
		return msgname;
	}
	public void setMsgname(String msgname) {
		this.msgname = msgname;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	
}
