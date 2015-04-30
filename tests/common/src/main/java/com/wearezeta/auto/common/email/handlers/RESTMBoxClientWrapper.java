package com.wearezeta.auto.common.email.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.email.MessagingUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

class RESTMBoxClientWrapper implements ISupportsMessagesPolling {
	private static final Logger log = ZetaLogger
			.getLog(RESTMBoxClientWrapper.class.getSimpleName());

	public RESTMBoxClientWrapper() {
	}

	protected List<String> getRecentMessages(String email, int minCount,
			int maxCount, int timeoutMilliseconds) throws Exception {
		List<String> result = new ArrayList<String>();
		JSONArray recentsEmails;
		recentsEmails = RESTMBoxAPI.getRecentEmailsForUser(email,
				minCount, maxCount, timeoutMilliseconds);
		for (int i = 0; i < recentsEmails.length(); i++) {
			final JSONObject recentEmailInfo = recentsEmails.getJSONObject(i);
			result.add(recentEmailInfo.getString("raw_text"));
		}
		return result;
	}

	@Override
	public List<String> getRecentMessages(int msgsCount) throws Exception {
		return getRecentMessages(MessagingUtils.getAccountName(), 0, msgsCount,
				0);
	}

	private final ExecutorService pool = Executors
			.newFixedThreadPool(CommonUtils.MAX_PARALLEL_USER_CREATION_TASKS);

	private static Map<String, Integer> retryCounts = new ConcurrentHashMap<String, Integer>();

	@Override
	public Future<String> getMessage(Map<String, String> expectedHeaders,
			int timeoutSeconds, long rejectMessagesBeforeTimestamp)
			throws Exception {
		final String deliveredTo = MessagingUtils
				.extractDeliveredToValue(expectedHeaders);
		if (retryCounts.containsKey(deliveredTo)) {
			retryCounts.put(deliveredTo, retryCounts.get(deliveredTo) + 1);
		} else {
			retryCounts.put(deliveredTo, 0);
		}
		RESTMBoxChangesListener listener = new RESTMBoxChangesListener(this,
				expectedHeaders, timeoutSeconds, rejectMessagesBeforeTimestamp,
				retryCounts.get(deliveredTo));
		log.debug(String.format(
				"Started email listener for message containing headers %s...",
				expectedHeaders.toString()));
		return pool.submit(listener);
	}
}
