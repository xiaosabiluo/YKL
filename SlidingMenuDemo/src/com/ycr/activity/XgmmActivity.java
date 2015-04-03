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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

public class XgmmActivity extends Activity implements OnClickListener{

	private ImageButton back;
	private ImageButton add;
	private TextView title;
	private EditText odPassText;
	private EditText newPassText;
	private EditText qrPassText;
	private SharedPreferences sharedPreferences;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xgmm);
		initView();
	}
	private void initView() {
		back=(ImageButton) findViewById(R.id.back);
		add=(ImageButton)findViewById(R.id.add);
		title=(TextView)findViewById(R.id.title);
		title.setText("修改密码");
		odPassText=(EditText)findViewById(R.id.odPass);
		newPassText=(EditText)findViewById(R.id.newPass);
		qrPassText=(EditText)findViewById(R.id.qrPass);
		back.setOnClickListener(this);
		add.setOnClickListener(this);
		sharedPreferences=this.getSharedPreferences("UserInfoFile", Context.MODE_PRIVATE);//共享数据
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.add://修改密码
			String odp=sharedPreferences.getString("password", "");
			if(odPassText.getText().toString().equals(""))
			{
				odPassText.setError(getResources().getString(R.string.hint_pwdRest_old));
				odPassText.requestFocus();
				return;
			}
			else if(newPassText.getText().toString().equals(""))
			{
				newPassText.setError(getResources().getString(R.string.hint_pwdRest_new));
				newPassText.requestFocus();
				return;
			}
			else if(qrPassText.getText().toString().equals(""))
			{
				qrPassText.setError(getResources().getString(R.string.hint_pwdRest_qr));
				qrPassText.requestFocus();
				return;
			}
			else
			{
				if(qrPassText.getText().toString().equals(newPassText.getText().toString())){
					if(odPassText.getText().toString().equals(odp))
						pwdReset();
					else{
						new AlertDialog.Builder(XgmmActivity.this)
	 		               .setMessage("原密码输入错误，请重新输入")
	 		               .setPositiveButton("确认",null)
	 		               .setCancelable(false)
	 		               .show();
					}
				}
				else{
					qrPassText.setError(getResources().getString(R.string.hint_pwdRest_bj));
					qrPassText.requestFocus();
					return;
				}
			}
			break;
		default:
			break;
		}
	}
	private void pwdReset() {
		RequestParams params = new RequestParams();
		try {
			JSONObject json=new JSONObject();
			json.put("accountId", sharedPreferences.getString("id", ""));
			json.put("oldPwd", odPassText.getText().toString());
			json.put("newPwd", newPassText.getText().toString());
			json.put("request", PublicJsonParse.getPublicParam("loginOut"));
			params.addHeader("Content-Type", "application/json");
			params.setBodyEntity(new StringEntity(json.toString()));
		} catch (JSONException e) {
			e.printStackTrace(); 
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,App.service+"loginOut",params,
		    new RequestCallBack<String>(){

		        @Override
		        public void onStart() {
		        }

		        @Override
		        public void onLoading(long total, long current, boolean isUploading) {
		            
		        }

		        @Override
		        public void onSuccess(ResponseInfo<String> responseInfo) {
		        	Gson gson=new Gson();
		        	Type typeToken = new TypeToken<HttpResult<UserInfo>>(){}.getType();
		        	HttpResult<UserInfo> result=gson.fromJson(responseInfo.result,typeToken);
		        	if(result.isResult()){
		        		new AlertDialog.Builder(XgmmActivity.this)
	 		               .setMessage("密码修改成功")
	 		               .setPositiveButton("确认",
	 		                       new DialogInterface.OnClickListener() {
	 		                           @Override
	 		                           public void onClick(DialogInterface dialog, int which) {
		 		      		        		finish();
	 		                           }
	 		               })
	 		               .setCancelable(false)
	 		               .show();
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
