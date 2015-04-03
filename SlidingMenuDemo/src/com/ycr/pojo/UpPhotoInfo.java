package com.ycr.pojo;

import java.io.Serializable;

public class UpPhotoInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4591821044082108565L;
	private String photoUrl;//
	private String cover;//
	private String pName;//
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}


}
