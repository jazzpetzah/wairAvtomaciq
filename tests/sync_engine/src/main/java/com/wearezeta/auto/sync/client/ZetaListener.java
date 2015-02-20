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

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.osx.common.OSXExecutionContext;
import com.wearezeta.auto.sync.ExecutionContext;

public class ZetaListener extends Thread {
	private static final Logger log = ZetaLogger.getLog(ZetaListener.class
			.getSimpleName());

	private ZetaInstance parent;

	private Platform platform() {
		return parent.getPlatform();
	}

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
		while (parent.getState() != InstanceState.FINISHED
				&& parent.getState() != InstanceState.ERROR_CRASHED) {
			boolean isLastListen = false;
			if (ExecutionContext.allInstancesFinishSending()) {
				isLastListen = true;
			}

			pageSources.put(new Date(), getChatSource());

			if (isLastListen) {
				log.debug(parent.getPlatform()
						+ "listener: all messages sent, received and processed. Closing");
				parent.setState(InstanceState.FINISHED);
			}
			if (parent.getState() == InstanceState.CREATED) {
				parent.setState(InstanceState.SENDING);
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}

		log.debug("Parsing collected page sources...");
		for (Entry<Date, String> pageSource : pageSources.entrySet()) {
			final Pattern pattern;
			final Matcher matcher;
			switch (platform()) {
			case Android:
				pattern = Pattern.compile(ANDROID_UUID_TEXT_MESSAGE_PATTERN);
				matcher = pattern.matcher(pageSource.getValue());
				while (matcher.find()) {
					MessageEntry currentMessage = new MessageEntry("text",
							matcher.group(1), pageSource.getKey(), true);
					String text = currentMessage.messageContent;
					if (registeredMessages.get(text) == null) {
						registeredMessages.put(text, currentMessage);
					}
				}
				break;
			case Mac:
				pattern = Pattern.compile(OSX_UUID_TEXT_MESSAGE_PATTERN);
				matcher = pattern.matcher(pageSource.getValue());
				while (matcher.find()) {
					MessageEntry currentMessage = new MessageEntry("text",
							matcher.group(1), pageSource.getKey(), true);
					String text = currentMessage.messageContent;
					if (registeredMessages.get(text) == null) {
						registeredMessages.put(text, currentMessage);
					}
				}
				break;
			case iOS:
				pattern = Pattern.compile(IOS_UUID_TEXT_MESSAGE_PATTERN);
				matcher = pattern.matcher(pageSource.getValue());
				while (matcher.find()) {
					MessageEntry currentMessage = new MessageEntry("text",
							matcher.group(1), pageSource.getKey(), true);
					String text = currentMessage.messageContent;
					if (registeredMessages.get(text) == null) {
						registeredMessages.put(text, currentMessage);
					}
				}
				break;
			default:
				break;
			}
		}
	}

