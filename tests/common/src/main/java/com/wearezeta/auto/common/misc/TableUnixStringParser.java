package com.wearezeta.auto.common.misc;

import org.apache.commons.lang3.StringEscapeUtils;

public class TableUnixStringParser {
	
	public static String convertStringToUnixCode(String stringInput){
		
		String parsedString = StringEscapeUtils.unescapeJava(stringInput);
		return parsedString;
	}

}
