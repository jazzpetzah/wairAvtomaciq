package com.wearezeta.auto.common.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;

public class ActivationMessage extends BackendMessage {

	public ActivationMessage(Message msg) {
		super(msg);
	}

	public String extractActivationLink() throws IOException,
			MessagingException {
		ArrayList<String> links = new ArrayList<String>();

		String regex = "<a href=\"([^\"]*)\"[^>]*>VERIFY</a>";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher urlMatcher = p.matcher(this.getContent());
		while (urlMatcher.find()) {
			links.add(urlMatcher.group(1));
		}
		return links.get(0);
	}

	private static final String ZETA_PURPOSE_HEADER_NAME = "X-Zeta-Purpose";
	public String getXZetaPurpose() throws MessagingException {
		return this.getHeaderValue(ZETA_PURPOSE_HEADER_NAME);
	}

	private static final String ZETA_KEY_HEADER_NAME = "X-Zeta-Key";
	public String getXZetaKey() throws MessagingException {
		return this.getHeaderValue(ZETA_KEY_HEADER_NAME);
	}

	private static final String ZETA_CODE_HEADER_NAME = "X-Zeta-Code";
	public String getXZetaCode() throws MessagingException {
		return this.getHeaderValue(ZETA_CODE_HEADER_NAME);
	}
	
	public static boolean isActivationMessage(Message msg) {
		try {
			return (msg.getHeader(ZETA_CODE_HEADER_NAME) != null);
		} catch (MessagingException e) {
			return false;
		}
	}
}
