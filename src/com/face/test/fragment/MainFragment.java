package com.face.test.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;

import com.face.test.R;
import com.face.test.ReportTask;
import com.face.test.Result;
import com.face.test.Utils.Http;
import com.face.test.Utils.Util;
import com.face.test.bean.ClientError;
import com.face.test.bean.Person;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.NiftyDialogBuilder;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class MainFragment extends Fragment implements OnTabChangeListener,
		OnClickListener {
	private PopupWindow mpopupWindow;
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
	public static final int NOT_FOUND = 4;

	private Timer timer;

	private Person person;

	private BmobFile bmobFile;

	private HttpRequests request = null;// 在线api

	private SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式

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
					Bitmap bitmap = Util.watermarkBitmap(curBitmap[n],
							(List<String>) msg.obj);
					if (n == 0) {
						imageView1.setImageBitmap(bitmap);
					} else if (n == 1) {
						imageView2.setImageBitmap(bitmap);
					}
					File f = Util.saveBitmap(bitmap, df.format(new Date()));
					bmobFile = new BmobFile(f);
					bmobFile.upload(getActivity(), uploadFileListener);
					if (progressBar != null) {
						progressBar.dismiss();
					}

					Toast.makeText(getActivity(),
							getResources().getString(R.string.save),
							Toast.LENGTH_LONG).show();
					// NiftyDialogBuilder niftyDialogBuilder =
					// NiftyDialogBuilder
					// .getInstance(getActivity());
					// niftyDialogBuilder.getWindow().setGravity(Gravity.BOTTOM);
					// niftyDialogBuilder.withTitle("检测结果：")
					// .withMessage((String) msg.obj).show();
					break;
				case DECTOR_FAIL:
					if (progressBar != null) {
						progressBar.dismiss();
					}
					timer.cancel();
					Thread.currentThread().interrupt();
					Toast.makeText(getActivity(),
							getResources().getString(R.string.no_net_state),
							Toast.LENGTH_LONG).show();
					break;
				case COMPARE_FAIL:
					if (progressBar != null) {
						progressBar.dismiss();
					}
					Toast.makeText(getActivity(),
							getResources().getString(R.string.choose_two),
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
		curBitmap[n] = Util.getScaledBitmap(str, 700);
		if (n == 0) {
			imageView1.setImageBitmap(curBitmap[n]);
		} else if (n == 1) {
			imageView2.setImageBitmap(curBitmap[n]);
		}

		progressBar = Util.getProgressDialog(getActivity());

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
		imageView1.setOnLongClickListener(new ImageView.OnLongClickListener() {
			NiftyDialogBuilder niftyDialogBuilder;

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				niftyDialogBuilder = NiftyDialogBuilder
						.getInstance(getActivity());
				niftyDialogBuilder.getWindow().setGravity(Gravity.CENTER);
				niftyDialogBuilder.withTitle("提示").withButton1Text("保存")
						// def gone
						.withButton2Text("取消")
						.setButton1Click(new View.OnClickListener() {

							@Override
							public void onClick(View v) {

								Util.saveBitmap(((BitmapDrawable) (imageView1
										.getDrawable())).getBitmap(), df
										.format(new Date()));
								Toast.makeText(
										getActivity(),
										getResources().getString(
												R.string.save_success),
										Toast.LENGTH_LONG).show();
								;
								niftyDialogBuilder.dismiss();
							}
						}).setButton2Click(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								niftyDialogBuilder.dismiss();
							}
						}).show();

				return true;
			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.pick:
//			startActivityForResult(new Intent("android.intent.action.PICK",
//					MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1001);
			break;
		case R.id.detect:
			progressBar = Util.getProgressDialog(getActivity());
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
			byte[] bytes = Util.getBitmapByte(getActivity(), curBitmap[n]);
			JSONObject jsonObject = null;
			List<String> list = null;

			try {
				jsonObject = request.detectionDetect(new PostParameters()
						.setImg(bytes).setMode("oneface"));
				list = Util.Jsonn(jsonObject);
				if (list == null) {
					// resultsingle = "没检测到人脸";
					list = new ArrayList<String>();
					list.add("没检测到人脸");
				} else {
					face[n] = Util.face.getFaceId();
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

	@Override
	public void onTabChanged(String arg0) {
		n = tabHost.getCurrentTab();
	}
	
	private void showPopMenu() {
		View view = View.inflate(getActivity(), R.layout.share_popup_menu, null);
		RelativeLayout rl_camera = (RelativeLayout) view.findViewById(R.id.rl_camera);
		RelativeLayout rl_tuku = (RelativeLayout) view.findViewById(R.id.rl_tuku);
		Button bt_cancle = (Button) view.findViewById(R.id.bt_cancle);

		rl_camera.setOnClickListener(this);
		rl_tuku.setOnClickListener(this);
		bt_cancle.setOnClickListener(this);
	
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mpopupWindow.dismiss();
			}
		});
		
		view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in));
		LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_bottom_in));
		
		if(mpopupWindow==null){
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
			// Util.deletefile();
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
