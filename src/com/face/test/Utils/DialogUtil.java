package com.face.test.Utils;

import java.util.Date;

import com.face.test.R;

import cn.pedant.SweetAlert.SweetAlertDialog;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.BitmapDrawable;

public class DialogUtil {
	private static SweetAlertDialog sDialog;

	public static Dialog dialog(Context context, String message) {
		Dialog dialog = new AlertDialog.Builder(context).setTitle("提示")
				.setMessage(message)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						dialog.cancel();
					}
				}).create();
		return dialog;
	}

	public static ProgressDialog getProgressDialog(Context context) {

		ProgressDialog progressDialog = ProgressDialog.show(context, "正在检测...",
				"非wifi环境可能需要较长的等待时间...", true, false);
		progressDialog.setCancelable(false);
		progressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO 自动生成的方法存根
				dialog.dismiss();
			}
		});
		return progressDialog;
	}
	
	public static SweetAlertDialog getsweetAlertDialog(Context context){
		sDialog = new SweetAlertDialog(context,
				SweetAlertDialog.NORMAL_TYPE)
				.setTitleText("保存图片？")
				.setConfirmText("OK")
				.showCancelButton(true)
				.setConfirmClickListener(
						new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sDialog) {
//								boolean b=BitmapUtil.saveBitmap(
//										((BitmapDrawable) (imageView
//												.getDrawable()))
//												.getBitmap(), df
//												.format(new Date()));
//								if (b) {
//									sDialog.setTitleText("保存成功")
//											.setContentText(
//													context.getResources()
//															.getString(
//																	R.string.save_success))
//																	.changeAlertType(
//												SweetAlertDialog.SUCCESS_TYPE);
//								}else {
//									sDialog.setTitleText("保存失败")
//											.setContentText(
//													context.getResources()
//															.getString(
//																	R.string.save_fail))
//											.changeAlertType(
//													SweetAlertDialog.ERROR_TYPE);
//								}
//								
//								sDialog.setConfirmText("OK")
//										.setConfirmClickListener(null);
//										
							}
						});
		return sDialog;
	}
}
