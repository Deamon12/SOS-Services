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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.dto.Standard;
import com.mysql.dto.Tag;
import com.mysql.dto.User;
import com.mysql.results.StandardResult;
import com.sos.gcm.GoogleCloudMessaging;

public class SOSModel
{
	private Connection connection;

	public SOSModel(){
		openConnection();
	}

	public void openConnection(){
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
		try {
			connection.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}



	/**
	 * Get Tag List
	 * @return
	 */
	public StandardResult getTags() {

		String query = 
				"SELECT * FROM tags";

		StandardResult finalResult = new StandardResult();
		List <Standard> results;// = new ArrayList<Standard>();

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			results = convertToJSON(rs);
			
			finalResult.setSuccess(1);
			finalResult.setResult(results);
			if (results.size() > 0){
				finalResult.setExpectResults(1);
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
	 * @return
	 */
	public StandardResult createUser(String firstName, String lastName, String password, 
			String email , String deviceId){

		//TODO Check to see if this userId exists in database already?
		//TODO Save DeviceId
		String userId = createUserId();


		String query = 
				"INSERT INTO users (user_id, first_name, last_name, password, email, active, description, main_info)"
						+ " VALUES ('"+userId+"', '"+firstName+"', '"+lastName+"', '"+password+"', '"+email+"', True,"
						+ " 'empty until user adds?', 'UCSD, Computer Science')";

		//TODO Add (FROM) return or move return user info to another call?

		StandardResult finalResult = new StandardResult();
		List <Standard> results = new ArrayList<Standard>();		

		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
			ResultSet rs = stmt.executeQuery(query);   //TODO change to updateQuery of not returning result
			
			/*while (rs.next()){


				User temp = new User();

				temp.setUserID(rs.getString("user_id"));
				temp.setFirstName(rs.getString("first_name"));
				temp.setLastName(rs.getString("last_name"));
				temp.setEmail(rs.getString("email"));
				temp.setMainInfo(rs.getString("main_info"));

				results.add(temp);
			}*/
			results = convertToJSON(rs);


			finalResult.setSuccess(1);
			finalResult.setResult(results);
			if (results.size() > 0){
				finalResult.setExpectResults(1);
			}

		}
		catch (SQLException error){
			System.out.println("Error executing query, "+ error.getErrorCode()+" : " + error.getMessage());
			if(error.getErrorCode() == 1062){
				finalResult.setResult("Email address is already in use");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		closeConnection();

		return finalResult;
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public StandardResult getUserById(String userId){

		//TODO search by userId

		String query = "SELECT user_id, first_name, last_name, email, main_info "
				+ "FROM users";

		StandardResult finalResult = new StandardResult();
		List <Standard> results = new ArrayList<Standard>();

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			
			/*
			while (rs.next()){

				User temp = new User();

				temp.setUserID(rs.getString("user_id"));
				temp.setFirstName(rs.getString("first_name"));
				temp.setLastName(rs.getString("last_name"));
				temp.setEmail(rs.getString("email"));
				temp.setMainInfo(rs.getString("main_info"));

				results.add(temp);
			}*/
			
			results = convertToJSON(rs);
			

			finalResult.setSuccess(1);
			finalResult.setResult(results);
			if (results.size() > 0){
				finalResult.setExpectResults(1);
			}


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
	 * @return
	 */
	public StandardResult doLogin(String email, String password){

		//TODO search for user by email and password. Return entire user object?
		String query = "";

		StandardResult finalResult = new StandardResult();
		List <Standard> results = new ArrayList<Standard>();

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			results = convertToJSON(rs);
			
			finalResult.setSuccess(1);
			finalResult.setResult(results);
			if (results.size() > 0){
				finalResult.setExpectResults(1);
			}

		}
		catch (SQLException error){
			System.out.println("Error executing query: " + error);
		} catch (Exception e) {
			e.printStackTrace();
		}

		closeConnection();

		return finalResult;

	}



	public StandardResult getQuestions(String latitude, String longitude,  List<String> tags, int limit) {

		//TODO
		String query = "";

		StandardResult finalResult = new StandardResult();
		List <Standard> results;

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			results = convertToJSON(rs);
			
			finalResult.setSuccess(1);
			finalResult.setResult(results);
			if (results.size() > 0){
				finalResult.setExpectResults(1);
			}

		}
		catch (Exception error){
			System.out.println("Error executing query: " + error);
		}

		closeConnection();
		return finalResult;
	}


	
	/**
	 * Return question info
	 * @param questionId - unique question id
	 * @return 
	 */
	public StandardResult viewQuestion(String questionId) {

		//TODO
		String query = "";

		StandardResult finalResult = new StandardResult();
		List <Standard> results;

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			results = convertToJSON(rs);
			
			finalResult.setSuccess(1);
			finalResult.setResult(results);
			if (results.size() > 0){
				finalResult.setExpectResults(1);
			}

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
	public StandardResult askToJoinGroup(String questionId, String userId) {

		//TODO notify group leader. add asking userId to "waiting for response" list? (for UI)
		String query = "";

		StandardResult finalResult = new StandardResult();
		List <Standard> results;

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			results = convertToJSON(rs);
			
			finalResult.setSuccess(1);
			finalResult.setResult(results);
			if (results.size() > 0){
				finalResult.setExpectResults(1);
			}
			
			//If we get here, we can send a notification
			//to group leader
			List<String> devices = null;
			sendNotification( devices );
			

		}
		catch (Exception error){
			System.out.println("Error executing query: " + error);
		}

		closeConnection();
		return finalResult;
	}
	
	
	public StandardResult acceptUser(String questionId, String userId) {

		//TODO add userId to group membber list and send notification
		String query = "";

		StandardResult finalResult = new StandardResult();
		List <Standard> results;

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			results = convertToJSON(rs);
			
			finalResult.setSuccess(1);
			finalResult.setResult(results);
			if (results.size() > 0){
				finalResult.setExpectResults(1);
			}
			
			//If we get here, we can send a notification
			//to asking user
			List<String> devices = null;
			sendNotification( devices );
			

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
            jsonArray.add(obj);
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
