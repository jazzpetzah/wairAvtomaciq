package com.wearezeta.auto.common;

import com.wearezeta.auto.common.DriverUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CommonUtils {

	public static final String ENGLISH_LANG_NAME = "english";

	public static final String FIRST_OS_NAME = "Windows";
	public static final String YOUR_USER_1 = "aqaUser";
	public static final String YOUR_USER_2 = "yourUser";
	public static final String YOUR_UNCONNECTED_USER = "yourContact";
	public static final String YOUR_PASS = "aqaPassword";
	public static final String CONTACT_1 = "aqaContact1";
	public static final String CONTACT_2 = "aqaContact2";
	public static final String CONTACT_3 = "aqaContact3";
	public static final String CONTACT_4 = "aqaPictureContact";
	public static final String CONTACT_5 = "aqaAvatar TestContact";
	public static LinkedList<ClientUser> yourUsers = new LinkedList<ClientUser>();
	public static LinkedList<ClientUser> contacts = new LinkedList<ClientUser>();

	public static final String CONTACT_PICTURE_NAME = "aqaPictureContact";
	public static final String CONTACT_PICTURE_EMAIL = "qa1+aqaPictureContact@wearezeta.com";
	public static final String CONTACT_PICTURE_PASSWORD = "picture123";
	public static final String CONTACT_AVATAR_NAME = "aqaAvatar TestContact";
	public static final String CONTACT_AVATAR_EMAIL = "qa1+aqaAvatarTestContact@wearezeta.com";
	public static final String CONTACT_AVATAR_PASSWORD = "avatar123";

	private static final String USER_IMAGE = "userpicture.jpg";
	private static final String RESULT_USER_IMAGE = "userpicture_mobile_check.jpg";

	public static String getOsName() {
		return System.getProperty("os.name");
	}

	public static void uploadPhotoToAndroid(String photoPathOnDevice)
			throws Exception {
		if (getOsName().contains(FIRST_OS_NAME)) {
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
		if (getOsName().contains(FIRST_OS_NAME)) {
			Runtime.getRuntime().exec(
					"cmd /C adb shell am force-stop com.waz.zclient");
		} else {
			executeOsXCommand(new String[] { "/bin/bash", "-c",
					"adb shell am force-stop com.waz.zclient" });
		}
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
		for (ClientUser user : yourUsers) {
			if (user.getName().equalsIgnoreCase(username)) {
				return user;
			}
		}
		for (ClientUser user : contacts) {
			if (user.getName().equalsIgnoreCase(username)) {
				return user;
			}
		}
		throw new NoSuchElementException("No user with username: " + username
				+ " is in an available list");
	}

	public static String retrieveRealUserContactPasswordValue(String value) {
		Map<String, String> usernameConstantAndUsernameReal = new HashMap<String, String>();
		usernameConstantAndUsernameReal.put(YOUR_USER, yourUsers.get(0)
				.getName());
		usernameConstantAndUsernameReal.put(YOUR_USER_1, yourUsers.get(1)
				.getName());
		usernameConstantAndUsernameReal.put(YOUR_UNCONNECTED_USER, yourUsers
				.get(2).getName());
		usernameConstantAndUsernameReal.put(YOUR_PASS, yourUsers.get(0)
				.getPassword());
		usernameConstantAndUsernameReal.put(CONTACT_1, contacts.get(0)
				.getName());
		usernameConstantAndUsernameReal.put(CONTACT_2, contacts.get(1)
				.getName());
		usernameConstantAndUsernameReal.put(CONTACT_3, contacts.get(2)
				.getName());
		usernameConstantAndUsernameReal.put(CONTACT_4, CONTACT_PICTURE_NAME);
		usernameConstantAndUsernameReal.put(CONTACT_5, CONTACT_AVATAR_NAME);
		usernameConstantAndUsernameReal.put("aqaPictureContactEmail",
				CONTACT_PICTURE_EMAIL);
		usernameConstantAndUsernameReal.put("aqaPictureContactPassword",
				CONTACT_PICTURE_PASSWORD);
		usernameConstantAndUsernameReal.put("aqaAvatarTestContactEmail",
				CONTACT_AVATAR_EMAIL);
		usernameConstantAndUsernameReal.put("aqaAvatarTestContactPassword",
				CONTACT_AVATAR_PASSWORD);

		for (Map.Entry<String, String> entry : usernameConstantAndUsernameReal
				.entrySet()) {
			if (entry.getKey().toLowerCase()
					.equalsIgnoreCase(value.toLowerCase())) {
				value = entry.getValue();
			}
		}
		return value;
	}

	public static String getImagePath(Class c) throws IOException {
		String path = getValueFromConfig(c, "defaultImagesPath") + USER_IMAGE;
		return path;
	}

	public static String getImagesPath(Class c) throws IOException {

		return getValueFromConfig(c, "defaultImagesPath");
	}

	public static String getResultImagePath(Class c) throws IOException {

		String path = getValueFromConfig(c, "defaultImagesPath")
				+ RESULT_USER_IMAGE;
		return path;
	}

	public static String getPictureResultsPathFromConfig(Class c)
			throws IOException {

		return getValueFromConfig(c, "pictureResultsPath");
	}

	private static String getValueFromConfig(Class c, String key)
			throws IOException {

		String val = "";
		InputStream configFileStream = null;

		try {
			URL configFile = c.getClass().getResource("/Configuration.cnf");
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

	private static String getValueFromCommonConfig(Class c, String key)
			throws IOException {

		String val = "";
		InputStream configFileStream = null;

		try {
			URL configFile = c.getClass().getResource(
					"/CommonConfiguration.cnf");
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

	public static String getDefaultEmailFromConfig(Class c) throws IOException {

		return getValueFromCommonConfig(c, "defaultEmail");
	}

	public static String getDefaultEmailServerFromConfig(Class c)
			throws IOException {

		return getValueFromCommonConfig(c, "defaultEmailServer");
	}

	public static String getDriverTimeoutFromConfig(Class c) throws IOException {

		return getValueFromConfig(c, "driverTimeoutSeconds");
	}

	public static String getDefaultPasswordFromConfig(Class c)
			throws IOException {

		return getValueFromCommonConfig(c, "defaultPassword");
	}

	public static String getDefaultBackEndUrlFromConfig(Class c)
			throws IOException {

		return getValueFromCommonConfig(c, "defaultBackEndUrl");
	}

	public static String getUrlFromConfig(Class c) throws IOException {

		return getValueFromConfig(c, "Url");
	}

	public static Boolean getIsSimulatorFromConfig(Class c) throws IOException {

		return (getValueFromConfig(c, "isSimulator").equals("true"));
	}

	public static String getSwipeScriptPath(Class c) throws IOException {

		return getValueFromConfig(c, "swipeScriptPath");
	}

	public static String getAppPathFromConfig(Class c) throws IOException {

		return getValueFromConfig(c, "appPath");
	}

	public static String getAndroidActivityFromConfig(Class c)
			throws IOException {

		return getValueFromConfig(c, "activity");
	}

	public static String getSimulatorImagesPathFromConfig(Class c)
			throws IOException {
		return getValueFromConfig(c, "iosImagesPath");
	}

	public static String getGenerateUsersFlagFromConfig(Class c)
			throws IOException {

		return getValueFromConfig(c, "generateUsers");
	}

	public static String getAndroidPackageFromConfig(Class c)
			throws IOException {

		return getValueFromConfig(c, "package");
	}

	public static String getUserPicturePathFromConfig(Class c)
			throws IOException {

		return getValueFromConfig(c, "pathToUserpic");
	}

	public static String generateGUID() {
		return UUID.randomUUID().toString();
	}

	/*
	 * BEGIN: For parsing the XML files of other languages and inputting them in
	 * the character test as per multiple iOS user stories Currently only being
	 * used for the English language
	 */
	private static String getLanguageAlphabet(String languageName)
			throws Throwable {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		docBuilderFactory.setValidating(false);
		docBuilderFactory.setNamespaceAware(true);
		docBuilderFactory.setFeature("http://xml.org/sax/features/namespaces",
				false);
		docBuilderFactory.setFeature("http://xml.org/sax/features/validation",
				false);
		docBuilderFactory
				.setFeature(
						"http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
						false);
		docBuilderFactory
				.setFeature(
						"http://apache.org/xml/features/nonvalidating/load-external-dtd",
						false);
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

		InputStream languageFileStream = null;
		String languageFilePath = String.format("/LanguageFiles/%s.xml",
				languageName.toLowerCase());
		try {
			URL languageFile = CommonUtils.class.getResource(languageFilePath);
			languageFileStream = languageFile.openStream();

			if (languageFileStream == null) {
				throw new Exception(String.format(
						"Failed to load %s from resources", languageFilePath));
			}
			Document doc = docBuilder.parse(languageFileStream);
			doc.getDocumentElement().normalize();
			NodeList characterTypes = doc
					.getElementsByTagName("exemplarCharacters");
			StringBuilder alphabet = new StringBuilder(100);
			for (int i = 0; i < characterTypes.getLength(); i++) {
				Node firstTypeNode = characterTypes.item(i);
				if (firstTypeNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstElement = (Element) firstTypeNode;

					if (null != firstElement.getAttribute("type")) {
						if (firstElement.getAttribute("type").equals(
								"auxiliary")) {
							continue;
						}
					}
					NodeList textFNList = firstElement.getChildNodes();
					String characters = ((Node) textFNList.item(0))
							.getNodeValue().trim();
					String[] charactersArr = characters.replaceAll("^\\[|\\]$",
							"").split("\\s");
					for (String chr : charactersArr) {
						if (chr.length() == 6
								&& chr.substring(0, 2).equals("\\u")) {
							int c = Integer.parseInt(chr.substring(2, 6), 16);
							alphabet.append(Character.toString((char) c));
						} else {
							alphabet.append(chr);
						}
					}
				}

			}
			return alphabet.toString();
		} finally {
			if (languageFileStream != null) {
				languageFileStream.close();
			}
		}
	}

	public static List<String> getUnicodeStringAsCharList(String str) {
		List<String> characters = new ArrayList<String>();
		Pattern pat = Pattern.compile("\\p{L}\\p{M}*|\\W");
		Matcher matcher = pat.matcher(str);
		while (matcher.find()) {
			characters.add(matcher.group());
		}
		return characters;
	}

	public static String generateRandomString(int len, String languageName)
			throws Throwable {
		String alphabet = getLanguageAlphabet(languageName);
		List<String> characters = getUnicodeStringAsCharList(alphabet);
		// Appium does not type characters beyond standard ASCII set, we have to
		// cut those characters
		if (languageName.toLowerCase().equals(ENGLISH_LANG_NAME)) {
			List<String> ascii_characters = new ArrayList<String>();
			for (String chr : characters) {
				if (chr.length() == 1 && (int) chr.charAt(0) <= 127) {
					ascii_characters.add(chr);
				}
			}
			characters = ascii_characters;
		}
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(characters.get(rnd.nextInt(characters.size())));
		}
		return sb.toString();
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

	public static void generateAdditionalContacts() {
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
			contacts.addLast(user);
		}
	}

	public static void generateUsers(int contactNumber) throws IOException,
			MessagingException {
		for (int i = 0; i < 3; i++) {
			ClientUser user = new ClientUser();
			user.setEmail(CreateZetaUser.registerUserAndReturnMail());
			user.setPassword(getDefaultPasswordFromConfig(CommonUtils.class));
			if (user.getEmail() != null) {
				user.setUserState(UsersState.Created);
				yourUsers.addLast(user);
			} else {
				throw new NullPointerException("User was not created");
			}
		}

		for (int i = 0; i < contactNumber; i++) {
			String contact = CreateZetaUser.registerUserAndReturnMail();
			if (contact != null) {
				ClientUser user = new ClientUser();
				user.setEmail(contact);
				user.setPassword(getDefaultPasswordFromConfig(CommonUtils.class));
				user.setUserState(UsersState.Created);
				contacts.addLast(user);
			} else {
				throw new NullPointerException("Contact was not created");
			}
		}
		generateAdditionalContacts();
	}

	/*
	 * public static void iOSSimulatorCameraRoll() throws IOException,
	 * InterruptedException{
	 * 
	 * String scriptPath = CommonUtils.getPhotoScriptPath(CommonUtils.class);
	 * 
	 * String [] cmd = new String []{"/bin/bash", scriptPath, "7.1"};
	 * 
	 * Process process = Runtime.getRuntime().exec(cmd);
	 * System.out.println("Process Code "+ process.waitFor()); }
	 */

	public static void usePrecreatedUsers() {
		ClientUser contact3 = new ClientUser(
				"smoketester+bbf79363bd3d4ff3ae6a835ed27fe274@wearezeta.com",
				"aqa123456", "34a6a8a88a6e4f9aa1bc77b94ec7ae3a",
				UsersState.AllContactsConnected);
		ClientUser contact2 = new ClientUser(
				"smoketester+34a6a8a88a6e4f9aa1bc77b94ec7ae3a@wearezeta.com",
				"aqa123456", "34a6a8a88a6e4f9aa1bc77b94ec7ae3a",
				UsersState.AllContactsConnected);
		ClientUser contact1 = new ClientUser(
				"smoketester+3e54e65b95cc46608d970b3e949e4773@wearezeta.com",
				"aqa123456", "3e54e65b95cc46608d970b3e949e4773",
				UsersState.AllContactsConnected);
		ClientUser yourUser3 = new ClientUser(
				"smoketester+bbf79363bd3d4ff3ae6a835ed27fe274@wearezeta.com",
				"aqa123456", "bbf79363bd3d4ff3ae6a835ed27fe274",
				UsersState.AllContactsConnected);
		ClientUser yourUser2 = new ClientUser(
				"smoketester+50d287c2407e4c5e8af578979d436c88@wearezeta.com",
				"aqa123456", "50d287c2407e4c5e8af578979d436c88",
				UsersState.AllContactsConnected);
		ClientUser yourUser1 = new ClientUser(
				"smoketester+1f91773deae943948da19b86cd818388@wearezeta.com",
				"aqa123456", "1f91773deae943948da19b86cd818388",
				UsersState.AllContactsConnected);
		yourUsers = new LinkedList<ClientUser>();
		contacts = new LinkedList<ClientUser>();
		yourUsers.addLast(yourUser1);
		yourUsers.addLast(yourUser2);
		yourUsers.addLast(yourUser3);
		contacts.addLast(contact1);
		contacts.addLast(contact2);
		contacts.addLast(contact3);
	}

	public static String getAndroidDeviceNameFromConfig(Class c)
			throws IOException {

		return getValueFromConfig(c, "deviceName");
	}

}
