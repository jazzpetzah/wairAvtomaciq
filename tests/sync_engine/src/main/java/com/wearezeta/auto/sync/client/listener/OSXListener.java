package com.wearezeta.auto.sync.client.listener;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.osx.pages.ConversationPage;
import com.wearezeta.auto.osx.pages.PagesCollection;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.client.platform.OSXWireInstance;

public class OSXListener extends WireListener {

	private static final Logger log = ZetaLogger.getLog(OSXListener.class
			.getSimpleName());

	public OSXListener(OSXWireInstance parent) {
		super(parent);
	}

	@Override
	public void waitForMessage(String message, boolean checkTime) {
		try {
			PagesCollection.conversationPage = (ConversationPage) PagesCollection.loginPage
					.instantiatePage(ConversationPage.class);
			ConversationPage conversationPage = PagesCollection.conversationPage;
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

	private static final String UUID_TEXT_MESSAGE_PATTERN = "<AXGroup[^>]*>\\s*<AXStaticText[^>]*AXValue=\"([a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12})\"[^>]*/>\\s*</AXGroup>";

	@Override
	public ArrayList<MessageEntry> receiveAllChatMessagesImpl(boolean checkTime) {
		try {
			return new ArrayList<MessageEntry>(parsePageSources(checkTime)
					.values());
		} catch (Exception e) {
			// TODO: process exception
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return new ArrayList<MessageEntry>();
	}

	@Override
	public String getChatSource() {
		try {
			PagesCollection.conversationPage = (ConversationPage) PagesCollection.loginPage
					.instantiatePage(ConversationPage.class);
			return PagesCollection.conversationPage.getPageSource();
		} catch (Exception e) {
			// TODO: process exception
			log.error(e.getMessage());
		}
		return "";
	}

	@Override
	public String UUID_TEXT_MESSAGE_PATTERN() {
		return UUID_TEXT_MESSAGE_PATTERN;
	}

	@Override
	public void storePageSourceImpl(boolean doScroll) {
		String chatSource = getChatSource();
		pageSources.put(new Date(), chatSource);
	}
}
