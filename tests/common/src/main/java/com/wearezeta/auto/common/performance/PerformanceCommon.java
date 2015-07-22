package com.wearezeta.auto.common.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

public final class PerformanceCommon {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final Logger logger = ZetaLogger.getLog(PerformanceCommon.class
			.getSimpleName());

	public Logger getLogger() {
		return this.logger;
	}

	public ClientUsersManager getUserManager() {
		return this.usrMgr;
	}

	private static final int MIN_WAIT_SECONDS = 5;
	private static final int MAX_WAIT_SECONDS = 10;

	public static final String DEFAULT_PERF_IMAGE = "perf/default.jpg";

	private static PerformanceCommon instance = null;

	private PerformanceCommon() {
	}

	public synchronized static PerformanceCommon getInstance() {
		if (instance == null) {
			instance = new PerformanceCommon();
		}
		return instance;
	}

	public void sendMultipleMessagesIntoConversation(String convoName,
			int msgsCount) throws Exception {
		final String convo_id = BackendAPIWrappers.getConversationIdByName(
				usrMgr.getSelfUserOrThrowError(), convoName);
		final List<String> msgsToSend = new ArrayList<>();
		for (int i = 0; i < msgsCount; i++) {
			msgsToSend.add(CommonUtils.generateGUID());
		}
		BackendAPIWrappers.sendConversationMessages(
				usrMgr.findUserByNameOrNameAlias(convoName), convo_id,
				msgsToSend);
	}

	public static interface PerformanceLoop {
		public void run() throws Exception;
	}

	private static final Random rand = new Random();

	public void runPerformanceLoop(PerformanceLoop loop,
			final int timeoutMinutes) throws Exception {
		final long millisecondsStarted = System.currentTimeMillis();
		do {
			loop.run();

			final int sleepDurationSeconds = (MIN_WAIT_SECONDS + rand
					.nextInt(MAX_WAIT_SECONDS - MIN_WAIT_SECONDS + 1));
			getLogger().debug(
					String.format("Sleeping %s seconds", sleepDurationSeconds));
			Thread.sleep(sleepDurationSeconds * 1000);
			getLogger()
					.debug(String
							.format("Approximately %s second(s) left till the end of the perf test",
									timeoutMinutes
											* 60
											- (System.currentTimeMillis() - millisecondsStarted)
											/ 1000));
		} while (System.currentTimeMillis() - millisecondsStarted < timeoutMinutes * 60000);
	}

	// private void sendDefaultImageToUser(int imagesCount) throws Exception {
	// final ClientUser selfUser = getUserManager().getSelfUserOrThrowError();
	// final ClassLoader classLoader = this.getClass().getClassLoader();
	// final InputStream defaultImage = classLoader
	// .getResourceAsStream(DEFAULT_PERF_IMAGE);
	// try {
	// for (int i = 0; i < imagesCount; i++) {
	// BackendAPIWrappers.sendPictureToChatById(selfUser,
	// getRandomConversationId(selfUser), defaultImage);
	// }
	// } finally {
	// if (defaultImage != null) {
	// defaultImage.close();
	// }
	// }
	// }
}
