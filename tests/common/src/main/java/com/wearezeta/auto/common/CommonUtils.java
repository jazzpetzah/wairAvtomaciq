package com.wearezeta.auto.common;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.ws.rs.core.UriBuilderException;

import org.apache.commons.collections.ListUtils;
import org.json.JSONException;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.wearezeta.auto.common.driver.DriverUtils;

public class CommonUtils {
	public static final String OS_NAME_WINDOWS = "Windows";

	public static final int USERS_COUNT = 4;
	public static final String YOUR_USER_1 = "aqaUser";
	public static final String YOUR_USER_2 = "yourUser";
	public static final String YOUR_USER_3 = "yourContact";
	public static final String YOUR_USER_4 = "yourNotContact";
	public static final String YOUR_UNCONNECTED_USER = YOUR_USER_3;
	public static final String YOUR_PASS = "aqaPassword";
	public static final String CONTACT_1 = "aqaContact1";
	public static final String CONTACT_2 = "aqaContact2";
	public static final String CONTACT_3 = "aqaContact3";
	public static final String CONTACT_4 = "aqaPictureContact";
	public static final String CONTACT_5 = "aqaAvatar TestContact";
	public static final int USERS_CREATION_TIMEOUT = 60 * 5; // seconds
	public static List<ClientUser> yourUsers = new CopyOnWriteArrayList<ClientUser>();
	public static List<ClientUser> contacts = new CopyOnWriteArrayList<ClientUser>();
	public static final int MAX_PARALLEL_USER_CREATION_TASKS = 5;

	public static final String CONTACT_PICTURE_NAME = "aqaPictureContact";
	public static final String CONTACT_PICTURE_EMAIL = "qa1+aqaPictureContact@wearezeta.com";
	public static final String CONTACT_PICTURE_PASSWORD = "picture123";
	public static final String CONTACT_AVATAR_NAME = "aqaAvatar TestContact";
	public static final String CONTACT_AVATAR_EMAIL = "qa1+aqaAvatarTestContact@wearezeta.com";
	public static final String CONTACT_AVATAR_PASSWORD = "avatar123";

	private static final String USER_IMAGE = "userpicture_landscape.jpg";
	private static final String RESULT_USER_IMAGE = "userpicture_mobile_check.jpg";

	public static String getOsName() {
		return System.getProperty("os.name");
	}

	public static void executeOsXCommand(String[] cmd) throws Exception {
		Process process = Runtime.getRuntime().exec(cmd);
		System.out.println("Process Code " + process.waitFor());
	}

	public static ClientUser findUserNamed(String username) {
		// searches yourUsers and contacts lists only.
		// Can be used to replace Example: CommonUtils.yourUser.get(0) with
		// findUserNamed(aqaUser)
		username = retrieveRealUserContactPasswordValue(username);
		for (Object item : ListUtils.union(yourUsers, contacts)) {
			ClientUser user = (ClientUser) item;
			if (user.getName().equalsIgnoreCase(username)) {
				return user;
			}
		}
		throw new NoSuchElementException("No user with username: " + username
				+ " is in an available list");
	}

	public static String retrieveRealUserContactPasswordValue(String value) {
		// TODO: This method requires better optimization
		Map<String, String> replacementMap = new LinkedHashMap<String, String>();
		if (yourUsers.size() > 0) {
			replacementMap.put(YOUR_USER_1, yourUsers.get(0).getName());
			replacementMap.put(YOUR_USER_2, yourUsers.get(1).getName());
			replacementMap.put(YOUR_USER_3, yourUsers.get(2).getName());
			replacementMap.put(YOUR_USER_4, yourUsers.get(3).getName());
			replacementMap.put(YOUR_PASS, yourUsers.get(0).getPassword());
		}
		if (contacts.size() > 0) {
			replacementMap.put(CONTACT_1, contacts.get(0).getName());
			replacementMap.put(CONTACT_2, contacts.get(1).getName());
			replacementMap.put(CONTACT_3, contacts.get(2).getName());
			replacementMap.put(CONTACT_4, CONTACT_PICTURE_NAME);
			replacementMap.put(CONTACT_5, CONTACT_AVATAR_NAME);
		}
		// TODO: Magic strings 
		replacementMap.put("aqaPictureContactEmail", CONTACT_PICTURE_EMAIL);
		replacementMap.put("aqaPictureContactPassword",
				CONTACT_PICTURE_PASSWORD);
		replacementMap.put("aqaAvatarTestContactEmail", CONTACT_AVATAR_EMAIL);
		replacementMap.put("aqaAvatarTestContactPassword",
				CONTACT_AVATAR_PASSWORD);

		String result = value;
		for (Entry<String, String> replacementEntry : replacementMap.entrySet()) {
			if (result.contains(replacementEntry.getKey())) {
				// TODO: Should we continue replacements or we can return entry.value immediately?
				result = result.replace(replacementEntry.getKey(),
						replacementEntry.getValue());
			}
		}
		return result;
	}

