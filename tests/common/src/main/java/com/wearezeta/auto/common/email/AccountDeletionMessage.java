package com.wearezeta.auto.common.email;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountDeletionMessage extends WireMessage {

	public AccountDeletionMessage(String msg) throws Exception {
		super(msg);
	}

	public String extractAccountDeletionLink() throws Exception {
		String regex = "https://[a-zA-Z_0-9.=-]+/d/\\?key=[a-zA-Z_0-9.\\-\\\\&_=]+";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher urlMatcher = p.matcher(this.getContent());
		if (urlMatcher.find()) {
			return urlMatcher.group(0);
		} else {
			throw new Exception("Unable to extract URL from mail: " + getContent());
		}
	}

	public static final String MESSAGE_PURPOSE = "Delete";

	@Override
	protected String getExpectedPurposeValue() {
		return MESSAGE_PURPOSE;
	}
}
