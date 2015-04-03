package com.ycr.pojo;

import java.io.Serializable;

public class MessageInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6014574311226952262L;
	private String noticeId;//	消息id
	private String noticeMessage;//	消息内容
	private String noticeTime;//	消息时间yyyyMMddHHmmss
	private String noticeMan;//	消息触发人
	public String getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
	public String getNoticeMessage() {
		return noticeMessage;
	}
	public void setNoticeMessage(String noticeMessage) {
		this.noticeMessage = noticeMessage;
	}
	public String getNoticeTime() {
		return noticeTime;
	}
	public void setNoticeTime(String noticeTime) {
		this.noticeTime = noticeTime;
	}
	public String getNoticeMan() {
		return noticeMan;
	}
	public void setNoticeMan(String noticeMan) {
		this.noticeMan = noticeMan;
	}
	
}
