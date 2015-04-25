package com.mysql.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.dto.ChildComment;
import com.mysql.dto.VenueComment;
import com.mysql.dto.Profile;
import com.mysql.dto.Standard;
import com.mysql.dto.Venue;
import com.mysql.dto.VenueComment;
import com.mysql.results.StandardResult;


public class LocalLiveSpotModel
{
	private Connection connection;

	public LocalLiveSpotModel(){
		openConnection();
	}


	public void openConnection(){
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String connectionUrl = "jdbc:mysql://localhost:3306/LiveSpotDB";
			String connectionUser = "livespotuser";
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
	 * Get Venue List
	 * @return
	 */
	public StandardResult getVenues(){

		String query = 
				"SELECT venues.locationID, venues.name, venues.city, venues.state, venues.stateAbrev "+
						"FROM venues " +
						"ORDER BY venues.name";

		StandardResult finalResult = new StandardResult();
		List <Standard> results = new ArrayList<Standard>();

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()){

				Venue temp = new Venue();

				temp.setLocation(rs.getString("locationID"));
				temp.setName(rs.getString("name"));
				temp.setCity(rs.getString("city"));
				temp.setState(rs.getString("state"));
				temp.setStateAbrev(rs.getString("stateAbrev"));

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

	
	public StandardResult getProfileData(String userID){

		String query = "SELECT feeds.id as feedID, feeds.date, feeds.text, feeds.img as feedImg, feeds.active, feeds.locationID, feeds.twitterFeedID," +
	    		"users.userID, users.first, users.last, users.img " +
	    		"FROM feeds " +
	    		"JOIN (users) ON (feeds.img != '' AND feeds.userID = '"+ userID + "' AND users.userID = feeds.userID)  " +
	    		"ORDER BY feeds.date DESC";
		
		
		StandardResult finalResult = new StandardResult();
		ArrayList <VenueComment> imageList = new ArrayList<VenueComment>();
		Profile profileTemp = new Profile();
		
		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			
			while (rs.next()){
				//Eh
				profileTemp.setFirst(rs.getString("first"));
				profileTemp.setLast(rs.getString("last"));
				profileTemp.setImg(rs.getString("img"));
				
				
				VenueComment imageTemp = new VenueComment();
				imageTemp.setId(rs.getString("feedID"));
				imageTemp.setActive(rs.getString("active"));
				imageTemp.setDate(rs.getString("date"));
				imageTemp.setImg(rs.getString("feedImg"));
				imageTemp.setLocationID(rs.getString("locationID"));
				imageTemp.setText(rs.getString("text"));
				imageTemp.setUserID(rs.getString("userID"));
				imageTemp.setTwitterFeedID(rs.getString("twitterFeedID"));
				
				
				//System.out.println(imageTemp.toString());
				
				imageList.add(imageTemp);
			}
			
			//System.out.println("profile: "+profileTemp.toString());
			finalResult.setSuccess(1);
			profileTemp.setImages(imageList);
		}
		catch (SQLException error){
			System.out.println("Error executing query: " + error);
		}

		if (imageList.size() > 0)
			finalResult.setExpectResults(1);

		finalResult.setResult(profileTemp);

		closeConnection();

		return finalResult;
	}


	public StandardResult getPhotosByUser(String userID){

		String query = "SELECT * FROM feeds WHERE userID = '" + userID + "'";
		
		StandardResult finalResult = new StandardResult();
		ArrayList <Standard> results = new ArrayList<Standard>();

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()){

				VenueComment temp = new VenueComment();

				temp.setUserID(rs.getString("userID"));
				temp.setLocationID(rs.getString("locationID"));
				temp.setImg(rs.getString("img"));
				temp.setText(rs.getString("text"));
				temp.setActive(rs.getString("active"));
				temp.setDate(rs.getString("date"));
				temp.setTwitterFeedID(rs.getString("twitterFeedID"));

				results.add(temp);
			}

			finalResult.setSuccess(1);

		}
		catch (SQLException error){
			System.out.println("Error executing query: " + error);
		}

		if (results.size() > 0)
			finalResult.setExpectResults(1);

		finalResult.setResult(results);

		closeConnection();

