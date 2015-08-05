package com.face.test.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.face.test.App;
import com.face.test.R;
import com.face.test.Utils.BitmapUtil;
import com.umeng.socialize.controller.UMSocialService;

public class StarsDetailActivity extends Activity implements OnClickListener {
	private Button btnshare, btnback;
	private ImageView ivimage1, ivimage2;
	private TextView tvscore,tvname;
	private String result_score;
	private String result_url;
	private UMSocialService mController;
	private String result_name;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stars_detail);
		result_score=getIntent().getExtras().getString("result_score");
		result_url=getIntent().getExtras().getString("result_image");
		result_name=getIntent().getExtras().getString("result_name");
		initView();
		
	}

	private void initView() {
		btnshare = (Button) findViewById(R.id.stars_share);
		btnback = (Button) findViewById(R.id.stars_back);
		tvscore = (TextView) findViewById(R.id.image_score);
		tvscore.setText("撞脸指数："+result_score.substring(0, 2));
		tvname=(TextView)findViewById(R.id.stars_tv_name);
		tvname.setText(result_name);
		ivimage1 = (ImageView) findViewById(R.id.local_photo);
		ivimage2 = (ImageView) findViewById(R.id.result_photo);
		App.displayImage(result_url, ivimage2);
		ivimage1.setImageBitmap(App.getBitmap());
		btnshare.setOnClickListener(this);
		btnback.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.stars_back:
			finish();

			break;
		case R.id.stars_share:
//			View view=findViewById(R.id.all_layout);
//			Bitmap bitmap=BitmapUtil.convertViewToBitmap(view);
			
			Bitmap bitmap=BitmapUtil.myShot(StarsDetailActivity.this);
			
			App.setShare(StarsDetailActivity.this, mController,"", bitmap);

			break;

		default:
			break;
		}

	}

}
