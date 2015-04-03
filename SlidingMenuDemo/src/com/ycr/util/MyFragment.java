package com.ycr.util;

import android.support.v4.app.Fragment;
/**
 * ListView
 * @author Administrator
 *
 */
public class MyFragment extends Fragment{
	private boolean clickFlag=true;
	public boolean isClickFlag() {
		return clickFlag;
	}

	public void setClickFlag(boolean clickFlag) {
		this.clickFlag = clickFlag;
	}
}
