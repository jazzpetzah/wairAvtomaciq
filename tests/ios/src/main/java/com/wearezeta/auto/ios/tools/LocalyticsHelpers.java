package com.wearezeta.auto.ios.tools;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LocalyticsHelpers {
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
                    // in case this is one-line JSON
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
        return result;
    }

    private static final String EVENT_NAME_KEY = "event";
    private static final String ATTRIBUTES_NAME_KEY = "attributes";

    public static long getEventOccurrencesCount(String currentLog, String eventName) {
        return filterLog(currentLog).stream().filter(
                x -> x.has(EVENT_NAME_KEY) && x.getString(EVENT_NAME_KEY).equals(eventName)
        ).count();
    }

    private static boolean hasAllItems(JSONObject src, JSONObject expectedAttributes) {
        for (String expectedKey : expectedAttributes.keySet()) {
            if (!src.has(expectedKey) || !src.get(expectedKey).equals(expectedAttributes.get(expectedKey))) {
                return false;
            }
        }
        return true;
    }

    public static long getEventOccurrencesCount(String currentLog, String eventName, JSONObject expectedAttributes) {
        return filterLog(currentLog).stream().filter(
                x -> x.has(EVENT_NAME_KEY) && x.getString(EVENT_NAME_KEY).equals(eventName) &&
                        x.has(ATTRIBUTES_NAME_KEY) && hasAllItems(x.getJSONObject(ATTRIBUTES_NAME_KEY),
                        expectedAttributes)
        ).count();
    }
}
