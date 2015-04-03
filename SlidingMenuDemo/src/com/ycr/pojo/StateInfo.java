package com.ycr.pojo;

import java.io.Serializable;

public class StateInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1041980920128230607L;
	private String accountId;
	private String isWork="1";
	private String isGoOut="2";
	private String isUnClear="1";
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getIsWork() {
		return isWork;
	}
	public void setIsWork(String isWork) {
		this.isWork = isWork;
	}
	public String getIsGoOut() {
		return isGoOut;
	}
	public void setIsGoOut(String isGoOut) {
		this.isGoOut = isGoOut;
	}
	public String getIsUnClear() {
		return isUnClear;
	}
	public void setIsUnClear(String isUnClear) {
		this.isUnClear = isUnClear;
	}
}
