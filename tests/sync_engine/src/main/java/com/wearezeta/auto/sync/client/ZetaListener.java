package com.wearezeta.auto.sync.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

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
	
	public ZetaListener(ZetaInstance parent) {
		this.parent = parent;
	}
	
	@Override
	public void run() {
		while (parent.getState() != InstanceState.FINISHED) {
			boolean isLastListen = false;
			if (ExecutionContext.allInstancesFinishSending()) {
				isLastListen = true;
			}
			
			ArrayList<MessageEntry> currentMessages = receiveChatMessages();

			for (MessageEntry currentMessage: currentMessages) {
				String text = currentMessage.messageContent;
				if (registeredMessages.get(text) == null) {
					registeredMessages.put(text, currentMessage);
				}
			}

			if (isLastListen) {
				log.debug(parent.getPlatform() + " processing is finished.");
				parent.setState(InstanceState.FINISHED);
			}
			if (parent.getState() == InstanceState.CREATED) {
				parent.setState(InstanceState.SENDING);
			}
		}
	}

	public ArrayList<MessageEntry> receiveChatMessages() {
		try {
		if (platform().equals(CommonUtils.PLATFORM_NAME_ANDROID)) {
			return receiveChatMessagesAndroid();
		} else if (platform().equals(CommonUtils.PLATFORM_NAME_OSX)) {
			return receiveChatMessagesOsx();
		} else if (platform().equals(CommonUtils.PLATFORM_NAME_IOS)) {
			return receiveChatMessagesIos();
		}
		} catch (Exception e) {
			//TODO: process exception
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return new ArrayList<MessageEntry>();
	}
	
	public void waitForMessageAndroid(String message) {
		com.wearezeta.auto.android.pages.DialogPage dialogPage = 
				com.wearezeta.auto.android.pages.PagesCollection.dialogPage;
		MessageEntry entry = dialogPage.receiveMessage(message);
		if (entry != null) {
			registeredMessages.put(message, entry);
		} else {
			notReceivedMessages.put(message, ExecutionContext.sentMessages.get(message));
		}
	}
	
	public void waitForMessageIos(String message) {
		com.wearezeta.auto.ios.pages.DialogPage dialogPage = 
				com.wearezeta.auto.ios.pages.PagesCollection.dialogPage;
		MessageEntry entry = dialogPage.receiveMessage(message);
		if (entry != null) {
			registeredMessages.put(message, entry);
		} else {
			notReceivedMessages.put(message, ExecutionContext.sentMessages.get(message));
		}
	}

	public void waitForMessageOsx(String message) {
		try {
			com.wearezeta.auto.osx.steps.CommonSteps.senderPages.setConversationPage(
				new com.wearezeta.auto.osx.pages.ConversationPage(
						CommonUtils.getOsxAppiumUrlFromConfig(ContactListPageSteps.class),
						CommonUtils.getOsxApplicationPathFromConfig(ContactListPageSteps.class)));
			com.wearezeta.auto.osx.pages.ConversationPage conversationPage =
				com.wearezeta.auto.osx.steps.CommonSteps.senderPages.getConversationPage();
			MessageEntry entry = conversationPage.receiveMessage(message);
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
	
	private ArrayList<MessageEntry> receiveChatMessagesAndroid() {
		com.wearezeta.auto.android.pages.DialogPage dialogPage = 
				com.wearezeta.auto.android.pages.PagesCollection.dialogPage;
		return dialogPage.listAllMessages();
	}
	
	private ArrayList<MessageEntry> receiveChatMessagesOsx() throws MalformedURLException, IOException {
		com.wearezeta.auto.osx.steps.CommonSteps.senderPages.setConversationPage(
				new com.wearezeta.auto.osx.pages.ConversationPage(
						CommonUtils.getOsxAppiumUrlFromConfig(ContactListPageSteps.class),
						CommonUtils.getOsxApplicationPathFromConfig(ContactListPageSteps.class)));
		com.wearezeta.auto.osx.pages.ConversationPage conversationPage =
				com.wearezeta.auto.osx.steps.CommonSteps.senderPages.getConversationPage();
		return conversationPage.listAllMessages();
	}
	
	private ArrayList<MessageEntry> receiveChatMessagesIos() {
		com.wearezeta.auto.ios.pages.DialogPage dialogPage =
				com.wearezeta.auto.ios.pages.PagesCollection.dialogPage;
		return dialogPage.listAllMessages();
	}
}
