package com.ycr.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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
import com.ycr.activity.GrxcImageViewActivity;
import com.ycr.pojo.MediaInfo;
import com.ycr.slidemenu.FragmentGrxc;
import com.ycr.util.BitmapHelp;

public class ImageAdapter extends BaseAdapter {
	private BitmapUtils bitmapUtils;
    private BitmapDisplayConfig bigPicDisplayConfig;
	private Activity mContext;
	private List<MediaInfo> dataList=null;
	private List<MediaInfo> selectlist;
	public ImageAdapter(Activity c, List<MediaInfo> list, List<MediaInfo> sellist) {
		mContext = c;
		dataList =list;
		selectlist=sellist;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	class Holder {
		private ImageView iv;
		private ImageView selected;
		private TextView text;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;

		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(mContext, R.layout.item_image_grid, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.image);
			holder.selected = (ImageView) convertView
					.findViewById(R.id.isselected);
			holder.text = (TextView) convertView
					.findViewById(R.id.item_image_grid_text);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final MediaInfo item = dataList.get(position);
		photo(holder.iv,item.getMediaUrl());
		if(position!=dataList.size()-1)
			holder.selected.setImageResource(-1);
		holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
		holder.text.setBackgroundColor(Color.GRAY);
		holder.iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
					item.setSelected(!item.isSelected());
					if (item.isSelected()&&FragmentGrxc.flag) {
						holder.selected.setImageResource(R.drawable.icon_data_select);
						//holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
						selectlist.add(item);
					}else if(!item.isSelected()&&FragmentGrxc.flag){
						holder.selected.setImageResource(-1);
						//holder.text.setBackgroundColor(0x00000000);
						
						item.setSelected(false);
					}else{
						if(position==dataList.size()-1){
							/*Intent intent = new Intent(mContext,ImageGridActivity.class);
							mContext.startActivityForResult(intent, 1);*/
						}else{
							Intent intent = new Intent();
							intent.putExtra("infoid", "");
							intent.setClass(mContext, GrxcImageViewActivity.class);
							mContext.startActivity(intent);
						}
					}
			}

		});

		return convertView;
	}
	public List<MediaInfo> getSelectlist() {
		return selectlist;
	}
	public void setSelectlist(List<MediaInfo> selectlist) {
		this.selectlist = selectlist;
	}
	private void photo(ImageView imageView,String imgUrl){
	    if (bitmapUtils == null) {
	        bitmapUtils = BitmapHelp.getBitmapUtils(mContext);
	    }

	    bigPicDisplayConfig = new BitmapDisplayConfig();
	    //bigPicDisplayConfig.setShowOriginal(true); // 显示原始图片,不压缩, 尽量不要使用, 图片太大时容易OOM。
	    bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
		case 0:
		Bundle b=data.getExtras();  //data为B中回传的Intent
		String str=b.getString("ListenB");//str即为回传的值"Hello, this is B speaking"
		/* 得到B回传的数据后做什么... 略 */
		                      break;
		default:
		          break;
		}
		}
}
