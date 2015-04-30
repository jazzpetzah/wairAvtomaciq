package com.wearezeta.auto.common.email.handlers;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

public class IMAPSMailbox implements ISupportsMessagesPolling {
	private static final Logger log = ZetaLogger.getLog(IMAPSMailbox.class
			.getSimpleName());

	private ISupportsMessagesPolling mailboxHandler;

	public List<String> getRecentMessages(int msgsCount) throws Exception {
		return mailboxHandler.getRecentMessages(msgsCount);
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
}
