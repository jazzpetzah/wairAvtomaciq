package com.wearezeta.auto.common;

public class EmailHeaders {
	
 	private String lastUserEmail = null;
 	private String mailSubject = null;
 	private String xZetaPurpose = null;
 	private String xZetaKey = null;
 	private String xZetaCode = null;

	String getLastUserEmail() {
		return lastUserEmail;
	}
	void setLastUserEmail(String lastUserEmail) {
		this.lastUserEmail = lastUserEmail;
	}
	String getMailSubject() {
		return mailSubject;
	}
	void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}
	String getXZetaPurpose() {
		return xZetaPurpose;
	}
	void setXZetaPurpose(String xZetaPurpose) {
		this.xZetaPurpose = xZetaPurpose;
	}
	String getXZetaKey() {
		return xZetaKey;
	}
	void setXZetaKey(String xZetaKey) {
		this.xZetaKey = xZetaKey;
	}
	String getXZetaCode() {
		return xZetaCode;
	}
	void setXZetaCode(String xZetaCode) {
		this.xZetaCode = xZetaCode;
	}
}

