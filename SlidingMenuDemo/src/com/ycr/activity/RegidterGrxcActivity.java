package com.ycr.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
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
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ycr.choosephotos.Bimp;
import com.ycr.choosephotos.ChoosePhotosActivity;
import com.ycr.pojo.App;
import com.ycr.pojo.HttpResultList;
import com.ycr.pojo.MediaInfo;
import com.ycr.pojo.UserInfo;
import com.ycr.util.PublicJsonParse;
import com.ycr.util.ReadImgToBinary;

public class RegidterGrxcActivity extends Activity{
	private MediaInfo media=new MediaInfo();
	private GridView mGridView;
	private BaseAdapter adapterM;
	private List<MediaInfo> dataListM=new ArrayList<MediaInfo>();
	private List<MediaInfo> selectList=new ArrayList<MediaInfo>();
	public static boolean flag=false;
	private TextView edit;
	private TextView del;
	private TextView isok;
	private TextView title;
	private ImageButton add;
	private ImageButton back;
	ProgressDialog mProgressDialog;
    public static boolean isUpload=false;
    long lastClick=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grxc_tab);
		lastClick=System.currentTimeMillis();
		media.setMediaUrl("add");
		dataListM.add(media);
    	mGridView=(GridView) findViewById(R.id.gridview);
    	edit=(TextView)findViewById(R.id.edit);
    	edit.setOnClickListener(textViewOnClicknew);
    	title=(TextView)findViewById(R.id.title);
    	title.setText("个人相册");
    	del=(TextView)findViewById(R.id.del);
    	del.setOnClickListener(textViewOnClicknew);
    	isok=(TextView)findViewById(R.id.isok);
    	isok.setOnClickListener(textViewOnClicknew);
    	back=(ImageButton)findViewById(R.id.back);
    	back.setOnClickListener(textViewOnClicknew);
    	add=(ImageButton)findViewById(R.id.add);
    	add.setOnClickListener(textViewOnClicknew);
    	adapterM = new BaseAdapter() {
			public View getView(int position, View convertView, ViewGroup parent) {
				final Holder holder;
				if (convertView == null) {
					holder = new Holder();
					convertView = View.inflate(RegidterGrxcActivity.this, R.layout.item_image_grid, null);
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
					holder.iv.setImageBitmap(item.getBitmap());
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
									Intent intent = new Intent(RegidterGrxcActivity.this,ChoosePhotosActivity.class);
									intent.putExtra("isRegidter", true);
									intent.putExtra("isImages", true);
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
									intent.setClass(RegidterGrxcActivity.this, GrxcImageViewActivity.class);
									RegidterGrxcActivity.this.startActivity(intent);
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
    	loadImages();
    }
    private void loadImages(){
    	List<String> medias=new ArrayList<String>();
    	for (Bitmap bitmap : Bimp.bmp) {
    		MediaInfo info=new MediaInfo();
    		info.setBitmap(bitmap);
    		info.setMediaUrl("");
    		dataListM.add(dataListM.size()-1,info);
    		String img=ReadImgToBinary.imgToBase64(null, bitmap, null);
        	medias.add(img);
		}
    	App.user.setMedias(medias);
    	adapterM.notifyDataSetChanged();
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
			case R.id.add:
				if (System.currentTimeMillis() - lastClick <= 3000)
					return;
				else
				{
					if(App.user.getMedias()!=null&&App.user.getMedias().size()>=3){
						mProgressDialog = new ProgressDialog(RegidterGrxcActivity.this);//创建ProgressDialog对象
			            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //设置进度条风格，风格为圆形，旋转的 
			            //mProgressDialog.setTitle("读取ing...");// 设置ProgressDialog标题 
			            mProgressDialog.setMessage("正在注册...");// 设置ProgressDialog提示信息 
			            mProgressDialog.setIndeterminate(true);//设置ProgressDialog 的进度条不明确
			            mProgressDialog.show();// 让ProgressDialog显示
			            mProgressDialog.setCancelable(false);
						userRegister();
					}else{
						Toast toast = Toast.makeText(getApplicationContext(),"至少上传3张图片", Toast.LENGTH_SHORT);
	 					toast.setGravity(Gravity.CENTER, 0, 0);
	 					toast.show();
					}
				}
				lastClick = System.currentTimeMillis();
				break;
			case R.id.isok:
				del.setVisibility(View.GONE);
				isok.setVisibility(View.GONE);
				edit.setVisibility(View.VISIBLE);
				flag=false;
				dataListM.add(media);
				break;
			case R.id.back:
				Intent intent=new Intent(RegidterGrxcActivity.this,RegidterGrzlActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				RegidterGrxcActivity.this.startActivity(intent);
				finish();
			case R.id.del:
				for (MediaInfo mediaInfo: selectList) {
					dataListM.remove(mediaInfo);
					Bimp.bmp.remove(mediaInfo.getBitmap());
				}
				selectList=new ArrayList<MediaInfo>();
			default:
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
				Toast.makeText(RegidterGrxcActivity.this.getApplicationContext(), "请选择要删除的图片", 400).show();
				break;
			default:
				break;
			}
		}
	};
	class Holder {
		private ImageView iv;
		private ImageView selected;
		private TextView text;
	}
	private void userRegister() {
 		RequestParams params = new RequestParams();
 		try {
 			JSONObject json=new JSONObject();
 			json.put("mobile", App.user.getPhone());
 			json.put("password", App.user.getPassword());
 			json.put("photo", App.user.getPhotoUrl());
 			json.put("nickName", URLEncoder.encode(App.user.getName()==null?"":App.user.getName(),"utf-8"));
 			json.put("sex", App.user.getSex());
 			json.put("birthday", App.user.getBirthday());
 			json.put("country", URLEncoder.encode(App.user.getCountry()==null?"":App.user.getCountry(),"utf-8"));
 			json.put("province", URLEncoder.encode(App.user.getProvince()==null?"":App.user.getProvince(),"utf-8"));
 			json.put("city", URLEncoder.encode(App.user.getCity()==null?"":App.user.getCity(),"utf-8"));
 			json.put("region", URLEncoder.encode(App.user.getCity()==null?"":App.user.getCity(),"utf-8"));
 			json.put("remark", URLEncoder.encode(App.user.getRemark()==null?"":App.user.getRemark(),"utf-8"));
 			json.put("longitude", 0.0);
 			json.put("latitude", 0.0);
 			json.put("inviteCode", App.user.getCode());
 			json.put("idCard", App.user.getIdcard());
 			json.put("medias", new JSONArray(new Gson().toJson(App.user.getMedias())));
 			json.put("request", PublicJsonParse.getPublicParam("userRegister"));
 			params.addHeader("Content-Type", "application/json");
 			params.addHeader("charset", HTTP.UTF_8);
 			params.setBodyEntity(new StringEntity(json.toString()));
 		} catch (JSONException e) {
 			e.printStackTrace(); 
 		}
 		catch (UnsupportedEncodingException e) {
 			e.printStackTrace();
 		}
 		HttpUtils http = new HttpUtils();
 		http.configTimeout(30000);
 		http.send(HttpRequest.HttpMethod.POST,App.service+"userRegister",params,
 		    new RequestCallBack<String>(){
 		        @Override
 		        public void onSuccess(ResponseInfo<String> responseInfo) {
 		        	Gson gson=new GsonBuilder().serializeNulls().create();
 		        	Type typeToken = new TypeToken<HttpResultList<UserInfo>>(){}.getType();
 		        	String jsonElement=responseInfo.result.replace("[]", "null");
 		        	HttpResultList<UserInfo> result=gson.fromJson(jsonElement,typeToken);
 		        	if(result.isResult()){
 		        		new AlertDialog.Builder(RegidterGrxcActivity.this)
 		               .setMessage("注册成功")
 		               .setPositiveButton("确认",
 		                       new DialogInterface.OnClickListener() {
 		                           @Override
 		                           public void onClick(DialogInterface dialog, int which) {
 		                        	    Intent intent = new Intent(RegidterGrxcActivity.this,LoginActivity.class);
 		                        	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_TASK_ON_HOME);
	 		      		        		RegidterGrxcActivity.this.startActivity(intent);
	 		      		        		App.user=new UserInfo();
	 		      		        		mProgressDialog.cancel();
	 		      		        		Bimp.bmp=new ArrayList<Bitmap>();
	 		      		        		finish();
 		                           }
 		               })
 		               .setCancelable(false)
 		               .show();
 		        	}else{
 		        		mProgressDialog.cancel();
 		        		Toast toast = Toast.makeText(getApplicationContext(),result.getError(), Toast.LENGTH_SHORT);
 	 					toast.setGravity(Gravity.CENTER, 0, 0);
 	 					toast.show();
 		        	}
 		        }

 		        @Override
 		        public void onFailure(HttpException error, String msg) {
 		        	mProgressDialog.cancel();
 		        	Toast toast = Toast.makeText(getApplicationContext(),"网络异常", Toast.LENGTH_SHORT);
 					toast.setGravity(Gravity.CENTER, 0, 0);
 					toast.show();
 		        }
 		});
 	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 依据resultCode进行接收,这也是上个界面中需要设置resultCode的原因了
		if(resultCode==0)
			return;
		String sdStatus = Environment.getExternalStorageState();  
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用  
            return;  
        }  
        @SuppressWarnings("static-access")
		String name = new android.text.format.DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";     
        Bundle bundle = data.getExtras();  
        Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
        FileOutputStream b = null;  
        File file = new File("/sdcard/yunkelai/");  
        file.mkdirs();// 创建文件夹  
        String fileName = "/sdcard/yunkelai/"+name;  
  
            try {  
                b = new FileOutputStream(fileName);  
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                b.flush();  
                b.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        //App.user.setPhotoUrl(ReadImgToBinary.imgToBase64(null, bitmap, null));
		super.onActivityResult(requestCode, resultCode, data);
	}
}
