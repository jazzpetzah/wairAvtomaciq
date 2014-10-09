package com.wearezeta.auto.sync.report;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.client.ZetaInstance;
import com.wearezeta.auto.sync.client.ZetaSender;

class UserReport {
	public String name;
	public String loggedOnPlatform;
	public String startupTime;
	public boolean isEnabled;
}

class MessageReport {
	public String message;
	public String sentFrom;
	public boolean isOsxReceiveTimeOK;
	public String osxReceiveTime;
	public boolean isIosReceiveTimeOK;
	public String iosReceiveTime;
	public boolean isAndroidReceiveTimeOK;
	public String androidReceiveTime;
}

public class ReportData {
	public ArrayList<UserReport> users = new ArrayList<UserReport>();

	public ArrayList<MessageReport> messages = new ArrayList<MessageReport>();
	public double averageIosReceiveTime;
	public double averageAndroidReceiveTime;
	public double averageOsxReceiveTime;
	
	public boolean areClientsStable;
	public boolean areMessagesReceived;
	public boolean areMessagesReceiveTimeCorrect;
	public boolean areMessagesOrderCorrect;

	public ArrayList<String> iosMessages;
	public ArrayList<String> osxMessages;
	public ArrayList<String> androidMessages;
	
	public void fillReportInfo() {
		for (Map.Entry<String, ZetaInstance> client : ExecutionContext.clients
				.entrySet()) {
			UserReport user = new UserReport();
			if (client.getValue().isEnabled()) {
				user.name = client.getValue().getUserInstance().getEmail();
				user.loggedOnPlatform = client.getKey();
				user.startupTime = Double.toString(client.getValue().getStartupTimeMs()/1000d) + "s";
				user.isEnabled = true;
			} else {
				user.loggedOnPlatform = client.getKey();
				user.isEnabled = false;
			}
			users.add(user);
		}

		processMessages();
	}
	
