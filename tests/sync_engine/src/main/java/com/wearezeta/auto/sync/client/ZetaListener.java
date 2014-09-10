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
	private String platform;
	
	public LinkedHashMap<String, MessageEntry> registeredMessages = new LinkedHashMap<String, MessageEntry>();

	public ZetaListener(ZetaInstance parent, String platform) {
		this.parent = parent;
		this.platform = platform;
	}
	
	@Override
	public void run() {
		while (parent.getState() != InstanceState.FINISHED) {
			boolean isLastListen = false;
			if (ExecutionContext.allInstancesFinishSending()) {
				isLastListen = true;
			}
			ArrayList<MessageEntry> currentMessages = null;
			if (platform.equals(CommonUtils.PLATFORM_NAME_ANDROID)) {
				com.wearezeta.auto.android.pages.DialogPage dialogPage = 
						com.wearezeta.auto.android.pages.PagesCollection.dialogPage;
				currentMessages = dialogPage.listAllMessages();
			} else if (platform.equals(CommonUtils.PLATFORM_NAME_IOS)) {
				com.wearezeta.auto.ios.pages.DialogPage dialogPage =
						com.wearezeta.auto.ios.pages.PagesCollection.dialogPage;
				currentMessages = dialogPage.listAllMessages();
			} else if (platform.equals(CommonUtils.PLATFORM_NAME_OSX)) {
				try {
					com.wearezeta.auto.osx.steps.CommonSteps.senderPages.setConversationPage(
							new com.wearezeta.auto.osx.pages.ConversationPage(
									CommonUtils.getOsxAppiumUrlFromConfig(ContactListPageSteps.class),
									CommonUtils.getOsxApplicationPathFromConfig(ContactListPageSteps.class)));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				com.wearezeta.auto.osx.pages.ConversationPage conversationPage =
						com.wearezeta.auto.osx.steps.CommonSteps.senderPages.getConversationPage();
				
				currentMessages = conversationPage.listAllMessages();
			}
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

}
