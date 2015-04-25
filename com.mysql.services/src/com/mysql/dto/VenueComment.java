package com.mysql.dto;

public class VenueComment extends Standard{

		private String active;
		private String date;
		private String id;
		private String img;
		private String text;
		private String locationID;
		private String userID;
		private String twitterFeedID;
		
		private Profile user;
		
		private Object parent;
		private Object child;
		private Object likes;
		
		public Object getParent() {
			return parent;
		}
		public void setParent(Object parent) {
			this.parent = parent;
		}
		public Object getChild() {
			return child;
		}
		public void setChild(Object child) {
			this.child = child;
		}
		public Object getLikes() {
			return likes;
		}
		public void setLikes(Object likes) {
			this.likes = likes;
		}
		public String getActive() {
			return active;
		}
		public void setActive(String active) {
			this.active = active;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getImg() {
			return img;
		}
		public void setImg(String img) {
			this.img = img;
		}
		public String getLocationID() {
			return locationID;
		}
		public void setLocationID(String locationID) {
			this.locationID = locationID;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getUserID() {
			return userID;
		}
		public void setUserID(String userID) {
			this.userID = userID;
		}
		public String getTwitterFeedID() {
			return twitterFeedID;
		}
		public void setTwitterFeedID(String twitterFeedID) {
			this.twitterFeedID = twitterFeedID;
		}
		
		@Override
		public String toString() {
			return "FeedImage [active=" + active + ", date=" + date + ", id="
					+ id + ", img=" + img + ", locationID=" + locationID
					+ ", text=" + text + ", userID=" + userID
					+ ", twitterFeedID=" + twitterFeedID + "]";
		}
		public Profile getUser() {
			return user;
		}
		public void setUser(Profile user) {
			this.user = user;
		}
		
		
		
}
