package com.wearezeta.auto.sync.client;

import java.util.Date;


import org.apache.log4j.Logger;

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
	private String platform;

	public static Date sendingStartDate = null;
	
	public ZetaSender(ZetaInstance parent, String platform, int numberOfMessages) {
		this.parent = parent;
		this.toSend = numberOfMessages;
		this.platform = platform;
	}

	@Override
	public void run() {
		while (toSend > 0) {
			if (parent.getState() != InstanceState.SENDING) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}

			log.debug(toSend + " messages to send from " + platform);
			
			if (sendingStartDate == null) {
				sendingStartDate = new Date();
			}

			String message = CommonUtils.generateGUID();
			if (parent.getIsSendUsingBackend()) {
				backendSendTextMessage(SyncEngineUtil.CHAT_NAME, message);
				toSend--;
			}
			else if (platform.equals(CommonUtils.PLATFORM_NAME_ANDROID)) {
				com.wearezeta.auto.android.DialogPageSteps steps = new com.wearezeta.auto.android.DialogPageSteps();
				try {
					steps.ITypeTheMessageAndSendIt(message);
				} catch (Throwable e) {
					e.printStackTrace();
				} finally {
					toSend--;
				}
			} else if (platform.equals(CommonUtils.PLATFORM_NAME_OSX)) {
				com.wearezeta.auto.osx.steps.ConversationPageSteps steps = new com.wearezeta.auto.osx.steps.ConversationPageSteps();
				steps.IWriteMessage(message);
				steps.WhenISendMessage();
				toSend--;
			} else if (platform.equals(CommonUtils.PLATFORM_NAME_IOS)) {
				try {
					com.wearezeta.auto.ios.DialogPageSteps steps = new com.wearezeta.auto.ios.DialogPageSteps();
					steps.ISendPredefinedMessage(message);
				} catch (Throwable e) {
					log.error(e.getMessage());
					e.printStackTrace();
				} finally {
					toSend--;
				}
			} else {
				log.error("Unknown platform name " + platform
						+ ". Thread stopped.");
				break;
			}

			MessageEntry entry = new MessageEntry("text", message,
					platform, new Date());
			ExecutionContext.addNewSentTextMessage(entry);
			
			if (ExecutionContext.sendingInterval > 0) {
				try {
					Thread.sleep(ExecutionContext.sendingInterval * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		log.debug("All messages sent. Waiting for listener.");
		parent.setState(InstanceState.FINAL_LISTENING);
	}

	private ClientUser backendClient() {
		if (platform.equals(CommonUtils.PLATFORM_NAME_ANDROID)) {
			return ExecutionContext.androidZeta().getUserInstance();
		} else if (platform.equals(CommonUtils.PLATFORM_NAME_IOS)) {
			return ExecutionContext.iosZeta().getUserInstance();
		} else if (platform.equals(CommonUtils.PLATFORM_NAME_OSX)) {
			return ExecutionContext.osxZeta().getUserInstance();
		} else {
			return null;
		}
	}

	private void backendSendTextMessage(String chat, String message) {
			try {
				BackEndREST.sendDialogMessageByChatName(backendClient(), chat,
						message);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
