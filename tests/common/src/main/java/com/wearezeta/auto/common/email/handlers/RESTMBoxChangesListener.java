package com.wearezeta.auto.common.email.handlers;

import java.util.List;
import java.util.Map;

import javax.mail.Message;

import com.wearezeta.auto.common.email.MessagingUtils;

class RESTMBoxChangesListener extends AbstractMBoxChangesListener {
	private int retryNumber = 0;

	@Override
	protected RESTMBoxClientWrapper getParentMbox() {
		return (RESTMBoxClientWrapper) this.parentMBox;
	}

	public RESTMBoxChangesListener(RESTMBoxClientWrapper parentMBox,
			Map<String, String> expectedHeaders, int timeoutSeconds,
			long filterMessagesAfter, int retryNumber) {
		super(parentMBox, expectedHeaders, timeoutSeconds, filterMessagesAfter);
		this.retryNumber = retryNumber;
	}

	@Override
	public String call() throws Exception {
		final String deliveredTo = MessagingUtils
				.extractDeliveredToValue(this.expectedHeaders);
		final List<String> deliveredMessages = this.getParentMbox()
				.getRecentMessages(deliveredTo, this.retryNumber + 1,
						this.retryNumber + 1, this.timeoutSeconds * 1000);
		log.debug(String.format("Got %s new incoming message(s) for %s",
				deliveredMessages.size(), deliveredTo));
		for (String deliveredMessage : deliveredMessages) {
			final Message foundMsg = MessagingUtils
					.stringToMsg(deliveredMessage);
			if (this.areAllHeadersInMessage(foundMsg)) {
				if (foundMsg.getSentDate().getTime() >= filterMessagesAfter) {
					log.debug("\tMessage accepted by timestamp");
					return deliveredMessage;
				} else {
					log.error("\t!!! Message rejected because it is outdated. Please check your local time (should be in sync with world time)!");
				}
			}
		}
		throw new RuntimeException(
				String.format(
						"Email message containing headers %s has not been found within %d seconds",
						this.expectedHeaders.toString(), this.timeoutSeconds));
	}
}
