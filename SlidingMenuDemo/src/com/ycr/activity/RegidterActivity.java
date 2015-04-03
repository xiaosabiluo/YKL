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

public class RegidterActivity extends Activity {

	private ImageButton back;
	private ImageButton add;
	private EditText phone;
	private EditText idcard;
	private EditText password;//判断是从那个地方进来的；
	private EditText qrpassword;
	private EditText code;
	private TextView title;
	long lastClick=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regidter);
		lastClick=System.currentTimeMillis();
		phone=(EditText)findViewById(R.id.phone);
		phone.setText(App.user.getPhone());
		idcard=(EditText)findViewById(R.id.idcard);
		idcard.setText(App.user.getIdcard());
		password=(EditText)findViewById(R.id.password);
		password.setText(App.user.getPassword());
		qrpassword=(EditText)findViewById(R.id.qrpassword);
		qrpassword.setText(App.user.getQrpassword());
		code=(EditText)findViewById(R.id.code);
		code.setText(App.user.getCode());
		title=(TextView)findViewById(R.id.title);
    	title.setText("注册");
		add=(ImageButton)findViewById(R.id.add);
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (System.currentTimeMillis() - lastClick <= 3000)
					return;
				else
				{
					if(phone.getText().toString().equals("")){
						Toast toast = Toast.makeText(getApplicationContext(),"手机号码必填", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						phone.requestFocus();
						return;
					}else if(idcard.getText().toString().equals("")){
						Toast toast = Toast.makeText(getApplicationContext(),"身份证号码必填", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						idcard.requestFocus();
						return;
					}else if(password.getText().toString().equals("")){
						Toast toast = Toast.makeText(getApplicationContext(),"密码必填", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						password.requestFocus();
						return;
					}else if(qrpassword.getText().toString().equals("")){
						Toast toast = Toast.makeText(getApplicationContext(),"确认密码必填", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						qrpassword.requestFocus();
						return;
					}else if(code.getText().toString().equals("")){
						Toast toast = Toast.makeText(getApplicationContext(),"邀请码必填", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						code.requestFocus();
						return;
					}else{
						if(qrpassword.getText().toString().equals(password.getText().toString()))
							inviteCodeVerify();
						else{
							qrpassword.setError(getResources().getString(R.string.hint_pwdRest_bj));
							qrpassword.requestFocus();
							return;
						}
					}
				}
				lastClick=System.currentTimeMillis();
			}
		});
		back=(ImageButton)findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(RegidterActivity.this,LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				RegidterActivity.this.startActivity(intent);
				finish();
			}
		});
	}
	private void inviteCodeVerify() {
		RequestParams params = new RequestParams();
		try {
			JSONObject json=new JSONObject();
			json.put("businessMobile", phone.getText().toString());
			json.put("inviteCode", code.getText().toString());
			json.put("idCard", idcard.getText().toString());
			json.put("request", PublicJsonParse.getPublicParam("inviteCodeVerify"));
			params.addHeader("Content-Type", "application/json");
			params.setBodyEntity(new StringEntity(json.toString()));
		} catch (JSONException e) {
			e.printStackTrace(); 
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,App.service+"inviteCodeVerify",params,
		    new RequestCallBack<String>(){
		        @Override
		        public void onSuccess(ResponseInfo<String> responseInfo) {
		        	Gson gson=new Gson();
		        	Type typeToken = new TypeToken<HttpResult<UserInfo>>(){}.getType();
		        	HttpResult<UserInfo> result=gson.fromJson(responseInfo.result,typeToken);
		        	if(result.isResult()){
		        		App.user.setPhone(phone.getText().toString());
						App.user.setIdcard(idcard.getText().toString());
						App.user.setPassword(password.getText().toString());
						App.user.setCode(code.getText().toString());
						App.user.setQrpassword(qrpassword.getText().toString());
						Intent intent=new Intent(RegidterActivity.this,RegidterGrzlActivity.class);
						RegidterActivity.this.startActivity(intent);
						finish();
		        	}else{
		        		Toast toast = Toast.makeText(getApplicationContext(),result.getError(), Toast.LENGTH_SHORT);
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
