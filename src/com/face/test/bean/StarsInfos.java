package com.face.test.bean;

import java.util.List;

public class StarsInfos {
	private String session_id;
	private List<StarInfo> candidate;
	public static class StarInfo{
		private String face_id;
		private String similarity;
		private String tag;
		public String getTag() {
			return tag;
		}
		public void setTag(String tag) {
			this.tag = tag;
		}
		public String getSimilarity() {
			return similarity;
		}
		public void setSimilarity(String similarity) {
			this.similarity = similarity;
		}
		public String getFace_id() {
			return face_id;
		}
		public void setFace_id(String face_id) {
			this.face_id = face_id;
		}
	}
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	public List<StarInfo> getCandidate() {
		return candidate;
	}
	public void setCandidate(List<StarInfo> candidate) {
		this.candidate = candidate;
	}

}
