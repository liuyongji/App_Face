package com.face.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.face.test.adapter.StarsAdapter;
import com.face.test.bean.Stars;
import com.face.test.bean.StarsInfos;
import com.google.gson.Gson;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class StarsResultActivity extends SherlockFragmentActivity {
	private ListView listView;
	private StarsInfos starsInfos;
	private StarsAdapter starsAdapter;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_stars_result);
		listView = (ListView) findViewById(R.id.listView1);
		String result = getIntent().getExtras().getString("stars");
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();

		Gson gson = new Gson();
		starsInfos = gson.fromJson(result, StarsInfos.class);
		List<String> data=new ArrayList<String>();
		for (int i = 0; i < starsInfos.getCandidate().size(); i++) {
			data.add(starsInfos.getCandidate().get(i).getFace_id());
		}
		BmobQuery<Stars> query = new BmobQuery<Stars>();
//		query.addWhereEqualTo("faceId", names[1]);
		query.addWhereContainedIn("faceId",data);
		query.findObjects(this, new FindListener<Stars>() {

			@Override
			public void onError(int arg0, String arg1) {
				
				Toast.makeText(StarsResultActivity.this, arg1, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(List<Stars> stars) {
				starsAdapter=new StarsAdapter(StarsResultActivity.this, stars,starsInfos);
				listView.setAdapter(starsAdapter);
			}
		});

	}

}
