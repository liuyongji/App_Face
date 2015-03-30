package com.face.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.dk.animation.SwitchAnimationUtil;
import com.dk.animation.SwitchAnimationUtil.AnimationType;
import com.umeng.analytics.MobclickAgent;
import com.umeng.scrshot.UMScrShotController.OnScreenshotListener;
import com.umeng.scrshot.adapter.UMAppAdapter;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sensor.UMSensor.OnSensorListener;
import com.umeng.socialize.sensor.UMSensor.WhitchButton;
import com.umeng.socialize.sensor.controller.UMShakeService;
import com.umeng.socialize.sensor.controller.impl.UMShakeServiceFactory;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Result extends Activity implements OnClickListener {

	private Button reset, share;
	private TextView resultTextView;
	private ImageView result1, result2;
	private List<Bitmap> list;

	private UMSocialService mController;
	private UMAppAdapter umAppAdapter;
	private List<SHARE_MEDIA> platforms;
	private UMShakeService umsharecontorl;
	private CircleShareContent circleMedia;
	private String ShareContent;
	// 93f25458b2909f967e6ba19d089f14d7 weixin screst
	public static final String url = "http://myfacetest.bmob.cn/";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		initview();
		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		String s_result = bundle.getString("Compare");
		String resultString = bundle.getString("Result");
		list = MyApplication.getBitmaps();
		result1.setImageBitmap(list.get(0));
		result2.setImageBitmap(list.get(1));
		resultTextView.setText(resultString);
		new SwitchAnimationUtil().startAnimation(getWindow().getDecorView(),
				AnimationType.ALPHA);
		ShareContent = "我正在使用一个很有趣的人脸检测应用，我的颜值相似度达到" + s_result
				+ "果然是天生丽质呢，你也来测试一下看看吧" + url;
		initshare();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				MyApplication.getJminstance().s(Result.this);
			}
		}, 2000);
	}

	private void initview() {
		// TODO 自动生成的方法存根
		result1 = (ImageView) findViewById(R.id.result1);
		result2 = (ImageView) findViewById(R.id.result2);

		reset = (Button) findViewById(R.id.button1);
		share = (Button) findViewById(R.id.button2);
		resultTextView = (TextView) findViewById(R.id.textView2);
		reset.setOnClickListener(this);
		share.setOnClickListener(this);
	}

	private void initshare() {
		// TODO 自动生成的方法存根
		mController = UMServiceFactory.getUMSocialService("com.face.test");
		mController.setShareContent(ShareContent);
		mController.setShareMedia(new UMImage(Result.this, R.drawable.icon3));
		// 注册微博一键登录
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		mController.getConfig().setSsoHandler(new SinaSsoHandler());

		// 微信
		UMWXHandler wxHandler = new UMWXHandler(Result.this,
				"wxd0792b8632aa595b", "93f25458b2909f967e6ba19d089f14d7");
		wxHandler.addToSocialSDK();

		UMWXHandler wxCircleHandler = new UMWXHandler(Result.this,
				"wxd0792b8632aa595b", "93f25458b2909f967e6ba19d089f14d7");
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		circleMedia = new CircleShareContent();
//		circleMedia.setShareContent(ShareContent);
		circleMedia.setTitle(getResources().getString(R.string.app_name));
		circleMedia.setShareImage(new UMImage(Result.this, R.drawable.icon3));
		circleMedia.setTargetUrl(url);
		mController.setShareMedia(circleMedia);

		// 腾讯
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(Result.this,
				"1101987894", "McgaoeK2xHK8T0qm");
		qqSsoHandler.addToSocialSDK();
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(Result.this,
				"1101987894", "McgaoeK2xHK8T0qm");
		qZoneSsoHandler.addToSocialSDK();

		mController.getConfig().setSinaCallbackUrl("http://www.sina.com");
		mController.getConfig().removePlatform(SHARE_MEDIA.DOUBAN);
		umsharecontorl = UMShakeServiceFactory.getShakeService("com.face.test");
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.button1:
			Intent intent = new Intent(Result.this, MainActivity.class);
			Result.this.finish();
			startActivity(intent);
			break;
		case R.id.button2:
			umAppAdapter = new UMAppAdapter(this);
			umsharecontorl.takeScrShot(Result.this, umAppAdapter,
					onScreenshotListener);
			mController.openShare(Result.this, false);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Dialog isExit = new AlertDialog.Builder(Result.this)
					.setTitle("系统提示")
					.setMessage("你确定退出吗")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO 自动生成的方法存根
									finish();
								}
							}).setNegativeButton("取消", null).create();
			isExit.show();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		MobclickAgent.onResume(this);
		umAppAdapter = new UMAppAdapter(this);
		platforms = new ArrayList<SHARE_MEDIA>();
		platforms.add(SHARE_MEDIA.SINA);
		platforms.add(SHARE_MEDIA.QZONE);
		platforms.add(SHARE_MEDIA.WEIXIN_CIRCLE);
		// platforms.add(SHARE_MEDIA.TENCENT);

		umsharecontorl.setShareContent(ShareContent);
		umsharecontorl.registerShakeListender(Result.this, umAppAdapter,
				platforms, onSensorListener);

	}

	@Override
	protected void onPause() {
		// TODO 自动生成的方法存根
		super.onPause();
		MobclickAgent.onPause(this);
		umsharecontorl.unregisterShakeListener(Result.this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	private OnSensorListener onSensorListener = new OnSensorListener() {

		@Override
		public void onStart() {
			// TODO 自动生成的方法存根
			// Toast.makeText(Result.this, "onstart", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onComplete(SHARE_MEDIA arg0, int arg1, SocializeEntity arg2) {
			// TODO 自动生成的方法存根
			// Toast.makeText(Result.this, "分享成功", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onButtonClick(WhitchButton arg0) {
			// TODO 自动生成的方法存根
		}

		@Override
		public void onActionComplete(SensorEvent arg0) {
			// TODO 自动生成的方法存根
			circleMedia.setShareContent(ShareContent);
			circleMedia.setTitle(getResources().getString(R.string.app_name));
			circleMedia.setTargetUrl(url);
			mController.setShareMedia(circleMedia);
		}
	};
	private OnScreenshotListener onScreenshotListener = new OnScreenshotListener() {

		@Override
		public void onComplete(Bitmap bitmap) {
			// TODO 自动生成的方法存根
			mController.setShareMedia(new UMImage(Result.this, bitmap));
			// 设置点击分享到微信朋友圈的图片
			circleMedia.setShareImage(new UMImage(Result.this, bitmap));

		}
	};

}
