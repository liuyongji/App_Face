package com.face.test.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.face.test.ImagePagerActivity;
import com.face.test.MyApplication;
import com.face.test.R;
import com.face.test.Result;
import com.face.test.adapter.ImageAdapter;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;

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
	private List<String> list = new ArrayList<String>();
	private GridView mGridView;
	private ImageAdapter imageAdapter;

	private UMSocialService mController;
	private CircleShareContent circleMedia;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		File[] files = new File(path).listFiles();
		for (int i = 0; i < files.length; i++) {
			list.add("file:///" + files[i].getAbsolutePath());
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
			public boolean onItemLongClick(AdapterView<?> parent,
					final View view, final int position, long id) {

				sDialog = new SweetAlertDialog(getActivity(),
						SweetAlertDialog.NORMAL_TYPE);
				sDialog.setTitleText("what are you going to do");
				sDialog.setConfirmText("delete!");
				sDialog.showCancelButton(true);
				sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						File file = new File(list.get(position));
						list.remove(position);
						file.delete();
						imageAdapter.notifyDataSetChanged();
						sDialog.setTitleText("Deleted!")
								.setContentText(
										"Your imaginary file has been deleted!")
								.setConfirmText("OK")
								.setConfirmClickListener(null)
								.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
					}
				});
				sDialog.setCancelText("share!");
				sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						// initshare();
						ImageView imageView = (ImageView) view
								.findViewById(R.id.imageView1);
						Bitmap bitmap = ((BitmapDrawable) (imageView
								.getDrawable())).getBitmap();
						MyApplication.setShare(
								getActivity(),
								mController,
								getActivity().getResources().getString(
										R.string.sharecontent)
										+ Result.url, bitmap);
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

	private void initshare() {
		// TODO 自动生成的方法存根
		mController = UMServiceFactory.getUMSocialService("com.face.test");
		mController.setShareMedia(new UMImage(getActivity(), R.drawable.icon3));
		// 注册微博一键登录
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		mController.getConfig().setSsoHandler(new SinaSsoHandler());

		// 微信
		UMWXHandler wxHandler = new UMWXHandler(getActivity(),
				"wxd0792b8632aa595b");
		wxHandler.addToSocialSDK();

		UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(),
				"wxd0792b8632aa595b");
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		circleMedia = new CircleShareContent();
		circleMedia.setShareImage(new UMImage(getActivity(), R.drawable.icon3));

		mController.setShareMedia(circleMedia);

		// 腾讯
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(),
				"1101987894", "McgaoeK2xHK8T0qm");
		qqSsoHandler.addToSocialSDK();
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(),
				"1101987894", "McgaoeK2xHK8T0qm");
		qZoneSsoHandler.addToSocialSDK();

		mController.getConfig().setSinaCallbackUrl("http://www.sina.com");
		mController.getConfig().removePlatform(SHARE_MEDIA.DOUBAN);
		mController.openShare(getActivity(), false);
	}

	private void imageBrower(int position, List<String> list) {
		Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
		intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,
				(ArrayList<String>) list);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);

	}

}
