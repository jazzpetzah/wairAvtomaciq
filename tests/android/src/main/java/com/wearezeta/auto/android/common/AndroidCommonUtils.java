package com.wearezeta.auto.android.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;

public class AndroidCommonUtils extends CommonUtils {
	private static final Logger log = ZetaLogger
			.getLog(AndroidCommonUtils.class.getSimpleName());

	private static final String stagingBackend = "[\"https://staging-nginz-https.zinfra.io\", \"https://staging-nginz-ssl.zinfra.io/await\", \"1003090516085\"]";
	private static final String edgeBackend = "[\"https://edge-nginz-https.zinfra.io\", \"https://edge-nginz-ssl.zinfra.io/await\", \"1003090516085\"]";
	private static final String productionBackend = "[\"https://prod-nginz-https.wire.com\", \"https://prod-nginz-ssl.wire.com/await\", \"782078216207\"]";

	private static final String BACKEND_JSON = "customBackend.json";
	private static final String BACKEND_FILE_LOCATION = "/mnt/sdcard/customBackend.json";

	public static final String ADB_PREFIX = "";
//	public static final String ADB_PREFIX = "/Applications/android-sdk/platform-tools/";

	private static ArrayList<String> addressBookAddedNames = new ArrayList<String>();

	private static void executeAdb(final String cmdline) throws Exception {
		executeOsXCommand(new String[] { "/bin/bash", "-c",
				ADB_PREFIX + "adb " + cmdline });
	}

	public static void uploadPhotoToAndroid(String photoPathOnDevice)
			throws Exception {
		executeAdb(String.format("push %s %s", getImagePath(CommonUtils.class),
				photoPathOnDevice));
		executeAdb("shell \"am broadcast -a android.intent.action.MEDIA_MOUNTED -d "
				+ "file:///sdcard \"Broadcasting: Intent { act=android.intent.action.MEDIA_MOUNTED dat=file:///sdcard }");
	}

	public static void openGalleryApplication() throws Exception {
		executeAdb("shell pm clear com.google.android.gallery3d");
		executeAdb("shell am start -t image/* -a android.intent.action.VIEW");
	}

	public static void unlockScreen() throws Exception {
		executeAdb("shell am start -n io.appium.unlock/.Unlock");
	}

	public static void genericScreenTap(int x, int y) throws Exception {
		executeAdb(String.format("shell input tap %d %d", x, y));
	}

	public static void openBroswerApplication() throws Exception {
		executeAdb("shell am start -a android.intent.action.VIEW -d http://www.google.com");
	}

	public static void copyFileFromAndroid(String filePathOnSystem,
			String filePathOnDevice) throws Exception {
		executeAdb(String.format("pull %s %s", filePathOnDevice,
				filePathOnSystem));
	}

	public static void disableHints() throws Exception {
		executeAdb("shell touch /sdcard/disableOnBoardingHints");
	}

	public static void disableHockeyUpdates() throws Exception {
		executeAdb("shell touch /sdcard/disableHockeyUpdates");
	}

	private static String getAdbOutput(String cmdLine) throws Exception {
		String result = "no info";

		String adbCommand = ADB_PREFIX + "adb " + cmdLine;
		final Process process = Runtime.getRuntime().exec(
				new String[] { "/bin/bash", "-c", adbCommand });
		if (process == null) {
			throw new RuntimeException(String.format(
					"Failed to execute command line '%s'", cmdLine));
		}
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String s;
			while ((s = in.readLine()) != null) {
				result = s + "\n";
			}
			outputErrorStreamToLog(process.getErrorStream());
		} finally {
			if (in != null) {
				in.close();
			}
		}

