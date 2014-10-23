package com.wearezeta.auto.android.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;

public class AndroidCommonUtils extends CommonUtils {
	private static final Logger log = ZetaLogger.getLog(AndroidCommonUtils.class.getSimpleName());
	
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
	
	public static void disableHints() throws Exception {
		if (getOsName().contains(OS_NAME_WINDOWS)) {
			Runtime.getRuntime().exec(
					"cmd /C adb shell touch /sdcard/disableOnBoardingHints");
		} else {
			executeOsXCommand(new String[] { "/bin/bash", "-c",
					"adb shell touch /sdcard/disableOnBoardingHints" });
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
	
	private static String getZMessagingBuildFromClassesDex() throws IOException {
		String zmessagingBuild = "no info";
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			File file = new File(getAndroidClientInfoPathFromConfig(AndroidCommonUtils.class));
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			String s;
			Pattern pattern = Pattern.compile("zMessaging ([0-9\\.]*)");
			Matcher matcher = null;
			while ((s = bufferedReader.readLine()) != null) {
				matcher = pattern.matcher(s);
				if (matcher.find()) {
					zmessagingBuild = matcher.group(1);
					break;
				}
			}
		} catch(Exception ex) {
			 log.error("Failed to read Android client properties.\n" + ex.getMessage());
		} finally {
			if (bufferedReader != null) bufferedReader.close();
			if (fileReader != null) fileReader.close();
		}
		return zmessagingBuild;
	}
	
	public static String getPropertyFromAdb(String propertyName) throws IOException, InterruptedException {
		String result = "no info";

		String adbCommand = String.format("adb shell getprop %s", propertyName);
		Process process = null;
		if (getOsName().contains(OS_NAME_WINDOWS)) {
			process = Runtime.getRuntime().exec("cmd /C " + adbCommand);
		} else {
			process = Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", adbCommand });
		}
		
		if (process != null) {
			InputStream stream = null;
			InputStreamReader isReader = null;
			BufferedReader bufferedReader = null;
			try {
				stream = process.getInputStream();
				isReader = new InputStreamReader(stream);
				bufferedReader = new BufferedReader(isReader);
				String s;
			
				while (( s = bufferedReader.readLine() ) != null ) {
					result = s;
				}
				stream.close();
				outputErrorStreamToLog(process.getErrorStream());
				log.debug("Request for property " + propertyName + " via adb exited with code " + process.waitFor());
			} catch (IOException e) {
				log.error(e.getMessage());
			} finally {
				if (bufferedReader != null) bufferedReader.close();
				if (isReader != null) isReader.close();
				if (stream != null) stream.close();
			}
		}
		return result;
	}
	
	private static String readClientVersionFromAdb() throws IOException, InterruptedException {
		String clientBuild = "no info";
		String adbCommand = String.format("adb shell dumpsys package %s | grep versionName", CommonUtils.getAndroidPackageFromConfig(AndroidLocators.class));
		Process process = null;
		if (getOsName().contains(OS_NAME_WINDOWS)) {
			process = Runtime.getRuntime().exec("cmd /C " + adbCommand);
		} else {
			process = Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", adbCommand });
		}
		
		if (process != null) {
			InputStream stream = null;
			InputStreamReader isReader = null;
			BufferedReader bufferedReader = null;
			try {
				stream = process.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(stream));
				String possibleVersionName;
			
				while (( possibleVersionName = br.readLine() ) != null ) {
					String[] versionArray = possibleVersionName.trim().split("=");
					if (versionArray.length == 2) {
						clientBuild = versionArray[1];
					}
				}
				outputErrorStreamToLog(process.getErrorStream());
				log.debug("Request for package version finished with code " + process.waitFor());
			} catch (IOException e) {
				log.error(e.getMessage());
			} finally {
				if (bufferedReader != null) bufferedReader.close();
				if (isReader != null) isReader.close();
				if (stream != null) stream.close();
			}
		}
		return clientBuild;
	}
	
	public static BuildVersionInfo readClientVersion() throws IOException, InterruptedException {
		String clientBuild = readClientVersionFromAdb();
		String zmessagingBuild = getZMessagingBuildFromClassesDex();
		
		return new BuildVersionInfo(clientBuild, zmessagingBuild);
	}
	
	public static ClientDeviceInfo readDeviceInfo() throws IOException, InterruptedException {
		String os = "Android";
		String osBuild = getPropertyFromAdb("ro.build.version.release");
		String deviceName = getPropertyFromAdb("ro.product.manufacturer") + " " + getPropertyFromAdb("ro.product.model");
		String gsmNetworkType = getPropertyFromAdb("gsm.network.type");
		String isWifiEnabled = "no info";
		
		return new ClientDeviceInfo(os, osBuild, deviceName, gsmNetworkType, isWifiEnabled);
	}
	
	public static String getAndroidClientInfoPathFromConfig(Class<?> c)
			throws IOException {
		return CommonUtils.getValueFromConfig(c, "androidClientInfoPath");
	}
	
	public static String getAndroidAppiumLogPathFromConfig(Class<?> c)
			throws IOException {
		return CommonUtils.getValueFromConfig(c, "androidAppiumLogPath");
	}
}
