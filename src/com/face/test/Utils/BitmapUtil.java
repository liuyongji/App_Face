package com.face.test.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.face.test.bean.ClientError;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextPaint;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
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
	
	public static Bitmap resizeImage(Bitmap bitmap, int screenWidth) {

	     int width = bitmap.getWidth();

	     int height = bitmap.getHeight();

	     int newWidth = screenWidth;

	     int newHeight = screenWidth*height/width; // 根据屏幕的宽度，计算按比较缩放后的高度

	     

//	     Log.i("lyj","width图片原始宽度：" + String.valueOf(width));
//
//	     Log.i("lyj","height图片原始高度：" + String.valueOf(height));   

	     



	     // calculate the scale

	     float scaleWidth = ((float) newWidth) / width;

	     float scaleHeight = ((float) newHeight) / height;



	     // create a matrix for the manipulation

	     Matrix matrix = new Matrix();

	     // resize the Bitmap

	     matrix.postScale(scaleWidth, scaleHeight);

	     // if you want to rotate the Bitmap

	     // matrix.postRotate(45);


	     // recreate the new Bitmap

	     Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

//	     Log.i("lyj","width图片缩放后宽度：" + resizedBitmap.getWidth());
//
//	     Log.i("lyj","height图片缩放后高度：" + resizedBitmap.getHeight());

	     // make a Drawable from Bitmap to allow to set the Bitmap

	     // to the ImageView, ImageButton or what ever

	     return resizedBitmap;

	} 
	
	//获取屏幕截图
	public static Bitmap myShot(Activity activity) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();
 
        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        Display display = activity.getWindowManager().getDefaultDisplay();
 
        // 获取屏幕宽和高
        int widths = display.getWidth();
        int heights = display.getHeight();
 
        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
 
        // 去掉状态栏
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
                statusBarHeights, widths, heights - statusBarHeights);
 
        // 销毁缓存信息
        view.destroyDrawingCache();
 
        return bmp;
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

	public static File saveBitmap(Bitmap bitmap, String name) {
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

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
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

	public static Bitmap watermarkBitmap(Bitmap src, String title) {
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

			cv.drawText(title, 0, h - 27, textPaint);

		}
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		cv.restore();// 存储
		return newb;
	}
	
	  public static Bitmap watermarkBitmap(Bitmap src, Bitmap watermark,
	            float with,float hheth) {
	        if (src == null) {
	            return null;
	        }
	        int w = src.getWidth();
	        int h = src.getHeight(); 
	        //需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了        
	        Bitmap newb= Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
	        Canvas cv = new Canvas(newb);
	        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src    
	        Paint paint=new Paint();
	        //加入图片
	        if (watermark != null) {
	            int ww = watermark.getWidth();
	            int wh = watermark.getHeight();
	            paint.setAlpha(50);
	            cv.drawBitmap(watermark, with, hheth, paint);// 在src的右下角画入水印            
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
	
	public static Bitmap getImageFromAssetsFile(Context context, String fileName) {   
	    Bitmap image = null;   
	    AssetManager am = context.getResources().getAssets();   
	    try {   
	        InputStream is = am.open(fileName);   
	        image = BitmapFactory.decodeStream(is);   
	        is.close();   
	    } catch (IOException e) {   
	        e.printStackTrace();   
	    }   
	    return image;   
	}
	 public static Bitmap doodle(Bitmap src, Bitmap watermark)  
	    {  
	        // 另外创建一张图片  
	        Bitmap newb = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图  
	        Canvas canvas = new Canvas(newb);  
	        canvas.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入原图片src  
	        canvas.drawBitmap(watermark, (src.getWidth() - watermark.getWidth()) / 2, (src.getHeight() - watermark.getHeight()) / 2, null); // 涂鸦图片画到原图片中间位置  
	        canvas.save(Canvas.ALL_SAVE_FLAG);  
	        canvas.restore();  
	          
	        watermark.recycle();  
	        watermark = null;  
	          
	        return newb;  
	    } 

}
