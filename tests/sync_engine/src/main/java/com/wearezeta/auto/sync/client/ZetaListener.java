package com.wearezeta.auto.sync.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.osx.steps.ContactListPageSteps;
import com.wearezeta.auto.sync.ExecutionContext;

public class ZetaListener extends Thread {
	private static final Logger log = ZetaLogger.getLog(ZetaListener.class.getSimpleName());
	
	private ZetaInstance parent;
	private String platform() { return parent.getPlatform(); }
	
	public LinkedHashMap<String, MessageEntry> registeredMessages = new LinkedHashMap<String, MessageEntry>();
	public LinkedHashMap<String, MessageEntry> notReceivedMessages = new LinkedHashMap<String, MessageEntry>();
	
	public LinkedHashMap<Date, String> pageSources = new LinkedHashMap<Date, String>();
	
	public static final String ANDROID_UUID_TEXT_MESSAGE_PATTERN = "<android.widget.TextView[^>]*text=\"([a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12})\"[^>]*/>";
	public static final String IOS_UUID_TEXT_MESSAGE_PATTERN = "<UIATextView[^>]*value=\"([a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12})\"[^>]*>\\s*</UIATextView>";
	public static final String OSX_UUID_TEXT_MESSAGE_PATTERN = "<AXGroup[^>]*>\\s*<AXStaticText[^>]*AXValue=\"([a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12})\"[^>]*/>\\s*</AXGroup>";
	
	public ZetaListener(ZetaInstance parent) {
		this.parent = parent;
	}
	
	@Override
	public void run() {
		while (parent.getState() != InstanceState.FINISHED && parent.getState() != InstanceState.ERROR_CRASHED) {
			boolean isLastListen = false;
			if (ExecutionContext.allInstancesFinishSending()) {
				isLastListen = true;
			}
			
			pageSources.put(new Date(), getChatSource());
			
			if (isLastListen) {
				log.debug(parent.getPlatform() + "listener: all messages sent, received and processed. Closing");
				parent.setState(InstanceState.FINISHED);
			}
			if (parent.getState() == InstanceState.CREATED) {
				parent.setState(InstanceState.SENDING);
			}
			try { Thread.sleep(50); } catch (InterruptedException e) { }
		}
		
		log.debug("Parsing collected page sources...");
		for (Entry<Date, String> pageSource: pageSources.entrySet()) {
			if (platform().equals(CommonUtils.PLATFORM_NAME_ANDROID)) {
				Pattern pattern = Pattern.compile(ANDROID_UUID_TEXT_MESSAGE_PATTERN);
				Matcher matcher = pattern.matcher(pageSource.getValue());
				while (matcher.find()) {
					MessageEntry currentMessage = new MessageEntry("text", matcher.group(1), pageSource.getKey(), true);
					String text = currentMessage.messageContent;
					if (registeredMessages.get(text) == null) {
						registeredMessages.put(text, currentMessage);
					}
				}
			} else if (platform().equals(CommonUtils.PLATFORM_NAME_OSX)) {
				Pattern pattern = Pattern.compile(OSX_UUID_TEXT_MESSAGE_PATTERN);
				Matcher matcher = pattern.matcher(pageSource.getValue());
				while (matcher.find()) {
					MessageEntry currentMessage = new MessageEntry("text", matcher.group(1), pageSource.getKey(), true);
					String text = currentMessage.messageContent;
					if (registeredMessages.get(text) == null) {
						registeredMessages.put(text, currentMessage);
					}
				}
			} else if (platform().equals(CommonUtils.PLATFORM_NAME_IOS)) {
				Pattern pattern = Pattern.compile(IOS_UUID_TEXT_MESSAGE_PATTERN);
				Matcher matcher = pattern.matcher(pageSource.getValue());
				while (matcher.find()) {
					MessageEntry currentMessage = new MessageEntry("text", matcher.group(1), pageSource.getKey(), true);
					String text = currentMessage.messageContent;
					if (registeredMessages.get(text) == null) {
						registeredMessages.put(text, currentMessage);
					}
				}
			}
		}
	}

	public String getChatSource() {
		try {
			if (platform().equals(CommonUtils.PLATFORM_NAME_ANDROID)) {
				com.wearezeta.auto.android.pages.DialogPage dialogPage = 
						com.wearezeta.auto.android.pages.PagesCollection.dialogPage;
				return dialogPage.getPageSource();
			} else if (platform().equals(CommonUtils.PLATFORM_NAME_OSX)) {
				com.wearezeta.auto.osx.steps.CommonSteps.senderPages.setConversationPage(
						new com.wearezeta.auto.osx.pages.ConversationPage(
								CommonUtils.getOsxAppiumUrlFromConfig(ContactListPageSteps.class),
								CommonUtils.getOsxApplicationPathFromConfig(ContactListPageSteps.class)));
				com.wearezeta.auto.osx.pages.ConversationPage conversationPage =
						com.wearezeta.auto.osx.steps.CommonSteps.senderPages.getConversationPage();
				return conversationPage.getPageSource();
			} else if (platform().equals(CommonUtils.PLATFORM_NAME_IOS)) {
				com.wearezeta.auto.ios.pages.DialogPage dialogPage =
						com.wearezeta.auto.ios.pages.PagesCollection.dialogPage;
				return dialogPage.getPageSource();
			}
			} catch (Exception e) {
				//TODO: process exception
				log.error(e.getMessage());
				e.printStackTrace();
			}
			return "";
	}
	
