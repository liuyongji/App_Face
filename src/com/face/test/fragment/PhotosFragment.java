package com.face.test.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.face.test.ImageAdapter;
import com.face.test.ImagePagerActivity;
import com.face.test.R;
import com.face.test.Utils.Util;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.NiftyDialogBuilder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.Toast;
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
		mGridView.setOnItemLongClickListener(new OnItemLongClickListener() {
			SweetAlertDialog sDialog;

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				sDialog=new SweetAlertDialog(getActivity(),SweetAlertDialog.NORMAL_TYPE);
				sDialog.setTitleText("what are you going to do");
				sDialog.setConfirmText("delete!");
				sDialog.showCancelButton(true);
				sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						 sDialog
			                .setTitleText("Deleted!")
			                .setContentText("Your imaginary file has been deleted!")
			                .setConfirmText("OK")
			                .setConfirmClickListener(null)
			                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//						sDialog.dismissWithAnimation();
					}
				});
				sDialog.setCancelText("share!");
				sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						sDialog.dismissWithAnimation();
					}
				});
				sDialog.show();

				return true;
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
