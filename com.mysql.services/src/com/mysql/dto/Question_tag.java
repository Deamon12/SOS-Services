package com.mysql.dto;

public class Question_tag extends Standard{
	
	private int questionTagID;
	private int questionID;
	private int tagID;
	

	public int getQuestionTagID(){
		return questionTagID;
	}
	
	public void setQuestionTagID(int questionTagID) {
		this.questionTagID = questionTagID;
	}
	
	public int getQuestionID(){
		return questionID;
	}
	
	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}

	public int getTagID(){
		return tagID;
	}	

	public void setTagID(int tagID) {
		this.tagID = tagID;
	}

}