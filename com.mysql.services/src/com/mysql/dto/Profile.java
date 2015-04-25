package com.mysql.dto;

import java.util.ArrayList;
import java.util.List;

public class Profile extends Standard{

	private String first;
	private String last;
	private String img;
	private String userID;
	private String venueCount;
	private String pictureCount;
	private String imageCount;
	private List<VenueComment> images;
	
	
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
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getVenueCount() {
		return venueCount;
	}
	public void setVenueCount() {
		List<String> locationIds = new ArrayList<String>();
		
		for(int a = 0; a < images.size(); a++){
			if(!locationIds.contains(images.get(a).getLocationID())){
				locationIds.add(images.get(a).getLocationID());
			}
		}
		this.venueCount = locationIds.size()+"";
	}
	public String getPictureCount() {
		return pictureCount;
	}
	public void setPictureCount(String pictureCount) {
		this.pictureCount = pictureCount;
	}
	public String getImageCount() {
		return imageCount;
	}
	public void setImageCount(String imageCount) {
		this.imageCount = imageCount;
	}
	public List<VenueComment> getImages() {
		return images;
	}
	public void setImages(List<VenueComment> images) {
		this.imageCount = images.size()+"";
		this.pictureCount = images.size()+"";
		this.images = images;
		setVenueCount();
	}
	
	@Override
	public String toString() {
		return "Profile [first=" + first + ", last=" + last + ", img=" + img
				+ ", venueCount=" + venueCount + ", pictureCount="
				+ pictureCount + ", imageCount=" + imageCount + ", images="
				+ images + "]";
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	
	
}
