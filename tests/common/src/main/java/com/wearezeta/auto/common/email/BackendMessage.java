package com.wearezeta.auto.common.email;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

public class BackendMessage {
	private Message msg;

	private Message getMessage() {
		return this.msg;
	}

	public BackendMessage(Message msg) {
		this.msg = msg;
	}

	public final String getHeaderValue(String headerName)
			throws MessagingException {
		boolean wasFolderOpened = this.msg.getFolder().isOpen();
		try {
			if (!wasFolderOpened) {
				this.msg.getFolder().open(Folder.READ_ONLY);
			}
			return this.getMessage().getHeader(headerName)[0];
		} finally {
			if (!wasFolderOpened) {
				this.msg.getFolder().close(false);
			}
		}
	}

	public String getContent() throws IOException, MessagingException {
		String content = "";
		boolean wasFolderOpened = this.msg.getFolder().isOpen();
		Object msgContent = null;
		try {
			if (!wasFolderOpened) {
				this.msg.getFolder().open(Folder.READ_ONLY);
			}
			msgContent = this.getMessage().getContent();

			if (msgContent instanceof Multipart) {
				Multipart multipart = (Multipart) msgContent;
				StringBuilder multipartContent = new StringBuilder();
				for (int j = 0; j < multipart.getCount(); j++) {
					BodyPart bodyPart = multipart.getBodyPart(j);
					if (bodyPart.getDisposition() == null) {
						multipartContent.append(getText(bodyPart));
					}
				}
				content = multipartContent.toString();
			} else {
				content = msgContent.toString();
			}

			return content;
		} finally {
			if (!wasFolderOpened) {
				this.msg.getFolder().close(false);
			}
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
