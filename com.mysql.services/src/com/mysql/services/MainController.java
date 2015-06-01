package com.mysql.services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.android.gcm.demo.server.Datastore;
import com.google.gson.Gson;

@Path ("/serviceclass")
public class MainController {
	
	
	
	@Path ("/test")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		
		return "Test Call from MainController class. With Recent Changes";
	}
	
	
	@GET
	@Path ("/getTags")
	@Produces ("application/json")
	public String getTags(){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.getTags());
	}
	
	@GET
	@Path("/createUser")
	@Produces("application/json")
	public String createUser(
			@QueryParam("firstName") String firstName, 
			@QueryParam("lastName") String lastName,
			@QueryParam("password") String password, 
			@QueryParam("email") String email, 
			@QueryParam("image") String image,
			@QueryParam("deviceId") String deviceId){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.createUser(firstName, lastName, password, email, image, deviceId));
	}

	@GET
	@Path ("/getUserById")
	@Produces ("application/json")
	public String getUserById(@QueryParam("userId") String userId){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.getUserById(userId));
	}
	
	@GET
	@Path ("/updateProfile")
	@Produces ("application/json")
	public String updateProfile(@QueryParam("userId") String userId,
			@QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName,
			@QueryParam("school") String school,
			@QueryParam("major") String major,
			@QueryParam("description") String description,
			@QueryParam("image") String image){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.updateProfile(userId, firstName, lastName, school, major, description, image));
	}
	
	@GET
	@Path ("/doLogin")
	@Produces ("application/json")
	public String doLogin(
			@QueryParam("email") String email, 
			@QueryParam("password") String password){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.doLogin(email, password));
	}
	
	@GET
	@Path ("/forgotPassword")
	@Produces ("application/json")
	public String forgotPassword(
			@QueryParam("email") String email)
			{
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.forgotPassword(email));
	}
	
	@GET
	@Path ("/resetPassword")
	@Produces ("application/json")
	public String resetPassword(
			@QueryParam("userId") String userId, 
			@QueryParam("password") String password){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.resetPassword(userId, password));
	}
	
	@GET
	@Path ("/resetEmail")
	@Produces ("application/json")
	public String resetEmail(
			@QueryParam("email") String email, 
			@QueryParam("userId") String userId){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.resetEmail(email, userId));
	}
	
	@GET
	@Path ("/inGroup")
	@Produces ("application/json")
	public String inGroup(
			@QueryParam("userId") String userId){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.inGroup(userId));
	}
	
	@GET
	@Path ("/hasQuestion")
	@Produces ("application/json")
	public String hasQuestion(
			@QueryParam("userId") String userId){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.hasQuestion(userId));
	}

	@GET
	@Path ("/editQuestion")
	@Produces ("application/json")
	public String editQuestion(
			@QueryParam("questionId") int questionId, 
			@QueryParam("text") String text,
			@QueryParam("tags") List<String> tags,
			@QueryParam("tutor") int tutor,
			@QueryParam("studyGroup") int studyGroup,
			@QueryParam("topic") String topic,
			@QueryParam("visibleLocation") int visibleLocation
			){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.editQuestion(questionId,
				text, tags, tutor, studyGroup, topic, visibleLocation));
	}
	
	@GET
	@Path ("/removeAllTags")
	@Produces ("application/json")
	public String removeAllTags(
			@QueryParam("questionId") int questionId){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.removeAllTags(questionId));
	}
	
	@GET
	@Path ("/editLocation")
	@Produces ("application/json")
	public String editLocation(
			@QueryParam("questionId") int questionId, 
			@QueryParam("latitude") double latitude, 
			@QueryParam("longitude") double longitude){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.editLocation(questionId, latitude, longitude));
	}
	
	@GET
	@Path ("/changeOwner")
	@Produces ("application/json")
	public String changeOwner(
			@QueryParam("questionId") int questionId,
			@QueryParam("userId") String userId){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.changeOwner(questionId, userId));
	}
		
	@GET
	@Path ("/setVisibility")
	@Produces ("application/json")
	public String setVisibility(
			@QueryParam("questionId") int questionId,
			@QueryParam("visibility") int visible){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.setVisibility(questionId, visible));
	}
	
	@GET
	@Path ("/getQuestions")
	@Produces ("application/json")
	public String getQuestions(
			@QueryParam("latitude") double latitude, 
			@QueryParam("longitude") double longitude, 
			@QueryParam("tags") List<String> tags,
			@QueryParam("limit") double limit){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.getQuestions(latitude, longitude, tags, limit));
	}
	
	
	@GET
	@Path ("/createQuestion")
	@Produces ("application/json")
	public String createQuestion(
			@QueryParam("userId") String userId, 
			@QueryParam("latitude") double latitude, 
			@QueryParam("longitude") double longitude, 
			@QueryParam("text") String text,
			@QueryParam("tags") List<String> tags,
			@QueryParam("tutor") int tutor,
			@QueryParam("studyGroup") int studyGroup,
			@QueryParam("topic") String topic,
			@QueryParam("visibleLocation") int visibleLocation
			){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.createQuestion(userId, latitude, 
				longitude, text, tags, tutor, studyGroup, topic, visibleLocation));
	}
	
	@GET
	@Path ("/viewQuestion")
	@Produces ("application/json")
	public String viewQuestion(@QueryParam("questionId") int questionId){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.viewQuestion(questionId));
	}
	
	@GET
	@Path ("/getComments")
	@Produces ("application/json")
	public String getComments(@QueryParam("questionId") int questionId){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.getComments(questionId));
	}
	
	@GET
	@Path ("/addComment")
	@Produces ("application/json")
	public String addComment(@QueryParam("questionId") int questionId,
			@QueryParam("userId") String userId,
			@QueryParam("comment") String comment){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.addComment(questionId, userId, comment));
	}
	
	
	@GET
	@Path ("/askToJoinGroup")
	@Produces ("application/json")
	public String askToJoinGroup(@QueryParam("questionId") int questionId, 
			@QueryParam("userId") String userId,
			@QueryParam("tutor") int tutor){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.askToJoinGroup(questionId, userId, tutor));
	}
	
	
	@GET
	@Path ("/acceptUser")
	@Produces ("application/json")
	public String acceptUser(@QueryParam("questionId") int questionId, 
			@QueryParam("userId") String userId,
			@QueryParam("tutor") int tutor){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.acceptUser(questionId, userId, tutor));
	}
	

	@GET
	@Path ("/removeUser")
	@Produces ("application/json")
	public String removeUser(@QueryParam("userId") String userId){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.removeUser(userId));
	}
	
	@GET
	@Path ("/viewMembers")
	@Produces ("application/json")
	public String viewMembers(@QueryParam("questionId") int questionId){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.viewMembers(questionId));
	}

	@GET
	@Path ("/closeGroup")
	@Produces ("application/json")
	public String closeGroup(@QueryParam("questionId") int questionId){ 
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.closeGroup(questionId));
	}	

	@GET
	@Path ("/openGroup")
	@Produces ("application/json")
	public String openGroup(@QueryParam("questionId") int questionId){ 
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.openGroup(questionId));
	}

	@GET
	@Path ("/removeGroup")
	@Produces ("application/json")
	public String removeGroup(@QueryParam("questionId") int questionId){ 
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.removeGroup(questionId));
	}	
	
	@GET
	@Path ("/rateTutor")
	@Produces ("application/json")
	public String rateTutor(@QueryParam("userId") String userId,
			@QueryParam("ratedUserId") String ratedUserId,
			@QueryParam("like") int like,
			@QueryParam("rated") boolean rated){ 
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.rateTutor(userId, ratedUserId, like, rated));
	}	

	
	@Path ("/gcmregister")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String gcmRegisterDevice(@QueryParam("deviceID") String deviceID){
		
		
		if(deviceID == null || deviceID.equals("")){
			return "Missing param: deviceID";
		}
		List<String> devices = Datastore.getDevices();
		if(!(devices.contains(deviceID))){
			Datastore.register(deviceID);
			return Datastore.getDevices().size()+" Successfully added device: "+deviceID;
		}
		else
			return "Not adding device: "+deviceID+". Already exists.";
		
	}
	
	
	
	@Path ("/gcmclear")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String gcmClearDevices(){
		Datastore.clearDevices();
		
		return "List cleared: "+Datastore.getDevices();
	}
	
	@Path ("/gcmdevicelist")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String gcmDeviceList(){
		List<String> devices = Datastore.getDevices();
		if (devices.isEmpty()) {
			System.out.print("No devices registered!");
			return "No devices registered!";
		} else {
			System.out.print(devices.size() + " device(s) registered!");
			return devices.size() + " device(s) registered!";
		}
	}
	
}
