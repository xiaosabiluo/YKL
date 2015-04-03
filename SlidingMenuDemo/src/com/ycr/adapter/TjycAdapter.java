package com.ycr.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.indoorclub.girl.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.ycr.activity.YcpyActivity;
import com.ycr.pojo.CompanysInfo;
import com.ycr.util.BitmapHelp;

public class TjycAdapter extends BaseAdapter {
	private BitmapUtils bitmapUtils;
    private BitmapDisplayConfig bigPicDisplayConfig;
	private Activity mContext;
	private List<CompanysInfo> dataList=null;
	private boolean flag=true;
	private Drawable drawable;
	public TjycAdapter(Activity c, List<CompanysInfo> list,boolean flag) {
		mContext = c;
		dataList =list;
		this.flag=flag;
		drawable=mContext.getResources().getDrawable(R.drawable.photo);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}
	@Override
	public int getCount() {
		return dataList.size();
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		 final CompanysInfo user=dataList.get(position);
		 ImageView add=null;
		 if(flag){
			 convertView=View.inflate(mContext, R.layout.tjyc_item, null);
			 add=(ImageView)convertView.findViewById(R.id.addYc);
			 add.setBackgroundResource(R.drawable.btn_add);
			 TextView desc = (TextView) convertView.findViewById(R.id.desc);
			 desc.setText(user.getConsume());
		 }else{
			 convertView=View.inflate(mContext, R.layout.tjyc_item_grid, null);
			 add=(ImageView)convertView.findViewById(R.id.addYc);
			 add.setBackgroundResource(R.drawable.btn_joinclub_pressed);
			 ImageView userimage=(ImageView)convertView.findViewById(R.id.userimage);
			 if(user.getCompanyPhotoUrl().equals("")||user.getCompanyPhotoUrl()==null)
				 userimage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.photo));
				 else
					photo(userimage,user.getCompanyPhotoUrl());
		 }
		 if(user.getIsHavingJoin().equals("1"))
			 add.setVisibility(View.GONE);
		 TextView userid = (TextView) convertView.findViewById(R.id.ycid);
		 final String id=user.getCompanyId();
		 userid.setText(id);
		 ImageView image=(ImageView)convertView.findViewById(R.id.image);
		 if(user.getCompanyPhotoUrl().equals("")||user.getCompanyPhotoUrl()==null)
				image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.photo));
			 else
			 {
				String companyImage=user.getCompanyPhotoUrl();
				photo(image,companyImage.substring(0,companyImage.lastIndexOf("/")));
			 }
		 TextView name = (TextView) convertView.findViewById(R.id.name);
		 final String names=user.getCompanyName();
		 name.setText(names);
		 //ImageView stateImage=(ImageView)convertView.findViewById(R.id.stateImage);
		 TextView count = (TextView) convertView.findViewById(R.id.count);
		 count.setText(user.getCompanyNum());
		 //ImageView ageImage=(ImageView)convertView.findViewById(R.id.ageImage);
		 TextView space = (TextView) convertView.findViewById(R.id.space);
		 space.setText(Math.round(Double.parseDouble(user.getCompanyDistance())/100d/10d)+"千米");
		 TextView address = (TextView) convertView.findViewById(R.id.address);
		 address.setText(user.getCompanyAddress());
		 add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String isHaveCompany=user.getIsHaveCompany();
				Intent intent = new Intent();
				/*if(isHaveCompany.equals("1")){
					intent.putExtra("name", names);
					intent.putExtra("clubId", id);
					intent.putExtra("type", 1);
					intent.putExtra("managerId", "");
					intent.putExtra("isHaveCompany", isHaveCompany);
					intent.setClass(mContext, JrycActivity.class);
					mContext.startActivity(intent);
				}else{
					intent.putExtra("id", id);
					intent.putExtra("name", names);
					intent.putExtra("type", 1);
					intent.putExtra("isHaveCompany", isHaveCompany);
					intent.setClass(mContext, YcpyActivity.class);
					mContext.startActivity(intent);
				}旧的逻辑*/
				intent.putExtra("id", id);
				intent.putExtra("name", names);
				intent.putExtra("type", 1);
				intent.putExtra("isHaveCompany", isHaveCompany);
				intent.setClass(mContext, YcpyActivity.class);
				mContext.startActivity(intent);
			}
		});
		 return convertView;
	
	}
	private void photo(ImageView imageView,String imgUrl){
	    if (bitmapUtils == null) {
	        bitmapUtils = BitmapHelp.getBitmapUtils(mContext);
	    }

	    bigPicDisplayConfig = new BitmapDisplayConfig();
	    //bigPicDisplayConfig.setShowOriginal(true); // 显示原始图片,不压缩, 尽量不要使用, 图片太大时容易OOM。
	    bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
	    bigPicDisplayConfig.setLoadFailedDrawable(drawable);
	    bigPicDisplayConfig.setBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext));
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
	public List<CompanysInfo> getDataList() {
		return dataList;
	}
	public void setDataList(List<CompanysInfo> dataList) {
		this.dataList = dataList;
	}
	
}
