package com.face.test.adapter;

import java.util.List;

import com.face.test.MyApplication;
import com.face.test.R;
import com.face.test.Utils.BitmapUtil;
import com.face.test.Utils.Util;
import com.face.test.bean.Stars;
import com.face.test.bean.StarsInfos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StarsAdapter extends BaseAdapter {
	private Context context;
	private static LayoutInflater inflater;
	private StarsInfos starsInfos;
	private List<Stars> list;

	public StarsAdapter(Context context, List<Stars> list, StarsInfos starsInfos) {
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
		this.starsInfos = starsInfos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.listview_stats_item, null);
		TextView name = (TextView) convertView
				.findViewById(R.id.listview_item_name);
//		TextView brief = (TextView) convertView
//				.findViewById(R.id.listview_item_brief);
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.listview_item_image);
		ImageView imageView1=(ImageView) convertView
				.findViewById(R.id.score_image_1);
		ImageView imageView2=(ImageView) convertView
				.findViewById(R.id.score_image_2);
		ImageView imageView3=(ImageView) convertView
				.findViewById(R.id.score_image_3);
		Stars stars = list.get(position);
		name.setText(stars.getName());
		MyApplication.displayImage(stars.getBmobFile().getFileUrl(), imageView);
		String image1=Util.change1(starsInfos.getCandidate().get(position)
				.getSimilarity(),1);
		String image2=Util.change1(starsInfos.getCandidate().get(position)
				.getSimilarity(),2);
		String image3=Util.change1(starsInfos.getCandidate().get(position)
				.getSimilarity(),3);
		
		
		imageView1.setImageBitmap(BitmapUtil.getImageFromAssetsFile(context, image1));
		imageView2.setImageBitmap(BitmapUtil.getImageFromAssetsFile(context, image2));
		imageView3.setImageBitmap(BitmapUtil.getImageFromAssetsFile(context, image3));
		
		
		return convertView;
	}

}
