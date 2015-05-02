package com.mysql.dto;

import java.sql.Timestamp;

public class Comment extends Standard {
	
	private int commentID;
	private Timestamp posted;
	private String comment;
	private int questionID;


	public int getCommentID() {
		return commentID;
	}
	
	public void setCommentID(int commentID) {
		this.commentID = commentID;
	}
	
	public Timestamp getPosted() {
		return posted;
	}

	public void setPosted(Timestamp posted) {
		this.posted = posted;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		 this.comment = comment;
	}
	
	public int getQuestionID() {
		return questionID;
	}
	
	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}
	
}