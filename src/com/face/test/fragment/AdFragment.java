package com.face.test.fragment;

import net.youmi.android.diy.DiyManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class AdFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		LinearLayout layout=new LinearLayout(getActivity());
		DiyManager.showRecommendWall(getActivity()); 
		return layout;
	}

}
