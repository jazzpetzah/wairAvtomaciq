package com.wearezeta.auto.sync.report;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.client.InstanceState;
import com.wearezeta.auto.sync.client.ZetaInstance;
import com.wearezeta.auto.sync.client.ZetaSender;

class UserReport {
	public String name;
	public String loggedOnPlatform;
	public String startupTime;
	public BuildVersionInfo buildVersion;
	public ClientDeviceInfo deviceData;
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
	public boolean checkTime = true;
}

public class ReportData {
	private static final Logger log = ZetaLogger.getLog(ReportData.class.getSimpleName());
	
	public ArrayList<UserReport> users = new ArrayList<UserReport>();

	public ArrayList<MessageReport> messages = new ArrayList<MessageReport>();
	
	public double averageIosReceiveTime;
	public double averageAndroidReceiveTime;
	public double averageOsxReceiveTime;
	
	public boolean areClientsStable;
	public boolean isAndroidStable;
	public boolean isIosStable;
	public boolean isOsxStable;
	
	public boolean areMessagesReceived;
	public boolean isAndroidReceiveMessages;
	public boolean isOsxReceiveMessages;
	public boolean isIosReceiveMessages;
	
	public boolean areMessagesReceiveTimeCorrect;
	public boolean isAndroidReceiveMessagesInTime;
	public boolean isIosReceiveMessagesInTime;
	public boolean isOsxReceiveMessagesInTime;
	
	public boolean areMessagesOrderCorrect;
	public boolean isAndroidMessagesOrderCorrect;
	public boolean isIosMessagesOrderCorrect;
	public boolean isOsxMessagesOrderCorrect;

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
				user.buildVersion = client.getValue().getVersionInfo();
				user.deviceData = client.getValue().getDeviceInfo();
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
		
		isOsxReceiveMessages = isMessagesReceived(osxReceivedMessages, ExecutionContext.sentMessages, CommonUtils.PLATFORM_NAME_OSX);
		isIosReceiveMessages = isMessagesReceived(iosReceivedMessages, ExecutionContext.sentMessages, CommonUtils.PLATFORM_NAME_IOS);
		isAndroidReceiveMessages = isMessagesReceived(androidReceivedMessages, ExecutionContext.sentMessages, CommonUtils.PLATFORM_NAME_ANDROID);
		areMessagesReceived = isOsxReceiveMessages && isIosReceiveMessages && isAndroidReceiveMessages;
		
		sentMessages = ExecutionContext.sentMessages;
		
		long iosSumReceiveTime = 0;
		long androidSumReceiveTime = 0;
		long osxSumReceiveTime = 0;
		
		areMessagesReceiveTimeCorrect = true;
		isIosReceiveMessagesInTime = true;
		isAndroidReceiveMessagesInTime = true;
		isOsxReceiveMessagesInTime = true;
		
		for (Map.Entry<String, MessageEntry> entry: sentMessages.entrySet()) {
			MessageReport report = new MessageReport();
			report.message = entry.getKey();
			MessageEntry sentMessage = entry.getValue();
			report.sentFrom = entry.getValue().sender;
			report.checkTime = entry.getValue().checkTime;
			if (report.checkTime) {
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
			} else {
				report.isIosReceiveTimeOK = true;
				report.isAndroidReceiveTimeOK = true;
				report.isOsxReceiveTimeOK = true;
			}
			
			if (!report.isIosReceiveTimeOK) isIosReceiveMessagesInTime = false;
			if (!report.isAndroidReceiveTimeOK) isAndroidReceiveMessagesInTime = false;
			if (!report.isOsxReceiveTimeOK) isOsxReceiveMessagesInTime = false;

			messages.add(report);
		}
		
		averageIosReceiveTime = (iosSumReceiveTime / (double)iosReceivedMessages.size())/1000d;
		averageAndroidReceiveTime = (androidSumReceiveTime / (double)androidReceivedMessages.size())/1000d;
		averageOsxReceiveTime = (osxSumReceiveTime / (double)osxReceivedMessages.size())/1000d;
		
		averageIosReceiveTime = Math.round(averageIosReceiveTime*1000)/1000.0d;
		averageAndroidReceiveTime = Math.round(averageAndroidReceiveTime*1000)/1000.0d;
		averageOsxReceiveTime = Math.round(averageOsxReceiveTime*1000)/1000.0d;
		
		if (!isIosReceiveMessagesInTime || !isAndroidReceiveMessagesInTime || !isOsxReceiveMessagesInTime) {
			areMessagesReceiveTimeCorrect = false;
		}
		
		isAndroidStable = (ExecutionContext.androidZeta().getState() != InstanceState.ERROR_CRASHED);
		isIosStable = ExecutionContext.iosZeta().getState() != InstanceState.ERROR_CRASHED;
		isOsxStable = ExecutionContext.osxZeta().getState() != InstanceState.ERROR_CRASHED;
		areClientsStable = isAndroidStable && isIosStable && isOsxStable;
		
		isOsxMessagesOrderCorrect = ExecutionContext.isPlatformMessagesOrderCorrect(CommonUtils.PLATFORM_NAME_OSX);
		isAndroidMessagesOrderCorrect = ExecutionContext.isPlatformMessagesOrderCorrect(CommonUtils.PLATFORM_NAME_ANDROID);
		isIosMessagesOrderCorrect = ExecutionContext.isPlatformMessagesOrderCorrect(CommonUtils.PLATFORM_NAME_IOS);
		areMessagesOrderCorrect = isOsxMessagesOrderCorrect && isAndroidMessagesOrderCorrect && isIosMessagesOrderCorrect;

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
	}
	
	public static boolean isMessagesReceived(
			LinkedHashMap<String, MessageEntry> receivedMessages,
			LinkedHashMap<String, MessageEntry> sentMessages,
			String platform) {
		int sentFromAnotherPlatformCount = 0;

		for (MessageEntry sent: sentMessages.values()) {
			if (!sent.sender.equals(platform) && sent.checkTime) {
				sentFromAnotherPlatformCount++;
			}
		}
		
		return sentFromAnotherPlatformCount == receivedMessages.size();
	}
	
	public static final String toXml(ReportData data) {
		XStream xstream = new XStream(new DomDriver()); // does not require XPP3 library
		xstream.alias("ReportData", ReportData.class);
		xstream.alias("UserInfo", UserReport.class);
		xstream.alias("MessageReport", MessageReport.class);
		xstream.alias("BuildVersion", BuildVersionInfo.class);
		String xml = xstream.toXML(data);
		return xml;
	}
}