	public void scrollToTheEndOfConversation() {
		if (platform().equals(CommonUtils.PLATFORM_NAME_IOS)) {
			com.wearezeta.auto.ios.pages.DialogPage dialogPage =
						com.wearezeta.auto.ios.pages.PagesCollection.dialogPage;
			dialogPage.scrollToTheEndOfConversation();
		}
	}
	
	public ArrayList<MessageEntry> receiveChatMessages(boolean checkTime) {
		try {
		if (platform().equals(CommonUtils.PLATFORM_NAME_ANDROID)) {
			return receiveChatMessagesAndroid(checkTime);
		} else if (platform().equals(CommonUtils.PLATFORM_NAME_OSX)) {
			return receiveChatMessagesOsx(checkTime);
		} else if (platform().equals(CommonUtils.PLATFORM_NAME_IOS)) {
			return receiveChatMessagesIos(checkTime);
		}
		} catch (Throwable e) {
			//TODO: process exception
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return new ArrayList<MessageEntry>();
	}
	
	public void waitForMessageAndroid(String message, boolean checkTime) {
		com.wearezeta.auto.android.pages.DialogPage dialogPage = 
				com.wearezeta.auto.android.pages.PagesCollection.dialogPage;
		MessageEntry entry = dialogPage.receiveMessage(message, checkTime);
		if (entry != null) {
			registeredMessages.put(message, entry);
		} else {
			notReceivedMessages.put(message, ExecutionContext.sentMessages.get(message));
		}
	}
	
	public void waitForMessageIos(String message, boolean checkTime) {
		com.wearezeta.auto.ios.pages.DialogPage dialogPage = 
				com.wearezeta.auto.ios.pages.PagesCollection.dialogPage;
		MessageEntry entry = dialogPage.receiveMessage(message, checkTime);
		if (entry != null) {
			registeredMessages.put(message, entry);
		} else {
			notReceivedMessages.put(message, ExecutionContext.sentMessages.get(message));
		}
	}

	public void waitForMessageOsx(String message, boolean checkTime) {
		try {
			com.wearezeta.auto.osx.steps.CommonSteps.senderPages.setConversationPage(
				new com.wearezeta.auto.osx.pages.ConversationPage(
						CommonUtils.getOsxAppiumUrlFromConfig(ContactListPageSteps.class),
						CommonUtils.getOsxApplicationPathFromConfig(ContactListPageSteps.class)));
			com.wearezeta.auto.osx.pages.ConversationPage conversationPage =
				com.wearezeta.auto.osx.steps.CommonSteps.senderPages.getConversationPage();
			MessageEntry entry = conversationPage.receiveMessage(message, checkTime);
			if (entry != null) {
				registeredMessages.put(message, entry);
			} else {
				notReceivedMessages.put(message, ExecutionContext.sentMessages.get(message));
			}
		} catch (Exception e) {
			//TODO: process exception
			log.error(e.getMessage());
		}
	}
	
	private ArrayList<MessageEntry> receiveChatMessagesAndroid(boolean checkTime) {
		com.wearezeta.auto.android.pages.DialogPage dialogPage = 
				com.wearezeta.auto.android.pages.PagesCollection.dialogPage;
		return dialogPage.listAllMessages(checkTime);
	}
	
	private ArrayList<MessageEntry> parsePageSources(String messagePattern, boolean checkTime) {
		LinkedHashMap<String, MessageEntry> result = new LinkedHashMap<String, MessageEntry>();
		for (Map.Entry<Date, String> source: pageSources.entrySet()) {
			Pattern pattern = Pattern.compile(messagePattern);
			Matcher matcher = pattern.matcher(source.getValue());
			while (matcher.find()) {
					result.put(matcher.group(1), new MessageEntry("text", matcher.group(1), new Date(), checkTime));
			}
		}
		return new ArrayList<MessageEntry>(result.values());
	}
	
	private static final String UUID_OSX_TEXT_MESSAGE_PATTERN = "<AXGroup[^>]*>\\s*<AXStaticText[^>]*AXValue=\"([a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12})\"[^>]*/>\\s*</AXGroup>";
	private ArrayList<MessageEntry> receiveChatMessagesOsx(boolean checkTime) throws MalformedURLException, IOException {
		return parsePageSources(UUID_OSX_TEXT_MESSAGE_PATTERN, checkTime);
	}
	
	private static final String UUID_IOS_TEXT_MESSAGE_PATTERN = "<UIATextView[^>]*value=\"[^\"a-z0-9]*([a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12})\"[^>]*>\\s*</UIATextView>";
	private ArrayList<MessageEntry> receiveChatMessagesIos(boolean checkTime) throws Exception, Throwable {
//		com.wearezeta.auto.ios.pages.DialogPage dialogPage = 
//				com.wearezeta.auto.ios.pages.PagesCollection.dialogPage;
//		return dialogPage.listAllMessages(checkTime);
		return parsePageSources(UUID_IOS_TEXT_MESSAGE_PATTERN, checkTime);
	}

	public LinkedHashMap<Date, String> getPageSources() {
		return pageSources;
	}

	public void setPageSources(LinkedHashMap<Date, String> pageSources) {
		this.pageSources = pageSources;
	}
	
	public boolean isSessionLost() {
		return com.wearezeta.auto.common.BasePage.getDriver(parent.getPlatform()).isSessionLost();
	}
}
