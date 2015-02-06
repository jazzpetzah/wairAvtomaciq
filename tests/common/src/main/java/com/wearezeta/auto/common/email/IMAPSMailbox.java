package com.wearezeta.auto.common.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
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

	public static String getActivationLink(
			MBoxChangesListener listener, int timeout) throws TimeoutException,
			InterruptedException, MessagingException, IOException {
		try {
			Message message = listener.getMatchedMessage(timeout);
			if (message != null) {
				return createActivationURLFromMessage(message);
			}
			throw new TimeoutException(
					String.format(
							"The email message for user %s has not been received within %s second(s) timeout",
							listener.getParentMBox().getUser(), timeout));
		} finally {
			listener.getParentMBox().closeFolder();
		}
	}
	
	private static String createActivationURLFromMessage(Message message) throws IOException, MessagingException{
		String content = "";        
		Object msgContent = message.getContent();
		if (msgContent instanceof Multipart) {

			Multipart multipart = (Multipart) msgContent;
			for (int j = 0; j < multipart.getCount(); j++) {

				BodyPart bodyPart = multipart.getBodyPart(j);

				String disposition = bodyPart.getDisposition();

				if (disposition != null && (disposition.equalsIgnoreCase(Part.ATTACHMENT))) { 
					DataHandler handler = bodyPart.getDataHandler();                                 
				}
				else { 
					content = getText(bodyPart);  
				}
			}
		}
		else            {    
			content= message.getContent().toString();
		}

		return pullLink(content);
	}
	private static String getText(Part p) throws
	MessagingException, IOException {
		if (p.isMimeType("text/*")) {
			String s = (String)p.getContent();
			p.isMimeType("text/html");
			return s;
		}

		if (p.isMimeType("multipart/alternative")) {
			// prefer html text over plain text
			Multipart mp = (Multipart)p.getContent();
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
			Multipart mp = (Multipart)p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				String s = getText(mp.getBodyPart(i));
				if (s != null)
					return s;
			}
		}

		return null;
	}

	private static String pullLink(String text) {
		ArrayList<String> links = new ArrayList<String>();

		String regex = "<a href=\"([^\"]*)\"[^>]*>VERIFY</a>";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher urlMatcher = p.matcher(text);
		while (urlMatcher.find())
		{
			links.add(urlMatcher.group(1));
		}
		return links.get(0);
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

	public static IMAPSMailbox createDefaultInstance()
			throws MessagingException, IOException, InterruptedException {
		return new IMAPSMailbox(
				CommonUtils.getDefaultEmailServerFromConfig(IMAPSMailbox.class),
				MAILS_FOLDER, getName(), getPassword());
	}

	public static String getName() throws IOException {
		return CommonUtils.getDefaultEmailFromConfig(IMAPSMailbox.class);
	}

	private static String getPassword() throws IOException {
		return CommonUtils.getDefaultPasswordFromConfig(IMAPSMailbox.class);
	}
}
