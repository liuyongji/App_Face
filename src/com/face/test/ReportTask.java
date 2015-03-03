package com.face.test;

import com.face.test.bean.ClientError;
import com.face.test.tools.Http;

import android.content.Context;
import android.os.AsyncTask;


public class ReportTask extends AsyncTask<String, Void, Boolean> {
	private Context context;
	public ReportTask(Context c){
		this.context=c;
	}
	@Override
	protected Boolean doInBackground(String... params) {
		new Http(context).postUrl(new ClientError(), params[0]);
		return true;
	}
}
