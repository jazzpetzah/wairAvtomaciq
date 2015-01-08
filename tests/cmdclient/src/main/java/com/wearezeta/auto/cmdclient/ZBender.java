package com.wearezeta.auto.cmdclient;

import java.io.InputStream;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.UsersState;
import com.wearezeta.auto.common.log.ZetaLogger;


public class ZBender 
{
	private static final Logger log = ZetaLogger.getLog(ZBender.class.getSimpleName());
	private static final Logger backendLog = ZetaLogger.getLog(BackEndREST.class.getSimpleName());
	
	private static final String IMG = "/bender.jpg";
	
	public static void sendPicture(ClientUser user, String contact, String path) throws Throwable {
		InputStream configFileStream = null;
		if (path.equals("default")) {
		
			configFileStream = ZBender.class.getClass().getResourceAsStream(IMG);
		}
		
		BackEndREST.sendPictureToChatByName(user, contact, path, configFileStream);
		
		if (configFileStream != null) {
			configFileStream.close();
		}
	}
	
	public static void sendPictureWithInterval(ClientUser user, String contact, int interval, int messageCount, String path) throws Throwable {
		if (messageCount != -1) {
			for (int i = 0; i < messageCount; i++) {
				
				log.info("Sending image " + Integer.toString(i + 1) + " of " + Integer.toString(messageCount) + 
						" to contact " + contact);
				sendPicture(user, contact, path);
				Thread.sleep(interval * 1000);
			}
			
			log.info("Total images sent - " + Integer.toString(messageCount));
		}
		else {
			int count = 1;
			while (true) {
				
				log.info("Sending image " + Integer.toString(count) + " to contact " + contact);
				
				sendPicture(user, contact, path);
				count++;
				Thread.sleep(interval * 1000);
			}
		}
	}
	
	public static void sendMessageWithInterval(ClientUser user, String contact, int interval, int messageCount) throws Exception {
		if (messageCount != -1) {
			for (int i = 0; i < messageCount; i++) {
				
				log.info("Sending message " + Integer.toString(i + 1) + " of " + Integer.toString(messageCount) + 
						" to contact " + contact);
				BackEndREST.sendDialogMessageByChatName(user, contact, Integer.toString(i + 1) + " " + CommonUtils.generateGUID());
				Thread.sleep(interval * 1000);
			}
			
			log.info("Total messages sent - " + Integer.toString(messageCount));
		}
		else {
			int count = 1;
			while (true) {
				
				log.info("Sending message " + Integer.toString(count) + " to contact " + contact);
				
				BackEndREST.sendDialogMessageByChatName(user, contact, Integer.toString(count) + " " + CommonUtils.generateGUID());
				count++;
				Thread.sleep(interval * 1000);
			}
		}
	}
	
    public static void main( String[] args ) throws Throwable
    {
    	backendLog.setLevel(Level.INFO);
    	
    	log.info("Valid arguments :\n"
    			+ "-m your zeta client email\n"
    			+ "-p your zeta client password\n"
    			+ "-u chat or contact name who will receive messages\n"
    			+ "-img full path to image file or -1 for default image"
    			+ "-c (optional) number of messages/images to send, default is 1\n"
    			+ "-i (optional) interval between messages/images (seconds), default is 0\n");
    	
    	if (args.length % 2 == 1 || args.length == 0) {
    		log.info("Invalid number of arguments");
    		
    		return;
    	}
    	
    	String login = "error", password = "error", contact = "", imgPath = "", backend = "staging", name = "ZBender";
    	Boolean sendImg = false, showContacts = false;
    	int messageCount = 1, interval = 0;
    	for (int i = 0; i < args.length; i = i + 2) {
    		switch (args[i]) {
    		case "-m" :
    			login = args[i+1];
    			break;
    		
			case "-p" :
				password = args[i+1];
				break;
			
			case "-u" :
				contact = args[i+1];
				break;
				
			case "-c" :
				messageCount = Integer.parseInt(args[i+1]);
				break;
				
			case "-i" :
				interval = Integer.parseInt(args[i+1]);
				break;
				
			case "-img" :
				imgPath = args[i+1];
				sendImg = true;
				break;
    		
    		case "-contacts" :
    			showContacts = true;
    			break;
    		
			case "-backend" :
				backend = args[i+1];
				break;
				
			case "-name" :
				name = args[i+1];
				break;	
		}
    	}
    	
    	if (login.equals("error") || password.equals("error")) {
    		log.error("invalid credentials");
    		return;
    	}
    	
    	switch (backend) {
	    	case "edge":
	    		backend = "https://edge-nginz-https.zinfra.io";
	    		break;
	    	case "production":
	    		backend = "https://prod-nginz-https.wire.com";
	    		break;
	    	default:
	    		backend = "https://dev-nginz-https.zinfra.io";
	    		break;
    	}

    	BackEndREST.setDefaultBackendURL(backend);
    	
		ClientUser yourСontact = new ClientUser(login, password, name, UsersState.AllContactsConnected);
		
		if (showContacts) {
			BackEndREST.loginByUser(yourСontact);
			log.info("================================");
			String [] contacts = BackEndREST.getConversationsAsStringArray(yourСontact);
			for (int i = 0; i < contacts.length; i++) {
				log.info(contacts[i]);
			}
			log.info("================================");
			
			return;
		}

		long startDate = new Date().getTime();
		
		if (true == sendImg) {
			
			if (messageCount != -1) {
				if (messageCount == 1)
					messageCount++;
				sendMessageWithInterval(yourСontact, contact, interval, messageCount / 2);
				sendPictureWithInterval(yourСontact, contact, interval, messageCount / 2, imgPath);
			}
			else {
				while (true) {
					sendMessageWithInterval(yourСontact, contact, interval, 1);
					sendPictureWithInterval(yourСontact, contact, interval, 1, imgPath);
				}
			}
		}
		else {
			
			sendMessageWithInterval(yourСontact, contact, interval, messageCount);
		}
		
		long endDate = new Date().getTime();
		
		log.info("Total execution time - " + (endDate - startDate) / 1000 + " seconds");
    }
}
