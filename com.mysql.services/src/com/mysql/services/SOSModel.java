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

import com.mysql.results.Standard;
import com.mysql.results.StandardResult;
import com.sos.gcm.GoogleCloudMessaging;

public class SOSModel
{
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
				"SELECT * FROM tags";

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
			String email, String deviceId){
		
		//create unique user ID
		String userId = createUserId();

		//query to insert into users table
		String query = 
				"INSERT INTO users (user_id, first_name, last_name, password, email, active, school, major)"
						+ " VALUES ('"+userId+"', '"+firstName+"', '"+lastName+"', '"+password+"', '"+email+"', True, "
						+ " null , null)";
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
	String query = "SELECT user_id, first_name, last_name, email, school, major "
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

		//TODO search for user by email and password
		String query = "SELECT * FROM users WHERE email= '" +email+"' and password= '"+password+"'";

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
		} catch (Exception e) {
			e.printStackTrace();
		}

		closeConnection();

		return finalResult;

	}
	public StandardResult forgotPassword(String email){

		//TODO search for user by email and password
		String query = "SELECT user_id, first_name, last_name FROM users WHERE email= '" +email+"'";

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
		} catch (Exception e) {
			e.printStackTrace();
		}

		closeConnection();

		return finalResult;

	}
	
	public StandardResult resetPassword(String email, String password){

		//TODO search for user by email and password
		String query = "UPDATE users SET password = '"+password+"' WHERE email = '"+email+"'";

		StandardResult finalResult = new StandardResult();

		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
			
			finalResult.setSuccess(1);

		}
		catch (SQLException error){
			System.out.println("Error executing query: " + error);
		} catch (Exception e) {
			e.printStackTrace();
		}

		closeConnection();

		return finalResult;

	}
	
	
	public StandardResult createQuestion(String userId, double latitude, double longitude, 
			String text, List<String> tags, int tutor, int studyGroup, String topic){

		//inserts new question into questions table
		String query = 
				"INSERT INTO questions (user_id, latitude, longitude, text, tutor, study_group, topic)"
				+ "VALUES ('"+userId+"', '"+latitude+"','"+longitude+"','"+text+"',"
						+ "'" +tutor+"','"+studyGroup+"', '"+topic+"')";
		

		StandardResult finalResult = new StandardResult();

		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
			
			JSONArray temp = new JSONArray();
			JSONArray tagtemp = new JSONArray();

			//get questionId to later use to add into question_tag table
			String getQuestionId=
					"SELECT question_id FROM questions"
					+ " WHERE user_id = '"+userId+"'";
			
			ResultSet rs = stmt.executeQuery(getQuestionId);
			temp = convertToJSON(rs);
			int questionId = temp.getJSONObject(0).getInt("question_id");
			
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
				"INSERT INTO members (question_id, user_id)"
					+ "VALUES ('"+questionId+"', '"+userId+"')";
			stmt.executeUpdate(addMember);
			
			
			finalResult.setSuccess(1);

		}
		catch (SQLException error){
			System.out.println("Error executing query, "+ error.getErrorCode()+" : " + error.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
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
					+ "q.latitude, q.longitude, q.topic FROM questions AS q INNER JOIN users as u ON u.user_id = q.user_id"
					+ " WHERE q.latitude BETWEEN "+minLat+" AND "+maxLat+"AND q.longitude BETWEEN "+minLong+" AND "+maxLong;
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
					+ " q.latitude, q.longitude, q.topic FROM questions AS q INNER JOIN question_tag AS qt "
					+ "ON q.question_id = qt.question_id INNER JOIN tags AS t on qt.tag_id = t.tag_id INNER JOIN users AS u ON "
					+ "u.user_id=q.user_id WHERE t.tag IN ("
					+alltags+" ) AND q.latitude BETWEEN "+minLat+" AND "+maxLat+" AND q.longitude BETWEEN "
					+minLong+" AND "+maxLong;
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
				+ "u.image, u.user_id, q.topic FROM questions as q INNER JOIN users as u ON q.user_id = u.user_id WHERE q.question_id = '"
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
	public StandardResult askToJoinGroup(int questionId, String userId) {
		//TODO showup user info 
		//TODO notify group leader. add asking userId to "waiting for response" list? (for UI)
		String query = "SELECT device_id FROM users JOIN questions ON (users.user_id = questions.user_id"
				+ " AND questions.question_id = "+questionId+")";

		String userInfo = "SELECT user_id, first_name, last_name, image FROM users WHERE user_id = '"+ userId;
		
		StandardResult finalResult = new StandardResult();
		JSONArray results;

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			results = convertToJSON(rs);
			
			finalResult.setSuccess(1);
			finalResult.setResult(results);
			finalResult.setExpectResults(results.length());
			
			//If we get here, we can send a notification
			//to group leader
			List<String> devices = null;
			//sendNotification( devices );
			

		}
		catch (Exception error){
			System.out.println("Error executing query: " + error);
		}

		closeConnection();
		return finalResult;
	}
	
	
	public StandardResult acceptUser(int questionId, String userId) {

		//TODO add userId to group member list and send notification
		String query = "INSERT INTO members (question_id, user_id) VALUES ("+questionId+",'"+userId+"')";
		
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
			//sendNotification( devices );
			

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


	/**
	 * Send a notification to a list of devices via GoogleCloudMessaging
	 * @param devices
	 */
	private void sendNotification(List<String> devices){

		GoogleCloudMessaging testing = new GoogleCloudMessaging();
		try {
			System.out.println( testing.sendMessage("Spaghetti Monster" , devices));
		} catch (IOException error) {
			System.out.println("Error executing query: " + error);
		}

	}





}
