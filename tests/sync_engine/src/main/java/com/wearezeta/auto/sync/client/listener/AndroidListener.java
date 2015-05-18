package com.wearezeta.auto.sync.client.listener;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.client.platform.AndroidWireInstance;

public class AndroidListener extends WireListener {

	private static final Logger log = ZetaLogger.getLog(AndroidListener.class
			.getSimpleName());

	public AndroidListener(AndroidWireInstance parent) {
		super(parent);
	}

	@Override
	public void waitForMessage(String message, boolean checkTime) throws Exception {
		DialogPage dialogPage = PagesCollection.dialogPage;
		MessageEntry entry = dialogPage.receiveMessage(message, checkTime);
		if (entry != null) {
			registeredMessages.put(message, entry);
		} else {
			notReceivedMessages.put(message,
					ExecutionContext.sentMessages.get(message));
		}
	}

	@Override
	public List<MessageEntry> receiveAllChatMessagesImpl(boolean checkTime) {
		try {
			return PagesCollection.dialogPage.listAllMessages(checkTime);
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

	public static final String UUID_TEXT_MESSAGE_PATTERN = "<android.widget.TextView[^>]*text=\"([a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12})\"[^>]*/>";

	@Override
	public String UUID_TEXT_MESSAGE_PATTERN() {
		return UUID_TEXT_MESSAGE_PATTERN;
	}

	@Override
	public void storePageSourceImpl(boolean doScroll) {
		// do nothing. Android test doesn't require page sources
	}

}
