package com.face.test.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;
import com.face.test.R;
import com.face.test.ReportTask;
import com.face.test.Result;
import com.face.test.bean.ClientError;
import com.face.test.bean.Person;
import com.face.test.tools.Http;
import com.face.test.tools.Tool;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.NiftyDialogBuilder;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class MainFragment extends Fragment implements OnTabChangeListener,
		OnClickListener {
	public static Bitmap curBitmap[] = new Bitmap[2];
	private final static String TAG = "facetest";
	private ImageView imageView1 = null;
	private ImageView imageView2 = null;
	private Button seletcButton, duibiButton;
	// private HandlerThread detectThread = null;
	private Handler detectHandler = null;
	private String face[] = new String[2];
	public int n = 0;
	private ProgressDialog progressBar;

	private TabHost tabHost;
	public static final int DECTOR_SUCCESS = 0;
	public static final int COMPARE_SUCCESS = 1;
	public static final int COMPARE_FAIL = 2;
	public static final int DECTOR_FAIL = 3;

	private Timer timer;

	private Person person;

	private BmobFile bmobFile;

	private HttpRequests request = null;// 在线api

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		View view = inflater.inflate(R.layout.main3, container, false);
		request = new HttpRequests("99a9423512d4f19c17bd8d6b526e554c",
				"z8stpP3-HMdYhg6kAK73A2nBFwZg4Thl");
		initview(view);

		detectHandler = new Handler() {
			@SuppressWarnings("unchecked")
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case DECTOR_SUCCESS:
					if (progressBar != null) {
						progressBar.dismiss();
					}
					NiftyDialogBuilder niftyDialogBuilder = NiftyDialogBuilder
							.getInstance(getActivity());
					niftyDialogBuilder.getWindow().setGravity(Gravity.BOTTOM);
					niftyDialogBuilder.withTitle("检测结果：")
							.withMessage((String) msg.obj).show();
					break;
				case DECTOR_FAIL:
					if (progressBar != null) {
						progressBar.dismiss();
					}
					timer.cancel();
					Thread.currentThread().interrupt();
					Toast.makeText(getActivity(), "网络不给力哦", Toast.LENGTH_LONG)
							.show();
					break;
				case COMPARE_FAIL:
					if (progressBar != null) {
						progressBar.dismiss();
					}
					Toast.makeText(getActivity(), "请选择两张清晰的人脸图片",
							Toast.LENGTH_LONG).show();
					break;
				case COMPARE_SUCCESS:
					if (progressBar != null) {
						progressBar.dismiss();
					}
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根

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
                getData(str);

			}
			break;
		}
		}

	}

	private void getData(String str) {
		if ((curBitmap[n] != null) && (!curBitmap[n].isRecycled()))
			curBitmap[n].recycle();
		curBitmap[n] = Tool.getScaledBitmap(str, 700);
		if (n == 0) {
			imageView1.setImageBitmap(curBitmap[n]);
		} else if (n == 1) {
			imageView2.setImageBitmap(curBitmap[n]);
		}
		File f = Tool.saveBitmap(curBitmap[n]);
		bmobFile = new BmobFile(f);
		bmobFile.upload(getActivity(), uploadFileListener);
		progressBar = Tool.getProgressDialog(getActivity());

		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				Message message = new Message();
				message.what = DECTOR_FAIL;
				detectHandler.sendMessage(message);
				this.cancel();
			}
		}, 25000);
		new Thread(dector).start();

	}

	private void initview(View view) {
		seletcButton = (Button) view.findViewById(R.id.pick);
		duibiButton = (Button) view.findViewById(R.id.detect);
		seletcButton.setOnClickListener(this);
		duibiButton.setOnClickListener(this);
		imageView1 = (ImageView) view.findViewById(R.id.imageview1);
		imageView2 = (ImageView) view.findViewById(R.id.imageview2);
		tabHost = (TabHost) view.findViewById(android.R.id.tabhost);
		tabHost.setup();
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("照片1")
				.setContent(R.id.imageview1));
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("照片2")
				.setContent(R.id.imageview2));
		tabHost.setOnTabChangedListener(this);
		n = tabHost.getCurrentTab();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.pick:
			startActivityForResult(new Intent("android.intent.action.PICK",
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1001);
			break;
		case R.id.detect:
			progressBar = Tool.getProgressDialog(getActivity());
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
				resultcompare = Tool.CompareResult(jsonObject);
				similarityresult = Tool.Similarity(jsonObject);
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
			byte[] bytes = Tool.getBitmapByte(getActivity(), curBitmap[n]);
			JSONObject jsonObject = null;
			String resultsingle = null;

			try {
				jsonObject = request.detectionDetect(new PostParameters()
						.setImg(bytes).setMode("oneface"));
				resultsingle = Tool.Jsonn(jsonObject);
				if (resultsingle == null || resultsingle == "") {
					resultsingle = "没检测到人脸";
				} else {
					face[n] = Tool.face.getFaceId();
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
			message.obj = resultsingle;
			detectHandler.sendMessage(message);
		}
	};

	@Override
	public void onTabChanged(String arg0) {
		// TODO 自动生成的方法存根

	}

	private UploadFileListener uploadFileListener = new UploadFileListener() {

		@Override
		public void onSuccess() {
			// TODO 自动生成的方法存根
			person = new Person();
			person.setUser("user");
			person.setFile(bmobFile);
			person.save(getActivity());
			Tool.deletefile();
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

}
