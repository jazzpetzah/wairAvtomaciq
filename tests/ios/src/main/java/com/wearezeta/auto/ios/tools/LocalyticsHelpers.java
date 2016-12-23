package com.wearezeta.auto.ios.tools;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LocalyticsHelpers {
    private static final String LOG_RECORD_MARKER = "<ANALYTICS>";

    private static List<String> filterLog(String fullLog) {
        return Arrays.stream(fullLog.split("\n")).filter(x ->
                x.contains(LOG_RECORD_MARKER)
        ).collect(Collectors.toList());
    }

    private static final Pattern EVENT_LOG_PATTERN = Pattern.compile(
            "Tagging Event:\\s+((?:\\S|\\s(?!\\s))*)"
    );

    public static int getEventOccurrencesCount(String currentLog, String eventName) {
        final List<String> allRecords = filterLog(currentLog);
        int occurrences = 0;
        for (String logRecord : allRecords) {
            final Matcher matcher = EVENT_LOG_PATTERN.matcher(logRecord);
            if (matcher.find() && matcher.group(1).equals(eventName)) {
                ++occurrences;
            }
        }
        return occurrences;
    }
}
