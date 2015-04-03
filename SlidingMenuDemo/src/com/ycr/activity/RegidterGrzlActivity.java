package com.ycr.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.indoorclub.girl.R;
import com.ycr.choosephotos.ChoosePhotosActivity;
import com.ycr.pojo.App;
import com.ycr.util.GPSDetection;
import com.ycr.util.ReadImgToBinary;
import com.ycr.util.Start;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;
import com.zf.iosdialog.widget.MyAlertDialog;

public class RegidterGrzlActivity extends Activity implements OnClickListener{
	private GPSDetection GPSDetection=null;
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
	private View xzlayout;
	private View sexinfo;
	private TextView grsm;
	private View grsminfo;
    private WheelView country;
    private WheelView city;
    private WheelView ccity;
    private ImageButton back;
	private ImageButton add;
	private TextView title;
	private boolean upImage=false;
	long lastClick=0;
	private File file;
    private Uri uri;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grzl_tab);
		file = new File(this.getExternalFilesDir(null), "image.jpg");
        uri = Uri.fromFile(file);
		lastClick=System.currentTimeMillis();
		back=(ImageButton) findViewById(R.id.back);
		add=(ImageButton)findViewById(R.id.add);
		add.setVisibility(View.VISIBLE);
		title=(TextView)findViewById(R.id.title);
		title.setText("个人资料");
		back.setOnClickListener(this);
		add.setOnClickListener(this);
    	tximage=(ImageView)findViewById(R.id.tximage);
    	Intent intent=getIntent();
    	boolean isPhotoZoom=intent.getBooleanExtra("isPhotoZoom", false);
    	if(isPhotoZoom){
    		uri=Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), ReadImgToBinary.base64ToBitmap(App.user.getPhotoUrl(), "usertemp", null), null,null));
            startPhotoZoom(uri);
    	}
    	else if(App.user.getPhotoUrl()!=null&&!App.user.getPhotoUrl().equals("")){
    		tximage.setImageBitmap(ReadImgToBinary.base64ToBitmap(App.user.getPhotoUrl(), "usertemp", null));
    		upImage=true;
    	}
    	txinfo=(View)findViewById(R.id.txinfo);
    	name=(TextView)findViewById(R.id.name);
    	name.setText(App.user.getName());
    	ncinfo=(View)findViewById(R.id.ncinfo);
    	sex=(TextView)findViewById(R.id.sex);
    	if(App.user.getSex()!=null)
    		sex.setText(App.user.getSex().equals("0")?"女":"男");
    	birthday=(TextView)findViewById(R.id.birthday);
    	birthday.setText(App.user.getBirthday());
    	srinfo=(View)findViewById(R.id.srinfo);
    	gx=(TextView)findViewById(R.id.gx);
    	gx.setText(App.user.getCountry()==null?"":App.user.getCountry()+" "+App.user.getProvince()==null?"":App.user.getProvince()+" "+App.user.getCity()==null?"":App.user.getCity());
    	gxinfo=(View)findViewById(R.id.gxinfo);
    	xz=(TextView)findViewById(R.id.xz);
    	xz.setText(App.user.getConstellation());
    	xzlayout=findViewById(R.id.xzlayout);
    	xzlayout.setVisibility(View.GONE);
    	grsm=(TextView)findViewById(R.id.grsm);
    	grsm.setText(App.user.getRemark());
    	grsminfo=(View)findViewById(R.id.grsminfo);
    	sexinfo=(View)findViewById(R.id.sexinfo);
    	sexinfo.setOnClickListener(this);
    	txinfo.setOnClickListener(this);
    	ncinfo.setOnClickListener(this);
    	srinfo.setOnClickListener(this);
    	gxinfo.setOnClickListener(this);
    	grsminfo.setOnClickListener(this);
    	GPSDetection=new GPSDetection(this);
        boolean gps_status=GPSDetection.GPSProvider();
        if(!gps_status)
        {
        	//new AlertDialog.Builder(LoginActivity.this).setTitle("GPS检测提示").setMessage("GPS未开启！").show();
        	final AlertDialog.Builder dialog02 = new AlertDialog.Builder(RegidterGrzlActivity.this);
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
    }
	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch (v.getId()) {
		case R.id.back:
			intent=new Intent(RegidterGrzlActivity.this,RegidterActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			RegidterGrzlActivity.this.startActivity(intent);
			finish();
			break;
		case R.id.add:
			if (System.currentTimeMillis() - lastClick <= 3000)
				return;
			else
			{
				if(upImage==false){
					Toast toast = Toast.makeText(getApplicationContext(),"必须上传头像", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				}else if(name.getText().toString().equals("")){
					Toast toast = Toast.makeText(getApplicationContext(),"昵称必填", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				}else if(sex.getText().toString().equals("")){
					Toast toast = Toast.makeText(getApplicationContext(),"性别必选", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				}else if(birthday.getText().toString().equals("")){
					Toast toast = Toast.makeText(getApplicationContext(),"生日必选", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				}else if(gx.getText().toString().equals("")){
					Toast toast = Toast.makeText(getApplicationContext(),"故乡必选", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				}else if(grsm.getText().toString().equals("")){
					Toast toast = Toast.makeText(getApplicationContext(),"个人说明必填", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				}else{
					intent = new Intent();
					intent.putExtra("isRegidter", true);
					intent.setClass(this, RegidterGrxcActivity.class);
					startActivity(intent);
					finish();
				}
			}
			lastClick=System.currentTimeMillis();
			break;
		case R.id.txinfo:
			new ActionSheetDialog(this)
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
			                startActivityForResult(intent, 2);
						}
					})
			.addSheetItem("从手机相册选取", SheetItemColor.Blue,
					new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							Intent intent = new Intent();
							intent.putExtra("isRegidter", true);
							intent.setClass(RegidterGrzlActivity.this, ChoosePhotosActivity.class);
							RegidterGrzlActivity.this.startActivity(intent);
						}
					}).show();
			break;
		case R.id.ncinfo:
			intent = new Intent();
			intent.putExtra("type", "1");
			intent.putExtra("isRegidter", true);
			intent.setClass(this, JrycActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.sexinfo:
			new ActionSheetDialog(this)
			.builder()
			.setCancelable(true)
			.setCanceledOnTouchOutside(true)
			.addSheetItem("女", SheetItemColor.Blue,
					new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							App.user.setSex("0");
							sex.setText("女");
						}
					})
			.addSheetItem("男", SheetItemColor.Blue,
					new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							App.user.setSex("1");
							sex.setText("男");
						}
					}).show();
			break;
		case R.id.grsminfo:
			intent = new Intent();
			intent.putExtra("type", "2");
			intent.putExtra("isRegidter", true);
			intent.setClass(this, JrycActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.srinfo:
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			LayoutInflater inflater = LayoutInflater.from(this);
			final View timepickerview = inflater.inflate(R.layout.timepicker,
					null);
			ScreenInfo screenInfo = new ScreenInfo(this);
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
			new AlertDialog.Builder(this)
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
			final MyAlertDialog dialog1 = new MyAlertDialog(this);
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
				}
			});
			dialog1.show();
			break;
		default:
			break;
		}
	}
	private View dialogm() {
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.wheelcity_cities_layout, null);
		country = (WheelView) contentView
				.findViewById(R.id.wheelcity_country);
		country.setVisibleItems(3);
		country.setViewAdapter(new CountryAdapter(this));

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
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
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
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
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
	// 需要接收上一个Activity返回的数据,要重写Activity的 onActivityResult()方法.
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 依据resultCode进行接收,这也是上个界面中需要设置resultCode的原因了
		
		if(resultCode==0)
			return;
		else{
			if(requestCode==2)
				startPhotoZoom(uri);
			else{
				switch (resultCode) {
				case 1:
					name.setText(App.user.getName());
					break;
				case 2:
					grsm.setText(App.user.getRemark());
					break;
				case 4:
					tximage.setImageBitmap(ReadImgToBinary.base64ToBitmap(App.user.getPhotoUrl(), "usertemp", null));
				case -1:
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
		            upImage=true;
		            App.user.setPhotoUrl(ReadImgToBinary.imgToBase64(null, bitmap, null));
					break;
					default:
						break;
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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
