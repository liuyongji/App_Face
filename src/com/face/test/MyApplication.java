package com.face.test;

import net.youmi.android.AdManager;
import cn.bmob.v3.Bmob;

import com.face.test.Utils.CrashHandler;
import com.myface.JMSManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sensor.controller.impl.UMShakeServiceFactory;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class MyApplication extends Application {
	private ImageLoaderConfiguration configuration;
	private static ImageLoader imageLoader = ImageLoader.getInstance();
	private static DisplayImageOptions options;

	private static JMSManager jmInstance;

	@Override
	public void onCreate() {
		// TODO 自动生成的方法存根
		super.onCreate();
		configuration = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).threadPoolSize(3)
				.discCacheFileCount(100).build();
		imageLoader.init(configuration);
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.loading).cacheInMemory(true)
				.cacheOnDisc(true).build();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		AdManager.getInstance(getApplicationContext()).init("3336b684c26b7540",
				"70229ffe9c877dfe", false);
		Bmob.initialize(getApplicationContext(),
				"6bb1226b16bb29f5b8e3b71621af32fc");
		// jmInstance = JMSManager.getInstance(this,
		// "1e336d97-5d70-40ae-a0bb-0b91fb25525f", 1);
		// jmInstance.c(1, 4, true);
		// jmInstance.o(true, false, 6, false, false, true);
		// jmInstance.debug(0); // 2 打印 log，部分功能Toast提示， 发布产品时请设置为0，默认为1
	}

	public static JMSManager getJminstance() {
		return jmInstance;
	}

	public static void displayImage(String url, ImageView imageView) {
		imageLoader.displayImage(url, imageView, options);
	}

	public static UMSocialService setShare(Activity context,
			UMSocialService mController,String shareContent,Bitmap shareImage) {
		mController = UMServiceFactory.getUMSocialService("com.face.test");
		if (shareContent!=null) {
			mController.setShareContent(shareContent);
		}
		
		mController.setShareMedia(new UMImage(context, shareImage));
		
		// 注册微博一键登录
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// 微信
		UMWXHandler wxHandler = new UMWXHandler(context, "wxd0792b8632aa595b");
		wxHandler.addToSocialSDK();
		UMWXHandler wxCircleHandler = new UMWXHandler(context,
				"wxd0792b8632aa595b");
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(shareContent);
		circleMedia.setTitle(shareContent);
		circleMedia.setShareImage(new UMImage(context,shareImage));
		circleMedia.setTargetUrl(Result.url);
		mController.setShareMedia(circleMedia);

		// 腾讯
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(context, "1101987894",
				"McgaoeK2xHK8T0qm");
		qqSsoHandler.addToSocialSDK();
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context,
				"1101987894", "McgaoeK2xHK8T0qm");
		qZoneSsoHandler.addToSocialSDK();

		mController.getConfig().setSinaCallbackUrl("http://www.sina.com");
		mController.getConfig().removePlatform(SHARE_MEDIA.DOUBAN);
		return mController;

	}

}
