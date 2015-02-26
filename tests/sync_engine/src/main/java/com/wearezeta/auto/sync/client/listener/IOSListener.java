package com.wearezeta.auto.sync.client.listener;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.ios.pages.DialogPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.client.platform.IOSWireInstance;

public class IOSListener extends WireListener {

	private static final Logger log = ZetaLogger.getLog(IOSListener.class
			.getSimpleName());

	public IOSListener(IOSWireInstance parent) {
		super(parent);
	}

	@Override
	public void waitForMessage(String message, boolean checkTime) {
		DialogPage dialogPage = PagesCollection.dialogPage;
		MessageEntry entry = dialogPage.receiveMessage(message, checkTime);
		if (entry != null) {
			registeredMessages.put(message, entry);
		} else {
			notReceivedMessages.put(message,
					ExecutionContext.sentMessages.get(message));
		}
	}

	private static final String UUID_TEXT_MESSAGE_PATTERN = "<UIATextView[^>]*value=\"[^\"a-z0-9]*([a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12})\"[^>]*>\\s*</UIATextView>";

	@Override
	public ArrayList<MessageEntry> receiveAllChatMessages(boolean checkTime) {
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
			return PagesCollection.dialogPage.getPageSource();
		} catch (Exception e) {
			// TODO: process exception
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String UUID_TEXT_MESSAGE_PATTERN() {
		return UUID_TEXT_MESSAGE_PATTERN;
	}

}
