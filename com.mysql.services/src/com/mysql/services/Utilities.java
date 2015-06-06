package com.mysql.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class Utilities {

	
	/**
	 * Convert geo location coordinate to Miles, Meters, or Kilos...
	 * @param lat1 double
	 * @param lon1 double
	 * @param lat2 double
	 * @param lon2 double
	 * @param unit String
	 * @return double
	 */
	static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515; //Miles
		if (unit.equalsIgnoreCase("K")){ //Kilometers
			dist = dist * 1.609344;
		} else if (unit.equalsIgnoreCase("N")){ //Nautical miles - WAT
			dist = dist * 0.8684;
		}
		else if(unit.equalsIgnoreCase("ME")){ //Meters
			dist = (dist * 1.609344) * 1000;
		}
		return (dist);
	}
	
	static double distLat(double limit){
		double dist = limit/69;
		return dist;
	}

	static double distLong(double limit){
		double dist = limit/69.172;
		return dist;
	}



	/*::  This function converts decimal degrees to radians:*/
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}


	/*::  This function converts radians to decimal degrees:*/
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	/*::  This function converts miles to meters:*/
	public static double ConvertMetersToMiles(double meters)
    {
        return (meters / 1609.344);
    }

	
	/**
	 * Used to encrypt the users password string to SHA1
	 * @author deamon
	 */
	static class Encrypt {
		static String get_SHA_1_SecurePassword(String passwordToHash)
		{
			String generatedPassword = null;
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				//md.update(salt.getBytes()); //No salt....
				byte[] bytes = md.digest(passwordToHash.getBytes());
				StringBuilder sb = new StringBuilder();
				for(int i=0; i< bytes.length ;i++)
				{
					sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
				}
				generatedPassword = sb.toString();
			}
			catch (NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			}
			return generatedPassword;
		}
	}
	
	/**
	 * Send email with password to email address
	 * 
	 * @param sendTo
	 * @param password
	 */
	static void sendEmail(String sendTo, String password){
		final String FROM = "soscse110@gmail.com";   
	    final String TO = sendTo;  
	    
	    final String BODY = "This is your new Anchor password: "+ password +". Please use this to log into Anchor!";
	    final String SUBJECT = "Anchor Password Reset";
	    
	    // Supply your SMTP credentials below. Note that your SMTP credentials are different from your AWS credentials.
	    final String SMTP_USERNAME = "AKIAJJQCMBP36LKMKKEA";  // Replace with your SMTP username.
	    final String SMTP_PASSWORD = "ApwHRXvThcESHiHWDY1XGeLuWPEEXHTST5+8HPU63t6x";  // Replace with your SMTP password.
	    
	    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
	    final String HOST = "email-smtp.us-west-2.amazonaws.com";    
	    
	    // Port we will connect to on the Amazon SES SMTP endpoint. We are choosing port 25 because we will use
	    // STARTTLS to encrypt the connection.
	    final int PORT = 25;


        // Create a Properties object to contain connection configuration information.
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT); 
            
            // Set properties indicating that we want to use STARTTLS to encrypt the connection.
            // The SMTP session will begin on an unencrypted connection, and then the client
        // will issue a STARTTLS command to upgrade to an encrypted connection.
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");

        // Create a Session object to represent a mail session with the specified properties. 
            Session session = Session.getDefaultInstance(props);

        Transport transport = null; 
        try
        {
        // Create a message with the specified information. 
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
        msg.setSubject(SUBJECT);
        msg.setContent(BODY,"text/plain");
            
        // Create a transport.        
        transport = session.getTransport();
                    
        // Send the message.
        
            System.out.println("Attempting to send an email through the Amazon SES SMTP interface...");
            
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
                
            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
            transport.close();        	
        }
        catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }
	}
	
}

