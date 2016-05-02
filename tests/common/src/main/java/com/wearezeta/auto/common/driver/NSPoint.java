package com.wearezeta.auto.common.driver;

import com.wearezeta.auto.common.log.ZetaLogger;
import java.util.regex.Matcher;
import org.apache.log4j.Logger;

public class NSPoint {
    private static final Logger log = ZetaLogger.getLog(NSPoint.class.getSimpleName());
	private int x;
	private int y;

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
		throw new IllegalArgumentException(
				"Could not find point in given String");
	}

	public String toString() {
		return String.format("NSPoint: {%d, %d}", x, y);
	}
}
