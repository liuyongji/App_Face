package com.face.test.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Bitchs extends BmobObject{
	private BmobFile file;
	private String verson;
	public String getVerson() {
		return verson;
	}
	public void setVerson(String verson) {
		this.verson = verson;
	}
	public BmobFile getFile() {
		return file;
		
	}
	public void setFile(BmobFile file) {
		this.file = file;
	}

}
