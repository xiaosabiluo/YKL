package com.ycr.choosephotos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.indoorclub.girl.R;
import com.ycr.activity.RegidterGrxcActivity;
import com.ycr.activity.RegidterGrzlActivity;
import com.ycr.choosephotos.ImageGridAdapter.TextCallback;
import com.ycr.pojo.App;
import com.ycr.slidemenu.FragmentGrxc;
import com.ycr.slidemenu.FragmentWdzl;
import com.ycr.slidemenu.MainActivity;
import com.ycr.util.ReadImgToBinary;

public class ImageGridActivity extends Activity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	private List<ImageItem> dataList;
	private GridView gridView;
	private ImageGridAdapter adapter;
	private AlbumHelper helper;
	private Button bt;
	private ImageButton back;
	private TextView title;
	private boolean isRegidter=false;
	private boolean isImages=false;
	public static int countIma=0;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_image_grid);
		isRegidter=getIntent().getBooleanExtra("isRegidter",false);
		isImages=getIntent().getBooleanExtra("isImages",false);
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		dataList = (List<ImageItem>) getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);
		initView();
		bt = (Button) findViewById(R.id.bt);
		bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ArrayList<String> list = new ArrayList<String>();
				Collection<String> c = adapter.map.values();
				Iterator<String> it = c.iterator();
				for (; it.hasNext();) {
					list.add(it.next());
				}
				if (Bimp.act_bool) {
					setResult(Activity.RESULT_OK);
					Bimp.act_bool = false;
				}
				for (int i = 0; i < list.size(); i++) {
					if (Bimp.bmp.size() < 3) {	
						//ImageCaesium caes=new ImageCaesium();
						//Bitmap bm=caes.compressImage(list.get(i));
						try {
							Bitmap bm = Bimp.revitionImageSize(list.get(i));
							Bimp.bmp.add(bm);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}
				}
				if(isRegidter){
					if(isImages){
						Intent intent = new Intent(ImageGridActivity.this,RegidterGrxcActivity.class);
			            intent.putExtra("isRegidter", true);
			            intent.putExtra("isImages", true);
			            /*List<String> medias=new ArrayList<String>();
			            for(Bitmap bit:Bimp.bmp){
			            	String img=ReadImgToBinary.imgToBase64(null, bit, null);
			            	medias.add(img);
			            }
			            App.user.setMedias(medias);*/
			            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			            ImageGridActivity.this.startActivity(intent);
			            finish();
					}else{
						if(Bimp.bmp.size()==1){
							//LeftSlidingMenuFragment.roundedImageView.setImageBitmap(Bimp.bmp.get(0));
							App.user.setPhotoUrl(ReadImgToBinary.imgToBase64(null, Bimp.bmp.get(0), null));
							Intent intent = new Intent(ImageGridActivity.this,RegidterGrzlActivity.class);
				            intent.putExtra("isRegidter", true);
				            intent.putExtra("isImages", false);
				            intent.putExtra("isPhotoZoom", true);
				            Bimp.bmp=new ArrayList<Bitmap>();
				            ImageGridActivity.this.startActivity(intent);/*
							Bundle bundle = new Bundle();  
							bundle.putParcelable("data", Bimp.bmp.get(0));
							intent.putExtras(bundle);
							setResult(Activity.RESULT_OK, intent);*/
							//Bimp.bmp=new ArrayList<Bitmap>();
				            finish();
						}else if(Bimp.bmp.size()>1){
			            	Toast toast = Toast.makeText(getApplicationContext(),"只能上传1张图片", Toast.LENGTH_SHORT);
		 					toast.setGravity(Gravity.CENTER, 0, 0);
		 					toast.show();
		 					Bimp.bmp=new ArrayList<Bitmap>();
			            }
						else{
							Toast toast = Toast.makeText(getApplicationContext(),"至少上传1张图片", Toast.LENGTH_SHORT);
		 					toast.setGravity(Gravity.CENTER, 0, 0);
		 					toast.show();
		 					Bimp.bmp=new ArrayList<Bitmap>();
						}
						
					}
				}else{
					if(FragmentWdzl.type==0){
						if(Bimp.bmp.size()==1){
			            	/*Intent intent=new Intent();
							Bundle bundle = new Bundle();  
							bundle.putParcelable("data", Bimp.bmp.get(0));
							intent.putExtras(bundle);
							setResult(Activity.RESULT_OK, intent);
							Bimp.bmp=new ArrayList<Bitmap>();*/
							App.user.setPhotoUrl(ReadImgToBinary.imgToBase64(null, Bimp.bmp.get(0), null));
							FragmentWdzl.type=0;
				            Intent intent = new Intent(ImageGridActivity.this,MainActivity.class);
				            intent.putExtra("status",R.id.wdzlBtnLayout);
				            intent.putExtra("isPhotoZoom", true);
				            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				            ImageGridActivity.this.startActivity(intent);
				            //Bimp.bmp=new ArrayList<Bitmap>();
				            finish();
			            }else if(Bimp.bmp.size()>1){
			            	Toast toast = Toast.makeText(getApplicationContext(),"只能上传1张图片", Toast.LENGTH_SHORT);
		 					toast.setGravity(Gravity.CENTER, 0, 0);
		 					toast.show();
		 					Bimp.bmp=new ArrayList<Bitmap>();
			            }
						else{
							Toast toast = Toast.makeText(getApplicationContext(),"至少上传1张图片", Toast.LENGTH_SHORT);
		 					toast.setGravity(Gravity.CENTER, 0, 0);
		 					toast.show();
		 					Bimp.bmp=new ArrayList<Bitmap>();
						}
					}else if(FragmentWdzl.type==1){
						FragmentWdzl.type=1;
						FragmentGrxc.isUpload=true;
			            Intent intent = new Intent(ImageGridActivity.this,MainActivity.class);
			            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			            ImageGridActivity.this.startActivity(intent);
			            finish();
					}
				}
			}
		});
	}
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
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,mHandler);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new TextCallback() {
			public void onListen(int count) {
				bt.setText("完成" + "(" + count + ")");
			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.notifyDataSetChanged();
			}
		});
	}
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(ImageGridActivity.this, "最多选择3张图片", 400).show();
				break;

			default:
				break;
			}
		}
	};
}
