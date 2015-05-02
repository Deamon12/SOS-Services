package com.mysql.dto;

public class Member extends Standard {
	private int memberID;
	private String userID;
	private int questionID;
	
	public int getMemberID(){
		return memberID;
	}

	public void setMemberID(int memberID) {
		this.memberID = memberID;
	}

	public String getUserID(){
		return userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public int getQuestionID(){
		return questionID;
	}
	
	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}
}