package com.wearezeta.auto.common.email;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface ISupportsMessagesPolling {
	public List<String> getRecentMessages(int msgsCount) throws Exception;

	public Future<String> getMessage(Map<String, String> expectedHeaders,
			int timeoutSeconds) throws Exception;

	public Future<String> getMessage(Map<String, String> expectedHeaders,
			int timeoutSeconds, long rejectMessagesBeforeTimestamp)
			throws Exception;
}
