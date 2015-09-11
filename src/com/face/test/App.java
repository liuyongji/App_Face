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
import com.face.test.activity.ResultActivity;
import com.facepp.http.HttpRequests;
import com.myface.JMSManager;
import com.myface.n.i;
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
import android.content.Context;
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
	
	public static void OpenCamera(Activity context,ImageView imageView){

		if (CameraHelper.showAlertIfNotSupportCamera(context))
			return;
		// 组件选项配置
		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/camera/TuCameraOption.html
		TuCameraOption option = new TuCameraOption();

		// 控制器类型
		// option.setComponentClazz(TuCameraFragment.class);

		// 设置根视图布局资源ID
		// option.setRootViewLayoutId(TuCameraFragment.getLayoutId());

		// 保存到临时文件 (默认不保存, 当设置为true时, TuSdkResult.imageFile, 处理完成后将自动清理原始图片)
		// option.setSaveToTemp(false);

		// 保存到系统相册 (默认不保存, 当设置为true时, TuSdkResult.sqlInfo, 处理完成后将自动清理原始图片)
		option.setSaveToAlbum(true);

		// 保存到系统相册的相册名称
		// option.setSaveToAlbumName("TuSdk");

		// 照片输出压缩率 (默认:90，0-100 如果设置为0 将保存为PNG格式)
		// option.setOutputCompress(90);

		// 相机方向 (默认:CameraInfo.CAMERA_FACING_BACK){@link
		// android.hardware.Camera.CameraInfo}
		// option.setAvPostion(CameraInfo.CAMERA_FACING_BACK);

		// 照片输出图片长宽 (默认：全屏)
		// option.setOutputSize(new TuSdkSize(1440, 1920));

		// 闪关灯模式
		// option.setDefaultFlashMode(Camera.Parameters.FLASH_MODE_OFF);

		// 是否开启滤镜支持 (默认: 关闭)
		option.setEnableFilters(true);

		// 默认是否显示滤镜视图 (默认: 不显示, 如果mEnableFilters = false, mShowFilterDefault将失效)
		option.setShowFilterDefault(true);

		// 滤镜组行视图宽度 (单位:DP)
		// option.setGroupFilterCellWidthDP(75);

		// 滤镜组选择栏高度 (单位:DP)
		// option.setFilterBarHeightDP(100);

		// 滤镜分组列表行视图布局资源ID (默认:
		// tusdk_impl_component_widget_group_filter_group_view，如需自定义请继承自
		// GroupFilterGroupView)
		// option.setGroupTableCellLayoutId(GroupFilterGroupView.getLayoutId());

		// 滤镜列表行视图布局资源ID (默认:
		// tusdk_impl_component_widget_group_filter_item_view，如需自定义请继承自
		// GroupFilterItemView)
		// option.setFilterTableCellLayoutId(GroupFilterItemView.getLayoutId());

		// 开启滤镜配置选项
		option.setEnableFilterConfig(true);

		// 需要显示的滤镜名称列表 (如果为空将显示所有自定义滤镜)
		// 滤镜名称参考 TuSDK.bundle/others/lsq_tusdk_configs.json
		// filterGroups[]->filters[]->name lsq_filter_%{Brilliant}
		// String[] filters = { "SkinNature", "SkinPink", "SkinJelly",
		// "SkinNoir",
		// "SkinRuddy", "SkinPowder", "SkinSugar" };
		// option.setFilterGroup(Arrays.asList(filters));

		// 是否保存最后一次使用的滤镜
		option.setSaveLastFilter(true);

		// 自动选择分组滤镜指定的默认滤镜
		option.setAutoSelectGroupDefaultFilter(true);

		// 开启用户滤镜历史记录
		option.setEnableFiltersHistory(true);

		// 显示滤镜标题视图
		option.setDisplayFiltersSubtitles(true);

		// 触摸聚焦视图ID (默认: tusdk_impl_component_camera_focus_touch_view)
		// option.setFocusTouchViewId(TuFocusTouchView.getLayoutId());

		// 视频视图显示比例 (默认: 0, 全屏)
		// option.setCameraViewRatio(0);

		// 是否直接输出图片数据 (默认:false，输出已经处理好的图片Bitmap)
		// option.setOutputImageData(false);

		// 开启系统拍照声音 (默认:true)
		// option.setEnableCaptureSound(true);

		// 自定义拍照声音RAW ID，默认关闭系统发声
		// option.setCaptureSoundRawId(R.raw.lsq_camera_focus_beep);

		// 自动释放相机在拍摄后 (节省手机内存, 需要手动再次启动)
		// option.setAutoReleaseAfterCaptured(false);

		// 开启长按拍摄 (默认：false)
		option.setEnableLongTouchCapture(true);

		// 开启聚焦声音 (默认:true)
		// option.setEnableFocusBeep(true);

		// 是否需要统一配置参数 (默认false, 取消三星默认降噪，锐化)
		// option.setUnifiedParameters(false);

		// 预览视图实时缩放比例 (默认:0.7f, 实时预览时，缩小到全屏大小比例，提升预览效率， 0 < mPreviewEffectScale
		// <= 1)
		// option.setPreviewEffectScale(0.7f);

		// 视频覆盖区域颜色 (默认：0xFF000000)
		// option.setRegionViewColor(0xFF000000);

		// 禁用前置摄像头自动水平镜像 (默认: false，前置摄像头拍摄结果自动进行水平镜像)
		// option.setDisableMirrorFrontFacing(true);

		TuCameraFragment fragment = option.fragment();
		
		if (imageView!=null) {
			fragment.setDelegate(new TuSdkDelegate(context,imageView));
		}else {
			fragment.setDelegate(new TuSdkDelegate(context));
		}
		
		

		// see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/base/TuSdkHelperComponent.html
		TuSdkHelperComponent componentHelper = new TuSdkHelperComponent(context);
		// 开启相机
		componentHelper.presentModalNavigationActivity(fragment, true);
	
		
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
