package com.wearezeta.auto.common.email.handlers;

import java.util.Map;
import java.util.concurrent.Future;

public interface ISupportsMessagesPolling {
	void waitUntilMessagesCountReaches(String deliveredTo,
			int expectedMsgsCount, int timeoutSeconds) throws Exception;

	Future<String> getMessage(Map<String, String> expectedHeaders,
			int timeoutSeconds, long rejectMessagesBeforeTimestamp)
			throws Exception;
	
	boolean isAlive();
}
