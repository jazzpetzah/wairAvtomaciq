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

import com.wearezeta.auto.common.email.handlers.RESTMBoxAPI;

class RESTMBoxClientWrapper implements ISupportsMessagesPolling {
    private static final Logger log = ZetaLogger.getLog(RESTMBoxClientWrapper.class.getSimpleName());

    public RESTMBoxClientWrapper() {
    }

    protected List<String> getRecentMessages(String email, int minCount, int maxCount, int timeoutMilliseconds)
            throws Exception {
        List<String> result = new ArrayList<>();
        final JSONArray recentEmails =
                RESTMBoxAPI.getRecentEmailsForUser(email, minCount, maxCount, timeoutMilliseconds);
        for (int i = 0; i < recentEmails.length(); i++) {
            final JSONObject recentEmailInfo = recentEmails.getJSONObject(i);
            result.add(recentEmailInfo.getString("raw_text"));
        }
        return result;
    }

    private final ExecutorService pool = Executors.newFixedThreadPool(CommonUtils.MAX_PARALLEL_USER_CREATION_TASKS);

    @Override
    public Future<String> getMessage(Map<String, String> expectedHeaders, int timeoutSeconds,
                                     long rejectMessagesBeforeTimestamp) throws Exception {
        RESTMBoxChangesListener listener = new RESTMBoxChangesListener(this, expectedHeaders, timeoutSeconds,
                rejectMessagesBeforeTimestamp);
        log.debug(String.format("Started email listener for message containing headers %s...",
                expectedHeaders.toString()));
        return pool.submit(listener);
    }

    @Override
    public void waitUntilMessagesCountReaches(String deliveredTo, int expectedMsgsCount, int timeoutSeconds)
            throws Exception {
        getRecentMessages(deliveredTo, expectedMsgsCount, expectedMsgsCount, timeoutSeconds * 1000);
    }

    @Override
    public boolean isAlive() {
        try {
            RESTMBoxAPI.getRecentEmailsForUser(MessagingUtils.getAccountName(), 0, 0, 1000);
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }
}
