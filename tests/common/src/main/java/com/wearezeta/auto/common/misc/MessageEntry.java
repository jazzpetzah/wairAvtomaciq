package com.wearezeta.auto.common.misc;

import java.util.Date;

public class MessageEntry {
	public String messageType;
	public String messageContent;
	public String sender;
	public Date appearanceDate;
	public boolean checkTime;
	
	public MessageEntry(String type, String content, String sender, Date date, boolean checkTime) {
		this.messageType = type;
		this.messageContent = content;
		this.sender = sender;
		this.appearanceDate = date;
		this.checkTime = checkTime;
	}
	
	public MessageEntry(String type, String content, Date date, boolean checkTime) {
		this.messageType = type;
		this.messageContent = content;
		this.appearanceDate = date;
		this.checkTime = checkTime;
	}
	
	public String toString() {
		if (sender == null || sender.isEmpty()) {
			return messageType + ": " + messageContent + " (" + appearanceDate + ")";
			
		} else {
			return messageType + ": " + messageContent + " (" + appearanceDate + ", sent from - " + sender + ")";
		}
	}
}
