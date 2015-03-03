package com.face.test.tools;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.face.test.bean.ClientError;

import android.content.Context;

public class Http {
	private static final String TAG = "Http";
	private static final int TIMES = 3;	
	private int mTimes = 1;	
	private Context context;
	public Http(Context c){
		this.context=c;
	}
	byte[] getUrlBytes(String urlSpec) {
		HttpURLConnection connection = null;
		byte[] source = null;
		try {
			URL url = new URL(urlSpec);
			connection = (HttpURLConnection) url.openConnection();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			InputStream in = connection.getInputStream();
			
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				int bytesRead = 0;
				byte[] buffer = new byte[1024];
				while ((bytesRead = in.read(buffer)) > 0) {
					out.write(buffer, 0, bytesRead);
				}
				out.close();
				source = out.toByteArray();
			} 
			else
				postUrl(new ClientError(),connection.getResponseCode()+"");
		} catch (Exception e) {
			postUrl(new ClientError(), getError(e));
		} finally {
			if(connection != null) connection.disconnect();
		}
		if(source == null && mTimes <= TIMES) {
			mTimes ++;
			source = getUrlBytes(urlSpec);
		}
		return source;
	}
	
	public static String getError(Exception e) {
		StringBuffer sb = new StringBuffer();
        StackTraceElement[] stackArray = e.getStackTrace();
        for (int i = 0; i < stackArray.length; i++) {
            sb.append(stackArray[i].toString() + "\n");
        }
		return sb.toString();
	}
	
	public void postUrl(ClientError  clientError, String  e) {
		clientError.setmError(e);
		clientError.setmModel(android.os.Build.MODEL);
		clientError.setmSystem(android.os.Build.VERSION.RELEASE);
		clientError.save(context);
	}
	
}
