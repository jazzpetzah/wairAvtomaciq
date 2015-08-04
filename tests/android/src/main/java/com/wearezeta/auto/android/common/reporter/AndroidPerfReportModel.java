package com.wearezeta.auto.android.common.reporter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.common.performance.PerfReportModel;

public class AndroidPerfReportModel extends PerfReportModel {
	private static final String APP_LAUNCH_TIME_REGEX = "App launch time ([\\d]+)";

	private static final String LOGIN_STARTED_REGEX = "Login pressed at ([\\d]+)";

	private static final String LOGIN_COMPLETED_REGEX = "Login completed at ([\\d]+)";

	private static final String CONVERSATION_PAGE_VISIBLE_REGEX = "Conversation page visible after ([\\d]+)";

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
			loadValuesFromDeviceInfo(deviceInfo);
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
		this.setAppStartupTime(readLogValue(APP_LAUNCH_TIME_REGEX, output));
		List<Long> signInStartedTimestamps = readLogValues(LOGIN_STARTED_REGEX,
				output);
		List<Long> signInCompletedTimestamps = readLogValues(
				LOGIN_COMPLETED_REGEX, output);
		// The last recorded value contains what we need
		this.setSignInTime(signInCompletedTimestamps
				.get(signInCompletedTimestamps.size() - 1)
				- signInStartedTimestamps.get(0));
		this.clearConvoStartupTimes();
		for (long timeMillis : readLogValues(CONVERSATION_PAGE_VISIBLE_REGEX,
				output)) {
			this.addConvoStartupTime(timeMillis);
		}
	}
}
