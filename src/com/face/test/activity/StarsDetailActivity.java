package com.face.test.activity;

import com.face.test.MyApplication;
import com.face.test.R;
import com.face.test.R.id;
import com.face.test.R.layout;
import com.face.test.Utils.BitmapUtil;
import com.umeng.socialize.controller.UMSocialService;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StarsDetailActivity extends Activity implements OnClickListener {
	private Button btnshare, btnback;
	private ImageView ivimage1, ivimage2;
	private TextView tvscore;
	private String result_score;
	private String result_url;
	private UMSocialService mController;
	
	private Bitmap mBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stars_detail);
		result_score=getIntent().getExtras().getString("result_score");
		result_url=getIntent().getExtras().getString("result_image");
//		mBitmap=getIntent().getParcelableExtra("bitmap");
		
		initView();
		
	}

	private void initView() {
		btnshare = (Button) findViewById(R.id.stars_share);
		btnback = (Button) findViewById(R.id.stars_back);
		tvscore = (TextView) findViewById(R.id.image_score);
		tvscore.setText("撞脸指数："+result_score.substring(0, 2));
		ivimage1 = (ImageView) findViewById(R.id.local_photo);
		ivimage2 = (ImageView) findViewById(R.id.result_photo);
		MyApplication.displayImage(result_url, ivimage2);
		ivimage1.setImageBitmap(MyApplication.getBitmap());
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
			View view=findViewById(R.id.all_layout);
			Bitmap bitmap=BitmapUtil.convertViewToBitmap(view);
			
			MyApplication.setShare(StarsDetailActivity.this, mController,"", bitmap);

			break;

		default:
			break;
		}

	}

}
