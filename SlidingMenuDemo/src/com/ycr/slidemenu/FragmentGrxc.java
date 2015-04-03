package com.ycr.slidemenu;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.indoorclub.girl.R;
import com.indoorclub.girl.R.drawable;
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
import com.ycr.activity.GrxcImageViewActivity;
import com.ycr.choosephotos.Bimp;
import com.ycr.choosephotos.ChoosePhotosActivity;
import com.ycr.pojo.App;
import com.ycr.pojo.HttpResult;
import com.ycr.pojo.HttpResultList;
import com.ycr.pojo.MediaInfo;
import com.ycr.pojo.MediaListInfo;
import com.ycr.pojo.UpPhotoInfo;
import com.ycr.util.BitmapHelp;
import com.ycr.util.PublicJsonParse;
import com.ycr.util.ReadImgToBinary;

public class FragmentGrxc extends Fragment{
	private MediaInfo media=new MediaInfo();
	private GridView mGridView;
	private BaseAdapter adapterM;
	private List<MediaInfo> dataListM=new ArrayList<MediaInfo>();
	private List<MediaInfo> selectList=new ArrayList<MediaInfo>();
	public static boolean flag=false;
	private TextView edit;
	private TextView del;
	private TextView isok;
	private SharedPreferences sharedPreferences;
	private BitmapUtils bitmapUtils;
    private BitmapDisplayConfig bigPicDisplayConfig;
    private ProgressDialog mProgressDialog;
    private int type=2;
    public static boolean isUpload=false;
    private View headerlayout;
    private ImageButton add;
    private Drawable drawable;
    public FragmentGrxc(){}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
		media.setMediaUrl("add");
		drawable=getActivity().getResources().getDrawable(R.drawable.photo);
    	View view = inflater.inflate(R.layout.grxc_tab, container, false);
    	headerlayout=view.findViewById(R.id.activityHeader);
    	headerlayout.setVisibility(View.GONE);
    	sharedPreferences=getActivity().getSharedPreferences("UserInfoFile", Context.MODE_PRIVATE);//共享数据
    	type=getArguments().getInt("type");
    	add=(ImageButton)view.findViewById(R.id.add);
    	add.setVisibility(View.GONE);
    	getMedia();
    	mGridView=(GridView) view.findViewById(R.id.gridview);
    	edit=(TextView)view.findViewById(R.id.edit);
    	edit.setOnClickListener(textViewOnClicknew);
    	del=(TextView)view.findViewById(R.id.del);
    	del.setOnClickListener(textViewOnClicknew);
    	isok=(TextView)view.findViewById(R.id.isok);
    	isok.setOnClickListener(textViewOnClicknew);
    	adapterM = new BaseAdapter() {
			public View getView(int position, View convertView, ViewGroup parent) {
				final Holder holder;
				if (convertView == null) {
					holder = new Holder();
					convertView = View.inflate(getActivity(), R.layout.item_image_grid, null);
					holder.iv = (ImageView) convertView.findViewById(R.id.image);
					holder.selected = (ImageView) convertView.findViewById(R.id.isselected);
					holder.text = (TextView) convertView.findViewById(R.id.item_image_grid_text);
					convertView.setTag(holder);
				} else {
					holder = (Holder) convertView.getTag();
				}
				final MediaInfo item = dataListM.get(position);
				if(item.isSelected()&&flag){
					holder.selected.setImageResource(R.drawable.icon_data_select);
					//holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
				}
				else
					holder.selected.setImageResource(-1);
				if(item.getMediaUrl().equals("add")){
					holder.iv.setImageResource(R.drawable.btn_add_media);
					holder.iv.setScaleType(ScaleType.FIT_XY);
					holder.text.setBackgroundColor(Color.WHITE);
				}
				else{
					photo(holder.iv,item.getMediaUrl());
					holder.text.setBackgroundColor(Color.GRAY);
				}
				holder.iv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
							item.setSelected(!item.isSelected());
							if(flag){
								if (item.isSelected()) {
									holder.selected.setImageResource(R.drawable.icon_data_select);
									//holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
									selectList.add(item);
								}else if(!item.isSelected()){
									holder.selected.setImageResource(-1);
									//holder.text.setBackgroundColor(0x00000000);
									item.setSelected(false);
									selectList.remove(item);
								}
							}
							else{
								selectList.remove(item);
								holder.selected.setImageResource(-1);
								//holder.text.setBackgroundColor(0x00000000);
								item.setSelected(false);
								if(item.getMediaUrl().equals("add")){
									Intent intent = new Intent(getActivity(),ChoosePhotosActivity.class);
									startActivityForResult(intent, Activity.RESULT_FIRST_USER);
								}else{
									dataListM.remove(media);
									dataListM.remove(item);
									dataListM.add(0, item);
									Intent intent = new Intent();
									String [] url=new String[dataListM.size()];
									for (int i = 0; i < dataListM.size(); i++) {
										url[i]=dataListM.get(i).getMediaUrl();
									}
									Bundle bundle=new Bundle();
									bundle.putStringArray("url",url);
									intent.putExtras(bundle);
									intent.setClass(getActivity(), GrxcImageViewActivity.class);
									getActivity().startActivity(intent);
								}
							}
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
    	mGridView.setAdapter(adapterM);//调用ImageAdapter.java  
    	mGridView.setOnItemClickListener(new OnItemClickListener(){//监听事件  
         public void onItemClick(AdapterView<?> parent, View view, int position, long id)   
         {  
         }  
        });  
    	if(isUpload){
    		mProgressDialog = new ProgressDialog(getActivity());//创建ProgressDialog对象
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //设置进度条风格，风格为圆形，旋转的 
            //mProgressDialog.setTitle("读取ing...");// 设置ProgressDialog标题 
            mProgressDialog.setMessage("正在上传...");// 设置ProgressDialog提示信息 
            mProgressDialog.setIndeterminate(true);//设置ProgressDialog 的进度条不明确
            mProgressDialog.setCancelable(true);// 设置ProgressDialog 可以按退回键取消
            mProgressDialog.show();// 让ProgressDialog显示
            mProgressDialog.setCancelable(false);
    		updateMedia();
    	}
    	return view;
    }
	private OnClickListener textViewOnClicknew=new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.edit:
				del.setVisibility(View.VISIBLE);
				isok.setVisibility(View.VISIBLE);
				edit.setVisibility(View.GONE);
				dataListM.remove(media);
				flag=true;
				break;
			case R.id.isok:
				del.setVisibility(View.GONE);
				isok.setVisibility(View.GONE);
				edit.setVisibility(View.VISIBLE);
				flag=false;
				dataListM.add(media);
				break;
			default:
				delMedia();
				break;
			}
			adapterM.notifyDataSetChanged();
		}
	};
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(getActivity(), "请选择要删除的图片", 400).show();
				break;
			default:
				break;
			}
		}
	};
	private void getMedia() {
 		RequestParams params = new RequestParams();
 		try {
 			JSONObject json=new JSONObject();
 			json.put("accountId", sharedPreferences.getString("id", ""));
 			json.put("type", 2);
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
 		        		if(!flag)
 		        			dataListM.add(media);
 		        		adapterM.notifyDataSetChanged();
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
	private void delMedia() {
 		RequestParams params = new RequestParams();
 		try {
 			String mediaId="";
 			for (MediaInfo mediaInfo : selectList) {
 				mediaId+=mediaInfo.getMediaId()+",";
			}
 			if(mediaId.lastIndexOf(",")!=-1)
 				mediaId.substring(0,mediaId.lastIndexOf(",")-1);
 			JSONObject json=new JSONObject();
 			json.put("accountId", sharedPreferences.getString("id", ""));
 			json.put("type", 2);
 			json.put("mediaId", mediaId);
 			json.put("request", PublicJsonParse.getPublicParam("delMedia"));
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
 		http.send(HttpRequest.HttpMethod.POST,App.service+"delMedia",params,
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
 		        		Toast toast = Toast.makeText(getActivity().getApplicationContext(),"操作成功", Toast.LENGTH_SHORT);
 						toast.setGravity(Gravity.CENTER, 0, 0);
 						toast.show();
 						getMedia();
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
	        bitmapUtils = BitmapHelp.getBitmapUtils(getActivity());
	    }

	    bigPicDisplayConfig = new BitmapDisplayConfig();
	    //bigPicDisplayConfig.setShowOriginal(true); // 显示原始图片,不压缩, 尽量不要使用, 图片太大时容易OOM。
	    bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
	    bigPicDisplayConfig.setLoadFailedDrawable(drawable);
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
	class Holder {
		private ImageView iv;
		private ImageView selected;
		private TextView text;
	}
	private void updateMedia() {
 		RequestParams params = new RequestParams();
 		try {
 			List<UpPhotoInfo> images=new ArrayList<UpPhotoInfo>();
 			for (Bitmap bitmap : Bimp.bmp) {
 				UpPhotoInfo photo=new UpPhotoInfo();
 				photo.setPhotoUrl(ReadImgToBinary.imgToBase64(null, bitmap, null));
 				images.add(photo);
 			}
 			Gson gson=new Gson();
 			JSONObject json=new JSONObject();
 			json.put("accountId", sharedPreferences.getString("id", ""));
 			json.put("type",2);
 			json.put("photos",new JSONArray(gson.toJson(images)));
 			json.put("request", PublicJsonParse.getPublicParam("updateMedia"));
 			params.addHeader("Content-Type", "application/json");
 			params.setBodyEntity(new StringEntity(json.toString()));
 		} catch (JSONException e) {
 			e.printStackTrace(); 
 		}
 		catch (UnsupportedEncodingException e) {
 			e.printStackTrace();
 		}
 		HttpUtils http = new HttpUtils();
 		http.send(HttpRequest.HttpMethod.POST,App.service+"updateMedia",params,
 		    new RequestCallBack<String>(){
				public void onSuccess(ResponseInfo<String> responseInfo) {
 		        	Gson gson=new Gson();
 		        	Type typeToken = new TypeToken<HttpResult<MediaListInfo>>(){}.getType();
 		        	HttpResult<MediaListInfo> result=gson.fromJson(responseInfo.result,typeToken);
 		        	if(result.isResult()){
 		        		List<MediaInfo> list=result.getData().getMedisa();
 		        		for (MediaInfo mediaInfo : list) {
 		        			if(mediaInfo.getError().equals(""))
 		        				dataListM.add(dataListM.size()-1,mediaInfo);
						}
 		        		adapterM.notifyDataSetChanged();
 		        	}else{
 		        		
 		        	}
 		        	FragmentGrxc.isUpload=false;
 		        	Bimp.bmp=new ArrayList<Bitmap>();
 		        	mProgressDialog.cancel();
 		        }

 		        @Override
 		        public void onFailure(HttpException error, String msg) {
 		        	Toast toast = Toast.makeText(getActivity().getApplicationContext(),"网络异常", Toast.LENGTH_SHORT);
 					toast.setGravity(Gravity.CENTER, 0, 0);
 					toast.show();
 					FragmentGrxc.isUpload=false;
 					Bimp.bmp=new ArrayList<Bitmap>();
 					mProgressDialog.cancel();
 		        }
 		});
 	}
}
