package com.face.test.activity;

import java.io.File;

import us.pinguo.edit.sdk.PGEditActivity;
import us.pinguo.edit.sdk.base.PGEditSDK;
import com.face.test.MyApplication;
import com.face.test.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		agent = new FeedbackAgent(MenuActivity.this);
		initView();

	}

	private void initView() {
		tvversion = (TextView) findViewById(R.id.tv_app_info);
		tvversion.setText("版本:" + MyApplication.getVersion() + "，作者：@liu_yj");
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
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
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
			intent.putExtra("key", 5);
			break;
		case R.id.btn_edit:
			intent = new Intent(Intent.ACTION_PICK,
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, REQUEST_CODE_PICK_PICTURE);
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
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_PICK_PICTURE
				&& resultCode == Activity.RESULT_OK && null != data) {

			Uri selectedImage = data.getData();
			String[] filePathColumns = new String[] { MediaStore.Images.Media.DATA };
			Cursor c = this.getContentResolver().query(selectedImage,
					filePathColumns, null, null, null);
			c.moveToFirst();
			int columnIndex = c.getColumnIndex(filePathColumns[0]);
			String mPicturePath = c.getString(columnIndex);
			c.close();
			String folderPath = Environment.getExternalStorageDirectory()
					+ File.separator + "facetest" + File.separator;
			File file = new File(folderPath);
			if (!file.exists()) {
				file.mkdir();
			}
			String outFilePath = folderPath + System.currentTimeMillis()
					+ ".jpg";
			PGEditSDK.instance().startEdit(this, PGEditActivity.class,
					mPicturePath, outFilePath);
			return;
		}

		if (requestCode == PGEditSDK.PG_EDIT_SDK_REQUEST_CODE
				&& resultCode == Activity.RESULT_OK) {

//			PGEditResult editResult = PGEditSDK.instance().handleEditResult(
//					data);

			Toast.makeText(this, "图片已保存到历史相册", Toast.LENGTH_LONG).show();
		}

		if (requestCode == PGEditSDK.PG_EDIT_SDK_REQUEST_CODE
				&& resultCode == PGEditSDK.PG_EDIT_SDK_RESULT_CODE_CANCEL) {
			Toast.makeText(this, "编辑取消", Toast.LENGTH_SHORT).show();
		}

		
	}

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
//			System.exit(0);
		}
	}

}
