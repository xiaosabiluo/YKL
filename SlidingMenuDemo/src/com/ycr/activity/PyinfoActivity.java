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
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.ycr.pojo.MediaInfo;
import com.ycr.pojo.UserInfo;
import com.ycr.util.BitmapHelp;
import com.ycr.util.PublicJsonParse;
import com.indoorclub.girl.R;

/*
 *@author: ZhengHaibo  
 *web:     http://blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *2013-12-23  Nanjing,njupt,China
 */
public class PyinfoActivity extends Activity implements OnClickListener{
	private UserInfo userInfo= new UserInfo();
	private List<CompanysInfo> dataList=new ArrayList<CompanysInfo>();
	private List<MediaInfo> dataListM=new ArrayList<MediaInfo>();
	private ImageButton back;
	private ImageButton add;
	private TextView title;
	private String id;
	private String name;
	//private TextView dqwz;
	private TextView grsm;
	private GridView mGridView;
	private BaseAdapter adapter;
	private BaseAdapter adapterM;
	private BitmapUtils bitmapUtils;
    private BitmapDisplayConfig bigPicDisplayConfig;
    private SharedPreferences sharedPreferences;
    private Drawable drawable;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pyinfo);
		sharedPreferences=this.getSharedPreferences("UserInfoFile", Context.MODE_PRIVATE);//共享数据
		drawable=getResources().getDrawable(R.drawable.photo);
		getMedia();
		initView();
	}
	private void initView() {
		Intent intent=getIntent();
		id=intent.getStringExtra("id");
		name=intent.getStringExtra("name");
		String type=intent.getStringExtra("type");
		back=(ImageButton) findViewById(R.id.back);
		add=(ImageButton)findViewById(R.id.addUser);
		if(type.equals("1"))
			add.setVisibility(View.GONE);
		title=(TextView)findViewById(R.id.title);
		title.setText(name);
		back.setOnClickListener(this);
		add.setOnClickListener(this);
		//dqwz=(TextView)findViewById(R.id.dqwz);
		grsm=(TextView)findViewById(R.id.grsm);
		getBusinessDetail();
		mGridView=(GridView) findViewById(R.id.gridview);
		adapterM = new BaseAdapter() {
			public View getView(int position, View convertView, ViewGroup parent) {
				 final MediaInfo user=dataListM.get(position);
				 convertView=LayoutInflater.from(PyinfoActivity.this).inflate(R.layout.companys_image_grid, null);
				 TextView userid = (TextView) convertView.findViewById(R.id.item_image_grid_text);
				 final String id=user.getMediaId();
				 userid.setText(id);
				 ImageView image=(ImageView)convertView.findViewById(R.id.image);
				 if(user.getMediaUrl().equals("")||user.getMediaUrl()==null)
						image.setImageDrawable(getResources().getDrawable(R.drawable.photo));
					 else
						photo(image,user.getMediaUrl());
				 image.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dataListM.remove(user);
						dataListM.add(0, user);
						Intent intent = new Intent();
						String [] url=new String[dataListM.size()];
						for (int i = 0; i < dataListM.size(); i++) {
							url[i]=dataListM.get(i).getMediaUrl();
						}
						Bundle bundle=new Bundle();
						bundle.putStringArray("url",url);
						intent.putExtras(bundle);
						intent.setClass(PyinfoActivity.this, GrxcImageViewActivity.class);
						PyinfoActivity.this.startActivity(intent);
					}
				 });
				 return convertView;
			}

			public long getItemId(int position) {
				return position;
			}

			public Object getItem(int position) {
				return dataListM.get(position);
			}

			public int getCount() {
				return dataListM.size();
			}
		};
		mGridView.setAdapter(adapterM);
		final ListView listView = (ListView) findViewById(R.id.listview);
		adapter = new BaseAdapter() {
			public View getView(int position, View convertView, ViewGroup parent) {
				 final CompanysInfo user=dataList.get(position);
				 convertView=LayoutInflater.from(PyinfoActivity.this).inflate(R.layout.tjyc_item, null);
				 TextView userid = (TextView) convertView.findViewById(R.id.ycid);
				 final String id=user.getCompanyId();
				 userid.setText(id);
				 ImageView image=(ImageView)convertView.findViewById(R.id.image);
				 if(user.getCompanyPhotoUrl().equals("")||user.getCompanyPhotoUrl()==null)
						image.setImageDrawable(getResources().getDrawable(R.drawable.photo));
					 else
						photo(image,user.getCompanyPhotoUrl());
				 TextView name = (TextView) convertView.findViewById(R.id.name);
				 final String names=user.getCompanyName();
				 name.setText(names);
				 //ImageView stateImage=(ImageView)convertView.findViewById(R.id.stateImage);
				 TextView count = (TextView) convertView.findViewById(R.id.count);
				 count.setText(user.getCompanyNum());
				 //ImageView ageImage=(ImageView)convertView.findViewById(R.id.ageImage);
				 TextView space = (TextView) convertView.findViewById(R.id.space);
				 space.setText(Math.round(Double.parseDouble(user.getCompanyDistance())/100d/10d)+"千米");
				 TextView desc = (TextView) convertView.findViewById(R.id.desc);
				 desc.setText(user.getConsume());
				 TextView address = (TextView) convertView.findViewById(R.id.address);
				 address.setText(user.getCompanyAddress());
				 ImageView add=(ImageView)convertView.findViewById(R.id.addYc);
				 add.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra("id", user.getCompanyId());
						intent.putExtra("name", user.getCompanyName());
						intent.putExtra("isHaveCompany", user.getIsHaveCompany());
						intent.putExtra("type", user.getIsOwner());
						intent.setClass(PyinfoActivity.this, YcpyActivity.class);
						PyinfoActivity.this.startActivity(intent);
					}
				});
				 return convertView;
			}

			public long getItemId(int position) {
				return position;
			}

			public Object getItem(int position) {
				return dataList.get(position);
			}

			public int getCount() {
				return dataList.size();
			}
		};
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				CompanysInfo info=dataList.get(arg2);
				Intent intent = new Intent();
				intent.putExtra("id", info.getCompanyId());
				intent.putExtra("name", info.getCompanyName());
				intent.putExtra("isHaveCompany", info.getIsHaveCompany());
				intent.putExtra("type", info.getIsOwner());
				intent.setClass(PyinfoActivity.this, YcinfoActivity.class);
				PyinfoActivity.this.startActivity(intent);
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.addUser://加为好友
			addFriend(id);
			break;
		default:
			break;
		}
	}
	private void getBusinessDetail() {
 		RequestParams params = new RequestParams();
 		try {
 			JSONObject json=new JSONObject();
 			json.put("loginAccountId", sharedPreferences.getString("id", ""));
 			json.put("accountId", id);
 			json.put("request", PublicJsonParse.getPublicParam("getBusinessDetail"));
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
 		http.send(HttpRequest.HttpMethod.POST,App.service+"getBusinessDetail",params,
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
 		        		userInfo=result.getData();
 		        		dataList=userInfo.getCompanys();
 		        		dataList=dataList==null?new ArrayList<CompanysInfo>():dataList;
 		        		grsm.setText(userInfo.getRemark());
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
	private void getMedia() {
 		RequestParams params = new RequestParams();
 		try {
 			JSONObject json=new JSONObject();
 			json.put("accountId", sharedPreferences.getString("id", ""));
 			json.put("type", 1);
 			json.put("request", PublicJsonParse.getPublicParam("getMedia"));
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
 		http.send(HttpRequest.HttpMethod.POST,App.service+"getMedia",params,
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
 		        	Type typeToken = new TypeToken<HttpResultList<MediaInfo>>(){}.getType();
 		        	String jsonElement=responseInfo.result.replace("[]", "null");
 		        	HttpResultList<MediaInfo> result=gson.fromJson(jsonElement,typeToken);
 		        	if(result.isResult()){
 		        		dataListM=result.getData();
 		        		dataListM=dataListM==null?new ArrayList<MediaInfo>():dataListM;
 		        		adapterM.notifyDataSetChanged();
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
	private void addFriend(String friendId) {
 		RequestParams params = new RequestParams();
 		try {
 			JSONObject json=new JSONObject();
 			json.put("accountId", sharedPreferences.getString("id", ""));
 			json.put("friendId", friendId);
 			json.put("request", PublicJsonParse.getPublicParam("addFriend"));
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
 		http.send(HttpRequest.HttpMethod.POST,App.service+"addFriend",params,
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
 						AlertDialog.Builder builder = new Builder(PyinfoActivity.this); 
		        		builder.setMessage("好友添加成功");
		        		builder.setPositiveButton("好的", null);
		        		builder.show();
		        		Intent intent = new Intent();
		    			intent.putExtra("id", id);
		    			intent.putExtra("name", name);
		    			intent.setClass(PyinfoActivity.this, TjpyActivity.class);
		    			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    			PyinfoActivity.this.startActivity(intent);
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
