package com.ycr.pojo;

import java.io.Serializable;

public class GoodInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4589515589462123672L;
	private String id;
	private String ycid;
	private String ycname;
	private String mony;
	private String username;
	private String phone;
	private String date;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getYcid() {
		return ycid;
	}
	public void setYcid(String ycid) {
		this.ycid = ycid;
	}
	public String getYcname() {
		return ycname;
	}
	public void setYcname(String ycname) {
		this.ycname = ycname;
	}
	public String getMony() {
		return mony;
	}
	public void setMony(String mony) {
		this.mony = mony;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
