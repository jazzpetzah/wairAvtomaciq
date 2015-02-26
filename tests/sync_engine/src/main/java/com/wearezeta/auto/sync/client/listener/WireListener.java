package com.wearezeta.auto.sync.client.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
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

	protected WireInstance parent;

	protected Platform platform() {
		return parent.platform();
	}

	public LinkedHashMap<String, MessageEntry> registeredMessages = new LinkedHashMap<String, MessageEntry>();
	public LinkedHashMap<String, MessageEntry> notReceivedMessages = new LinkedHashMap<String, MessageEntry>();

	public LinkedHashMap<Date, String> pageSources = new LinkedHashMap<Date, String>();

	public WireListener(WireInstance parent) {
		this.parent = parent;
	}

	public abstract String UUID_TEXT_MESSAGE_PATTERN();

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
				log.debug(parent.platform()
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
		registeredMessages.putAll(parsePageSources(true));
	}

	public abstract String getChatSource();

	public abstract void waitForMessage(String message, boolean checkTime);

	public abstract ArrayList<MessageEntry> receiveAllChatMessages(
			boolean checkTime);

	public void scrollToTheEndOfConversation() {
		com.wearezeta.auto.ios.pages.DialogPage dialogPage = com.wearezeta.auto.ios.pages.PagesCollection.dialogPage;
		dialogPage.scrollToTheEndOfConversation();
	}

	protected LinkedHashMap<String, MessageEntry> parsePageSources(
			boolean checkTime) {
		LinkedHashMap<String, MessageEntry> result = new LinkedHashMap<String, MessageEntry>();
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

	public LinkedHashMap<Date, String> getPageSources() {
		return pageSources;
	}

	public void setPageSources(LinkedHashMap<Date, String> pageSources) {
		this.pageSources = pageSources;
	}

	public boolean isSessionLost() {
		return ((ZetaDriver) PlatformDrivers.getInstance().getDriver(
				parent.platform())).isSessionLost();
	}
}
