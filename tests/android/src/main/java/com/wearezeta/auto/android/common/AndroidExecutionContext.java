package com.wearezeta.auto.android.common;

import com.wearezeta.auto.common.CommonUtils;

public final class AndroidExecutionContext {
	public static int getOSVersion() throws Exception {
		return Integer.parseInt(CommonUtils.getValueFromConfig(
				AndroidExecutionContext.class, "androidVersion"));
	}

	public static boolean isLocationByIdSupported() throws Exception {
		return getOSVersion() > 42;
	}
}
