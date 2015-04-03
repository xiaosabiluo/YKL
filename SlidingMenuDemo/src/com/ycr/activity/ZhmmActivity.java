/*
 * $filename: AboutActivity.java,v $
 * $Date: 2013-12-23  $
 * Copyright (C) ZhengHaibo, Inc. All rights reserved.
 * This software is Made by Zhenghaibo.
 */
package com.ycr.activity;




import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.indoorclub.girl.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ycr.pojo.App;
import com.ycr.pojo.HttpResult;
import com.ycr.pojo.UserInfo;
import com.ycr.util.PublicJsonParse;

public class ZhmmActivity extends Activity implements OnClickListener{

	private ImageButton back;
	private ImageButton add;
	private ImageButton huoqu;
	private TextView title;
	private EditText phone;
	private EditText code;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhmm);
		initView();
	}
	private void initView() {
		back=(ImageButton) findViewById(R.id.back);
		add=(ImageButton)findViewById(R.id.add);
		huoqu=(ImageButton)findViewById(R.id.huoqu);
		title=(TextView)findViewById(R.id.title);
		title.setText("找回密码");
		phone=(EditText)findViewById(R.id.phone);
		code=(EditText)findViewById(R.id.code);
		back.setOnClickListener(this);
		add.setOnClickListener(this);
		huoqu.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			Intent intent=new Intent(ZhmmActivity.this,LoginActivity.class);
			ZhmmActivity.this.startActivity(intent);
			this.finish();
			break;
		case R.id.add://下一步
			if(phone.getText().toString().trim().equals("")){
				Toast toast = Toast.makeText(getApplicationContext(),"请输入手机号码", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				phone.requestFocus();
			}else if(code.getText().toString().trim().equals("")){
				Toast toast = Toast.makeText(getApplicationContext(),"请输入验证码", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				code.requestFocus();
			}else{
				checkVerification();
			}
			break;
		case R.id.huoqu://获取短信
			if(!phone.getText().toString().trim().equals(""))
				getVerification();
			else{
				Toast toast = Toast.makeText(getApplicationContext(),"请输入手机号码", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				phone.requestFocus();
			}
			break;
		default:
			break;
		}
	}
	private void getVerification() {
 		RequestParams params = new RequestParams();
 		try {
 			JSONObject json=new JSONObject();
 			json.put("mobile", phone.getText().toString());
 			json.put("accountType", 2);
 			json.put("request", PublicJsonParse.getPublicParam("getVerification"));
 			params.addHeader("Content-Type", "application/json");
 			params.setBodyEntity(new StringEntity(json.toString()));
 		} catch (JSONException e) {
 			e.printStackTrace(); 
 		}
 		catch (UnsupportedEncodingException e) {
 			e.printStackTrace();
 		}
 		HttpUtils http = new HttpUtils();
 		http.configTimeout(30000);
 		http.send(HttpRequest.HttpMethod.POST,App.service+"getVerification",params,
 		    new RequestCallBack<String>(){

 		        @Override
 		        public void onStart() {
 		        }

 		        @Override
 		        public void onLoading(long total, long current, boolean isUploading) {
 		            
 		        }
 		        @Override
 		        public void onSuccess(ResponseInfo<String> responseInfo) {
 		        	Gson gson=new GsonBuilder().serializeNulls().create();
 		        	Type typeToken = new TypeToken<HttpResult<UserInfo>>(){}.getType();
 		        	String jsonElement=responseInfo.result.replace("[]", "null");
 		        	HttpResult<UserInfo> result=gson.fromJson(jsonElement,typeToken);
 		        	if(result.isResult()){
 		        		
 		        	}else{
 		        		Toast toast = Toast.makeText(getApplicationContext(),"获取效验码失败", Toast.LENGTH_SHORT);
 						toast.setGravity(Gravity.CENTER, 0, 0);
 						toast.show();
 		        	}
 		        }

 		        @Override
 		        public void onFailure(HttpException error, String msg) {
 		        	Toast toast = Toast.makeText(getApplicationContext(),"网络异常", Toast.LENGTH_SHORT);
 					toast.setGravity(Gravity.CENTER, 0, 0);
 					toast.show();
 		        }
 		});
 	}
	private void checkVerification() {
 		RequestParams params = new RequestParams();
 		try {
 			JSONObject json=new JSONObject();
 			json.put("mobile", phone.getText().toString().trim());
 			json.put("accountType", 2);
 			json.put("verification", code.getText().toString().trim());
 			json.put("request", PublicJsonParse.getPublicParam("checkVerification"));
 			params.addHeader("Content-Type", "application/json");
 			params.setBodyEntity(new StringEntity(json.toString()));
 		} catch (JSONException e) {
 			e.printStackTrace(); 
 		}
 		catch (UnsupportedEncodingException e) {
 			e.printStackTrace();
 		}
 		HttpUtils http = new HttpUtils();
 		http.configTimeout(30000);
 		http.send(HttpRequest.HttpMethod.POST,App.service+"checkVerification",params,
 		    new RequestCallBack<String>(){

 		        @Override
 		        public void onStart() {
 		        }

 		        @Override
 		        public void onLoading(long total, long current, boolean isUploading) {
 		            
 		        }
 		        @Override
 		        public void onSuccess(ResponseInfo<String> responseInfo) {
 		        	Gson gson=new GsonBuilder().serializeNulls().create();
 		        	Type typeToken = new TypeToken<HttpResult<UserInfo>>(){}.getType();
 		        	String jsonElement=responseInfo.result.replace("[]", "null");
 		        	HttpResult<UserInfo> result=gson.fromJson(jsonElement,typeToken);
 		        	if(result.isResult()){
 		   				Intent intent=new Intent(ZhmmActivity.this,ZhXgmmActivity.class);
 		   				intent.putExtra("mobile",phone.getText().toString().trim());
 		   				ZhmmActivity.this.startActivity(intent);
 		   				finish();
 		        	}else{
 		        		Toast toast = Toast.makeText(getApplicationContext(),"效验码输入错误", Toast.LENGTH_SHORT);
 						toast.setGravity(Gravity.CENTER, 0, 0);
 						toast.show();
 		        	}
 		        }

 		        @Override
 		        public void onFailure(HttpException error, String msg) {
 		        	Toast toast = Toast.makeText(getApplicationContext(),"网络异常", Toast.LENGTH_SHORT);
 					toast.setGravity(Gravity.CENTER, 0, 0);
 					toast.show();
 		        }
 		});
 	}
}
