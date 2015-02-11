package com.wearezeta.auto.common.email;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;

public class WireMessage extends BackendMessage {

	public WireMessage(Message msg) throws MessagingException, IOException {
		super(msg);
	}

	protected static final String ZETA_PURPOSE_HEADER_NAME = "X-Zeta-Purpose";

	public String getXZetaPurpose() throws MessagingException {
		return this.getHeaderValue(ZETA_PURPOSE_HEADER_NAME);
	}

	protected static final String ZETA_KEY_HEADER_NAME = "X-Zeta-Key";

	public String getXZetaKey() throws MessagingException {
		return this.getHeaderValue(ZETA_KEY_HEADER_NAME);
	}

	protected static final String ZETA_CODE_HEADER_NAME = "X-Zeta-Code";

	public String getXZetaCode() throws MessagingException {
		return this.getHeaderValue(ZETA_CODE_HEADER_NAME);
	}

}
