package com.wearezeta.auto.common.email;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.mail.BodyPart;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

public class BackendMessage {
	private Message msg;
	private Map<String, String> mapHeaders = new HashMap<String, String>();

	public BackendMessage(Message msg) throws Exception {
		this.msg = msg;

		IMAPSMailbox.getInstance().openFolder();
		try {
			@SuppressWarnings("unchecked")
			Enumeration<Header> hdrs = msg.getAllHeaders();
			while (hdrs.hasMoreElements()) {
				Header hdr = hdrs.nextElement();
				mapHeaders.put(hdr.getName(), hdr.getValue());
			}
		} finally {
			IMAPSMailbox.getInstance().closeFolder();
		}
	}

	public final String getHeaderValue(String headerName)
			throws MessagingException {
		return this.mapHeaders.get(headerName);
	}

	public String getContent() throws Exception {
		IMAPSMailbox.getInstance().openFolder();
		try {
			Object msgContent = this.msg.getContent();
			if (msgContent instanceof Multipart) {
				Multipart multipart = (Multipart) msgContent;
				StringBuilder multipartContent = new StringBuilder();
				for (int j = 0; j < multipart.getCount(); j++) {
					BodyPart bodyPart = multipart.getBodyPart(j);
					if (bodyPart.getDisposition() == null) {
						multipartContent.append(getText(bodyPart));
					}
				}
				return multipartContent.toString();
			} else {
				return msgContent.toString();
			}
		} finally {
			IMAPSMailbox.getInstance().closeFolder();
		}
	}

	/**
	 * http://www.oracle.com/technetwork/java/javamail/faq/index.html#mainbody
	 * 
	 * @param p
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	private String getText(Part p) throws MessagingException, IOException {
		if (p.isMimeType("text/*")) {
			String s = (String) p.getContent();
			p.isMimeType("text/html");
			return s;
		}

		if (p.isMimeType("multipart/alternative")) {
			// prefer html text over plain text
			Multipart mp = (Multipart) p.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					if (text == null)
						text = getText(bp);
					continue;
				} else if (bp.isMimeType("text/html")) {
					String s = getText(bp);
					if (s != null)
						return s;
				} else {
					return getText(bp);
				}
			}
			return text;
		} else if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				String s = getText(mp.getBodyPart(i));
				if (s != null)
					return s;
			}
		}

		return null;
	}

	private static final String DELIVERED_TO_HEADER_NAME = "Delivered-To";

	public String getLastUserEmail() throws MessagingException {
		return this.getHeaderValue(DELIVERED_TO_HEADER_NAME);
	}

	private static final String SUBJECT_HEADER_NAME = "Subject";

	public String getMailSubject() throws MessagingException {
		return this.getHeaderValue(SUBJECT_HEADER_NAME);
	}
}
