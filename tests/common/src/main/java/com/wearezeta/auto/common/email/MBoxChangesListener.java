package com.wearezeta.auto.common.email;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;

public class MBoxChangesListener implements MessageCountListener {
	private Map<String, String> expectedHeaders = new HashMap<String, String>();
	private CountDownLatch waitObj;
	private Message matchedMessage;
	private IMAPSMailbox parentMBox;

	public IMAPSMailbox getParentMBox() {
		return this.parentMBox;
	}

	public MBoxChangesListener(IMAPSMailbox parentMBox,
			Map<String, String> expectedHeaders, CountDownLatch wait) {
		this.expectedHeaders = expectedHeaders;
		this.waitObj = wait;
		this.parentMBox = parentMBox;
	}

	public Message getMatchedMessage(int timeout) throws InterruptedException {
		this.waitObj.await(timeout, TimeUnit.SECONDS);
		return this.matchedMessage;
	}

	@Override
	public void messagesAdded(MessageCountEvent e) {
		Message[] addedMessages = e.getMessages();
		for (Message msg : addedMessages) {
			boolean allHeadersFound = true;
			for (Entry<String, String> expectedHeader : this.expectedHeaders
					.entrySet()) {
				boolean isHeaderFound = false;
				final String expectedHeaderName = expectedHeader.getKey();
				final String expectedHeaderValue = expectedHeader.getValue();
				try {
					final String[] headerValue = msg
							.getHeader(expectedHeaderName);
					if (headerValue != null) {
						if (headerValue[0].equals(expectedHeaderValue)) {
							isHeaderFound = true;
						}
					}
				} catch (MessagingException e1) {
					isHeaderFound = false;
				}
				allHeadersFound = allHeadersFound & isHeaderFound;
			}
			if (allHeadersFound) {
				this.matchedMessage = msg;
				this.waitObj.countDown();
				return;
			}
		}
	}

	@Override
	public void messagesRemoved(MessageCountEvent e) {
		// TODO Auto-generated method stub
	}

}
