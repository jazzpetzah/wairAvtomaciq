package com.wearezeta.auto.sync.report.data;

import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;

public class UserReport {
	public String name;
	public Platform loggedOnPlatform;
	public String startupTime;
	public BuildVersionInfo buildVersion;
	public ClientDeviceInfo deviceData;
	public boolean isEnabled;
}