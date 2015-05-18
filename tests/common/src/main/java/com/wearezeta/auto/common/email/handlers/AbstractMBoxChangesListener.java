package com.wearezeta.auto.common.email.handlers;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;

public abstract class AbstractMBoxChangesListener implements Callable<String> {
	protected Map<String, String> expectedHeaders = new HashMap<String, String>();
	protected Object parentMBox;
	protected int timeoutSeconds;
	protected long filterMessagesAfter;

	protected Object getParentMbox() {
		return this.parentMBox;
	}

	protected final Logger log = ZetaLogger.getLog(this.getClass()
			.getSimpleName());

	public AbstractMBoxChangesListener(Object parentMBox,
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

	protected boolean areAllHeadersInMessage(Message msg) {
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
}
