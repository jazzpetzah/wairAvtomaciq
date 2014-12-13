package com.wearezeta.auto.common.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;

import javax.mail.*;

public class IMAPSMailbox {
	private static final Semaphore semaphore = new Semaphore(1);

	private static final String MAIL_PROTOCOL = "imaps";
	private String mailServer;
	private String mailFolder;
	private Folder folder = null;
	private String user;
	private String password;
	
	private Thread messagesCountNotifier;

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
			CACHED_FOLDERS.put(serverName, localFolder);
		}
		return CACHED_FOLDERS.get(serverName);
	}

	public static void clearCaches() throws MessagingException {
		for (String serverName : CACHED_STORES.keySet()) {
			if (CACHED_FOLDERS.containsKey(serverName)) {
				Folder folder = CACHED_FOLDERS.get(serverName);
				if (folder.isOpen()) {
					folder.close(true);
				}
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
	
	public IMAPSMailbox open() throws MessagingException, InterruptedException {
		semaphore.acquire();
		try {
			folder = getCachedFolder(mailServer, mailFolder, user, password);
			if (!folder.isOpen()) {
				try {
					folder.open(Folder.READ_WRITE);
				} catch (MessagingException ex) {
					folder.open(Folder.READ_ONLY);
				}
			}
		} finally {
			semaphore.release();
		}
		return this;
	}

	public List<EmailHeaders> getLastMailHeaders(int msgsCount)
			throws MessagingException {
		int currentMsgsCount = folder.getMessageCount();
		Message[] fetchedMsgs;
		if (msgsCount > currentMsgsCount) {
			fetchedMsgs = folder.getMessages();
		} else {
			fetchedMsgs = folder.getMessages(currentMsgsCount - msgsCount,
					currentMsgsCount);
		}
		List<EmailHeaders> resultList = new ArrayList<EmailHeaders>();
		for (Message msg : fetchedMsgs) {
			resultList.add(EmailHeaders.createFromMessage(msg));
		}
		return resultList;
	}

	public MBoxChangesListener startMboxMonitor(
			Map<String, String> expectedHeaders) {
		CountDownLatch wait = new CountDownLatch(1);
		MBoxChangesListener listener = new MBoxChangesListener(this,
				expectedHeaders, wait);
		// This is to force mbox update notifications
		messagesCountNotifier = new Thread() {
			@Override
			public void run() {
				Folder dstFolder = IMAPSMailbox.this.folder;
				while (dstFolder.isOpen()) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						break;
					}
				}
			}
		};
		messagesCountNotifier.start();
		folder.addMessageCountListener(listener);
		return listener;
	}

	public static EmailHeaders getFilteredMessageHeaders(
			MBoxChangesListener listener, int timeout) throws TimeoutException,
			InterruptedException, MessagingException {
		Message message = listener.getMatchedMessage(timeout);
		if (message != null) {
			return EmailHeaders.createFromMessage(message);
		}
		throw new TimeoutException(
				String.format(
						"The email message has not been received within %s second(s) timeout",
						timeout));
	}

	public void close() throws InterruptedException {
		semaphore.acquire();
		try {
			if (folder.isOpen()) {
				try {
					folder.close(true);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
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
