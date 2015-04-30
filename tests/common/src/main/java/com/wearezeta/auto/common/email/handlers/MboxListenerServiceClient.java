package com.wearezeta.auto.common.email.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.email.MessagingUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

class MboxListenerServiceClient implements ISupportsMessagesPolling {
	private static final long POLLING_FREQUENCY_MILLISECONDS = 2000;

	private static final Logger log = ZetaLogger
			.getLog(MboxListenerServiceClient.class.getSimpleName());

	public MboxListenerServiceClient() {
	}

	private static JSONArray waitForMessagesCount(String email, int minCount,
			int timeoutSeconds) throws Exception {
		final long millisecondsStarted = System.currentTimeMillis();
		do {
			final JSONArray recentsEmails = EmailListenerServiceREST
					.getRecentEmailsForUser(email, minCount);
			if (recentsEmails.length() >= minCount) {
				return recentsEmails;
			}
			Thread.sleep(POLLING_FREQUENCY_MILLISECONDS);
		} while (System.currentTimeMillis() - millisecondsStarted <= timeoutSeconds * 1000);
		throw new TimeoutException(
				String.format(
						"Timeout occured while waiting until the count of recent messages for user '%s' is greater than %s after %s second(s)",
						email, minCount, timeoutSeconds));
	}

	protected List<String> getRecentMessages(String email, int maxCount,
			int minCount, int timeoutSeconds) throws Exception {
		List<String> result = new ArrayList<String>();
		JSONArray recentsEmails;
		if (minCount > 0) {
			recentsEmails = waitForMessagesCount(email, minCount,
					timeoutSeconds);
		} else {
			recentsEmails = EmailListenerServiceREST.getRecentEmailsForUser(
					email, maxCount);
		}
		for (int i = 0; i < recentsEmails.length(); i++) {
			final JSONObject recentEmailInfo = recentsEmails.getJSONObject(i);
			result.add(recentEmailInfo.getString("raw_text"));
		}
		return result;
	}

	@Override
	public List<String> getRecentMessages(int msgsCount) throws Exception {
		return getRecentMessages(MessagingUtils.getAccountName(), msgsCount, 0,
				0);
	}

	private final ExecutorService pool = Executors
			.newFixedThreadPool(CommonUtils.MAX_PARALLEL_USER_CREATION_TASKS);

	@Override
	public Future<String> getMessage(Map<String, String> expectedHeaders,
			int timeoutSeconds, long rejectMessagesBeforeTimestamp)
			throws Exception {
		RESTChangesListener listener = new RESTChangesListener(this,
				expectedHeaders, timeoutSeconds, rejectMessagesBeforeTimestamp);
		log.debug(String.format(
				"Started email listener for message containing headers %s...",
				expectedHeaders.toString()));
		return pool.submit(listener);
	}
}
