package com.ycr.pojo;

import java.io.Serializable;

public class MetadataInfo implements Serializable{
	private static final long serialVersionUID = 7640051774724449372L;
	/**  
	* 单例对象实例  
	*/    
	private static MetadataInfo instance = null;
	private MenuInfo info;
	public static MetadataInfo getInstance() {    
		if (instance == null) {                            
		    instance = new MetadataInfo();         
		}    
		return instance;    
	}
	public MenuInfo getInfo() {
		return info;
	}
	public void setInfo(MenuInfo info) {
		this.info = info;
	}
	
}
