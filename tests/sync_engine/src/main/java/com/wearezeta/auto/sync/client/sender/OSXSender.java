package com.wearezeta.auto.sync.client.sender;

import com.wearezeta.auto.osx.steps.ConversationPageSteps;
import com.wearezeta.auto.sync.client.platform.OSXWireInstance;

public class OSXSender extends WireSender {

	public OSXSender(OSXWireInstance parent,
			int numberOfMessages) {
		super(parent, numberOfMessages);
	}

	@Override
	protected void sendTextMessageClient(String message) throws Exception {
		ConversationPageSteps steps = new ConversationPageSteps();
		steps.IWriteMessage(message);
		steps.WhenISendMessage();
	}
}
