package com.ycr.activity;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.ycr.util.BitmapHelp;
import com.indoorclub.girl.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GrxcImageViewActivity extends Activity implements OnPageChangeListener{
	/**
	 * ViewPager
	 */
	private ViewPager viewPager;
	
	/**
	 * 装点点的ImageView数组
	 */
	private ImageView[] tips;
	private String[] imgUrls;
	private ImageView[] imageViews;
	private ImageButton back;
	private BitmapUtils bitmapUtils;
    private BitmapDisplayConfig bigPicDisplayConfig;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grxc_image_view);
		ViewGroup group = (ViewGroup)findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		back=(ImageButton)findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener()  
	    {   
		    public void onClick(View v)   
		    {  
		    	GrxcImageViewActivity.this.finish();  
		     }  
		});
		Intent intent=getIntent();
		imgUrls=intent.getExtras().getStringArray("url");
		//将点点加入到ViewGroup中
		tips = new ImageView[imgUrls.length];
		for(int i=0; i<tips.length; i++){
			ImageView imageView = new ImageView(this);
	    	imageView.setLayoutParams(new LayoutParams(10,10));
	    	tips[i] = imageView;
	    	if(i == 0){
	    		//tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
	    	}else{
	    		//tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
	    	}
	    	
	    	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,  LayoutParams.WRAP_CONTENT));
	    	layoutParams.leftMargin = 5;
	    	layoutParams.rightMargin = 5;
	    	group.addView(imageView, layoutParams);
		}
		
		
		//将图片装载到数组中
		imageViews = new ImageView[imgUrls.length];
		for(int i=0; i<imageViews.length; i++){
			ImageView imageView = new ImageView(this);
			imageViews[i] = imageView;
			photo(imageView, imgUrls[i]);
		}
		
		//设置Adapter
		viewPager.setAdapter(new MyAdapter());
		//设置监听，主要是设置点点的背景
		viewPager.setOnPageChangeListener(this);
		//设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
		viewPager.setCurrentItem((imageViews.length) * 100);
		
	}
	
	/**
	 * 
	 * @author xiaanming
	 *
	 */
	public class MyAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			//((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);
			
		}

		/**
		 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
		 */
		@Override  
	    public Object instantiateItem(View container, int position) {  
	        try {    
	            ((ViewPager)container).addView(imageViews[position % imageViews.length], 0);  
	        }catch(Exception e){  
	            //handler something  
	        }  
	        return imageViews[position % imageViews.length];  
	    }  
		
		
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		setImageBackground(arg0 % imageViews.length);
	}
	
	/**
	 * 设置选中的tip的背景
	 * @param selectItems
	 */
	private void setImageBackground(int selectItems){
		for(int i=0; i<tips.length; i++){
			if(i == selectItems){
				//tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			}else{
				//tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
		}
	}
	private void photo(ImageView imageView,String imgUrl){
	    if (bitmapUtils == null) {
	        bitmapUtils = BitmapHelp.getBitmapUtils(getApplicationContext());
	    }

	    bigPicDisplayConfig = new BitmapDisplayConfig();
	    //bigPicDisplayConfig.setShowOriginal(true); // 显示原始图片,不压缩, 尽量不要使用, 图片太大时容易OOM。
	    bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
	    bigPicDisplayConfig.setBitmapMaxSize(BitmapCommonUtils.getScreenSize(this));
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
}

