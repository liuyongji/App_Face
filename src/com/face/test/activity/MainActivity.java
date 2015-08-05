package com.face.test.activity;

import com.face.test.R;
import com.face.test.fragment.FuqixiangFragment;
import com.face.test.fragment.MainFragment;
import com.face.test.fragment.PhotosFragment;
import com.face.test.fragment.StarsFragment;
import com.face.test.fragment.TaohuaFragment;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity {
	private Fragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_drawlayout);
		MobclickAgent.updateOnlineConfig(MainActivity.this);
		UmengUpdateAgent.update(this);
		int key = getIntent().getExtras().getInt("key", 1);
		switch (key) {
		case 1:
			fragment = new MainFragment();
			break;
		case 2:
			fragment = new FuqixiangFragment();
			break;
		case 3:
			fragment = new StarsFragment();
			break;
		case 4:
			fragment=new PhotosFragment();
			break;
		case 5:
			fragment=new TaohuaFragment();
			break;

		default:
			fragment = new MainFragment();
			break;
		}
		// getSupportFragmentManager().beginTransaction().addToBackStack(null);
		getSupportFragmentManager().beginTransaction()
				.add(R.id.drawer_content, fragment).commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
