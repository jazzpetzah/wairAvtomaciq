package com.wearezeta.auto.sync.client;

import java.util.Date;


import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;

import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.SyncEngineUtil;

public class ZetaSender extends Thread {
	private static final Logger log = ZetaLogger.getLog(ZetaSender.class
			.getSimpleName());

	private ZetaInstance parent;
	private int toSend;
	private String platform() { return parent.getPlatform(); }

	public static Date sendingStartDate = null;
	
	public ZetaSender(ZetaInstance parent, int numberOfMessages) {
		this.parent = parent;
		this.toSend = numberOfMessages;
	}

	@Override
	public void run() {
		while (toSend > 0) {
			if (!SyncEngineUtil.isPlatformCorrect(platform())) {
				log.debug("Incorrect platform is set for client: " + platform());
			}
			
			if (parent.getState() != InstanceState.SENDING) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}

			log.debug(toSend + " messages to send from " + platform());
			

			String message = CommonUtils.generateGUID();
			sendTextMessage(SyncEngineUtil.CHAT_NAME, message);
			toSend--;
			
			if (parent.getMessagesSendingInterval() > 0) {
				try {
					Thread.sleep(parent.getMessagesSendingInterval() * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		log.debug("All messages sent. Waiting for listener.");
		parent.setState(InstanceState.FINAL_LISTENING);
	}
	
	private ClientUser backendClient() {
		return parent.getUserInstance();
	}

	public void sendTextMessage(String chat, String message) {
		sendTextMessage(chat, message, true);
	}
	
	public void sendTextMessage(String chat, String message, boolean checkTime) {
		if (sendingStartDate == null) {
			sendingStartDate = new Date();
		}
		try {
		if (parent.getIsSendUsingBackend()) {
			sendTextMessageBackend(SyncEngineUtil.CHAT_NAME, message);
		}
		else if (platform().equals(CommonUtils.PLATFORM_NAME_ANDROID)) {
			sendTextMessageAndroid(message);
		} else if (platform().equals(CommonUtils.PLATFORM_NAME_OSX)) {
			sendTextMessageOsx(message);
		} else if (platform().equals(CommonUtils.PLATFORM_NAME_IOS)) {
			sendTextMessageIos(message);
		}
		} catch (Throwable e) {
			//TODO: register error
			log.error(e.getMessage());
			e.printStackTrace();
		}

		MessageEntry entry = new MessageEntry("text", message,
				platform(), new Date(), checkTime);
		ExecutionContext.addNewSentTextMessage(entry, checkTime);
	}
	
	private void sendTextMessageAndroid(String message) throws Throwable {
		com.wearezeta.auto.android.DialogPageSteps steps = new com.wearezeta.auto.android.DialogPageSteps();
		steps.ITypeTheMessageAndSendIt(message);
	}
	
	private void sendTextMessageOsx(String message) {
		com.wearezeta.auto.osx.steps.ConversationPageSteps steps = new com.wearezeta.auto.osx.steps.ConversationPageSteps();
		steps.IWriteMessage(message);
		steps.WhenISendMessage();
	}
	
	private void sendTextMessageIos(String message) throws Throwable {
		com.wearezeta.auto.ios.DialogPageSteps steps = new com.wearezeta.auto.ios.DialogPageSteps();
		com.wearezeta.auto.ios.pages.DialogPage page = com.wearezeta.auto.ios.pages.PagesCollection.dialogPage;
		page.ScrollToLastMessage();
		steps.ISendUsingScriptPredefinedMessage(message);
	}
	
	public void sendAllMessagesIos(String messages[], boolean checkTime) {
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
	
	private void sendTextMessageBackend(String chat, String message) throws Exception {
		BackEndREST.sendDialogMessageByChatName(backendClient(), chat, message);
	}
}
