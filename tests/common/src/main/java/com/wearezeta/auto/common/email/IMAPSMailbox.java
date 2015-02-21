package com.wearezeta.auto.common.email;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.mail.*;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

public class IMAPSMailbox {
	private static final String MAIL_PROTOCOL = "imaps";
	private static final String MAILS_FOLDER = "Inbox";
	private static final int FOLDER_OPEN_TIMEOUT = 60 * 5; // seconds
	private static final int TOO_MANY_CONNECTIONS_TIMEOUT = 60 * 5 * 1000; // milliseconds

	private static final Logger log = ZetaLogger.getLog(IMAPSMailbox.class
			.getSimpleName());

	private final Semaphore folderStateGuard = new Semaphore(1);

	private static Store store = null;
	static {
		final Properties props = System.getProperties();
		final Session session = Session.getInstance(props, null);
		try {
			try {
				store = session.getStore(MAIL_PROTOCOL);
				store.connect(getServerName(), -1, getName(), getPassword());
			} catch (AuthenticationFailedException e) {
				if (e.getMessage().contains("simultaneous")) {
					Thread.sleep(TOO_MANY_CONNECTIONS_TIMEOUT);
					store.connect(getServerName(), -1, getName(), getPassword());
				} else {
					throw e;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private Folder folder = null;

	protected Folder getFolder() {
		return this.folder;
	}

	protected void openFolder() throws MessagingException, InterruptedException {
		this.openFolder(this.getFolder());
	}

	public void openFolder(Folder folderToOpen) throws MessagingException,
			InterruptedException {
		folderStateGuard.tryAcquire(FOLDER_OPEN_TIMEOUT, TimeUnit.SECONDS);
		if (!folderToOpen.isOpen()) {
			folderToOpen.open(Folder.READ_ONLY);
		}
	}

	protected void closeFolder() throws MessagingException {
		this.closeFolder(this.getFolder());
	}

	public void closeFolder(Folder folderToClose) throws MessagingException {
		try {
			if (folderToClose.isOpen()) {
				folderToClose.close(false);
			}
		} finally {
			folderStateGuard.release();
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

	private final ExecutorService pool = Executors.newFixedThreadPool(5);

	public Future<Message> getMessage(Map<String, String> expectedHeaders)
			throws MessagingException, InterruptedException {
		this.openFolder();
		MBoxChangesListener listener = new MBoxChangesListener(this,
				expectedHeaders);

		folder.addMessageCountListener(listener);
		return pool.submit(listener);
	}

	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					store.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static IMAPSMailbox instance = null;

	public static synchronized IMAPSMailbox getInstance() throws Exception {
		if (instance == null) {
			instance = new IMAPSMailbox();
			log.debug(String.format("Created %s singleton",
					IMAPSMailbox.class.getSimpleName()));
		}
		return instance;
	}

	private IMAPSMailbox() throws Exception {
		this.folder = store.getDefaultFolder().getFolder(MAILS_FOLDER);
	}

	public static String getServerName() throws Exception {
		return CommonUtils.getDefaultEmailServerFromConfig(IMAPSMailbox.class);
	}

	public static String getName() throws Exception {
		return CommonUtils.getDefaultEmailFromConfig(IMAPSMailbox.class);
	}

	private static String getPassword() throws Exception {
		return CommonUtils.getDefaultPasswordFromConfig(IMAPSMailbox.class);
	}
}
