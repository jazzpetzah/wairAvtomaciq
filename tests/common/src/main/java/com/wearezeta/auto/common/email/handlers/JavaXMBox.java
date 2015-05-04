package com.wearezeta.auto.common.email.handlers;

import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.mail.*;
import javax.mail.search.SearchTerm;

import org.apache.log4j.Logger;

import com.sun.mail.iap.ProtocolException;
import com.wearezeta.auto.common.email.MessagingUtils;
import com.wearezeta.auto.common.email.handlers.JavaXMBoxChangesListener;
import com.wearezeta.auto.common.log.ZetaLogger;

class JavaXMBox implements ISupportsMessagesPolling {
	private static final String MAIL_PROTOCOL = "imaps";
	private static final String MAILS_FOLDER = "Inbox";
	private static final int FOLDER_OPEN_TIMEOUT = 60 * 15; // seconds
	private static final int MBOX_MAX_CONNECT_RETRIES = 10;
	private static final int NEW_MSG_CHECK_INTERVAL = 500; // milliseconds

	private static final Logger log = ZetaLogger.getLog(JavaXMBox.class
			.getSimpleName());

	private static final Semaphore folderStateGuard = new Semaphore(1);
	private static Thread messagesCountNotifier = null;

	private static Store store = null;
	private static Folder folder = null;

	protected Folder getFolder() {
		return folder;
	}

	protected void openFolder(boolean shouldStartNotifier)
			throws MessagingException, InterruptedException {
		this.openFolder(this.getFolder(), shouldStartNotifier);
	}

	private static Random random = new Random();

	private static final int MAX_OPEN_RETRIES = 3;

	public void openFolder(final Folder folderToOpen,
			boolean shouldStartNotifier) throws MessagingException,
			InterruptedException {
		folderStateGuard.tryAcquire(FOLDER_OPEN_TIMEOUT, TimeUnit.SECONDS);

		if (!folderToOpen.isOpen()) {
			int ntry = 0;
			do {
				try {
					folderToOpen.open(Folder.READ_ONLY);
					break;
				} catch (Exception e) {
					if (((e instanceof MessagingException) || (e instanceof ProtocolException))
							&& ntry + 1 < MAX_OPEN_RETRIES) {
						log.error(String
								.format("Folder open operation failed: '%s' (retry %s of %s)...",
										e.getMessage(), ntry + 1,
										MAX_OPEN_RETRIES));
						Thread.sleep(1000 * (5 + random.nextInt(5)));
						ntry++;
						continue;
					}
					try {
						throw e;
					} finally {
						folderStateGuard.release();
					}
				}
			} while (ntry < MAX_OPEN_RETRIES);
		}

		if (shouldStartNotifier && messagesCountNotifier == null) {
			// This is to force mbox update notifications
			messagesCountNotifier = new Thread() {
				@Override
				public void run() {
					log.debug("Starting email messages notifier thread...");
					while (JavaXMBox.this.getFolder() != null
							&& !this.isInterrupted()) {
						try {
							if (!JavaXMBox.this.getFolder().isOpen()) {
								JavaXMBox.this.getFolder().open(
										Folder.READ_ONLY);
								JavaXMBox.this.getFolder().getMessageCount();
							}
							Thread.sleep(NEW_MSG_CHECK_INTERVAL);
						} catch (InterruptedException e) {
							log.debug("Email message notifier thread has been successfully interrupted");
							return;
						} catch (MessagingException e) {
							e.printStackTrace();
						}
					}
					log.debug("Email message notifier thread exited");
				}
			};
			messagesCountNotifier.start();
		}
	}

	protected void closeFolder(boolean shouldStopNotifier)
			throws MessagingException {
		this.closeFolder(this.getFolder(), shouldStopNotifier);
	}

	public void closeFolder(Folder folderToClose, boolean shouldStopNotifier)
			throws MessagingException {
		try {
			if (folderToClose.isOpen()) {
				folderToClose.close(false);
			}
		} finally {
			if (shouldStopNotifier && messagesCountNotifier != null) {
				messagesCountNotifier.interrupt();
				messagesCountNotifier = null;
			}
			folderStateGuard.release();
		}
	}

	private final ExecutorService pool = Executors.newFixedThreadPool(1);

	public Future<String> getMessage(Map<String, String> expectedHeaders,
			int timeoutSeconds, long rejectMessagesBeforeTimestamp)
			throws Exception {
		this.openFolder(true);
		JavaXMBoxChangesListener listener = new JavaXMBoxChangesListener(this,
				expectedHeaders, timeoutSeconds, rejectMessagesBeforeTimestamp);
		this.getFolder().addMessageCountListener(listener);
		log.debug(String.format(
				"Started email listener for message containing headers %s...",
				expectedHeaders.toString()));
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

	private static void initStore() throws Exception {
		final Properties props = System.getProperties();
		final Session session = Session.getInstance(props, null);
		int ntry = 0;
		Exception savedException = null;
		do {
			try {
				store = session.getStore(MAIL_PROTOCOL);
				store.connect(MessagingUtils.getServerHost(), -1,
						MessagingUtils.getAccountName(),
						MessagingUtils.getAccountPassword());
				break;
			} catch (Exception e) {
				log.debug(String.format(
						"Failed to connect to the mailbox (ntry %d of %d):",
						ntry, MBOX_MAX_CONNECT_RETRIES));
				e.printStackTrace();
				savedException = e;
			}
			Thread.sleep(30 * 1000);
			ntry++;
		} while (ntry < MBOX_MAX_CONNECT_RETRIES);
		if (ntry >= MBOX_MAX_CONNECT_RETRIES) {
			throw savedException;
		}
		folder = store.getDefaultFolder().getFolder(MAILS_FOLDER);
	}

	private static final Semaphore storeLock = new Semaphore(1);

	public JavaXMBox() throws Exception {
		storeLock.acquire();
		try {
			if (store == null) {
				initStore();
			}
		} finally {
			storeLock.release();
		}
	}

	@Override
	public void waitUntilMessagesCountReaches(String deliveredTo,
			int expectedMsgsCount, int timeoutSeconds) throws Exception {
		SearchTerm term = new SearchTerm() {
			private static final long serialVersionUID = 1L;

			public boolean match(Message message) {
				try {
					if (MessagingUtils.extractDeliveredToValue(message).equals(
							deliveredTo)) {
						return true;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				return false;
			}
		};

		int fetchedMsgsCount = 0;
		this.openFolder(false);
		try {
			final long millisecondsStarted = System.currentTimeMillis();
			do {
				fetchedMsgsCount = folder.search(term).length;
				if (fetchedMsgsCount >= expectedMsgsCount) {
					break;
				}
				Thread.sleep(NEW_MSG_CHECK_INTERVAL);
			} while (System.currentTimeMillis() - millisecondsStarted <= timeoutSeconds * 1000);
		} finally {
			this.closeFolder(false);
		}
		assert fetchedMsgsCount >= expectedMsgsCount : String
				.format("Count of messages delivered to %s is %s, but should be at least %s after %s second(s)",
						deliveredTo, fetchedMsgsCount, expectedMsgsCount,
						timeoutSeconds);
	}
}
