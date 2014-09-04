package com.wearezeta.auto.cmdclient;

import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.UsersState;
import com.wearezeta.auto.common.log.ZetaLogger;


public class ZConsoleClient 
{
	private static final Logger log = ZetaLogger.getLog(ZConsoleClient.class.getSimpleName());
	private static final Logger backendLog = ZetaLogger.getLog(BackEndREST.class.getSimpleName());
	
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
    public static void main( String[] args ) throws Exception
    {
    	backendLog.setLevel(Level.INFO);
    	
    	log.info("Valid arguments :\n"
    			+ "-m your zeta client email\n"
    			+ "-p your zeta client password\n"
    			+ "-u chat or contact name who will receive messages\n"
    			+ "-c (optional) number of messages to send, default is 1\n"
    			+ "-i (optional) interval between messages (seconds), default is 0");
    	
    	if (args.length % 2 == 1 || args.length == 0) {
    		log.info("Invalid number of arguments");
    		
    		return;
    	}
    	
    	String login = "", password = "", contact = "";
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
			}
    	}

		ClientUser yourСontact = new ClientUser(login, password, "ZConsoleClient", UsersState.AllContactsConnected);
			
		long startDate = new Date().getTime();

		sendMessageWithInterval(yourСontact, contact, interval, messageCount);
		
		long endDate = new Date().getTime();
		
		log.info("Total execution time - " + (endDate - startDate) / 1000 + " seconds");
    }
}
