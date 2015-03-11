package com.face.test.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.demo_zhy_mms_miui.ViewPagerIndicator;
import com.face.test.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TestFragment extends Fragment {
	private ViewPager mViewPager;
	private ViewPagerIndicator mIndicator;
	private MyViewAdapter mAdapter;
	private List<String> mDatas = Arrays.asList("dsafd1", "sf2");

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.main4, container, false);
		mViewPager = (ViewPager) view.findViewById(R.id.pager);
		mIndicator = (ViewPagerIndicator) view.findViewById(R.id.id_indicator);
		mIndicator.setTabItemTitles(mDatas);
		
		List<ImageView> list=new ArrayList<ImageView>();
		ImageView  i1=new ImageView(getActivity());
		i1.setImageResource(R.id.loading);
		ImageView  i2=new ImageView(getActivity());
		i2.setImageResource(R.id.loading);
		list.add(i1);
		list.add(i2);
		mAdapter = new MyViewAdapter(list);
		mViewPager.setAdapter(mAdapter);
		// 设置关联的ViewPager
		mIndicator.setViewPager(mViewPager, 0);
		return view;
	}

	public class MyViewAdapter extends PagerAdapter {
		public List<ImageView> imageViews;
		public MyViewAdapter(List<ImageView> imageViews){
			this.imageViews=imageViews;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			container.addView(imageViews.get(position));
			return imageViews.get(position);
		}

	}

}
