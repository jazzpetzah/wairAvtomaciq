package com.wearezeta.auto.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Semaphore;

import javax.mail.*;

public class IMAPSMailbox {
	private static final Semaphore semaphore = new Semaphore(1);
	
	private static final String MAIL_PROTOCOL = "imaps";
	private String mailServer;
	private String mailFolder;
	private Folder folder = null;
	private String user;
	private String password;

	private static Map<String, Store> CACHED_STORES = new HashMap<String, Store>();
	private static Map<String, Folder> CACHED_FOLDERS = new HashMap<String, Folder>();

	private static Folder getCachedFolder(String serverName, String folderName,
			String user, String password) throws MessagingException {
		if (!CACHED_STORES.containsKey(serverName)) {
			Properties props = System.getProperties();
			Session session = Session.getInstance(props, null);
			Store store = session.getStore(MAIL_PROTOCOL);
			store.connect(serverName, -1, user, password);
			CACHED_STORES.put(serverName, store);

			Folder defaultFolder = store.getDefaultFolder();
			Folder localFolder = defaultFolder.getFolder(folderName);
			try {
				localFolder.open(Folder.READ_WRITE);
			} catch (MessagingException ex) {
				localFolder.open(Folder.READ_ONLY);
			}
			CACHED_FOLDERS.put(serverName, localFolder);
		}
		return CACHED_FOLDERS.get(serverName);
	}

	public static void clearCaches() throws MessagingException {
		for (String serverName : CACHED_STORES.keySet()) {
			if (CACHED_FOLDERS.containsKey(serverName)) {
				Folder folder = CACHED_FOLDERS.get(serverName);
				folder.close(false);
			}
			Store store = CACHED_STORES.get(serverName);
			store.close();
		}
		CACHED_STORES.clear();
		CACHED_FOLDERS.clear();
	}


	public IMAPSMailbox(String mailServer, String mailFolder, String user,
			String password) throws MessagingException {
		this.mailServer = mailServer;
		this.mailFolder = mailFolder;
		this.user = user;
		this.password = password;
	}

	private List<EmailHeaders> getPredefinedHeadersFromMessages(
			Message[] messages) throws MessagingException {
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

	public void open() throws MessagingException, InterruptedException {
		semaphore.acquire();
		try {
			folder = getCachedFolder(mailServer, mailFolder, user, password);
		} finally {
			semaphore.release();
		}
	}

	public void close() throws InterruptedException {
		semaphore.acquire();
		try {
		folder = null;
		} finally {
			semaphore.release();
		}
	}
	
	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					IMAPSMailbox.clearCaches();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		});
	}
}

