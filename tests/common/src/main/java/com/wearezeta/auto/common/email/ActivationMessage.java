package com.wearezeta.auto.common.email;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivationMessage extends WireMessage {

	public ActivationMessage(String msg) throws Exception {
		super(msg);
	}

    private static final String ZETA_KEY_HEADER_NAME = "X-Zeta-Key";

    public String getXZetaKey() throws MessagingException {
        return this.getHeaderValue(ZETA_KEY_HEADER_NAME);
    }

    private static final String ZETA_CODE_HEADER_NAME = "X-Zeta-Code";

    public String getXZetaCode() throws MessagingException {
        return this.getHeaderValue(ZETA_CODE_HEADER_NAME);
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

	@Override
	protected String getExpectedPurposeValue() {
		return MESSAGE_PURPOSE;
	}
}
