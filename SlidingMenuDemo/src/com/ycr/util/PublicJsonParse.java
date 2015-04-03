package com.ycr.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.ycr.pojo.AppUtil;

public class PublicJsonParse {
	/**
	 * auther YangCR
	 * description 初始化公共参数
	 * msgsender 调用的方法名称
	 * **/
	public static JSONObject getPublicParam(String msgsender){
		AppUtil util=new AppUtil(); 
		util.setMsgsender(msgsender);
		Gson gson=new Gson();
		JSONObject json = null;
		try {
			json = new JSONObject(gson.toJson(util));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
}
