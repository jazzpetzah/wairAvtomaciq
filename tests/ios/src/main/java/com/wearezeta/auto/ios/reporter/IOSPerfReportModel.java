package com.wearezeta.auto.ios.reporter;

import java.util.List;
import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.common.performance.PerfReportModel;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;

public class IOSPerfReportModel extends PerfReportModel {
	private static final String APP_LAUNCH_TIME_REGEX = "App launch time ([\\d]+)";

	private static final String LOGIN_SUCCESS_REGEX = "Login success after ([\\d]+)";

	private static final String CONTACT_LIST_LOADED_REGEX = "Contact List load after ([\\d]+)";

	private static final String CONVERSATION_PAGE_VISIBLE_REGEX = "Convesation loaded after ([\\d]+)";

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

	@Override
	protected List<Long> readLogValues(final String patternStr,
			final String output) {
		final List<Long> result = super.readLogValues(patternStr, output);
		// workaround for false entry on contact list update
		if (result.size() > 0 && result.get(0) > 1000) {
			result.remove(0);
		}
		return result;
	}

	public void loadDataFromLog(final String output) {
		this.setAppStartupTime(readLastLogValue(APP_LAUNCH_TIME_REGEX, output));
		this.setSignInTime(readLogValue(LOGIN_SUCCESS_REGEX, output)
				+ readLogValue(CONTACT_LIST_LOADED_REGEX, output));
		this.clearConvoStartupTimes();
		for (long timeMillis : readLogValues(CONVERSATION_PAGE_VISIBLE_REGEX,
				output)) {
			this.addConvoStartupTime(timeMillis);
		}
	}
}
