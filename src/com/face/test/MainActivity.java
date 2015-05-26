package com.face.test;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.face.test.fragment.FuqixiangFragment;
import com.face.test.fragment.MainFragment;
import com.face.test.fragment.PhotosFragment;
import com.face.test.fragment.StarsFragment;
import com.myface.JMSManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends SherlockFragmentActivity {
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

		default:
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
