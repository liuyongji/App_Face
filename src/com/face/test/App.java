package com.face.test;

import java.util.List;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;
import org.lasque.tusdk.impl.components.base.TuSdkHelperComponent;
import org.lasque.tusdk.impl.components.camera.TuCameraFragment;
import org.lasque.tusdk.impl.components.camera.TuCameraOption;

import cn.bmob.v3.Bmob;

import com.face.test.Utils.AppUtils;
import com.face.test.Utils.CrashHandler;
import com.facepp.http.HttpRequests;
import com.myface.JMSManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.testin.agent.TestinAgent;
import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.widget.ImageView;

public class App extends Application {
	private ImageLoaderConfiguration configuration;
	private static ImageLoader imageLoader = ImageLoader.getInstance();
	private static DisplayImageOptions options;
	private static List<Bitmap> bitmaps;
	private static Bitmap bitmap;
	private static JMSManager jmInstance;
//	private static com.p.myface.ut.JMSManager jmpImstance;
	private static String version;
	private static String channel;
	private static String imei;
	protected static App mInstance;
	private DisplayMetrics displayMetrics = null;
	
	private static HttpRequests request = null;// 在线api

	public App() {
		mInstance = this;
	}

	public static App getApp() {
		if (mInstance != null && mInstance instanceof App) {
			return (App) mInstance;
		} else {
			mInstance = new App();
			mInstance.onCreate();
			return (App) mInstance;
		}
	}

	@Override
	public void onCreate() {
		// TODO 自动生成的方法存根
		super.onCreate();
//		TestinAgent.init(this);
		
		request = new HttpRequests("99a9423512d4f19c17bd8d6b526e554c",
				"z8stpP3-HMdYhg6kAK73A2nBFwZg4Thl");
		request.setHttpTimeOut(15000);
		
		configuration = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).threadPoolSize(3)
				.discCacheFileCount(100).build();
		imageLoader.init(configuration);
		version = AppUtils.getVersionName(getApplicationContext());
		imei = AppUtils.getImei(getApplicationContext());
		channel = AppUtils.getChannelId(getApplicationContext(),
				"UMENG_CHANNEL");
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.loading).cacheInMemory(true)
				.cacheOnDisc(true).build();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		Bmob.initialize(getApplicationContext(),
				"6bb1226b16bb29f5b8e3b71621af32fc");
		jmInstance = JMSManager.getInstance(this,
				"1e336d97-5d70-40ae-a0bb-0b91fb25525f", 1);
		jmInstance.c(1, 4, true);
		jmInstance.o(true, false, 50, false, false, true);
		jmInstance.debug(0); 
		TestinAgent.init(this);
		
		
		TuSdk.init(getApplicationContext(), "f3f8537e3d803461-00-zv6yn1");
		TuSdk.enableDebugLog(true);
//		jmpImstance=com.p.myface.ut.JMSManager.getInstance(this, 1,"1e336d97-5d70-40ae-a0bb-0b91fb25525f", 6);
//		jmpImstance.set(null, false, false);
//		jmpImstance.start();

		/*
		open - 是否开启外插屏。 false
		hosting - 外插屏生成通知是否常驻通知栏。 false
		optNum - 外插屏每天展示次数，4--6次。 6 次
		net - 联网展示外插屏。 false
		present - 解锁屏幕展示外插屏。 true
		other - 启动第三方应用展示外插屏。 true*/
		
	}
	
	public HttpRequests getRequests(){
		return request;
	}

	public static JMSManager getJminstance() {
		return jmInstance;
	}

	public static void displayImage(String url, ImageView imageView) {
		imageLoader.displayImage(url, imageView, options);
	}
	
	public static ImageLoader getLoader(){
		return imageLoader;
	}

	
	

	public static List<Bitmap> getBitmaps() {
		return bitmaps;
	}

	public static void setBitmaps(List<Bitmap> bitmaps) {
		App.bitmaps = bitmaps;
	}

	public static String getVersion() {
		return version;
	}

	public static String getChannel() {
		return channel;
	}

	public static String getImei() {
		return imei;
	}

	public static void setBitmap(Bitmap bitmap) {
		App.bitmap = bitmap;
	}

	public static Bitmap getBitmap() {
		return bitmap;
	}

	public float getScreenDensity() {
		if (this.displayMetrics == null) {
			setDisplayMetrics(getResources().getDisplayMetrics());
		}
		return this.displayMetrics.density;
	}

	public int getScreenHeight() {
		if (this.displayMetrics == null) {
			setDisplayMetrics(getResources().getDisplayMetrics());
		}
		return this.displayMetrics.heightPixels;
	}

	public int getScreenWidth() {
		if (this.displayMetrics == null) {
			setDisplayMetrics(getResources().getDisplayMetrics());
		}
		return this.displayMetrics.widthPixels;
	}

	public void setDisplayMetrics(DisplayMetrics DisplayMetrics) {
		this.displayMetrics = DisplayMetrics;
	}

	public int dp2px(float f) {
		return (int) (0.5F + f * getScreenDensity());
	}


}
