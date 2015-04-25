package com.mysql.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

@Path ("/serviceclass")
public class MainController {

	
	@Path ("/test")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "Test Call from MainController class";
	}
	
	
	@GET
	@Path ("/getVenues")
	@Produces ("application/json")
	public String getVenueList(){
		
		LocalLiveSpotModel model = new LocalLiveSpotModel();
		return new Gson().toJson(model.getVenues());
	}
	
	@GET
	@Path ("/getProfileData")
	@Produces ("application/json")
	public String getProfileData(@QueryParam("userID") String userID){
		
		LocalLiveSpotModel model = new LocalLiveSpotModel();
		return new Gson().toJson(model.getProfileData(userID));
	}
	
	@GET
	@Path ("/getPhotosByUser")
	@Produces ("application/json")
	public String getProfileImages(@QueryParam("userID") String userID){
		
		LocalLiveSpotModel model = new LocalLiveSpotModel();
		return new Gson().toJson(model.getPhotosByUser(userID));
	}
	
	/*@GET
	@Path ("/getVenueComments")
	@Produces ("application/json")
	public String getVenueComments(){
		
		LocalLiveSpotModel model = new LocalLiveSpotModel();
		return new Gson().toJson(model.getVenueComments());
	}*/
	
	
	
	@GET
	@Path ("/getVenuePhotos")
	@Produces ("application/json")
	public String getVenuePhotos(@QueryParam("lid") String locationID){
		
		LocalLiveSpotModel model = new LocalLiveSpotModel();
		return new Gson().toJson(model.getVenuePhotos(locationID));
	}
	
	
/*
	@GET
	@Path ("/getVenueNews/{venueID}")
	@Produces ("application/json")
	public StandardNewsResponse getSpecificVenueNews(@PathParam("venueID") String venueID){
		
		LocalLiveSpotModel model = new LocalLiveSpotModel();

		return model.getSpecificVenueNews(venueID);
	}
	
	@GET
	@Path ("/getRecentNews")
	@Produces ("application/json")
	public StandardNewsResult getRecentNews(){
		
		LocalLiveSpotModel model = new LocalLiveSpotModel();
		
		return model.getRecentNews();
	}
	

	@GET
	@Path ("/getChildComments/{feedID}")
	@Produces ("application/json")
	public StandardChildCommentResult getChildComments (@PathParam("feedID") String feedID) throws IOException, SQLException, ClassNotFoundException{
		
		LocalLiveSpotModel model = new LocalLiveSpotModel();
		
		return model.getChildComments(feedID);
	}
	*/
	
}
