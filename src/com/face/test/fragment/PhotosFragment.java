package com.face.test.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.face.test.App;
import com.face.test.R;
import com.face.test.activity.ImagePagerActivity;
import com.face.test.activity.ResultActivity;
import com.face.test.adapter.ImageAdapter;
import com.umeng.socialize.controller.UMSocialService;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

public class PhotosFragment extends Fragment {
	public static String path = Environment.getExternalStorageDirectory()
			.getPath() + "/facetest/";
	private List<String> list;
	private GridView mGridView;
	private ImageAdapter imageAdapter;
	private File[] files;

	private UMSocialService mController;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
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
			public boolean onItemLongClick(AdapterView<?> parent,
					final View view, final int position, long id) {

				sDialog = new SweetAlertDialog(getActivity(),
						SweetAlertDialog.NORMAL_TYPE);
				sDialog.setTitleText("what are you going to do");
				sDialog.setConfirmText(getResources()
						.getString(R.string.delete));
				sDialog.showCancelButton(true);
				sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						File file = files[position];
						if (file.delete()) {
							list.remove(position);
							sDialog.setTitleText("已删除!")
									.setContentText(
											"Your imaginary file has been deleted!")
									.setConfirmText("OK")
									.showCancelButton(false)
									.setConfirmClickListener(null)
									.changeAlertType(
											SweetAlertDialog.SUCCESS_TYPE);
						} else {
							sDialog.setTitleText("删除失败")
									.setContentText(" deleted failed!")
									.setConfirmText("OK")
									.showCancelButton(false)
									.setConfirmClickListener(null)
									.changeAlertType(
											SweetAlertDialog.ERROR_TYPE);
						}
						imageAdapter.notifyDataSetChanged();

					}
				});
				sDialog.setCancelText("分享!");
				sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						// initshare();
						ImageView imageView = (ImageView) view
								.findViewById(R.id.imageView1);
						Bitmap bitmap = ((BitmapDrawable) (imageView
								.getDrawable())).getBitmap();
						App.setShare(
								getActivity(),
								mController,
								getActivity().getResources().getString(
										R.string.sharecontent)
										+ ResultActivity.url, bitmap);
						sDialog.dismissWithAnimation();
					}
				});
				sDialog.show();

				return true;
			}
		});
		return view;
	}
	
	
	private void initData(){
		File filepath=new File(path);
		if (!filepath.exists()) {
			filepath.mkdir();
		}
		list=new ArrayList<String>();
		files = new File(path).listFiles();
		for (int i = 0; i < files.length; i++) {
			list.add("file:///" + files[i].getAbsolutePath());
		}
		imageAdapter = new ImageAdapter(getActivity(), list);
		mGridView.setAdapter(imageAdapter);
	}
	@Override
	public void onResume() {
		super.onResume();
		initData();
	}

	private void imageBrower(int position, List<String> list) {
		Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
		intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,
				(ArrayList<String>) list);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);

	}

}
