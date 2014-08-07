package com.wearezeta.auto.android.common;

import com.wearezeta.auto.common.CommonUtils;

public class AndroidCommonUtils extends CommonUtils {
	public static void uploadPhotoToAndroid(String photoPathOnDevice)
			throws Exception {
		if (getOsName().contains(OS_NAME_WINDOWS)) {
			Runtime.getRuntime().exec(
					"cmd /C adb push " + getImagePath(CommonUtils.class) + " "
							+ photoPathOnDevice);
			Runtime.getRuntime()
					.exec("cmd /C adb -d shell \"am broadcast -a android.intent.action.MEDIA_MOUNTED -d file:///sdcard \"Broadcasting: Intent { act=android.intent.action.MEDIA_MOUNTED dat=file:///sdcard }");
		} else {
			executeOsXCommand(new String[] { "/bin/bash", "-c", "adb push",
					getImagePath(CommonUtils.class), photoPathOnDevice });
			executeOsXCommand(new String[] {
					"/bin/bash",
					"-c",
					"adb shell \"am broadcast -a android.intent.action.MEDIA_MOUNTED -d file:///sdcard \"Broadcasting: Intent { act=android.intent.action.MEDIA_MOUNTED dat=file:///sdcard }" });
		}
	}

	public static void killAndroidClient() throws Exception {
		if (getOsName().contains(OS_NAME_WINDOWS)) {
			Runtime.getRuntime().exec(
					"cmd /C adb shell am force-stop com.waz.zclient");
		} else {
			executeOsXCommand(new String[] { "/bin/bash", "-c",
					"adb shell am force-stop com.waz.zclient" });
		}
	}
}
