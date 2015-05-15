package com.wearezeta.auto.sync.client.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.client.InstanceState;
import com.wearezeta.auto.sync.client.WireInstance;

public abstract class WireListener extends Thread {

	private static final Logger log = ZetaLogger.getLog(WireListener.class
			.getSimpleName());

	protected WireInstance owner;

	protected Platform platform() {
		return owner.platform();
	}

	public List<MessageEntry> allMessagesList = new ArrayList<MessageEntry>();

	public Map<String, MessageEntry> registeredMessages = new LinkedHashMap<String, MessageEntry>();
	public Map<String, MessageEntry> notReceivedMessages = new LinkedHashMap<String, MessageEntry>();

	public Map<Date, String> pageSources = new LinkedHashMap<Date, String>();

	public WireListener(WireInstance owner) {
		this.owner = owner;
	}

	public abstract String UUID_TEXT_MESSAGE_PATTERN();

	@Override
	public void run() {
		while (owner.getState() != InstanceState.FINISHED
				&& owner.getState() != InstanceState.ERROR_CRASHED) {
			boolean isLastListen = false;
			if (ExecutionContext.allInstancesFinishSending()) {
				isLastListen = true;
			}

			pageSources.put(new Date(), getChatSource());

			if (isLastListen) {
				log.debug(owner.platform()
						+ "listener: all messages sent, received and processed. Closing");
				owner.setState(InstanceState.FINISHED);
			}
			if (owner.getState() == InstanceState.CREATED) {
				owner.setState(InstanceState.SENDING);
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}

		log.debug("Parsing collected page sources...");
		registeredMessages.putAll(parsePageSources(true));
	}

	public abstract String getChatSource();

	public abstract void waitForMessage(String message, boolean checkTime)
			throws Exception;

	public void storePageSource(boolean doScroll) throws Exception {
		if (owner.isEnabled()
				&& owner.getState() != InstanceState.ERROR_CRASHED) {
			storePageSourceImpl(doScroll);
		}
	}

	public abstract void storePageSourceImpl(boolean doScroll) throws Exception;

	public void receiveAllChatMessages(boolean checkTime) {
		if (owner.isEnabled()) {
			allMessagesList = receiveAllChatMessagesImpl(checkTime);
		}
	}

	public abstract List<MessageEntry> receiveAllChatMessagesImpl(
			boolean checkTime);

	public void scrollToTheEndOfConversation() throws Exception {
		com.wearezeta.auto.ios.pages.DialogPage dialogPage = com.wearezeta.auto.ios.pages.PagesCollection.dialogPage;
		dialogPage.scrollToTheEndOfConversation();
	}

	protected Map<String, MessageEntry> parsePageSources(
			boolean checkTime) {
		Map<String, MessageEntry> result = new LinkedHashMap<String, MessageEntry>();
		for (Map.Entry<Date, String> source : pageSources.entrySet()) {
			final Pattern pattern = Pattern
					.compile(UUID_TEXT_MESSAGE_PATTERN());
			final Matcher matcher = pattern.matcher(source.getValue());
			while (matcher.find()) {
				MessageEntry currentMessage = new MessageEntry("text",
						matcher.group(1), source.getKey(), checkTime);
				String text = currentMessage.messageContent;
				if (result.get(text) == null) {
					result.put(text, currentMessage);
				}
			}
		}
		return result;
	}

	public Map<Date, String> getPageSources() {
		return pageSources;
	}

	public void setPageSources(LinkedHashMap<Date, String> pageSources) {
		this.pageSources = pageSources;
	}

	public boolean isSessionLost() {
		return ((ZetaDriver) PlatformDrivers.getInstance().getDriver(
				owner.platform())).isSessionLost();
	}
}
