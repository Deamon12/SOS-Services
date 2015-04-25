package com.mysql.dto;

public class Venue extends Standard{

	String location;
	String name;
	String city;
	String state;
	String stateAbrev;
	
	
	public Venue(){
		
	}
	
	public Venue(String name){
		System.out.println("Setting name: "+name);
		this.name = name;
	}
	

	public String getLocation() {
		return location;
	}
	public String getName() {
		return name;
	}
	public String getCity() {
		return city;
	}
	public String getState() {
		return state;
	}
	public String getStateAbrev() {
		return stateAbrev;
	}
	
	
	public void setLocation(String location) {
		this.location = location;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setStateAbrev(String stateAbrev) {
		this.stateAbrev = stateAbrev;
	}

	
	@Override
	public String toString() {
		return "VenueObject [location=" + location + ", name=" + name
				+ ", city=" + city + ", state=" + state + ", stateAbrev="
				+ stateAbrev + "]";
	}
	
	
	
}