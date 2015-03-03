package com.face.test.bean;

import cn.bmob.v3.BmobObject;

public class ClientError extends BmobObject{
	private String mModel;
	private String mSystem;
	private String mError;
	public String getmError() {
		return mError;
	}
	public void setmError(String mError) {
		this.mError = mError;
	}
	public String getmSystem() {
		return mSystem;
	}
	public void setmSystem(String mSystem) {
		this.mSystem = mSystem;
	}
	public String getmModel() {
		return mModel;
	}
	public void setmModel(String mModel) {
		this.mModel = mModel;
	}

}
