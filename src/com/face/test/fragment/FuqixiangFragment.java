package com.face.test.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.lasque.tusdk.core.TuSdkResult;
import org.w3c.dom.Text;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;

import com.common.util.FileUtils;
import com.face.test.App;
import com.face.test.R;
import com.face.test.Utils.BitmapUtil;
import com.face.test.Utils.DialogUtil;
import com.face.test.Utils.Util;
import com.face.test.bean.FaceInfos;
import com.face.test.bean.Person2;
import com.face.test.manager.CameraManager;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
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
import android.widget.TextView;
import android.widget.Toast;

public class FuqixiangFragment extends Fragment implements OnClickListener {
	private PopupWindow mpopupWindow;
	private Button btn_selete;
	private ImageView iv_imageview;
	private ProgressDialog progressBar;
	private Bitmap bitmap;
	private Handler detectHandler = null;
	private HttpRequests request = null;// 在线api
	private FaceInfos faceInfos;
	private BmobFile bmobFile;
	private TextView tv;
	
	
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		request = App.getApp().getRequests();
		
		EventBus.getDefault().register(this);
		
		View view = inflater.inflate(R.layout.main_fuqixiang, container, false);
		initView(view);
		detectHandler = new Handler() {
			public void handleMessage(Message msg) {
				
				if (progressBar != null) {
					progressBar.dismiss();
				}
				
				switch (msg.what) {
				case 7002:
					String result;
					tv.setVisibility(View.VISIBLE);
					if (faceInfos.getFace().get(0).getAttribute().getGender().getValue().equals(faceInfos.getFace().get(1).getAttribute().getGender().getValue())) {
						result=(String)msg.obj+"   "+getResources().getString(R.string.tongxinlian);
					}else {
						result=(String)msg.obj;
						
					}
					tv.setText(result);
//					bitmap=BitmapUtil.watermarkBitmap(bitmap, result);
//					iv_imageview.setImageBitmap(bitmap);
					File f = BitmapUtil.saveBitmap(
							bitmap, df.format(new Date()));
					bmobFile = new BmobFile(f);
					bmobFile.upload(getActivity(), uploadFileListener);
					
					break;
				case 1:
					Toast.makeText(getActivity(),
							getResources().getString(R.string.no_net_state),
							Toast.LENGTH_LONG).show();
					break;
				case 2:
					Toast.makeText(getActivity(),
							getResources().getString(R.string.tupiangeshi),
							Toast.LENGTH_LONG).show();
					break;
				case 1202:
					Toast.makeText(getActivity(),
							getResources().getString(R.string.serverbusy),
							Toast.LENGTH_LONG).show();
					break;
				case 1001:
					
				

				default:
					Toast.makeText(getActivity(),
							getResources().getString(R.string.no_net_state),
							Toast.LENGTH_LONG).show();
					break;
				}
				
			};
		};
		return view;
	}
	public void onEventMainThread(TuSdkResult info){
		String url=info.imageSqlInfo.path;
		Log.i("lyj-FuqixiangFragment", url);
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = 8;
//
		Bitmap bitmap = BitmapFactory.decodeFile(url, bitmapOptions);
		setbitmap(bitmap);
//		FileUtils.getInst().delete(new File(url));
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("FuqiFragment"); //统计页面
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 MobclickAgent.onPageEnd("FuqiFragment");
	}

	private void initView(View view) {
		tv=(TextView)view.findViewById(R.id.fuqifragment_textView);
		tv.setVisibility(View.GONE);
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
		
		tv.setVisibility(View.GONE);
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.fuqi_pick:
			showPopMenu();
//			tv_result.setVisibility(View.GONE);
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

		default:
			break;
		}
	}

	private void setbitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
		iv_imageview.setImageBitmap(bitmap);
		progressBar = DialogUtil.getProgressDialog(getActivity());
		new Thread(dector).start();
	}

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
				if (faceInfos.getFace().size()<2) {
//					timer.cancel();
					Message message = new Message();
					message.what = 2;
					detectHandler.sendMessage(message);
					return;
				}else {
					String face1=faceInfos.getFace().get(0).getFace_id();
					String face2=faceInfos.getFace().get(1).getFace_id();
					
					jsonObject = request.recognitionCompare(new PostParameters()
					.setFaceId1(face1).setFaceId2(face2));
					Log.i("lyj", jsonObject.toString());
					String float1=Util.changeFloat(Float.parseFloat(Util.Similarity(jsonObject)));
//					timer.cancel();
					Message message = new Message();
					message.obj=float1;
					message.what = 7002;
					detectHandler.sendMessage(message);
					
				}

			} catch (FaceppParseException e) {
				e.printStackTrace();
				Message message = new Message();
				if (e.getErrorCode()!=null) {
					message.what=e.getErrorCode();
				}else {
					message.what=1;
				}
				
				detectHandler.sendMessage(message);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		default:
			break;
		}
	}
	
	private UploadFileListener uploadFileListener = new UploadFileListener() {

		@Override
		public void onSuccess() {
			// TODO 自动生成的方法存根
			Person2 person = new Person2();
			person = new Person2();
			person.setUser(App.getImei());
			person.setFile(bmobFile);
			person.setDoubles(true);
			
			person.setSex(faceInfos.getFace().get(0).getAttribute().getGender()
					.getValue());
			person.setVerson(App.getVersion());
			person.setChannel(App.getChannel());
			person.setModel(Build.MODEL);
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
