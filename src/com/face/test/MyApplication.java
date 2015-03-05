package com.face.test;

import net.youmi.android.AdManager;
import cn.bmob.v3.Bmob;

import com.face.test.Utils.CrashHandler;
import com.myface.JMSManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;
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
//		jmInstance = JMSManager.getInstance(this,
//				"1e336d97-5d70-40ae-a0bb-0b91fb25525f", 1);
//		jmInstance.c(1, 4, true);
//		jmInstance.o(true, false, 6, false, false, true);
//		jmInstance.debug(0); // 2 打印 log，部分功能Toast提示， 发布产品时请设置为0，默认为1
	}

	public static JMSManager getJminstance() {
		return jmInstance;
	}
	
	public static void displayImage(String url,ImageView imageView){
		imageLoader.displayImage(url, imageView, options);
	} 

}
