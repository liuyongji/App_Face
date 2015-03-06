package com.face.test.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.face.test.ImageAdapter;
import com.face.test.ImagePagerActivity;
import com.face.test.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class PhotosFragment extends Fragment {
	public static  String path =Environment.getExternalStorageDirectory().getPath()
			+ "/facetest/";
	private List<String> list = new ArrayList<String>();
	private GridView mGridView;
	private ImageAdapter imageAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		File[] files =new File(path).listFiles();
		for (int i = 0; i < files.length; i++) {
			list.add("file:///"+ files[i].getAbsolutePath());
		}
		imageAdapter = new ImageAdapter(getActivity(), list);
		View view = inflater.inflate(R.layout.gridview_photo, container, false);
		mGridView = (GridView) view.findViewById(R.id.gv_photos);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				imageBrower(position, list);
			}
		});
		mGridView.setAdapter(imageAdapter);
		return view;
	}

	private void imageBrower(int position, List<String> list) {
		Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
		intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,
				(ArrayList<String>) list);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);
		
	}

}
