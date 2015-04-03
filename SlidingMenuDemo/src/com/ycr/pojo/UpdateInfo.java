package com.ycr.pojo;

import java.io.Serializable;

public class UpdateInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5633012045235587867L;
	private int needUpgrade;//	是否强制升级，1是，0否	1
	private int  needUpdate;//	是否有可用更新，1是，0否	1
	private String downloadUrl;//	指导用户下载客户端地址	http://test.do
	private String updateMessage;//	版本升级信息	增加好友信息
	public int getNeedUpgrade() {
		return needUpgrade;
	}
	public void setNeedUpgrade(int needUpgrade) {
		this.needUpgrade = needUpgrade;
	}
	public int getNeedUpdate() {
		return needUpdate;
	}
	public void setNeedUpdate(int needUpdate) {
		this.needUpdate = needUpdate;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getUpdateMessage() {
		return updateMessage;
	}
	public void setUpdateMessage(String updateMessage) {
		this.updateMessage = updateMessage;
	}

}
