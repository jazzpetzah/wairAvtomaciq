package com.wearezeta.auto.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.*;

public class IMAPSMailbox {
	
	private final String MAIL_PROTOCOL = "imaps";
	private String mailServer;
	private String mailFolder;
	private Store store = null;
	private Folder folder = null;
	private String user;
	private String password;
	
	
	
	public IMAPSMailbox(String mailServer, String mailFolder, String user, String password) throws MessagingException {
		this.mailServer = mailServer;
		this.mailFolder = mailFolder;
		this.user = user;
		this.password = password;
	}
	
	private List<EmailHeaders> getPredefinedHeadersFromMessages(Message[] messages) throws MessagingException {
		List<EmailHeaders> headersList = new ArrayList<EmailHeaders>();
		for (Message message : messages) {
			EmailHeaders headers = new EmailHeaders();
			headers.setLastUserEmail(message.getHeader("Delivered-To")[0]);
			headers.setMailSubject(message.getHeader("Subject")[0]);
			headers.setXZetaPurpose(message.getHeader("X-Zeta-Purpose")[0]);
			headers.setXZetaKey(message.getHeader("X-Zeta-Key")[0]);
			headers.setXZetaCode(message.getHeader("X-Zeta-Code")[0]);
			headersList.add(headers);
		}	
		return headersList;
	}
	
	public List<EmailHeaders> getLastMailHeaders(int messageCount)
			throws MessagingException {
		int allMsgsCount = folder.getMessageCount();
		int msgsCntToFetch = messageCount;
		if (allMsgsCount == 0) {
			System.out.println("ERROR: " + mailFolder + " folder is empty!");
			return new ArrayList<EmailHeaders>();
		} else if (allMsgsCount - msgsCntToFetch < 0) {
			msgsCntToFetch = allMsgsCount;
		}
	
		Message[] messages = folder.getMessages(
				(allMsgsCount - msgsCntToFetch), allMsgsCount);
		return getPredefinedHeadersFromMessages(messages);
	}
	
	public void open() throws MessagingException {
		Properties props = System.getProperties();
		Session session = Session.getInstance(props, null);
	    //store = session.getStore(MAIL_PROTOCOL);
		Store store = null;
		if (MAIL_PROTOCOL != null)
		store = session.getStore(MAIL_PROTOCOL);
		else
		store = session.getStore();

		if (mailServer != null || user != null || password != null)
			store.connect(mailServer, -1, user, password);
		else
			store.connect();

		Folder defaultFolder = store.getDefaultFolder();
		folder = defaultFolder.getFolder(mailFolder);

		try {
			folder.open(Folder.READ_WRITE);
		} catch (MessagingException ex) {
			folder.open(Folder.READ_ONLY);
		}
	}

    public void close() throws MessagingException {
		if (folder != null) {
			folder.close(false);
		}
		if (store != null) {
			store.close();
		}   	
    }
	 
}

