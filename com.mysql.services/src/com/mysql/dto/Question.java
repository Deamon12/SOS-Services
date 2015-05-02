package com.mysql.dto;

import java.sql.Timestamp;

public class Question extends Standard {
	
	private int questionID;
	private String userID;
	private float latitude;
	private float longitude;
	private String text;
	private Timestamp date;
	private boolean active;
	private boolean tutor;
	private boolean studyGroup;

	public int getQuestionID(){
		return questionID;
	}

	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}

	public String getUserID(){
		return userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public float getLatitude(){
		return latitude;
	}
	
	public void setLatitude(float latitude){
		this.latitude = latitude;
	}
	
	public float getLongitude(){
		return longitude;
	}
	
	public void setLongitude(float longitude){
		this.longitude = longitude;
	}
	
	public String getText(){
		return text;
	}
	
	public void setText(String text){
		this.text = text;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isTutor() {
		return tutor;
	}

	public void setTutor(boolean tutor) {
		this.tutor = tutor;
	}

	public boolean isStudyGroup() {
		return studyGroup;
	}

	public void setStudyGroup(boolean studyGroup) {
		this.studyGroup = studyGroup;
	}
	
}