	public void processMessages() {
		LinkedHashMap<String, MessageEntry> sentMessages = new LinkedHashMap<String, MessageEntry>();
		
		LinkedHashMap<String, MessageEntry> androidReceivedMessages = new LinkedHashMap<String, MessageEntry>();
		LinkedHashMap<String, MessageEntry> androidNotReceivedMessages = new LinkedHashMap<String, MessageEntry>();
		
		LinkedHashMap<String, MessageEntry> iosReceivedMessages = new LinkedHashMap<String, MessageEntry>();
		LinkedHashMap<String, MessageEntry> iosNotReceivedMessages = new LinkedHashMap<String, MessageEntry>();
		
		LinkedHashMap<String, MessageEntry> osxReceivedMessages = new LinkedHashMap<String, MessageEntry>();
		LinkedHashMap<String, MessageEntry> osxNotReceivedMessages = new LinkedHashMap<String, MessageEntry>();

		if (ExecutionContext.iosZeta().isEnabled()) {
			iosReceivedMessages = new LinkedHashMap<String, MessageEntry>();
			for (Map.Entry<String, MessageEntry> message : ExecutionContext
					.iosZeta().listener().registeredMessages.entrySet()) {
				if (message.getValue().appearanceDate
						.after(ZetaSender.sendingStartDate)) {

					iosReceivedMessages.put(message.getKey(), message.getValue());
				}
			}
			iosNotReceivedMessages = new LinkedHashMap<String, MessageEntry>();
			for (Map.Entry<String, MessageEntry> message : ExecutionContext
					.iosZeta().listener().notReceivedMessages.entrySet()) {
				iosNotReceivedMessages.put(message.getKey(), message.getValue());
			}
		}
		
		if (ExecutionContext.androidZeta().isEnabled()) {
			androidReceivedMessages = new LinkedHashMap<String, MessageEntry>();
			for (Map.Entry<String, MessageEntry> message : ExecutionContext
					.androidZeta().listener().registeredMessages.entrySet()) {
				if (message.getValue().appearanceDate
						.after(ZetaSender.sendingStartDate)) {
					androidReceivedMessages.put(message.getKey(), message.getValue());
				}
			}
			androidNotReceivedMessages = new LinkedHashMap<String, MessageEntry>();
			for (Map.Entry<String, MessageEntry> message : ExecutionContext
					.androidZeta().listener().notReceivedMessages.entrySet()) {
				androidNotReceivedMessages.put(message.getKey(), message.getValue());
			}
		}
		
		if (ExecutionContext.osxZeta().isEnabled()) {
			osxReceivedMessages = new LinkedHashMap<String, MessageEntry>();
			for (Map.Entry<String, MessageEntry> message : ExecutionContext
					.osxZeta().listener().registeredMessages.entrySet()) {
				if (message.getValue().appearanceDate
						.after(ZetaSender.sendingStartDate)) {

					osxReceivedMessages.put(message.getKey(), message.getValue());
				}
			}
			osxNotReceivedMessages = new LinkedHashMap<String, MessageEntry>();
			for (Map.Entry<String, MessageEntry> message : ExecutionContext
					.osxZeta().listener().notReceivedMessages.entrySet()) {
				osxNotReceivedMessages.put(message.getKey(), message.getValue());
			}
		}
		
		if (osxReceivedMessages.size() == ExecutionContext.osxZeta().getMessagesToSend()*2
				&& iosReceivedMessages.size() == ExecutionContext.osxZeta().getMessagesToSend()*2
				&& androidReceivedMessages.size() == ExecutionContext.osxZeta().getMessagesToSend()*2) {
			areMessagesReceived = true;
		} else {
			areMessagesReceived = false;
		}
		sentMessages = ExecutionContext.sentMessages;
		
		long iosSumReceiveTime = 0;
		long androidSumReceiveTime = 0;
		long osxSumReceiveTime = 0;
		
		for (Map.Entry<String, MessageEntry> entry: sentMessages.entrySet()) {
			MessageReport report = new MessageReport();
			report.message = entry.getKey();
			MessageEntry sentMessage = entry.getValue();
			report.sentFrom = entry.getValue().sender;
			if (report.sentFrom.equals(CommonUtils.PLATFORM_NAME_OSX)) {
				report.isOsxReceiveTimeOK = true;
				report.osxReceiveTime = "-1";
				MessageEntry iosMessage = iosReceivedMessages.get(entry.getKey());
				if (iosMessage != null) {
					long time = iosMessage.appearanceDate.getTime() - sentMessage.appearanceDate.getTime();
					report.iosReceiveTime = Double.toString(time/1000d) + "s";
					iosSumReceiveTime += time;
					if (time > 5000) {
						report.isIosReceiveTimeOK = false;
					} else {
						report.isIosReceiveTimeOK = true;
					}
				} else {
					report.iosReceiveTime = "not received";
					report.isIosReceiveTimeOK = false;
				}
				MessageEntry androidMessage = androidReceivedMessages.get(entry.getKey());
				if (androidMessage != null) {
					long time = androidMessage.appearanceDate.getTime() - sentMessage.appearanceDate.getTime();
					androidSumReceiveTime += time;
					report.androidReceiveTime = Double.toString(time/1000d) + "s";
					if (time > 5000) {
						report.isAndroidReceiveTimeOK = false;
					} else {
						report.isAndroidReceiveTimeOK = true;
					}
				} else {
					report.androidReceiveTime = "not received";
					report.isAndroidReceiveTimeOK = false;
				}
			}
			
			if (report.sentFrom.equals(CommonUtils.PLATFORM_NAME_IOS)) {
				report.isIosReceiveTimeOK = true;
				report.iosReceiveTime = "-1";
				MessageEntry osxMessage = osxReceivedMessages.get(entry.getKey());
				if (osxMessage != null) {
					long time = osxMessage.appearanceDate.getTime() - sentMessage.appearanceDate.getTime();
					osxSumReceiveTime += time;
					report.osxReceiveTime = Double.toString(time/1000d) + "s";
					if (time > 5000) {
						report.isOsxReceiveTimeOK = false;
					} else {
						report.isOsxReceiveTimeOK = true;
					}
				} else {
					report.osxReceiveTime = "not received";
					report.isOsxReceiveTimeOK = false;
				}
				MessageEntry androidMessage = androidReceivedMessages.get(entry.getKey());
				if (androidMessage != null) {
					long time = androidMessage.appearanceDate.getTime() - sentMessage.appearanceDate.getTime();
					androidSumReceiveTime += time;
					report.androidReceiveTime = Double.toString(time/1000d) + "s";
					if (time > 5000) {
						report.isAndroidReceiveTimeOK = false;
					} else {
						report.isAndroidReceiveTimeOK = true;
					}
				} else {
					report.androidReceiveTime = "not received";
					report.isAndroidReceiveTimeOK = false;
				}
			}
			
			if (report.sentFrom.equals(CommonUtils.PLATFORM_NAME_ANDROID)) {
				report.isAndroidReceiveTimeOK = true;
				report.androidReceiveTime = "-1";
				MessageEntry osxMessage = osxReceivedMessages.get(entry.getKey());
				if (osxMessage != null) {
					long time = osxMessage.appearanceDate.getTime() - sentMessage.appearanceDate.getTime();
					osxSumReceiveTime += time;
					report.osxReceiveTime = Double.toString(time/1000d) + "s";
					if (time > 5000) {
						report.isOsxReceiveTimeOK = false;
					} else {
						report.isOsxReceiveTimeOK = true;
					}
				} else {
					report.osxReceiveTime = "not received";
					report.isOsxReceiveTimeOK = false;
				}
				MessageEntry iosMessage = iosReceivedMessages.get(entry.getKey());
				if (iosMessage != null) {
					long time = iosMessage.appearanceDate.getTime() - sentMessage.appearanceDate.getTime();
					iosSumReceiveTime += time;
					report.iosReceiveTime = Double.toString(time/1000d) + "s";
					if (time > 5000) {
						report.isIosReceiveTimeOK = false;
					} else {
						report.isIosReceiveTimeOK = true;
					}
				} else {
					report.iosReceiveTime = "not received";
					report.isIosReceiveTimeOK = false;
				}
			}
			
			averageIosReceiveTime = (iosSumReceiveTime / (double)iosReceivedMessages.size())/1000d;
			averageAndroidReceiveTime = (androidSumReceiveTime / (double)androidReceivedMessages.size())/1000d;
			averageOsxReceiveTime = (osxSumReceiveTime / (double)osxReceivedMessages.size())/1000d;
			
			averageIosReceiveTime = Math.round(averageIosReceiveTime*1000)/1000.0d;
			averageAndroidReceiveTime = Math.round(averageAndroidReceiveTime*1000)/1000.0d;
			averageOsxReceiveTime = Math.round(averageOsxReceiveTime*1000)/1000.0d;
			
			if (report.isIosReceiveTimeOK
					&& report.isAndroidReceiveTimeOK
					&& report.isOsxReceiveTimeOK) {
				areMessagesReceiveTimeCorrect = true;
			} else {
				areMessagesReceiveTimeCorrect = false;
			}
			areClientsStable = true;
			areMessagesOrderCorrect = ExecutionContext.messagesOrderCorrect();

			iosMessages = new ArrayList<String>();
			for (MessageEntry iosMessageEntry: ExecutionContext.iosZeta().getMessagesListAfterTest()) {
				iosMessages.add(iosMessageEntry.messageContent);
			}
			osxMessages = new ArrayList<String>();
			for (MessageEntry osxMessageEntry: ExecutionContext.osxZeta().getMessagesListAfterTest()) {
				osxMessages.add(osxMessageEntry.messageContent);
			}
			androidMessages = new ArrayList<String>();
			for (MessageEntry androidMessageEntry: ExecutionContext.androidZeta().getMessagesListAfterTest()) {
				androidMessages.add(androidMessageEntry.messageContent);
			}
			
			messages.add(report);
		}
	}
	
	public static final String toXml(ReportData data) {
		XStream xstream = new XStream(new DomDriver()); // does not require XPP3 library
		xstream.alias("ReportData", ReportData.class);
		xstream.alias("UserInfo", UserReport.class);
		xstream.alias("MessageReport", MessageReport.class);
		String xml = xstream.toXML(data);
		return xml;
	}
}
