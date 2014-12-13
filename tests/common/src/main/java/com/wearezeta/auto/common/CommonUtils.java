package com.wearezeta.auto.common;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.core.UriBuilderException;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class CommonUtils {
	public static final String OS_NAME_WINDOWS = "Windows";
	public static final int USERS_COUNT = 11;
	public static final String PERFORMANCE_USER = "perfUser";
	public static final String PERFORMANCE_PASS = "perfPass";
	public static final String YOUR_USER_1 = "aqaUser";
	public static final String YOUR_USER_2 = "yourUser";
	public static final String YOUR_USER_3 = "yourContact";
	public static final String YOUR_USER_4 = "yourNotContact1";
	public static final String YOUR_USER_5 = "yourNotContact2";
	public static final String YOUR_USER_6 = "yourNotContact3";
	public static final String YOUR_USER_7 = "yourNotContact4";
	public static final String YOUR_USER_8 = "yourIgnore";
	public static final String YOUR_USER_9 = "yourAccept";
	public static final String YOUR_USER_10 = "yourGroupChat";
	public static final String YOUR_USER_11 = "yourNotContact5";
	public static final String YOUR_UNCONNECTED_USER = YOUR_USER_3;
	public static final String YOUR_PASS = "aqaPassword";
	public static final String CONTACT_1 = "aqaContact1";
	public static final String CONTACT_2 = "aqaContact2";
	public static final String CONTACT_3 = "aqaContact3";
	public static final String CONTACT_4 = "aqaPictureContact";
	public static final String CONTACT_5 = "aqaAvatar TestContact";
	public static final String CONTACT_6 = "aqaBlock";
	public static final int USERS_CREATION_TIMEOUT = 60 * 5; // seconds
	public static ClientUser perfUser = new ClientUser();
	public static List<ClientUser> yourUsers = new CopyOnWriteArrayList<ClientUser>();
	public static List<ClientUser> contacts = new CopyOnWriteArrayList<ClientUser>();
	public static List<ClientUser> additionalUsers = new CopyOnWriteArrayList<ClientUser>();
	public static List<ClientUser> requiredContacts = new CopyOnWriteArrayList<ClientUser>();
	public static List<ConvPair> user_chats = new CopyOnWriteArrayList<ConvPair>();
	public static final int MAX_PARALLEL_USER_CREATION_TASKS = 5;
	public static final int NUMBER_OF_REGISTRATION_RETRIES = 5;

	public static final String CONTACT_PICTURE_NAME = "aqaPictureContact";
	public static final String CONTACT_PICTURE_EMAIL = "smoketester+aqaPictureContact@wearezeta.com";
	public static final String CONTACT_PICTURE_PASSWORD = "picture123";
	public static final String CONTACT_AVATAR_NAME = "aqaAvatar TestContact";
	public static final String CONTACT_AVATAR_EMAIL = "smoketester+aqaAvatarTestContact@wearezeta.com";
	public static final String CONTACT_AVATAR_PASSWORD = "avatar123";

	private static final String USER_IMAGE = "userpicture_landscape.jpg";
	private static final String RESULT_USER_IMAGE = "userpicture_mobile_check.jpg";
	private static final String PING_IMAGE = "ping_image.png";
	private static final String HOT_PING_IMAGE = "hot_ping_image.png";
	private static final String IOS_PING_IMAGE = "ios_ping_image.png";
	private static final String IOS_HOT_PING_IMAGE = "ios_hot_ping_image.png";

	private static final Random rand = new Random();
	public static final int BACKEND_SYNC_TIMEOUT = 5000 + rand.nextInt(4000); // milliseconds

	public static final String PLATFORM_NAME_OSX = "Mac";
	public static final String PLATFORM_NAME_ANDROID = "Android";
	public static final String PLATFORM_NAME_IOS = "iOS";
	private static final Logger log = ZetaLogger.getLog(CommonUtils.class
			.getSimpleName());

	public static String getOsName() {
		return System.getProperty("os.name");
	}

	public static void executeOsXCommand(String[] cmd) throws Exception {
		Process process = Runtime.getRuntime().exec(cmd);
		log.debug("Process started for cmdline " + Arrays.toString(cmd));
		outputErrorStreamToLog(process.getErrorStream());
		log.debug("Process exited with code " + process.waitFor());
	}

	public static void outputErrorStreamToLog(InputStream stream)
			throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		StringBuilder sb = new StringBuilder("\n");
		String s;
		while ((s = br.readLine()) != null) {
			sb.append("\t" + s + "\n");
		}
		String output = sb.toString();
		if (!output.trim().isEmpty()) {
			log.debug(output);
		}
		stream.close();
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

	public static String retrieveRealUserEmailValue(String contact) {
		String email = "";
		boolean flag = false;
		for (ClientUser user : CommonUtils.yourUsers) {
			if (user.getName().equals(contact)) {
				email = user.getEmail();
				flag = true;
				break;
			}
		}
		if (flag == false) {
			for (ClientUser user : CommonUtils.contacts) {
				if (user.getName().equals(contact)) {
					email = user.getEmail();
					break;
				}
			}
		}

		return email;
	}

	public static String retrieveRealUserContactPasswordValue(String value) {
		// TODO: This method requires better optimization
		String result;
		if (value.toLowerCase().equals(
				CommonUtils.PERFORMANCE_USER.toLowerCase())) {
			result = perfUser.getName();
		} else if (value.equals(CommonUtils.PERFORMANCE_PASS)) {
			result = perfUser.getPassword();
		} else {
			Map<String, String> replacementMap = new LinkedHashMap<String, String>();
			if (yourUsers.size() > 0) {
				try {
					replacementMap.put(YOUR_USER_1, yourUsers.get(0).getName());
					replacementMap.put(YOUR_PASS, yourUsers.get(0)
							.getPassword());
					replacementMap.put(YOUR_USER_2, yourUsers.get(1).getName());
					replacementMap.put(YOUR_USER_3, yourUsers.get(2).getName());
					replacementMap.put(YOUR_USER_4, yourUsers.get(3).getName());
					replacementMap.put(YOUR_USER_5, yourUsers.get(4).getName());
					replacementMap.put(YOUR_USER_6, yourUsers.get(5).getName());
					replacementMap.put(YOUR_USER_7, yourUsers.get(6).getName());
					replacementMap.put(YOUR_USER_8, yourUsers.get(7).getName());
					replacementMap.put(YOUR_USER_9, yourUsers.get(8).getName());
					replacementMap
							.put(YOUR_USER_10, yourUsers.get(9).getName());
					replacementMap.put(YOUR_USER_11, yourUsers.get(10)
							.getName());
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
			if (contacts.size() > 0) {
				try {
					replacementMap.put(CONTACT_1, contacts.get(0).getName());
					replacementMap.put(CONTACT_2, contacts.get(1).getName());
					replacementMap.put(CONTACT_3, contacts.get(2).getName());
					replacementMap.put(CONTACT_4, CONTACT_PICTURE_NAME);
					replacementMap.put(CONTACT_5, CONTACT_AVATAR_NAME);
					replacementMap.put(CONTACT_6, contacts.get(3).getName());
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
			// TODO: Magic strings
			replacementMap.put("aqaPictureContactEmail", CONTACT_PICTURE_EMAIL);
			replacementMap.put("aqaPictureContactPassword",
					CONTACT_PICTURE_PASSWORD);
			replacementMap.put("aqaAvatarTestContactEmail",
					CONTACT_AVATAR_EMAIL);
			replacementMap.put("aqaAvatarTestContactPassword",
					CONTACT_AVATAR_PASSWORD);

			result = value;
			for (Entry<String, String> replacementEntry : replacementMap
					.entrySet()) {
				if (result.contains(replacementEntry.getKey())) {
					// TODO: Should we continue replacements or we can return
					// entry.value immediately?
					result = result.replace(replacementEntry.getKey(),
							replacementEntry.getValue());
				}
			}

		}
		return result;
	}

	public static String getBackendType(Class<?> c) throws IOException {
		return getValueFromCommonConfig(c, "backendType");
	}

	public static String getImagePath(Class<?> c) throws IOException {
		String path = getValueFromConfig(c, "defaultImagesPath") + USER_IMAGE;
		return path;
	}

	public static String getPingIconPath(Class<?> c) throws IOException {
		String path = getValueFromConfig(c, "defaultImagesPath") + PING_IMAGE;
		return path;
	}

	public static String getPingIconPathIOS(Class<?> c) throws IOException {
		String path = getValueFromConfig(c, "defaultImagesPath")
				+ IOS_PING_IMAGE;
		return path;
	}

	public static String getHotPingIconPath(Class<?> c) throws IOException {
		String path = getValueFromConfig(c, "defaultImagesPath")
				+ HOT_PING_IMAGE;
		return path;
	}

	public static String getHotPingIconPathIOS(Class<?> c) throws IOException {
		String path = getValueFromConfig(c, "defaultImagesPath")
				+ IOS_HOT_PING_IMAGE;
		return path;
	}

	public static String getImagesPath(Class<?> c) throws IOException {
		return getValueFromConfig(c, "defaultImagesPath");
	}

	public static String getIsOldWayUsersGeneration(Class<?> c)
			throws IOException {
		String users = getValueFromConfig(c, "oldWayUsersGeneration");
		return users;
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

	public static String getValueFromConfig(Class<?> c, String key)
			throws IOException {
		return getValueFromConfigFile(c, key, "Configuration.cnf");
	}

	public static String getValueFromCommonConfig(Class<?> c, String key)
			throws IOException {
		return getValueFromConfigFile(c, key, "CommonConfiguration.cnf");
	}

	public static String getDefaultEmailFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "defaultEmail");
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

	public static String getOsxAppiumUrlFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "osxAppiumUrl");
	}

	public static String getAndroidAppiumUrlFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "androidAppiumUrl");
	}

	public static String getIosAppiumUrlFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "iosAppiumUrl");
	}

	public static Boolean getIsSimulatorFromConfig(Class<?> c)
			throws IOException {
		return (getValueFromConfig(c, "isSimulator").equals("true"));
	}

	public static String getSwipeScriptPath(Class<?> c) throws IOException {
		return getValueFromConfig(c, "swipeScriptPath");
	}

	public static String getOsxApplicationPathFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "osxApplicationPath");
	}

	public static String getAndroidApplicationPathFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "androidApplicationPath");
	}

	public static String getIosApplicationPathFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "iosApplicationPath");
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

	public static String getAndroidPackageFromConfig(Class<?> c) {
		try {
			return getValueFromConfig(c, "package");
		} catch (IOException e) {
			return null;
		}
	}

	public static String getUserPicturePathFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "pathToUserpic");
	}

	public static String generateGUID() {
		return UUID.randomUUID().toString();
	}

	public static String generateRandomString(int lengh) {
		return RandomStringUtils.randomAlphanumeric(lengh);
	}

	public static BufferedImage getElementScreenshot(WebElement element,
			RemoteWebDriver driver) throws IOException {
		BufferedImage screenshot = DriverUtils
				.takeScreenshot((ZetaDriver) driver);
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
		Map<String, String> names = new HashMap<String, String>();
		names.put("aqaPictureContactEmail", CONTACT_PICTURE_NAME);
		names.put("aqaAvatarTestContactEmail", CONTACT_AVATAR_NAME);

		Map<String, String> creds = new HashMap<String, String>();
		creds.put("aqaPictureContactEmail", "aqaPictureContactPassword");
		creds.put("aqaAvatarTestContactEmail", "aqaAvatarTestContactPassword");

		for (Map.Entry<String, String> entry : creds.entrySet()) {
			ClientUser user = new ClientUser();
			user.setEmail(CommonUtils
					.retrieveRealUserContactPasswordValue(entry.getKey()));
			user.setName(names.get(entry.getKey()));
			user.setPassword(CommonUtils
					.retrieveRealUserContactPasswordValue(entry.getValue()));
			user.setUserState(UsersState.Created);
			contacts.add(user);
		}
	}

	public static void generateUsers(int contactsNumber) throws Exception {
		generateUsers(USERS_COUNT, contactsNumber);
	}

	public static void generateUsers(int usersNumber, int contactsNumber)
			throws Exception {
		ExecutorService executor = Executors
				.newFixedThreadPool(MAX_PARALLEL_USER_CREATION_TASKS);
		for (int i = 0; i < usersNumber + contactsNumber; i++) {
			final boolean isContact = (i >= usersNumber);
			Runnable worker = new Thread(new Runnable() {
				public void run() {
					boolean doRetry = true;
					int count = 0;
					int waitTime = 1;
					while (doRetry && count < NUMBER_OF_REGISTRATION_RETRIES) {
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
							doRetry = false;
						} catch (Exception e) {
							doRetry = true;
							e.printStackTrace();
						}
						if (doRetry) {
							count++;
							try {
								Thread.sleep(waitTime * 1000);
							} catch (InterruptedException e) {
							}
							waitTime += 2;
						}
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
		if (yourUsers.size() != usersNumber
				|| contacts.size() != contactsNumber) {
			throw new BackendRequestException(
					"Failed to create new users or contacts on the backend");
		}

		generateAdditionalContacts();
	}

	public static void generateNUsers(int usersNum)
			throws InterruptedException, BackendRequestException {
		ExecutorService executor = Executors
				.newFixedThreadPool(MAX_PARALLEL_USER_CREATION_TASKS);
		final AtomicInteger numOfUsersCreatedWOErrors = new AtomicInteger();
		numOfUsersCreatedWOErrors.set(0);
		for (int i = 0; i < usersNum; i++) {
			Runnable worker = new Thread(new Runnable() {
				public void run() {
					try {
						String email = CreateZetaUser
								.registerUserAndReturnMail();
						if (email == null)
							return;
						ClientUser user = new ClientUser();
						user.setEmail(email);
						user.setPassword(CommonUtils
								.getDefaultPasswordFromConfig(CommonUtils.class));
						additionalUsers.add(user);
						numOfUsersCreatedWOErrors.set(numOfUsersCreatedWOErrors
								.incrementAndGet());
					} catch (Exception e) {
						log.debug("error" + e.getMessage());
					}
				}
			});
			executor.submit(worker);
		}
		executor.shutdown();
		if (!executor
				.awaitTermination(USERS_CREATION_TIMEOUT, TimeUnit.SECONDS)) {
			throw new BackendRequestException(
					String.format(
							"The backend has failed to create %d users within %d seconds timeout",
							usersNum, USERS_CREATION_TIMEOUT));
		}
		if (numOfUsersCreatedWOErrors.get() != usersNum) {
			throw new BackendRequestException(String.format(
					"Failed to create %d new users or contacts on the backend",
					usersNum));
		}
	}

	public static void sendConnectionRequestInThreads(final ClientUser yourUser)
			throws IllegalArgumentException, UriBuilderException, IOException,
			JSONException, BackendRequestException, InterruptedException {
		ExecutorService executor = Executors
				.newFixedThreadPool(MAX_PARALLEL_USER_CREATION_TASKS);
		for (int i = 0; i < additionalUsers.size(); i++) {
			final ClientUser user = additionalUsers.get(i);
			Runnable worker = new Thread(new Runnable() {
				public void run() {
					try {
						BackEndREST.autoTestSendRequest(user, yourUser);
						ConvPair pair = new ConvPair();
						pair.setContact(user);
						pair.setConvName(yourUser.getName());
						user_chats.add(pair);
					} catch (Exception e) {
						log.debug(e.getMessage());
					}
				}
			});
			executor.submit(worker);
		}
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			log.debug(e.getMessage());
		}
	}

	public static void sendRandomMessagesToUser(int messCount) {
		Random random = new Random();
		if (messCount > user_chats.size()) {
			messCount = user_chats.size();
		}
		ExecutorService executor = Executors
				.newFixedThreadPool(MAX_PARALLEL_USER_CREATION_TASKS);
		for (int i = 0; i < messCount; i++) {
			ConvPair pair = user_chats
					.get(random.nextInt(user_chats.size() - 1));
			final ClientUser user = pair.getContact();
			final String contact = pair.getConvName();
			Runnable worker = new Thread(new Runnable() {
				public void run() {
					try {
						BackEndREST.sendDialogMessageByChatName(user, contact,
								CommonUtils.generateGUID());
					} catch (Throwable e) {
						log.debug(e.getMessage());
					}
				}
			});
			executor.submit(worker);
		}
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			log.debug(e.getMessage());
		}
	}

	public static void sendDefaultImageToUser(int imagesCount) throws Throwable {
		InputStream configFileStream = null;
		Random random = new Random();
		configFileStream = new FileInputStream(getImagePath(CommonUtils.class));
		for (int i = 0; i < imagesCount; i++) {
			ConvPair pair = user_chats
					.get(random.nextInt(user_chats.size() - 1));
			final ClientUser user = pair.getContact();
			final String contact = pair.getConvName();
			BackEndREST.sendPictureToChatByName(user, contact, "default",
					configFileStream);
		}
		if (configFileStream != null) {
			configFileStream.close();
		}
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

	public static void generatePerformanceUser() throws Exception {
		String email = CreateZetaUser.registerUserAndReturnMail();
		if (email == null)
			return;
		perfUser.setEmail(email);
		perfUser.setPassword(getDefaultPasswordFromConfig(CommonUtils.class));
		perfUser = BackEndREST.loginByUser(perfUser);
		perfUser = BackEndREST.getUserInfo(perfUser);
		yourUsers.add(perfUser);
	}
}
