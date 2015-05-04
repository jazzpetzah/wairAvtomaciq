package com.wearezeta.auto.common.email.handlers;

import java.util.NoSuchElementException;

enum MBoxHandlerType {
	JavaX(JavaXMBox.class), REST(RESTMBoxClientWrapper.class);

	private final Class<? extends ISupportsMessagesPolling> handlerClass;

	public Class<? extends ISupportsMessagesPolling> getHandlerClass() {
		return this.handlerClass;
	}

	private MBoxHandlerType(Class<? extends ISupportsMessagesPolling> cls) {
		this.handlerClass = cls;
	}

	public static MBoxHandlerType fromString(String name) {
		for (MBoxHandlerType value : MBoxHandlerType.values()) {
			if (name.equals(value.name())) {
				return value;
			}
		}

		throw new NoSuchElementException(String.format(
				"Mailbox handler '%s' is unknown", name));
	}
}
