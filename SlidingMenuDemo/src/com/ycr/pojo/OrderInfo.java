package com.ycr.pojo;

import java.io.Serializable;

public class OrderInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4819137673450690568L;
	private String orderId;//	订单id
	private String orderTime;//	订单时间
	private String clubId;//	公司ID
	private String clubLogo;//	公司logo
	private String clubName;//	公司名称
	private String clubRoom;//	房间
	private String orderStatus;//	订单状态 1=预约2确认3取消4结束5出台
	private String workStatus;//	工作状态1=工作中0=空闲
	private String outStatus;//	出台状态1=是0=否
	private String distance;//	业务员与夜场距离
	private String nickName;//	昵称
	private String mobile;//	电话
	private String clubAddress;//	公司地址
	private String remark;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getClubId() {
		return clubId;
	}
	public void setClubId(String clubId) {
		this.clubId = clubId;
	}
	public String getClubLogo() {
		return clubLogo;
	}
	public void setClubLogo(String clubLogo) {
		this.clubLogo = clubLogo;
	}
	public String getClubName() {
		return clubName;
	}
	public void setClubName(String clubName) {
		this.clubName = clubName;
	}
	public String getClubRoom() {
		return clubRoom;
	}
	public void setClubRoom(String clubRoom) {
		this.clubRoom = clubRoom;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	public String getOutStatus() {
		return outStatus;
	}
	public void setOutStatus(String outStatus) {
		this.outStatus = outStatus;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getClubAddress() {
		return clubAddress;
	}
	public void setClubAddress(String clubAddress) {
		this.clubAddress = clubAddress;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
