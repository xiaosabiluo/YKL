package com.ycr.slidemenu;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TabWidget;
import android.widget.TextView;
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
import com.ycr.adapter.TjycAdapter;
import com.ycr.pojo.App;
import com.ycr.pojo.ApplicationDate;
import com.ycr.pojo.CompanysInfo;
import com.ycr.pojo.CompanysList;
import com.ycr.pojo.HttpResultList;
import com.ycr.util.MyFragment;
import com.ycr.util.PublicJsonParse;
import com.ycr.view.MyListGView;
import com.ycr.view.MyListGView.OnRefreshListener;
/**
 * 朋友圈页面
 * @author Administrator
 *
 */
public class FragmentTjyc extends MyFragment implements OnItemClickListener{
	private List<CompanysInfo> dataList=new ArrayList<CompanysInfo>();
	private TjycAdapter adapter;
	private TabWidget mTabWidget;
	private String[] addresses = { "综合排序", "距离", "星级" };
    private Button[] mBtnTabs = new Button[addresses.length];
    private SharedPreferences sharedPreferences;
    private static int type=0;
    private ImageButton ivTitleBtnRight;
    private MyListGView listView;
    private GridView gridView;
    private static boolean flagFar=false;
    private static boolean flagStar=false;
    public FragmentTjyc(){}
    public FragmentTjyc(ImageButton ivTitleBtnRight1){
    	ivTitleBtnRight=ivTitleBtnRight1;
    }
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	final View view = inflater.inflate(R.layout.fragment_tjyc, container, false);
        /*ImageSpan imageSpan = new ImageSpan(getResources().getDrawable(R.drawable.sort_up));
        SpannableString spannableString = new SpannableString(addresses[1]);
        spannableString.setSpan(imageSpan, spannableString.length() - 1, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);*/
    	sharedPreferences=getActivity().getSharedPreferences("UserInfoFile", Context.MODE_PRIVATE);//共享数据
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
    	mTabWidget = (TabWidget) view.findViewById(R.id.tabWidget);
        mTabWidget.setStripEnabled(false);
        mBtnTabs[0] = new Button(getActivity());
        mBtnTabs[0].setFocusable(true);
        mBtnTabs[0].setText(addresses[0]);
        mBtnTabs[0].setTextColor(Color.RED);
        mBtnTabs[0].setBackgroundColor(Color.TRANSPARENT);
        //mBtnTabs[0].setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.sort_up), null);
        mBtnTabs[0].setBackgroundResource(R.drawable.wdzl_bg_selector);
        mTabWidget.addView(mBtnTabs[0]);
        /* 
         * Listener必须在mTabWidget.addView()之后再加入，用于覆盖默认的Listener，
         * mTabWidget.addView()中默认的Listener没有NullPointer检测。
         */
        mBtnTabs[0].setOnClickListener(mTabClickListener);
        mBtnTabs[1] = new Button(getActivity());
        mBtnTabs[1].setFocusable(true);
        mBtnTabs[1].setText(addresses[1]);
        mBtnTabs[1].setTextColor(Color.GRAY);
        mBtnTabs[1].setBackgroundColor(Color.TRANSPARENT);
        mBtnTabs[1].setBackgroundResource(R.drawable.wdzl_bg_selector);
        //mBtnTabs[1].setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.sort_up), null);
        mTabWidget.addView(mBtnTabs[1]);
        mBtnTabs[1].setOnClickListener(mTabClickListener);
        mBtnTabs[2] = new Button(getActivity());
        mBtnTabs[2].setFocusable(true);
        mBtnTabs[2].setText(addresses[2]);
        mBtnTabs[2].setTextColor(Color.GRAY);
        mBtnTabs[2].setBackgroundColor(Color.TRANSPARENT);
        //mBtnTabs[2].setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.sort_up), null);
        mBtnTabs[2].setBackgroundResource(R.drawable.wdzl_bg_selector);
        mTabWidget.addView(mBtnTabs[2]);
        mBtnTabs[2].setOnClickListener(mTabClickListener);
        listView = (MyListGView) view.findViewById(R.id.listView);
        gridView = (GridView) view.findViewById(R.id.gridview);
    	updateLayout();
    	return view;
    }
    private void updateLayout(){
    	if(isClickFlag()){
    		gridView.setVisibility(View.GONE);
    		listView.setVisibility(View.VISIBLE);
    		adapter=new TjycAdapter(getActivity(), dataList,true);
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
        	adapter=new TjycAdapter(getActivity(), dataList,false);
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
		intent.putExtra("isHavingJoin", info.getIsHavingJoin());
		intent.setClass(getActivity(), YcinfoActivity.class);
		getActivity().startActivity(intent);
	}
    private OnClickListener mTabClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v == mBtnTabs[0])
            {
                mBtnTabs[0].setTextColor(Color.RED);
                mBtnTabs[1].setTextColor(Color.GRAY);
                mBtnTabs[2].setTextColor(Color.GRAY);
                type=0;
                findCompany();
            } else if (v == mBtnTabs[1])
            {
                mBtnTabs[0].setTextColor(Color.GRAY);
                mBtnTabs[1].setTextColor(Color.RED);
                mBtnTabs[2].setTextColor(Color.GRAY);
                type=1;
				sortStringMethod(dataList);
                adapter.setDataList(dataList);
                adapter.notifyDataSetChanged();
                /*if(flagFar)
                	mBtnTabs[1].setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.sort_up), null);
                else
                	mBtnTabs[1].setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.sort_down), null);*/
                flagFar=!flagFar;
            } else if (v == mBtnTabs[2])
            {
                mBtnTabs[0].setTextColor(Color.GRAY);
                mBtnTabs[1].setTextColor(Color.GRAY);
                mBtnTabs[2].setTextColor(Color.RED);
                type=2;
				sortStringMethod(dataList);
                adapter.setDataList(dataList);
                adapter.notifyDataSetChanged();
                /*if(flagStar)
                	mBtnTabs[2].setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.sort_up), null);
                else
                	mBtnTabs[2].setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.sort_down), null);*/
                flagStar=!flagStar;
            }
		}
    	
    };
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
 		        	if(result!=null&&result.isResult()){
 		        		for (CompanysList orderList : result.getData()) {
 		        			if(orderList.getType()==2){
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
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static void sortStringMethod(List<CompanysInfo> list){  
        Collections.sort(list, new Comparator(){  
            public int compare(Object o1, Object o2) {  
            	CompanysInfo infoa=(CompanysInfo)o1;  
            	CompanysInfo infob=(CompanysInfo)o2;  
            	if(type==1){
            		if(flagFar)
            			return new Double(infoa.getCompanyDistance()).compareTo(new Double(infob.getCompanyDistance()));
            		else
            			return new Double(infob.getCompanyDistance()).compareTo(new Double(infoa.getCompanyDistance()));
            	}
            	else{
            		if(flagStar)
            			return infoa.getLowerConsume().compareTo(infob.getLowerConsume());
            		else
            			return infob.getLowerConsume().compareTo(infoa.getLowerConsume());
            	}
            	
            }             
        });  
    }
}

