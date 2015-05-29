package com.wearezeta.auto.sync.client.sender;

import com.wearezeta.auto.android.steps.DialogPageSteps;
import com.wearezeta.auto.sync.client.platform.AndroidWireInstance;

public class AndroidSender extends WireSender {

	public AndroidSender(AndroidWireInstance parent, int numberOfMessages) {
		super(parent, numberOfMessages);
	}

	@Override
	protected void sendTextMessageClient(String message) throws Exception {
		DialogPageSteps steps = new DialogPageSteps();
		steps.ITypeMessageAndSendIt(message);
	}
}
