package com.face.test;

import java.util.List;

import org.lasque.tusdk.core.TuSdk;

import cn.bmob.v3.Bmob;

import com.face.test.Utils.AppUtils;
import com.face.test.Utils.CrashHandler;
import com.face.test.activity.ResultActivity;
import com.facepp.http.HttpRequests;
import com.myface.JMSManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.testin.agent.TestinAgent;
//import com.testin.agent.TestinAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

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

	public static void setShare(final Activity context,
			UMSocialService mController, String shareContent, Bitmap shareImage) {
		mController = UMServiceFactory.getUMSocialService("com.face.test");
		if (shareContent != null) {
			mController.setShareContent(shareContent);
		}

		mController.setShareMedia(new UMImage(context, shareImage));

		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		mController.getConfig().setSsoHandler(new SinaSsoHandler());

		UMWXHandler wxHandler = new UMWXHandler(context, "wxd0792b8632aa595b",
				"93f25458b2909f967e6ba19d089f14d7");
		wxHandler.addToSocialSDK();
		UMWXHandler wxCircleHandler = new UMWXHandler(context,
				"wxd0792b8632aa595b", "93f25458b2909f967e6ba19d089f14d7");
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setTitle(context.getResources().getString(
				R.string.app_name));
		// weixinContent.setShareContent("shareContent");
		weixinContent.setShareImage(new UMImage(context, shareImage));
		weixinContent.setTargetUrl(ResultActivity.url);
		mController.setShareMedia(weixinContent);

		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareImage(new UMImage(context, shareImage));
		circleMedia.setTargetUrl(ResultActivity.url);
		mController.setShareMedia(circleMedia);

		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(context, "1101987894",
				"McgaoeK2xHK8T0qm");
		qqSsoHandler.addToSocialSDK();
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context,
				"1101987894", "McgaoeK2xHK8T0qm");
		qZoneSsoHandler.addToSocialSDK();

		QQShareContent qqShareContent = new QQShareContent();
		// qqShareContent.setShareContent(shareContent);
		qqShareContent.setTitle(context.getResources().getString(
				R.string.app_name));
		qqShareContent.setShareImage(new UMImage(context, shareImage));
		qqShareContent.setTargetUrl(ResultActivity.url);

		mController.setShareMedia(qqShareContent);

		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent(shareContent);
		qzone.setTargetUrl(ResultActivity.url);
		qzone.setTitle(context.getResources().getString(R.string.app_name));
		qzone.setShareImage(new UMImage(context, shareImage));
		mController.setShareMedia(qzone);

		mController.getConfig().setSinaCallbackUrl("http://www.sina.com");
		mController.getConfig().removePlatform(SHARE_MEDIA.DOUBAN);
		mController.openShare(context, false);

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

	public int px2dp(float pxValue) {
		return (int) (pxValue / getScreenDensity() + 0.5f);
	}

	// 获取应用的data/data/....File目录
	public String getFilesDirPath() {
		return getFilesDir().getAbsolutePath();
	}

	// 获取应用的data/data/....Cache目录
	public String getCacheDirPath() {
		return getCacheDir().getAbsolutePath();
	}

}
