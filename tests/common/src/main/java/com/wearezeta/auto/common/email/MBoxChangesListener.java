package com.wearezeta.auto.common.email;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;

class MBoxChangesListener implements MessageCountListener, Callable<Message> {
	private static final int NEW_MSG_CHECK_INTERVAL = 500; // milliseconds

	private Map<String, String> expectedHeaders = new HashMap<String, String>();
	private CountDownLatch waitObj = new CountDownLatch(1);
	private Message matchedMessage = null;
	private IMAPSMailbox parentMBox;
	private Thread messagesCountNotifier;
	private int timeoutSeconds;

	public MBoxChangesListener(IMAPSMailbox parentMBox,
			Map<String, String> expectedHeaders, int timeoutSeconds) {
		this.expectedHeaders = expectedHeaders;
		this.parentMBox = parentMBox;
		this.timeoutSeconds = timeoutSeconds;

		// This is to force mbox update notifications
		messagesCountNotifier = new Thread() {
			@Override
			public void run() {
				final Folder dstFolder = parentMBox.getFolder();
				while (dstFolder != null) {
					try {
						if (!dstFolder.isOpen()) {
							dstFolder.open(Folder.READ_ONLY);
						}
						dstFolder.getMessageCount();
						Thread.sleep(NEW_MSG_CHECK_INTERVAL);
					} catch (InterruptedException e) {
						return;
					} catch (MessagingException e) {
						// Ignore exception
					}
				}
			}
		};
		messagesCountNotifier.start();
	}

	private boolean areAllHeadersInMessage(Message msg) {
		for (Entry<String, String> expectedHeader : this.expectedHeaders
				.entrySet()) {
			boolean isHeaderFound = false;
			final String expectedHeaderName = expectedHeader.getKey();
			final String expectedHeaderValue = expectedHeader.getValue();
			try {
				String[] headerValues = null;
				try {
					if (!msg.getFolder().isOpen()) {
						msg.getFolder().open(Folder.READ_ONLY);
					}
					headerValues = msg.getHeader(expectedHeaderName);
				} catch (NullPointerException e) {
					// Ignore NPE bug in java mail lib
				}
				if (headerValues != null) {
					for (String headerValue : headerValues) {
						if (headerValue.equals(expectedHeaderValue)) {
							isHeaderFound = true;
							break;
						}
					}
				}
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			if (!isHeaderFound) {
				return false;
			}
		}
		return true;
	}

	private static class MessagesByDateComparator implements
			Comparator<Message> {
		@Override
		public int compare(Message m1, Message m2) {
			try {
				return m1.getSentDate().compareTo(m2.getSentDate());
			} catch (MessagingException e) {
				e.printStackTrace();
				return 0;
			}
		}
	}

	@Override
	public void messagesAdded(MessageCountEvent e) {
		List<Message> addedMessages = Arrays.asList(e.getMessages());
		Collections.sort(addedMessages,
				Collections.reverseOrder(new MessagesByDateComparator()));
		for (Message msg : addedMessages) {
			if (areAllHeadersInMessage(msg)) {
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
			if (this.waitObj.await(timeoutSeconds, TimeUnit.SECONDS)) {
				return this.matchedMessage;
			}
		} finally {
			this.parentMBox.getFolder().removeMessageCountListener(this);
			this.parentMBox.closeFolder();
			messagesCountNotifier.interrupt();
		}
		throw new RuntimeException(
				String.format(
						"Email message containing headers %s has not been found within %d seconds",
						this.expectedHeaders.toString(), this.timeoutSeconds));
	}
}
