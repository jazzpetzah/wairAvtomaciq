package com.wearezeta.auto.common.email.handlers;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

public class IMAPSMailbox implements ISupportsMessagesPolling {
	private static final Logger log = ZetaLogger.getLog(IMAPSMailbox.class
			.getSimpleName());

	private ISupportsMessagesPolling mailboxHandler = null;

	private ISupportsMessagesPolling instantiateHandler(
			MBoxHandlerType handlerType) throws Exception {
		final Constructor<?> ctor = handlerType.getHandlerClass()
				.getConstructor();
		return (ISupportsMessagesPolling) ctor.newInstance();
	}

	private void setMainMailboxHandler(MBoxHandlerType firstPriorityHandlerType)
			throws Exception {
		final ISupportsMessagesPolling firstPriorityHandler = instantiateHandler(firstPriorityHandlerType);
		if (firstPriorityHandler.isAlive()) {
			this.mailboxHandler = firstPriorityHandler;
		} else {
			for (MBoxHandlerType handlerType : MBoxHandlerType.values()) {
				if (handlerType == firstPriorityHandlerType) {
					continue;
				}
				final ISupportsMessagesPolling handlerInstance = instantiateHandler(handlerType);
				if (handlerInstance.isAlive()) {
					this.mailboxHandler = handlerInstance;
					break;
				}
			}
		}
		if (this.mailboxHandler == null) {
			throw new RuntimeException(
					"All available email message handlers are currently dead. Cannot proceed further :-(");
		}
		log.info(String
				.format("First priority email messages handler is set to '%s' (suggested type was '%s')",
						this.mailboxHandler.getClass().getSimpleName(),
						firstPriorityHandlerType.name()));
	}

	public Future<String> getMessage(Map<String, String> expectedHeaders,
			int timeoutSeconds) throws Exception {
		return getMessage(expectedHeaders, timeoutSeconds, 0);
	}

	public Future<String> getMessage(Map<String, String> expectedHeaders,
			int timeoutSeconds, long rejectMessagesBeforeTimestamp)
			throws Exception {
		return mailboxHandler.getMessage(expectedHeaders, timeoutSeconds,
				rejectMessagesBeforeTimestamp);
	}

	private static IMAPSMailbox instance = null;

	public static synchronized IMAPSMailbox getInstance() throws Exception {
		if (instance == null) {
			final MBoxHandlerType firstPriorityHandlerType = MBoxHandlerType
					.fromString(CommonUtils.getValueFromCommonConfig(
							IMAPSMailbox.class, "mailboxHandlerType"));
			instance = new IMAPSMailbox(firstPriorityHandlerType);
			log.debug(String.format("Successfully created %s singleton",
					IMAPSMailbox.class.getSimpleName()));
		}
		return instance;
	}

	private IMAPSMailbox(MBoxHandlerType firstPriorityHandlerType)
			throws Exception {
		this.setMainMailboxHandler(firstPriorityHandlerType);
	}

	@Override
	public void waitUntilMessagesCountReaches(String deliveredTo,
			int expectedMsgsCount, int timeoutSeconds) throws Exception {
		this.mailboxHandler.waitUntilMessagesCountReaches(deliveredTo,
				expectedMsgsCount, timeoutSeconds);
	}

	@Override
	public boolean isAlive() {
		return true;
	}
}
