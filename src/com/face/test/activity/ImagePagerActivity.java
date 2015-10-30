package com.face.test.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.face.test.App;
import com.face.test.R;
import com.face.test.R.id;
import com.face.test.R.layout;
import com.face.test.R.menu;
import com.face.test.R.string;
import com.face.test.manager.ShareManager;
import com.loveplusplus.demo.image.HackyViewPager;
import com.loveplusplus.demo.image.ImageDetailFragment;
import com.umeng.socialize.controller.UMSocialService;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ImagePagerActivity extends FragmentActivity {
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index";
	public static final String EXTRA_IMAGE_URLS = "image_urls";

	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;
	private ImagePagerAdapter mAdapter;
	private ArrayList<String> urls;
	private SweetAlertDialog sDialog;

	private UMSocialService mController;
	public static String path = Environment.getExternalStorageDirectory()
			.getPath() + "/facetest/";
	private File[] files;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("ImagePagerActivity", "onCreate");
		setContentView(R.layout.image_detail_pager);
		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		urls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
		files = new File(path).listFiles();

		mPager = (HackyViewPager) findViewById(R.id.pager);
		mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);

		CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
				.getAdapter().getCount());
		indicator.setText(text);
		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				CharSequence text = getString(R.string.viewpager_indicator,
						arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
			}

		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.pagermenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.share:
			Bitmap bm = BitmapFactory.decodeFile(files[pagerPosition].getAbsolutePath());
			ShareManager.getInstance().setShare(ImagePagerActivity.this, mController,
					getResources().getString(R.string.sharecontent)
							+ ResultActivity.url, bm);
			break;
		case R.id.delete:
			// File file = new File(urls.get(pagerPosition));
			// file.delete();
			sDialog = new SweetAlertDialog(ImagePagerActivity.this,
					SweetAlertDialog.NORMAL_TYPE);
			sDialog.setTitleText("你确定要删除？");
			sDialog.setConfirmText("删除!");
			sDialog.showCancelButton(true);
			sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
				@Override
				public void onClick(final SweetAlertDialog sDialog) {
					File file = files[pagerPosition];
					if (file.delete()) {
						urls.remove(pagerPosition);
						sDialog.setTitleText("已删除!")
								.setContentText(
										"Your imaginary file has been deleted!")
								.setConfirmText("OK").showCancelButton(false)
								.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

									@Override
									public void onClick(
											SweetAlertDialog sweetAlertDialog) {
										// TODO Auto-generated method stub
										ImagePagerActivity.this.finish();
									}
									
								})
								.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
						
					} else {
						sDialog.setTitleText("删除失败")
								.setContentText(" deleted failed!")
								.setConfirmText("OK").showCancelButton(false)
								.setConfirmClickListener(null)
								.changeAlertType(SweetAlertDialog.ERROR_TYPE);
					}
//					mAdapter.notifyDataSetChanged();

				}
			});
			sDialog.show();

		default:
			break;
		}
		return true;
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public List<String> fileList;

		public ImagePagerAdapter(FragmentManager fm, List<String> fileList) {
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.size();
		}

		@Override
		public Fragment getItem(int position) {
			String url = fileList.get(position);
			Fragment fragment = ImageDetailFragment.newInstance(url);

			return fragment;
		}

	}
}