package com.wearezeta.auto.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.wearezeta.auto.common.misc.MessageEntry;

public class ReportGenerator {
	public static ArrayList<String> chatUsers;
	public static HashMap<String, String> loggedInUsers;
	public static HashMap<String, Long> startupTimes;
	
	public static ArrayList<MessageEntry> sentMessages;
	public static LinkedHashMap<String, MessageEntry> androidReceivedMessages = new LinkedHashMap<String, MessageEntry>();
	public static LinkedHashMap<String, MessageEntry> iosReceivedMessages = new LinkedHashMap<String, MessageEntry>();;
	public static LinkedHashMap<String, MessageEntry> osxReceivedMessages = new LinkedHashMap<String, MessageEntry>();;

	public static String generate() {
		StringBuilder sb = new StringBuilder();
		sb.append("Chat users: \n");
		for (String chatUser: chatUsers) {
			sb.append("\t" + chatUser + "\n");
		}
		
		sb.append("Logged in users: \n");
		for (Map.Entry<String, String> loggedIn: loggedInUsers.entrySet()) {
			sb.append("\t" + loggedIn.getKey() + ": " + loggedIn.getValue()
					+ (loggedIn.getValue().equals("-==DISABLED==-")?"\n":
						(" (Startup time: "
								+ (startupTimes.get(loggedIn.getKey())/1000.0d))
								+ "s)\n"));
		}
		
		sb.append("Sent messages: \n");
		int i = 0;
		for (MessageEntry sentMessage: sentMessages) {
			sb.append((i++) + ":\t" + sentMessage + "\n");
		}

		sb.append("Received messages (android): \n");
		i = 0;
		for (Map.Entry<String, MessageEntry> sentMessage: androidReceivedMessages.entrySet()) {
			sb.append((i++) + ":\t" + sentMessage.getValue() + "\n");
		}
		
		sb.append("Received messages (osx): \n");
		i = 0;
		for (Map.Entry<String, MessageEntry> sentMessage: osxReceivedMessages.entrySet()) {
			sb.append((i++) + ":\t" + sentMessage.getValue() + "\n");
		}
		
		sb.append("Received messages (ios): \n");
		i = 0;
		for (Map.Entry<String, MessageEntry> sentMessage: iosReceivedMessages.entrySet()) {
			sb.append((i++) + ":\t" + sentMessage.getValue() + "\n");
		}
		
		return sb.toString();
	}
}
