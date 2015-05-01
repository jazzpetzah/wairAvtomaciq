package com.wearezeta.auto.sync.report.data;

import com.wearezeta.auto.common.Platform;

public class MessageReport {
	public String message;
	public Platform sentFrom;
	public boolean isOsxReceiveTimeOK;
	public String osxReceiveTime;
	public boolean isIosReceiveTimeOK;
	public String iosReceiveTime;
	public boolean isAndroidReceiveTimeOK;
	public String androidReceiveTime;
	public boolean checkTime = true;
}
