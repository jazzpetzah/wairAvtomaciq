package com.wearezeta.auto.sync.client.reporter;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.sync.client.WireInstance;

public class OSXInstanceReporter extends InstanceReporter {

	private static final Logger log = ZetaLogger
			.getLog(OSXInstanceReporter.class.getSimpleName());

	public OSXInstanceReporter(WireInstance owner) {
		super(owner);
	}

	@Override
	protected ClientDeviceInfo readClientDevice() {
		ClientDeviceInfo deviceInfo = new ClientDeviceInfo();
		try {
			deviceInfo = OSXCommonUtils.readDeviceInfo();
		} catch (Exception e) {
			log.error("Error while getting device information for OS X client.\n"
					+ e.getMessage());
		}
		return deviceInfo;
	}

	@Override
	protected BuildVersionInfo readBuildVersion() {
		BuildVersionInfo buildInfo = new BuildVersionInfo();
		try {
			buildInfo = OSXCommonUtils.readClientVersionFromPlist();
		} catch (Exception e) {
			log.error("Failed to read client info for OSX client.\n"
					+ e.getMessage());
		}
		return buildInfo;
	}

}
