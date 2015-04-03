package com.ycr.slidemenu;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.igexin.sdk.PushManager;
import com.indoorclub.girl.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.ycr.activity.LoginActivity;
import com.ycr.activity.TjpyActivity;
import com.ycr.activity.UpdateManager;
import com.ycr.pojo.App;
import com.ycr.pojo.ApplicationDate;
import com.ycr.pojo.HttpResult;
import com.ycr.pojo.HttpResultList;
import com.ycr.pojo.MenuInfo;
import com.ycr.pojo.MetadataInfo;
import com.ycr.pojo.OrderInfo;
import com.ycr.pojo.OrderList;
import com.ycr.pojo.PushInfo;
import com.ycr.util.GPSDetection;
import com.ycr.util.MyFragment;
import com.ycr.util.MyLocationListener;
import com.ycr.util.PublicJsonParse;


public class MainActivity extends SlidingFragmentActivity implements OnClickListener{
	private GPSDetection GPSDetection=null;
	public LocationClient mLocationClient = null;
	public MyLocationListener myListener = new MyLocationListener();
	protected SlidingMenu leftRightSlidingMenu;
	private LeftSlidingMenuFragment leftFrag;
	private ImageButton ivTitleBtnLeft;
	private ImageButton ivTitleBtnRight;
	private MyFragment mContent;
	private TextView title;
	public static int status=R.id.wdzlBtnLayout;
	private SharedPreferences sharedPreferences;
	private int type=0;//消息传递过来的参数
	private String orderId="";//消息传递过来的参数
	private MetadataInfo info;
	private AlertDialog dialog;
    private AlertDialog.Builder  builder;
	public MainActivity(){}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GPSDetection=new GPSDetection(this);
        boolean gps_status=GPSDetection.GPSProvider();
        if(!gps_status)
        {
        	//new AlertDialog.Builder(LoginActivity.this).setTitle("GPS检测提示").setMessage("GPS未开启！").show();
        	final AlertDialog.Builder dialog02 = new AlertDialog.Builder(this);
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
		mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
	    mLocationClient.registerLocationListener( myListener );    //注册监听函数
	    mLocationClient.start();
	    mLocationClient.requestLocation();/*
		if(ApplicationDate.getInstance().getInfo()!=null)
			ApplicationDate.getInstance().getInfo().finish();
		PushManager.getInstance().initialize(this.getApplicationContext());*/
		if(UpdateManager.cancelUpdate==false){
			UpdateManager manager = new UpdateManager(MainActivity.this);
			manager.checkUpdate();
		}
		sharedPreferences=MainActivity.this.getSharedPreferences("UserInfoFile", Context.MODE_PRIVATE);//共享数据
		ApplicationDate.getInstance().setInfo(this);
		updateToken();
		info=MetadataInfo.getInstance();
		setContentView(R.layout.activity_main);
		Intent intent=getIntent();
		status=intent.getIntExtra("status",R.id.wdzlBtnLayout);
		type=intent.getIntExtra("type",0);
		//orderId=intent.getStringExtra("orderId");
		//orderId=sharedPreferences.getString("id", "");
		orderId=ApplicationDate.getInstance().getOrderId();
		title=(TextView) findViewById(R.id.ivTitleName);
		initView();
		updateMetadata();
	}
	/*@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		status=getIntent().getIntExtra("status", 0);
		leftFrag.switchTo(status);
	}*/
	private void initView() {
		addUser();
		builder=new AlertDialog.Builder(this);
    	dialog=builder.create();
    	dialog.setCancelable(false);
		Gson gson=new GsonBuilder().serializeNulls().create();
     	Type typeToken = new TypeToken<HttpResult<MenuInfo>>(){}.getType();
     	String jsonElement="{\"result\":\"TRUE\",\"error\":\"\",\"data\":{\"menus\":{\"wdzl\":{\"menuValue\":\"我的资料\",\"menuSequence\":\"1\"},\"tjyc\":{\"menuValue\":\"推荐会所\",\"menuSequence\":\"4\"},\"wdyc\":{\"menuValue\":\"我的会所\",\"menuSequence\":\"3\"},\"gzsz\":{\"menuValue\":\"交友设置\",\"menuSequence\":\"5\"},\"wddd\":{\"menuValue\":\"我的约会\",\"menuSequence\":\"6\"},\"xx\":{\"menuValue\":\"消息\",\"menuSequence\":\"7\"},\"wdpy\":{\"menuValue\":\"我的朋友\",\"menuSequence\":\"2\"}},\"metadataMap\":{\"\":{},\"wdzl\":{\"grxc\":\"个人相册\",\"title\":\"我的资料\",\"grsp\":\"个人视频\",\"grzl_data\":{\"item2\":\"昵称\",\"item4\":\"生日\",\"item1\":\"头像\",\"item3\":\"性别\",\"item6\":\"星座\",\"item5\":\"故乡\",\"item7\":\"个人说明\"},\"grzl\":\"个人资料\"},\"jryzh\":{\"item1\":\"加入会所\",\"title\":\"加入会所\"},\"yzhxq\":{\"item2\":\"地址\",\"item4\":\"经理\",\"item1\":\"简介\",\"item3\":\"星级\",\"title\":\"会所详情\",\"item6\":\"加入会所\",\"item5\":\"图片\",\"item7\":\"退出会所\"},\"hyxq\":{\"item2\":\"个人说明\",\"item1\":\"当前位置\",\"item3\":\"加入的会所\",\"title\":\"好友详情\",\"status\":{\"1\":\"有空\",\"0\":\"没空\"}},\"yzhsx\":{\"item2\":\"区域\",\"item1\":\"星级\",\"title\":\"会所筛选\"},\"tjyc\":{\"item2\":\"综合排序\",\"item4\":\"星级\",\"item1\":\"请输入会所名称/地址\",\"item3\":\"距离\",\"title\":\"推荐会所\",\"item5\":\"加入\"},\"wdyc\":{\"item2\":\"加入会所\",\"item1\":\"请输入会所名称/地址\",\"item3\":\"退出\",\"title\":\"我的会所\"},\"gzsz\":{\"item2\":\"保持设置\",\"item4\":\"是否方便\",\"item1\":\"约会\",\"item3\":\"方便\",\"title\":\"工作设置\",\"item6\":\"推荐会所\",\"item5\":\"已加入会所\"},\"wddd\":{\"item2\":\"历史\",\"item1\":\"今天\",\"item3\":\"重置\",\"title\":\"我的约会\",\"status\":{\"3\":\"已取消\",\"2\":\"已确认\",\"1\":\"已预约\",\"5\":\"已成功\",\"4\":\"已结束\"}},\"wdpy\":{\"item1\":\"请输入手机号/昵称\",\"title\":\"我的朋友\",\"status\":{\"2\":\"有空\",\"1\":\"没空\"}}}},\"msgname\":\"updateMetadata\"}";
     	HttpResult<MenuInfo> result=gson.fromJson(jsonElement, typeToken);
 		info.setInfo(result.getData());
		ivTitleBtnLeft = (ImageButton)this.findViewById(R.id.ivTitleBtnLeft);
		ivTitleBtnLeft.setOnClickListener(this);
		ivTitleBtnRight = (ImageButton)this.findViewById(R.id.ivTitleBtnRight);
		ivTitleBtnRight.setOnClickListener(this);
		if(status==R.id.tjycBtnLayout)
			title.setText(getResources().getString(R.string.menu_tjyc));
		setBehindContentView(R.layout.main_left_layout);
		initLeftRightSlidingMenu();
		if(orderId!=null&&!orderId.equals("")){
			getMyOrderDetail();
		}
	}
	
