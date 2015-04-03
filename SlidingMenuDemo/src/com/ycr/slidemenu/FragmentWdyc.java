package com.ycr.slidemenu;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.location.BDLocation;
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
import com.ycr.activity.YcinfoActivity;
import com.ycr.adapter.WdycAdapter;
import com.ycr.pojo.App;
import com.ycr.pojo.ApplicationDate;
import com.ycr.pojo.CompanysInfo;
import com.ycr.pojo.CompanysList;
import com.ycr.pojo.HttpResultList;
import com.ycr.pojo.UserInfo;
import com.ycr.util.GPSDetection;
import com.ycr.util.GPSHelp;
import com.ycr.util.MyFragment;
import com.ycr.util.PublicJsonParse;
import com.ycr.view.MyListGView;
import com.ycr.view.MyListGView.OnRefreshListener;
/**
 * 朋友圈页面
 * @author Administrator
 *
 */
public class FragmentWdyc extends MyFragment implements OnItemClickListener{
	private GPSDetection GPSDetection=null;
	private List<CompanysInfo> dataList=new ArrayList<CompanysInfo>();
	private WdycAdapter adapter;
    private SharedPreferences sharedPreferences;
    private MyListGView listView;
    private GridView gridView;
    private ImageButton ivTitleBtnRight;
    private MyOnClickListener listener;
    public FragmentWdyc(){}
    public FragmentWdyc(ImageButton ivTitleBtnRight1){
    	ivTitleBtnRight=ivTitleBtnRight1;
    }
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_wdyc, container, false);
    	GPSDetection=new GPSDetection(getActivity());
        boolean gps_status=GPSDetection.GPSProvider();
        if(!gps_status)
        {
        	//new AlertDialog.Builder(LoginActivity.this).setTitle("GPS检测提示").setMessage("GPS未开启！").show();
        	final AlertDialog.Builder dialog02 = new AlertDialog.Builder(getActivity());
            dialog02.setTitle("GPS检测提示");
            dialog02.setIcon(R.drawable.ic_launcher);
            dialog02.setMessage("GPS未开启，是否开启？");
            dialog02.setPositiveButton("确定", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialoginterface, int which)
                {
                    GPSDetection.OpenGPS();
                }
            });
            dialog02.setNegativeButton("取消", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialoginterface, int which)
                {
                	dialog02.create().cancel();
                }
            });
            dialog02.create().show(); 
        }
    	sharedPreferences=getActivity().getSharedPreferences("UserInfoFile", Context.MODE_PRIVATE);//共享数据
    	listener=new MyOnClickListener();
    	ivTitleBtnRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateLayout();
				if(isClickFlag())
					v.setBackgroundResource(R.drawable.btn_sp);
				else
					v.setBackgroundResource(R.drawable.btn_hp);
			}
		});
    	listView = (MyListGView) view.findViewById(R.id.listView);
        gridView = (GridView) view.findViewById(R.id.gridview);
        updateLayout();
    	return view;
    }
     private void updateLayout(){
     	if(isClickFlag()){
     		gridView.setVisibility(View.GONE);
     		listView.setVisibility(View.VISIBLE);
     		adapter=new WdycAdapter(getActivity(), dataList,true,listener);
 			listView.setAdapter(adapter);
 			listView.setOnItemClickListener(this);
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
     }else{
         	gridView.setVisibility(View.VISIBLE);
     		listView.setVisibility(View.GONE);
         	adapter=new WdycAdapter(getActivity(), dataList,false,listener);
 			gridView.setAdapter(adapter);
 			gridView.setOnItemClickListener(this);
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
     	findCompany();
     	setClickFlag(!isClickFlag());
     }
     @Override
 	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
     	CompanysInfo info=null;
     	if(!isClickFlag())
     		info=dataList.get(arg2-1);
     	else
     		info=dataList.get(arg2);
 		Intent intent = new Intent();
 		intent.putExtra("id", info.getCompanyId());
 		intent.putExtra("name", info.getCompanyName());
 		intent.putExtra("isHaveCompany", info.getIsHaveCompany());
 		intent.putExtra("type", info.getIsOwner());
 		intent.setClass(getActivity(), YcinfoActivity.class);
 		getActivity().startActivity(intent);
 	}
    private void findCompany() {
    	BDLocation location=ApplicationDate.getInstance().getLocation();
 		RequestParams params = new RequestParams();
 		try {
 			JSONObject json=new JSONObject();
 			json.put("accountId", sharedPreferences.getString("id", ""));
 			json.put("myCompany", true);
 			json.put("recommendCompany",false);
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
 		        	if(result!=null&&result.isResult()){
 		        		for (CompanysList orderList : result.getData()) {
 		        			if(orderList.getType()==1){
 		        				dataList=orderList.getCompanys();
 		        				dataList=dataList==null?new ArrayList<CompanysInfo>():dataList;
 		        				break;
 		        			}else{
 		        				dataList=new ArrayList<CompanysInfo>();
 		        			}
						}
 		        		adapter.setDataList(dataList);
 		        		adapter.notifyDataSetChanged();
 		        	}else{
 		        		Toast toast = Toast.makeText(getActivity().getApplicationContext(),"网络异常!", Toast.LENGTH_SHORT);
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
    private void JoinOrQuitCompany(String id,String isHaveCompany) {
		LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
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
 		        		Toast toast = Toast.makeText(getActivity().getApplicationContext(),"已退出夜场", Toast.LENGTH_SHORT);
 						toast.setGravity(Gravity.CENTER, 0, 0);
 						toast.show();
 						findCompany();
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
    public class MyOnClickListener implements OnClickListener {  
        int position;  
        public void setPosition(int position) {  
            this.position = position;  
        }
    	@Override
    	public void onClick(View v) {
    		CompanysInfo info=dataList.get(position);
    		JoinOrQuitCompany(info.getCompanyId(),info.getIsHaveCompany());
    	}
    } 
}

