package com.wearezeta.auto.common.email;

import javax.mail.Message;
import javax.mail.MessagingException;

public class EmailHeaders {
	
 	private String lastUserEmail = null;
 	private String mailSubject = null;
 	private String xZetaPurpose = null;
 	private String xZetaKey = null;
 	private String xZetaCode = null;

	public String getLastUserEmail() {
		return lastUserEmail;
	}
	public void setLastUserEmail(String lastUserEmail) {
		this.lastUserEmail = lastUserEmail;
	}
	public String getMailSubject() {
		return mailSubject;
	}
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}
	public String getXZetaPurpose() {
		return xZetaPurpose;
	}
	public void setXZetaPurpose(String xZetaPurpose) {
		this.xZetaPurpose = xZetaPurpose;
	}
	public String getXZetaKey() {
		return xZetaKey;
	}
	public void setXZetaKey(String xZetaKey) {
		this.xZetaKey = xZetaKey;
	}
	public String getXZetaCode() {
		return xZetaCode;
	}
	public void setXZetaCode(String xZetaCode) {
		this.xZetaCode = xZetaCode;
	}
	
	public static EmailHeaders createFromMessage(Message msg) throws MessagingException {
		EmailHeaders headers = new EmailHeaders();
		headers.setLastUserEmail(msg.getHeader("Delivered-To")[0]);
		headers.setMailSubject(msg.getHeader("Subject")[0]);
		headers.setXZetaPurpose(msg.getHeader("X-Zeta-Purpose")[0]);
		headers.setXZetaKey(msg.getHeader("X-Zeta-Key")[0]);
		headers.setXZetaCode(msg.getHeader("X-Zeta-Code")[0]);
		return headers;
	}
}

