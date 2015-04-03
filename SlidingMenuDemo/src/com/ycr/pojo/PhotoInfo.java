package com.ycr.pojo;

import java.io.Serializable;

public class PhotoInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4591821044082108565L;
	private String imageUrl;//	图片url	http://1.Png
	private String sImageUrl;//	缩略图	http://1.Png
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getsImageUrl() {
		return sImageUrl;
	}
	public void setsImageUrl(String sImageUrl) {
		this.sImageUrl = sImageUrl;
	}

}
