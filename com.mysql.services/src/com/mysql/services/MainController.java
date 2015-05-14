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
			@QueryParam("deviceId") String deviceId){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.createUser(firstName, lastName, password, email, deviceId));
	}

	@GET
	@Path ("/getUserById")
	@Produces ("application/json")
	public String getUserById(@QueryParam("userId") String userId){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.getUserById(userId));
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
			@QueryParam("studyGroup") int studyGroup
			){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.createQuestion(userId, latitude, 
				longitude, text, tags, tutor, studyGroup));
	}
	
	@GET
	@Path ("/viewQuestion")
	@Produces ("application/json")
	public String viewQuestion(@QueryParam("questionId") int questionId){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.viewQuestion(questionId));
	}
	
	
	@GET
	@Path ("/askToJoinGroup")
	@Produces ("application/json")
	public String askToJoinGroup(@QueryParam("questionId") int questionId, 
			@QueryParam("userId") String userId){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.askToJoinGroup(questionId, userId));
	}
	
	
	@GET
	@Path ("/acceptUser")
	@Produces ("application/json")
	public String acceptUser(@QueryParam("questionId") int questionId, 
			@QueryParam("userId") String userId){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.acceptUser(questionId, userId));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param groupId - groupId; Use it to retrieve the userId who is hosting the group so we can notify them
	 * @param userId - Person wanting to join the group
	 * @return 
	 */
	/*
	@Path ("/sendNotification")
	@GET
	@Produces("application/json")
	public String sendNotification(@QueryParam("groupId") String groupId, @QueryParam("userId") String userId){
		
		
		//SOSModel model = new SOSModel();
		//return new Gson().toJson(model.sendNotification(groupId, userId));
		
		//GoogleCloudMessaging is our object that handles notificatoin calls
		 
				GoogleCloudMessaging testing = new GoogleCloudMessaging();
				try {
					
					return testing.sendMessage("Message testing" , Datastore.getDevices());
				} catch (IOException e) {
					
					return e.toString();
				}
				
		
		
	}*/
	
	/*
	@Path ("/sendNotification")
	@GET
	//@Produces(MediaType.TEXT_PLAIN)
	@Produces("application/json")
	public String sendNotification(@QueryParam("userId") String userId, @QueryParam("groupId") String userId){
		
		
		
		Datastore.clearDevices();
		
		String emulatorId = "APA91bEcFh1-Kgq_rcnmonKKTYhiwIqSxuHsaN90DeuaQKpr08b4jSScLQmL2b1W4_KPKvpYqAzQaeJS_18efcLdh9CxMXJNd2pTx0VG1M97rI9okHvj2rCYmCvTjiRPkukk4TdMQxiy-LXhrKnyods5sEMYrOx65Q";
		String myId = "APA91bFYrg-EnRpTuwC1L9SugPGSZyLHCUNJDJnd2Iu7DVERg-F2TEC1DsF-i_INE0rYZQUsA4q1v_733RqwyDklfE8DLI5Y99jC1ef-sUa4I15Wb3Y20j0aevzZNRvYNgDG1Ir0JpdibWXLz4GGEQ_qX4fFJCg4Hw";
		Datastore.register(myId);
		Datastore.register(emulatorId);
		
		//GoogleCloudMessaging is our object that handles notificatoin calls
		 
		GoogleCloudMessaging testing = new GoogleCloudMessaging();
		try {
			
			return testing.sendMessage("Message testing" , Datastore.getDevices());
		} catch (IOException e) {
			
			return e.toString();
		}
	}*/
	
	
	
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
	
	/*
	@Path ("/gcmtest")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String gcmTest() {
		
		if (devices.isEmpty()) {
			System.out.print("No devices registered!");
			return "No devices registered!";
		} else {
			System.out.print(devices.size() + " device(s) registered!");
			return devices.size() + " device(s) registered!";
		}
		//return "devices: "+devices.toString();
		
		
	}

	*/
	
	
	
	
	
	
}
