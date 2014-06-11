package com.wearezeta.auto.osx.util;

import java.util.regex.Matcher;

public class NSPoint {
	private int x;
	private int y;

	public NSPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int x() { return x; }
	public int y() { return y; }

	public static NSPoint fromString(String coords) {
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(".*\\{([0-9]*),\\s([0-9]*)\\}");
		Matcher matcher = pattern.matcher(coords);
		while (matcher.find()) {
			return new NSPoint(Integer.parseInt(matcher.group(1)),Integer.parseInt(matcher.group(2)));
		}
		return null;
	}
	
	public String toString() {
		return "NSPoint: {" + x + ", " + y + "}";
	}
}