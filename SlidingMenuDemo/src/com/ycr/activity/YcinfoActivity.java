/*
 * $filename: AboutActivity.java,v $
 * $Date: 2013-12-23  $
 * Copyright (C) ZhengHaibo, Inc. All rights reserved.
 * This software is Made by Zhenghaibo.
 */
package com.ycr.activity;




import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ycr.pojo.App;
import com.ycr.pojo.CompanysInfo;
import com.ycr.pojo.HttpResult;
import com.ycr.pojo.HttpResultList;
import com.ycr.pojo.PhotoInfo;
import com.ycr.pojo.UserInfo;
import com.ycr.util.BitmapHelp;
import com.ycr.util.GPSHelp;
import com.ycr.util.PublicJsonParse;
import com.ycr.view.MyGridGView;
import com.indoorclub.girl.R;

/*
 *@author: ZhengHaibo  
 *web:     http://blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *2013-12-23  Nanjing,njupt,China
 */
public class YcinfoActivity extends Activity implements OnClickListener{
	private CompanysInfo dataList=new CompanysInfo();
	private ImageButton back;
	private ImageButton add;
	private TextView title;
	private String id;
	private String name;
	private String type;
	private String isHaveCompany;
	private String isHavingJoin;
	private TextView jj;
	private TextView dz;
	private TextView xf;
	private MyGridGView mGridView;
	private BaseAdapter adapter;
	private BitmapUtils bitmapUtils;
    private BitmapDisplayConfig bigPicDisplayConfig;
    private SharedPreferences sharedPreferences;
    private Drawable drawable;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ycinfo);
		sharedPreferences=this.getSharedPreferences("UserInfoFile", Context.MODE_PRIVATE);//共享数据
		drawable=getResources().getDrawable(R.drawable.photo);
		initView();
	}
	private void initView() {
		dataList.setPhotoVos(new ArrayList<PhotoInfo>());
		Intent intent=getIntent();
		id=intent.getStringExtra("id");
		name=intent.getStringExtra("name");
		type=intent.getStringExtra("type");
		isHaveCompany=intent.getStringExtra("isHaveCompany");
		isHavingJoin=intent.getStringExtra("isHavingJoin");
		getCompany();
		back=(ImageButton) findViewById(R.id.back);
		add=(ImageButton)findViewById(R.id.add);
		title=(TextView)findViewById(R.id.title);
		title.setText(name);
		back.setOnClickListener(this);
		add.setOnClickListener(this);
		jj=(TextView)findViewById(R.id.jj);
		dz=(TextView)findViewById(R.id.dz);
		xf=(TextView)findViewById(R.id.xf);
		mGridView=(MyGridGView) findViewById(R.id.gridview);
		adapter = new BaseAdapter() {
			public View getView(int position, View convertView, ViewGroup parent) {
				 PhotoInfo user=dataList.getPhotoVos().get(position);
				 convertView=LayoutInflater.from(YcinfoActivity.this).inflate(R.layout.companys_image_grid, null);
				 TextView userid = (TextView) convertView.findViewById(R.id.item_image_grid_text);
				 final String id=user.getImageUrl();
				 userid.setText(id);
				 ImageView image=(ImageView)convertView.findViewById(R.id.image);
				 if(user.getImageUrl().equals("")||user.getImageUrl()==null)
						image.setImageDrawable(getResources().getDrawable(R.drawable.photo));
					 else
						photo(image,user.getImageUrl());
				 return convertView;
			}

			public long getItemId(int position) {
				return position;
			}

			public Object getItem(int position) {
				return dataList.getPhotoVos().get(position);
			}

			public int getCount() {
				return dataList.getPhotoVos().size();
			}
		};
		mGridView.setAdapter(adapter);
		if(type.equals(""))
			add.setBackgroundResource(R.drawable.btn_joinclub_pressed);
		else
			add.setBackgroundResource(R.drawable.btn_outclub_pressed);
		if(isHavingJoin!=null&&isHavingJoin.equals("1"))
			add.setVisibility(View.GONE);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.add://加入夜场
			if(type.equals("")){
				/*if(isHaveCompany.equals("1")){
					Intent intent = new Intent();
					intent.putExtra("name", name);
					intent.putExtra("clubId", id);
					intent.putExtra("type", 1);
					intent.putExtra("managerId", "");
					intent.putExtra("isHaveCompany", isHaveCompany);
					intent.setClass(YcinfoActivity.this, JrycActivity.class);
					YcinfoActivity.this.startActivity(intent);
				}else{
					Intent intent = new Intent();
					intent.putExtra("id", id);
					intent.putExtra("name", name);
					intent.putExtra("type", 1);
					intent.putExtra("isHaveCompany", isHaveCompany);
					intent.setClass(YcinfoActivity.this, YcpyActivity.class);
					YcinfoActivity.this.startActivity(intent);
				}就的逻辑*/
				Intent intent = new Intent();
				intent.putExtra("id", id);
				intent.putExtra("name", name);
				intent.putExtra("type", 1);
				intent.putExtra("isHaveCompany", isHaveCompany);
				intent.setClass(YcinfoActivity.this, YcpyActivity.class);
				YcinfoActivity.this.startActivity(intent);
			}else{
				JoinOrQuitCompany();
			}
			break;
		default:
			break;
		}
	}
	private void getCompany() {
 		RequestParams params = new RequestParams();
 		try {
 			JSONObject json=new JSONObject();
 			json.put("accountId", sharedPreferences.getString("id", ""));
 			json.put("clubId", id);
 			json.put("request", PublicJsonParse.getPublicParam("getCompany"));
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
 		http.send(HttpRequest.HttpMethod.POST,App.service+"getCompany",params,
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
 		        	Type typeToken = new TypeToken<HttpResult<CompanysInfo>>(){}.getType();
 		        	String jsonElement=responseInfo.result.replace("[]", "null");
 		        	HttpResult<CompanysInfo> result=gson.fromJson(jsonElement,typeToken);
 		        	if(result.isResult()){
 		        		dataList=result.getData();
 		        		dataList.setPhotoVos(dataList.getPhotoVos()==null?new ArrayList<PhotoInfo>():dataList.getPhotoVos());
	 		       		jj.setText(dataList.getDescription());
	 		       		dz.setText(dataList.getAddress());
	 		       		xf.setText(dataList.getFee());
 		        		adapter.notifyDataSetChanged();
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
	private void JoinOrQuitCompany() {
		LocationManager locationManager = (LocationManager)YcinfoActivity.this.getSystemService(Context.LOCATION_SERVICE);
    	Location location=GPSHelp.Location(locationManager);
 		RequestParams params = new RequestParams();
 		try {
 			JSONObject json=new JSONObject();
 			json.put("accountId", sharedPreferences.getString("id", ""));
 			json.put("clubId", id);
 			json.put("type", 0);
 			json.put("isHaveCompany", isHaveCompany);
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
 		        		Toast toast = Toast.makeText(YcinfoActivity.this.getApplicationContext(),"已退出夜场", Toast.LENGTH_SHORT);
 						toast.setGravity(Gravity.CENTER, 0, 0);
 						toast.show();
 		        	}else{
 		        		Toast toast = Toast.makeText(YcinfoActivity.this.getApplicationContext(),result.getError(), Toast.LENGTH_SHORT);
 						toast.setGravity(Gravity.CENTER, 0, 0);
 						toast.show();
 		        	}
 		        }

 		        @Override
 		        public void onFailure(HttpException error, String msg) {
 		        	Toast toast = Toast.makeText(YcinfoActivity.this.getApplicationContext(),msg, Toast.LENGTH_SHORT);
 					toast.setGravity(Gravity.CENTER, 0, 0);
 					toast.show();
 		        }
 		});
 	}
	private void photo(ImageView imageView,String imgUrl){
	    if (bitmapUtils == null) {
	        bitmapUtils = BitmapHelp.getBitmapUtils(getApplicationContext());
	    }

	    bigPicDisplayConfig = new BitmapDisplayConfig();
	    //bigPicDisplayConfig.setShowOriginal(true); // 显示原始图片,不压缩, 尽量不要使用, 图片太大时容易OOM。
	    bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
	    bigPicDisplayConfig.setLoadFailedDrawable(drawable);
	    bigPicDisplayConfig.setBitmapMaxSize(BitmapCommonUtils.getScreenSize(this));
	    BitmapLoadCallBack<ImageView> callback = new DefaultBitmapLoadCallBack<ImageView>() {
	        @Override
	        public void onLoadStarted(ImageView container, String uri, BitmapDisplayConfig config) {
	            super.onLoadStarted(container, uri, config);
	        }
	        @Override
	        public void onLoadCompleted(ImageView container, String uri, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
	            super.onLoadCompleted(container, uri, bitmap, config, from);
	        }
	    };
	    bitmapUtils.display(imageView, imgUrl, bigPicDisplayConfig, callback);
    }
}
