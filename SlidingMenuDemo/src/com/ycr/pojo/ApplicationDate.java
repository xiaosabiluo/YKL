package com.ycr.pojo;

import java.io.Serializable;

import com.baidu.location.BDLocation;
import com.ycr.slidemenu.MainActivity;

public class ApplicationDate implements Serializable{
	private static final long serialVersionUID = 7640051774724449372L;
	/**  
	* 单例对象实例  
	*/    
	private static ApplicationDate instance = null;
	private MainActivity info;
	private BDLocation location;
	private  String orderId;
	public static ApplicationDate getInstance() {    
		if (instance == null) {                            
		    instance = new ApplicationDate();         
		}    
		return instance;    
	}
	public MainActivity getInfo() {
		return info;
	}
	public void setInfo(MainActivity info) {
		this.info = info;
	}
	public BDLocation getLocation() {
		return location;
	}
	public void setLocation(BDLocation location) {
		this.location = location;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
}
