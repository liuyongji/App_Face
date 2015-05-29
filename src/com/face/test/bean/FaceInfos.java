package com.face.test.bean;

import java.util.List;

public class FaceInfos {
	private String session_id;
	private String img_height;
	private String img_width;
	private String img_id;
	private String url;
	private String response_code;
	private List<FaceInfo> face;
	public  static class FaceInfo {
		private String tag;
		private String face_id;
		private Position position;
		private Attribute attribute;
		
		public static class Position{
			private MouthLeft mouth_left;
			private MouthRight mouth_right;
			private Center center;
			private Nose nose;
		    private Eyeleft eye_left;
		    private EyeRight eye_right;
			private String height;
			private String width;
			public static class MouthLeft{
				private String y;
				private String x;
				public String getX() {
					return x;
				}
				public void setX(String x) {
					this.x = x;
				}
				public String getY() {
					return y;
				}
				public void setY(String y) {
					this.y = y;
				}
			}
			public static class MouthRight{
				private String y;
				private String x;
				public String getX() {
					return x;
				}
				public void setX(String x) {
					this.x = x;
				}
				public String getY() {
					return y;
				}
				public void setY(String y) {
					this.y = y;
				}
			}
			public static class Center{
				private String y;
				private String x;
				public String getX() {
					return x;
				}
				public void setX(String x) {
					this.x = x;
				}
				public String getY() {
					return y;
				}
				public void setY(String y) {
					this.y = y;
				}
			}
			public static class Nose{
				private String y;
				private String x;
				public String getX() {
					return x;
				}
				public void setX(String x) {
					this.x = x;
				}
				public String getY() {
					return y;
				}
				public void setY(String y) {
					this.y = y;
				}
			}
			public static class Eyeleft{
				private String y;
				private String x;
				public String getX() {
					return x;
				}
				public void setX(String x) {
					this.x = x;
				}
				public String getY() {
					return y;
				}
				public void setY(String y) {
					this.y = y;
				}
			}
			public static class EyeRight{
				private String y;
				private String x;
				public String getX() {
					return x;
				}
				public void setX(String x) {
					this.x = x;
				}
				public String getY() {
					return y;
				}
				public void setY(String y) {
					this.y = y;
				}
			}
			public String getWidth() {
				return width;
			}
			public void setWidth(String width) {
				this.width = width;
			}
			public String getHeight() {
				return height;
			}
			public void setHeight(String height) {
				this.height = height;
			}
			public EyeRight getEye_right() {
				return eye_right;
			}
			public void setEye_right(EyeRight eye_right) {
				this.eye_right = eye_right;
			}
			public Eyeleft getEye_left() {
				return eye_left;
			}
			public void setEye_left(Eyeleft eye_left) {
				this.eye_left = eye_left;
			}
			public Nose getNose() {
				return nose;
			}
			public void setNose(Nose nose) {
				this.nose = nose;
			}
			public Center getCenter() {
				return center;
			}
			public void setCenter(Center center) {
				this.center = center;
			}
			public MouthRight getMouth_right() {
				return mouth_right;
			}
			public void setMouth_right(MouthRight mouth_right) {
				this.mouth_right = mouth_right;
			}
			public MouthLeft getMouth_left() {
				return mouth_left;
			}
			public void setMouth_left(MouthLeft mouth_left) {
				this.mouth_left = mouth_left;
			}
		}
		public static class Attribute{
			private Race race;
			private Gender gender;
			private Smilling smilling;
			private Age age;
			private Glass glass;
			public static class Glass{
				private String value;
				private String confidence;
				public String getValue() {
					return value;
				}
				public void setValue(String value) {
					this.value = value;
				}
				public String getConfidence() {
					return confidence;
				}
				public void setConfidence(String confidence) {
					this.confidence = confidence;
				}
			}
			public static class Race{
				private String value;
				private String confidence;
				public String getValue() {
					return value;
				}
				public void setValue(String value) {
					this.value = value;
				}
				public String getConfidence() {
					return confidence;
				}
				public void setConfidence(String confidence) {
					this.confidence = confidence;
				}
			}
			public static class Gender{
				private String value;
				private String confidence;
				public String getValue() {
					return value;
				}
				public void setValue(String value) {
					this.value = value;
				}
				public String getConfidence() {
					return confidence;
				}
				public void setConfidence(String confidence) {
					this.confidence = confidence;
				}
			}
			public static class Smilling{
				private String value;

				public String getValue() {
					return value;
				}

				public void setValue(String value) {
					this.value = value;
				}
			}
			public static class Age{
				private String value;
				private String range;
				public String getRange() {
					return range;
				}
				public void setRange(String range) {
					this.range = range;
				}
				public String getValue() {
					return value;
				}
				public void setValue(String value) {
					this.value = value;
				}
			}
			public Age getAge() {
				return age;
			}
			public void setAge(Age age) {
				this.age = age;
			}
			public Smilling getSmilling() {
				return smilling;
			}
			public void setSmilling(Smilling smilling) {
				this.smilling = smilling;
			}
			public Gender getGender() {
				return gender;
			}
			public void setGender(Gender gender) {
				this.gender = gender;
			}
			public Race getRace() {
				return race;
			}
			public void setRace(Race race) {
				this.race = race;
			}
			public Glass getGlass() {
				return glass;
			}
			public void setGlass(Glass glass) {
				this.glass = glass;
			}
		}
		public String getTag() {
			return tag;
		}
		public void setTag(String tag) {
			this.tag = tag;
		}
		public String getFace_id() {
			return face_id;
		}
		public void setFace_id(String face_id) {
			this.face_id = face_id;
		}
		public Position getPosition() {
			return position;
		}
		public void setPosition(Position position) {
			this.position = position;
		}
		public Attribute getAttribute() {
			return attribute;
		}
		public void setAttribute(Attribute attribute) {
			this.attribute = attribute;
		}
		
	}
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	public String getImg_height() {
		return img_height;
	}
	public void setImg_height(String img_height) {
		this.img_height = img_height;
	}
	public String getImg_width() {
		return img_width;
	}
	public void setImg_width(String img_width) {
		this.img_width = img_width;
	}
	public String getImg_id() {
		return img_id;
	}
	public void setImg_id(String img_id) {
		this.img_id = img_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getResponse_code() {
		return response_code;
	}
	public void setResponse_code(String response_code) {
		this.response_code = response_code;
	}
	public List<FaceInfo> getFace() {
		return face;
	}
	public void setFace(List<FaceInfo> face) {
		this.face = face;
	}

}
