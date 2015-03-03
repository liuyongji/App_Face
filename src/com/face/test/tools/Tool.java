package com.face.test.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.face.test.bean.ClientError;
import com.face.test.bean.Face;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

public class Tool {
	public static Face face;
	private static File f;
	
	/**
	 * 
	 * @param bitmap
	 * @return byte[]
	 */
	 public static byte[] getBitmapByte(Context context,Bitmap bitmap){  
	        ByteArrayOutputStream out = new ByteArrayOutputStream();  
	        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);  
	        try {  
	            out.flush();  
	            out.close();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	            new Http(context).postUrl(new ClientError(), Http.getError(e));
	        }  
	        return out.toByteArray();  
	    } 
	 public static Bitmap getScaledBitmap(String fileName, int dstWidth)
	    {
	        BitmapFactory.Options localOptions = new BitmapFactory.Options();
	        localOptions.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(fileName, localOptions);
	        int originWidth = localOptions.outWidth;
	        int originHeight = localOptions.outHeight;

	        localOptions.inSampleSize = originWidth > originHeight ? originWidth / dstWidth
	                : originHeight / dstWidth;
	        localOptions.inJustDecodeBounds = false;
	        return BitmapFactory.decodeFile(fileName, localOptions);
	    }
	 /*
	  * json 解析
	  */
	 
	 public static String Jsonn(JSONObject jsonObject) throws JSONException{
	   	
	   	StringBuffer buffer = new StringBuffer();
	   	JSONArray jsonArray = jsonObject.getJSONArray("face");
	   	if(jsonArray.length()>0){
	   	 for (int i = 0; i < jsonArray.length(); i++) {  
	   		 JSONObject personObject = jsonArray.getJSONObject(i);
	   		 JSONObject attrObject = personObject.getJSONObject("attribute");
	   		 face=new Face();
	   		 face.setFaceId(personObject.getString("face_id"));
	   		 face.setAgeValue(attrObject.getJSONObject("age").getInt("value")); 
	   		 face.setAgeRange(attrObject.getJSONObject("age").getInt("range")); 
	   		 face.setGenderValue(genderConvert(attrObject.getJSONObject("gender").getString("value")));
	   		 face.setGenderConfidence(attrObject.getJSONObject("gender").getDouble("confidence"));
	   	     face.setRaceValue(raceConvert(attrObject.getJSONObject("race").getString("value")));
	   	     face.setRaceConfidence(attrObject.getJSONObject("race").getDouble("confidence"));
	   	     face.setSmilingValue(attrObject.getJSONObject("smiling").getDouble("value"));
//	   	     buffer.append(face.getFaceId()+"\n");
	   	     buffer.append("检测到人脸信息如下：\n");
	     	 buffer.append("种族：").append(face.getRaceValue()).append("人种\n");
	     	 buffer.append("种族准确度：").append(Double.toString(face.getRaceConfidence()).substring(0, 5)).append("%\n");
	     	 buffer.append("性别：").append(face.getGenderValue()).append("\n");
	     	 buffer.append("性别准确度：").append(Double.toString(face.getGenderConfidence()).substring(0, 5)).append("%\n");
	     	 buffer.append("年龄：").append(face.getAgeValue()).append("岁\n");
	     	 buffer.append("年龄误差：").append(face.getAgeRange()).append("\n");
	     	 buffer.append("微笑指数：").append(face.getSmilingValue());
	   	  }
	   	}
	   	else {
			return null;
		}
	   
	   	 return buffer.toString();
	   }
	 
	 /** 
	     * 性别转换（英文->中文） 
	     *  
	     * @param gender 
	     * @return 
	     */  
	    private static String genderConvert(String gender) {  
	        String result = "男性";  
	        if ("Male".equals(gender))  
	            result = "男性";  
	        else if ("Female".equals(gender))  
	            result = "女性";  
	  
	        return result;  
	    } 
	    
	    /** 
	     * 人种转换（英文->中文） 
	     *  
	     * @param race 
	     * @return 
	     */  
	    private static String raceConvert(String race) {  
	        String result = "黄色";  
	        if ("Asian".equals(race))  
	            result = "黄色";  
	        else if ("White".equals(race))  
	            result = "白色";  
	        else if ("Black".equals(race))  
	            result = "黑色";  
	        return result;
	    } 
	    /**
	     * 
	     * @param jsonObject
	     * @return 各部分相似度
	     * @throws JSONException
	     */
	    public static String CompareResult(JSONObject jsonObject) throws JSONException{
	    	StringBuffer buffer = new StringBuffer();
	    	JSONObject component=jsonObject.getJSONObject("component_similarity");
	    	buffer.append("眼睛：");
	    	buffer.append(component.getString("eye").substring(0, 5)).append("%").append("\n");
	    	buffer.append("嘴巴: ");
	    	buffer.append(component.getString("mouth").substring(0, 5)).append("%").append("\n");
	    	buffer.append("鼻子：");
	    	buffer.append(component.getString("nose").substring(0, 5)).append("%").append("\n");
	    	buffer.append("眉毛：");
	    	buffer.append(component.getString("eyebrow").substring(0, 5)).append("%").append("\n");
	    	buffer.append("总体相似度：");
		    String string=jsonObject.getString("similarity").substring(0, 5);
		    buffer.append(string).append("%");
		   	 return buffer.toString();
		   }
	    /**
	     * 
	     * @param jsonObject 
	     * @return  仅返回总体相似度
	     * @throws JSONException
	     */
	    public static String Similarity(JSONObject jsonObject) throws JSONException{
		  
	    	String string=jsonObject.getString("similarity").substring(0, 5)+"%";
		   	 return string;
		   }
	    public static Dialog dialog(Context context,String message){
	    	Dialog dialog=new AlertDialog.Builder(context)
	    	.setTitle("提示")
	    	.setMessage(message)
	    	.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO 自动生成的方法存根
					dialog.cancel();
				}
			}).create();
	    	return dialog;
	    }
	    
	    public static ProgressDialog getProgressDialog(Context context){
	    	
	    	ProgressDialog progressDialog=ProgressDialog.show(context, "正在检测...", "Please wait...", true, false);
	    	progressDialog.setCancelable(false);
	    	progressDialog.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO 自动生成的方法存根
					dialog.dismiss();
				}
			});
	    	return progressDialog;
	    }
	    
	    public static String bitmapChangeString(Bitmap bitmap){
			if(bitmap != null){
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
				String str = new String(Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT));
				return str;
			}
			return null;
		}
	    
	    public static File saveBitmap(Bitmap bitmap) { 
//	    	f.createTempFile("prefix", "suffix");
	    	
	    	try { 
	    		f=new File(Environment.getExternalStorageDirectory().getPath()+"/tempbtp.png");
		    	if (f.exists()) { 
		    	f.delete(); 
		    	} 
	    	FileOutputStream out = new FileOutputStream(f); 
	    	bitmap.compress(Bitmap.CompressFormat.PNG, 90, out); 
	    	out.flush(); 
	    	out.close(); 
	    	Log.i("facetest", "create success"); 
	    	} catch (FileNotFoundException e) { 
	    	// TODO Auto-generated catch block 
	    	e.printStackTrace(); 
	    	} catch (IOException e) { 
	    	// TODO Auto-generated catch block 
	    	e.printStackTrace(); 
	    	}
			return f; 
	    	} 
	    public static void deletefile(){
	    	f.delete();
	    	Log.i("facetest", "delete success");
	    }
	 

}
