package com.face.test;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends Activity implements OnClickListener{
	// 定义一个变量，来标识是否退出
	private static boolean isExit = false;
	private Button btnstart, btnanswers, btnfeeback, btnranklist,btnhistroy;
	private FeedbackAgent agent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		agent = new FeedbackAgent(MenuActivity.this);
		initView();
		
		
	}

	private void initView() {
		btnstart = (Button) findViewById(R.id.main_btnstart);
		btnanswers = (Button) findViewById(R.id.btn_answers);
		btnfeeback = (Button) findViewById(R.id.btn_feeback);
		btnranklist = (Button) findViewById(R.id.btn_ranklist);
		btnhistroy=(Button)findViewById(R.id.btn_histroy);
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
			agent.startFeedbackActivity();
			return;
		case R.id.btn_ranklist:
			intent.putExtra("key", 3);
			break;
		case R.id.btn_histroy:
			intent.putExtra("key", 4);
			break;
		default:
			break;
		}
		startActivity(intent);

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
			System.exit(0);
		}
	}

}
