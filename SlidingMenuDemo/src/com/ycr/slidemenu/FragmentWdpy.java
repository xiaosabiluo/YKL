package com.ycr.slidemenu;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
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
import com.ycr.activity.PyinfoActivity;
import com.ycr.activity.TjpyActivity;
import com.ycr.pojo.App;
import com.ycr.pojo.HttpResult;
import com.ycr.pojo.UserInfo;
import com.ycr.util.BitmapHelp;
import com.ycr.util.GPSHelp;
import com.ycr.util.MyFragment;
import com.ycr.util.PublicJsonParse;
import com.ycr.view.MyListGView;
import com.ycr.view.MyListGView.OnRefreshListener;
import com.indoorclub.girl.R;
/**
 * 朋友圈页面
 * @author Administrator
 *
 */
public class FragmentWdpy extends MyFragment{
	private List<UserInfo> dataList=new ArrayList<UserInfo>();
	private BaseAdapter adapter;
	private SharedPreferences sharedPreferences;
	private BitmapUtils bitmapUtils;
    private BitmapDisplayConfig bigPicDisplayConfig;
    private MyListGView listView;
    private ImageButton ivTitleBtnRight;
    public FragmentWdpy(){}
    public FragmentWdpy(ImageButton ivTitleBtnRight1){
    	ivTitleBtnRight=ivTitleBtnRight1;
    }
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_wdpy, container, false);
    	listView = (MyListGView) view.findViewById(R.id.listView);
    	sharedPreferences=getActivity().getSharedPreferences("UserInfoFile", Context.MODE_PRIVATE);//共享数据
    	findMyFriend();
    	ivTitleBtnRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), TjpyActivity.class);
				getActivity().startActivity(intent);
			}
		});
    	adapter = new BaseAdapter() {
			public View getView(final int position, View convertView, ViewGroup parent) {
				 final UserInfo user=dataList.get(position);
				 convertView=LayoutInflater.from(getActivity()).inflate(R.layout.wdpy_item, null);
				 TextView userid = (TextView) convertView.findViewById(R.id.userid);
				 userid.setText(user.getAccountId());
				 ImageView image=(ImageView)convertView.findViewById(R.id.image);
				 if(user.getPhotoUrl().equals("")||user.getPhotoUrl()==null||user.getPhotoUrl().equals("商品1的头像"))
					 image.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.photo));
				 else
					 photo(image,user.getPhotoUrl());
				 TextView username = (TextView) convertView.findViewById(R.id.username);
				 username.setText(user.getName());
				 ImageView stateImage=(ImageView)convertView.findViewById(R.id.stateImage);
				 TextView state = (TextView) convertView.findViewById(R.id.state);
				 String states=user.getStatus().equals("1")?"[上班]":"[休息]";
				 state.setText(states);
				 if(user.getStatus().equals("1"))
					 stateImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.sb));
				 else
					 stateImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.xb));
				 //ImageView ageImage=(ImageView)convertView.findViewById(R.id.ageImage);
				 TextView age = (TextView) convertView.findViewById(R.id.age);
				 age.setText(user.getAge());
				 TextView desc = (TextView) convertView.findViewById(R.id.desc);
				 desc.setText(user.getRemark());
				 ImageView del=(ImageView)convertView.findViewById(R.id.delWdpy);
				 del.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AlertDialog.Builder builder = new Builder(getActivity());
						builder.setMessage("是否确认删除");
						builder.setTitle("提示");
						builder.setPositiveButton("确认",
								new android.content.DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										  delMyFriend(user.getAccountId());
									}
								});
						builder.setNegativeButton("取消",
								new android.content.DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								});
						builder.create().show();
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
				intent.putExtra("id", user.getAccountId());
				intent.putExtra("name", user.getName());
				intent.putExtra("type", "1");
				intent.setClass(getActivity(), PyinfoActivity.class);
				getActivity().startActivity(intent);
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
    	return view;
    }
    private void findMyFriend() {
    	LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
    	Location location=GPSHelp.Location(locationManager);
 		RequestParams params = new RequestParams();
 		try {
 			JSONObject json=new JSONObject();
 			json.put("accountId", sharedPreferences.getString("id", ""));
 			if(location!=null){
 				json.put("longitude",location.getLongitude());
 				json.put("latitude", location.getLatitude());
 			}
 			json.put("request", PublicJsonParse.getPublicParam("findMyFriend"));
 			params.addHeader("Content-Type", "application/json");
 			params.setBodyEntity(new StringEntity(json.toString()));
 		} catch (JSONException e) {
 			e.printStackTrace(); 
 		}
 		catch (UnsupportedEncodingException e) {
 			e.printStackTrace();
 		}
 		HttpUtils http = new HttpUtils();
 		http.send(HttpRequest.HttpMethod.POST,App.service+"findMyFriend",params,
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
 		        	Type typeToken = new TypeToken<HttpResult<List<UserInfo>>>(){}.getType();
 		        	HttpResult<List<UserInfo>> result=gson.fromJson(responseInfo.result,typeToken);
 		        	if(result.isResult()){
 		        		dataList = result.getData();
 		        		dataList=dataList==null?new ArrayList<UserInfo>():dataList;
 		        		adapter.notifyDataSetChanged();
 		        	}else{
 		        		Toast toast = Toast.makeText(getActivity().getApplicationContext(),result.getError(), Toast.LENGTH_SHORT);
 						toast.setGravity(Gravity.CENTER, 0, 0);
 						toast.show();
 		        	}
 		        }

 		        @Override
 		        public void onFailure(HttpException error, String msg) {
 		        	Toast toast = Toast.makeText(getActivity().getApplicationContext(),msg, Toast.LENGTH_SHORT);
 					toast.setGravity(Gravity.CENTER, 0, 0);
 					toast.show();
 		        }
 		});
 	}
    private void delMyFriend(String friendId) {
 		RequestParams params = new RequestParams();
 		try {
 			JSONObject json=new JSONObject();
 			json.put("accountId", sharedPreferences.getString("id", ""));
 			json.put("friendId", friendId);
 			json.put("request", PublicJsonParse.getPublicParam("delMyFriend"));
 			params.addHeader("Content-Type", "application/json");
 			params.setBodyEntity(new StringEntity(json.toString()));
 		} catch (JSONException e) {
 			e.printStackTrace(); 
 		}
 		catch (UnsupportedEncodingException e) {
 			e.printStackTrace();
 		}
 		HttpUtils http = new HttpUtils();
 		http.send(HttpRequest.HttpMethod.POST,App.service+"delMyFriend",params,
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
 		        	Type typeToken = new TypeToken<HttpResult<List<UserInfo>>>(){}.getType();
 		        	HttpResult<List<UserInfo>> result=gson.fromJson(responseInfo.result,typeToken);
 		        	if(result.isResult()){
 		        		AlertDialog.Builder builder = new Builder(getActivity()); 
		        		builder.setMessage("删除好友成功");
		        		builder.setPositiveButton("好的",new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								findMyFriend();
							}
						});
		        		builder.show();
		        		
 		        	}else{
 		        		Toast toast = Toast.makeText(getActivity().getApplicationContext(),result.getError(), Toast.LENGTH_SHORT);
 						toast.setGravity(Gravity.CENTER, 0, 0);
 						toast.show();
 		        	}
 		        }

 		        @Override
 		        public void onFailure(HttpException error, String msg) {
 		        	Toast toast = Toast.makeText(getActivity().getApplicationContext(),msg, Toast.LENGTH_SHORT);
 					toast.setGravity(Gravity.CENTER, 0, 0);
 					toast.show();
 		        }
 		});
 	}
    private void photo(ImageView imageView,String imgUrl){
	    if (bitmapUtils == null) {
	        bitmapUtils = BitmapHelp.getBitmapUtils(getActivity().getApplicationContext());
	    }

	    bigPicDisplayConfig = new BitmapDisplayConfig();
	    //bigPicDisplayConfig.setShowOriginal(true); // 显示原始图片,不压缩, 尽量不要使用, 图片太大时容易OOM。
	    bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
	    bigPicDisplayConfig.setBitmapMaxSize(BitmapCommonUtils.getScreenSize(getActivity()));
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

