package com.face.test.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.dk.animation.SwitchAnimationUtil;
import com.dk.animation.SwitchAnimationUtil.AnimationType;
import com.face.test.App;
import com.face.test.R;
import com.face.test.R.drawable;
import com.face.test.R.id;
import com.face.test.R.layout;
import com.face.test.R.string;
import com.face.test.Utils.BitmapUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.scrshot.UMScrShotController.OnScreenshotListener;
import com.umeng.scrshot.adapter.UMAppAdapter;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
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

public class ResultActivity extends Activity implements OnClickListener {

	private Button reset, share;
	private TextView resultTextView;
	private ImageView result1, result2;
	private List<Bitmap> list;

	private UMSocialService mController;
	private List<SHARE_MEDIA> platforms;
	// 93f25458b2909f967e6ba19d089f14d7 weixin screst
	public static final String url = "http://myfacetest.bmob.cn/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		initview();
		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		bundle.getString("Compare");
		String resultString = bundle.getString("Result");
		list =App.getBitmaps();
		if (list!=null) {
			result1.setImageBitmap(list.get(0));
			result2.setImageBitmap(list.get(1));
		}
		
		resultTextView.setText(resultString);
		new SwitchAnimationUtil().startAnimation(getWindow().getDecorView(),
				AnimationType.ALPHA);
		//		initshare();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				App.getJminstance().s(ResultActivity.this);
			}
		}, 1000);
	}
	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		MobclickAgent.onResume(this);
		platforms = new ArrayList<SHARE_MEDIA>();
		platforms.add(SHARE_MEDIA.SINA);
		platforms.add(SHARE_MEDIA.QZONE);
		platforms.add(SHARE_MEDIA.WEIXIN_CIRCLE);
		// platforms.add(SHARE_MEDIA.TENCENT);

//		umsharecontorl.setShareContent(ShareContent);
//		umsharecontorl.registerShakeListender(ResultActivity.this, umAppAdapter,
//				platforms, onSensorListener);

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


	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.button1:
			Intent intent = new Intent(ResultActivity.this, MainActivity.class);
			intent.putExtra("key", 1);
			startActivity(intent);
			ResultActivity.this.finish();
			break;
		case R.id.button2:
			
			Bitmap bitmap=BitmapUtil.myShot(ResultActivity.this);
			App.setShare(ResultActivity.this, mController,"", bitmap);
			break;
		default:
			break;
		}
	}


	@Override
	protected void onPause() {
		// TODO 自动生成的方法存根
		super.onPause();
		MobclickAgent.onPause(this);
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

}
