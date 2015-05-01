package com.wearezeta.auto.sync.client.sender;

import java.util.Date;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.SEConstants;
import com.wearezeta.auto.sync.SyncEngineUtil;
import com.wearezeta.auto.sync.client.InstanceState;
import com.wearezeta.auto.sync.client.WireInstance;

public abstract class WireSender extends Thread {

	private static final Logger log = ZetaLogger.getLog(WireSender.class
			.getSimpleName());

	private WireInstance owner;
	private int toSend;

	private Platform platform() {
		return owner.platform();
	}

	public static Date sendingStartDate = null;

	public WireSender(WireInstance parent, int numberOfMessages) {
		this.owner = parent;
		this.toSend = numberOfMessages;
	}

	@Override
	public void run() {
		while (toSend > 0) {
			if (!SyncEngineUtil.isPlatformCorrect(platform())) {
				log.debug("Incorrect platform is set for client: " + platform());
			}

			if (owner.getState() != InstanceState.SENDING) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}

			log.debug(toSend + " messages to send from " + platform());

			String message = CommonUtils.generateGUID();
			sendTextMessage(SEConstants.Common.TEST_CONVERSATION, message);
			toSend--;

			if (owner.getMessagesSendingInterval() > 0) {
				try {
					Thread.sleep(owner.getMessagesSendingInterval() * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		log.debug("All messages sent. Waiting for listener.");
		owner.setState(InstanceState.FINAL_LISTENING);
	}

	private ClientUser backendClient() {
		return owner.getUserInstance();
	}

	public void sendTextMessage(String chat, String message) {
		sendTextMessage(chat, message, true);
	}

	public void sendTextMessage(String chat, String message, boolean checkTime) {
		if (sendingStartDate == null) {
			sendingStartDate = new Date();
		}

		try {
			if (owner.isSendToBackend()) {
				sendTextMessageBackend(SEConstants.Common.TEST_CONVERSATION,
						message);
			} else {
				sendTextMessageClient(message);
			}
		} catch (Exception e) {
			// TODO: register error
			log.error(e.getMessage());
			e.printStackTrace();
		}

		MessageEntry entry = new MessageEntry("text", message, platform(),
				new Date(), checkTime);
		ExecutionContext.addNewSentTextMessage(entry, checkTime);
	}

	protected abstract void sendTextMessageClient(String message)
			throws Exception;

	public void sendAllMessagesIos(String messages[], boolean checkTime)
			throws Exception {
		com.wearezeta.auto.ios.pages.DialogPage dialogPage = com.wearezeta.auto.ios.pages.PagesCollection.dialogPage;
		Date sendDate = new Date();
		try {
			dialogPage.sendMessagesUsingScript(messages);
			for (int j = 0; j < messages.length; j++) {
				MessageEntry entry = new MessageEntry("text", messages[j],
						platform(), sendDate, checkTime);
				ExecutionContext.addNewSentTextMessage(entry, checkTime);
			}
		} catch (WebDriverException e) {
			log.fatal("Failed to send messages from iOS client.");
		}
	}

	private void sendTextMessageBackend(String chat, String message)
			throws Exception {
		BackendAPIWrappers.sendDialogMessageByChatName(backendClient(), chat,
				message);
	}
}
