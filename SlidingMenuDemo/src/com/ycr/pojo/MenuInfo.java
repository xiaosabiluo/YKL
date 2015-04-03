package com.ycr.pojo;

import java.io.Serializable;
import java.util.HashMap;

public class MenuInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 840336715651438292L;
	private HashMap<String, HashMap<String, Object>> menus;
	private HashMap<String, HashMap<String, Object>> metadataMap;
	public HashMap<String, HashMap<String, Object>> getMenus() {
		return menus;
	}
	public void setMenus(HashMap<String, HashMap<String, Object>> menus) {
		this.menus = menus;
	}
	public HashMap<String, HashMap<String, Object>> getMetadataMap() {
		return metadataMap;
	}
	public void setMetadataMap(HashMap<String, HashMap<String, Object>> metadataMap) {
		this.metadataMap = metadataMap;
	}
	
}
