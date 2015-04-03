package com.ycr.pojo;

import java.io.Serializable;
import java.util.List;

public class CompanysInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5860439400122908103L;
	private String companyId;//	公司id	1
	private String companyName;//	公司名称 	 
	private String companyLogo;//	公司logo	
	private String companyPhotoUrl;//	公司图片	
	private String companyNum;//	公司的业务员人数（业务员大于100（包含100）的为hot）	
	private String companyDistance;//	公司距离	
	private String companyAddress;//	公司地址	
	private boolean isNew;//	是否显示new图表（一个星期内加入的为new）true = 显示 false = 不显示	
	private String description;//	公司描述	
	private String isJoin;//是否已加入
	private String isOwner;//	是否自己公司1=是0否	0
	private String lowerConsume;//	最低价格	600
	private String consume;//	公司消费	500，600，700
	private String isHaveCompany;//	是否有公司归属1=有0=无	0
	private String isHavingJoin;//	1小时内是否已加入过公司，为true时app加入按钮不显示1=是0=否	false
	private String clubId;//	公司Id	1
	private String fee;//	消费	500-700
	private String managerName;//	业务经理名称	经理1.经理2
	private String address;//地址
	private List<PhotoInfo> photoVos;// 	公司图片	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyLogo() {
		return companyLogo;
	}
	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}
	public String getCompanyPhotoUrl() {
		return companyPhotoUrl;
	}
	public void setCompanyPhotoUrl(String companyPhotoUrl) {
		this.companyPhotoUrl = companyPhotoUrl;
	}
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getCompanyDistance() {
		return companyDistance;
	}
	public void setCompanyDistance(String companyDistance) {
		this.companyDistance = companyDistance;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIsOwner() {
		return isOwner;
	}
	public void setIsOwner(String isOwner) {
		this.isOwner = isOwner;
	}
	public String getLowerConsume() {
		return lowerConsume;
	}
	public void setLowerConsume(String lowerConsume) {
		this.lowerConsume = lowerConsume;
	}
	public String getConsume() {
		return consume;
	}
	public void setConsume(String consume) {
		this.consume = consume;
	}
	public String getIsHaveCompany() {
		return isHaveCompany;
	}
	public void setIsHaveCompany(String isHaveCompany) {
		this.isHaveCompany = isHaveCompany;
	}
	public String getIsHavingJoin() {
		return isHavingJoin;
	}
	public void setIsHavingJoin(String isHavingJoin) {
		this.isHavingJoin = isHavingJoin;
	}
	public String getClubId() {
		return clubId;
	}
	public void setClubId(String clubId) {
		this.clubId = clubId;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<PhotoInfo> getPhotoVos() {
		return photoVos;
	}
	public void setPhotoVos(List<PhotoInfo> photoVos) {
		this.photoVos = photoVos;
	}
	public String getIsJoin() {
		return isJoin;
	}
	public void setIsJoin(String isJoin) {
		this.isJoin = isJoin;
	}
	
}
