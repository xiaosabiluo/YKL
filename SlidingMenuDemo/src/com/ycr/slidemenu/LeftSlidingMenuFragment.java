package com.ycr.slidemenu;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.indoorclub.girl.R;
/**
 * 主要控制左边按钮点击事件
 * @author Administrator
 *
 */
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
import com.ycr.activity.LoginActivity;
import com.ycr.activity.UpdateManager;
import com.ycr.activity.XgmmActivity;
import com.ycr.pojo.App;
import com.ycr.pojo.HttpResult;
import com.ycr.pojo.HttpResultList;
import com.ycr.pojo.MesCountInfo;
import com.ycr.pojo.MetadataInfo;
import com.ycr.pojo.UserInfo;
import com.ycr.util.BadgeView;
import com.ycr.util.BitmapHelp;
import com.ycr.util.MyFragment;
import com.ycr.util.PublicJsonParse;
import com.ycr.util.ReadImgToBinary;
/**
 * 主要控制左边按钮点击事件
 * @author Administrator
 *
 */
public class LeftSlidingMenuFragment extends Fragment implements OnClickListener{
	private View wdzlBtnLayout;
	private View wdjmBtnLayout;
	private View wdycBtnLayout;
	private View tjycBtnLayout;
	private View gzszBtnLayout;
	private View wdddBtnLayout;
	private View xxBtnLayout;
	private TextView wdzlText;
	private TextView wdjmText;
	private TextView wdycText;
	private TextView tjycText;
	private TextView gzszText;
	private TextView wdddText;
	private TextView xxText;
	private BadgeView wdzlBadge;
	private BadgeView wdjmBadge;
	private BadgeView wdycBadge;
	private BadgeView tjycBadge;
	private BadgeView gzszBadge;
	private BadgeView wdddBadge;
	private BadgeView xxBadge;
	private TextView xgmm;
	private TextView exit;
	public static RoundedImageView roundedImageView;
	private SharedPreferences sharedPreferences;
	private BitmapUtils bitmapUtils;
    private BitmapDisplayConfig bigPicDisplayConfig;
    private MainActivity ra =null;
    private MetadataInfo info;
    private String strDate;
    public int status;
    private Drawable drawable;
    public LeftSlidingMenuFragment(){}
    public LeftSlidingMenuFragment(int status){
    	this.status=status;
    }
     @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    }
     
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	  View view = inflater.inflate(R.layout.main_left_fragment, container,false);
    	  info=MetadataInfo.getInstance();
    	  drawable=getResources().getDrawable(R.drawable.photo);
    	  wdzlBtnLayout = view.findViewById(R.id.wdzlBtnLayout);
    	  wdzlBtnLayout.setOnClickListener(this);
    	  wdzlText=(TextView) wdzlBtnLayout.findViewById(R.id.toolbox_title);
    	  wdzlText.setText(info.getInfo().getMenus().get("wdzl").get("menuValue").toString());
    	  ImageView wdzlImage=(ImageView) wdzlBtnLayout.findViewById(R.id.notification_indicator);
    	  wdzlBadge=new BadgeView(getActivity(), wdzlImage);
    	  wdzlBadge.setBadgeMargin(0,0);
    	  wdzlBadge.setTextSize(5);
    	  wdjmBtnLayout = view.findViewById(R.id.wdjmBtnLayout);
    	  wdjmBtnLayout.setOnClickListener(this);
    	  wdjmText=(TextView) wdjmBtnLayout.findViewById(R.id.toolbox_title);
    	  wdjmText.setText(info.getInfo().getMenus().get("wdpy").get("menuValue").toString());
    	  ImageView wdjmImage=(ImageView) wdjmBtnLayout.findViewById(R.id.notification_indicator);
    	  wdjmBadge=new BadgeView(getActivity(), wdjmImage);
    	  wdjmBadge.setBadgeMargin(0,0);
    	  wdjmBadge.setTextSize(5);
    	  wdycBtnLayout = view.findViewById(R.id.wdycBtnLayout);
    	  wdycBtnLayout.setOnClickListener(this);
    	  wdycText=(TextView) wdycBtnLayout.findViewById(R.id.toolbox_title);
    	  wdycText.setText(info.getInfo().getMenus().get("wdyc").get("menuValue").toString());
    	  ImageView wdycImage=(ImageView) wdycBtnLayout.findViewById(R.id.notification_indicator);
    	  wdycBadge=new BadgeView(getActivity(), wdycImage);
    	  wdycBadge.setBadgeMargin(0,0);
    	  wdycBadge.setTextSize(5);
    	  tjycBtnLayout = view.findViewById(R.id.tjycBtnLayout);
    	  tjycBtnLayout.setOnClickListener(this);
    	  tjycText=(TextView) tjycBtnLayout.findViewById(R.id.toolbox_title);
    	  tjycText.setText(info.getInfo().getMenus().get("tjyc").get("menuValue").toString());
    	  ImageView tjycImage=(ImageView) tjycBtnLayout.findViewById(R.id.notification_indicator);
    	  tjycBadge=new BadgeView(getActivity(), tjycImage);
    	  tjycBadge.setBadgeMargin(0,0);
    	  tjycBadge.setTextSize(5);
    	  gzszBtnLayout = view.findViewById(R.id.gzszBtnLayout);
    	  gzszBtnLayout.setOnClickListener(this);
    	  gzszText=(TextView) gzszBtnLayout.findViewById(R.id.toolbox_title);
    	  gzszText.setText(info.getInfo().getMenus().get("gzsz").get("menuValue").toString());
    	  ImageView gzszImage=(ImageView) gzszBtnLayout.findViewById(R.id.notification_indicator);
    	  gzszBadge=new BadgeView(getActivity(), gzszImage);
    	  gzszBadge.setBadgeMargin(0,0);
    	  gzszBadge.setTextSize(5);
    	  wdddBtnLayout = view.findViewById(R.id.wdddBtnLayout);
    	  wdddBtnLayout.setOnClickListener(this);
    	  wdddText=(TextView) wdddBtnLayout.findViewById(R.id.toolbox_title);
    	  wdddText.setText(info.getInfo().getMenus().get("wddd").get("menuValue").toString());
    	  ImageView wdddImage=(ImageView) wdddBtnLayout.findViewById(R.id.notification_indicator);
    	  wdddBadge=new BadgeView(getActivity(), wdddImage);
    	  wdddBadge.setBadgeMargin(0,0);
    	  wdddBadge.setTextSize(5);
    	  xxBtnLayout = view.findViewById(R.id.xxBtnLayout);
    	  xxBtnLayout.setOnClickListener(this);
    	  xxText=(TextView) xxBtnLayout.findViewById(R.id.toolbox_title);
    	  xxText.setText(info.getInfo().getMenus().get("xx").get("menuValue").toString());
    	  ImageView xxImage=(ImageView) xxBtnLayout.findViewById(R.id.notification_indicator);
    	  xxBadge=new BadgeView(getActivity(), xxImage);
    	  xxBadge.setBadgeMargin(0,0);
    	  xxBadge.setTextSize(5);
    	  roundedImageView = (RoundedImageView)view.findViewById(R.id.headImageView);
    	  roundedImageView.setOnClickListener(this);
    	  xgmm = (TextView)view.findViewById(R.id.xgmm);
    	  exit = (TextView)view.findViewById(R.id.exit);
    	  xgmm.setOnClickListener(this);
    	  exit.setOnClickListener(this);
    	  sharedPreferences=getActivity().getSharedPreferences("UserInfoFile", Context.MODE_PRIVATE);//共享数据
    	  switchTo(status);
    	  menuNotice();
    	  if(App.user.getPhotoUrl()!=null){
	    	  if(App.user.getPhotoUrl().indexOf("http")==-1){
					Bitmap bitmap=ReadImgToBinary.base64ToBitmap(App.user.getPhotoUrl(), "usertemp", null);
			    	roundedImageView.setImageBitmap(bitmap);
				}
				else
				{
					photo(App.user.getPhotoUrl());
				}
    	  }else{
    		  roundedImageView.setImageDrawable(getResources().getDrawable(R.drawable.photo));
    	  }
    	  return view;
    }

	@Override
	public void onClick(View v) {
		switchTo(v.getId());
	}
	public void switchTo(int status){
		MyFragment newContent = null;
		Bundle bundle =new Bundle(); 
		bundle.putInt("status",status);  
		Intent intent=null;
		Editor editor=null;
		ra= (MainActivity) getActivity();
		switch (status) {
		case R.id.wdzlBtnLayout:
			newContent = new FragmentWdzl();
			wdzlBtnLayout.setSelected(true);
			wdjmBtnLayout.setSelected(false);
			wdycBtnLayout.setSelected(false);
			tjycBtnLayout.setSelected(false);
			gzszBtnLayout.setSelected(false);
			wdddBtnLayout.setSelected(false);
			xxBtnLayout.setSelected(false);
			editor = sharedPreferences.edit();
		    editor.putInt("status",status);
		    editor.commit();
			break;
		case R.id.wdjmBtnLayout:
			newContent = new FragmentWdpy(ra.getIvTitleBtnRight());
			wdzlBtnLayout.setSelected(false);
			wdjmBtnLayout.setSelected(true);
			wdycBtnLayout.setSelected(false);
			tjycBtnLayout.setSelected(false);
			gzszBtnLayout.setSelected(false);
			wdddBtnLayout.setSelected(false);
			xxBtnLayout.setSelected(false);
			editor = sharedPreferences.edit();
		    editor.putInt("status",status);
		    editor.commit();
			break;
		case R.id.wdycBtnLayout:
			newContent = new FragmentWdyc(ra.getIvTitleBtnRight());
			wdzlBtnLayout.setSelected(false);
			wdjmBtnLayout.setSelected(false);
			wdycBtnLayout.setSelected(true);
			tjycBtnLayout.setSelected(false);
			gzszBtnLayout.setSelected(false);
			wdddBtnLayout.setSelected(false);
			xxBtnLayout.setSelected(false);
			editor = sharedPreferences.edit();
		    editor.putInt("status",status);
		    editor.commit();
			if(wdycBadge.isShown())
				wdycBadge.toggle();
		    break;
		case R.id.tjycBtnLayout:
			newContent = new FragmentTjyc(ra.getIvTitleBtnRight());
			wdzlBtnLayout.setSelected(false);
			wdjmBtnLayout.setSelected(false);
			wdycBtnLayout.setSelected(false);
			tjycBtnLayout.setSelected(true);
			gzszBtnLayout.setSelected(false);
			wdddBtnLayout.setSelected(false);
			xxBtnLayout.setSelected(false);
			editor = sharedPreferences.edit();
		    editor.putInt("status",status);
		    editor.commit();
		    break;
		case R.id.gzszBtnLayout:
			newContent = new FragmentGzsz();
			wdzlBtnLayout.setSelected(false);
			wdjmBtnLayout.setSelected(false);
			wdycBtnLayout.setSelected(false);
			tjycBtnLayout.setSelected(false);
			gzszBtnLayout.setSelected(true);
			wdddBtnLayout.setSelected(false);
			xxBtnLayout.setSelected(false);
			editor = sharedPreferences.edit();
		    editor.putInt("status",status);
		    editor.commit();
		    break;
		case R.id.wdddBtnLayout:
			newContent = new FragmentWddd();
			wdzlBtnLayout.setSelected(false);
			wdjmBtnLayout.setSelected(false);
			wdycBtnLayout.setSelected(false);
			tjycBtnLayout.setSelected(false);
			gzszBtnLayout.setSelected(false);
			wdddBtnLayout.setSelected(true);
			xxBtnLayout.setSelected(false);
			if(wdddBadge.isShown())
			wdddBadge.toggle();
			editor = sharedPreferences.edit();
		    editor.putInt("status",status);
		    editor.commit();
		    break;
		case R.id.xxBtnLayout:
			newContent = new FragmentXx();
			wdzlBtnLayout.setSelected(false);
			wdjmBtnLayout.setSelected(false);
			wdycBtnLayout.setSelected(false);
			tjycBtnLayout.setSelected(false);
			gzszBtnLayout.setSelected(false);
			wdddBtnLayout.setSelected(false);
			xxBtnLayout.setSelected(true);
			if(xxBadge.isShown())
			xxBadge.toggle();
			editor = sharedPreferences.edit();
		    editor.putInt("status",status);
		    editor.commit();
		    break;
		case R.id.headImageView:
			//Toast.makeText(getActivity(), "点击头像", Toast.LENGTH_SHORT).show();
			//intent = new Intent(getActivity(),AboutActivity.class);
			//startActivity(intent);
			break;
		case R.id.xgmm:
			intent = new Intent(getActivity(),XgmmActivity.class);
			startActivity(intent);
			break;
		case R.id.exit:
			dialog();
			break;
		default:
			newContent = new FragmentGzsz();
			wdzlBtnLayout.setSelected(false);
			wdjmBtnLayout.setSelected(false);
			wdycBtnLayout.setSelected(false);
			tjycBtnLayout.setSelected(false);
			gzszBtnLayout.setSelected(true);
			wdddBtnLayout.setSelected(false);
			xxBtnLayout.setSelected(false);
			editor = sharedPreferences.edit();
		    editor.putInt("status",status);
		    editor.commit();
			break;
		}
		if (newContent != null){
			newContent.setArguments(bundle);
			switchFragment(newContent);
		}
	}
	
	private void switchFragment(MyFragment fragment) {
		if (getActivity() == null)
			return;
			ra.switchContent(fragment);
		
	}
	private void loginOut() {
		RequestParams params = new RequestParams();
		try {
			JSONObject json=new JSONObject();
			json.put("accountId", sharedPreferences.getString("id", ""));
			json.put("request", PublicJsonParse.getPublicParam("loginOut"));
			params.addHeader("Content-Type", "application/json");
			params.setBodyEntity(new StringEntity(json.toString()));
			removeShared();
		} catch (JSONException e) {
			e.printStackTrace(); 
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,App.service+"loginOut",params,
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
		        	Type typeToken = new TypeToken<HttpResult<UserInfo>>(){}.getType();
		        	HttpResult<UserInfo> result=gson.fromJson(responseInfo.result,typeToken);
		        	if(result.isResult()){
		        		
		        	}else{
		        		Toast toast = Toast.makeText(getActivity().getApplicationContext(),result.getError(), Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
		        	}
		        }

		        @Override
		        public void onFailure(HttpException error, String msg) {
		        	
		        }
		});
	}
	/**
	 * 弹出退出确认对话框
	 */
	private void dialog() {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setMessage("确定要退出吗?");
		builder.setTitle("提示");
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						loginOut();
						UpdateManager.cancelUpdate=false;
						Intent intent=new Intent(getActivity(),LoginActivity.class);
						getActivity().startActivity(intent);
						getActivity().finish();
					}
				});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
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
	    editor.putInt("status",0);
	    editor.commit();
	}
	public View getTjycBtnLayout() {
		return tjycBtnLayout;
	}

	public void setTjycBtnLayout(View tjycBtnLayout) {
		this.tjycBtnLayout = tjycBtnLayout;
	}
	private void photo(String imgUrl){
		if (bitmapUtils == null) {
	        bitmapUtils = BitmapHelp.getBitmapUtils(getActivity().getApplicationContext());
	    }

	    bigPicDisplayConfig = new BitmapDisplayConfig();
	    //bigPicDisplayConfig.setShowOriginal(true); // 显示原始图片,不压缩, 尽量不要使用, 图片太大时容易OOM。
	    bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
	    bigPicDisplayConfig.setBitmapMaxSize(BitmapCommonUtils.getScreenSize(getActivity()));
	    bigPicDisplayConfig.setLoadFailedDrawable(drawable);
	    BitmapLoadCallBack<ImageView> callback = new DefaultBitmapLoadCallBack<ImageView>() {
	        @Override
	        public void onLoadStarted(ImageView container, String uri, BitmapDisplayConfig config) {
	            super.onLoadStarted(container, uri, config);
	            //Toast.makeText(getActivity().getApplicationContext(), uri, 300).show();
	        }

	        @Override
	        public void onLoadCompleted(ImageView container, String uri, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
	            super.onLoadCompleted(container, uri, bitmap, config, from);
	        }
	    };

	    bitmapUtils.display(roundedImageView, imgUrl, bigPicDisplayConfig, callback);
	}
	private void menuNotice() {
		RequestParams params = new RequestParams();
		try {
			Gson gson=new Gson();
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");  
	        Date date=new Date();
	        strDate=sharedPreferences.getString("menuDate", format.format(date));//format.format(date);  
			JSONObject json=new JSONObject();
			List<HashMap<String, String>> types=new ArrayList<HashMap<String,String>>();
			HashMap<String, String> type1=new HashMap<String, String>();
			type1.put("type", "1");
			type1.put("updatetime", strDate);
			types.add(type1);
			HashMap<String, String> type2=new HashMap<String, String>();
			type2.put("type", "2");
			type2.put("updatetime", strDate);
			types.add(type2);
			HashMap<String, String> type5=new HashMap<String, String>();
			type5.put("type", "5");
			type5.put("updatetime", strDate);
			types.add(type5);
			json.put("accountId", sharedPreferences.getString("id", ""));
			json.put("types", new JSONArray(gson.toJson(types)));
			json.put("request", PublicJsonParse.getPublicParam("menuNotice"));
			params.addHeader("Content-Type", "application/json");
			params.setBodyEntity(new StringEntity(json.toString()));
		} catch (JSONException e) {
			e.printStackTrace(); 
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,App.service+"menuNotice",params,
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
		        	Type typeToken = new TypeToken<HttpResultList<MesCountInfo>>(){}.getType();
		        	HttpResultList<MesCountInfo> result=gson.fromJson(responseInfo.result,typeToken);
		        	if(result!=null){
		        	if(result.isResult()){
		        		for (MesCountInfo info : (List<MesCountInfo>)result.getData()) {
		        			if(info.getType()==1)
		        				wdddBadge.show();
		        			else if(info.getType()==2)
		        				xxBadge.show();
		        			else
		        				wdycBadge.show();
						}
		        	}
		        	SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");  
			        Date date=new Date();
		        	Editor editor = sharedPreferences.edit();
	    		    editor.putString("menuDate",format.format(date));
	    		    editor.commit();
		        }}

		        @Override
		        public void onFailure(HttpException error, String msg) {
		        	Toast toast = Toast.makeText(getActivity().getApplicationContext(),"网络异常", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
		        }
		});
	}
}
