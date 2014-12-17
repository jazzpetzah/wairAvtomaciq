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

	private Folder folder = null;
	private String user;
	private Thread messagesCountNotifier;

	private static Map<String, Store> CACHED_STORES = new HashMap<String, Store>();

	private static Store getCachedStore(String serverName, String user,
			String password) throws MessagingException, InterruptedException {
		semaphore.acquire();
		try {
			if (!CACHED_STORES.containsKey(serverName)) {
				Properties props = System.getProperties();
				Session session = Session.getInstance(props, null);
				Store store = session.getStore(MAIL_PROTOCOL);
				store.connect(serverName, -1, user, password);
				CACHED_STORES.put(serverName, store);
			}
			return CACHED_STORES.get(serverName);
		} finally {
			semaphore.release();
		}
	}

	public static void clearCaches() throws MessagingException,
			InterruptedException {
		semaphore.acquire();
		try {
			for (String serverName : CACHED_STORES.keySet()) {
				Store store = CACHED_STORES.get(serverName);
				store.close();
			}
			CACHED_STORES.clear();
		} finally {
			semaphore.release();
		}
	}

	public IMAPSMailbox(String mailServer, String mailFolder, String user,
			String password) throws MessagingException, InterruptedException {
		this.user = user;
		Store store = getCachedStore(mailServer, user, password);
		Folder defaultFolder = store.getDefaultFolder();
		this.folder = defaultFolder.getFolder(mailFolder);
	}

	public String getUser() {
		return this.user;
	}

	private void openFolder() throws MessagingException {
		if (!folder.isOpen()) {
			try {
				folder.open(Folder.READ_WRITE);
			} catch (MessagingException ex) {
				folder.open(Folder.READ_ONLY);
			}
		}
	}

	public List<EmailHeaders> getLastMailHeaders(int msgsCount)
			throws MessagingException, InterruptedException {
		if (!folder.isOpen()) {
			this.openFolder();
		}
		try {
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
		} finally {
			this.closeFolder();
		}
	}

	public MBoxChangesListener startMboxListener(
			Map<String, String> expectedHeaders) throws MessagingException,
			InterruptedException {
		if (!folder.isOpen()) {
			this.openFolder();
		}
		CountDownLatch wait = new CountDownLatch(1);
		MBoxChangesListener listener = new MBoxChangesListener(this,
				expectedHeaders, wait);
		// This is to force mbox update notifications
		messagesCountNotifier = new Thread() {
			@Override
			public void run() {
				Folder dstFolder = IMAPSMailbox.this.folder;
				while (dstFolder != null && dstFolder.isOpen()) {
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
		try {
			Message message = listener.getMatchedMessage(timeout);
			if (message != null) {
				return EmailHeaders.createFromMessage(message);
			}
			throw new TimeoutException(
					String.format(
							"The email message for user %s has not been received within %s second(s) timeout",
							listener.getParentMBox().getUser(), timeout));
		} finally {
			listener.getParentMBox().closeFolder();
		}
	}

	private void closeFolder() {
		if (folder.isOpen()) {
			try {
				folder.close(true);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					IMAPSMailbox.clearCaches();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
