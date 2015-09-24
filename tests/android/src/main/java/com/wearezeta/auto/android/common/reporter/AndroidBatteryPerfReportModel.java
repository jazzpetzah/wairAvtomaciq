package com.wearezeta.auto.android.common.reporter;

import org.apache.log4j.Logger;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.common.performance.BatteryPerfReportModel;

public class AndroidBatteryPerfReportModel extends BatteryPerfReportModel {
	private static final Logger log = ZetaLogger
			.getLog(AndroidBatteryPerfReportModel.class.getSimpleName());

	public AndroidBatteryPerfReportModel() {
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
}
