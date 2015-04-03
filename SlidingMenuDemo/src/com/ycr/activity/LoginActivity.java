package com.ycr.activity;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.igexin.sdk.PushManager;
import com.indoorclub.girl.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ycr.choosephotos.Bimp;
import com.ycr.pojo.App;
import com.ycr.pojo.HttpResult;
import com.ycr.pojo.UserInfo;
import com.ycr.slidemenu.MainActivity;
import com.ycr.util.PublicJsonParse;
public class LoginActivity extends Activity implements OnClickListener{
	private EditText username=null;
	private EditText password=null;
	private ImageButton   btulogin=null;
	private SharedPreferences sharedPreferences;//共享数据
	private ProgressDialog mProgressDialog  = null; //进度条
	private String name;
	private TextView regiditer;
	private TextView wjmm;
	long lastClick=0;
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	lastClick=System.currentTimeMillis();
    	PushManager.getInstance().initialize(this.getApplicationContext());
        setContentView(R.layout.login);
        username=(EditText) findViewById(R.id.txt_username);
        password=(EditText) findViewById(R.id.txt_password);
        btulogin=(ImageButton) findViewById(R.id.btn_login);
        btulogin.setOnClickListener(this);
        password.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);
        sharedPreferences = this.getSharedPreferences("UserInfoFile", Context.MODE_PRIVATE);
        name=sharedPreferences.getString("phone", "");
        regiditer=(TextView)findViewById(R.id.regiditer);
        regiditer.setOnClickListener(this);
        wjmm=(TextView)findViewById(R.id.wjmm);
        wjmm.setOnClickListener(this);
        if(!name.equals("")&&name!=null){
        	addUser();
        	toMain(false);
        }else{
        	App.user=new UserInfo();
        	Bimp.bmp=new ArrayList<Bitmap>();
        	UpdateManager manager = new UpdateManager(LoginActivity.this);
    		manager.checkUpdate();
        }
        // 隐藏密码为InputType.TYPE_TEXT_VARIATION_PASSWORD，也就是0x81
        // 显示密码为InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD，也就是0x91
    }
	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch (v.getId()) {
		case R.id.btn_login:
			if (System.currentTimeMillis() - lastClick <= 3000)
				return;
			else
			{
				if(username.getText().toString().equals(""))
				{
					username.setError(getResources().getString(R.string.login_hint_username_error));
					username.requestFocus();
					return;
				}
				else if(password.getText().toString().equals(""))
				{
					password.setError(getResources().getString(R.string.login_hint_password_error));
					password.requestFocus();
					return;
				}
				else
				{
					mProgressDialog = new ProgressDialog(LoginActivity.this);//创建ProgressDialog对象
		            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //设置进度条风格，风格为圆形，旋转的 
		            //mProgressDialog.setTitle("读取ing...");// 设置ProgressDialog标题 
		            mProgressDialog.setMessage("正在登录请稍后...");// 设置ProgressDialog提示信息 
		            mProgressDialog.setIndeterminate(true);//设置ProgressDialog 的进度条不明确
		            mProgressDialog.setCancelable(true);// 设置ProgressDialog 可以按退回键取消
		            mProgressDialog.show();// 让ProgressDialog显示
					login();
				}
			}
			lastClick=System.currentTimeMillis();
			break;
		case R.id.wjmm:
			intent=new Intent(LoginActivity.this,ZhmmActivity.class);
			LoginActivity.this.startActivity(intent);
			finish();
			break;
		default:
			intent=new Intent(LoginActivity.this,RegidterActivity.class);
			LoginActivity.this.startActivity(intent);
			finish();
			break;
		}
	}
	private void login() {
		RequestParams params = new RequestParams();
		try {
			JSONObject json=new JSONObject();
			json.put("mobile", username.getText().toString());
			json.put("accountPwd", password.getText().toString());
			json.put("accountType", "2");
			json.put("token", PushManager.getInstance().getClientid(getApplicationContext()));//
			json.put("ip", "");
			json.put("request", PublicJsonParse.getPublicParam("accountLogin"));
			params.addHeader("Content-Type", "application/json");
			params.setBodyEntity(new StringEntity(json.toString()));
		} catch (JSONException e) {
			e.printStackTrace(); 
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//params.addHeader("name", "value");
		//params.addQueryStringParameter("name", "value");
		// 只包含字符串参数时默认使用BodyParamsEntity，
		// 类似于UrlEncodedFormEntity（"application/x-www-form-urlencoded"）。
		//params.addBodyParameter("name", "value");
		// 加入文件参数后默认使用MultipartEntity（"multipart/form-data"），
		// 如需"multipart/related"，xUtils中提供的MultipartEntity支持设置subType为"related"。
		// 使用params.setBodyEntity(httpEntity)可设置更多类型的HttpEntity（如：
		// MultipartEntity,BodyParamsEntity,FileUploadEntity,InputStreamUploadEntity,StringEntity）。
		// 例如发送json参数：params.setBodyEntity(new StringEntity(jsonStr,charset));
		//params.addBodyParameter("file", new File("path"));
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,App.service+"accountLogin",params,
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
		        		App.user=result.getData();
		        		addShared(result.getData());
		        		toMain(true);
		        	}else{
		        		mProgressDialog.cancel();
		        		Toast toast = Toast.makeText(getApplicationContext(),"请输入正确的用户名和密码", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
		        	}
		        }

		        @Override
		        public void onFailure(HttpException error, String msg) {
		        	mProgressDialog.cancel();
		        	Toast toast = Toast.makeText(getApplicationContext(),"网络异常！", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
		        }
		});
	}
	public void addShared(UserInfo user){
	    Editor editor = sharedPreferences.edit();
	    editor.putString("id",user.getId());
	    editor.putString("name",user.getName());
	    editor.putString("type",user.getType());
	    editor.putString("phone",user.getPhone());
	    editor.putString("email",user.getEmail());
	    editor.putString("address",user.getAddress());
	    editor.putString("country",user.getCountry());
	    editor.putString("province",user.getProvince());
	    editor.putString("city",user.getCity());
	    editor.putString("qq",user.getQq());
	    editor.putString("sex",user.getSex());
	    editor.putString("companyName",user.getCompanyName());
	    editor.putString("companyId",user.getCompanyId());
	    editor.putString("remark",user.getRemark());
	    editor.putString("photoUrl",user.getPhotoUrl());
	    editor.putString("constellation",user.getConstellation());
	    editor.putString("birthday",user.getBirthday());
	    editor.putString("password",password.getText().toString());
	    editor.commit();
	}
	public void toMain(Boolean flag){
		int status=sharedPreferences.getInt("status", 0);
		Intent intent=new Intent();
		intent.putExtra("status", status);
		intent.setClass(LoginActivity.this,MainActivity.class);
		LoginActivity.this.startActivity(intent);
		finish();
		if(flag)
		mProgressDialog.cancel();
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
}