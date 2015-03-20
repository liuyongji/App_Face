package com.face.test.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.face.test.R;
import com.face.test.Utils.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MyViewAdapter extends PagerAdapter {
//	public List<Bitmap> bitmaps;
	private Context context;
	private SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式

	public MyViewAdapter( Context context) {
//		this.bitmaps = bitmaps;
		this.context = context;
	}

	@Override
	public int getCount() {
		
		return 2;
	}
	

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		
		final ImageView imageView = new ImageView(context);

//		if (bitmaps.get(position) != null) {
//			imageView.setImageBitmap(bitmaps.get(position));
//		} else {
			imageView.setImageResource(R.drawable.demo);
//		}
		imageView.setTag(position);

		imageView.setOnLongClickListener(new ImageView.OnLongClickListener() {
			SweetAlertDialog sDialog;

			@Override
			public boolean onLongClick(View v) {
				sDialog = new SweetAlertDialog(context,
						SweetAlertDialog.NORMAL_TYPE)
						.setTitleText("保存图片？")
						.setConfirmText("OK")
						.showCancelButton(true)
						.setConfirmClickListener(
								new SweetAlertDialog.OnSweetClickListener() {
									@Override
									public void onClick(SweetAlertDialog sDialog) {
										boolean b=Util.saveBitmap(
												((BitmapDrawable) (imageView
														.getDrawable()))
														.getBitmap(), df
														.format(new Date()));
										if (b) {
											sDialog.setTitleText("保存成功")
													.setContentText(
															context.getResources()
																	.getString(
																			R.string.save_success))
																			.changeAlertType(
														SweetAlertDialog.SUCCESS_TYPE);
										}else {
											sDialog.setTitleText("保存失败")
													.setContentText(
															context.getResources()
																	.getString(
																			R.string.save_fail))
													.changeAlertType(
															SweetAlertDialog.ERROR_TYPE);
										}
										
										sDialog.setConfirmText("OK")
												.setConfirmClickListener(null);
												
									}
								});
				sDialog.show();

				return true;
			}
		});
		container.addView(imageView);
		return imageView;
	}

}
