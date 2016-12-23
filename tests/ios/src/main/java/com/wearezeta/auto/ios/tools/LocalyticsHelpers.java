package com.wearezeta.auto.ios.tools;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LocalyticsHelpers {
    private static final String LOG_RECORD_MARKER = "<ANALYTICS>:";

    private static List<JSONObject> filterLog(String fullLog) {
        final List<JSONObject> result = new ArrayList<>();
        boolean isRecordStarted = false;
        StringBuilder currentRecord = new StringBuilder();
        for (final String logLine : fullLog.split("\n")) {
            final int matchPos = logLine.indexOf(LOG_RECORD_MARKER);
            if (matchPos > 0) {
                isRecordStarted = true;
                currentRecord = new StringBuilder();
                currentRecord.append(logLine.substring(matchPos + LOG_RECORD_MARKER.length()));
                continue;
            }
            if (isRecordStarted && logLine.startsWith(" ")) {
                currentRecord.append(logLine);
                continue;
            }
            final String resultRecord = currentRecord.toString();
            if (resultRecord.length() > 0) {
                result.add(new JSONObject(resultRecord.trim()));
            }
            isRecordStarted = false;
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

    private static boolean hasAllMapItems(JSONObject src, Map<String, Object> expectedAttributes) {
        for (Map.Entry<String, Object> entry : expectedAttributes.entrySet()) {
            final String attrName = entry.getKey();
            final Object attrValue = entry.getValue();
            if (!src.has(attrName) || !src.get(attrName).equals(attrValue)) {
                return false;
            }
        }
        return true;
    }

    public static long getEventOccurrencesCount(String currentLog, String eventName,
                                                Map<String, Object> expectedAttributes) {
        return filterLog(currentLog).stream().filter(
                x -> x.has(EVENT_NAME_KEY) && x.getString(EVENT_NAME_KEY).equals(eventName) &&
                        x.has(ATTRIBUTES_NAME_KEY) && hasAllMapItems(x.getJSONObject(ATTRIBUTES_NAME_KEY),
                        expectedAttributes)
        ).count();
    }
}
