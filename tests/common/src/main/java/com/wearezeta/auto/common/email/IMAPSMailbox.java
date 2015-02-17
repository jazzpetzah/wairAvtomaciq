package com.wearezeta.auto.common.email;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;

import javax.mail.*;

import com.wearezeta.auto.common.CommonUtils;

public class IMAPSMailbox {
	private static final Semaphore semaphore = new Semaphore(1);
	private static final String MAIL_PROTOCOL = "imaps";
	private static final String MAILS_FOLDER = "Inbox";
	private static final int NEW_MSG_MIN_CHECK_INTERVAL = 5000; // seconds

	private static final Random random = new Random();

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
				try {
					final Folder inbox = store.getDefaultFolder().getFolder(
							MAILS_FOLDER);
					if (inbox.isOpen()) {
						inbox.close(false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
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

	private void openFolder() throws MessagingException, InterruptedException {
		int tryNum = 0;
		final int maxRetry = 5;

		if (!folder.isOpen()) {
			while (true) {
				try {
					folder.open(Folder.READ_ONLY);
					return;
				} catch (MessagingException ex) {
					Thread.sleep((tryNum + 1) * 3 * 1000);
					tryNum++;
					if (tryNum >= maxRetry) {
						throw ex;
					}
				}
			}
		}
	}

	private void closeFolder() throws MessagingException {
		if (folder.isOpen()) {
			folder.close(false);
		}
	}

	public List<Message> getRecentMessages(int msgsCount)
			throws MessagingException, InterruptedException {
		this.openFolder();
		try {
			int currentMsgsCount = folder.getMessageCount();
			Message[] fetchedMsgs;
			if (msgsCount > currentMsgsCount) {
				fetchedMsgs = folder.getMessages();
			} else {
				fetchedMsgs = folder.getMessages(currentMsgsCount - msgsCount,
						currentMsgsCount);
			}
			return new ArrayList<Message>(Arrays.asList(fetchedMsgs));
		} finally {
			this.closeFolder();
		}
	}

	public MBoxChangesListener startMboxListener(
			Map<String, String> expectedHeaders) throws MessagingException,
			InterruptedException {
		this.openFolder();
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
						Thread.sleep(NEW_MSG_MIN_CHECK_INTERVAL
								+ random.nextInt(NEW_MSG_MIN_CHECK_INTERVAL));
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		};
		messagesCountNotifier.start();
		folder.addMessageCountListener(listener);
		return listener;
	}

	public static Message getFilteredMessage(MBoxChangesListener listener,
			int timeout) throws TimeoutException, InterruptedException,
			MessagingException {
		try {
			Message message = listener.getMatchedMessage(timeout);
			if (message != null) {
				return message;
			}
			throw new TimeoutException(
					String.format(
							"The email message for user %s has not been received within %s second(s) timeout",
							listener.getParentMBox().getUser(), timeout));
		} finally {
			listener.getParentMBox().closeFolder();
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

	public static IMAPSMailbox createDefaultInstance() throws Exception {
		return new IMAPSMailbox(
				CommonUtils.getDefaultEmailServerFromConfig(IMAPSMailbox.class),
				MAILS_FOLDER, getName(), getPassword());
	}

	public static String getName() throws Exception {
		return CommonUtils.getDefaultEmailFromConfig(IMAPSMailbox.class);
	}

	private static String getPassword() throws Exception {
		return CommonUtils.getDefaultPasswordFromConfig(IMAPSMailbox.class);
	}
}
