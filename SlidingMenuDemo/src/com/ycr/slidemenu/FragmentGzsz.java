package com.ycr.slidemenu;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.indoorclub.girl.R;
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
import com.ycr.activity.YcinfoActivity;
import com.ycr.activity.YcpyActivity;
import com.ycr.pojo.App;
import com.ycr.pojo.ApplicationDate;
import com.ycr.pojo.CompanysInfo;
import com.ycr.pojo.CompanysList;
import com.ycr.pojo.HttpResult;
import com.ycr.pojo.HttpResultList;
import com.ycr.pojo.StateInfo;
import com.ycr.util.BitmapHelp;
import com.ycr.util.MyFragment;
import com.ycr.util.PublicJsonParse;
import com.ycr.util.SlideButton;
import com.ycr.util.SlideButton.OnChangedListener;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;
/**
 * ListView
 * @author Administrator
 *
 */
public class FragmentGzsz extends MyFragment implements OnChangedListener,OnClickListener{
	private List<CompanysInfo> dataList=new ArrayList<CompanysInfo>();
	private StateInfo stateInfo=new StateInfo();
	private SlideButton slideButton;  
	private View titleLayout;
	private BaseAdapter adapter;
	private SharedPreferences sharedPreferences;
    private BitmapUtils bitmapUtils;
    private BitmapDisplayConfig bigPicDisplayConfig;
    private ListView listView;
    private CheckBox radioinfo;
    private LinearLayout tjyctitle;
    private LinearLayout wdyctitle;
    private View grsminfo;
    private TextView dz;
    private Drawable drawable;
    public FragmentGzsz(){}
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_gzsz, container, false);
    	drawable=getResources().getDrawable(R.drawable.photo);
    	slideButton=null;
    	sharedPreferences=getActivity().getSharedPreferences("UserInfoFile", Context.MODE_PRIVATE);//共享数据
		listView = (ListView) view.findViewById(R.id.listview);
		View layout=LayoutInflater.from(getActivity()).inflate(R.layout.gzsz_header, null);
		titleLayout=view.findViewById(R.id.titleLayout);
		listView.addHeaderView(layout);
        tjyctitle=(LinearLayout)view.findViewById(R.id.tjyctitle);
        wdyctitle=(LinearLayout)view.findViewById(R.id.wdyctitle);
        dz=(TextView)view.findViewById(R.id.dz);
        grsminfo=(View)view.findViewById(R.id.grsminfo);
        grsminfo.setOnClickListener(this);
        slideButton = (SlideButton)view.findViewById(R.id.slidebutton);  
        slideButton.SetOnChangedListener(this);
        radioinfo=(CheckBox)view.findViewById(R.id.radioinfo);
        radioinfo.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					stateInfo.setIsUnClear("1");
					buttonView.setBackgroundResource(R.drawable.radio_select);
				}
				else{
					stateInfo.setIsUnClear("0");
					buttonView.setBackgroundResource(R.drawable.radio_default);
				}
				updateState();
			}
		});
		adapter = new BaseAdapter() {
			public View getView(int position, View convertView, ViewGroup parent) {
				 CompanysInfo user=dataList.get(position);
				 convertView=LayoutInflater.from(getActivity()).inflate(R.layout.tjyc_item, null);
				 
				 TextView userid = (TextView) convertView.findViewById(R.id.ycid);
				 final String id=user.getCompanyId();
				 userid.setText(id);
				 ImageView image=(ImageView)convertView.findViewById(R.id.image);
				 if(user.getCompanyPhotoUrl().equals("")||user.getCompanyPhotoUrl()==null)
						image.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.photo));
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
						intent.putExtra("id", id);
						intent.putExtra("name", names);
						intent.setClass(getActivity(), YcpyActivity.class);
						getActivity().startActivity(intent);
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
				CompanysInfo info=dataList.get(arg2-1);
				Intent intent = new Intent();
				intent.putExtra("id", info.getCompanyId());
				intent.putExtra("name", info.getCompanyName());
				intent.putExtra("isHaveCompany", info.getIsHaveCompany());
				intent.putExtra("type", info.getIsOwner());
				intent.setClass(getActivity(), YcinfoActivity.class);
				getActivity().startActivity(intent);
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {  
            @Override  
            public void onScrollStateChanged(AbsListView view, int scrollState) {  
  
            }  
            @Override  
            public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {  
                if (firstVisibleItem >= 1) {  
                	titleLayout.setVisibility(View.VISIBLE);  
                } else {  
  
                	titleLayout.setVisibility(View.GONE);  
                }  
            }  
        });  
		getState();
		return view;
     }
     private void findCompany() {
    	 BDLocation location=ApplicationDate.getInstance().getLocation();
  		RequestParams params = new RequestParams();
  		try {
  			JSONObject json=new JSONObject();
  			json.put("accountId", sharedPreferences.getString("id", ""));
  			json.put("myCompany", false);
  			json.put("recommendCompany",true);
  			json.put("nearCompany",false);
  			json.put("pageNum","1");
  			json.put("pageSize","6");
  			json.put("price","");
  			json.put("province","");
  			json.put("city","");
  			json.put("region","");
  			if(location!=null){
  				json.put("longitude",location.getLongitude());
  				json.put("latitude", location.getLatitude());
  	 			}
  			json.put("request", PublicJsonParse.getPublicParam("findCompany"));
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
  		http.send(HttpRequest.HttpMethod.POST,App.service+"findCompany",params,
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
  		        	Type typeToken = new TypeToken<HttpResultList<CompanysList>>(){}.getType();
  		        	String jsonElement=responseInfo.result.replace("[]", "null");
  		        	HttpResultList<CompanysList> result=gson.fromJson(jsonElement,typeToken);
  		        	if(result!=null)
  		        	{
	  		        	if(result.isResult()){
	  		        		for (CompanysList orderList : result.getData()) {
	  		        			if(orderList.getType()==2){
	  		        				dataList=orderList.getCompanys();
	  		        				break;
	  		        			}else{
	  		        				dataList=new ArrayList<CompanysInfo>();
	  		        			}
	 						}
	  		        		dataList=dataList==null?new ArrayList<CompanysInfo>():dataList;
	  		        		adapter.notifyDataSetChanged();
	  		        	}else{
	  		        		Toast toast = Toast.makeText(getActivity().getApplicationContext(),result.getError(), Toast.LENGTH_SHORT);
	  						toast.setGravity(Gravity.CENTER, 0, 0);
	  						toast.show();
	  		        	}
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
	@Override
	public void OnChanged(boolean b) {
		if (b) {  
			stateInfo.setIsWork("1");
			tjyctitle.setVisibility(View.VISIBLE);
			findCompany();
        } else {  
        	stateInfo.setIsWork("0");
        	tjyctitle.setVisibility(View.GONE);
			dataList=new ArrayList<CompanysInfo>();
			adapter.notifyDataSetChanged();
        } 
		updateState();
	}
	private void getState() {
 		RequestParams params = new RequestParams();
 		try {
 			JSONObject json=new JSONObject();
 			json.put("accountId", sharedPreferences.getString("id", ""));
 			json.put("request", PublicJsonParse.getPublicParam("getState"));
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
 		http.send(HttpRequest.HttpMethod.POST,App.service+"getState",params,
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
 		        	Type typeToken = new TypeToken<HttpResult<StateInfo>>(){}.getType();
 		        	String jsonElement=responseInfo.result.replace("[]", "null");
 		        	HttpResult<StateInfo> result=gson.fromJson(jsonElement,typeToken);
 		        	if(result.isResult()){
 		        		stateInfo=result.getData();
 		        		stateInfo=stateInfo==null?new StateInfo():stateInfo;
 		        		//加载值
 		        		initViewValue();
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
	private void updateState() {
 		RequestParams params = new RequestParams();
 		try {
 			JSONObject json=new JSONObject();
 			json.put("accountId", sharedPreferences.getString("id", ""));
 			json.put("isWork", stateInfo.getIsWork());
 			json.put("isGoOut", stateInfo.getIsGoOut());
 			json.put("isUnClear",stateInfo.getIsUnClear());
 			json.put("request", PublicJsonParse.getPublicParam("updateState"));
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
 		http.send(HttpRequest.HttpMethod.POST,App.service+"updateState",params,
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
 		        	Type typeToken = new TypeToken<HttpResult<StateInfo>>(){}.getType();
 		        	String jsonElement=responseInfo.result.replace("[]", "null");
 		        	HttpResult<StateInfo> result=gson.fromJson(jsonElement,typeToken);
 		        	if(result.isResult()){
 		        		//stateInfo=result.getData();
 		        		//stateInfo=stateInfo==null?new StateInfo():stateInfo;
 		        		//initViewValue();
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
	private void initViewValue(){
		radioinfo.setChecked(stateInfo.getIsUnClear().equals("1")?true:false);
		if(radioinfo.isChecked())
			radioinfo.setBackgroundResource(R.drawable.radio_select);
		else
			radioinfo.setBackgroundResource(R.drawable.radio_default);
        slideButton.setCheck(stateInfo.getIsWork().equals("1")?true:false);
        if(slideButton.isChecked()){
        	tjyctitle.setVisibility(View.VISIBLE);
        	findCompany();
        }
        if(stateInfo.getIsGoOut().equals("1"))
        	dz.setText("是");
        else if(stateInfo.getIsGoOut().equals("0"))
        	dz.setText("否");
        else
        	dz.setText("待定");
	}
	@Override
	public void onClick(View v) {
		new ActionSheetDialog(getActivity())
		.builder()
		.setTitle("是否方便")
		.setCancelable(false)
		.setCanceledOnTouchOutside(false)
		.addSheetItem("是", SheetItemColor.Blue,
				new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						dz.setText("是");
						stateInfo.setIsGoOut("1");
						updateState();
					}
				})
		.addSheetItem("否", SheetItemColor.Blue,
				new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						dz.setText("否");
						stateInfo.setIsGoOut("0");
						updateState();
					}
				})
		.addSheetItem("待定", SheetItemColor.Blue,
				new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						dz.setText("待定");
						stateInfo.setIsGoOut("2");
						updateState();
					}
				}).show();
	}
}
