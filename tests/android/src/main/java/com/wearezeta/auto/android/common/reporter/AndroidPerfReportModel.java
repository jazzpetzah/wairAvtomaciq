package com.wearezeta.auto.android.common.reporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.common.performance.PerfReportModel;

public class AndroidPerfReportModel extends PerfReportModel {
	private static final String APP_LAUNCH_TIME_REGEX = "App launch time ([\\d]*)";

	private static final String LOGIN_SUCCESS_REGEX = "Login success after ([\\d]*)";

	private static final String CONVERSATION_PAGE_VISIBLE_REGEX = "Conversation page visible after ([\\d]*)";

	private static final Logger log = ZetaLogger
			.getLog(AndroidPerfReportModel.class.getSimpleName());

	public AndroidPerfReportModel() {
		try {
			this.setBuildNumber(AndroidCommonUtils.readClientVersionFromAdb());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}

		try {
			final ClientDeviceInfo deviceInfo = AndroidCommonUtils
					.readDeviceInfo();
			// FIXME: handle other network types
			this.setNetworkType(deviceInfo.isWifiEnabled() ? NetworkType.WiFi
					: NetworkType.FourG);
			this.setDeviceName(deviceInfo.getDeviceName());
			this.setDeviceOSVersion(deviceInfo.getOperatingSystemBuild());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	private static long nanosecondsToMilliseconds(long nano) {
		return nano / 1000000L;
	}

	private static List<Long> readLogValues(final String patternStr,
			final String output) {
		final Pattern pattern = Pattern.compile(patternStr);
		final Matcher matcher = pattern.matcher(output);
		final List<Long> result = new ArrayList<>();
		while (matcher.find()) {
			try {
				result.add(nanosecondsToMilliseconds(Long.parseLong(matcher
						.group(1))));
			} catch (NumberFormatException e) {
				log.error(e);
			}
		}
		return result;
	}

	private static long readLogValue(final String patternStr,
			final String output) {
		final Pattern pattern = Pattern.compile(patternStr);
		final Matcher matcher = pattern.matcher(output);
		while (matcher.find()) {
			try {
				return nanosecondsToMilliseconds(Long.parseLong(matcher
						.group(1)));
			} catch (NumberFormatException e) {
				log.error(e);
			}
		}
		return 0;
	}

	public void loadDataFromLogCat(final String output) {
		// TODO: Remove experimental values
		// this.setAppStartupTimeMillis(readLogValue(APP_LAUNCH_TIME_REGEX,
		// output));
		// this.setSignInTime(readLogValue(LOGIN_SUCCESS_REGEX, output));
		// this.clearConvoStartupTimes();
		// for (long timeMillis : readLogValues(CONVERSATION_PAGE_VISIBLE_REGEX,
		// output)) {
		// this.addConvoStartupTime(timeMillis);
		// }
		final Random rand = new Random();
		this.setAppStartupTimeMillis(10000 + rand.nextInt(30000));
		this.setSignInTime(20000 + rand.nextInt(30000));
		this.clearConvoStartupTimes();
		this.addConvoStartupTime(20000);
		this.addConvoStartupTime(1000);
		this.addConvoStartupTime(1100);
		this.addConvoStartupTime(1300);
		this.addConvoStartupTime(1400);
		this.addConvoStartupTime(1200);
	}
}