		return result.trim();
	}

	private static String getZMessagingBuildFromClassesDex() throws IOException {
		String zmessagingBuild = "no info";
		BufferedReader bufferedReader = null;
		try {
			File file = new File(
					getAndroidClientInfoPathFromConfig(AndroidCommonUtils.class));
			bufferedReader = new BufferedReader(new FileReader(file));
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
		} catch (Exception ex) {
			log.error("Failed to read Android client properties.\n"
					+ ex.getMessage());
		} finally {
			if (bufferedReader != null)
				bufferedReader.close();
		}
		return zmessagingBuild;
	}

	private static String getPropertyFromAdb(String propertyName)
			throws Exception {
		return getAdbOutput(String.format("shell getprop %s", propertyName));
	}

	public static String readClientVersionFromAdb() throws Exception {
		final String output = getAdbOutput(String.format(
				"shell dumpsys package %s | grep versionName", CommonUtils
						.getAndroidPackageFromConfig(AndroidCommonUtils.class)));
		if (output.contains("=")) {
			return output.substring(output.indexOf("="), output.length());
		} else {
			return output;
		}
	}

	public static BuildVersionInfo readClientVersion() throws Exception {
		String clientBuild = readClientVersionFromAdb();
		String zmessagingBuild = getZMessagingBuildFromClassesDex();

		return new BuildVersionInfo(clientBuild, zmessagingBuild);
	}

	private static String capitalizeManufacturerName(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	private static boolean isWifiEnabled() throws Exception {
		final String output = getAdbOutput("adb shell dumpsys wifi");

		if (output.contains("Wi-Fi is disabled")) {
			return false;
		} else if (output.contains("Wi-Fi is enabled")) {
			final Pattern pattern = Pattern
					.compile("mNetworkInfo (NetworkInfo: |\\[)?type: [^,]*, state: ([^,]*),");
			final Matcher matcher = pattern.matcher(output);
			String state = "no info";
			while (matcher.find()) {
				state = matcher.group(2);
				log.debug("Retrieved wifi state: " + state);
			}
			if (state.contains("DISCONNECTED") || state.contains("SCANNING")) {
				return false;
			} else if (state.startsWith("CONNECTED")) {
				return true;
			}
		}
		return false;
	}

	public static ClientDeviceInfo readDeviceInfo() throws Exception {
		String os = "Android";
		String osBuild = getPropertyFromAdb("ro.build.version.release");
		String deviceName = capitalizeManufacturerName(getPropertyFromAdb("ro.product.manufacturer"))
				+ " " + getPropertyFromAdb("ro.product.model");
		String gsmNetworkType = getPropertyFromAdb("gsm.network.type");
		Boolean isWifiEnabled = isWifiEnabled();

		return new ClientDeviceInfo(os, osBuild, deviceName, gsmNetworkType,
				isWifiEnabled);
	}

	public static String getRxLogResourceFilePathFromConfig(Class<?> c)
			throws Exception {
		return CommonUtils.getValueFromConfig(c, "resourceFilePath");
	}

	public static String getRxLogResultsPathFromConfig(Class<?> c)
			throws Exception {
		return CommonUtils.getValueFromConfig(c, "resultsPath");
	}

	public static String getAndroidClientInfoPathFromConfig(Class<?> c)
			throws Exception {
		return CommonUtils.getValueFromConfig(c, "androidClientInfoPath");
	}

	public static String getAndroidAppiumLogPathFromConfig(Class<?> c)
			throws Exception {
		return CommonUtils.getValueFromConfig(c, "androidAppiumLogPath");
	}

	public static String getAndroidAddressBookMailAccountFromConfig(Class<?> c)
			throws Exception {
		return CommonUtils.getValueFromConfig(c,
				"androidAddressBookMailAccount");
	}

	public static void deployBackendFile(String fileName) throws Exception {
		executeAdb(String.format("push %s %s", fileName, BACKEND_FILE_LOCATION));
	}

	public static String createBackendJSON(String bt)
			throws FileNotFoundException, UnsupportedEncodingException {
		File file = new File(BACKEND_JSON);
		if (file.exists()) {
			FileUtils.deleteQuietly(file);
		}
		PrintWriter writer = new PrintWriter(file);

		switch (bt) {
		case "edge":
			writer.println(edgeBackend);
			break;
		case "production":
			writer.println(productionBackend);
			break;
		case "staging":
			writer.println(stagingBackend);
			break;
		}

		writer.close();
		return file.getAbsolutePath();
	}

	/**
	 * http://android.stackexchange.com/questions/30157/how-to-return-to-the-
	 * home-screen-with-a-terminal-command
	 * 
	 * @throws Exception
	 */
	public static void switchToHomeScreen() throws Exception {
		executeAdb("shell am start -a android.intent.action.MAIN -c android.intent.category.HOME");
	}

	/**
	 * http://stackoverflow.com/questions/4567904/how-to-start-an-application-
	 * using-android-adb-tools
	 * 
	 * @param packageId
	 * @param mainActivity
	 * @throws Exception
	 */
	public static void switchToApplication(String packageId, String mainActivity)
			throws Exception {
		executeAdb(String.format("shell am start -n %s/%s", packageId,
				mainActivity));
	}

	public static void rotateLanscape() throws Exception {
		executeAdb("shell content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:1");
	}

	public static void rotatePortrait() throws Exception {
		executeAdb("shell content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:0");
	}

	/**
	 * http://stackoverflow.com/questions/13850192/how-to-lock-android-screen-
	 * via-adb
	 * 
	 * @throws Exception
	 */
	public static void lockScreen() throws Exception {
		executeAdb("shell input keyevent 26");
	}

	public static void tapBackButton() throws Exception {
		executeAdb("shell input keyevent 4");
	}

	public static int insertNewContactForMailAccountInAddressBook(String email)
			throws Exception {
		executeAdb(String.format("shell content insert "
				+ "--uri content://com.android.contacts/raw_contacts "
				+ "--bind account_type:s:com.google "
				+ "--bind account_name:s:%s", email));

		String idsList = getAdbOutput("shell content query "
				+ "--uri content://com.android.contacts/raw_contacts "
				+ "--projection _id");
		Pattern pattern = Pattern.compile("_id=(\\d+)");
		Matcher matcher = pattern.matcher(idsList);
		int value = 0;
		while (matcher.find()) {
			try {
				value = Integer.parseInt(matcher.group(1));
			} catch (NumberFormatException e) {
			}
		}
		return value;
	}

	public static void insertPhoneNumberForContactInAddressBook(int id,
			String phone) throws Exception {
		executeAdb(String.format("shell content insert "
				+ "--uri content://com.android.contacts/data "
				+ "--bind raw_contact_id:i:%s "
				+ "--bind mimetype:s:vnd.android.cursor.item/phone_v2 "
				+ "--bind data1:s:%s", id, phone));
	}

	public static void insertNameForContactInAddressBook(int id, String name)
			throws Exception {
		executeAdb(String.format("shell content insert "
				+ "--uri content://com.android.contacts/data "
				+ "--bind raw_contact_id:i:%s "
				+ "--bind mimetype:s:vnd.android.cursor.item/name "
				+ "--bind data1:s:'%s'", id, name));
	}

	public static void insertEmailForContactInAddressBook(int id, String email)
			throws Exception {
		executeAdb(String.format("shell content insert "
				+ "--uri content://com.android.contacts/data "
				+ "--bind raw_contact_id:i:%s "
				+ "--bind mimetype:s:vnd.android.cursor.item/email_v2 "
				+ "--bind data1:s:'%s'", id, email));
	}

	public static void removeAddressBookContactWithName(String name)
			throws Exception {
		executeAdb(String.format("shell content delete "
				+ "--uri content://com.android.contacts/raw_contacts "
				+ "--where \"display_name='%s'\""));
	}

	public static void cleanAddressBook() throws Exception {
		executeAdb("shell content delete "
				+ "--uri content://com.android.contacts/raw_contacts");
	}

	public static void removeTestContactsFromAddressBook() throws Exception {
		for (String name : addressBookAddedNames) {
			removeAddressBookContactWithName(name);
		}
	}

	public static void addPreDefinedUsersToAddressBook() throws Exception {
		final String USER_WITH_EMAIL_NAME = "vb003";
		final String USER_WITH_EMAIL_EMAIL = "vova+vb003@wire.com";
		final String USER_WITH_PHONE_NAME = "Amelia";
		final String USER_WITH_PHONE_PHONE = "+491705027882";
		int id = AndroidCommonUtils
				.insertNewContactForMailAccountInAddressBook(getAndroidAddressBookMailAccountFromConfig(AndroidCommonUtils.class));
		AndroidCommonUtils.insertNameForContactInAddressBook(id,
				USER_WITH_EMAIL_NAME);
		AndroidCommonUtils.insertEmailForContactInAddressBook(id,
				USER_WITH_EMAIL_EMAIL);
		addressBookAddedNames.add(USER_WITH_EMAIL_NAME);
		id = AndroidCommonUtils
				.insertNewContactForMailAccountInAddressBook(getAndroidAddressBookMailAccountFromConfig(AndroidCommonUtils.class));
		AndroidCommonUtils.insertNameForContactInAddressBook(id,
				USER_WITH_PHONE_NAME);
		AndroidCommonUtils.insertPhoneNumberForContactInAddressBook(id,
				USER_WITH_PHONE_PHONE);
		addressBookAddedNames.add(USER_WITH_PHONE_NAME);
	}

	public static double getScreenDensity() throws Exception {
		String result = getAdbOutput("shell getprop ro.sf.lcd_density");
		double screenPixels = Integer.parseInt(result);
		double densityIndependentPixels = 160; // the number of dp in a screen
												// is constant
		return screenPixels / densityIndependentPixels;
	}
}
