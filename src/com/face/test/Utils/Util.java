package com.face.test.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.face.test.bean.Face;

public class Util {
	public static Face face;

	/*
	 * json 解析
	 */

	public static String Jsonn(JSONObject jsonObject) throws JSONException {

		StringBuffer buffer = new StringBuffer();
		// List<String> list = new ArrayList<String>();

		JSONArray jsonArray = jsonObject.getJSONArray("face");
		if (jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject personObject = jsonArray.getJSONObject(i);
				JSONObject attrObject = personObject.getJSONObject("attribute");
				face = new Face();
				face.setFaceId(personObject.getString("face_id"));
				face.setAgeValue(attrObject.getJSONObject("age")
						.getInt("value"));
				face.setAgeRange(attrObject.getJSONObject("age")
						.getInt("range"));
				face.setGenderValue(genderConvert(attrObject.getJSONObject(
						"gender").getString("value")));
				face.setGenderConfidence(attrObject.getJSONObject("gender")
						.getDouble("confidence"));
				face.setRaceValue(raceConvert(attrObject.getJSONObject("race")
						.getString("value")));
				face.setRaceConfidence(attrObject.getJSONObject("race")
						.getDouble("confidence"));
				face.setSmilingValue(attrObject.getJSONObject("smiling")
						.getDouble("value"));
				// buffer.append(face.getFaceId()+"\n");

				// list.add("肤色：" + face.getRaceValue());
				// list.add("肤色准确度："
				// + Double.toString(face.getRaceConfidence()).substring(
				// 0, 5) + "%");
				// list.add("性别：" + face.getGenderValue());
				// list.add("性别准确度："
				// + Double.toString(face.getGenderConfidence())
				// .substring(0, 5) + "%");
				// list.add("年龄：" + face.getAgeValue() + "岁");
				// list.add("年龄误差：" + face.getAgeRange());
				// list.add("微笑指数：" + face.getSmilingValue() + "%");

				buffer.append("肤色：").append(face.getRaceValue()).append("  ");
				buffer.append("性别：").append(face.getGenderValue()).append("  ");
				buffer.append("误差：")
						.append(Double.toString(face.getGenderConfidence())
								.substring(0, 5)).append("% ").append("\n");
				buffer.append("年龄：").append(face.getAgeValue()).append("岁 ");
				buffer.append("误差：").append(face.getAgeRange()).append("岁  ");
				buffer.append("笑容度：")
						.append(Double.toString(face.getSmilingValue())
								.substring(0, 5)).append("%");
			}
		} else {
			buffer.append("没检测到人脸");
			// return null;
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
	public static String CompareResult(JSONObject jsonObject)
			throws JSONException {
		StringBuffer buffer = new StringBuffer();
		JSONObject component = jsonObject.getJSONObject("component_similarity");
		buffer.append("眼睛：");
		buffer.append(component.getString("eye").substring(0, 5)).append("%")
				.append("\n");
		buffer.append("嘴巴: ");
		buffer.append(component.getString("mouth").substring(0, 5)).append("%")
				.append("\n");
		buffer.append("鼻子：");
		buffer.append(component.getString("nose").substring(0, 5)).append("%")
				.append("\n");
		buffer.append("眉毛：");
		buffer.append(component.getString("eyebrow").substring(0, 5))
				.append("%").append("\n");
		buffer.append("总体相似度：");
		String string = jsonObject.getString("similarity").substring(0, 5);
		buffer.append(string).append("%");
		return buffer.toString();
	}

	/**
	 * 
	 * @param jsonObject
	 * @return 仅返回总体相似度
	 * @throws JSONException
	 */
	public static String Similarity(JSONObject jsonObject) throws JSONException {

		String string = jsonObject.getString("similarity").substring(0, 5);

		return string;
	}

	public static String changeFloat(float float1) {
		int tmpflt = (int) float1;
		String s = " ";
		if (tmpflt < 80) {
			tmpflt = 80 + (int) (Math.random() * 20);
			s="情侣指数："+tmpflt;
		}else if (tmpflt>92) {
			s="情侣指数："+tmpflt+"  有可能是同一个人";
		}else if (tmpflt<90&&tmpflt>85) {
			s="情侣指数："+tmpflt+"  有可能是亲人";
		}{
			
		}
		return s;
	}

	public static String change1(String sim, int code) {
		int num = (int) Float.parseFloat(sim);
		// if (num<10) {
		// return "num_0.png";
		// }
		int j = 0;
		switch (code) {
		case 1:
			j = num / 10 / 10 % 10;
			
			break;
		case 2:
			j = num / 10 % 10;
			break;
		case 3:
			j = num % 10;
			break;
		default:
			break;
		}

		String result = "num_" + j + ".png";
		return result;
	}

}
