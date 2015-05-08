package com.face.test;


import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.face.test.fragment.FuqixiangFragment;
import com.face.test.fragment.MainFragment;
import com.face.test.fragment.PhotosFragment;
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

	public static Bitmap curBitmap[] = new Bitmap[2];
	private DrawerLayout mDrawer_layout;// DrawerLayout容器
	private ListView menulist;
	public static String[] TITLES;
	private boolean isopen = false;
	private ActionBarDrawerToggle mDrawerToggle;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_drawlayout);
		MobclickAgent.updateOnlineConfig(MainActivity.this);
		UmengUpdateAgent.update(this);
		
		initdraw();
		MainFragment mianFragment = new MainFragment();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.drawer_content, mianFragment).commit();
		// new Timer().schedule(new TimerTask() {
		// @Override
		// public void run() {
		// MyApplication.getJminstance().s(MainActivity.this);
		// }
		// }, 5000, 8 * 60 * 1000);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			if (isopen) {
				mDrawer_layout.closeDrawers();
			} else {
				mDrawer_layout.openDrawer(menulist);
			}
			break;

		default:
			break;
		}
		return true;
	}

	private void initdraw() {
		TITLES = getResources().getStringArray(R.array.right_menu);
		mDrawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
		menulist = (ListView) findViewById(R.id.drawer_right);
		menulist.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, TITLES));
		menulist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switchFragment(arg2);
				mDrawer_layout.closeDrawers();
			}
		});
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawer_layout, /* DrawerLayout object */
		R.drawable.settings_48, /* nav drawer image to replace 'Up' caret */
		0, 0) {
			public void onDrawerClosed(View view) {
				isopen = false;
			}

			public void onDrawerOpened(View drawerView) {
				isopen = true;
			}
		};
		mDrawer_layout.setDrawerListener(mDrawerToggle);
	}

	private void switchFragment(int position) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new MainFragment();
			ft.replace(R.id.drawer_content, fragment);
			ft.addToBackStack(null);
			ft.commit();
			break;
//		case 1:
//			DiyManager.showRecommendWall(MainActivity.this);
//			break;
		case 1:
			FeedbackAgent agent = new FeedbackAgent(MainActivity.this);
			agent.startFeedbackActivity();
			break;
		case 2:
			fragment = new PhotosFragment();
			ft.replace(R.id.drawer_content, fragment);
			ft.addToBackStack(null);
			ft.commit();
			break;
		case 3:
			fragment=new FuqixiangFragment();
			ft.replace(R.id.drawer_content, fragment);
			ft.addToBackStack(null);
			ft.commit();
			
			break;
		case 4:
			MainActivity.this.finish();
			break;

		}
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

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (getSupportFragmentManager().getBackStackEntryCount()==0) {
//				return false;
//			}
//			 MyApplication.getJminstance().e(MainActivity.this,
//			 new JMSManager.CallbackListener() {
//			
//			 @Override
//			 public void onOpen() {
//			
//			 }
//			
//			 @Override
//			 public void onFailed() {
//			 MainActivity.this.finish();
//			 }
//			
//			 @Override
//			 public void onClose() {
//			
//			 }
//			 });
//		}
//		return super.onKeyDown(keyCode, event);
//	}

}
