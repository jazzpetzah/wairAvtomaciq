package com.wearezeta.auto.ios.tools;

import com.wearezeta.auto.common.log.ZetaLogger;
import org.json.JSONObject;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LocalyticsHelpers {
    private static final Logger log = ZetaLogger.getLog(LocalyticsHelpers.class.getSimpleName());

    private static final String LOG_RECORD_MARKER = "<ANALYTICS>:";
    private static final Pattern NEXT_LOG_LINE_PATTERN = Pattern.compile("^\\s+");

    private static List<JSONObject> filterLog(String fullLog) {
        final List<JSONObject> result = new ArrayList<>();
        boolean isRecordStarted = false;
        StringBuilder currentRecord = new StringBuilder();
        for (final String logLine : fullLog.split("\n")) {
            final int matchPos = logLine.indexOf(LOG_RECORD_MARKER);
            if (matchPos > 0) {
                final String resultRecord = currentRecord.toString();
                if (!resultRecord.isEmpty()) {
                    // in case this was one-liner JSON
                    result.add(new JSONObject(resultRecord.trim()));
                }
                isRecordStarted = true;
                currentRecord = new StringBuilder();
                currentRecord.append(logLine.substring(matchPos + LOG_RECORD_MARKER.length()));
                continue;
            }
            if (isRecordStarted && NEXT_LOG_LINE_PATTERN.matcher(logLine).find()) {
                currentRecord.append(logLine);
                continue;
            }
            final String resultRecord = currentRecord.toString();
            if (!resultRecord.isEmpty()) {
                result.add(new JSONObject(resultRecord.trim()));
                currentRecord = new StringBuilder();
            }
            isRecordStarted = false;
        }
        final String resultRecord = currentRecord.toString();
        if (!resultRecord.isEmpty()) {
            result.add(new JSONObject(resultRecord.trim()));
        }
        log.debug(String.format("Parsed %d Localytics action occurrences from the iOS log:\n%s",
                result.size(), result));
        return result;
    }

    private static final String EVENT_NAME_KEY = "event";
    private static final String ATTRIBUTES_NAME_KEY = "attributes";

    public static long getEventOccurrencesCount(String currentLog, String eventName) {
        log.debug(String.format("Getting occurrences count for Localytics event '%s'...", eventName));
        return filterLog(currentLog).stream().filter(
                x -> x.has(EVENT_NAME_KEY) && x.getString(EVENT_NAME_KEY).equals(eventName)
        ).count();
    }

    private static boolean hasAllItems(JSONObject actualAttributes, JSONObject expectedAttributes) {
        log.debug(String.format("Comparing actual event attributes %s " +
                "with expected attributes %s...", actualAttributes, expectedAttributes));
        for (String expectedKey : expectedAttributes.keySet()) {
            if (!actualAttributes.has(expectedKey)
                    || !actualAttributes.get(expectedKey).equals(expectedAttributes.get(expectedKey))) {
                log.debug(String.format("Comparison failed for the key '%s'", expectedKey));
                return false;
            }
        }
        log.debug("Comparison succeeded");
        return true;
    }

    public static long getEventOccurrencesCount(String currentLog, String eventName, JSONObject expectedAttributes) {
        log.debug(String.format("Getting occurrences count for Localytics event '%s' with attributes '%s'...",
                eventName, expectedAttributes.toString()));
        return filterLog(currentLog).stream().filter(
                x -> x.has(EVENT_NAME_KEY) && x.getString(EVENT_NAME_KEY).equals(eventName) &&
                        x.has(ATTRIBUTES_NAME_KEY) && hasAllItems(x.getJSONObject(ATTRIBUTES_NAME_KEY),
                        expectedAttributes)
        ).count();
    }
}
