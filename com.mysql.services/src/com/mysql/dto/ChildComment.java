package com.mysql.dto;

public class ChildComment extends Standard{

	private String date;
	private String userID;
	private String text;
	private String first;
	private String last;
	private String userImg;
	private String feedID;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getFirst() {
		return first;
	}
	public void setFirst(String first) {
		this.first = first;
	}
	public String getLast() {
		return last;
	}
	public void setLast(String last) {
		this.last = last;
	}
	public String getUserImg() {
		return userImg;
	}
	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	public String getFeedID() {
		return feedID;
	}
	public void setFeedID(String feedID) {
		this.feedID = feedID;
	}
	
	
}