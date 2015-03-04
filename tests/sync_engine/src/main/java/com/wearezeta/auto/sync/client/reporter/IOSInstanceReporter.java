package com.wearezeta.auto.sync.client.reporter;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;
import com.wearezeta.auto.sync.client.WireInstance;

public class IOSInstanceReporter extends InstanceReporter {

	private static final Logger log = ZetaLogger
			.getLog(IOSInstanceReporter.class.getSimpleName());

	public IOSInstanceReporter(WireInstance owner) {
		super(owner);
	}

	@Override
	protected ClientDeviceInfo readClientDevice() {
		ClientDeviceInfo deviceInfo = new ClientDeviceInfo();
		try {
			deviceInfo = IOSCommonUtils.readDeviceInfo();
		} catch (Exception e) {
			log.error("Failed to get iOS device info. "
					+ "Seems like client were crashed during test.\n"
					+ e.getMessage());
		}
		return deviceInfo;
	}

	@Override
	protected BuildVersionInfo readBuildVersion() {
		BuildVersionInfo buildInfo = new BuildVersionInfo();
		try {
			buildInfo = IOSCommonUtils.readClientVersionFromPlist();
		} catch (Exception e) {
			log.error("Failed to get iOS client info from Info.plist.\n"
					+ e.getMessage());
		}
		return buildInfo;
	}

}
