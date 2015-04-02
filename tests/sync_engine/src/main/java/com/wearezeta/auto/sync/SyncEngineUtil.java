package com.wearezeta.auto.sync;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.log.ZetaLogger;

public class SyncEngineUtil {

	private static Logger log = ZetaLogger.getLog(SyncEngineUtil.class
			.getSimpleName());

	public static final int MAX_PARALLEL_USER_CREATION_TASKS = 3;

	public static final int USERS_CREATION_TIMEOUT = 60 * 5; // seconds

	public String PASSWORD_ALIAS = "user1Password";

	public static final Platform[] platforms = new Platform[] { Platform.Mac,
			Platform.Android, Platform.iOS };

	public static long readDateFromAppiumLog(String logFile) throws IOException {
		long result = -1;

		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			File f = new File(logFile);
			fileReader = new FileReader(f);
			bufferedReader = new BufferedReader(fileReader);
			String s;
			Pattern pattern = Pattern
					.compile(SEConstants.Common.LAUNCH_TIME_RECORD_IN_APPIUM_LOG);
			Matcher matcher;
			while ((s = bufferedReader.readLine()) != null) {
				matcher = pattern.matcher(s);
				if (matcher.find()) {
					result = Long.parseLong(matcher.group(1));
					break;
				}
			}
		} catch (IOException e) {
			log.error("Failed to read appium.log for launch time.\n"
					+ e.getMessage());
		} finally {
			if (bufferedReader != null)
				bufferedReader.close();
			if (fileReader != null)
				fileReader.close();
		}
		return result;
	}

	public static String prepareCheckStatus(boolean check) {
		return (check ? "passed" : "failed");
	}

	public static boolean getCommonGenerateUsersFromConfig(Class<?> c)
			throws Exception {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c,
				"common.generate.users"));
	}

	public static boolean getOSXClientEnabledFromConfig(Class<?> c)
			throws Exception {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c,
				"osx.client.enabled"));
	}

	public static boolean getAndroidClientEnabledFromConfig(Class<?> c)
			throws Exception {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c,
				"android.client.enabled"));
	}

	public static boolean getIosClientEnabledFromConfig(Class<?> c)
			throws Exception {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c,
				"ios.client.enabled"));
	}

	public static boolean getOSXBackendSenderFromConfig(Class<?> c)
			throws Exception {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c,
				"osx.backend.sender"));
	}

	public static boolean getAndroidBackendSenderFromConfig(Class<?> c)
			throws Exception {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c,
				"android.backend.sender"));
	}

	public static boolean getIosBackendSenderFromConfig(Class<?> c)
			throws Exception {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c,
				"ios.backend.sender"));
	}

	public static String getAcceptanceReportPathFromConfig(Class<?> c)
			throws Exception {
		return CommonUtils.getValueFromConfig(c, "acceptance.report.path");
	}

	public static int getAcceptanceMaxSendingIntervalFromConfig(Class<?> c)
			throws NumberFormatException, Exception {
		return Integer.parseInt(CommonUtils.getValueFromConfig(c,
				"acceptance.max.send.interval.sec"));
	}

	public static int getClientMessagesCount(Class<?> c)
			throws NumberFormatException, Exception {
		return Integer.parseInt(CommonUtils.getValueFromConfig(c,
				"acceptance.messages.count"));
	}

	public static boolean isPlatformCorrect(Platform platform) {
		return platform == Platform.Android || platform == Platform.iOS
				|| platform == Platform.Mac;
	}
}
