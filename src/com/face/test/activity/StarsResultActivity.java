package com.face.test.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.face.test.MyApplication;
import com.face.test.R;
import com.face.test.R.id;
import com.face.test.R.layout;
import com.face.test.adapter.StarsAdapter;
import com.face.test.bean.Stars;
import com.face.test.bean.StarsInfos;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.google.gson.Gson;

public class StarsResultActivity extends SherlockFragmentActivity {
	private ListView listView;
	private StarsInfos starsInfos;
	private StarsAdapter starsAdapter;

	private List<Stars> starlist;
	
//	private Bitmap mBitmap;
//	private HttpRequests request = null;// 在线api

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_stars_result);
		
		
		String result = getIntent().getExtras().getString("stars");
		
//		mBitmap=getIntent().getParcelableExtra("bitmap");
		
		listView = (ListView) findViewById(R.id.listView1);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
//				Toast.makeText(StarsResultActivity.this, id, Toast.LENGTH_SHORT)
//						.show();
				Intent intent=new Intent(StarsResultActivity.this,StarsDetailActivity.class);
				intent.putExtra("result_score", starsInfos.getCandidate().get(arg2).getSimilarity());
				intent.putExtra("result_image", starlist.get(arg2).getBmobFile().getFileUrl());
				intent.putExtra("result_name", starlist.get(arg2).getName());
				startActivity(intent);
				


			}
		});
		
		// Toast.makeText(this, result, Toast.LENGTH_LONG).show();

		Gson gson = new Gson();
		starsInfos = gson.fromJson(result, StarsInfos.class);
		List<String> data = new ArrayList<String>();
		for (int i = 0; i < starsInfos.getCandidate().size(); i++) {
			data.add(starsInfos.getCandidate().get(i).getFace_id());
		}
		
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				MyApplication.getJminstance().s(StarsResultActivity.this);
			}
		}, 2000);
		

		BmobQuery<Stars> query = new BmobQuery<Stars>();
		query.addWhereContainedIn("faceId", data);
		query.findObjects(this, new FindListener<Stars>() {

			@Override
			public void onError(int arg0, String arg1) {

				Toast.makeText(StarsResultActivity.this, arg1,
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(List<Stars> stars) {
				StarsResultActivity.this.starlist = stars;
				
				

				for (int i = 0; i < stars.size(); i++) {
					for (int j = 0; j < stars.size(); j++) {
						if (stars
								.get(j)
								.getFaceId()
								.equals(starsInfos.getCandidate().get(i)
										.getFace_id())) {
							Stars tmp = null;
							tmp = stars.get(i);
							stars.set(i, stars.get(j));
							stars.set(j, tmp);
						}
					}

				}

				starsAdapter = new StarsAdapter(StarsResultActivity.this,
						stars, starsInfos);
				listView.setAdapter(starsAdapter);
			}
		});

	}

}
