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
	
	public static LinkedHashMap<String, MessageEntry> sentMessages = new LinkedHashMap<String, MessageEntry>();
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
		int i = 1;
		for (Map.Entry<String, MessageEntry> sentMessage: sentMessages.entrySet()) {
			sb.append((i++) + ":\t" + sentMessage.getValue() + "\n");
		}

		sb.append("Received messages (android): \n");
		i = 1;
		double average = 0;
		for (Map.Entry<String, MessageEntry> sentMessage: androidReceivedMessages.entrySet()) {
			String message = sentMessage.getValue().messageContent;
			long receivementTime = sentMessage.getValue().appearanceDate.getTime()-sentMessages.get(message).appearanceDate.getTime();
			average += receivementTime;
			sb.append((i++) + ":\t" + sentMessage.getValue() + " (received in: " + receivementTime + "ms)\n");
		}
		sb.append("Average receive time (android): " + (average/(double)i-1) + "ms\n\n");
		
		sb.append("Received messages (osx): \n");
		i = 1;
		average = 0;
		for (Map.Entry<String, MessageEntry> sentMessage: osxReceivedMessages.entrySet()) {
			String message = sentMessage.getValue().messageContent;
			long receivementTime = sentMessage.getValue().appearanceDate.getTime()-sentMessages.get(message).appearanceDate.getTime();
			average += receivementTime;
			sb.append((i++) + ":\t" + sentMessage.getValue() + " (received in: " + receivementTime + "ms)\n");
		}
		sb.append("Average receive time (osx): " + (average/(double)i-1) + "ms\n\n");
		
		sb.append("Received messages (ios): \n");
		i = 1;
		average = 0;
		for (Map.Entry<String, MessageEntry> sentMessage: iosReceivedMessages.entrySet()) {
			String message = sentMessage.getValue().messageContent;
			long receivementTime = sentMessage.getValue().appearanceDate.getTime()-sentMessages.get(message).appearanceDate.getTime();
			average += receivementTime;
			sb.append((i++) + ":\t" + sentMessage.getValue() + " (received in: " + receivementTime + "ms)\n");
		}
		sb.append("Average receive time (ios): " + (average/(double)i-1) + "ms\n\n");
		
		return sb.toString();
	}
}
