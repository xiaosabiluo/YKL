package com.ycr.pojo;

import java.io.Serializable;

import android.graphics.Bitmap;

public class MediaInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6112189706863000129L;
	private String mediaId;//	媒体id	1
	private String mediaUrl;//	媒体路径	D:/img/a.png
	private String mediaName;//	媒体名称	生活照
	private String mediaDetail;//	媒体描述	很好看
	private String mediaTab;//	媒体标签	
	private String mediaSeq;//	媒体顺序	1
	private String mediaSUrl;//	媒体缩略图路径	D:/img/a.png
	private String isDefault;//	是否默认1=是0=否	1
	private String error;
	private Bitmap bitmap;
	private boolean isSelected = false;
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public String getMediaUrl() {
		return mediaUrl;
	}
	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}
	public String getMediaName() {
		return mediaName;
	}
	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}
	public String getMediaDetail() {
		return mediaDetail;
	}
	public void setMediaDetail(String mediaDetail) {
		this.mediaDetail = mediaDetail;
	}
	public String getMediaTab() {
		return mediaTab;
	}
	public void setMediaTab(String mediaTab) {
		this.mediaTab = mediaTab;
	}
	public String getMediaSeq() {
		return mediaSeq;
	}
	public void setMediaSeq(String mediaSeq) {
		this.mediaSeq = mediaSeq;
	}
	public String getMediaSUrl() {
		return mediaSUrl;
	}
	public void setMediaSUrl(String mediaSUrl) {
		this.mediaSUrl = mediaSUrl;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
}
