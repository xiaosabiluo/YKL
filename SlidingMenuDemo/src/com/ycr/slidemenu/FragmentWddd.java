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
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ycr.pojo.App;
import com.ycr.pojo.ApplicationDate;
import com.ycr.pojo.HttpResult;
import com.ycr.pojo.HttpResultList;
import com.ycr.pojo.OrderInfo;
import com.ycr.pojo.OrderList;
import com.ycr.util.BitmapHelp;
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
public class FragmentWddd extends MyFragment{
	private List<OrderInfo> dataList=new ArrayList<OrderInfo>();
	private BaseAdapter adapter;
	private TabWidget mTabWidget;
	private String[] addresses = { "今天", "历史" };
    private Button[] mBtnTabs = new Button[addresses.length];
    private String phone="";
    private int currentItem=-1;
    private boolean flag=false;
    private SharedPreferences sharedPreferences;
    private BitmapUtils bitmapUtils;
    private BitmapDisplayConfig bigPicDisplayConfig;
    private int type=2;
    private String orderId="";//消息传递过来的参数
    private AlertDialog dialog;
    private AlertDialog.Builder  builder;
    public FragmentWddd(){}
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_wddd, container, false);
    	orderId=ApplicationDate.getInstance().getOrderId();
    	sharedPreferences=getActivity().getSharedPreferences("UserInfoFile", Context.MODE_PRIVATE);//共享数据
    	mTabWidget = (TabWidget) view.findViewById(R.id.tabWidget);
        mTabWidget.setStripEnabled(false);
        mBtnTabs[0] = new Button(getActivity());
        mBtnTabs[0].setFocusable(true);
        mBtnTabs[0].setText(addresses[0]);
        mBtnTabs[0].setTextColor(Color.RED);
        mBtnTabs[0].setBackgroundColor(Color.TRANSPARENT);
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
        mTabWidget.addView(mBtnTabs[1]);
        mBtnTabs[1].setOnClickListener(mTabClickListener);
        getMyOrder();
        if(orderId!=null&&!orderId.equals("")){
			getMyOrderDetail();
		}
		final MyListGView listView = (MyListGView) view.findViewById(R.id.listView);
		class Holder {
			private TextView ycid;
			private ImageView image;
			private TextView ycname;
			private TextView mony;
			private TextView date;
			private TextView space;
			private ImageView show;
			private ImageView phoneImage;
			private RelativeLayout layout;
			private TextView username;
			private TextView phone;
		}
		adapter = new BaseAdapter() {
			public View getView(int position, View convertView, ViewGroup parent) {
				final Holder holder;
				if (convertView == null) {
					holder = new Holder();
					convertView = View.inflate(getActivity(), R.layout.wddd_item, null);
					holder.ycid = (TextView) convertView.findViewById(R.id.ycid);
					holder.image = (ImageView) convertView.findViewById(R.id.image);
					holder.ycname = (TextView) convertView.findViewById(R.id.ycname);
					holder.mony = (TextView) convertView.findViewById(R.id.mony);
					holder.date = (TextView) convertView.findViewById(R.id.date);
					holder.space = (TextView) convertView.findViewById(R.id.space);
					holder.show = (ImageView) convertView.findViewById(R.id.show);
					holder.layout = (RelativeLayout) convertView.findViewById(R.id.layout);
					holder.username = (TextView) convertView.findViewById(R.id.username);
					holder.phone = (TextView) convertView.findViewById(R.id.phone);
					holder.phoneImage = (ImageView) convertView.findViewById(R.id.phoneImage);
					holder.phoneImage.setOnClickListener(btnOnclick);
					holder.phone.setOnClickListener(btnOnclick);
					convertView.setTag(holder);
				} else {
					holder = (Holder) convertView.getTag();
				}
				final OrderInfo item = dataList.get(position);
				if(item!=null){
					holder.ycid.setText(item.getClubId());
					holder.image.setImageResource(R.drawable.ic_launcher);
					if(item.getClubLogo().equals("")||item.getClubLogo()==null)
						holder.image.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.photo));
					 else
						photo(holder.image,item.getClubLogo());
					holder.ycname.setText(item.getClubName());
					holder.mony.setText(item.getClubRoom());
					holder.date.setText(item.getOrderTime());
					if(item.getOrderStatus().equals("1"))//1=预约2确认3取消4结束5出台
						holder.space.setText("预约");
					if(item.getOrderStatus().equals("2"))//1=预约2确认3取消4结束5出台
						holder.space.setText("确认");
					if(item.getOrderStatus().equals("3"))//1=预约2确认3取消4结束5出台
						holder.space.setText("取消");
					if(item.getOrderStatus().equals("4"))//1=预约2确认3取消4结束5出台
						holder.space.setText("结束");
					if(item.getOrderStatus().equals("5"))//1=预约2确认3取消4结束5出台
						holder.space.setText("出台");
					holder.username.setText(item.getNickName());
					holder.phone.setText(item.getMobile());
					phone=item.getMobile();
					if (position == currentItem) {
						holder.layout.setVisibility(View.VISIBLE);
						holder.show.setImageResource(R.drawable.btn_up);
						if(!flag){
							holder.layout.setVisibility(View.GONE);
							holder.show.setImageResource(R.drawable.btn_down);
						}
					}else{
						holder.layout.setVisibility(View.GONE);
						holder.show.setImageResource(R.drawable.btn_down);
					}
				}
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
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				flag=!flag;
				currentItem=position-1;
				adapter.notifyDataSetChanged();
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
    private OnClickListener mTabClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v == mBtnTabs[0])
            {
                mBtnTabs[0].setTextColor(Color.RED);
                mBtnTabs[1].setTextColor(Color.GRAY);
                type=2;
            } else if (v == mBtnTabs[1])
            {
                mBtnTabs[0].setTextColor(Color.GRAY);
                mBtnTabs[1].setTextColor(Color.RED);
                type=1;
            }
			getMyOrder();
		}
    	
    };
    private OnClickListener btnOnclick=new View.OnClickListener(){
    	@Override
    	public void onClick(View v) {
	    	if(phone!=null&&!"".equals(phone.trim()))
	    	{
	    		Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));
	        	startActivity(intent);
	    	}
    	}
    };
    private void getMyOrder() {
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
 			json.put("request", PublicJsonParse.getPublicParam("getMyOrder"));
 			params.addHeader("Content-Type", "application/json");
 			params.setBodyEntity(new StringEntity(json.toString()));
 		} catch (JSONException e) {
 			e.printStackTrace(); 
 		}
 		catch (UnsupportedEncodingException e) {
 			e.printStackTrace();
 		}
 		HttpUtils http = new HttpUtils();
 		http.send(HttpRequest.HttpMethod.POST,App.service+"getMyOrder",params,
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
 		        	Type typeToken = new TypeToken<HttpResultList<OrderList>>(){}.getType();
 		        	String jsonElement=responseInfo.result.replace("[]", "null");
 		        	HttpResultList<OrderList> result=gson.fromJson(jsonElement,typeToken);
 		        	if(result.isResult()){
 		        		for (OrderList orderList : result.getData()) {
 		        			if(orderList.getType()==type){
 		        				dataList=orderList.getOrders();
 		        				dataList=dataList==null?new ArrayList<OrderInfo>():dataList;
 		        				break;
 		        			}else{
 		        				dataList=new ArrayList<OrderInfo>();
 		        			}
						}
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
    private void getMyOrderDetail() {
 		RequestParams params = new RequestParams();
 		try {
 			JSONObject json=new JSONObject();
 			json.put("accountId", sharedPreferences.getString("id", ""));
 			json.put("orderId",orderId);
 			json.put("request", PublicJsonParse.getPublicParam("getMyOrderDetail"));
 			params.addHeader("Content-Type", "application/json");
 			params.setBodyEntity(new StringEntity(json.toString()));
 		} catch (JSONException e) {
 			e.printStackTrace(); 
 		}
 		catch (UnsupportedEncodingException e) {
 			e.printStackTrace();
 		}
 		HttpUtils http = new HttpUtils();
 		http.send(HttpRequest.HttpMethod.POST,App.service+"getMyOrderDetail",params,
 		    new RequestCallBack<String>(){
 		        @Override
 		        public void onSuccess(ResponseInfo<String> responseInfo) {
 		        	Gson gson=new GsonBuilder().serializeNulls().create();
 		        	Type typeToken = new TypeToken<HttpResult<OrderInfo>>(){}.getType();
 		        	String jsonElement=responseInfo.result.replace("[]", "null");
 		        	HttpResult<OrderInfo> result=gson.fromJson(jsonElement,typeToken);
 		        	if(result.isResult()){
 		        		if(result.getData().getOrderStatus().equals("1"))
 		        			dialog(result.getData().getClubName(),result.getData().getClubRoom(),result.getData().getRemark());
 		        		else{
 		        			Editor editor = sharedPreferences.edit();
 	 					    editor.putString("orderId","");
 	 					    editor.commit();
 	 					    ApplicationDate.getInstance().setOrderId("");
 		        			dialog.cancel();
 		        		}
 		        	}else{
 		        		/*Toast toast = Toast.makeText(getApplicationContext(),result.getError(), Toast.LENGTH_SHORT);
 						toast.setGravity(Gravity.CENTER, 0, 0);
 						toast.show();*/
 		        	}
 		        }
 		        @Override
 		        public void onFailure(HttpException error, String msg) {
 		        	Toast toast = Toast.makeText(getActivity().getApplicationContext(),"网络异常", Toast.LENGTH_SHORT);
 					toast.setGravity(Gravity.CENTER, 0, 0);
 					toast.show();
 		        }
 		});
 	}
    public void dialog(String clubName,String clubRoom,String remark){
    	builder=new AlertDialog.Builder(getActivity());
    	builder.setTitle("新预定")  
    	.setMessage("你有一个新预定\n"+clubName+"\n房间号:"+clubRoom+"   备注:"+remark)
    	.setPositiveButton("同意", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ApplicationDate.getInstance().setOrderId("");
				confirmOrder("a");
				dialog.cancel();
			}
		})
    	.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ApplicationDate.getInstance().setOrderId("");
				confirmOrder("r");
				dialog.cancel();
			}
		});
    	dialog=builder.create();
    	dialog.setCancelable(false);
    	dialog.show();
    }
	private void confirmOrder(String confirmStatus) {
 		RequestParams params = new RequestParams();
 		try {
 			JSONObject json=new JSONObject();
 			json.put("accountId", sharedPreferences.getString("id", ""));
 			json.put("orderId",orderId);
 			json.put("confirmStatus", confirmStatus);
 			json.put("request", PublicJsonParse.getPublicParam("confirmOrder"));
 			params.addHeader("Content-Type", "application/json");
 			params.setBodyEntity(new StringEntity(json.toString()));
 		} catch (JSONException e) {
 			e.printStackTrace(); 
 		}
 		catch (UnsupportedEncodingException e) {
 			e.printStackTrace();
 		}
 		HttpUtils http = new HttpUtils();
 		http.send(HttpRequest.HttpMethod.POST,App.service+"confirmOrder",params,
 		    new RequestCallBack<String>(){
 		        @Override
 		        public void onSuccess(ResponseInfo<String> responseInfo) {
 		        	Gson gson=new GsonBuilder().serializeNulls().create();
 		        	Type typeToken = new TypeToken<HttpResultList<OrderList>>(){}.getType();
 		        	String jsonElement=responseInfo.result.replace("[]", "null");
 		        	HttpResultList<OrderList> result=gson.fromJson(jsonElement,typeToken);
 		        	if(result==null){
	 		        	/*if(result.isResult()){
	 		        		Toast toast = Toast.makeText(getApplicationContext(),result.getError(), Toast.LENGTH_SHORT);
	 						toast.setGravity(Gravity.CENTER, 0, 0);
	 						toast.show();
	 		        	}else{
	 		        		Toast toast = Toast.makeText(getApplicationContext(),result.getError(), Toast.LENGTH_SHORT);
	 						toast.setGravity(Gravity.CENTER, 0, 0);
	 						toast.show();
	 		        	}*/
 		        	}
 		        }
 		        @Override
 		        public void onFailure(HttpException error, String msg) {
 		        	Toast toast = Toast.makeText(getActivity().getApplicationContext(),"网络异常", Toast.LENGTH_SHORT);
 					toast.setGravity(Gravity.CENTER, 0, 0);
 					toast.show();
 		        }
 		});
 	}
}

