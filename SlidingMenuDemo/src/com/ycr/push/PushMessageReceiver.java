package com.ycr.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.igexin.sdk.PushConsts;
import com.indoorclub.girl.R;
import com.ycr.activity.LoginActivity;
import com.ycr.pojo.ApplicationDate;
import com.ycr.pojo.PushInfo;
import com.ycr.slidemenu.MainActivity;

public class PushMessageReceiver extends BroadcastReceiver {

	private SharedPreferences sharedPreferences;
	private Context context;
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context=context;
		sharedPreferences=context.getSharedPreferences("UserInfoFile", Context.MODE_PRIVATE);//共享数据
		Bundle bundle = intent.getExtras();
		Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {

		case PushConsts.GET_MSG_DATA:
			// 获取透传数据
			// String appid = bundle.getString("appid");
			int action=bundle.getInt("action");
			byte[] payload = bundle.getByteArray("payload");
			//String taskid = bundle.getString("taskid");
			//String messageid = bundle.getString("messageid");

			// smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
			//boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
			if (payload != null) {
				String data = new String(payload);
				Gson gson=new Gson();
				PushInfo push=gson.fromJson(data, PushInfo.class);
				showNotification(action,push);
			}
			break;
		case PushConsts.GET_CLIENTID:
			// 获取ClientID(CID)
			// 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
			String clientid = bundle.getString("clientid");
			Editor editor = sharedPreferences.edit();
		    editor.putString("clientid",clientid);
		    editor.commit();
			break;
		case PushConsts.THIRDPART_FEEDBACK:
			/*String appid = bundle.getString("appid");
			String taskid = bundle.getString("taskid");
			String actionid = bundle.getString("actionid");
			String result = bundle.getString("result");
			long timestamp = bundle.getLong("timestamp");

			Log.d("GetuiSdkDemo", "appid = " + appid);
			Log.d("GetuiSdkDemo", "taskid = " + taskid);
			Log.d("GetuiSdkDemo", "actionid = " + actionid);
			Log.d("GetuiSdkDemo", "result = " + result);
			Log.d("GetuiSdkDemo", "timestamp = " + timestamp);*/
			break;
		default:
			break;
		}
	}
	/**
     * 在状态栏显示通知
     */
    private void showNotification(int action,PushInfo push){
    	if(push.getType().equals("0")){
    		removeShared();
    		Intent notificationIntent =new Intent(this.context, LoginActivity.class);
    		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_TASK_ON_HOME);
    		this.context.startActivity(notificationIntent);
        }else
        {
	        // 创建一个NotificationManager的引用   
	        NotificationManager notificationManager = (NotificationManager)this.context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);   
	        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this.context); 
	        mBuilder.setContentTitle("新消息");
	        // 设置通知的事件消息   
	        Intent notificationIntent =new Intent(this.context, MainActivity.class); // 点击该通知后要跳转的Activity  
	        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        notificationIntent.putExtra("status",sharedPreferences.getInt("status", R.id.wdzlBtnLayout));
        	notificationIntent.putExtra("type",push.getType());
        	notificationIntent.putExtra("orderId",push.getParam());
        	notificationIntent.putExtra("content",push.getContent());
        	ApplicationDate.getInstance().setOrderId(push.getParam());
        	Editor editor = sharedPreferences.edit();
		    editor.putString("orderId",push.getParam());
		    editor.commit();
	        /*if(push.getType().equals("1")){
	        	if(push.getContent().equals("10")){
	        		mBuilder.setContentTitle("新消息");
		        	notificationIntent.putExtra("status",R.id.wdddBtnLayout);
		        	notificationIntent.putExtra("type",1);
	        	}else{
		        	mBuilder.setContentTitle("预约消息");
		        	notificationIntent.putExtra("status",R.id.wdddBtnLayout);
		        	notificationIntent.putExtra("type",1);
	        	}
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
				Editor editor = sharedPreferences.edit();
			    editor.putString("orderId",push.getParam());
			    editor.commit();
	        }*/
	        mBuilder.setContentText(push.getContent());
	        //mBuilder.setTicker(""); //通知首次出现在通知栏，带上升动画效果的  
	        mBuilder.setWhen(System.currentTimeMillis());//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间  
	        //mBuilder.setPriority(Notification.PRIORITY_DEFAULT); //设置该通知优先级  
	        mBuilder.setAutoCancel(true);//设置这个标志当用户单击面板就可以让通知将自动取消    
	        mBuilder.setOngoing(false);//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)  
	        mBuilder.setDefaults(Notification.DEFAULT_ALL);//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合  
	        //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission  
	        mBuilder.setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON  
	        
	        PendingIntent contentItent = PendingIntent.getActivity(this.context, 0, notificationIntent, 0);   
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
	        notificationManager.notify(action, notification);  
        }
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
}
