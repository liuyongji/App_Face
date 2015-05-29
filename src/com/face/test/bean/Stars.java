package com.face.test.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Stars extends BmobObject{
	private String faceId;
	private String url;
	private String name;
	private BmobFile bmobFile;
	public String getFaceId() {
		return faceId;
	}
	public void setFaceId(String faceId) {
		this.faceId = faceId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public BmobFile getBmobFile() {
		return bmobFile;
	}
	public void setBmobFile(BmobFile bmobFile) {
		this.bmobFile = bmobFile;
	}

}