		return finalResult;
	}

	/*
	public StandardResult getVenueComments(){

		String query = 
				"SELECT venues.locationID, venues.name, venues.city, venues.state, venues.stateAbrev "+
						"FROM venues " +
						"ORDER BY venues.name";

		StandardResult finalResult = new StandardResult();
		List <VenueComment> results = new ArrayList<VenueComment>();

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()){

				VenueComment temp = new VenueComment();

				temp.setLocationID(rs.getString("locationID"));
				temp.setFirst(rs.getString("first"));
				temp.setLast(rs.getString("last"));
				temp.setDate(rs.getString("date"));
				temp.setText(rs.getString("text"));
				temp.setUserID(rs.getString("userID"));
				temp.setUserImg(rs.getString("userIMG"));
				
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
*/
	/**
	 * TODO: How to get complete array of child comments
	 * @param locationID
	 * @return
	 */
	public StandardResult getVenuePhotos(String locationID){

		String query = "SELECT feeds.id as feedID, feeds.date, feeds.text, feeds.img as feedImg, feeds.twitterFeedID, feeds.active," +
	    		" users.userID, users.first, users.last, users.img as userImg "
				//+ ", likes.userID as likesUserID, likes.feedID as likesFeedID "
	    		//+ "childcomments.feedID as childFeedID, childcomments.text as childText "
	    		+ "FROM feeds "
	    		+ "JOIN (users) "
	    		//+ "JOIN (likes) "
	    		+ "WHERE locationID = '" + locationID + "' AND feeds.img != '' "// AND likes.feedID = feedID "
				+ "ORDER BY feeds.date DESC LIMIT 50";
		
		
		
		
		
		/*String query = "SELECT feeds.id as feedID, feeds.date, feeds.text, feeds.img as feedImg, feeds.twitterFeedID, feeds.active," +
	    		" users.userID, users.first, users.last, users.img " +
	    		"FROM feeds " +
	    		"JOIN (users) ON (feeds.img != '' AND users.userID = feeds.userID) " +
	    		//"JOIN (likes) ON (feeds.img != '' AND likes.userID = feeds.userID) " +
	    		"ORDER BY feeds.date DESC LIMIT 50";
		*/
		//"FROM feeds " +
		//"JOIN (users) ON (feeds.img != '' AND feeds.userID = '"+ userID + "' AND users.userID = feeds.userID)  " +
		

		StandardResult finalResult = new StandardResult();
		List <VenueComment> results = new ArrayList<VenueComment>();
		List <Standard> child = new ArrayList<Standard>();
		
		
		
		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()){
				
				VenueComment temp = new VenueComment();
				VenueComment parent = new VenueComment();
				Profile user = new Profile();
				ChildComment tempChild = new ChildComment();
				
				
				user.setFirst(rs.getString("first"));
				user.setLast(rs.getString("last"));
				user.setImg(rs.getString("userImg"));
				user.setUserID(rs.getString("userID"));
				
				parent.setDate(rs.getString("date"));
				parent.setUserID(rs.getString("userID"));
				parent.setImg(rs.getString("feedImg"));
				parent.setText(rs.getString("text"));
				parent.setLocationID(locationID);
				parent.setTwitterFeedID(rs.getString("twitterFeedID"));
				parent.setActive(rs.getString("active"));
				parent.setUser(user);
				
				//tempChild.setText(rs.getString("childText"));
				//tempChild.setFeedID(rs.getString("childFeedID"));
				
				//System.out.println(rs.getString("childFeedID")+", "+rs.getString("childText"));
				//System.out.println(rs.getString("likesUserID")+", "+ rs.getString("likesFeedID")+", "+rs.getString("feedID"));
				
				child.add(tempChild);
				temp.setParent(parent);
				results.add(temp);
			}

			/*String query2 = "SELECT feeds.id as feedID, feeds.date, feeds.text, feeds.img as feedImg, feeds.twitterFeedID, feeds.active," +
		    		" users.userID, users.first, users.last, users.img as userImg "
					//+ ", likes.userID as likesUserID, likes.feedID as likesFeedID "
		    		//+ "childcomments.feedID as childFeedID, childcomments.text as childText "
		    		+ "FROM feeds "
		    		+ "JOIN (users) "
		    		//+ "JOIN (likes) "
		    		+ "WHERE locationID = '" + locationID + "' AND feeds.img != '' "// AND likes.feedID = feedID "
					+ "ORDER BY feeds.date DESC LIMIT 50";
			*/
			
			//query2 = "SELECT * FROM likes WHERE userID = '" + userID + "'";
			
			
			
			
			
			
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




	/*
	public StandardSizePhotoResponse getAllVenuePhotos () throws ClassNotFoundException
	{
		List <PhotoResult> results = new ArrayList<PhotoResult>();

		try
		{   
		    Statement stmt = this.connection.createStatement();

		    String query = "SELECT feeds.id as feedID, feeds.date, feeds.text, feeds.img as feedImg," +
		    		" users.userID, users.first, users.last, users.img " +
		    		"FROM feeds " +
		    		"JOIN (users) ON (feeds.img != '' AND users.userID = feeds.userID) " +
		    		"ORDER BY feeds.date DESC LIMIT 50";

		    ResultSet rs = stmt.executeQuery(query);

		    while (rs.next())
		    {
		    	PhotoResult temp = new PhotoResult();

		        temp.setFeedID(rs.getInt("feedID"));
		        temp.setDate(rs.getString("date"));
		        temp.setText(rs.getString("text"));
		        temp.setFeedImg(rs.getString("feedImg"));
		        temp.setUserID(rs.getString("userID"));

		        results.add(temp);
		    }

		}
		catch (SQLException error)
		{
			System.out.println("There was an error writing the statement: " + error);
		}

		//set result properties
		StandardSizePhotoResponse standardResult = new StandardSizePhotoResponse();

		standardResult.setResultSize(results.size());
		standardResult.setResults(results);

		if (results.size() > 0)
			standardResult.setSuccess(1);
		else
			standardResult.setSuccess(0);

		return standardResult;
	}*/


	/**
	 * 
	 * @param venueID
	 * @return 
	 */
	/*
	public StandardSizePhotoResponse getSpecificVenuePhotos (String venueID){

		List <PhotoResult> results = new ArrayList<PhotoResult>();

		try{   
		    Statement stmt = connection.createStatement();

		    String query = "SELECT feeds.id as feedID, feeds.date, feeds.text, feeds.img as feedImg, " +
		    		"users.userID, users.first, users.last, users.img " +
		    		"FROM feeds " +
		    		//JOIN: users that have feedimg that are posted to given venueId
		    		"JOIN (users) ON (feeds.img != '' AND feeds.locationID = '"+ venueID + "' AND users.userID = feeds.userID)  " +
		    		"ORDER BY feeds.date DESC LIMIT 50";

		    ResultSet rs = stmt.executeQuery(query);

		    while (rs.next()){
		    	PhotoResult temp = new PhotoResult();

		        temp.setFeedID(rs.getInt("feedID"));
		        temp.setDate(rs.getString("date"));
		        temp.setText(rs.getString("text"));
		        temp.setFeedImg(rs.getString("feedImg"));
		        temp.setUserID(rs.getString("userID"));

		        results.add(temp);
		    }

		}
		catch (SQLException error){
			System.out.println("There was an error writing the statement: " + error);
		}

		//set result properties
		StandardSizePhotoResponse standardResult = new StandardSizePhotoResponse();

		standardResult.setResultSize(results.size());
		standardResult.setResults(results);

		if (results.size() > 0)
			standardResult.setSuccess(1);
		else
			standardResult.setSuccess(0);

		closeConnection();
		return standardResult;
	}


public StandardNewsResponse getSpecificVenueNews(String venueID){

		List <NewsResult> results = new ArrayList<NewsResult>();

		try{   
		    Statement stmt = connection.createStatement();

		    String query = //Select variables from sportsnews table
					"SELECT sportsnews.storyID, sportsteams.ID as sportsteamID, sportsnews.headline, sportsnews.mobileLink, "
					+ "sportsnews.linkText, sportsnews.imageLink, sportsnews.published, sportsnews.source, sportsnews.description, "
					+ "sportsnews.imageWidth, sportsnews.imageHeight " +
					"FROM sportsteams " +
					//Find TeamId's that pertain to input venueId, via sportsteams Table
					//Use teamId to display news that goes with input venueId
					"JOIN (sportsnews) ON (sportsteams.locationID = " + venueID + " " +
					"AND sportsnews.teamID = sportsteams.ID "+ 
					"AND sportsteams.active = 1 "+
					"AND sportsnews.active = 1) " +
					"ORDER BY sportsnews.published DESC LIMIT 20";


		    ResultSet rs = stmt.executeQuery(query);

		    while (rs.next()){
		    	NewsResult temp = new NewsResult();


		    	//set temp data
				temp.setStoryID(rs.getString("storyID"));
				temp.setSportsTeamID(rs.getInt("sportsteamID"));
				temp.setHeadline(rs.getString("headline"));
				temp.setMobileLink(rs.getString("mobileLink"));
				temp.setLinkText(rs.getString("linkText"));
				temp.setImageLink(rs.getString("imageLink"));
				temp.setPublished(rs.getString("published"));
				temp.setSource(rs.getString("source"));
				temp.setDescription(rs.getString("description"));
				temp.setImageWidth(rs.getString("imageWidth"));
				temp.setImageHeight(rs.getString("imageHeight"));


		        results.add(temp);
		    }

		}
		catch (SQLException error){
			System.out.println("There was an error writing the statement: " + error);
		}

		//set result properties
		StandardNewsResponse standardResult = new StandardNewsResponse();

		standardResult.setResultSize(results.size());
		standardResult.setResults(results);

		if (results.size() > 0)
			standardResult.setSuccess(1);
		else
			standardResult.setSuccess(0);

		closeConnection();
		return standardResult;
	}

	 */


	/**Get recent news stories, no venueId with this call.
	 * @return
	 */
	/*public StandardNewsResult getRecentNews(){

		String query = //Select variables from sportsnews table
				"SELECT sportsnews.storyID, sportsteams.ID as sportsteamID, sportsnews.headline, sportsnews.mobileLink, "
				+ "sportsnews.linkText, sportsnews.imageLink, sportsnews.published, sportsnews.source, sportsnews.description, "
				+ "sportsnews.imageWidth, sportsnews.imageHeight " +
				//from?
				"FROM sportsteams " +
				"JOIN (sportsnews) ON (sportsteams.active = 1 AND sportsnews.teamID = sportsteams.ID and sportsnews.active = 1) " +
				"ORDER BY sportsnews.published DESC LIMIT 20";

		//create list to save the results
		List <NewsResult> results = new ArrayList<NewsResult>();

		try{
			Statement stmt = this.connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()){
				NewsResult temp = new NewsResult();

				//set temp data
				temp.setStoryID(rs.getString("storyID"));
				temp.setSportsTeamID(rs.getInt("sportsteamID"));
				temp.setHeadline(rs.getString("headline"));
				temp.setMobileLink(rs.getString("mobileLink"));
				temp.setLinkText(rs.getString("linkText"));
				temp.setImageLink(rs.getString("imageLink"));
				temp.setPublished(rs.getString("published"));
				temp.setSource(rs.getString("source"));
				temp.setDescription(rs.getString("description"));
				temp.setImageWidth(rs.getString("imageWidth"));
				temp.setImageHeight(rs.getString("imageHeight"));

				results.add(temp);
			}
		}
		catch (SQLException error){
			System.out.println("Error executing query: " + error);
		}

		//response object
		StandardNewsResult standardNewsResult = new StandardNewsResult();
		standardNewsResult.setSuccess(1);

		if (results.size() > 0)
			standardNewsResult.setExpectResults(1);
		else
			standardNewsResult.setExpectResults(0);

		standardNewsResult.setResults(results);

		closeConnection();
		return standardNewsResult;
	}
	 */
	/**
	 * 
	 * @param feedID
	 * @return
	 */
	/*
	public StandardChildCommentResult getChildComments(String feedID){

		String query = 
				"SELECT childcomments.date, childcomments.userID, childcomments.text, users.first, users.last, users.img as userImg " +
				"FROM childcomments " +
				"JOIN (users) ON (childcomments.feedID = " + feedID +" "+
				"AND childcomments.active = 1 "+ 
				"AND users.userID = childcomments.userID " +
				"AND users.active = 1) " +
				"ORDER BY childcomments.date ASC LIMIT 50";

		List <ChildCommentResult> results = new ArrayList<ChildCommentResult>();

		try{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()){
				ChildCommentResult temp = new ChildCommentResult();

				//set date
				temp.setUserID(rs.getString("userID"));
				temp.setDate(rs.getString("date"));
				temp.setText(rs.getString("text"));
				temp.setFirst(rs.getString("first"));
				temp.setLast(rs.getString("last"));
				temp.setUserImg(rs.getString("userImg"));

				results.add(temp);
			}
		}
		catch (SQLException error){
			System.out.println("Error executing query: " + error);
		}

		//response object
		StandardChildCommentResult response = new StandardChildCommentResult();
		response.setSuccess(1);
		response.setCallback("getChildComments");
		if (results.size() > 0){
			response.setExpectResults(1);

			response.setChildCommentCount(results.size());
			response.setResult(results);
		}
		else
			response.setExpectResults(0);

		closeConnection();
		return response;
	}
	 */
}
