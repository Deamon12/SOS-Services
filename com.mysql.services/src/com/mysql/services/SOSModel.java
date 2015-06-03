package com.mysql.services;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mysql.results.StandardResult;
import com.sos.gcm.GoogleCloudMessaging;
import com.mysql.services.Utilities;
import com.mysql.services.Utilities.Encrypt;

public class SOSModel
{
	private final int addMember = 1;
	private final int addTutor = 2;
	private final int acceptUser = 3;
	private final int deleteUser = 4;
	private Connection connection;

	public SOSModel(){
		openConnection();
	}

	public void openConnection(){
		//open database connection
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String connectionUrl = "jdbc:mysql://localhost:3306/sosdb";
			String connectionUser = "sosadmin";
			String connectionPassword = "password";
			connection = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);

			System.out.println("Connected to DB");

		}catch (SQLException error){
			System.out.print("Error connecting to database: " + error);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void closeConnection(){
		//close database connection
		try {
			connection.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}



	/**
	 * Get Tag List
	 * @return tag_id, tag
	 */
	public StandardResult getTags() {
		//get all tag names and tag id's
		String query = 
				"SELECT * FROM tags ORDER BY tag_id DESC";

		StandardResult finalResult = new StandardResult();
		JSONArray results;

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			results = convertToJSON(rs);
			
			finalResult.setSuccess(1);
			finalResult.setResult(results);
			if (results.length() > 0){
				finalResult.setExpectResults(results.length());
			}

		}
		catch (Exception error){
			System.out.println("Error executing query: " + error);

			finalResult.setResult(error.getMessage());
		}

		closeConnection();
		return finalResult;
	}
	
	
	public StandardResult getRateList(String userId) {
		String query = 
				"SELECT rated_user, rating FROM ratings WHERE user_id = '"+userId+"'";

		StandardResult finalResult = new StandardResult();
		JSONArray results;

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			results = convertToJSON(rs);
			
			finalResult.setSuccess(1);
			finalResult.setResult(results);
			finalResult.setExpectResults(results.length());

		}
		catch (Exception error){
			System.out.println("Error executing query: " + error);

			finalResult.setResult(error.getMessage());
		}

		closeConnection();
		return finalResult;
	}



	/**
	 * If successful creation return user object. So app can use it to continue to login.
	 * If unsuccessful creation return what the error is. ie account exists, email exists..
	 * 
	 * @param firstName
	 * @param lastName
	 * @param password
	 * @param email
	 * @return success or failure
	 */
	public StandardResult createUser(String firstName, String lastName, String password, 
			String email, String image, String deviceId){
		
		//create unique user ID
		String userId = createUserId();
		String query = "";
		//query to insert into users table with image
		if (image!=null){
                query = 
                        "INSERT INTO users (user_id, first_name, last_name, password, email, active, school, major, image)"
                                + " VALUES ('"+userId+"', '"+firstName+"', '"+lastName+"', '"+password+"', '"+email+"', True, "
                                + " '' , '', '" +image+"')";
		}
		else{
                query = 
					"INSERT INTO users (user_id, first_name, last_name, password, email, active, school, major)"
							+ " VALUES ('"+userId+"', '"+firstName+"', '"+lastName+"', '"+password+"', '"+email+"', True, "
							+ " '' , '')";
		}
		//query to insert into device for push notifications
		String device= 
				"INSERT INTO device (device_id, user_id)"
				+ "VALUES ('"+deviceId+"', '"+userId+"')";
				

		StandardResult finalResult = new StandardResult();	

		try{
			//execute both queries to update database
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
			stmt.executeUpdate(device);


			finalResult.setSuccess(1);
			finalResult.setResult("successfully created account");

		}
		catch (SQLException error){
			System.out.println("Error executing query, "+ error.getErrorCode()+" : " + error.getMessage());
			
			finalResult.setResult(error.getMessage());
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		closeConnection();

		return finalResult;
	}

	/**
	 * 
	 * @param userId
	 * @return user_id, first_name, last_name, email, school, major
	 */
	public StandardResult getUserById(String userId){

	//search by userId
	String query = "SELECT user_id, first_name, last_name, email, school, major, description, rating, image "
				+ "FROM users WHERE user_id='"+userId+"'";
		
		
		StandardResult finalResult = new StandardResult();
		JSONArray results;

		try{
			
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			results = convertToJSON(rs);
			

			finalResult.setSuccess(1);
			finalResult.setResult(results);
			finalResult.setExpectResults(results.length());
			

		}
		catch (SQLException error){
			System.out.println("Error executing query: " + error);
			finalResult.setResult(error.getMessage());
		} catch (Exception e) {
			
			e.printStackTrace();
		}


		closeConnection();

		return finalResult;

	}

	public StandardResult updateProfile(String userId, String firstName, String lastName, String school, String major, 
			String description, String image){

        String query = "UPDATE users SET first_name = '"+firstName+"', last_name ='"+lastName+"', school ='"+school+"', "
			+ "major= '"+major+"', description='"+description+"', image = '"+image+"' WHERE user_id = '"+userId+"'";
		
		
		StandardResult finalResult = new StandardResult();
		JSONArray results;

		try{
			
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
			
			finalResult.setSuccess(1);
			

		}
		catch (SQLException error){
			System.out.println("Error executing query: " + error);
			finalResult.setResult(error.getMessage());
		} catch (Exception e) {
			
			e.printStackTrace();
		}


		closeConnection();

		return finalResult;

	}

	/**
	 * @param email
	 * @param password
	 * @return single row or empty array if no row exists
	 */
	public StandardResult doLogin(String email, String password){

		String query = "SELECT user_id, first_name, last_name, date, image, email, active, rating, "
				+ "description, school, major FROM users WHERE email= '" +email+"' and password= '"+password+"'";

		StandardResult finalResult = new StandardResult();
		JSONArray results;

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			results = convertToJSON(rs);

			finalResult.setSuccess(1);
			finalResult.setResult(results);
			finalResult.setExpectResults(results.length());
			

		}
		catch (SQLException error){
			System.out.println("Error executing query: " + error);
			finalResult.setResult(error.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		closeConnection();

		return finalResult;

	}
	public StandardResult forgotPassword(String email){

		String newpass = createPassword();
		String securePassword = Encrypt.get_SHA_1_SecurePassword(newpass);
		System.out.println(newpass);
		System.out.println(securePassword);
		String getUser = "SELECT user_id, first_name, last_name FROM users WHERE email ='"+email+"'";
		String updatePassword = "UPDATE users SET password = '"+securePassword+"' WHERE email= '"+email+"'";
		StandardResult finalResult = new StandardResult();
		JSONArray results;
		
		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(getUser);
			results = convertToJSON(rs);

			stmt.executeUpdate(updatePassword);

			
			finalResult.setSuccess(1);
			finalResult.setResult(results);
			finalResult.setExpectResults(results.length());

		}
		catch (SQLException error){
			System.out.println("Error executing query: " + error);
			finalResult.setResult(error.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Utilities.sendEmail(email, newpass);

		closeConnection();

		return finalResult;

	}
	
	public StandardResult resetPassword(String userId, String password){

		//TODO search for user by email and password
		String query = "UPDATE users SET password = '"+password+"' WHERE user_id= '"+userId+"'";

		StandardResult finalResult = new StandardResult();

		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
			
			finalResult.setSuccess(1);

		}
		catch (SQLException error){
			System.out.println("Error executing query: " + error);
			finalResult.setResult(error.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		closeConnection();

		return finalResult;

	}

	public StandardResult resetEmail(String email, String userId){

		//TODO search for user by email and password
		String query = "UPDATE users SET email = '"+email+"' WHERE user_id = '"+userId+"'";

		StandardResult finalResult = new StandardResult();

		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
			
			finalResult.setSuccess(1);

		}
		catch (SQLException error){
			System.out.println("Error executing query: " + error);
			finalResult.setResult(error.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		closeConnection();

		return finalResult;

	}	
	
	public StandardResult inGroup(String userId){

		//TODO search for user by email and password
		String query = "SELECT question_id FROM members WHERE user_id= '" +userId+"'";

		StandardResult finalResult = new StandardResult();
		JSONArray results;

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			results = convertToJSON(rs);
			
			finalResult.setSuccess(1);
			finalResult.setResult(results);
			finalResult.setExpectResults(results.length());
			

		}
		catch (SQLException error){
			System.out.println("Error executing query: " + error);
			finalResult.setResult(error.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		closeConnection();

		return finalResult;

	}
	
	public StandardResult hasQuestion(String userId){

		//TODO search for user by email and password
		String query = "SELECT question_id FROM questions WHERE user_id= '" +userId+"'";

		StandardResult finalResult = new StandardResult();
		JSONArray results;

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			results = convertToJSON(rs);
			
			finalResult.setSuccess(1);
			finalResult.setResult(results);
			finalResult.setExpectResults(results.length());
			

		}
		catch (SQLException error){
			System.out.println("Error executing query: " + error);
			finalResult.setResult(error.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		closeConnection();

		return finalResult;

	}
	
	public StandardResult createQuestion(String userId, double latitude, double longitude, 
			String text, List<String> tags, int tutor, int studyGroup, String topic, int visibleLocation){

		//inserts new question into questions table
		String query = 
				"INSERT INTO questions (user_id, latitude, longitude, text, tutor, study_group, topic, visible_location)"
				+ "VALUES ('"+userId+"', '"+latitude+"','"+longitude+"','"+text+"',"
						+ "'" +tutor+"','"+studyGroup+"', '"+topic+"', "+visibleLocation+")";
		

		StandardResult finalResult = new StandardResult();

		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
			
			JSONArray temp = new JSONArray();
			JSONArray question = new JSONArray();
			JSONArray tagtemp = new JSONArray();

			//get questionId to later use to add into question_tag table
			String getQuestionId=
					"SELECT question_id FROM questions"
					+ " WHERE user_id = '"+userId+"'";
			
			ResultSet rs = stmt.executeQuery(getQuestionId);
			question = convertToJSON(rs);
			int questionId = question.getJSONObject(0).getInt("question_id");
			
			String alltags = "";
			for (int a=0; a<tags.size(); a++){
                //if tags is not in tag table then add to tag table
				String tagExist = 
						"SELECT tag_id FROM tags WHERE tag = '"+tags.get(a)+"'";
				rs = stmt.executeQuery(tagExist);
				temp = convertToJSON(rs);
				if(temp.length()==0){
					String insertTag = 
							"INSERT INTO tags (tag) VALUES ('"+tags.get(a)+"')";
					stmt.executeUpdate(insertTag);
				}
				if (a == 0){
					alltags = "'"+tags.get(a)+"'";
				}
				alltags = alltags+", '"+tags.get(a)+"'";
			}

			if(tags.size()>0){
			//get all tag_id's for the tags that are selected
			String getTagId = 
                        "SELECT tag_id FROM tags WHERE tag IN ("+alltags+")";
				rs = stmt.executeQuery(getTagId);
				tagtemp = convertToJSON(rs);
			}
				
			//link question to tags
			for (int j=0; j<tagtemp.length(); j++){
				JSONObject obj = tagtemp.getJSONObject(j);
				int id = obj.getInt("tag_id");
			    
				String insertQuestionTag = 
						"INSERT INTO question_tag (question_id, tag_id)"
						+ "VALUES('"+questionId+"','"+id+"')";
				stmt.executeUpdate(insertQuestionTag);
				
			}

			String addMember = 
				"INSERT INTO members (question_id, user_id, tutor)"
					+ "VALUES ('"+questionId+"', '"+userId+"', "+0+")";
			stmt.executeUpdate(addMember);
			
			
			finalResult.setSuccess(1);
			finalResult.setResult(question);
			finalResult.setExpectResults(question.length());

		}
		catch (SQLException error){
			System.out.println("Error executing query, "+ error.getErrorCode()+" : " + error.getMessage());
			finalResult.setResult(error.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
		}

		closeConnection();

		return finalResult;
	}
	
	public StandardResult removeAllTags(int questionId){

		String query = "DELETE FROM question_tag WHERE question_id = "+questionId;

		StandardResult finalResult = new StandardResult();

		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);

			finalResult.setSuccess(1);

		}
		catch (SQLException error){
			System.out.println("Error executing query: " + error);
			finalResult.setResult(error.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		closeConnection();

		return finalResult;

	}
	
	public StandardResult editQuestion(int questionId, String text, List<String> tags, int tutor,
			int studyGroup, String topic, int visibleLocation){

		//inserts new question into questions table
		String edit = 
				"UPDATE questions SET text = '"+text+"', tutor = "+tutor+", study_group="+studyGroup+","
						+ "topic='"+topic+"', visible_location="+visibleLocation+" WHERE question_id = "+questionId;

		String tag= "DELETE FROM question_tag WHERE question_id = "+questionId;

		StandardResult finalResult = new StandardResult();

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs;
			stmt.executeUpdate(tag);
			stmt.executeUpdate(edit);
			
			JSONArray temp = new JSONArray();
			JSONArray tagtemp = new JSONArray();

			String alltags = "";
			for (int a=0; a<tags.size(); a++){
                //if tags is not in tag table then add to tag table
				String tagExist = 
						"SELECT tag_id FROM tags WHERE tag = '"+tags.get(a)+"'";
				rs = stmt.executeQuery(tagExist);
				temp = convertToJSON(rs);
				if(temp.length()==0){
					String insertTag = 
							"INSERT INTO tags (tag) VALUES ('"+tags.get(a)+"')";
					stmt.executeUpdate(insertTag);
				}
				if (a == 0){
					alltags = "'"+tags.get(a)+"'";
				}
				alltags = alltags+", '"+tags.get(a)+"'";
			}

			if(tags.size()>0){
			//get all tag_id's for the tags that are selected
			String getTagId = 
                        "SELECT tag_id FROM tags WHERE tag IN ("+alltags+")";
				rs = stmt.executeQuery(getTagId);
				tagtemp = convertToJSON(rs);
			}
				
			//link question to tags
			for (int j=0; j<tagtemp.length(); j++){
				JSONObject obj = tagtemp.getJSONObject(j);
				int id = obj.getInt("tag_id");
			    
				String insertQuestionTag = 
						"INSERT INTO question_tag (question_id, tag_id)"
						+ "VALUES('"+questionId+"','"+id+"')";
				stmt.executeUpdate(insertQuestionTag);
				
			}

			finalResult.setSuccess(1);

		}
		catch (SQLException error){
			System.out.println("Error executing query, "+ error.getErrorCode()+" : " + error.getMessage());
			finalResult.setResult(error.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
		}

		closeConnection();

		return finalResult;
	}
	
	public StandardResult editLocation(int questionId, double latitude, double longitude){

		String query = "UPDATE questions SET latitude = "+latitude+", longitude = "+longitude
				+ "WHERE question_id ="+questionId;

		StandardResult finalResult = new StandardResult();

		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);

			finalResult.setSuccess(1);

		}
		catch (SQLException error){
			System.out.println("Error executing query: " + error);
			finalResult.setResult(error.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		closeConnection();

		return finalResult;

	}
	
	public StandardResult changeOwner(int questionId, String userId) {

		String query = "UPDATE questions SET user_id= '"+userId+"' WHERE question_id = "+questionId;
		
		StandardResult finalResult = new StandardResult();

		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);

			finalResult.setSuccess(1);
		}
		catch (SQLException error){
			System.out.println("Error executing query, "+ error.getErrorCode()+" : " + error.getMessage());
			finalResult.setResult(error.getMessage());
		}
		catch (Exception error){
			System.out.println("Error executing query: " + error);
		}

		closeConnection();
		return finalResult;
	}	
	
	public StandardResult setVisibility(int questionId, int visible) {

		String query = "UPDATE questions SET visible_location = "+visible+" WHERE question_id = "+questionId;
		
		StandardResult finalResult = new StandardResult();

		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);

			finalResult.setSuccess(1);
		}
		catch (SQLException error){
			System.out.println("Error executing query, "+ error.getErrorCode()+" : " + error.getMessage());
			finalResult.setResult(error.getMessage());
		}
		catch (Exception error){
			System.out.println("Error executing query: " + error);
		}

		closeConnection();
		return finalResult;
	}	
	

	public StandardResult getQuestions(double latitude, double longitude,  List<String> tags, double limit) {

		//get boundaries for latitude/longitude
		double minLat = latitude - Utilities.distLat(limit);
		double maxLat = latitude + Utilities.distLat(limit);
		double minLong = longitude - Utilities.distLong(limit);
		double maxLong = longitude + Utilities.distLong(limit);
		
		//execute query for if there is tag filter or if not tag filter
		String query = "";
		if(tags.isEmpty()){
			query = "SELECT q.question_id, q.text, q.date, q.topic, u.user_id, u.first_name, u.last_name, u.image,"
					+ "q.latitude, q.longitude, q.topic, q.tutor, q.study_group, q.visible_location FROM questions AS q"
					+ " INNER JOIN users as u ON u.user_id = q.user_id"
					+ " WHERE q.latitude BETWEEN "+minLat+" AND "+maxLat+"AND q.longitude BETWEEN "+minLong+" AND "+maxLong
					+ "AND q.active = 1 ORDER BY date DESC";
		}
		else{
			String alltags = "";
			for (int a=0; a<tags.size(); a++){
				if (a == 0){
					alltags = "'"+tags.get(a)+"'";
				}
				alltags = alltags+", '"+tags.get(a)+"'";
			}
			query = "SELECT DISTINCT q.question_id, q.text, q.date, q.topic, u.user_id, u.first_name, u.last_name, u.image,"
					+ " q.latitude, q.longitude, q.topic, q.tutor, q.study_group, q.visible_location FROM questions AS q"
					+ " INNER JOIN question_tag AS qt "
					+ "ON q.question_id = qt.question_id INNER JOIN tags AS t on qt.tag_id = t.tag_id INNER JOIN users AS u ON "
					+ "u.user_id=q.user_id WHERE t.tag IN ("
					+alltags+" ) AND q.latitude BETWEEN "+minLat+" AND "+maxLat+" AND q.longitude BETWEEN "
					+minLong+" AND "+maxLong+ " AND q.active = 1 ORDER BY date DESC";
		}

		StandardResult finalResult = new StandardResult();
		JSONArray results;

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			results = convertToJSON(rs);
			
			finalResult.setSuccess(1);
			finalResult.setResult(results);
			finalResult.setExpectResults(results.length());

		}
		catch (SQLException error){
			System.out.println("Error executing query, "+ error.getErrorCode()+" : " + error.getMessage());
			finalResult.setResult(error.getMessage());
		}

		catch (Exception error){
			System.out.println("Error executing query: " + error);
		}

		closeConnection();
		return finalResult;
	}

	
	/**
	 * Return question info
	 * @param question - unique question id
	 * @return question info and user info and tags associated with question
	 */
	public StandardResult viewQuestion(int questionId) {

		//get question info
		String query = "SELECT q.date, q.study_group, q.latitude, q.longitude, q.active, q.text, q.tutor, u.first_name, u.last_name,"
				+ "u.image, u.user_id, q.topic, q.visible_location FROM questions as q INNER JOIN users as u ON q.user_id = u.user_id WHERE q.question_id = '"
				+questionId+"'";
		//get each separate tag associated with question
		String getTags = "SELECT tags.tag FROM tags INNER JOIN question_tag ON question_tag.tag_id = tags.tag_id"
				+ " WHERE question_tag.question_id = '"+questionId+"'";
		
		StandardResult finalResult = new StandardResult();
		JSONArray results;
		JSONArray tagResults;

		try{
			//concatenate all the question info and tag info into 1 JSONArray
			Statement stmt = connection.createStatement();
			Statement stmt1 = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			ResultSet tagsRs = stmt1.executeQuery(getTags);

			results = convertToJSON(rs);
			tagResults = convertToJSON(tagsRs);
			
			for (int i=0; i<tagResults.length(); i++){
				results.put(tagResults.get(i));
			}
			
			
			finalResult.setSuccess(1);
			finalResult.setResult(results);
			finalResult.setExpectResults(results.length());

		}
		catch (SQLException error){
			System.out.println("Error executing query, "+ error.getErrorCode()+" : " + error.getMessage());
			finalResult.setResult(error.getMessage());
		}

		catch (Exception error){
			System.out.println("Error executing query: " + error);
		}

		closeConnection();
		return finalResult;
	}
	
	public StandardResult getComments(int questionId) {

		String query = "SELECT u.user_id, u.first_name, u.last_name, u.image, c.comment, c.posted FROM comments AS c INNER JOIN "
				+ "users AS u ON c.user_id = u.user_id WHERE c.question_id= '"+questionId+"' ORDER BY posted ASC";
		
		StandardResult finalResult = new StandardResult();
		JSONArray results;

		try{
			//concatenate all the question info and tag info into 1 JSONArray
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			results = convertToJSON(rs);
			
			finalResult.setSuccess(1);
			finalResult.setResult(results);
			finalResult.setExpectResults(results.length());

		}
		catch (SQLException error){
			System.out.println("Error executing query, "+ error.getErrorCode()+" : " + error.getMessage());
			finalResult.setResult(error.getMessage());
		}

		catch (Exception error){
			System.out.println("Error executing query: " + error);
		}

		closeConnection();
		return finalResult;
	}
	
	public StandardResult addComment(int questionId, String userId, String comment) {

		//get question info
		String query = "INSERT INTO comments (question_id, user_id, comment) VALUES ("+questionId+", '"+userId+"', '"+comment+"')"; 
		
		StandardResult finalResult = new StandardResult();

		try{
			//concatenate all the question info and tag info into 1 JSONArray
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);

			finalResult.setSuccess(1);
			

		}
		catch (SQLException error){
			System.out.println("Error executing query, "+ error.getErrorCode()+" : " + error.getMessage());
			finalResult.setResult(error.getMessage());
		}

		catch (Exception error){
			System.out.println("Error executing query: " + error);
		}

		closeConnection();
		return finalResult;
	}
	
	
	/**
	 * This is called when a person wants to join a group. Initiates notification scheme.
	 * This will be followed by a call to "joinGroup" if leader accepts this userId
	 * @param questionId
	 * @param userId
	 * @return 
	 */
	public StandardResult askToJoinGroup(int questionId, String userId, int tutor) {
		//TODO show up user info 
		//TODO notify group leader. add asking userId to "waiting for response" list? (for UI)
		String query = "SELECT d.device_id FROM device as d JOIN users as u ON u.user_id = d.user_id"
				+ " JOIN questions AS q ON (u.user_id = q.user_id AND q.question_id = "+questionId+")";

		String userInfo = "SELECT user_id, first_name, last_name, image FROM users WHERE user_id = '"+ userId+"'";
		
		StandardResult finalResult = new StandardResult();
		JSONArray results;
		JSONArray userResults;
        List<String> devices = new ArrayList();

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			Statement stmt1 = connection.createStatement();
			ResultSet rs1 = stmt1.executeQuery(userInfo);

			results = convertToJSON(rs);
			
			for(int a=0; a<results.length(); a++){
				devices.add(results.getJSONObject(a).getString("device_id"));
			}
			userResults = convertToJSON(rs1);
			
			for(int i=0; i<userResults.length(); i++){
				results.put(userResults.get(i));
			}
			
			JSONObject tut = new JSONObject();
			tut.put("tutor", tutor);
			results.put(tut);
			
			finalResult.setSuccess(1);
			finalResult.setResult(results);
			finalResult.setExpectResults(results.length());
			
			//If we get here, we can send a notification
			//to group leader
			if(tutor==1){
				sendNotification(devices, userId, addTutor, ""+questionId);
			}
			else{
				sendNotification(devices, userId, addMember, ""+questionId);
			}

		}
		catch (SQLException error){
			System.out.println("Error executing query, "+ error.getErrorCode()+" : " + error.getMessage());
			finalResult.setResult(error.getMessage());
		}

		catch (Exception error){
			System.out.println("Error executing query: " + error);
		}

		closeConnection();
		return finalResult;
	}
	
	
	public StandardResult acceptUser(int questionId, String userId, int tutor) {

		//TODO add userId to group member list and send notification
		String query = "INSERT INTO members (question_id, user_id, tutor) VALUES ("+questionId+",'"+userId+"', "+tutor+")";
		
		String getDevice = "SELECT device_id FROM device WHERE user_id= '"+userId+"'";

		StandardResult finalResult = new StandardResult();
        List<String> devices = new ArrayList();

		try{ 
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
			ResultSet rs = stmt.executeQuery(getDevice);

			while(rs.next()){
				devices.add(rs.getString("device_id"));
			}
		
			finalResult.setSuccess(1);
			
			//send list of devices to sendNotification
			System.out.println(devices);
			sendNotification(devices, userId, acceptUser, ""); 

		}
		catch (SQLException error){
			System.out.println("Error executing query, "+ error.getErrorCode()+" : " + error.getMessage());
			finalResult.setResult(error.getMessage());
		}

		catch (Exception error){
			System.out.println("Error executing query: " + error);
		}

		closeConnection();
		return finalResult;
	}
	
	public StandardResult removeUser(String userId) {

		//TODO add userId to group member list and send notification
		String query = "DELETE FROM members WHERE user_id = '"+userId+"'";
		
		String getDevice = "SELECT device_id FROM device WHERE user_id= '"+userId+"'";

		StandardResult finalResult = new StandardResult();
        List<String> devices = new ArrayList();

		try{ 
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
			ResultSet rs = stmt.executeQuery(getDevice);

			while(rs.next()){
				devices.add(rs.getString("device_id"));
			}
		
			finalResult.setSuccess(1);
			
			//send list of devices to sendNotification
			System.out.println(devices);
			sendNotification(devices, userId, deleteUser, "");
			

		}
		catch (SQLException error){
			System.out.println("Error executing query, "+ error.getErrorCode()+" : " + error.getMessage());
			finalResult.setResult(error.getMessage());
		}

		catch (Exception error){
			System.out.println("Error executing query: " + error);
		}

		closeConnection();
		return finalResult;
	}
	
	public StandardResult viewMembers(int questionId) {

		//get question info
		String query = "SELECT u.user_id, u.first_name, u.last_name, u.image,  m.tutor FROM members AS m INNER JOIN users AS u"
				+ " ON m.user_id = u.user_id WHERE m.question_id = "+questionId;
		
		StandardResult finalResult = new StandardResult();
		JSONArray results;

		try{
			//concatenate all the question info and tag info into 1 JSONArray
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			results = convertToJSON(rs);
			
			finalResult.setSuccess(1);
			finalResult.setResult(results);
			finalResult.setExpectResults(results.length());

		}
		catch (SQLException error){
			System.out.println("Error executing query, "+ error.getErrorCode()+" : " + error.getMessage());
			finalResult.setResult(error.getMessage());
		}

		catch (Exception error){
			System.out.println("Error executing query: " + error);
		}

		closeConnection();
		return finalResult;
	}
	
	public StandardResult openGroup(int questionId){

		//remove question from questions, remove group members from members, remove tags related to question
		String activate = "UPDATE questions SET active = 1 WHERE question_id = '"+questionId+"'";

		StandardResult finalResult = new StandardResult();

		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(activate);
			
			finalResult.setSuccess(1);

		}
		catch (SQLException error){
			System.out.println("Error executing query, "+ error.getErrorCode()+" : " + error.getMessage());
			finalResult.setResult(error.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		closeConnection();

		return finalResult;

	}
	
	public StandardResult closeGroup(int questionId){

		//remove question from questions, remove group members from members, remove tags related to question
		String inactivate = "UPDATE questions SET active = 0 WHERE question_id = '"+questionId+"'";

		StandardResult finalResult = new StandardResult();

		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(inactivate);
			
			finalResult.setSuccess(1);

		}
		catch (SQLException error){
			System.out.println("Error executing query, "+ error.getErrorCode()+" : " + error.getMessage());
			finalResult.setResult(error.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		closeConnection();

		return finalResult;

	}
	
	public StandardResult removeGroup(int questionId){

		//remove question from questions, remove group members from members, remove tags related to question
		String members = "DELETE FROM members WHERE question_id = "+questionId;
		String tags = "DELETE FROM question_tag WHERE question_id = "+questionId;
		String question = "DELETE FROM questions WHERE question_id = "+questionId;
		String comments = "DELETE FROM comments WHERE question_id = "+questionId;

		StandardResult finalResult = new StandardResult();

		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(members);
			stmt.executeUpdate(tags);
			stmt.executeUpdate(comments);
			stmt.executeUpdate(question);
			
			finalResult.setSuccess(1);

		}
		catch (SQLException error){
			System.out.println("Error executing query: " + error);
			finalResult.setResult(error.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		closeConnection();

		return finalResult;

	}
	
	public StandardResult rateTutor(String userId, String ratedUserId, int like, boolean rated) {

		//TODO add userId to group member list and send notification
		String getRating= "SELECT rating FROM users WHERE user_id = '"+ratedUserId+"'";
		
		String updateRatingsTable; 
		if (rated == true){
			updateRatingsTable = "UPDATE ratings SET rating = "+like+" WHERE user_id = '"+userId+"' AND rated_user = '"+ratedUserId+"'";
		}
		else{
			updateRatingsTable = "INSERT INTO ratings (rating, user_id, rated_user)"
					+ "VALUES("+like+", '"+userId+"', '"+ratedUserId+"')";
		}
		
		StandardResult finalResult = new StandardResult();

		try{ 
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(updateRatingsTable);
			ResultSet rs = stmt.executeQuery(getRating);
			
			rs.first();
			int currRating = rs.getInt("rating");
			System.out.println(currRating);
			
			currRating = currRating + like;
			System.out.println(currRating);
			
			String setRating = "UPDATE users SET rating = "+currRating+" WHERE user_id = '"+ratedUserId+"'";
			stmt.executeUpdate(setRating);
		
			finalResult.setSuccess(1);
			
		}
		catch (SQLException error){
			System.out.println("Error executing query, "+ error.getErrorCode()+" : " + error.getMessage());
			finalResult.setResult(error.getMessage());
		}

		catch (Exception error){
			System.out.println("Error executing query: " + error);
		}

		closeConnection();
		return finalResult;
	}

	public static JSONArray convertToJSON(ResultSet resultSet) throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < total_rows; i++) {
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
                        .toLowerCase(), resultSet.getObject(i + 1));
            }
            jsonArray.put(obj);
        }
        return jsonArray;
    }



	public static String createUserId(){
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}
	
	public static String createPassword(){
		SecureRandom random = new SecureRandom();
		return new BigInteger(40, random).toString(32);
	}


	/**
	 * Send a notification to a list of devices via GoogleCloudMessaging
	 * @param devices
	 */
	private void sendNotification(List<String> devices, String userId, int type, String questionId){

		GoogleCloudMessaging testing = new GoogleCloudMessaging();
		try {
			System.out.println( testing.sendMessage(questionId, type, userId, devices));
		} catch (IOException error) {
			System.out.println("Error executing query: " + error);
		}

	}
	

}
