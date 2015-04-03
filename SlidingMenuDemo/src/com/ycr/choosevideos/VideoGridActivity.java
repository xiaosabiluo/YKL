package com.ycr.choosevideos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.ycr.choosevideos.VideoGridAdapter.TextCallback;
import com.ycr.slidemenu.FragmentGrsp;
import com.ycr.slidemenu.FragmentWdzl;
import com.ycr.slidemenu.MainActivity;

public class VideoGridActivity extends Activity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	private List<VideoItem> dataList;
	private GridView gridView;
	private VideoGridAdapter adapter;
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
		dataList = (List<VideoItem>) getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);
		initView();
		bt = (Button) findViewById(R.id.bt);
		bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ArrayList<List<String>> list = new ArrayList<List<String>>();
				Collection<List<String>> c = adapter.map.values();
				Iterator<List<String>> it = c.iterator();
				for (; it.hasNext();) {
					list.add(it.next());
				}
				if (Bimp.act_bool) {
					setResult(Activity.RESULT_OK);
					Bimp.act_bool = false;
				}
				for (int i = 0; i < list.size(); i++) {
					if (Bimp.bmp.size() < 3) {	
						List<String> video=new ArrayList<String>();
						video.add(list.get(i).get(0));
						video.add(list.get(0).get(1));
						Bimp.bmp.add(video);
					}
				}
				FragmentWdzl.type=2;
				FragmentGrsp.isUpload=true;
	            Intent intent = new Intent(VideoGridActivity.this,MainActivity.class);
	            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            VideoGridActivity.this.startActivity(intent);
	            finish();
			}
		});
	}
	private void initView() {
		back=(ImageButton) findViewById(R.id.back);
		title=(TextView)findViewById(R.id.title);
		title.setText("视频");
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new VideoGridAdapter(VideoGridActivity.this, dataList,mHandler);
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
				Toast.makeText(VideoGridActivity.this, "最多选择3个视频", 400).show();
				break;

			default:
				break;
			}
		}
	};
}
