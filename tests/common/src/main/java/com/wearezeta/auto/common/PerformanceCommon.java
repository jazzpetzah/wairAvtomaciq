package com.wearezeta.auto.common;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.backend.BackendRequestException;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.SelfUserIsNotDefinedException;

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

	public static final int SEND_MESSAGE_NUM = 4;
	private static final int BACKEND_MESSAGE_COUNT = 5;
	private static final int MIN_WAIT_SECONDS = 5;
	private static final int MAX_WAIT_SECONDS = 30;
	public static final int SIMULTANEOUS_MSGS_COUNT = 5;

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

	public Random random = new Random();

	private String getRandomContactName(ClientUser selfUser)
			throws SelfUserIsNotDefinedException {
		List<ClientUser> otherUsers = getUserManager().getCreatedUsers();
		otherUsers.remove(selfUser);
		return otherUsers.get(random.nextInt(otherUsers.size())).getName();
	}

	private void generateIncomingMessages() throws Exception {
		sendRandomMessagesToUser(BACKEND_MESSAGE_COUNT, SIMULTANEOUS_MSGS_COUNT);
		try {
			sendDefaultImageToUser(BACKEND_MESSAGE_COUNT / 5);
		} catch (BackendRequestException e) {
			e.printStackTrace();
			getLogger().debug(e.getMessage());
		}
	}

	public static interface PerformanceLoop {
		public void run() throws Exception;
	}

	public void runPerformanceLoop(PerformanceLoop loop,
			final int timeoutMinutes) throws Exception {
		LocalDateTime startDateTime = LocalDateTime.now();
		long minutesElapsed = 0;
		do {
			generateIncomingMessages();

			loop.run();

			final long sleepDurationSeconds = (MIN_WAIT_SECONDS + random
					.nextInt(MAX_WAIT_SECONDS - MIN_WAIT_SECONDS + 1));
			getLogger().debug(
					String.format("Sleeping %s seconds", sleepDurationSeconds));
			Thread.sleep(sleepDurationSeconds * 1000);
			minutesElapsed = java.time.Duration.between(startDateTime,
					LocalDateTime.now()).toMinutes();
			getLogger()
					.debug(String
							.format("Approximately %s minute(s) left till the end of the perf test",
									timeoutMinutes - minutesElapsed));
		} while (minutesElapsed <= timeoutMinutes);
	}

	private void sendRandomMessagesToUser(int totalMsgsCount,
			int simultaneousMsgsCount) throws SelfUserIsNotDefinedException,
			InterruptedException {
		final ClientUser selfUser = getUserManager().getSelfUserOrThrowError();
		ExecutorService executor = Executors
				.newFixedThreadPool(simultaneousMsgsCount);
		for (int i = 0; i < totalMsgsCount; i++) {
			final String contactName = getRandomContactName(selfUser);
			Runnable worker = new Thread(new Runnable() {
				public void run() {
					try {
						BackendAPIWrappers.sendDialogMessageByChatName(
								selfUser, contactName,
								CommonUtils.generateGUID());
					} catch (Exception e) {
						e.printStackTrace();
						getLogger().debug(e.getMessage());
					}
				}
			});
			executor.submit(worker);
		}
		executor.shutdown();
		executor.awaitTermination(900, TimeUnit.SECONDS);
	}

	private void sendDefaultImageToUser(int imagesCount) throws Exception {
		final ClientUser selfUser = getUserManager().getSelfUserOrThrowError();
		final ClassLoader classLoader = this.getClass().getClassLoader();
		final InputStream defaultImage = classLoader
				.getResourceAsStream(DEFAULT_PERF_IMAGE);
		try {
			for (int i = 0; i < imagesCount; i++) {
				final String contact = getRandomContactName(selfUser);
				BackendAPIWrappers.sendPictureToChatByName(selfUser, contact,
						defaultImage);
			}
		} finally {
			if (defaultImage != null) {
				defaultImage.close();
			}
		}
	}
}
