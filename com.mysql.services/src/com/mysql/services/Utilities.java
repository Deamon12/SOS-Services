package com.mysql.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
//import com.amazonaws.services.simpleemail.*;
//import com.amazonaws.services.simpleemail.model.*;
//import com.amazonaws.regions.*;


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
	
	/*public class AmazonSESSample {
		 
	    static final String FROM = "SENDER@EXAMPLE.COM";  // Replace with your "From" address. This address must be verified.
	    static final String TO = "RECIPIENT@EXAMPLE.COM"; // Replace with a "To" address. If your account is still in the
	                                                      // sandbox, this address must be verified.
	    static final String BODY = "This email was sent through Amazon SES by using the AWS SDK for Java.";
	    static final String SUBJECT = "Amazon SES test (AWS SDK for Java)";
	  

	    public static void main(String[] args) throws IOException {    	
	                
	        // Construct an object to contain the recipient address.
	        Destination destination = new Destination().withToAddresses(new String[]{TO});
	        
	        // Create the subject and body of the message.
	        Content subject = new Content().withData(SUBJECT);
	        Content textBody = new Content().withData(BODY); 
	        Body body = new Body().withText(textBody);
	        
	        // Create a message with the specified subject and body.
	        Message message = new Message().withSubject(subject).withBody(body);
	        
	        // Assemble the email.
	        SendEmailRequest request = new SendEmailRequest().withSource(FROM).withDestination(destination).withMessage(message);
	        
	        try
	        {        
	            System.out.println("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");
	        
	            // Instantiate an Amazon SES client, which will make the service call. The service call requires your AWS credentials. 
	            // Because we're not providing an argument when instantiating the client, the SDK will attempt to find your AWS credentials 
	            // using the default credential provider chain. The first place the chain looks for the credentials is in environment variables 
	            // AWS_ACCESS_KEY_ID and AWS_SECRET_KEY. 
	            // For more information, see http://docs.aws.amazon.com/AWSSdkDocsJava/latest/DeveloperGuide/credentials.html
	            AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient();
	               
	            // Choose the AWS region of the Amazon SES endpoint you want to connect to. Note that your sandbox 
	            // status, sending limits, and Amazon SES identity-related settings are specific to a given AWS 
	            // region, so be sure to select an AWS region in which you set up Amazon SES. Here, we are using 
	            // the US West (Oregon) region. Examples of other regions that Amazon SES supports are US_EAST_1 
	            // and EU_WEST_1. For a complete list, see http://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html 
	            Region REGION = Region.getRegion(Regions.US_WEST_2);
	            client.setRegion(REGION);
	       
	            // Send the email.
	            client.sendEmail(request);  
	            System.out.println("Email sent!");
	        }
	        catch (Exception ex) 
	        {
	            System.out.println("The email was not sent.");
	            System.out.println("Error message: " + ex.getMessage());
	        }
	    }
	}*/
	

}

