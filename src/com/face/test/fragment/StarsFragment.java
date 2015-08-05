package com.face.test.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;

import com.face.test.App;
import com.face.test.R;
import com.face.test.Utils.BitmapUtil;
import com.face.test.Utils.DialogUtil;
import com.face.test.activity.StarsResultActivity;
import com.face.test.bean.Bitchs;
import com.face.test.bean.FaceInfos;
import com.face.test.bean.Person;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class StarsFragment extends Fragment implements OnClickListener {
	private PopupWindow mpopupWindow;
	private Button btn_selete;
	private ImageView iv_imageview;
//	private TextView tv_result;
	private ProgressDialog progressBar;
	private Timer timer;
	private Bitmap bitmap;
	private Handler detectHandler = null;
	private HttpRequests request = null;// 在线api
	private FaceInfos faceInfos;
	private BmobFile bmobFile;
	
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

	private String sdcard_temp = Environment.getExternalStorageDirectory()
			+ File.separator + "tmps.jpg";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		request = new HttpRequests("99a9423512d4f19c17bd8d6b526e554c",
				"z8stpP3-HMdYhg6kAK73A2nBFwZg4Thl");
		View view = inflater.inflate(R.layout.main_stars, container, false);
		initView(view);
		
		new Thread(train).start();
		
		detectHandler = new Handler() {
			public void handleMessage(Message msg) {
				
				if (progressBar != null) {
					progressBar.dismiss();
				}
				switch (msg.what) {
				case 0:
					
					File f = BitmapUtil.saveBitmap(
							bitmap, df.format(new Date()));
					bmobFile = new BmobFile(f);
					bmobFile.upload(getActivity(), uploadFileListener);
					
					Intent intent=new Intent(getActivity(),StarsResultActivity.class);
					
					intent.putExtra("stars", (String)msg.obj);
					startActivity(intent);
					break;
				case 1:
					Toast.makeText(getActivity(),
							getResources().getString(R.string.no_net_state),
							Toast.LENGTH_LONG).show();
					break;
				case 2:
					Toast.makeText(getActivity(),
							getResources().getString(R.string.no_noface),
							Toast.LENGTH_LONG).show();
				case 1202:
					Toast.makeText(getActivity(),
							getResources().getString(R.string.serverbusy),
							Toast.LENGTH_LONG).show();

				default:
					break;
				}
				
			};
		};
		return view;
	}

	private void initView(View view) {
		btn_selete = (Button) view.findViewById(R.id.fuqi_pick);
		btn_selete.setOnClickListener(this);
		iv_imageview = (ImageView) view.findViewById(R.id.fuqi_imageview);
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
		mpopupWindow.showAtLocation(btn_selete, Gravity.BOTTOM, 0, 0);
		mpopupWindow.update();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.fuqi_pick:
			showPopMenu();
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

		default:
			break;
		}
	}

	private void setbitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
		App.setBitmap(bitmap);
		iv_imageview.setImageBitmap(bitmap);
		progressBar = DialogUtil.getProgressDialog(getActivity());

		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				Message message = new Message();
				message.what = 1;
				detectHandler.sendMessage(message);
				this.cancel();
			}
		}, 25000);
		new Thread(dector).start();
	}
	
	Runnable train =new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				request.trainSearch(new PostParameters().setFacesetName("Stars1"));
			} catch (FaceppParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	

	Runnable dector = new Runnable() {

		@Override
		public void run() {

			byte[] bytes = BitmapUtil.getBitmapByte(getActivity(), bitmap);
			JSONObject jsonObject = null;
			try {
				jsonObject = request.detectionDetect(new PostParameters()
						.setImg(bytes));
				Log.i("lyj", jsonObject.toString());
				Gson gson = new Gson();
				faceInfos = gson.fromJson(jsonObject.toString(),
						FaceInfos.class);
				if (faceInfos.getFace().size()<=0) {
					timer.cancel();
					Message message = new Message();
					message.what = 2;
					detectHandler.sendMessage(message);
					return;
				}else {
					String face1=faceInfos.getFace().get(0).getFace_id();
					
					jsonObject= request.recognitionSearch(new PostParameters().setKeyFaceId(face1).setFacesetName("Stars1").setCount(6));
					
					Log.i("lyj", jsonObject.toString());
					timer.cancel();
					Message message = new Message();
					message.obj=jsonObject.toString();
					message.what = 0;
					detectHandler.sendMessage(message);
					
				}

			} catch (FaceppParseException e) {
				e.printStackTrace();
				Message message = new Message();
				message.what = e.getErrorCode();
				detectHandler.sendMessage(message);
			} 

		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
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
				setbitmap(bitmap);
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
			setbitmap(bitmap);
			break;
		default:
			break;
		}
	}
	
	private UploadFileListener uploadFileListener = new UploadFileListener() {

		@Override
		public void onSuccess() {
			// TODO 自动生成的方法存根
			Bitchs person = new Bitchs();
			person.setFile(bmobFile);
			person.setVerson(App.getVersion());
			person.save(getActivity());
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
