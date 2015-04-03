package com.ycr.slidemenu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TabWidget;
import android.widget.Toast;

import com.ycr.util.MyFragment;
import com.indoorclub.girl.R;
/**
 * 易信页面
 * @author Administrator
 *
 */
public class FragmentWdzl extends MyFragment implements OnClickListener{
    private TabWidget mTabWidget;
    private String[] addresses = { "个人资料", "个人相册", "个人视频" };
    private Button[] mBtnTabs = new Button[addresses.length];
    public static int type=0;
    private Fragment fragment;
    public FragmentWdzl(){}
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_wdzl, container, false);
    	mTabWidget = (TabWidget) view.findViewById(R.id.tabWidget1);
        mTabWidget.setStripEnabled(false);
        mBtnTabs[0] = new Button(getActivity());
        mBtnTabs[0].setFocusable(true);
        mBtnTabs[0].setText(addresses[0]);
        mBtnTabs[0].setTextColor(Color.GRAY);
        mBtnTabs[0].setBackgroundColor(Color.TRANSPARENT);
        mBtnTabs[0].setBackgroundResource(R.drawable.wdzl_bg_selector);
        mTabWidget.addView(mBtnTabs[0]);
        /* 
         * Listener必须在mTabWidget.addView()之后再加入，用于覆盖默认的Listener，
         * mTabWidget.addView()中默认的Listener没有NullPointer检测。
         */
        mBtnTabs[0].setOnClickListener(mTabClickListener);
        mBtnTabs[1] = new Button(getActivity());
        mBtnTabs[1].setFocusable(true);
        mBtnTabs[1].setText(addresses[1]);
        mBtnTabs[1].setTextColor(Color.GRAY);
        mBtnTabs[1].setBackgroundColor(Color.TRANSPARENT);
        mBtnTabs[1].setBackgroundResource(R.drawable.wdzl_bg_selector);
        mTabWidget.addView(mBtnTabs[1]);
        mBtnTabs[1].setOnClickListener(mTabClickListener);
        mBtnTabs[2] = new Button(getActivity());
        mBtnTabs[2].setFocusable(true);
        mBtnTabs[2].setText(addresses[2]);
        mBtnTabs[2].setTextColor(Color.GRAY);
        mBtnTabs[2].setBackgroundColor(Color.TRANSPARENT);
        mBtnTabs[2].setBackgroundResource(R.drawable.wdzl_bg_selector);
        mTabWidget.addView(mBtnTabs[2]);
        mBtnTabs[2].setOnClickListener(mTabClickListener);
        mTabWidget.setCurrentTab(0);
        switchContent();
    	return view;
    }
    private void init(int index) {
 		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
 		ft.add(R.id.realtabcontent, new FragmentGrzl() ,index+"");//将得到的fragment 替换当前的viewGroup内容，add则不替换会依次累加
 		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);//设置动画效果
 		ft.commitAllowingStateLoss();//提交
 	}
	@Override
	public void onClick(View v) {
		Toast.makeText(getActivity().getApplicationContext(), "很好！", Toast.LENGTH_SHORT).show();
	}
	private OnClickListener mTabClickListener = new OnClickListener() {
        @Override
        public void onClick(View v)
        {
        	if (v == mBtnTabs[0])
            {
            	type=0;
            } else if (v == mBtnTabs[1])
            {
            	type=1;
            } else if (v == mBtnTabs[2])
            {
            	type=2;
            }
        	switchContent();
        }
    };
    public void switchContent(){
    	if (type == 0)
        {
        	placeView(0);
            mBtnTabs[0].setTextColor(Color.RED);
            mBtnTabs[1].setTextColor(Color.GRAY);
            mBtnTabs[2].setTextColor(Color.GRAY);
        } else if (type==1)
        {
        	placeView(1);
            mBtnTabs[0].setTextColor(Color.GRAY);
            mBtnTabs[1].setTextColor(Color.RED);
            mBtnTabs[2].setTextColor(Color.GRAY);
        } else if (type==2)
        {
        	placeView(2);
            mBtnTabs[0].setTextColor(Color.GRAY);
            mBtnTabs[1].setTextColor(Color.GRAY);
            mBtnTabs[2].setTextColor(Color.RED);
        }
    }
    public void placeView(int index){
    	fragment = getChildFragmentManager().findFragmentByTag(index+"");// getActivity().getSupportFragmentManager().findFragmentByTag(index+""); 
		//得到一个fragment 事务（类似sqlite的操作）
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		Bundle bundle =null;
		if (fragment == null ) {
			switch (index) {
			case 0:
				fragment=new FragmentGrzl();
				break;
			case 1:
				fragment= new FragmentGrxc();
				bundle = new Bundle();
				bundle.putInt("type", 1);
		        fragment.setArguments(bundle);
				break;
			default:
				//fragment= new FragmentGrsp();
				fragment= new FragmentGrsp();
				bundle = new Bundle();
				bundle.putInt("type", 2);
		        fragment.setArguments(bundle);
		        break;
			}
		}
		ft.replace(R.id.realtabcontent, fragment,index+"");//将得到的fragment 替换当前的viewGroup内容，add则不替换会依次累加
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);//设置动画效果
		//ft.addToBackStack(null);
		ft.commitAllowingStateLoss();//提交

	}
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	fragment.onActivityResult(requestCode, resultCode, data);
    }
}
