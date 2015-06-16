package com.face.test.Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.telephony.TelephonyManager;

/**
 * 跟App相关的辅助类
 * 
 * 
 * 
 */
public class AppUtils
{

	private AppUtils()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");

	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context)
	{
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本名称信息]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context)
	{
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getChannelId(Context ctx, String channel) {
		String douId = null;
		PackageManager pm = ctx.getPackageManager();
		ApplicationInfo appInfo;
		try {
			appInfo = pm.getApplicationInfo(ctx.getPackageName(),
					PackageManager.GET_META_DATA);
			Bundle metaData = appInfo.metaData;
			if (metaData != null)
				douId = "" + metaData.getString(channel);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return douId;
	}
	
	
	public static String getImei(Context ctx){
		TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);  
		return tm.getDeviceId();
		
	}
	

}

