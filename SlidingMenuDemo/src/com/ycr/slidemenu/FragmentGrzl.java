package com.ycr.slidemenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gghl.view.wheelcity.AddressData;
import com.gghl.view.wheelcity.OnWheelChangedListener;
import com.gghl.view.wheelcity.WheelView;
import com.gghl.view.wheelcity.adapters.AbstractWheelTextAdapter;
import com.gghl.view.wheelcity.adapters.ArrayWheelAdapter;
import com.gghl.view.wheelview.JudgeDate;
import com.gghl.view.wheelview.ScreenInfo;
import com.gghl.view.wheelview.WheelMain;
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
import com.ycr.activity.JrycActivity;
import com.ycr.choosephotos.Bimp;
import com.ycr.choosephotos.ChoosePhotosActivity;
import com.ycr.pojo.App;
import com.ycr.pojo.HttpResult;
import com.ycr.pojo.UserInfo;
import com.ycr.util.BitmapHelp;
import com.ycr.util.PublicJsonParse;
import com.ycr.util.ReadImgToBinary;
import com.ycr.util.Start;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;
import com.zf.iosdialog.widget.MyAlertDialog;

public class FragmentGrzl extends Fragment implements OnClickListener{
	private UserInfo user=new UserInfo();
	private ImageView tximage;
	private View txinfo;
	private TextView name;
	private View ncinfo;
	private TextView sex;
	private TextView birthday;
	private View srinfo;
	private TextView gx;
	private View gxinfo;
	private TextView xz;
	private TextView grsm;
	private View grsminfo;
	private View sexinfo;
	private View headerlayout;
	private SharedPreferences sharedPreferences;
	private BitmapUtils bitmapUtils;
    private BitmapDisplayConfig bigPicDisplayConfig;
    private WheelView country;
    private WheelView city;
    private WheelView ccity;
    private Drawable drawable;
    public FragmentGrzl(){}
    private Uri uri;
    private boolean isPhotoZoom=false;
    private File file;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.grzl_tab, container, false);
    	file = new File(getActivity().getExternalFilesDir(null), "image.jpg");
    	uri = Uri.fromFile(file);
    	sharedPreferences=getActivity().getSharedPreferences("UserInfoFile", Context.MODE_PRIVATE);//共享数据
    	drawable=getResources().getDrawable(R.drawable.photo);
    	tximage=(ImageView)view.findViewById(R.id.tximage);
    	txinfo=(View)view.findViewById(R.id.txinfo);
    	name=(TextView)view.findViewById(R.id.name);
    	ncinfo=(View)view.findViewById(R.id.ncinfo);
    	sex=(TextView)view.findViewById(R.id.sex);
    	birthday=(TextView)view.findViewById(R.id.birthday);
    	srinfo=(View)view.findViewById(R.id.srinfo);
    	gx=(TextView)view.findViewById(R.id.gx);
    	headerlayout=view.findViewById(R.id.activityHeader);
    	headerlayout.setVisibility(View.GONE);
    	gxinfo=(View)view.findViewById(R.id.gxinfo);
    	sexinfo=(View)view.findViewById(R.id.sexinfo);
    	sexinfo.setOnClickListener(this);
    	xz=(TextView)view.findViewById(R.id.xz);
    	grsm=(TextView)view.findViewById(R.id.grsm);
    	grsminfo=(View)view.findViewById(R.id.grsminfo);
    	txinfo.setOnClickListener(this);
    	ncinfo.setOnClickListener(this);
    	srinfo.setOnClickListener(this);
    	gxinfo.setOnClickListener(this);
    	grsminfo.setOnClickListener(this);
    	user=App.user;
    	Intent intent=getActivity().getIntent();
    	isPhotoZoom=intent.getBooleanExtra("isPhotoZoom", false);
    	initData();
    	return view;
    }
	private void initData(){
		if(isPhotoZoom){
			Bitmap bitmap=Bimp.bmp.get(0);
			uri=Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, null,null));
            startPhotoZoom(uri);
			/*tximage.setImageBitmap(bitmap);
			App.user.setPhotoUrl(ReadImgToBinary.imgToBase64(null, bitmap, null));
			LeftSlidingMenuFragment.roundedImageView.setImageBitmap(bitmap);
			Bimp.bmp=new ArrayList<Bitmap>();
			updatePerson();*/
		}else
		{
			if(user.getPhotoUrl()==null){
				tximage.setImageDrawable(getResources().getDrawable(R.drawable.photo));
			}
			else
			{
				if(user.getPhotoUrl().indexOf("http")==-1){
					Bitmap bitmap=ReadImgToBinary.base64ToBitmap(App.user.getPhotoUrl(), "usertemp", null);
					tximage.setImageBitmap(bitmap);
				}
				else
				{
					photo(tximage, user.getPhotoUrl());
				}
			}
		}
		name.setText(user.getName());
		String sex1="女";
		if(!user.getSex().equals(""))
			sex1=user.getSex().equals("0")?"女":"男";
		sex.setText(sex1);
		birthday.setText(user.getBirthday());
		gx.setText(user.getProvince()+" "+user.getCity());
		xz.setText(user.getConstellation());
		grsm.setText(user.getRemark());
		if(!user.getBirthday().equals("")&&user.getBirthday()!=null){
			String m=user.getBirthday().substring(user.getBirthday().indexOf("-")+1,user.getBirthday().lastIndexOf("-") );
			String d=user.getBirthday().substring(user.getBirthday().lastIndexOf("-")+1, user.getBirthday().length());
			String xzs=Start.getConstellation(Integer.parseInt(m), Integer.parseInt(d));
			xz.setText(xzs);
		}
		addShared(user);
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
	            App.user.setPhotoUrl(ReadImgToBinary.imgToBase64(null, bitmap, null));
	        }
	        @Override
	        public void onLoadFailed(ImageView container, String uri, Drawable drawable) {
	            this.setDrawable(container, drawable);
	        }
	    };
	    bitmapUtils.display(imageView, imgUrl, bigPicDisplayConfig, callback);
	    //bitmapUtils.getBitmapFromMemCache(uri, config)
    }
	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch (v.getId()) {
		case R.id.txinfo:
			new ActionSheetDialog(getActivity())
			.builder()
			.setCancelable(true)
			.setCanceledOnTouchOutside(true)
			.addSheetItem("拍照", SheetItemColor.Blue,
					new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
			                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			                getActivity().startActivityForResult(intent, 2);
						}
					})
			.addSheetItem("从手机相册选取", SheetItemColor.Blue,
					new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							Intent intent = new Intent();
							intent.setClass(getActivity(), ChoosePhotosActivity.class);
							getActivity().startActivity(intent);
						}
					}).show();
			break;
		case R.id.ncinfo:
			intent = new Intent();
			intent.putExtra("type", "1");
			intent.setClass(getActivity(), JrycActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.grsminfo:
			intent = new Intent();
			intent.putExtra("type", "2");
			intent.setClass(getActivity(), JrycActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.sexinfo:
			new ActionSheetDialog(getActivity())
			.builder()
			.setCancelable(true)
			.setCanceledOnTouchOutside(true)
			.addSheetItem("女", SheetItemColor.Blue,
					new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							App.user.setSex("0");
							sex.setText("女");
							updatePerson();
						}
					})
			.addSheetItem("男", SheetItemColor.Blue,
					new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							App.user.setSex("1");
							sex.setText("男");
							updatePerson();
						}
					}).show();
			break;
		case R.id.srinfo:
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			final View timepickerview = inflater.inflate(R.layout.timepicker,
					null);
			ScreenInfo screenInfo = new ScreenInfo(getActivity());
			final WheelMain wheelMain = new WheelMain(timepickerview);
			wheelMain.screenheight = screenInfo.getHeight();
			String time = birthday.getText().toString();
			Calendar calendar = Calendar.getInstance();
			if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
				try {
					calendar.setTime(dateFormat.parse(time));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			wheelMain.initDateTimePicker(year, month, day);
			new AlertDialog.Builder(getActivity())
					.setTitle("选择时间")
					.setView(timepickerview)
					.setNegativeButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,int which) {
									birthday.setText(wheelMain.getTime());
									App.user.setBirthday(wheelMain.getTime());
									String m=wheelMain.getTime().substring(wheelMain.getTime().indexOf("-")+1,wheelMain.getTime().lastIndexOf("-") );
									String d=wheelMain.getTime().substring(wheelMain.getTime().lastIndexOf("-")+1, wheelMain.getTime().length());
									String xzs=Start.getConstellation(Integer.parseInt(m), Integer.parseInt(d));
									xz.setText(xzs);
									App.user.setConstellation(xzs);
									updatePerson();
								}
							})
					.setPositiveButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,int which) {
								}
							}).show();
			break;
		case R.id.gxinfo:
			View view = dialogm();
			final MyAlertDialog dialog1 = new MyAlertDialog(getActivity());
			dialog1.builder()
			.setTitle("选择出生地")
			.setView(view)
			.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});
			dialog1.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(View v) {
					App.user.setProvince( AddressData.PROVINCES[country.getCurrentItem()]);
					App.user.setCity(AddressData.CITIES[country.getCurrentItem()][city.getCurrentItem()]);
					App.user.setCountry(AddressData.COUNTIES[country.getCurrentItem()][city.getCurrentItem()][ccity.getCurrentItem()]);
					gx.setText(App.user.getProvince()+" "+App.user.getCity()+" "+App.user.getCountry());
					updatePerson();
				}
			});
			dialog1.show();
			break;
		default:
			break;
		}
	}
	private View dialogm() {
		View contentView = LayoutInflater.from(getActivity()).inflate(
				R.layout.wheelcity_cities_layout, null);
		country = (WheelView) contentView
				.findViewById(R.id.wheelcity_country);
		country.setVisibleItems(3);
		country.setViewAdapter(new CountryAdapter(getActivity()));

		final String cities[][] = AddressData.CITIES;
		final String ccities[][][] = AddressData.COUNTIES;
		city = (WheelView) contentView
				.findViewById(R.id.wheelcity_city);
		city.setVisibleItems(0);

		// 地区选择
		ccity = (WheelView) contentView
				.findViewById(R.id.wheelcity_ccity);
		ccity.setVisibleItems(0);// 不限城市

		country.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateCities(city, cities, newValue);
				App.user.setProvince( AddressData.PROVINCES[country.getCurrentItem()]);
				App.user.setCity(AddressData.CITIES[country.getCurrentItem()][city.getCurrentItem()]);
				App.user.setCountry(AddressData.COUNTIES[country.getCurrentItem()][city.getCurrentItem()][ccity.getCurrentItem()]);
			}
		});

		city.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updatecCities(ccity, ccities, country.getCurrentItem(),newValue);
				App.user.setProvince( AddressData.PROVINCES[country.getCurrentItem()]);
				App.user.setCity(AddressData.CITIES[country.getCurrentItem()][city.getCurrentItem()]);
				App.user.setCountry(AddressData.COUNTIES[country.getCurrentItem()][city.getCurrentItem()][ccity.getCurrentItem()]);
			}
		});

		ccity.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				App.user.setProvince( AddressData.PROVINCES[country.getCurrentItem()]);
				App.user.setCity(AddressData.CITIES[country.getCurrentItem()][city.getCurrentItem()]);
				App.user.setCountry(AddressData.COUNTIES[country.getCurrentItem()][city.getCurrentItem()][ccity.getCurrentItem()]);
			}
		});
		country.setCurrentItem(19);// 设置北京
		city.setCurrentItem(3);
		ccity.setCurrentItem(4);
		return contentView;
	}

	/**
	 * Updates the city wheel
	 */
	private void updateCities(WheelView city, String cities[][], int index) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(getActivity(),
				cities[index]);
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(0);
	}

	/**
	 * Updates the ccity wheel
	 */
	private void updatecCities(WheelView city, String ccities[][][], int index,
			int index2) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(getActivity(),
				ccities[index][index2]);
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(0);
	}

	/**
	 * Adapter for countries
	 */
	private class CountryAdapter extends AbstractWheelTextAdapter {
		// Countries names
		private String countries[] = AddressData.PROVINCES;

		/**
		 * Constructor
		 */
		protected CountryAdapter(Context context) {
			super(context, R.layout.wheelcity_country_layout, NO_RESOURCE);
			setItemTextResource(R.id.wheelcity_country_name);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return countries.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return countries[index];
		}
	}
	private void updatePerson() {
 		RequestParams params = new RequestParams("UTF-8");
 		try {
 			JSONObject json=new JSONObject();
 			json.put("accountId", sharedPreferences.getString("id", ""));
 			json.put("accountName", URLEncoder.encode(App.user.getName()==null?"":App.user.getName(),"utf-8"));
 			json.put("birthday",App.user.getBirthday());
 			json.put("description", URLEncoder.encode(App.user.getRemark()==null?"":App.user.getRemark(),"utf-8"));
 			json.put("province", URLEncoder.encode(App.user.getProvince()==null?"":App.user.getProvince(),"utf-8"));
 			json.put("city", URLEncoder.encode(App.user.getCity()==null?"":App.user.getCity(),"utf-8"));
 			json.put("constellation", URLEncoder.encode(App.user.getConstellation()==null?"":App.user.getConstellation(),"utf-8"));
 			json.put("photo", App.user.getPhotoUrl());
 			json.put("request", PublicJsonParse.getPublicParam("updatePerson"));
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
 		http.send(HttpRequest.HttpMethod.POST,App.service+"updatePerson",params,
 		    new RequestCallBack<String>(){
 		        @Override
 		        public void onSuccess(ResponseInfo<String> responseInfo) {
 		        	Gson gson=new GsonBuilder().serializeNulls().create();
 		        	Type typeToken = new TypeToken<HttpResult<UserInfo>>(){}.getType();
 		        	String jsonElement=responseInfo.result.replace("[]", "null");
 		        	HttpResult<UserInfo> result=gson.fromJson(jsonElement,typeToken);
 		        	if(result!=null)
 		        	{
	 		        	if(result.isResult()){
	 		        		
	 		        	}else{
	 		        		Toast toast = Toast.makeText(getActivity().getApplicationContext(),result.getError(), Toast.LENGTH_SHORT);
	 						toast.setGravity(Gravity.CENTER, 0, 0);
	 						toast.show();
	 		        	}
 		        	}
 		        }

 		        @Override
 		        public void onFailure(HttpException error, String msg) {
 		        	
 		        }
 		});
 	}
	public void addShared(UserInfo user){
	    Editor editor = sharedPreferences.edit();
	    editor.putString("id",user.getId());
	    editor.putString("accountid",user.getAccountId());
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
	    editor.putString("password",user.getPassword());
	    editor.commit();
	}
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (resultCode == 0)   
            return;
    	else{
    		if(requestCode==2)
				startPhotoZoom(uri);
    		else{
		        // 拍照
		        if (resultCode ==-1) {//拍照 
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
		            tximage.setImageBitmap(bitmap);// 将图片显示在ImageView里  
		            LeftSlidingMenuFragment.roundedImageView.setImageBitmap(bitmap);
		            App.user.setPhotoUrl(ReadImgToBinary.imgToBase64(null, bitmap, null));
		        }else if(resultCode==1){
		        	Bundle bundle = data.getExtras();
		        	String names=bundle.getString("name");
		        	name.setText(names);
		        	App.user.setName(names);
		        }else{
		        	Bundle bundle = data.getExtras();
		        	String names=bundle.getString("name");
		        	grsm.setText(names);
		        	App.user.setRemark(names);
		        }
		        updatePerson();
    		}
    	}
    }
	/**
     * 收缩图片
     * 
     * @param uri 
     */   
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true"); 
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1); 
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 64);
        intent.putExtra("outputY", 64);
        intent.putExtra("return-data", true); 
        startActivityForResult(intent, 3);
    } 
}
