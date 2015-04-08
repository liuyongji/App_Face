package com.face.test.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.face.test.bean.ClientError;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.os.Environment;
import android.text.TextPaint;
import android.util.Base64;
import android.util.Log;
import android.view.View;

public class BitmapUtil {
	private static File root;
	private static File tmpfile;

	
	public static Bitmap getScaledBitmap(String fileName, int dstWidth) {
		BitmapFactory.Options localOptions = new BitmapFactory.Options();
		localOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(fileName, localOptions);
		int originWidth = localOptions.outWidth;
		int originHeight = localOptions.outHeight;

		localOptions.inSampleSize = originWidth > originHeight ? originWidth
				/ dstWidth : originHeight / dstWidth;
		localOptions.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(fileName, localOptions);
	}

	public static byte[] getBitmapByte(Context context, Bitmap bitmap) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			new Http(context).postUrl(new ClientError(), Http.getError(e));
		}
		return out.toByteArray();
	}

	public static String bitmapChangeString(Bitmap bitmap) {
		if (bitmap != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			String str = new String(Base64.encodeToString(baos.toByteArray(),
					Base64.DEFAULT));
			return str;
		}
		return null;
	}

	public static File saveBitmap(Bitmap bitmap) {
		// f.createTempFile("prefix", "suffix");
		try {
			tmpfile = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/test.png");
			FileOutputStream out = new FileOutputStream(tmpfile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			Log.i("facetest", "create success");
		} catch (FileNotFoundException e) {
			Log.i("facetest", "FileNotFoundException");
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("facetest", "create fail");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tmpfile;
	}

	public static boolean saveBitmap(Bitmap bitmap, String name) {
		// f.createTempFile("prefix", "suffix");
		File file = null;
		try {
			root = new File(Environment.getExternalStorageDirectory().getPath()
					+ "/facetest/");
			if (!root.exists()) {
				root.mkdir();
			}
			file = new File(Environment.getExternalStorageDirectory().getPath()
					+ "/facetest/" + name + ".png");
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			Log.i("facetest", "create success");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 加水印 也可以加文字
	 * 
	 * @param src
	 * @param watermark
	 * @param title
	 * @return
	 */
	public static Bitmap watermarkBitmap(Bitmap src, List<String> title) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		// 需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		// Paint paint = new Paint();
		// 加入文字
		if (title != null) {
			String familyName = "宋体";
			Typeface font = Typeface.create(familyName, Typeface.NORMAL);
			TextPaint textPaint = new TextPaint();
			textPaint.setColor(Color.RED);
			textPaint.setTypeface(font);
			textPaint.setTextSize(22);
			// 这里是自动换行的
			// StaticLayout layout = new
			// StaticLayout(title,textPaint,w,Alignment.ALIGN_OPPOSITE,1.0F,0.0F,true);
			// layout.draw(cv);
			// 文字就加左上角算了
			for (int i = 0; i < title.size(); i++) {
				cv.drawText(title.get(i), 0, h - 27 * (title.size() - i),
						textPaint);
			}
		}
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		cv.restore();// 存储
		return newb;
	}

	public static void deletefile() {
		tmpfile.delete();
		Log.i("facetest", "delete success");
	}

	public static Bitmap convertViewToBitmap(View view) {
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}

}
