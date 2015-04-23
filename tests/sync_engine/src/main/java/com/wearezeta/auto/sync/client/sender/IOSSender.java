package com.wearezeta.auto.sync.client.sender;

import com.wearezeta.auto.ios.DialogPageSteps;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.sync.client.platform.IOSWireInstance;

public class IOSSender extends WireSender {

	public IOSSender(IOSWireInstance parent,
			int numberOfMessages) {
		super(parent, numberOfMessages);
	}

	@Override
	protected void sendTextMessageClient(String message) throws Exception {
		DialogPageSteps steps = new DialogPageSteps();
		PagesCollection.dialogPage.scrollToEndOfConversation();
		steps.ISendUsingScriptPredefinedMessage(message);
	}
}
