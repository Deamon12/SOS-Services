package com.mysql.services;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.android.gcm.demo.server.Datastore;
import com.google.gson.Gson;
import com.sos.gcm.GoogleCloudMessaging;

@Path ("/serviceclass")
public class MainController {

	//TODO Change to JSON objects from GSON
	
	//List<String> devices = new ArrayList<String>();
	@Path ("/gcmregister")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String gcmRegisterDevice(@QueryParam("deviceID") String deviceID){
		
		String emulatorId = "APA91bEcFh1-Kgq_rcnmonKKTYhiwIqSxuHsaN90DeuaQKpr08b4jSScLQmL2b1W4_KPKvpYqAzQaeJS_18efcLdh9CxMXJNd2pTx0VG1M97rI9okHvj2rCYmCvTjiRPkukk4TdMQxiy-LXhrKnyods5sEMYrOx65Q";
		String myId = "APA91bFYrg-EnRpTuwC1L9SugPGSZyLHCUNJDJnd2Iu7DVERg-F2TEC1DsF-i_INE0rYZQUsA4q1v_733RqwyDklfE8DLI5Y99jC1ef-sUa4I15Wb3Y20j0aevzZNRvYNgDG1Ir0JpdibWXLz4GGEQ_qX4fFJCg4Hw";
		//Datastore.register(myId);
		
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
	
	@Path ("/gcmsend")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String gcmsend(){
		
		Datastore.clearDevices();
		
		String emulatorId = "APA91bEcFh1-Kgq_rcnmonKKTYhiwIqSxuHsaN90DeuaQKpr08b4jSScLQmL2b1W4_KPKvpYqAzQaeJS_18efcLdh9CxMXJNd2pTx0VG1M97rI9okHvj2rCYmCvTjiRPkukk4TdMQxiy-LXhrKnyods5sEMYrOx65Q";
		String myId = "APA91bFYrg-EnRpTuwC1L9SugPGSZyLHCUNJDJnd2Iu7DVERg-F2TEC1DsF-i_INE0rYZQUsA4q1v_733RqwyDklfE8DLI5Y99jC1ef-sUa4I15Wb3Y20j0aevzZNRvYNgDG1Ir0JpdibWXLz4GGEQ_qX4fFJCg4Hw";
		Datastore.register(myId);
		Datastore.register(emulatorId);
		
		GoogleCloudMessaging testing = new GoogleCloudMessaging();
		try {
			
			return testing.sendMessage("Message testing" , Datastore.getDevices());
		} catch (IOException e) {
			
			return e.toString();
		}
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
