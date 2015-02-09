package com.wearezeta.auto.sync;

import com.wearezeta.auto.common.CommonUtils;

public class SyncEngineUtil {
	public static final int MAX_PARALLEL_USER_CREATION_TASKS = 3;

	public static final int USERS_CREATION_TIMEOUT = 60 * 5; // seconds

	public static final String CHAT_NAME = "SyncEngineTest";
	
	public static final String OSX_NAME_ALIAS = "user1Name";
	public static final String ANDROID_NAME_ALIAS = "user2Name";
	public static final String IOS_NAME_ALIAS = "user3Name";
	public static final String PASSWORD_ALIAS = "user1Password";
	
	public static final String[] platforms = new String[] {
		CommonUtils.PLATFORM_NAME_OSX,
		CommonUtils.PLATFORM_NAME_ANDROID,
		CommonUtils.PLATFORM_NAME_IOS
	};

	public static boolean getCommonGenerateUsersFromConfig(Class<?> c)
			throws Exception {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c, "common.generate.users"));
	}

	public static boolean getOSXClientEnabledFromConfig(Class<?> c)
			throws Exception {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c, "osx.client.enabled"));
	}

	public static boolean getAndroidClientEnabledFromConfig(Class<?> c)
			throws Exception {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c, "android.client.enabled"));
	}

	public static boolean getIosClientEnabledFromConfig(Class<?> c)
			throws Exception {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c, "ios.client.enabled"));
	}
	
	public static boolean getOSXBackendSenderFromConfig(Class<?> c)
			throws Exception {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c, "osx.backend.sender"));
	}

	public static boolean getAndroidBackendSenderFromConfig(Class<?> c)
			throws Exception {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c, "android.backend.sender"));
	}

	public static boolean getIosBackendSenderFromConfig(Class<?> c)
			throws Exception {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c, "ios.backend.sender"));
	}
	
	public static String getAcceptanceReportPathFromConfig(Class<?> c)
			throws Exception {
		return CommonUtils.getValueFromConfig(c, "acceptance.report.path");
	}

	public static int getAcceptanceMaxSendingIntervalFromConfig(Class<?> c)
			throws NumberFormatException, Exception {
		return Integer.parseInt(CommonUtils.getValueFromConfig(c, "acceptance.max.send.interval.sec"));
	}
	
	public static int getClientMessagesCount(Class<?> c)
			throws NumberFormatException, Exception {
		return Integer.parseInt(CommonUtils.getValueFromConfig(c, "acceptance.messages.count"));
	}
	
	public static boolean isPlatformCorrect(String platform) {
		return platform.equals(CommonUtils.PLATFORM_NAME_ANDROID) ||
				platform.equals(CommonUtils.PLATFORM_NAME_IOS) ||
				platform.equals(CommonUtils.PLATFORM_NAME_OSX);
	}
}
