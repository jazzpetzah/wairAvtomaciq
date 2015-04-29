package com.wearezeta.auto.common.email;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;

public class PasswordResetMessage extends WireMessage {

	public PasswordResetMessage(String msg) throws Exception {
		super(msg);
	}

	public String extractPasswordResetLink() throws Exception {
		ArrayList<String> links = new ArrayList<String>();

		String regex = "<a href=\"([^\"]*)\"[^>]*>CHANGE PASSWORD</a>";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher urlMatcher = p.matcher(this.getContent());
		while (urlMatcher.find()) {
			links.add(urlMatcher.group(1));
		}
		return links.get(0);
	}

	private static final String MESSAGE_PURPOSE = "PasswordReset";
	
	public static boolean isPasswordResetMessage(Message msg) {
		try {
			return (msg.getHeader(ZETA_CODE_HEADER_NAME) != null && msg
					.getHeader(ZETA_CODE_HEADER_NAME).equals(MESSAGE_PURPOSE));
		} catch (MessagingException e) {
			return false;
		}
	}
}
