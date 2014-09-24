package com.wearezeta.auto.perfhelper;

import com.wearezeta.auto.common.ClientUser;

public class ConvPair {
	private ClientUser contact;
	private String convName;
	
	public String getConvName() {
		return convName;
	}
	public void setConvName(String convName) {
		this.convName = convName;
	}
	public ClientUser getContact() {
		return contact;
	}
	public void setContact(ClientUser contact) {
		this.contact = contact;
	}

}
