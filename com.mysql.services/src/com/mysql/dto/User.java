package com.mysql.dto;

import java.sql.Timestamp;

public class User extends Standard{
	
	private String userID;
	private String firstName;
	private String lastName;
	private String password;
	private Timestamp date;
	private String image;
	private String email;
	private String facbeookID;
	private String googleplusID;
	private String secureID;
	private boolean active;
	private int rating;
	private String description;
	private String mainInfo;
	
	public String getUserID() {
		return userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Timestamp getDate() {
		return date;
	}
	
	public void setDate(Timestamp date) {
		this.date = date;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFacbeookID() {
		return facbeookID;
	}
	
	public void setFacbeookID(String facbeookID) {
		this.facbeookID = facbeookID;
	}
	
	public String getGoogleplusID() {
		return googleplusID;
	}
	
	public void setGoogleplusID(String googleplusID) {
		this.googleplusID = googleplusID;
	}
	
	public String getSecureID() {
		return secureID;
	}
	
	public void setSecureID(String secureID) {
		this.secureID = secureID;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public int getRating() {
		return rating;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getMainInfo() {
		return mainInfo;
	}
	
	public void setMainInfo(String mainInfo) {
		this.mainInfo = mainInfo;
	}
}