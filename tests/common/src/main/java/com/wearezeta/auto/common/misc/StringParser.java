package com.wearezeta.auto.common.misc;

import org.apache.commons.lang3.StringEscapeUtils;

public class StringParser {
	
	public static String unescapeString(String stringInput){
		
		String parsedString = StringEscapeUtils.unescapeJava(stringInput);
		return parsedString;
	}

}
