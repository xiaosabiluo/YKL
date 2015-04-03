package com.ycr.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.ycr.pojo.CompanysInfo;
import com.ycr.slidemenu.FragmentWdyc.MyOnClickListener;
import com.ycr.util.BitmapHelp;
import com.indoorclub.girl.R;

public class WdycAdapter extends BaseAdapter {
	private BitmapUtils bitmapUtils;
    private BitmapDisplayConfig bigPicDisplayConfig;
	private Activity mContext;
	private List<CompanysInfo> dataList=null;
	private boolean flag=true;
	private MyOnClickListener listener;
	private Drawable drawable;
	public WdycAdapter(Activity c, List<CompanysInfo> list,boolean flag,MyOnClickListener listener) {
		mContext = c;
		dataList =list;
		this.flag=flag;
		this.listener=listener;
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
		 CompanysInfo user=dataList.get(position);
		 listener.setPosition(position);
		 ImageView add=null;
		 if(flag){
			 convertView=View.inflate(mContext, R.layout.tjyc_item, null);
			 add=(ImageView)convertView.findViewById(R.id.addYc);
			 add.setBackgroundResource(R.drawable.btn_del);
			 TextView desc = (TextView) convertView.findViewById(R.id.desc);
			 desc.setText(user.getConsume());
		 }else{
			 convertView=View.inflate(mContext, R.layout.tjyc_item_grid, null);
			 add=(ImageView)convertView.findViewById(R.id.addYc);
			 add.setBackgroundResource(R.drawable.btn_outclub_pressed);
			 ImageView userimage=(ImageView)convertView.findViewById(R.id.userimage);
			 if(user.getCompanyPhotoUrl().equals("")||user.getCompanyPhotoUrl()==null)
				 userimage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.photo));
				 else
					photo(userimage,user.getCompanyPhotoUrl());
		 }
		 
		 TextView userid = (TextView) convertView.findViewById(R.id.ycid);
		 final String id=user.getCompanyId();
		 userid.setText(id);
		 ImageView image=(ImageView)convertView.findViewById(R.id.image);
		 if(user.getCompanyPhotoUrl().equals("")||user.getCompanyPhotoUrl()==null)
				image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.photo));
			 else
				photo(image,user.getCompanyPhotoUrl());
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
		 add.setOnClickListener(listener);
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
