package com.face.test;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class StarsResultActivity extends SherlockFragmentActivity{
	private ListView listView;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_stars_result);
		String result=getIntent().getExtras().getString("stars");
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		initView();
		
	}
	private void initView(){
		listView=(ListView)findViewById(R.id.listView1);
	}

}
