package com.wearezeta.auto.common.email.handlers;

import java.util.NoSuchElementException;

enum MailboxHandlerType {
	JavaX(JavaXMailbox.class), REST(MboxListenerServiceClient.class);

	private final Class<? extends ISupportsMessagesPolling> handlerClass;

	public Class<? extends ISupportsMessagesPolling> getHandlerClass() {
		return this.handlerClass;
	}

	private MailboxHandlerType(Class<? extends ISupportsMessagesPolling> cls) {
		this.handlerClass = cls;
	}

	public static MailboxHandlerType fromString(String name) {
		for (MailboxHandlerType value : MailboxHandlerType.values()) {
			if (name.equals(value.name())) {
				return value;
			}
		}

		throw new NoSuchElementException(String.format(
				"Mailbox handler '%s' is unknown", name));
	}
}
