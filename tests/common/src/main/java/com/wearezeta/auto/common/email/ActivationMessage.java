package com.wearezeta.auto.common.email;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;

public class ActivationMessage extends WireMessage {

	public ActivationMessage(Message msg) throws Exception {
		super(msg);
	}

	public String extractActivationLink() throws Exception {
		ArrayList<String> links = new ArrayList<String>();

		String regex = "<a href=\"([^\"]*)\"[^>]*>VERIFY</a>";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher urlMatcher = p.matcher(this.getContent());
		while (urlMatcher.find()) {
			links.add(urlMatcher.group(1));
		}
		return links.get(0);
	}

	private static final String MESSAGE_PURPOSE = "Activation";
	
	public static boolean isActivationMessage(Message msg) {
		try {
			return (msg.getHeader(ZETA_CODE_HEADER_NAME) != null && msg
					.getHeader(ZETA_CODE_HEADER_NAME).equals(MESSAGE_PURPOSE));
		} catch (MessagingException e) {
			return false;
		}
	}
}
