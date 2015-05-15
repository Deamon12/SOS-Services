package com.mysql.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.services.Utilities.Encrypt;


public class Tester {

	public static void main(String[] args) {
	
		String securePassword = Encrypt.get_SHA_1_SecurePassword("password");
		
		//System.out.println(new MainController().createUser("Jimbo", "Jones", securePassword, "email4@email.com", "1234"));
		
		//System.out.println(new MainController().doLogin("email@email.com", "password"));
		
		//System.out.println(new MainController().doLogin("aksdjf@ucsd.edu", "asdf"));
		List<String> tags = new ArrayList<String>();
		tags.add("there");
		//tags.add("hello");
		//System.out.println(new MainController().createQuestion( "user1", 0, 0, securePassword, tags, 0, 0, "CSE110"));
		//System.out.println(new MainController().viewQuestion(32));
		
		//System.out.println(new MainController().getQuestions(1.00, 2.00, tags, 25.00));
		//System.out.println(new MainController().acceptUser(3, "user1"));
		//System.out.println(new MainController().resetPassword("madf@ucsd.edu", "HELOOOOOOOO"));
		
		/*
		System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "M") + " Miles");
		System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "K") + " Kilometers");
		System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "N") + " Nautical Miles");
		System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "ME") + " Meters");
		*/
		

	}
}



