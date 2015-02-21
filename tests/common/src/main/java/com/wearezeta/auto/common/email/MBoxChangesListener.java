package com.wearezeta.auto.common.email;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;

class MBoxChangesListener implements MessageCountListener,
		Callable<Message> {
	private static final int NEW_MSG_MIN_CHECK_INTERVAL = 1000; // milliseconds
	
	private Map<String, String> expectedHeaders = new HashMap<String, String>();
	private CountDownLatch waitObj = new CountDownLatch(1);
	private Message matchedMessage;
	private IMAPSMailbox parentMBox;
	private final Random random = new Random();
	private Thread messagesCountNotifier;

	public MBoxChangesListener(IMAPSMailbox parentMBox,
			Map<String, String> expectedHeaders) {
		this.expectedHeaders = expectedHeaders;
		this.parentMBox = parentMBox;

		// This is to force mbox update notifications
		messagesCountNotifier = new Thread() {
			@Override
			public void run() {
				Folder dstFolder = parentMBox.getFolder();
				while (dstFolder != null && dstFolder.isOpen()) {
					try {
						Thread.sleep(NEW_MSG_MIN_CHECK_INTERVAL
								+ random.nextInt(NEW_MSG_MIN_CHECK_INTERVAL));
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		};
		messagesCountNotifier.start();
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

	@Override
	public Message call() throws Exception {
		try {
			this.waitObj.await();
			return this.matchedMessage;
		} finally {
			this.parentMBox.closeFolder();
			messagesCountNotifier.interrupt();
		}
	}
}
