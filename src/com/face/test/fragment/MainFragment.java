package com.face.test.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;

import com.andexert.library.ViewPagerIndicator;
import com.face.test.Aty_GetFaceEmotion;
import com.face.test.MyApplication;
import com.face.test.R;
import com.face.test.ReportTask;
import com.face.test.Result;
import com.face.test.Utils.BitmapUtil;
import com.face.test.Utils.DialogUtil;
import com.face.test.Utils.Http;
import com.face.test.Utils.Util;
import com.face.test.adapter.MyViewAdapter;
import com.face.test.bean.ClientError;
import com.face.test.bean.Person;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.isnc.facesdk.SuperID;
import com.isnc.facesdk.common.Cache;
import com.isnc.facesdk.common.SDKConfig;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

public class MainFragment extends Fragment implements OnClickListener {
	private PopupWindow mpopupWindow;
	private ViewPager mViewPager;
	private ViewPagerIndicator mIndicator;
	private MyViewAdapter mAdapter;
	private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
	private List<String> mDatas = Arrays.asList("照片1", "照片2");
	// public static Bitmap curBitmap[] = new Bitmap[2];
	private final static String TAG = "facetest";
	private Button seletcButton, duibiButton;
	// private HandlerThread detectThread = null;
	private Handler detectHandler = null;
	private String face[] = new String[2];
	private ProgressDialog progressBar;
	private SharedPreferences sharedPreferences;
//	private SharedPreferences sharedPreferences=getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);

	public static final int DECTOR_SUCCESS = 0;
	public static final int COMPARE_SUCCESS = 1;
	public static final int COMPARE_FAIL = 2;
	public static final int DECTOR_FAIL = 3;

	private Timer timer;

	private Person person;

	private BmobFile bmobFile;

	private String sdcard_temp = Environment.getExternalStorageDirectory()
			+ File.separator + "tmps.jpg";

	private HttpRequests request = null;// 在线api

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View view = inflater.inflate(R.layout.main4, container, false);
		request = new HttpRequests("99a9423512d4f19c17bd8d6b526e554c",
				"z8stpP3-HMdYhg6kAK73A2nBFwZg4Thl");
		sharedPreferences=getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		initview(view);

		detectHandler = new Handler() {
			@SuppressWarnings("unchecked")
			public void handleMessage(Message msg) {
				if (progressBar != null) {
					progressBar.dismiss();
				}

				switch (msg.what) {

				case DECTOR_SUCCESS:
					View view=mViewPager
							.findViewWithTag(mViewPager.getCurrentItem());
//					ImageView imageView = (ImageView) view.findViewById(R.id.mainfragment_imageview);
//					ImageView imageView = (ImageView) mViewPager
//							.findViewWithTag(mViewPager.getCurrentItem());
					TextView textView=(TextView)view.findViewById(R.id.mainfragment_textView);
					textView.setVisibility(View.VISIBLE);
					textView.setText((String)msg.obj);
//					Bitmap bitmap = BitmapUtil.watermarkBitmap(
//							bitmaps.get(mViewPager.getCurrentItem()),
//							(List<String>) msg.obj);
					Bitmap bitmap=BitmapUtil.convertViewToBitmap(view);
//					imageView.setImageBitmap(bitmap);
					File f = BitmapUtil.saveBitmap(bitmap);
					bmobFile = new BmobFile(f);
					bmobFile.upload(getActivity(), uploadFileListener);
					Toast.makeText(getActivity(),
							getResources().getString(R.string.save),
							Toast.LENGTH_LONG).show();
					break;
				case DECTOR_FAIL:
					timer.cancel();
					Thread.currentThread().interrupt();
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
					MyApplication.setBitmaps(bitmaps);
					Intent intent = new Intent(getActivity(), Result.class);
					Bundle bundle = new Bundle();
					bundle.putString("Compare",
							((ArrayList<String>) msg.obj).get(0));
					bundle.putString("Result",
							((ArrayList<String>) msg.obj).get(1));
					intent.putExtras(bundle);
					startActivity(intent);
					getActivity().finish();
					break;
				default:
					break;
				}
			};
		};
		return view;
	}

	private void initview(View view) {
		seletcButton = (Button) view.findViewById(R.id.pick);
		duibiButton = (Button) view.findViewById(R.id.detect);
		seletcButton.setOnClickListener(this);
		duibiButton.setOnClickListener(this);
		mViewPager = (ViewPager) view.findViewById(R.id.pager);
		mIndicator = (ViewPagerIndicator) view.findViewById(R.id.id_indicator);
		mIndicator.setTabItemTitles(mDatas);
		mAdapter = new MyViewAdapter(getActivity());
		mViewPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mViewPager, 0);
		bitmaps.add(null);
		bitmaps.add(null);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Bitmap bitmap;

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
				bitmap = BitmapUtil.getScaledBitmap(str, 700);
				setBitmap(bitmap);
			}
			break;
		}
		case 1002:
			if (resultCode == Activity.RESULT_CANCELED) {
				break;
			}
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
			bitmapOptions.inSampleSize = 8;

			bitmap = BitmapFactory.decodeFile(sdcard_temp, bitmapOptions);
			setBitmap(bitmap);
			//
			break;
		default:
//			if (resultCode==SDKConfig.LOGINSUCCESS) {
//				String superiduid = Cache.getCached(getActivity(), SDKConfig.KEY_APPUID);
//				Editor editor= sharedPreferences.edit();
//				editor.putString("superiduid", superiduid);
//				editor.commit();
//			}
			break;
		}

	}

	private void setBitmap(Bitmap bitmap) {
		bitmaps.set(mViewPager.getCurrentItem(), bitmap);
		View view=mViewPager
				.findViewWithTag(mViewPager.getCurrentItem());
		ImageView imageView = (ImageView) view.findViewById(R.id.mainfragment_imageview);
//		ImageView imageView = (ImageView) mViewPager.findViewWithTag(mViewPager
//				.getCurrentItem());
		imageView.setImageBitmap(bitmap);

		progressBar = DialogUtil.getProgressDialog(getActivity());

		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				Message message = new Message();
				message.what = DECTOR_FAIL;
				detectHandler.sendMessage(message);
				this.cancel();
			}
		}, 15000);
		new Thread(dector).start();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.pick:
			View contentview=mViewPager
			.findViewWithTag(mViewPager.getCurrentItem());
//			ImageView imageView = (ImageView) contentview.findViewById(R.id.mainfragment_imageview);
			TextView textView=(TextView)contentview.findViewById(R.id.mainfragment_textView);
			textView.setVisibility(View.GONE);
			// SuperID.faceLogin(getActivity());
//			String superIDuid=sharedPreferences.getString("superiduid", "error");
//			Log.i("SuperID", superIDuid);
//			SuperID.isUidAuthorized(getActivity(), superIDuid, intSuccessCallback,
//					intFailCallback);
			 showPopMenu();
			// SuperID.GetFaceEmotion(getActivity());
			// startActivity(new Intent(getActivity(),
			// Aty_GetFaceEmotion.class));
			break;
		case R.id.bt_cancle:
			mpopupWindow.dismiss();
			break;
		case R.id.rl_camera:
			mpopupWindow.dismiss();
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			File file = new File(sdcard_temp);
			if (file.exists()) {
				file.delete();
			}
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			startActivityForResult(intent, 1002);
			break;
		case R.id.rl_tuku:
			mpopupWindow.dismiss();
			startActivityForResult(new Intent("android.intent.action.PICK",
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1001);
			break;
		case R.id.detect:
			progressBar = DialogUtil.getProgressDialog(getActivity());
			new Thread(compare).start();
			break;
		}
	}

	Runnable compare = new Runnable() {
		@Override
		public void run() {
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
			} catch (FaceppParseException e1) {
				e1.printStackTrace();
				new ReportTask(getActivity()).execute(Http.getError(e1));
			} catch (JSONException e) {
				e.printStackTrace();
				new ReportTask(getActivity()).execute(Http.getError(e));
			}
			Log.i(TAG, jsonObject.toString());
			if (resultcompare == null || resultcompare == "") {
				resultcompare = "没检测到人脸";
			}
			ArrayList<String> result = new ArrayList<String>();
			result.add(similarityresult);
			result.add(resultcompare);
			Message message = new Message();
			message.what = COMPARE_SUCCESS;
			message.obj = result;
			detectHandler.sendMessage(message);

		}
	};
	Runnable dector = new Runnable() {

		@Override
		public void run() {

			byte[] bytes = BitmapUtil.getBitmapByte(getActivity(),
					bitmaps.get(mViewPager.getCurrentItem()));
			JSONObject jsonObject = null;
//			List<String> list = null;
			String list = null;

			try {
				jsonObject = request.detectionDetect(new PostParameters()
						.setImg(bytes).setMode("oneface"));
				list = Util.Jsonn(jsonObject);
				if (list.equals("没检测到人脸")) {
//					list = new ArrayList<String>();
//					list.add("没检测到人脸");
				} else {
					face[mViewPager.getCurrentItem()] = Util.face.getFaceId();
				}
			} catch (FaceppParseException e) {
				e.printStackTrace();
				new Http(getActivity()).postUrl(new ClientError(),
						Http.getError(e));
			} catch (JSONException e) {
				e.printStackTrace();
				new Http(getActivity()).postUrl(new ClientError(),
						Http.getError(e));
			}
			timer.cancel();
			Message message = new Message();
			message.what = DECTOR_SUCCESS;
			message.obj = list;
			detectHandler.sendMessage(message);
		}
	};

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
			person = new Person();
			person.setUser("user");
			person.setFile(bmobFile);
			person.save(getActivity());
			BitmapUtil.deletefile();
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

	private SuperID.IntSuccessCallback intSuccessCallback = new SuperID.IntSuccessCallback() {
		public void onSuccess(int arg0) {
			switch (arg0) {
			case SDKConfig.ISAUTHORIZED:
				SuperID.GetFaceEmotion(getActivity());
				break;
			case SDKConfig.NOAUTHORIZED:
				SuperID.faceLogin(getActivity());
				break;
			default:
				break;
			}
		};
	};
	private SuperID.IntFailCallback intFailCallback = new SuperID.IntFailCallback() {
		public void onFail(int error) {
			switch (error) {
			case SDKConfig.APPTOKENERROR:
				break;
			case SDKConfig.OTHER_ERROR:
			case SDKConfig.SDKVERSIONEXPIRED:
				break;
			case SDKConfig.APPTOKEN_EXPIRED:
				break;
			default:
				break;
			}
		};
	};

}
