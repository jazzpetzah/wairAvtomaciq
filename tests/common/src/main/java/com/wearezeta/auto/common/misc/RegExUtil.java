package com.wearezeta.auto.common.misc;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExUtil {

	public static String getStringByRegEx(String inputString, String regEx,
			boolean returnFirstMatchOnly) {
		String regExResult = "";
		Pattern _pattern = Pattern.compile(regEx);
		Matcher _matcher = _pattern.matcher(inputString);
		_matcher.find();
		try {
			regExResult = regExResult.concat(_matcher.group());
		} catch (IllegalStateException e) {
			// e.printStackTrace();
			// System.out.println("No match found for the given RegEx");
			regExResult = null;
		}
		while (_matcher.find() && !returnFirstMatchOnly) {
			regExResult = regExResult.concat(":").concat(_matcher.group());
		}
		return regExResult;
	}

	public static String getStringGroupByRegEx(String inputString,
			String regEx, int groupToReturn) {
		String regExResult = null;
		Pattern _pattern = Pattern.compile(regEx);
		Matcher _matcher = _pattern.matcher(inputString);
		ArrayList<String> str = new ArrayList<String>();
		while (_matcher.find())
			str.add(_matcher.group());
		String[] ar = (String[]) str.toArray(new String[0]);
		if (ar.length - 1 < groupToReturn || groupToReturn < 0) {
			System.out.println("No match found");
			return null;
		}
		regExResult = (String) str.toArray()[groupToReturn];
		return regExResult;
	}
}
