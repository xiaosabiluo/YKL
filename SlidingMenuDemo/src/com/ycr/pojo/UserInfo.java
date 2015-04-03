package com.ycr.pojo;

import java.io.Serializable;
import java.util.List;

public class UserInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3139043038750525501L;
	private String id;//用户ID
	private String accountId;//用户ID
	private String name;//用户名
	private String type;//用户类型 1=业务经理 2=业务员
	private String phone;//手机号
	private String email;//邮箱
	private String address;//地址
	private String country;//国家
	private String province;//省份
	private String city;//城市
	private String qq;//qq 
	private String companyName;//所属公司
	private String companyId;//所属公司ID
	private String remark;//备注
	private String photoUrl;//头像url
	private String constellation;//星座
	private String birthday;//出生日期
	private String group;//用户所在的组
	private String place;//用户所在位置
	private String distance;//当前用户所在位置距离
	private String status;//上班状态 0上班1休息
	private String mobile;//手机
	private String sex="0";//性别 0女1男
	private String age;//年龄
	private String nowClubId;//当前所在公司
	private String idcard;//身份证
	private String password;//密码
	private String qrpassword;
	private String code;
	private List<String> medias;//图片集合
	private List<CompanysInfo> companys;//已加入的公司
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public String getConstellation() {
		return constellation;
	}
	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getNowClubId() {
		return nowClubId;
	}
	public void setNowClubId(String nowClubId) {
		this.nowClubId = nowClubId;
	}
	public List<CompanysInfo> getCompanys() {
		return companys;
	}
	public void setCompanys(List<CompanysInfo> companys) {
		this.companys = companys;
	}
	public List<String> getMedias() {
		return medias;
	}
	public void setMedias(List<String> medias) {
		this.medias = medias;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getQrpassword() {
		return qrpassword;
	}
	public void setQrpassword(String qrpassword) {
		this.qrpassword = qrpassword;
	}
	
}
