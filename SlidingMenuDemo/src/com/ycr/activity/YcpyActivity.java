package com.ycr.activity;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.ycr.pojo.HttpResultList;
import com.ycr.pojo.UserInfo;
import com.ycr.util.BitmapHelp;
import com.ycr.util.PublicJsonParse;
import com.ycr.view.MyListGView;
import com.ycr.view.MyListGView.OnRefreshListener;
import com.indoorclub.girl.R;

public class YcpyActivity extends Activity{
	private List<UserInfo> dataList=new ArrayList<UserInfo>();
	private BaseAdapter adapter;
	private ImageButton back;
	private TextView title;
	private String id;
	private String name;
	private String isHaveCompany;
	private String type;
	private BitmapUtils bitmapUtils;
    private BitmapDisplayConfig bigPicDisplayConfig;
    private SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_ycpy);
		sharedPreferences=this.getSharedPreferences("UserInfoFile", Context.MODE_PRIVATE);//共享数据
		final MyListGView listView = (MyListGView) findViewById(R.id.listView);
		Intent intent=getIntent();
		id=intent.getStringExtra("id");
		name=intent.getStringExtra("name");
		type=intent.getStringExtra("type");
		isHaveCompany=intent.getStringExtra("isHaveCompany");
		getCompanyManager();
		title=(TextView)findViewById(R.id.title);
		title.setText(name);
		back=(ImageButton)findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener()  
	    {   
		    public void onClick(View v)   
		    {  
		    	YcpyActivity.this.finish();  
		     }  
		});
		adapter = new BaseAdapter() {
			public View getView(int position, View convertView, ViewGroup parent) {
				 UserInfo user=dataList.get(position);
				 convertView=LayoutInflater.from(YcpyActivity.this).inflate(R.layout.ycpy_item, null);
				 TextView userid = (TextView) convertView.findViewById(R.id.userid);
				 userid.setText(user.getAccountId());
				 ImageView image=(ImageView)convertView.findViewById(R.id.image);
				 if(user.getPhotoUrl().equals("")||user.getPhotoUrl()==null)
						image.setImageDrawable(getResources().getDrawable(R.drawable.photo));
					 else
						photo(image,user.getPhotoUrl());
				 TextView username = (TextView) convertView.findViewById(R.id.username);
				 username.setText(user.getName());
				 //ImageView ageImage=(ImageView)convertView.findViewById(R.id.ageImage);
				 TextView age = (TextView) convertView.findViewById(R.id.age);
				 age.setText(user.getAge());
				 TextView desc = (TextView) convertView.findViewById(R.id.desc);
				 desc.setText(user.getRemark());
				 ImageView add=(ImageView)convertView.findViewById(R.id.add);
				 add.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
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
				UserInfo user=dataList.get(arg2-1);
				Intent intent = new Intent();
				intent.putExtra("name", user.getName());
				intent.putExtra("clubId", id);
				intent.putExtra("type", type);
				intent.putExtra("managerId", user.getAccountId());
				intent.putExtra("isHaveCompany", isHaveCompany);
				intent.setClass(YcpyActivity.this, JrycActivity.class);
				YcpyActivity.this.startActivity(intent);
			}
		});
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						//dataList.addFirst("刷新后的内容");
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						adapter.notifyDataSetChanged();
						listView.onRefreshComplete();
					}

				}.execute(null,null,null);
			}
		});
    }
	private void getCompanyManager() {
 		RequestParams params = new RequestParams();
 		try {
 			JSONObject json=new JSONObject();
 			json.put("accountId", sharedPreferences.getString("id", ""));
 			json.put("clubId", id);
 			json.put("request", PublicJsonParse.getPublicParam("getCompanyManager"));
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
 		http.send(HttpRequest.HttpMethod.POST,App.service+"getCompanyManager",params,
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
 		        		dataList=result.getData();
 		        		dataList=dataList==null?new ArrayList<UserInfo>():dataList;
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
	private void photo(ImageView imageView,String imgUrl){
	    if (bitmapUtils == null) {
	        bitmapUtils = BitmapHelp.getBitmapUtils(getApplicationContext());
	    }

	    bigPicDisplayConfig = new BitmapDisplayConfig();
	    //bigPicDisplayConfig.setShowOriginal(true); // 显示原始图片,不压缩, 尽量不要使用, 图片太大时容易OOM。
	    bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
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
