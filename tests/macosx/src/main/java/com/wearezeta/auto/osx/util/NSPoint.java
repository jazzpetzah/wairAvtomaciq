package com.wearezeta.auto.osx.util;

import java.util.regex.Matcher;

public class NSPoint {

    private final int x;
    private final int y;

    public NSPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    private static final String NSPOINT_PARSING_STRING = ".*\\{([\\-]?[0-9]*)\\.*.*,\\s([\\-]?[0-9]*)\\.*.*\\}";

    public static NSPoint fromString(String coords) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern
                .compile(NSPOINT_PARSING_STRING);
        Matcher matcher = pattern.matcher(coords);
        while (matcher.find()) {
            return new NSPoint(Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)));
        }
        return null;
    }

    public String toString() {
        return String.format("NSPoint: {%d, %d}", x, y);
    }
}