	private void initLeftRightSlidingMenu() {
		//getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mContent).commit();
		FragmentTransaction leftFragementTransaction = getSupportFragmentManager().beginTransaction();
		leftFrag = new LeftSlidingMenuFragment(status);
		leftFragementTransaction.replace(R.id.main_left_fragment, leftFrag);
		leftFragementTransaction.commitAllowingStateLoss();
		// customize the SlidingMenu
		leftRightSlidingMenu = getSlidingMenu();
		leftRightSlidingMenu.setMode(SlidingMenu.LEFT);// 设置是左滑还是右滑，还是左右都可以滑，我这里只做了左滑
		leftRightSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// 设置菜单宽度
		leftRightSlidingMenu.setFadeDegree(0.35f);// 设置淡入淡出的比例
		leftRightSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置手势模式
		leftRightSlidingMenu.setShadowDrawable(R.drawable.shadow);// 设置左菜单阴影图片
		leftRightSlidingMenu.setFadeEnabled(true);// 设置滑动时菜单的是否淡入淡出
		leftRightSlidingMenu.setBehindScrollScale(0.333f);// 设置滑动时拖拽效果
		// go();
		/*leftRightSlidingMenu.setSecondaryMenu(R.layout.main_right_layout);//右边的菜单
		FragmentTransaction rightFragementTransaction = getSupportFragmentManager().beginTransaction();
		Fragment rightFrag = new RightSlidingMenuFragment();
		leftFragementTransaction.replace(R.id.main_right_fragment, rightFrag);
		rightFragementTransaction.commit();*/
	}
	/**
     * 在状态栏显示通知
     */
    public void showNotification(int action,PushInfo push){
    	if(push.getType().equals("0")){
    		removeShared();
    		Intent notificationIntent =new Intent(this, LoginActivity.class);
    		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		this.startActivity(notificationIntent);
        }else
        {
	        // 创建一个NotificationManager的引用   
	        NotificationManager notificationManager = (NotificationManager)this.getSystemService(android.content.Context.NOTIFICATION_SERVICE);   
	        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this); 
	        // 设置通知的事件消息   
	        Intent notificationIntent =new Intent(this, MainActivity.class); // 点击该通知后要跳转的Activity  
	        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        if(push.getType().equals("1")){
	        	mBuilder.setContentTitle("预约消息");
	        	notificationIntent.putExtra("status",R.id.wdddBtnLayout);
	        	notificationIntent.putExtra("type",1);
	        }
	        else if(push.getType().equals("2")){
	        	mBuilder.setContentTitle("系统消息");
	        	notificationIntent.putExtra("status",R.id.xxBtnLayout);
	        }
	        else if(push.getType().equals("5")){
	        	mBuilder.setContentTitle("夜场消息");
	        	notificationIntent.putExtra("status",R.id.wdycBtnLayout);
	        }
	        else{
	        	mBuilder.setContentTitle("预定消息");
	        	notificationIntent.putExtra("status",R.id.wdddBtnLayout);
	        	notificationIntent.putExtra("type",1);
	        	notificationIntent.putExtra("orderId",push.getParam());
	        	notificationIntent.putExtra("content",push.getContent());
	        }
	        mBuilder.setContentText(push.getContent());
	        //mBuilder.setTicker(""); //通知首次出现在通知栏，带上升动画效果的  
	        mBuilder.setWhen(System.currentTimeMillis());//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间  
	        //mBuilder.setPriority(Notification.PRIORITY_DEFAULT); //设置该通知优先级  
	        mBuilder.setAutoCancel(true);//设置这个标志当用户单击面板就可以让通知将自动取消    
	        mBuilder.setOngoing(false);//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)  
	        mBuilder.setDefaults(Notification.DEFAULT_ALL);//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合  
	        //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission  
	        mBuilder.setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON  
	        
	        PendingIntent contentItent = PendingIntent.getActivity(this, PendingIntent.FLAG_CANCEL_CURRENT, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);   
	        mBuilder.setContentIntent(contentItent);
	        // 定义Notification的各种属性   
	        Notification notification =mBuilder.build(); 
	        //FLAG_AUTO_CANCEL   该通知能被状态栏的清除按钮给清除掉
	        //FLAG_NO_CLEAR      该通知不能被状态栏的清除按钮给清除掉
	        //FLAG_ONGOING_EVENT 通知放置在正在运行
	        //FLAG_INSISTENT     是否一直进行，比如音乐一直播放，知道用户响应
			//notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中   
			//notification.flags |= Notification.FLAG_NO_CLEAR; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用   
			//notification.flags |= Notification.FLAG_SHOW_LIGHTS;   
	        //DEFAULT_ALL     使用所有默认值，比如声音，震动，闪屏等等
	        //DEFAULT_LIGHTS  使用默认闪光提示
	        //DEFAULT_SOUNDS  使用默认提示声音
	        //DEFAULT_VIBRATE 使用默认手机震动，需加上<uses-permission android:name="android.permission.VIBRATE" />权限
	        //notification.defaults = Notification.DEFAULT_LIGHTS; 
	        //叠加效果常量
	        //notification.defaults=Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND;
	        notification.ledARGB = Color.BLUE;   
	        notification.ledOnMS =5000; //闪光时间，毫秒
	        // 把Notification传递给NotificationManager   
	        notificationManager.notify(0, notification);  
        }
    }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivTitleBtnLeft:
			leftRightSlidingMenu.showMenu();
			break;
		case R.id.ivTitleBtnRight:
			//leftRightSlidingMenu.showSecondaryMenu(true);//右边的菜单
			switch (status) {
			case R.id.wdjmBtnLayout:
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, TjpyActivity.class);
				MainActivity.this.startActivity(intent);
				finish();
				break;
			case R.id.tjycBtnLayout:
				mContent.setClickFlag(!mContent.isClickFlag());
				//FragmentTjyc.updateLayout();
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		
	}
	/**
	 *    左侧菜单点击切换首页的内容
	 */
	
	public void switchContent(MyFragment fragment) {
		mContent = fragment;
		Bundle bundle=mContent.getArguments();
		status=bundle.getInt("status");
		mContent.setArguments(bundle);
		switch (status) {
		case R.id.wdzlBtnLayout:
			title.setText(info.getInfo().getMenus().get("wdzl").get("menuValue").toString());
			ivTitleBtnRight.setVisibility(View.GONE);
			break;
		case R.id.wdjmBtnLayout:
			title.setText(info.getInfo().getMenus().get("wdpy").get("menuValue").toString());
			ivTitleBtnRight.setBackgroundResource(R.drawable.nav_btn_add_small);
			ivTitleBtnRight.setVisibility(View.VISIBLE);
			break;
		case R.id.wdycBtnLayout:
			title.setText(info.getInfo().getMenus().get("wdyc").get("menuValue").toString());
			ivTitleBtnRight.setBackgroundResource(R.drawable.btn_hp);
			ivTitleBtnRight.setVisibility(View.VISIBLE);
			break;
		case R.id.tjycBtnLayout:
			title.setText(info.getInfo().getMenus().get("tjyc").get("menuValue").toString());
			ivTitleBtnRight.setBackgroundResource(R.drawable.btn_hp);
			ivTitleBtnRight.setVisibility(View.VISIBLE);
			break;
		case R.id.gzszBtnLayout:
			title.setText(info.getInfo().getMenus().get("gzsz").get("menuValue").toString());
			ivTitleBtnRight.setVisibility(View.GONE);
			break;
		case R.id.wdddBtnLayout:
			title.setText(info.getInfo().getMenus().get("wddd").get("menuValue").toString());
			ivTitleBtnRight.setVisibility(View.GONE);
			break;
		default:
			title.setText(info.getInfo().getMenus().get("xx").get("menuValue").toString());
			ivTitleBtnRight.setVisibility(View.GONE);
			break;
		}
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commitAllowingStateLoss();
		getSlidingMenu().showContent();
	}


	public ImageButton getIvTitleBtnRight() {
		return ivTitleBtnRight;
	}


	public void setIvTitleBtnRight(ImageButton ivTitleBtnRight) {
		this.ivTitleBtnRight = ivTitleBtnRight;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK )
		{
			Editor editor = sharedPreferences.edit();
		    editor.putInt("status",status);
		    editor.commit();
		    finish();
		}
		return true;
		
	}
	private void updateMetadata() {
 		RequestParams params = new RequestParams();
 		try {
 			JSONObject json=new JSONObject();
 			json.put("accountId", sharedPreferences.getString("id", ""));
 			json.put("type","b");
 			json.put("request", PublicJsonParse.getPublicParam("updateMetadata"));
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
 		http.send(HttpMethod.POST, App.service+"updateMetadata", params,
 		new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Gson gson=new GsonBuilder().serializeNulls().create();
		     	Type typeToken = new TypeToken<HttpResult<MenuInfo>>(){}.getType();
				String jsonElement=responseInfo.result.replace("[]", "null");
		     	HttpResult<MenuInfo> result=gson.fromJson(jsonElement,typeToken);
		     	if(result.isResult()){
		     		info.setInfo(result.getData());
		     	}else{
		     		jsonElement="{\"result\":\"TRUE\",\"error\":\"\",\"data\":{\"menus\":{\"wdzl\":{\"menuValue\":\"我的资料\",\"menuSequence\":\"1\"},\"tjyc\":{\"menuValue\":\"推荐会所\",\"menuSequence\":\"4\"},\"wdyc\":{\"menuValue\":\"我的会所\",\"menuSequence\":\"3\"},\"gzsz\":{\"menuValue\":\"交友设置\",\"menuSequence\":\"5\"},\"wddd\":{\"menuValue\":\"我的约会\",\"menuSequence\":\"6\"},\"xx\":{\"menuValue\":\"消息\",\"menuSequence\":\"7\"},\"wdpy\":{\"menuValue\":\"我的朋友\",\"menuSequence\":\"2\"}},\"metadataMap\":{\"\":{},\"wdzl\":{\"grxc\":\"个人相册\",\"title\":\"我的资料\",\"grsp\":\"个人视频\",\"grzl_data\":{\"item2\":\"昵称\",\"item4\":\"生日\",\"item1\":\"头像\",\"item3\":\"性别\",\"item6\":\"星座\",\"item5\":\"故乡\",\"item7\":\"个人说明\"},\"grzl\":\"个人资料\"},\"jryzh\":{\"item1\":\"加入会所\",\"title\":\"加入会所\"},\"yzhxq\":{\"item2\":\"地址\",\"item4\":\"经理\",\"item1\":\"简介\",\"item3\":\"星级\",\"title\":\"会所详情\",\"item6\":\"加入会所\",\"item5\":\"图片\",\"item7\":\"退出会所\"},\"hyxq\":{\"item2\":\"个人说明\",\"item1\":\"当前位置\",\"item3\":\"加入的会所\",\"title\":\"好友详情\",\"status\":{\"1\":\"有空\",\"0\":\"没空\"}},\"yzhsx\":{\"item2\":\"区域\",\"item1\":\"星级\",\"title\":\"会所筛选\"},\"tjyc\":{\"item2\":\"综合排序\",\"item4\":\"星级\",\"item1\":\"请输入会所名称/地址\",\"item3\":\"距离\",\"title\":\"推荐会所\",\"item5\":\"加入\"},\"wdyc\":{\"item2\":\"加入会所\",\"item1\":\"请输入会所名称/地址\",\"item3\":\"退出\",\"title\":\"我的会所\"},\"gzsz\":{\"item2\":\"保持设置\",\"item4\":\"是否方便\",\"item1\":\"约会\",\"item3\":\"方便\",\"title\":\"工作设置\",\"item6\":\"推荐会所\",\"item5\":\"已加入会所\"},\"wddd\":{\"item2\":\"历史\",\"item1\":\"今天\",\"item3\":\"重置\",\"title\":\"我的约会\",\"status\":{\"3\":\"已取消\",\"2\":\"已确认\",\"1\":\"已预约\",\"5\":\"已成功\",\"4\":\"已结束\"}},\"wdpy\":{\"item1\":\"请输入手机号/昵称\",\"title\":\"我的朋友\",\"status\":{\"2\":\"有空\",\"1\":\"没空\"}}}},\"msgname\":\"updateMetadata\"}";
		     		result=gson.fromJson(jsonElement, typeToken);
		     		info.setInfo(result.getData());
		     	}
			}
			@Override
	        public void onFailure(HttpException error, String msg) {
	        	Toast toast = Toast.makeText(MainActivity.this.getApplicationContext(),"网络异常", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				String jsonElement="{\"result\":\"TRUE\",\"error\":\"\",\"data\":{\"menus\":{\"wdzl\":{\"menuValue\":\"我的资料\",\"menuSequence\":\"1\"},\"tjyc\":{\"menuValue\":\"推荐会所\",\"menuSequence\":\"4\"},\"wdyc\":{\"menuValue\":\"我的会所\",\"menuSequence\":\"3\"},\"gzsz\":{\"menuValue\":\"交友设置\",\"menuSequence\":\"5\"},\"wddd\":{\"menuValue\":\"我的约会\",\"menuSequence\":\"6\"},\"xx\":{\"menuValue\":\"消息\",\"menuSequence\":\"7\"},\"wdpy\":{\"menuValue\":\"我的朋友\",\"menuSequence\":\"2\"}},\"metadataMap\":{\"\":{},\"wdzl\":{\"grxc\":\"个人相册\",\"title\":\"我的资料\",\"grsp\":\"个人视频\",\"grzl_data\":{\"item2\":\"昵称\",\"item4\":\"生日\",\"item1\":\"头像\",\"item3\":\"性别\",\"item6\":\"星座\",\"item5\":\"故乡\",\"item7\":\"个人说明\"},\"grzl\":\"个人资料\"},\"jryzh\":{\"item1\":\"加入会所\",\"title\":\"加入会所\"},\"yzhxq\":{\"item2\":\"地址\",\"item4\":\"经理\",\"item1\":\"简介\",\"item3\":\"星级\",\"title\":\"会所详情\",\"item6\":\"加入会所\",\"item5\":\"图片\",\"item7\":\"退出会所\"},\"hyxq\":{\"item2\":\"个人说明\",\"item1\":\"当前位置\",\"item3\":\"加入的会所\",\"title\":\"好友详情\",\"status\":{\"1\":\"有空\",\"0\":\"没空\"}},\"yzhsx\":{\"item2\":\"区域\",\"item1\":\"星级\",\"title\":\"会所筛选\"},\"tjyc\":{\"item2\":\"综合排序\",\"item4\":\"星级\",\"item1\":\"请输入会所名称/地址\",\"item3\":\"距离\",\"title\":\"推荐会所\",\"item5\":\"加入\"},\"wdyc\":{\"item2\":\"加入会所\",\"item1\":\"请输入会所名称/地址\",\"item3\":\"退出\",\"title\":\"我的会所\"},\"gzsz\":{\"item2\":\"保持设置\",\"item4\":\"是否方便\",\"item1\":\"约会\",\"item3\":\"方便\",\"title\":\"工作设置\",\"item6\":\"推荐会所\",\"item5\":\"已加入会所\"},\"wddd\":{\"item2\":\"历史\",\"item1\":\"今天\",\"item3\":\"重置\",\"title\":\"我的约会\",\"status\":{\"3\":\"已取消\",\"2\":\"已确认\",\"1\":\"已预约\",\"5\":\"已成功\",\"4\":\"已结束\"}},\"wdpy\":{\"item1\":\"请输入手机号/昵称\",\"title\":\"我的朋友\",\"status\":{\"2\":\"有空\",\"1\":\"没空\"}}}},\"msgname\":\"updateMetadata\"}";
				Gson gson=new GsonBuilder().serializeNulls().create();
		     	Type typeToken = new TypeToken<HttpResult<MenuInfo>>(){}.getType();
		     	HttpResult<MenuInfo> result=gson.fromJson(jsonElement, typeToken);
	     		info.setInfo(result.getData());
				initLeftRightSlidingMenu();
	        }
		});
	}
	private void updateToken() {
 		RequestParams params = new RequestParams();
 		try {
 			JSONObject json=new JSONObject();
 			json.put("accountId", sharedPreferences.getString("id", ""));
 			json.put("token",PushManager.getInstance().getClientid(getApplicationContext()));
 			json.put("request", PublicJsonParse.getPublicParam("updateToken"));
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
 		http.send(HttpMethod.POST, App.service+"updateToken", params,
 		new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				/*Gson gson=new GsonBuilder().serializeNulls().create();
		     	Type typeToken = new TypeToken<HttpResult<MenuInfo>>(){}.getType();
				String jsonElement=responseInfo.result.replace("[]", "null");
		     	HttpResult<MenuInfo> result=gson.fromJson(jsonElement,typeToken);*/
		     	
			}
			@Override
	        public void onFailure(HttpException error, String msg) {
	        	
	        }
		});
	}
	public void removeShared(){
	    Editor editor = sharedPreferences.edit();
	    editor.putString("id","");
	    editor.putString("name","");
	    editor.putString("type","");
	    editor.putString("phone","");
	    editor.putString("email","");
	    editor.putString("address","");
	    editor.putString("country","");
	    editor.putString("province","");
	    editor.putString("city","");
	    editor.putString("qq","");
	    editor.putString("companyName","");
	    editor.putString("companyId","");
	    editor.putString("remark","");
	    editor.putString("photoUrl","");
	    editor.putString("constellation","");
	    editor.putString("birthday","");
	    editor.putString("password","");
	    editor.commit();
	}
	/*protected void onDestroy()
	{
		locationManager.removeUpdates(llistener);
		locationManager.setTestProviderEnabled(provider, false);
		super.onDestroy();
	}*/
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		getSupportFragmentManager().getFragments().get(1).onActivityResult(requestCode, resultCode, data);
    }
	public void addUser(){
		App.user.setAccountId(sharedPreferences.getString("id", ""));
		App.user.setId(sharedPreferences.getString("id", ""));
		App.user.setName(sharedPreferences.getString("name", ""));
		App.user.setType(sharedPreferences.getString("type", ""));
		App.user.setPhone(sharedPreferences.getString("phone", ""));
		App.user.setEmail(sharedPreferences.getString("email", ""));
		App.user.setAddress(sharedPreferences.getString("address", ""));
		App.user.setCountry(sharedPreferences.getString("country", ""));
		App.user.setProvince(sharedPreferences.getString("province", ""));
		App.user.setCity(sharedPreferences.getString("city", ""));
		App.user.setQq(sharedPreferences.getString("qq", ""));
		App.user.setSex(sharedPreferences.getString("sex", ""));
		App.user.setCompanyName(sharedPreferences.getString("companyName", ""));
		App.user.setCompanyId(sharedPreferences.getString("companyId", ""));
		App.user.setRemark(sharedPreferences.getString("remark", ""));
		App.user.setPhotoUrl(sharedPreferences.getString("photoUrl", ""));
		App.user.setConstellation(sharedPreferences.getString("constellation", ""));
		App.user.setBirthday(sharedPreferences.getString("birthday", ""));
		App.user.setPassword(sharedPreferences.getString("password", ""));
	}
	public Bitmap GetImageInputStream(String imageurl){  
        URL url;  
        HttpURLConnection connection=null;  
        Bitmap bitmap=null;  
        try {  
            url = new URL(imageurl);  
            connection=(HttpURLConnection)url.openConnection();  
            connection.setConnectTimeout(6000); //超时设置  
            connection.setDoInput(true);   
            connection.setUseCaches(false); //设置不使用缓存  
            InputStream inputStream=connection.getInputStream();  
            bitmap=BitmapFactory.decodeStream(inputStream);  
            inputStream.close();  
        } catch (Exception e) {  
            e.printStackTrace();
            
        }  
        return bitmap;  
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
 		        	Toast toast = Toast.makeText(getApplicationContext(),"网络异常", Toast.LENGTH_SHORT);
 					toast.setGravity(Gravity.CENTER, 0, 0);
 					toast.show();
 		        }
 		});
 	}
	public void dialog(String clubName,String clubRoom,String remark){
    	builder=new AlertDialog.Builder(this);
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
 		        	Toast toast = Toast.makeText(getApplicationContext(),"网络异常", Toast.LENGTH_SHORT);
 					toast.setGravity(Gravity.CENTER, 0, 0);
 					toast.show();
 		        }
 		});
 	}
}
