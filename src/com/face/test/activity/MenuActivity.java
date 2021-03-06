package com.face.test.activity;

import java.io.File;

//import us.pinguo.edit.sdk.PGEditActivity;
//import us.pinguo.edit.sdk.base.PGEditSDK;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.gpuimage.extend.FilterManager;
import org.lasque.tusdk.core.gpuimage.extend.FilterManager.FilterManagerDelegate;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.TuEditMultipleComponent;
import org.lasque.tusdk.impl.components.base.TuSdkComponent.TuSdkComponentDelegate;

import com.common.util.FileUtils;
import com.face.test.App;
import com.face.test.R;
import com.face.test.Utils.AppUtils;
import com.face.test.manager.CameraManager;
import com.face.test.manager.LogManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends Activity implements OnClickListener {
	private static final int REQUEST_CODE_PICK_PICTURE = 0x100001;
	// 定义一个变量，来标识是否退出
	private static boolean isExit = false;
	private Button btnstart, btnanswers, btnfeeback, btnranklist, btnhistroy,
			btntaohua, btnedit;
	private FeedbackAgent agent;
	private TextView tvversion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		agent = new FeedbackAgent(MenuActivity.this);
		MobclickAgent.openActivityDurationTrack(false); // 统计
		TuSdk.checkFilterManager(mFilterManagerDelegate);
		initView();

		EventBus.getDefault().register(this);

//		LogManager.click(this);
//		LogManager.total(this);
	}

	private void initView() {
		tvversion = (TextView) findViewById(R.id.tv_app_info);
		tvversion.setText("版本:" + App.getVersion() + "，Developed by @liu_yj");
		btnstart = (Button) findViewById(R.id.main_btnstart);
		btnanswers = (Button) findViewById(R.id.btn_answers);
		btnfeeback = (Button) findViewById(R.id.btn_feeback);
		btnranklist = (Button) findViewById(R.id.btn_ranklist);
		btnhistroy = (Button) findViewById(R.id.btn_histroy);
		btntaohua = (Button) findViewById(R.id.btn_taohua);
		btnedit = (Button) findViewById(R.id.btn_edit);
		btnedit.setOnClickListener(this);
		btntaohua.setOnClickListener(this);
		btnhistroy.setOnClickListener(this);
		btnranklist.setOnClickListener(this);
		btnfeeback.setOnClickListener(this);
		btnanswers.setOnClickListener(this);
		btnstart.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		EventBus.getDefault().unregister(this);
		MobclickAgent.onPause(this);
	}

	public void onEventMainThread(TuSdkResult info) {
		
		String url = info.imageSqlInfo.path;
		Log.i("lyj-MenuActivity", url);
		FileUtils.getInst().Move(
				url,
				Environment.getExternalStorageDirectory().getPath()
						+ "/facetest/");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(MenuActivity.this, MainActivity.class);
		switch (v.getId()) {
		case R.id.main_btnstart:
			intent.putExtra("key", 1);
			break;
		case R.id.btn_answers:
			intent.putExtra("key", 2);
			break;
		case R.id.btn_feeback:
			intent.putExtra("key", 3);
			break;
		case R.id.btn_ranklist:
			intent.putExtra("key", 4);
			break;

		case R.id.btn_taohua:
			MobclickAgent.onEvent(this, "opencamera");
			CameraManager.OpenCamera(this);
			return;

			// intent.putExtra("key", 5);
			// break;
		case R.id.btn_edit:

			if (AppUtils.IsCanUseSdCard()) {
				intent = new Intent(Intent.ACTION_PICK,
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, REQUEST_CODE_PICK_PICTURE);
			} else
				Toast.makeText(this, "该功能需要sd卡支持", Toast.LENGTH_LONG).show();

			return;

		case R.id.btn_histroy:
			agent.startFeedbackActivity();
			return;
		default:
			break;
		}
		startActivity(intent);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_PICK_PICTURE && resultCode == RESULT_OK) {

			Uri selectedImage = data.getData();
			ContentResolver resolver = getContentResolver();
			String[] filePathColumns = new String[] { MediaStore.Images.Media.DATA };
			Cursor c = this.getContentResolver().query(selectedImage,
					filePathColumns, null, null, null);
			c.moveToFirst();
			int columnIndex = c.getColumnIndex(filePathColumns[0]);
			String mPicturePath = c.getString(columnIndex);
			c.close();

			Uri imageUri = Uri.fromFile(new File(mPicturePath));

			try {
				MobclickAgent.onEvent(this, "photoedit");
				Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
						imageUri);
				TuEditMultipleComponent component = TuSdk
						.editMultipleCommponent(this, delegate);
				component.setImage(photo)

				// 在组件执行完成后自动关闭组件
						.setAutoDismissWhenCompleted(true)
						// 开启组件
						.showComponent();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Intent intent = new Intent(MenuActivity.this,
			// CropPhotoActivity.class);
			// intent.setData(imageUri);
			// startActivity(intent);
		}

	}

	TuSdkComponentDelegate delegate = new TuSdkComponentDelegate() {
		@Override
		public void onComponentFinished(TuSdkResult result, Error error,
				TuFragment lastFragment) {

			Log.i("onComponentFinished", result.imageSqlInfo.path);
			// Toast.makeText(MenuActivity.this, result.imageSqlInfo.path,
			// Toast.LENGTH_LONG).show();
//			FileUtils.getInst().Move(
//					result.imageSqlInfo.path,
//					Environment.getExternalStorageDirectory().getPath()
//							+ "/facetest/");

		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			// 利用handler延迟发送更改状态信息
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
		}
	}

	private FilterManagerDelegate mFilterManagerDelegate = new FilterManagerDelegate() {
		@Override
		public void onFilterManagerInited(FilterManager manager) {
			// TuProgressHub.showSuccess(MenuActivity.this,
			// TuSdkContext.getString("lsq_inited"));
		}
	};

}
