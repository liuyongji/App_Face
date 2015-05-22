package com.face.test.bean;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Person extends BmobObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6654756067082669517L;
	private String user;
	private BmobFile file;
	private String location;
	private String date;
	private String sex;
	private boolean doubles;
	private String verson;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public BmobFile getFile() {
		return file;
	}
	public void setFile(BmobFile file) {
		this.file = file;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public boolean isDoubles() {
		return doubles;
	}
	public void setDoubles(boolean doubles) {
		this.doubles = doubles;
	}
	public String getVerson() {
		return verson;
	}
	public void setVerson(String verson) {
		this.verson = verson;
	}
	

}
