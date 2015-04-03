package com.ycr.pojo;

import java.io.Serializable;
import java.util.List;

public class CompanysList implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6680694633100049224L;
	private int type;//订单类型1=历史订2=当天订单
	private List<CompanysInfo> companys;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<CompanysInfo> getCompanys() {
		return companys;
	}
	public void setCompanys(List<CompanysInfo> companys) {
		this.companys = companys;
	}
	
}
