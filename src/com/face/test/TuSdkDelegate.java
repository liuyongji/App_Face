package com.face.test;

import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.camera.TuCameraFragment;
import org.lasque.tusdk.impl.components.camera.TuCameraFragment.TuCameraFragmentDelegate;

import com.common.util.FileUtils;

import de.greenrobot.event.EventBus;
import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;

public class TuSdkDelegate implements TuCameraFragmentDelegate{
	public TuSdkDelegate(Context context){
	}
	
	public TuSdkDelegate(Context context,ImageView imageView){
	}
	/**
	 * 获取一个拍摄结果
	 * 
	 * @param fragment
	 *            默认相机视图控制器
	 * @param result
	 *            拍摄结果
	 */
	@Override
	public void onTuCameraFragmentCaptured(TuCameraFragment fragment,
			TuSdkResult result)
	{
//		Toast.makeText(context ,result.imageSqlInfo.path, Toast.LENGTH_LONG).show();
		fragment.hubDismissRightNow();
		fragment.dismissActivityWithAnim();
		
//		File tempFile =new File(result.imageSqlInfo.path);
//		String fileName = tempFile.getName(); 
		
//		FileUtils.getInst().Move(
//				result.imageSqlInfo.path,
//				Environment.getExternalStorageDirectory().getPath()
//						+ "/facetest/");
//		
//		if (imageView!=null) {
//			App.displayImage("file:///"+result.imageSqlInfo.path, imageView);
//		}
		
		EventBus.getDefault().post(result); 
		
		
		
//		Intent i = new Intent("com.android.face");
//		i.putExtra("url", result.imageSqlInfo.path); 
//		context.sendBroadcast(i);
		
	}

	/**
	 * 获取一个拍摄结果 (异步方法)
	 * 
	 * @param fragment
	 *            默认相机视图控制器
	 * @param result
	 *            拍摄结果
	 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
	 */
	@Override
	public boolean onTuCameraFragmentCapturedAsync(TuCameraFragment fragment,
			TuSdkResult result)
	{
		TLog.d("onTuCameraFragmentCapturedAsync: %s", result);
		return false;
	}

	@Override
	public void onComponentError(TuFragment fragment, TuSdkResult result,
			Error error)
	{
		TLog.d("onComponentError: fragment - %s, result - %s, error - %s",
				fragment, result, error);
	}

}
