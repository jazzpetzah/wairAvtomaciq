package com.wearezeta.auto.common.email;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.mail.*;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

public class IMAPSMailbox implements ISupportsMessagesPolling {
	private static final String MAIL_PROTOCOL = "imaps";
	private static final String MAILS_FOLDER = "Inbox";
	private static final int MBOX_MAX_CONNECT_RETRIES = 10;
	private static final int NEW_MSG_CHECK_INTERVAL = 500; // milliseconds

	private static final Logger log = ZetaLogger.getLog(IMAPSMailbox.class
			.getSimpleName());

	public List<String> getRecentMessages(int msgsCount)
			throws Exception {
				return null;
	
	}

	public Future<String> getMessage(Map<String, String> expectedHeaders,
			int timeoutSeconds) throws MessagingException, InterruptedException {
		return getMessage(expectedHeaders, timeoutSeconds, 0);
	}

	public Future<String> getMessage(Map<String, String> expectedHeaders,
			int timeoutSeconds, long rejectMessagesBeforeTimestamp)
			throws MessagingException, InterruptedException {
				return null;
		
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
	}

	public static String getServerName() throws Exception {
		return CommonUtils.getDefaultEmailServerFromConfig(IMAPSMailbox.class);
	}

	public static String getName() throws Exception {
		return CommonUtils.getDefaultEmailFromConfig(IMAPSMailbox.class);
	}
}
