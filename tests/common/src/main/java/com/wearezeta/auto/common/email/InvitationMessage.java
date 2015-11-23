package com.wearezeta.auto.common.email;

public class InvitationMessage extends WireMessage {

	public InvitationMessage(String msg) throws Exception {
		super(msg);
	}

	@Override
	protected String getExpectedPurposeValue() {
		return MESSAGE_PURPOSE;
	}

	public static final String MESSAGE_PURPOSE = "Invitation";
}
