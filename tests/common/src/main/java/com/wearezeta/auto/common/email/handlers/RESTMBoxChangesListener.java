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
		final long millisecondsStarted = System.currentTimeMillis();
		do {
			final List<String> deliveredRawMessages = this.getParentMbox()
					.getRecentMessages(deliveredTo, 1, this.retryNumber + 1,
							this.timeoutSeconds * 1000);
			log.debug(String.format("Got %s incoming message(s) for %s",
					deliveredRawMessages.size(), deliveredTo));
			for (String deliveredRawMessage : deliveredRawMessages) {
				final Message foundMsg = MessagingUtils
						.stringToMsg(deliveredRawMessage);
				if (this.areAllHeadersInMessage(foundMsg)) {
					if (foundMsg.getSentDate().getTime() >= filterMessagesAfter) {
						log.debug("\tMessage accepted by timestamp");
						return deliveredRawMessage;
					} else {
						log.error("\t!!! Message rejected because it is outdated. Please check your local time (should be in sync with world time)!");
					}
				}
			}
			Thread.sleep(2000);
		} while (System.currentTimeMillis() - millisecondsStarted <= this.timeoutSeconds * 1000);
		throw new RuntimeException(
				String.format(
						"Email message containing headers %s has not been found within %d seconds after %s retry",
						this.expectedHeaders.toString(), this.timeoutSeconds,
						this.retryNumber));
	}
}
