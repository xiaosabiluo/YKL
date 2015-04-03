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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
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
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ycr.pojo.App;
import com.ycr.pojo.HttpResultList;
import com.ycr.pojo.UserInfo;
import com.ycr.slidemenu.MainActivity;
import com.ycr.util.GPSHelp;
import com.ycr.util.PublicJsonParse;
import com.indoorclub.girl.R;

public class JrycActivity extends Activity implements OnClickListener{

	private ImageButton back;
	private ImageButton add;
	private TextView title;
	private EditText textVal;
	private String clubId;
	private String type;//判断是从那个地方进来的；
	private String managerId;
	private String isHaveCompany;
	private boolean isRegidter=false;
	private SharedPreferences sharedPreferences;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jryc);
		sharedPreferences=this.getSharedPreferences("UserInfoFile", Context.MODE_PRIVATE);//共享数据
		initView();
	}
	private void initView() {
		Intent intent=getIntent();
		clubId=intent.getStringExtra("clubId");
		type=intent.getStringExtra("type");
		managerId=intent.getStringExtra("managerId");
		isRegidter=intent.getBooleanExtra("isRegidter", false);
		isHaveCompany=intent.getStringExtra("isHaveCompany");
		back=(ImageButton) findViewById(R.id.back);
		add=(ImageButton)findViewById(R.id.add);
		textVal=(EditText)findViewById(R.id.textVal);
		title=(TextView)findViewById(R.id.title);
		if(type!=null)
		{
			if(type.equals(""))
				title.setText("加入夜场");
			else if(type.equals("1")){
				title.setText("昵称");
				textVal.setText(App.user.getName());
			}
			else{
				title.setText("个人说明");
				textVal.setText(App.user.getRemark());
			}
		}else
			title.setText("加入夜场");
		back.setOnClickListener(this);
		add.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.add://加入夜场
			if(type==null||type.equals(""))
				JoinOrQuitCompany();
			else{
				String mes=type.equals("1")?"昵称不能为空":"个人说明不能为空";
				String name=textVal.getText().toString();
				if(textVal.getText().toString().replace(" ", "").equals("")){
					Toast toast = Toast.makeText(getApplicationContext(),mes, Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}else{
					if(type.equals("1"))
						App.user.setName(name);
					else
						App.user.setRemark(name);
					if(isRegidter){
						Intent intent=new Intent();
						MainActivity.status=0;
						intent.setClass(JrycActivity.this,RegidterGrzlActivity.class);
						intent.putExtra("name", name);
						JrycActivity.this.setResult(Integer.parseInt(type), intent);  
					}else{
						Intent intent=new Intent();
						Bundle bundle = new Bundle();  
						bundle.putString("name", name);
						intent.putExtras(bundle);
						this.setResult(Integer.parseInt(type), intent); 
					}
					finish();
				}
			}
			break;
		default:
			break;
		}
	}
	private void JoinOrQuitCompany() {
		LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
    	Location location=GPSHelp.Location(locationManager);
 		RequestParams params = new RequestParams();
 		try {
 			JSONObject json=new JSONObject();
 			json.put("accountId", sharedPreferences.getString("id", ""));
 			json.put("clubId", clubId);
 			json.put("remark",textVal.getText().toString());
 			json.put("type", 1);
 			json.put("isHaveCompany", isHaveCompany);
 			json.put("managerId", managerId);
 			json.put("request", PublicJsonParse.getPublicParam("joinOrQuitCompany"));
 			if(location!=null){
 				json.put("longitude",location.getLongitude());
 				json.put("latitude", location.getLatitude());
 			}
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
 		http.send(HttpRequest.HttpMethod.POST,App.service+"joinOrQuitCompany",params,
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
 		        	Type typeToken = new TypeToken<HttpResultList<UserInfo>>(){}.getType();
 		        	String jsonElement=responseInfo.result.replace("[]", "null");
 		        	HttpResultList<UserInfo> result=gson.fromJson(jsonElement,typeToken);
 		        	if(result.isResult()){
 		        		Toast toast = Toast.makeText(getApplicationContext(),"已成功发送请求", Toast.LENGTH_SHORT);
 						toast.setGravity(Gravity.CENTER, 0, 0);
 						toast.show();
 						Intent intent=new Intent();
 						intent.setClass(JrycActivity.this,MainActivity.class);
 						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 						intent.putExtra("status",R.id.tjycBtnLayout);
 						JrycActivity.this.startActivity(intent);
 						finish();
 		        	}else{
 		        		Toast toast = Toast.makeText(getApplicationContext(),result.getError(), Toast.LENGTH_SHORT);
 						toast.setGravity(Gravity.CENTER, 0, 0);
 						toast.show();
 		        	}
 		        }

 		        @Override
 		        public void onFailure(HttpException error, String msg) {
 		        	Toast toast = Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT);
 					toast.setGravity(Gravity.CENTER, 0, 0);
 					toast.show();
 		        }
 		});
 	}
}
