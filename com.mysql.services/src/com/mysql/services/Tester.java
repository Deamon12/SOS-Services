package com.mysql.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class Tester {

	public static void main(String[] args) {
	
		//String securePassword = Encrypt.get_SHA_1_SecurePassword("password");
		
		//System.out.println(new MainController().createUser("Jimbo", "Jones", securePassword, "email4@email.com"));
		
		//System.out.println(new MainController().doLogin("email@email.com", "password"));
		
		System.out.println(new MainController().acceptUser(3, "mingyuhu"));
		//System.out.println(new MainController().createQuestion( "user1", 0, 0, securePassword, null, 0, 0));
		
		/*
		System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "M") + " Miles");
		System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "K") + " Kilometers");
		System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "N") + " Nautical Miles");
		System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "ME") + " Meters");
		*/
		

	}


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
	

}