	public String getChatSource() {
		try {
			final BasePage dialogPage;
			switch (platform()) {
			case Android:
				dialogPage = com.wearezeta.auto.android.pages.PagesCollection.dialogPage;
				return dialogPage.getPageSource();
			case Mac:
				com.wearezeta.auto.osx.pages.PagesCollection.conversationPage = new com.wearezeta.auto.osx.pages.ConversationPage(
						OSXExecutionContext.appiumUrl,
						OSXExecutionContext.wirePath);
				dialogPage = com.wearezeta.auto.osx.pages.PagesCollection.conversationPage;
				return dialogPage.getPageSource();
			case iOS:
				dialogPage = com.wearezeta.auto.ios.pages.PagesCollection.dialogPage;
				return dialogPage.getPageSource();
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: process exception
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	public void scrollToTheEndOfConversation() {
		if (platform() == Platform.iOS) {
			com.wearezeta.auto.ios.pages.DialogPage dialogPage = com.wearezeta.auto.ios.pages.PagesCollection.dialogPage;
			dialogPage.scrollToTheEndOfConversation();
		}
	}

	public ArrayList<MessageEntry> receiveChatMessages(boolean checkTime) {
		try {
			switch (platform()) {
			case Android:
				return receiveChatMessagesAndroid(checkTime);
			case Mac:
				return receiveChatMessagesOsx(checkTime);
			case iOS:
				return receiveChatMessagesIos(checkTime);
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: process exception
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return new ArrayList<MessageEntry>();
	}

	public void waitForMessageAndroid(String message, boolean checkTime) {
		com.wearezeta.auto.android.pages.DialogPage dialogPage = com.wearezeta.auto.android.pages.PagesCollection.dialogPage;
		MessageEntry entry = dialogPage.receiveMessage(message, checkTime);
		if (entry != null) {
			registeredMessages.put(message, entry);
		} else {
			notReceivedMessages.put(message,
					ExecutionContext.sentMessages.get(message));
		}
	}

	public void waitForMessageIos(String message, boolean checkTime) {
		com.wearezeta.auto.ios.pages.DialogPage dialogPage = com.wearezeta.auto.ios.pages.PagesCollection.dialogPage;
		MessageEntry entry = dialogPage.receiveMessage(message, checkTime);
		if (entry != null) {
			registeredMessages.put(message, entry);
		} else {
			notReceivedMessages.put(message,
					ExecutionContext.sentMessages.get(message));
		}
	}

	public void waitForMessageOsx(String message, boolean checkTime) {
		try {
			com.wearezeta.auto.osx.pages.PagesCollection.conversationPage = new com.wearezeta.auto.osx.pages.ConversationPage(
					OSXExecutionContext.appiumUrl, OSXExecutionContext.wirePath);
			com.wearezeta.auto.osx.pages.ConversationPage conversationPage = com.wearezeta.auto.osx.pages.PagesCollection.conversationPage;
			MessageEntry entry = conversationPage.receiveMessage(message,
					checkTime);
			if (entry != null) {
				registeredMessages.put(message, entry);
			} else {
				notReceivedMessages.put(message,
						ExecutionContext.sentMessages.get(message));
			}
		} catch (Exception e) {
			// TODO: process exception
			log.error(e.getMessage());
		}
	}

	private ArrayList<MessageEntry> receiveChatMessagesAndroid(boolean checkTime) {
		com.wearezeta.auto.android.pages.DialogPage dialogPage = com.wearezeta.auto.android.pages.PagesCollection.dialogPage;
		return dialogPage.listAllMessages(checkTime);
	}

	private ArrayList<MessageEntry> parsePageSources(String messagePattern,
			boolean checkTime) {
		LinkedHashMap<String, MessageEntry> result = new LinkedHashMap<String, MessageEntry>();
		for (Map.Entry<Date, String> source : pageSources.entrySet()) {
			Pattern pattern = Pattern.compile(messagePattern);
			Matcher matcher = pattern.matcher(source.getValue());
			while (matcher.find()) {
				result.put(matcher.group(1),
						new MessageEntry("text", matcher.group(1), new Date(),
								checkTime));
			}
		}
		return new ArrayList<MessageEntry>(result.values());
	}

	private static final String UUID_OSX_TEXT_MESSAGE_PATTERN = "<AXGroup[^>]*>\\s*<AXStaticText[^>]*AXValue=\"([a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12})\"[^>]*/>\\s*</AXGroup>";

	private ArrayList<MessageEntry> receiveChatMessagesOsx(boolean checkTime)
			throws MalformedURLException, IOException {
		return parsePageSources(UUID_OSX_TEXT_MESSAGE_PATTERN, checkTime);
	}

	private static final String UUID_IOS_TEXT_MESSAGE_PATTERN = "<UIATextView[^>]*value=\"[^\"a-z0-9]*([a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12})\"[^>]*>\\s*</UIATextView>";

	private ArrayList<MessageEntry> receiveChatMessagesIos(boolean checkTime)
			throws Exception {
		// com.wearezeta.auto.ios.pages.DialogPage dialogPage =
		// com.wearezeta.auto.ios.pages.PagesCollection.dialogPage;
		// return dialogPage.listAllMessages(checkTime);
		return parsePageSources(UUID_IOS_TEXT_MESSAGE_PATTERN, checkTime);
	}

	public LinkedHashMap<Date, String> getPageSources() {
		return pageSources;
	}

	public void setPageSources(LinkedHashMap<Date, String> pageSources) {
		this.pageSources = pageSources;
	}

	public boolean isSessionLost() {
		return ((ZetaDriver) PlatformDrivers.getInstance().getDriver(
				parent.getPlatform())).isSessionLost();
	}
}
