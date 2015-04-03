package com.ycr.pojo;

import java.io.Serializable;

public class AppUtil implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 391608580795330651L;
	private String userId="";
	private String msgsender="login";//设置返回的方法名
	private String ostype="a";//系统类型
	private String appType="b";//业务类型终端
	private String osversion="1.0.0";//系统版本
	private String timestamp="";//时间戳
	private String transactionid="";//事物ID
	public String getMsgsender() {
		return msgsender;
	}
	public void setMsgsender(String msgsender) {
		this.msgsender = msgsender;
	}
	public String getOstype() {
		return ostype;
	}
	public void setOstype(String ostype) {
		this.ostype = ostype;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public String getOsversion() {
		return osversion;
	}
	public void setOsversion(String osversion) {
		this.osversion = osversion;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getTransactionid() {
		return transactionid;
	}
	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}