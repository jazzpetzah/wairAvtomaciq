package com.wearezeta.auto.ios.reporter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.common.performance.PerfReportModel;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;

public class IOSPerfReportModel extends PerfReportModel {
	private static final String APP_LAUNCH_TIME_REGEX = "App launch time ([\\d]+)";

	private static final String LOGIN_SUCCESS_REGEX = "Login success after ([\\d]+)";

	// FIXME: replace with message without mistakes after developers will fix it
	private static final String CONVERSATION_PAGE_VISIBLE_REGEX = "Conver?sation loaded afth?er ([\\d]+)";

	private static final Logger log = ZetaLogger
			.getLog(IOSPerfReportModel.class.getSimpleName());

	public IOSPerfReportModel() {
		try {
			this.setBuildNumber(IOSCommonUtils.readClientVersionFromPlist()
					.getClientBuildNumber());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}

		try {
			final ClientDeviceInfo deviceInfo = IOSCommonUtils.readDeviceInfo();
			loadValuesFromDeviceInfo(deviceInfo);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	private static List<Long> readLogValues(final String patternStr,
			final String output) {
		final Pattern pattern = Pattern.compile(patternStr);
		final Matcher matcher = pattern.matcher(output);
		final List<Long> result = new ArrayList<>();
		int i = 1;
		while (matcher.find()) {
			try {
				if (i++ % 2 != 0)
					result.add(Long.parseLong(matcher.group(1)));
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
				return Long.parseLong(matcher.group(1));
			} catch (NumberFormatException e) {
				log.error(e);
			}
		}
		return 0;
	}

	public void loadDataFromLog(final String output) {
		this.setAppStartupTime(readLogValue(APP_LAUNCH_TIME_REGEX, output));
		this.setSignInTime(readLogValue(LOGIN_SUCCESS_REGEX, output));
		this.clearConvoStartupTimes();
		for (long timeMillis : readLogValues(CONVERSATION_PAGE_VISIBLE_REGEX,
				output)) {
			this.addConvoStartupTime(timeMillis);
		}
	}
}
