package com.ycr.choosephotos;

import java.io.Serializable;
import java.util.List;





import com.indoorclub.girl.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ChoosePhotosActivity extends Activity {
	List<ImageBucket> dataList;
	GridView gridView;
	ImageBucketAdapter adapter;// 自定义的适配器
	AlbumHelper helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	private ImageButton back;
	private TextView title;
	private boolean isRegidter=false;
	private boolean isImages=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_image_bucket);
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		isRegidter=getIntent().getBooleanExtra("isRegidter", false);
		isImages=getIntent().getBooleanExtra("isImages", false);
		initData();
		initView();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		dataList = helper.getImagesBucketList(false);	
		bimap=BitmapFactory.decodeResource(
				getResources(),
				R.drawable.icon_addpic_unfocused);
	}

	/**
	 * 初始化view视图
	 */
	private void initView() {
		back=(ImageButton) findViewById(R.id.back);
		title=(TextView)findViewById(R.id.title);
		title.setText("相册");
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new ImageBucketAdapter(ChoosePhotosActivity.this, dataList);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Intent intent = new Intent(ChoosePhotosActivity.this,ImageGridActivity.class);
				intent.putExtra(ChoosePhotosActivity.EXTRA_IMAGE_LIST,(Serializable) dataList.get(position).imageList);
				intent.putExtra("isRegidter", isRegidter);
				intent.putExtra("isImages", isImages);
				startActivityForResult(intent, 100);
			}

		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode!=Activity.RESULT_OK){
			return;
		}
		switch (requestCode) {
		case 100:
			setResult(Activity.RESULT_OK,data);
			finish();
			break;

		default:
			break;
		}
	}
}
