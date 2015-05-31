package com.sos.gcm;

import java.io.IOException;
import java.util.List;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

public class GoogleCloudMessaging {

	//GCM API Key
	final String GCM_API_KEY = "AIzaSyAhwbFKIDlDCHVHUB6D4TSCnGlXc-yeX-0";
	final int RETRIES = 5;
	
	public String sendMessage(int type, String userId, List<String> devices) throws IOException{
		
		String status;
		if (devices.isEmpty()) {
			status = "Message ignored as there is no device specified!";
		} 
		else {
			
			Message message = new Message.Builder().addData("type", ""+type).addData("userId", userId).build();
			
			MulticastResult result = new Sender(GCM_API_KEY).send(message, devices, RETRIES);
			status = "Sent message to "+devices.size()+ " device(s): " + result;
		}
		return status;
		
	}
	
}
