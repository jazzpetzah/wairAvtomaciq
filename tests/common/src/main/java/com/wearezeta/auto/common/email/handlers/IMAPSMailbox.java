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

	private ISupportsMessagesPolling mailboxHandler;

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
			final MBoxHandlerType handlerType = MBoxHandlerType
					.fromString(CommonUtils.getValueFromCommonConfig(
							IMAPSMailbox.class, "mailboxHandlerType"));
			final Constructor<?> ctor = handlerType.getHandlerClass()
					.getConstructor();
			instance = new IMAPSMailbox(
					(ISupportsMessagesPolling) ctor.newInstance());
			log.debug(String.format(
					"Created %s singleton. Message handler is set to '%s'",
					IMAPSMailbox.class.getSimpleName(), handlerType.name()));
		}
		return instance;
	}

	private IMAPSMailbox(ISupportsMessagesPolling mailboxHandler)
			throws Exception {
		this.mailboxHandler = mailboxHandler;
	}

	@Override
	public void waitUntilMessagesCountReaches(String deliveredTo,
			int expectedMsgsCount, int timeoutSeconds) throws Exception {
		this.mailboxHandler.waitUntilMessagesCountReaches(deliveredTo,
				expectedMsgsCount, timeoutSeconds);
	}
}
