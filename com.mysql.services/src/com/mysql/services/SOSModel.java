package com.mysql.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.dto.Comment;
import com.mysql.dto.Member;
import com.mysql.dto.Question_tag;
import com.mysql.dto.Question;
import com.mysql.dto.Standard;
import com.mysql.dto.Tag;
import com.mysql.dto.User;
import com.mysql.results.StandardResult;

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
			String connectionUser = "root";
			String connectionPassword = "";
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
	public StandardResult getTags(){

		String query = 
				"SELECT * FROM tags";
						

		StandardResult finalResult = new StandardResult();
		List <Standard> results = new ArrayList<Standard>();

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()){

				Tag temp = new Tag();

				temp.setTag(rs.getString("tag"));
				temp.setTagID(rs.getInt("tag_id"));

				results.add(temp);
			}

			finalResult.setSuccess(1);
			finalResult.setResult(results);
			if (results.size() > 0){
				finalResult.setExpectResults(1);
			}


		}
		catch (SQLException error){
			System.out.println("Error executing query: " + error);
		}


		closeConnection();

		return finalResult;
	}
	
	
	/**
	 * PUT create user
	 * @return
	 */
	public StandardResult createUser(){

		String query = 
				"INSERT INTO users (user_id, first_name, last_name, password, email, active, description, main_info)"
				+ "VALUES ('user1', 'Bob', 'Smith', 'asdf', 'aksdjf@ucsd.edu', True,"
				+ " 'soemthing osmething', 'UCSD, Computer Science')";
		
		
        StandardResult finalResult = new StandardResult();
		List <Standard> results = new ArrayList<Standard>();		

        try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);

			finalResult.setSuccess(1);
			finalResult.setResult(results);
			if (results.size() > 0){
				finalResult.setExpectResults(1);
			}

		}
		catch (SQLException error){
			System.out.println("Error executing query: " + error);
		}

		closeConnection();
		
		return finalResult;
	}
	
	/**
	 * GET all users
	 * @return 
	 */
	public StandardResult getUsers(){
		
		String query = "SELECT user_id, first_name, last_name, email, main_info "
				+ "FROM users";
		StandardResult finalResult = new StandardResult();
		List <Standard> results = new ArrayList<Standard>();

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()){

				User temp = new User();

				temp.setUserID(rs.getString("user_id"));
				temp.setFirstName(rs.getString("first_name"));
				temp.setLastName(rs.getString("last_name"));
				temp.setEmail(rs.getString("email"));
				temp.setMainInfo(rs.getString("main_info"));

				results.add(temp);
			}

			finalResult.setSuccess(1);
			finalResult.setResult(results);
			if (results.size() > 0){
				finalResult.setExpectResults(1);
			}


		}
		catch (SQLException error){
			System.out.println("Error executing query: " + error);
		}


		closeConnection();

		return finalResult;
		
	}
	 
}
