package com.face.test.fragment;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.lasque.tusdk.core.TuSdkResult;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;

import com.andexert.library.ViewPagerIndicator;
import com.face.test.Const;
import com.face.test.App;
import com.face.test.R;
import com.face.test.Utils.BitmapUtil;
import com.face.test.Utils.DialogUtil;
import com.face.test.Utils.Util;
import com.face.test.activity.ResultActivity;
import com.face.test.adapter.MyViewAdapter;
import com.face.test.bean.FaceInfos;
import com.face.test.bean.Person2;
import com.face.test.manager.CameraManager;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainFragment extends Fragment implements OnClickListener, Const {
	private PopupWindow mpopupWindow;
	private ViewPager mViewPager;
	// private ViewPagerIndicator mIndicator;
	private MyViewAdapter mAdapter;
//	private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
	private List<String> mDatas = Arrays.asList("照片1", "照片2");
	private final static String TAG = "lyj";
	private Button seletcButton, duibiButton;
	private Myhandler detectHandler = null;
	private String face[] = new String[2];
	private ProgressDialog progressBar;
	private FaceInfos faceInfos;
	
//	private List<FaceInfos> faceInfos2=new ArrayList<FaceInfos>();

	private DectorThread dectorThread;
	private CompareThread compareThread;

	private int resultcode = -1;

	private Person2 person;

	private BmobFile bmobFile;
	
//	private MyReceive myReceive;

	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式

	private HttpRequests request = null;// 在线api

	// private File f;
	private class Myhandler extends Handler {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {

			resultcode = msg.what;
			if (progressBar != null) {
				progressBar.dismiss();
			}
			switch (msg.what) {

			case DECTOR_SUCCESS:

				View view = mViewPager.findViewWithTag(mViewPager
						.getCurrentItem());
				TextView textView = (TextView) view
						.findViewById(R.id.mainfragment_textView);
				textView.setVisibility(View.VISIBLE);
				textView.setText((String) msg.obj);

				Bitmap bitmap = BitmapUtil.convertViewToBitmap(mViewPager
						.findViewWithTag(mViewPager.getCurrentItem()));
				File f = BitmapUtil.saveBitmap(bitmap, df.format(new Date()));
				bmobFile = new BmobFile(f);
				bmobFile.upload(getActivity(), uploadFileListener);

				break;
			case DECTOR_FAIL:
				
				// timer.cancel();
				Toast.makeText(getActivity(),
						getResources().getString(R.string.no_net_state),
						Toast.LENGTH_LONG).show();
				break;
			case COMPARE_FAIL:
				Toast.makeText(getActivity(),
						getResources().getString(R.string.choose_two),
						Toast.LENGTH_LONG).show();
				break;
			case COMPARE_SUCCESS:
//				App.setBitmaps(bitmaps);
				Intent intent = new Intent(getActivity(), ResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("Compare",
						((ArrayList<String>) msg.obj).get(0));
				bundle.putString("Result", ((ArrayList<String>) msg.obj).get(1));
				// bundle.putSerializable("bitmaps",((ArrayList<Bitmap>)bitmaps));
				// intent.putExtra("bitmaps", (Serializable) bitmaps);
				intent.putExtras(bundle);
				startActivity(intent);
				getActivity().finish();
				break;
			case 1303:
				Toast.makeText(getActivity(),
						getResources().getString(R.string.photostoolarge),
						Toast.LENGTH_LONG).show();
				break;
			case 1301:
				Toast.makeText(getActivity(),
						getResources().getString(R.string.photoserror),
						Toast.LENGTH_LONG).show();
				break;
			case 1202:
				Toast.makeText(getActivity(),
						getResources().getString(R.string.serverbusy),
						Toast.LENGTH_LONG).show();
				break;
			case 1001:
				Toast.makeText(getActivity(),
						getResources().getString(R.string.no_net_state),
						Toast.LENGTH_LONG).show();
				break;
			

			default:
				Toast.makeText(getActivity(),
						getResources().getString(R.string.no_net_state),
						Toast.LENGTH_LONG).show();
				break;
			}
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.main4, container, false);
		request = App.getApp().getRequests();
		initview(view);
		
		EventBus.getDefault().register(this);
		

		detectHandler = new Myhandler();
		return view;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("MainFragment"); //统计页面
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 MobclickAgent.onPageEnd("MainFragment"); 
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case 1001: {
			if (data != null) {

				Uri localUri = data.getData();
				String[] arrayOfString = new String[1];
				arrayOfString[0] = "_data";
				Cursor localCursor = getActivity().getContentResolver().query(
						localUri, arrayOfString, null, null, null);
				if (localCursor == null)
					return;
				localCursor.moveToFirst();
				String str = localCursor.getString(localCursor
						.getColumnIndex(arrayOfString[0]));
				localCursor.close();
				
				setBitmap(str);

				
			}
			break;
		}

		default:
			break;
		}

	}
	public void onEvent(TuSdkResult info){
		String url=info.imageSqlInfo.path;
		Log.i("lyj-MainFragment", url);
		
		setBitmap(url);
		
//		FileUtils.getInst().delete(new File(url));
	}
	

	private void setBitmap(String url) {

		View view = mViewPager.findViewWithTag(mViewPager.getCurrentItem());
		ImageView imageView = (ImageView) view
				.findViewById(R.id.mainfragment_imageview);

		App.displayImage("file:///"+url, imageView);

		progressBar = DialogUtil.getProgressDialog(getActivity());

		dectorThread = new DectorThread(BitmapUtil.getScaledBitmap(url, 500));
		dectorThread.start();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.pick:
			View contentview = mViewPager.findViewWithTag(mViewPager
					.getCurrentItem());
			TextView textView = (TextView) contentview
					.findViewById(R.id.mainfragment_textView);
			textView.setVisibility(View.GONE);
			showPopMenu();
			break;
		case R.id.bt_cancle:
			mpopupWindow.dismiss();
			break;
		case R.id.rl_camera:

			mpopupWindow.dismiss();

			CameraManager.OpenCamera(getActivity());
			break;
		case R.id.rl_tuku:
			mpopupWindow.dismiss();
			startActivityForResult(new Intent("android.intent.action.PICK",
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1001);
			break;
		case R.id.detect:
			progressBar = DialogUtil.getProgressDialog(getActivity());
			compareThread = new CompareThread();
			compareThread.start();
			break;
		}
	}

	private class CompareThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			// byte[] bytes=Tool.getBitmapByte(curBitmap);
			JSONObject jsonObject = null;
			String resultcompare = null;
			String similarityresult = null;

			try {
				for (int i = 0; i < face.length; i++) {
					if (face[i] == null) {
						Message message = new Message();
						message.what = COMPARE_FAIL;
						detectHandler.sendMessage(message);
						return;
					}
				}

				jsonObject = request.recognitionCompare(new PostParameters()
						.setFaceId1(face[0]).setFaceId2(face[1]));
				resultcompare = Util.CompareResult(jsonObject);
				similarityresult = Util.Similarity(jsonObject);
			} catch (FaceppParseException e) {
				e.printStackTrace();
				Message message = new Message();
				if (e.getErrorCode() != null) {
					message.what = e.getErrorCode();
				} else {
					message.what = 3;
				}
				detectHandler.sendMessage(message);
				return;
			} catch (JSONException e) {
				e.printStackTrace();
				// timer.cancel();
				Message message = new Message();
				message.what = -1;
				detectHandler.sendMessage(message);
				return;
			}
			Log.i(TAG, jsonObject.toString());
			if (resultcompare == null || resultcompare == "") {
				resultcompare = "没检测到人脸";
			}
			ArrayList<String> result = new ArrayList<String>();
			result.add(similarityresult + "%");
			result.add(resultcompare);
			Message message = new Message();
			message.what = COMPARE_SUCCESS;
			message.obj = result;
			detectHandler.sendMessage(message);

		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		EventBus.getDefault().unregister(this);  
		super.onDestroy();
	}

	private class DectorThread extends Thread {
		
		
		private Bitmap bitmap;
		
		
		public DectorThread(Bitmap bitmap){
			this.bitmap=bitmap;
		}
		@Override
		public void run() {
			super.run();
			byte[] bytes = BitmapUtil.getBitmapByte(getActivity(),
					bitmap);
			JSONObject jsonObject = null;
			// List<String> list = null;
			String list = null;

			try {

				jsonObject = request.detectionDetect(new PostParameters()
						.setImg(bytes).setMode("oneface")
						.setAttribute("glass,gender,age,race,smiling"));
				Log.i(TAG, jsonObject.toString());
				list = Util.Jsonn(jsonObject);
				Gson gson = new Gson();
				faceInfos = gson.fromJson(jsonObject.toString(),
						FaceInfos.class);
				if (faceInfos.getFace().size() > 0) {
					face[mViewPager.getCurrentItem()] = faceInfos.getFace()
							.get(0).getFace_id();
					// face[mViewPager.getCurrentItem()] =
					// Util.face.getFaceId();
				} 
			} catch (FaceppParseException e) {

				e.printStackTrace();
				// timer.cancel();

				Message message = new Message();
				if (e.getErrorCode() != null) {
					message.what = e.getErrorCode();
				} else {
					message.what = 3;
				}

				detectHandler.sendMessage(message);
				return;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// timer.cancel();
			Message message = new Message();
			message.what = DECTOR_SUCCESS;
			message.obj = list;
			detectHandler.sendMessage(message);

		}

	}

	private void showPopMenu() {
		View view = View
				.inflate(getActivity(), R.layout.share_popup_menu, null);
		RelativeLayout rl_camera = (RelativeLayout) view
				.findViewById(R.id.rl_camera);
		RelativeLayout rl_tuku = (RelativeLayout) view
				.findViewById(R.id.rl_tuku);
		Button bt_cancle = (Button) view.findViewById(R.id.bt_cancle);

		rl_camera.setOnClickListener(this);
		rl_tuku.setOnClickListener(this);
		bt_cancle.setOnClickListener(this);

		view.startAnimation(AnimationUtils.loadAnimation(getActivity(),
				R.anim.fade_in));
		LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(),
				R.anim.push_bottom_in));

		if (mpopupWindow == null) {
			mpopupWindow = new PopupWindow(getActivity());
			mpopupWindow.setWidth(LayoutParams.MATCH_PARENT);
			mpopupWindow.setHeight(LayoutParams.MATCH_PARENT);
			mpopupWindow.setBackgroundDrawable(new BitmapDrawable());

			mpopupWindow.setFocusable(true);
			mpopupWindow.setOutsideTouchable(true);
		}

		mpopupWindow.setContentView(view);
		mpopupWindow.showAtLocation(seletcButton, Gravity.BOTTOM, 0, 0);
		mpopupWindow.update();
	}

	private UploadFileListener uploadFileListener = new UploadFileListener() {

		@Override
		public void onSuccess() {
			// TODO 自动生成的方法存根
			
			person = new Person2();
			person.setUser(App.getImei());
			person.setFile(bmobFile);
			person.setDoubles(false);
			person.setResultcode(resultcode);
			
			if (faceInfos.getFace()!=null) {
				person.setSex(faceInfos.getFace().get(0).getAttribute().getGender()
						.getValue());
			}			
			person.setVerson(App.getVersion());
			person.setChannel(App.getChannel());
			person.setModel(Build.MODEL);
			person.save(getActivity());
			// BitmapUtil.deletefile();
		}

		@Override
		public void onProgress(Integer arg0) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void onFailure(int arg0, String arg1) {
			// TODO 自动生成的方法存根
		}
	};

	private void initview(View view) {
		seletcButton = (Button) view.findViewById(R.id.pick);
		duibiButton = (Button) view.findViewById(R.id.detect);
		seletcButton.setOnClickListener(this);
		duibiButton.setOnClickListener(this);
		mViewPager = (ViewPager) view.findViewById(R.id.pager);
		ViewPagerIndicator mIndicator = (ViewPagerIndicator) view
				.findViewById(R.id.id_indicator);
		mIndicator.setTabItemTitles(mDatas);
		mAdapter = new MyViewAdapter(getActivity());
		mViewPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mViewPager, 0);
//		faceInfos2.add(null);
//		faceInfos2.add(null);
	}
}
