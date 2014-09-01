package com.wearezeta.auto.cmdclient;

import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.UsersState;


public class ZConsoleClient 
{
	public static void sendMessageWithInterval(ClientUser user, String contact, int interval, int messageCount) throws Exception {
		if (messageCount != -1) {
			for (int i = 0; i < messageCount; i++) {
				
				System.out.println("Sending message " + Integer.toString(i + 1) + " of " + Integer.toString(messageCount) + 
						" to contact " + contact);
				BackEndREST.sendDialogMessageByChatName(user, contact, CommonUtils.generateGUID());
				Thread.sleep(interval * 1000);
			}
		}
		else {
			while (true) {
				
				System.out.println("Sending message to contact " + contact);
				
				BackEndREST.sendDialogMessageByChatName(user, contact, CommonUtils.generateGUID());
				Thread.sleep(interval * 1000);
			}
		}
	}
    public static void main( String[] args ) throws Exception
    {
    	System.out.println("Valid arguments :\n"
    			+ "-m your zeta client email\n"
    			+ "-p your zeta client password\n"
    			+ "-u chat or contact name who will receive messages\n"
    			+ "-c (optional) number of messages to send, default is 1\n"
    			+ "-i (optional) interval between messages (seconds), default is 0");
    	
    	if (args.length % 2 == 1 || args.length == 0) {
    		System.out.println("Invalid number of arguments");
    		
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
			
		sendMessageWithInterval(yourСontact, contact, interval, messageCount);
    }
}
