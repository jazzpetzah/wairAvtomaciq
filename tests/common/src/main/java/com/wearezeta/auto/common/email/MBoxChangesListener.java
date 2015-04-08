package com.wearezeta.auto.common.email;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;

class MBoxChangesListener implements MessageCountListener, Callable<Message> {
	private Map<String, String> expectedHeaders = new HashMap<String, String>();
	private CountDownLatch waitObj = new CountDownLatch(1);
	private Message matchedMessage = null;
	private IMAPSMailbox parentMBox;
	private int timeoutSeconds;
	private long filterMessagesAfter;

	private static final Logger log = ZetaLogger
			.getLog(MBoxChangesListener.class.getSimpleName());

	public MBoxChangesListener(IMAPSMailbox parentMBox,
			Map<String, String> expectedHeaders, int timeoutSeconds,
			long filterMessagesAfter) {
		// clone map
		for (Entry<String, String> entry : expectedHeaders.entrySet()) {
			this.expectedHeaders.put(entry.getKey(), entry.getValue());
		}
		this.parentMBox = parentMBox;
		this.timeoutSeconds = timeoutSeconds;
		this.filterMessagesAfter = filterMessagesAfter;
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
					headerValues = msg.getHeader(expectedHeaderName);
				} catch (NullPointerException e) {
					// Ignore NPE bug in java mail lib
				}
				log.debug(String.format(
						"Checking if the email message contains %s: %s header",
						expectedHeaderName, expectedHeaderValue));
				if (headerValues != null) {
					for (String headerValue : headerValues) {
						log.debug(String.format("%s: %s -> %s",
								expectedHeaderName, headerValue,
								expectedHeaderValue));
						if (headerValue.equals(expectedHeaderValue)) {
							log.debug(String
									.format("The expected header value '%s' is found in the email",
											expectedHeaderValue));
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

	private List<Message> preprocessReceivedMessages(Message[] deliveredMessages) {
		List<Message> result = new ArrayList<Message>();
		for (Message msg : deliveredMessages) {
			try {
				log.debug("\tMessage timestamp:" + msg.getSentDate().getTime());
				log.debug("\tListener timestamp:" + filterMessagesAfter);
				if (msg.getSentDate().getTime() >= filterMessagesAfter) {
					result.add(msg);
					log.debug("\tMessage accepted by timestamp");
				} else {
					log.debug("\t!!! Message rejected because it is outdated. Please check your local time (should be in sync with world time)!");
				}
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		Collections.sort(result,
				Collections.reverseOrder(new MessagesByDateComparator()));
		return result;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private void logMessages(Message[] deliveredMessages) {
		for (Message msg : deliveredMessages) {
			log.debug("Received new message with headers:");
			Enumeration<Header> hdrs;
			try {
				hdrs = msg.getAllHeaders();
			} catch (MessagingException e) {
				e.printStackTrace();
				continue;
			}
			while (hdrs.hasMoreElements()) {
				final Header hdr = hdrs.nextElement();
				log.debug(String.format("\t\t%s: %s", hdr.getName(),
						hdr.getValue()));
			}
		}
	}

	@Override
	public void messagesAdded(MessageCountEvent e) {
		for (Message msg : preprocessReceivedMessages(e.getMessages())) {
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
			this.parentMBox.closeFolder(true);
		}
		throw new RuntimeException(
				String.format(
						"Email message containing headers %s has not been found within %d seconds",
						this.expectedHeaders.toString(), this.timeoutSeconds));
	}
}
