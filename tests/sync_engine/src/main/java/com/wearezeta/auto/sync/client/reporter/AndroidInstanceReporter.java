package com.wearezeta.auto.sync.client.reporter;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.sync.client.WireInstance;

public class AndroidInstanceReporter extends InstanceReporter {

	private static final Logger log = ZetaLogger
			.getLog(AndroidInstanceReporter.class.getSimpleName());

	public AndroidInstanceReporter(WireInstance owner) {
		super(owner);
	}

	@Override
	protected ClientDeviceInfo readClientDevice() {
		ClientDeviceInfo deviceInfo = new ClientDeviceInfo();
		try {
			deviceInfo = AndroidCommonUtils.readDeviceInfo();
		} catch (InterruptedException iex) {
			log.error(iex.getMessage());
		} catch (IOException ioex) {
			log.error(ioex.getMessage());
		}
		return deviceInfo;
	}

	@Override
	protected BuildVersionInfo readBuildVersion() {
		BuildVersionInfo buildInfo = new BuildVersionInfo();
		try {
			buildInfo = AndroidCommonUtils.readClientVersion();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return buildInfo;
	}

}
