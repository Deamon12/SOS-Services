package com.mysql.services;

import java.util.ArrayList;
import java.util.List;

import com.mysql.services.Utilities.Encrypt;


public class Tester {

	public static void main(String[] args) {
	
		String securePassword = Encrypt.get_SHA_1_SecurePassword("password");
		
		//System.out.println(new MainController().createUser("Jimbo", "Jones", securePassword, "1234@email.com", "1234"));
		
		//System.out.println(new MainController().doLogin("email@email.com", "password"));
		
		//System.out.println(new MainController().doLogin("email4@email.com", securePassword));
		List<String> tags = new ArrayList<String>();
		tags.add("there");
		tags.add("helloe");
		//System.out.println(new MainController().closeGroup(57));
		System.out.println(new MainController().createQuestion( "5re8i03bqkuc03bm28pid8nq9l", 0, 0, securePassword, tags, 0, 0, "CSE110", 1));
		//System.out.println(new MainController().viewQuestion(3));
		
		//System.out.println(new MainController().getQuestions(1.00, 2.00, tags, 1010.00));
		//System.out.println(new MainController().removeUser("user1"));
		//System.out.println(new MainController().resetPassword("madf@ucsd.edu", "HELOOOOOOOO"));
		//System.out.println(new MainController().askToJoinGroup(3, "mingyuhu"));
		//System.out.println(new MainController().getTags());
		//System.out.println(new MainController().rateTutor("mingyuhu", false));
		//System.out.println(new MainController().hasQuestion("mingyuhu"));
		//System.out.println(new MainController().addComment(60, "user1", "comment"));
		//System.out.println(new MainController().getComments(60));
		//System.out.println(new MainController().setVisibility(60, 1));
		
		/*
		System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "M") + " Miles");
		System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "K") + " Kilometers");
		System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "N") + " Nautical Miles");
		System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "ME") + " Meters");
		*/
		

	}
}



