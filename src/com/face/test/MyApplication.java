package com.face.test;


import net.youmi.android.AdManager;
import cn.bmob.v3.Bmob;

import com.face.test.tools.CrashHandler;
import com.myface.JMSManager;


import android.app.Application;

public class MyApplication extends Application{
	private static JMSManager jmInstance;
	@Override
	public void onCreate() {
		// TODO 自动生成的方法存根
		super.onCreate();
		 CrashHandler crashHandler = CrashHandler.getInstance();
		 crashHandler.init(getApplicationContext());  
		 AdManager.getInstance(getApplicationContext()).init("3336b684c26b7540", "70229ffe9c877dfe",false);
		 Bmob.initialize(getApplicationContext(), "6bb1226b16bb29f5b8e3b71621af32fc");
		 jmInstance = JMSManager.getInstance(this, "1e336d97-5d70-40ae-a0bb-0b91fb25525f", 1);
	     jmInstance.c(1, 4, true);
	     jmInstance.o(true, false, 6, false, false, true);
	     jmInstance.debug(0); // 2 打印 log，部分功能Toast提示， 发布产品时请设置为0，默认为1
	}
	public static JMSManager getJminstance(){
		 return jmInstance;
	}

}