	public static String getImagePath(Class<?> c) throws IOException {
		String path = getValueFromConfig(c, "defaultImagesPath") + USER_IMAGE;
		return path;
	}

	public static String getImagesPath(Class<?> c) throws IOException {
		return getValueFromConfig(c, "defaultImagesPath");
	}

	public static String getResultImagePath(Class<?> c) throws IOException {
		String path = getValueFromConfig(c, "defaultImagesPath")
				+ RESULT_USER_IMAGE;
		return path;
	}

	public static String getPictureResultsPathFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "pictureResultsPath");
	}

	private static String getValueFromConfigFile(Class<?> c, String key,
			String resourcePath) throws IOException {
		String val = "";
		InputStream configFileStream = null;
		try {
			URL configFile = c.getClass().getResource("/" + resourcePath);
			configFileStream = configFile.openStream();
			Properties p = new Properties();
			p.load(configFileStream);
			val = (String) p.get(key);
		} finally {
			if (configFileStream != null) {
				configFileStream.close();
			}
		}
		return val;
	}

	private static String getValueFromConfig(Class<?> c, String key)
			throws IOException {
		return getValueFromConfigFile(c, key, "Configuration.cnf");
	}

	private static String getValueFromCommonConfig(Class<?> c, String key)
			throws IOException {
		return getValueFromConfigFile(c, key, "CommonConfiguration.cnf");
	}

	public static String getDefaultEmailFromConfig(Class<?> c)
			throws IOException {
		return getValueFromCommonConfig(c, "defaultEmail");
	}

	public static String getDefaultEmailServerFromConfig(Class<?> c)
			throws IOException {
		return getValueFromCommonConfig(c, "defaultEmailServer");
	}

	public static String getDriverTimeoutFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "driverTimeoutSeconds");
	}

	public static String getDefaultPasswordFromConfig(Class<?> c)
			throws IOException {
		return getValueFromCommonConfig(c, "defaultPassword");
	}

	public static String getDefaultBackEndUrlFromConfig(Class<?> c)
			throws IOException {
		return getValueFromCommonConfig(c, "defaultBackEndUrl");
	}

	public static String getUrlFromConfig(Class<?> c) throws IOException {
		return getValueFromConfig(c, "Url");
	}

	public static Boolean getIsSimulatorFromConfig(Class<?> c)
			throws IOException {
		return (getValueFromConfig(c, "isSimulator").equals("true"));
	}

	public static String getSwipeScriptPath(Class<?> c) throws IOException {
		return getValueFromConfig(c, "swipeScriptPath");
	}

	public static String getAppPathFromConfig(Class<?> c) throws IOException {
		return getValueFromConfig(c, "appPath");
	}

	public static String getAndroidActivityFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "activity");
	}

	public static String getSimulatorImagesPathFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "iosImagesPath");
	}

	public static String getGenerateUsersFlagFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "generateUsers");
	}

	public static String getAndroidPackageFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "package");
	}

	public static String getUserPicturePathFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "pathToUserpic");
	}

	public static String generateGUID() {
		return UUID.randomUUID().toString();
	}

	public static BufferedImage getElementScreenshot(WebElement element,
			RemoteWebDriver driver) throws IOException {
		BufferedImage screenshot = DriverUtils.takeScreenshot(driver);
		org.openqa.selenium.Point elementLocation = element.getLocation();
		Dimension elementSize = element.getSize();
		return screenshot.getSubimage(elementLocation.x * 2,
				elementLocation.y * 2, elementSize.width * 2,
				elementSize.height * 2);
	}

	public static String getContactName(String login) {
		String[] firstParts = null;
		String[] secondParts = null;
		firstParts = login.split("\\+");
		secondParts = firstParts[1].split("@");
		return secondParts[0];
	}

	private static void generateAdditionalContacts() {
		// insert values of the contact in
		// "CommonUtils.retrieveRealUserContactPasswordValue" first
		Map<String, String> creds = new HashMap<String, String>();
		creds.put("aqaPictureContactEmail", "aqaPictureContactPassword");
		creds.put("aqaAvatarTestContactEmail", "aqaAvatarTestContactPassword");
		for (Map.Entry<String, String> entry : creds.entrySet()) {
			ClientUser user = new ClientUser();
			user.setEmail(CommonUtils
					.retrieveRealUserContactPasswordValue(entry.getKey()));
			user.setPassword(CommonUtils
					.retrieveRealUserContactPasswordValue(entry.getValue()));
			user.setUserState(UsersState.Created);
			contacts.add(user);
		}
	}

	public static void generateUsers(int contactNumber) throws IOException,
			MessagingException, IllegalArgumentException, UriBuilderException,
			JSONException, BackendRequestException, InterruptedException {
		ExecutorService executor = Executors
				.newFixedThreadPool(MAX_PARALLEL_USER_CREATION_TASKS);
		for (int i = 0; i < USERS_COUNT + contactNumber; i++) {
			final boolean isContact = (i >= USERS_COUNT);
			Runnable worker = new Thread(new Runnable() {
				public void run() {
					try {
						final String email = CreateZetaUser
								.registerUserAndReturnMail();
						if (email == null)
							return;
						ClientUser user = new ClientUser();
						user.setEmail(email);
						user.setPassword(getDefaultPasswordFromConfig(CommonUtils.class));
						user.setUserState(UsersState.Created);
						if (isContact) {
							contacts.add(user);
						} else {
							yourUsers.add(user);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			executor.execute(worker);
		}
		executor.shutdown();
		if (!executor
				.awaitTermination(USERS_CREATION_TIMEOUT, TimeUnit.SECONDS)) {
			throw new BackendRequestException(
					String.format(
							"The backend has failed to prepare predefined users within %d seconds timeout",
							USERS_CREATION_TIMEOUT));
		}
		if (yourUsers.size() != USERS_COUNT || contacts.size() != contactNumber) {
			throw new BackendRequestException(
					"Failed to create new users or contacts on the backend");
		}

		generateAdditionalContacts();
	}

	public static void usePrecreatedUsers() {
		// TODO: maybe these constants are global?
		final String DEFAULT_PASSWORD = "aqa123456";
		final String EMAIL_TEMPLATE = "smoketester+%s@wearezeta.com";

		String[] userIds = new String[] { "1f91773deae943948da19b86cd818388",
				"50d287c2407e4c5e8af578979d436c88",
				"bbf79363bd3d4ff3ae6a835ed27fe274" };
		for (String userId : userIds) {
			ClientUser user = new ClientUser(String.format(EMAIL_TEMPLATE,
					userId), DEFAULT_PASSWORD, userId,
					UsersState.AllContactsConnected);
			yourUsers.add(user);
		}

		String[] contactIds = new String[] {
				"3e54e65b95cc46608d970b3e949e4773",
				"34a6a8a88a6e4f9aa1bc77b94ec7ae3a",
				"34a6a8a88a6e4f9aa1bc77b94ec7ae3a" };
		for (String contactId : contactIds) {
			ClientUser contact = new ClientUser(String.format(EMAIL_TEMPLATE,
					contactId), DEFAULT_PASSWORD, contactId,
					UsersState.AllContactsConnected);
			contacts.add(contact);
		}
	}

	public static String getAndroidDeviceNameFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "deviceName");
	}

}
