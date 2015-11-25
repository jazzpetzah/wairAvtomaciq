package com.wearezeta.auto.common.email;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvitationMessage extends WireMessage {

	public InvitationMessage(String msg) throws Exception {
		super(msg);
	}

	public String extractInvitationLink() throws Exception {
		ArrayList<String> links = new ArrayList<String>();

		String regex = "<a href=\"([^\"]*)\"[^>]*>ACCEPT</a>";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher urlMatcher = p.matcher(this.getContent());
		while (urlMatcher.find()) {
			links.add(urlMatcher.group(1));
		}
		return links.get(0);
	}

	@Override
	protected String getExpectedPurposeValue() {
		return MESSAGE_PURPOSE;
	}

	public static final String MESSAGE_PURPOSE = "Invitation";
}
