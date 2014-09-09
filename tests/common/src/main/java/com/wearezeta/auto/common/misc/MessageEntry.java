package com.wearezeta.auto.common.misc;

import java.util.Date;

public class MessageEntry {
	public String messageType;
	public String messageContent;
	public String sender;
	public Date appearanceDate;
	
	public MessageEntry(String type, String content, String sender, Date date) {
		this.messageType = type;
		this.messageContent = content;
		this.sender = sender;
		this.appearanceDate = date;
	}
	
	public String toString() {
		return messageType + ": " + messageContent + " (" + appearanceDate + ", sent - " + sender + ")";
	}
}
