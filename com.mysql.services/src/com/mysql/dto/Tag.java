package com.mysql.dto;

public class Tag extends Standard{
	
	private int tagID;
	private String tag;
	
	public int getTagID() {
		return tagID;
	}
	
	public void setTagID(int tagID){
		this.tagID = tagID;
	}

	public String getTag() {
		return tag;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
	
}