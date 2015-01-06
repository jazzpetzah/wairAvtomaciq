package com.wearezeta.auto.common;

import java.io.FileInputStream;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.user_management.ClientUser;
import com.wearezeta.auto.common.user_management.ClientUsersManager;
import com.wearezeta.auto.common.user_management.ClientUsersManager.SelfUserIsNotDefinedException;

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
	public static final int BACK_END_MESSAGE_COUNT = 5;
	public static final int MIN_WAIT_VALUE_IN_MIN = 1;
	public static final int MAX_WAIT_VALUE_IN_MIN = 2;
	public static final int SIMULTANEOUS_MSGS_COUNT = 5;

	private static PerformanceCommon instance = null;

	private PerformanceCommon() {
	}

	public static PerformanceCommon getInstance() {
		if (instance == null) {
			instance = new PerformanceCommon();
		}
		return instance;
	}

	private Random random = new Random();

	private String getRandomContactName(ClientUser selfUser)
			throws SelfUserIsNotDefinedException {
		List<ClientUser> otherUsers = getUserManager().getCreatedUsers();
		otherUsers.remove(selfUser);
		return otherUsers.get(random.nextInt(otherUsers.size())).getName();
	}

	public void sendRandomMessagesToUser(int totalMsgsCount,
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
						getLogger().debug(e.getMessage());
					}
				}
			});
			executor.submit(worker);
		}
		executor.shutdown();
		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
	}

	public void sendDefaultImageToUser(int imagesCount) throws Exception {
		FileInputStream configFileStream = null;
		try {
			configFileStream = new FileInputStream(
					CommonUtils.getImagePath(PerformanceCommon.class));
			final ClientUser selfUser = getUserManager()
					.getSelfUserOrThrowError();
			for (int i = 0; i < imagesCount; i++) {
				final String contact = getRandomContactName(selfUser);
				BackendAPIWrappers.sendPictureToChatByName(selfUser, contact,
						"default", configFileStream);
			}
		} finally {
			if (configFileStream != null) {
				configFileStream.close();
			}
		}
	}
}
