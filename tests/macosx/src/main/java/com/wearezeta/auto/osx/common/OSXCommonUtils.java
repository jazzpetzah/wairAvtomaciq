package com.wearezeta.auto.osx.common;

import com.wearezeta.auto.common.CommonUtils;

public class OSXCommonUtils extends CommonUtils {
	public static void removeAllZClientSettingsFromDefaults() throws Exception {
		String command = "defaults delete com.wearezeta.zclient.mac";

		if (getOsName().contains(OS_NAME_WINDOWS)) {
			Runtime.getRuntime().exec("cmd /C " + command);
		} else {
			executeOsXCommand(new String[] { "/bin/bash", "-c", command });
		}
	}
}
