package com.mysql.services;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
	@Path ("/getTags")
	@Produces ("application/json")
	public String getTags(){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.getTags());
	}
	
	@GET
	@Path("/createUser")
	@Produces("application/json")
	public String createUser(){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.createUser());
	}

	@GET
	@Path ("/getUsers")
	@Produces ("application/json")
	public String getUsers(){
		
		SOSModel model = new SOSModel();
		return new Gson().toJson(model.getUsers());
	}
	
}
