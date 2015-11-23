package com.wearezeta.auto.common.email;

import javax.mail.MessagingException;

public abstract class WireMessage extends BackendMessage {

	public WireMessage(String msg) throws Exception {
		super(msg);
	}

	protected static final String ZETA_PURPOSE_HEADER_NAME = "X-Zeta-Purpose";

	public String getXZetaPurpose() throws MessagingException {
		return this.getHeaderValue(ZETA_PURPOSE_HEADER_NAME);
	}

    protected abstract String getExpectedPurposeValue();

    public boolean isValid() {
        try {
            return getXZetaPurpose().equals(getExpectedPurposeValue());
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
