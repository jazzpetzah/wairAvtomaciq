package com.wearezeta.auto.common.email.handlers;

import java.util.List;
import java.util.Map;

import javax.mail.Message;

import com.wearezeta.auto.common.email.MessagingUtils;

class RESTChangesListener extends AbstractMboxChangesListener {
	private static final int MAX_MSGS_TO_POLL = 5;

	@Override
	protected MboxListenerServiceClient getParentMbox() {
		return (MboxListenerServiceClient) this.parentMBox;
	}

	public RESTChangesListener(MboxListenerServiceClient parentMBox,
			Map<String, String> expectedHeaders, int timeoutSeconds,
			long filterMessagesAfter) {
		super(parentMBox, expectedHeaders, timeoutSeconds, filterMessagesAfter);
	}

	private String getDeliveredToValue() throws Exception {
		// Get emails for all recipients by default
		String deliveredTo = MessagingUtils.getAccountName();
		for (Map.Entry<String, String> pair : this.expectedHeaders.entrySet()) {
			if (pair.getKey().equals(MessagingUtils.DELIVERED_TO_HEADER)) {
				deliveredTo = pair.getValue();
				break;
			}
		}
		return deliveredTo;
	}

	@Override
	public String call() throws Exception {
		final String deliveredTo = getDeliveredToValue();
		final long millisecondsStarted = System.currentTimeMillis();
		do {
			final List<String> deliveredMessages = this.getParentMbox()
					.getRecentMessages(deliveredTo, MAX_MSGS_TO_POLL, 1,
							this.timeoutSeconds);
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
			Thread.sleep(MboxListenerServiceClient.POLLING_FREQUENCY_MILLISECONDS);
		} while (System.currentTimeMillis() - millisecondsStarted <= this.timeoutSeconds * 1000);
		throw new RuntimeException(
				String.format(
						"Email message containing headers %s has not been found within %d seconds",
						this.expectedHeaders.toString(), this.timeoutSeconds));
	}
}
