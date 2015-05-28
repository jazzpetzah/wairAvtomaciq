package com.wearezeta.auto.android.steps;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.google.common.base.Throwables;
import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.common.reporter.LogcatListener;
import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.CommonCallingSteps;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.InvitationLinkGenerator;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CommonAndroidSteps {
	static {
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

	private static final Logger log = ZetaLogger
			.getLog(CommonAndroidSteps.class.getSimpleName());

	private static String link = null;
	public static LogcatListener listener = new LogcatListener();

	private static ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	private Future<String> passwordResetMessage;
	private ClientUser userToRegister = null;
	private static boolean skipBeforeAfter = false;
	private final CommonSteps commonSteps = CommonSteps.getInstance();
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	public static final Platform CURRENT_PLATFORM = Platform.Android;

	public static final String PATH_ON_DEVICE = "/mnt/sdcard/DCIM/Camera/userpicture.jpg";
	public static final int DEFAULT_SWIPE_TIME = 1500;
	private static final String DEFAULT_USER_AVATAR = "aqaPictureContact600_800.jpg";

	private static String getUrl() throws Exception {
		return CommonUtils
				.getAndroidAppiumUrlFromConfig(CommonAndroidSteps.class);
	}

	private static String getPath() throws Exception {
		return CommonUtils
				.getAndroidApplicationPathFromConfig(CommonAndroidSteps.class);
	}

	@SuppressWarnings("unchecked")
	public Future<ZetaAndroidDriver> resetAndroidDriver(String url,
			String path, boolean isUnicode, Class<?> cls) throws Exception {
		final DesiredCapabilities capabilities = new DesiredCapabilities();
		LoggingPreferences object = new LoggingPreferences();
		object.enable("logcat", Level.ALL);
		capabilities.setCapability(CapabilityType.LOGGING_PREFS, object);
		capabilities.setCapability("platformName", CURRENT_PLATFORM.getName());
		capabilities.setCapability("deviceName",
				CommonUtils.getAndroidDeviceNameFromConfig(cls));
		capabilities.setCapability("app", path);
		capabilities.setCapability("appPackage",
				CommonUtils.getAndroidPackageFromConfig(cls));
		capabilities.setCapability("appActivity",
				CommonUtils.getAndroidActivityFromConfig(cls));
		capabilities.setCapability("appWaitActivity",
				CommonUtils.getAndroidWaitActivitiesFromConfig(cls));
		capabilities.setCapability("applicationName", "selendroid");
		capabilities.setCapability("automationName", "selendroid");

		if (isUnicode) {
			capabilities.setCapability("unicodeKeyboard", true);
			capabilities.setCapability("resetKeyboard", true);
		}

		try {
			return (Future<ZetaAndroidDriver>) PlatformDrivers.getInstance()
					.resetDriver(url, capabilities, 1,
							this::onDriverInitFinished,
							this::onDriverInitStarted);
		} catch (SessionNotCreatedException e) {
			// Unlock the screen and retry
			AndroidCommonUtils.unlockScreen();
			Thread.sleep(5000);
			return (Future<ZetaAndroidDriver>) PlatformDrivers.getInstance()
					.resetDriver(url, capabilities, 1,
							this::onDriverInitFinished,
							this::onDriverInitStarted);
		}
	}

	private Boolean onDriverInitStarted() {
		try {
			AndroidCommonUtils.uploadPhotoToAndroid(PATH_ON_DEVICE);
			AndroidCommonUtils.disableHints();
			AndroidCommonUtils.disableHockeyUpdates();
			String backendJSON = AndroidCommonUtils
					.createBackendJSON(CommonUtils.getBackendType(this
							.getClass()));
			AndroidCommonUtils.deployBackendFile(backendJSON);
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return true;
	}

	private static final int UPDATE_ALERT_VISIBILITY_TIMEOUT = 5; // seconds
	private static final long INTERFACE_INIT_TIMEOUT_MILLISECONDS = 15000;

	private void onDriverInitFinished(RemoteWebDriver drv) {
		final By locator = By
				.xpath(AndroidLocators.CommonLocators.xpathDismissUpdateButton);
		final long millisecondsStarted = System.currentTimeMillis();
		WebDriverException savedException = null;
		do {
			try {
				DriverUtils.waitUntilLocatorIsDisplayed(drv, locator, 1);
				break;
			} catch (WebDriverException e) {
				savedException = e;
				log.debug("Waiting for the views to initialize properly...");
			} catch (Exception e) {
				Throwables.propagate(e);
			}
		} while (System.currentTimeMillis() - millisecondsStarted <= INTERFACE_INIT_TIMEOUT_MILLISECONDS);
		if (System.currentTimeMillis() - millisecondsStarted > INTERFACE_INIT_TIMEOUT_MILLISECONDS) {
			log.error(String
					.format("UI views have not been initialized properly after %s seconds. Restarting Selendroid usually helps ;-)",
							INTERFACE_INIT_TIMEOUT_MILLISECONDS));
			throw savedException;
		}
		try {
			if (DriverUtils.waitUntilLocatorIsDisplayed(drv, locator,
					UPDATE_ALERT_VISIBILITY_TIMEOUT)) {
				drv.findElement(locator).click();
			}
		} catch (Exception e) {
			Throwables.propagate(e);
		}
	}

	private void initFirstPage(boolean isUnicode) throws Exception {
		final Future<ZetaAndroidDriver> lazyDriver = resetAndroidDriver(
				getUrl(), getPath(), isUnicode, this.getClass());
		PagesCollection.loginPage = new LoginPage(lazyDriver);
		ZetaFormatter.setLazyDriver(lazyDriver);
	}

	@Before("@performance")
	public void setUpPerformance() throws Exception {
		listener.startListeningLogcat();

		if (this.isSkipBeforeAfter()) {
			return;
		}
		try {
			AndroidCommonUtils.disableHints();
		} catch (Exception e) {
			e.printStackTrace();
		}
		initFirstPage(false);
	}

	@Before({ "~@unicode", "~@performance" })
	public void setUp() throws Exception {
		if (this.isSkipBeforeAfter()) {
			return;
		}
		commonBefore();
		initFirstPage(false);
	}

	@Before({ "@unicode", "~@performance" })
	public void setUpUnicode() throws Exception {
		if (this.isSkipBeforeAfter()) {
			return;
		}
		commonBefore();
		initFirstPage(true);
	}

	/**
	 * Presses the android back button
	 * 
	 * @step. ^I press back button$
	 * 
	 * @throws IOException
	 * 
	 */
	@When("^I press back button$")
	public void PressBackButton() throws Exception {
		PagesCollection.loginPage.navigateBack();
	}

	/**
	 * Hides the system keyboard
	 * 
	 * @step. ^I hide keyboard$
	 * @throws Exception
	 * 
	 */
	@When("^I hide keyboard$")
	public void IHideKeyboard() throws Exception {
		PagesCollection.loginPage.hideKeyboard();
	}

	@When("^I swipe right$")
	public void ISwipeRight() throws Exception {
		PagesCollection.currentPage.swipeRightCoordinates(DEFAULT_SWIPE_TIME);
	}

	@When("^I swipe left$")
	public void ISwipeLeft() throws Exception {
		PagesCollection.currentPage.swipeLeftCoordinates(DEFAULT_SWIPE_TIME);
	}

	@When("^I swipe up$")
	public void ISwipeUp() throws Exception {
		PagesCollection.currentPage.swipeUpCoordinates(DEFAULT_SWIPE_TIME);
	}

	@When("^I swipe down$")
	public void ISwipeDown() throws Exception {
		PagesCollection.currentPage.swipeDownCoordinates(DEFAULT_SWIPE_TIME);
	}

	public void commonBefore() throws Exception {
		try {
			// async calls/waiting instances cleanup
			CommonCallingSteps.getInstance().cleanupWaitingInstances();
			CommonCallingSteps.getInstance().cleanupCalls();
		} catch (Exception e) {
			// do not fail if smt fails here
			e.printStackTrace();
		}

		ZetaFormatter.setBuildNumber(AndroidCommonUtils
				.readClientVersionFromAdb());
	}

	/**
	 * Sends the application into back stack and displays the home screen.
	 * 
	 * @step. ^I minimize the application$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I minimize the application$")
	public void IMimizeApllication() throws Exception {
		PagesCollection.commonAndroidPage = PagesCollection.loginPage
				.minimizeApplication();
	}

	/**
	 * Locks the device
	 * 
	 * @step. ^I lock the device$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I lock the device$")
	public void ILockTheDevice() throws Exception {
		PagesCollection.loginPage.lockScreen();
	}

	/**
	 * Opens the Browser app
	 * 
	 * -unused
	 * 
	 * @step. ^I open the native browser application$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I open the native browser application$")
	public void IOpenBrowserApp() throws Exception {
		AndroidCommonUtils.openBroswerApplication();
	}

	/**
	 * Opens the gallery application (com.google.android.gallery3d)
	 * 
	 * -unused
	 * 
	 * @step. ^I open the gallery application$
	 * 
	 * @throws Exception
	 * 
	 */

	@When("^I open the gallery application$")
	public void IOpenGalleryApp() throws Exception {
		AndroidCommonUtils.openGalleryApplication();
	}

	/**
	 * Opens the gallery application and shares the default photo to wire
	 * (com.google.android.gallery3d)
	 * 
	 * @step. ^I share image from Gallery to Wire$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I share image from Gallery to Wire$")
	public void IShareImageFromGallery() throws Exception {
		PagesCollection.contactListPage.shareImageToWireFromGallery();
	}

	/**
	 * Opens the Browser app and shares the URL to wire (http://www.google.com)
	 * 
	 * @step. ^I share URL from native browser app to Wire with contact (.*)$
	 * 
	 * @param name
	 *            name of contact to share URL with
	 * @throws Exception
	 * 
	 */
	@When("^I share URL from native browser app to Wire with contact (.*)$")
	public void IShareURLBrowserApp(String name) throws Exception {
		IOpenBrowserApp();
		PagesCollection.contactListPage.shareURLFromNativeBrowser();
		if (PagesCollection.dialogPage == null) {
			PagesCollection.dialogPage = (DialogPage) PagesCollection.currentPage;
		}
		Thread.sleep(5000);
		PagesCollection.dialogPage.sendMessageInInput();
	}

	/**
	 * Takes screenshot for comparison
	 * 
	 * @step. ^I take screenshot$
	 * @throws Exception
	 * 
	 */
	@When("^I take screenshot$")
	public void WhenITake1stScreenshot() throws Exception {
		final Optional<BufferedImage> screenshot = PagesCollection.loginPage
				.takeScreenshot();
		if (screenshot.isPresent()) {
			images.add(screenshot.get());
		} else {
			throw new RuntimeException(
					"Selenium has failed to take the screenshot from current page");
		}
	}

	/**
	 * Taps on the center of the screen
	 * 
	 * @step. ^I tap on center of screen$
	 * 
	 * @throws Throwable
	 * 
	 */
	@When("^I tap on center of screen")
	public void WhenITapOnCenterOfScreen() throws Throwable {
		PagesCollection.currentPage.tapOnCenterOfScreen();
	}

	/**
	 * Compare that 1st and 2nd screenshots are not equal
	 * 
	 * @step. ^I compare 1st and 2nd screenshots and they are different$
	 * 
	 */
	@Then("^I compare 1st and 2nd screenshots and they are different$")
	public void ThenICompare1st2ndScreenshotsAndTheyAreDifferent() {
		double score = ImageUtil.getOverlapScore(images.get(0), images.get(1));
		Assert.assertTrue(score < 0.75d);
		images.clear();
	}

	/**
	 * Restores the application from a minimized state.
	 * 
	 * @step. ^I restore the application$
	 * @throws Exception
	 * 
	 */
	@When("^I restore the application$")
	public void IRestoreApllication() throws Exception {
		PagesCollection.loginPage.restoreApplication();
	}

	/**
	 * Verifies that user A has sent a connection request to user B
	 * 
	 * @step. ^(.*) sent connection request to (.*)$
	 * 
	 * @param userFromNameAlias
	 *            the user from which the connection request originated
	 * @param usersToNameAliases
	 *            the target user
	 * 
	 */
	@Given("^(.*) sent connection request to (.*)$")
	public void GivenConnectionRequestIsSentTo(String userFromNameAlias,
			String usersToNameAliases) throws Throwable {
		commonSteps.ConnectionRequestIsSentTo(userFromNameAlias,
				usersToNameAliases);
	}

	/**
	 * Verifies that user A has an avatar matching the picture in file X
	 * 
	 * @step. ^(.*) has an avatar picture from file (.*)$
	 * 
	 * @param name
	 *            the user to check
	 * @param picture
	 *            the file name of the picture to check against. The file name
	 *            is relative to the pictures directory as defined in the
	 *            Configurations.cnf file
	 * 
	 * @throws Throwable
	 * 
	 */
	@Given("^(.*) has an avatar picture from file (.*)$")
	public void GivenUserHasAnAvatarPicture(String name, String picture)
			throws Throwable {
		String picturePath = CommonUtils
				.getImagesPath(CommonAndroidSteps.class) + "/" + picture;
		try {
			name = usrMgr.findUserByNameOrNameAlias(name).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		commonSteps.IChangeUserAvatarPicture(name, picturePath);
	}

	/**
	 * Verifies that user A has an accent color C
	 * 
	 * @step. ^(.*) has an accent color (.*)$
	 * 
	 * @param name
	 *            the user to check
	 * @param colorName
	 *            the assumed accent color
	 * 
	 * @throws Throwable
	 * 
	 */
	@Given("^(.*) has an accent color (.*)$")
	public void GivenUserHasAnAccentColor(String name, String colorName)
			throws Throwable {
		try {
			name = usrMgr.findUserByNameOrNameAlias(name).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		commonSteps.IChangeUserAccentColor(name, colorName);
	}

	/**
	 * Verifies that user A has the name X
	 * 
	 * @step. ^(.*) has a name (.*)$
	 * 
	 * @param name
	 *            the user to check
	 * @param newName
	 *            the name to check they have
	 * 
	 * @throws Throwable
	 * 
	 */
	@Given("^(.*) has a name (.*)$")
	public void GivenUserHasAName(String name, String newName) throws Throwable {
		try {
			name = usrMgr.findUserByNameOrNameAlias(name).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		commonSteps.IChangeUserName(name, newName);
	}

	/**
	 * Connects current user with a connection link from user B
	 * 
	 * @step. ^I connect using invitation link from (.*)$
	 * 
	 * @param name
	 *            the user from which the connection link came
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I connect using invitation link from (.*)$")
	public void WhenIConnectUsingInvitationLinkFrom(String name)
			throws Exception {
		try {
			BackendAPIWrappers.tryLoginByUser(usrMgr
					.findUserByNameOrNameAlias(name));
			name = usrMgr.findUserByNameOrNameAlias(name).getId();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		String link = InvitationLinkGenerator.getInvitationToken(name);
		PagesCollection.commonAndroidPage.ConnectByInvitationLink(link);

	}

	/**
	 * Open Firefox browser
	 * 
	 * @step. ^I open Firefox$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I open Firefox$")
	public void WhenIOpenFirefox() throws Exception {
		PagesCollection.commonAndroidPage.openFirefoxBrowser();
	}

	/**
	 * Wait for Firefox Url bar
	 * 
	 * @step. ^I wait for Firefox Url bar$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I wait for Firefox Url bar$")
	public void WhenIWaitForFirefoxUrlBar() throws Exception {
		PagesCollection.commonAndroidPage.waitForFireFoxUrlBar();
	}

	/**
	 * Verifies that user A is connected to a given list of users
	 * 
	 * @step. ^(.*) is connected to (.*)$
	 * 
	 * @param userFromNameAlias
	 *            the user to check
	 * @param usersToNameAliases
	 *            A separated list of user names to check to see if connected to
	 *            user A.
	 * 
	 * @throws Exception
	 * 
	 */
	@Given("^(.*) is connected to (.*)$")
	public void UserIsConnectedTo(String userFromNameAlias,
			String usersToNameAliases) throws Exception {
		commonSteps.UserIsConnectedTo(userFromNameAlias, usersToNameAliases);
	}

	/**
	 * Silences a given user from the perspective of the another user through
	 * the backend
	 * 
	 * @step. ^(.*) is silenced to user (.*)$
	 * 
	 * @param mutedUser
	 *            the user to silence
	 * 
	 * @param otherUser
	 *            the user who does the silencing
	 * 
	 * @throws Exception
	 * 
	 */
	@Given("^(.*) is silenced to user (.*)$")
	public void UserIsSilenced(String mutedUser, String otherUser)
			throws Exception {
		mutedUser = usrMgr.findUserByNameOrNameAlias(mutedUser).getName();
		otherUser = usrMgr.findUserByNameOrNameAlias(otherUser).getName();

		commonSteps.MuteConversationWithUser(otherUser, mutedUser);
	}

	/**
	 * Verifies that user A is in a group chat with a group of other users
	 * 
	 * @step. ^(.*) has group chat (.*) with (.*)$
	 * 
	 * @param chatOwnerNameAlias
	 *            the user to check
	 * @param chatName
	 *            The name of the group chat
	 * @param otherParticipantsNameAliases
	 *            A separated list of user names to check to see if in a group
	 *            conversation with user A.
	 * 
	 * @throws Exception
	 * 
	 */
	@Given("^(.*) has group chat (.*) with (.*)$")
	public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
			String chatName, String otherParticipantsNameAliases)
			throws Exception {
		commonSteps.UserHasGroupChatWithContacts(chatOwnerNameAlias, chatName,
				otherParticipantsNameAliases);
	}

	/**
	 * Allows user A to ignore all incoming connection requests
	 * 
	 * @step. ^(.*) ignore all requests$
	 * 
	 * @param userToNameAlias
	 *            the user who will do the ignoring
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^(.*) ignore all requests$")
	public void IgnoreAllIncomingConnectRequest(String userToNameAlias)
			throws Exception {
		commonSteps.IgnoreAllIncomingConnectRequest(userToNameAlias);
	}

	/**
	 * Allows test to wait for T seconds
	 * 
	 * @step. ^I wait for (.*) second[s]*$
	 * 
	 * @param seconds
	 *            The number of seconds to wait
	 * @throws Exception
	 * 
	 */
	@When("^I wait for (\\d+) seconds?$")
	public void WaitForTime(int seconds) throws Exception {
		commonSteps.WaitForTime(seconds);
	}

	/**
	 * User A blocks user B
	 * 
	 * @step. ^User (.*) blocks user (.*)$
	 * 
	 * @param blockAsUserNameAlias
	 *            The user to do the blocking
	 * @param userToBlockNameAlias
	 *            The user to block
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^User (.*) blocks user (.*)$")
	public void BlockContact(String blockAsUserNameAlias,
			String userToBlockNameAlias) throws Exception {
		commonSteps.BlockContact(blockAsUserNameAlias, userToBlockNameAlias);
	}

	/**
	 * User A accepts all requests
	 * 
	 * @step. ^(.*) accept all requests$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^(.*) accept all requests$")
	public void AcceptAllIncomingConnectionRequests(String userToNameAlias)
			throws Exception {
		commonSteps.AcceptAllIncomingConnectionRequests(userToNameAlias);
	}

	/**
	 * User A sends a ping to a conversation
	 * 
	 * @step. ^Contact (.*) ping conversation (.*)$
	 * 
	 * @param pingFromUserNameAlias
	 *            The user to do the pinging
	 * @param dstConversationName
	 *            the target conversation to send the ping to
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^Contact (.*) ping conversation (.*)$")
	public void UserPingedConversation(String pingFromUserNameAlias,
			String dstConversationName) throws Exception {
		commonSteps.UserPingedConversation(pingFromUserNameAlias,
				dstConversationName);
	}

	/**
	 * User A sends a hotping to a conversation
	 * 
	 * @step. ^Contact (.*) hotping conversation (.*)$
	 * 
	 * @param hotPingFromUserNameAlias
	 *            The user to do the hotpinging
	 * @param dstConversationName
	 *            the target converation to send the ping to
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^Contact (.*) hotping conversation (.*)$")
	public void UserHotPingedConversation(String hotPingFromUserNameAlias,
			String dstConversationName) throws Exception {
		commonSteps.UserHotPingedConversation(hotPingFromUserNameAlias,
				dstConversationName);
	}

	/**
	 * Transfers Wire contacts to Mac (Why is this step in the android step
	 * files? - dean).
	 * 
	 * @step. ^I add contacts list users to Mac contacts$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I add contacts list users to Mac contacts$")
	public void AddContactsUsersToMacContacts() throws Exception {
		commonSteps.AddContactsUsersToMacContacts();
	}

	/**
	 * Removes Wire contacts from Mac (Why is this step in the android step
	 * files? - dean).
	 * 
	 * @step. ^I remove contacts list users from Mac contacts$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I remove contacts list users from Mac contacts$")
	public void IRemoveContactsListUsersFromMacContact() throws Exception {
		commonSteps.IRemoveContactsListUsersFromMacContact();
	}

	/**
	 * User A sends a simple text message to user B
	 * 
	 * @step. ^Contact (.*) send message to user (.*)$
	 * 
	 * @param msgFromUserNameAlias
	 *            the user who sends the message
	 * @param dstUserNameAlias
	 *            The user to receive the message
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^Contact (.*) send message to user (.*)$")
	public void UserSendMessageToConversation(String msgFromUserNameAlias,
			String dstUserNameAlias) throws Exception {
		commonSteps.UserSentMessageToUser(msgFromUserNameAlias,
				dstUserNameAlias, CommonUtils.generateRandomString(10));
	}

	/**
	 * Verifies that there are N new users for a test, and makes them if they
	 * don't exist. -unused
	 * 
	 * @step. ^There \\w+ (\\d+) user[s]*$
	 * 
	 * @param count
	 *            the number of users to make
	 * 
	 * @throws Exception
	 * 
	 */
	@Given("^There \\w+ (\\d+) user[s]*$")
	public void ThereAreNUsers(int count) throws Exception {
		commonSteps.ThereAreNUsers(CURRENT_PLATFORM, count);
	}

	/**
	 * Verifies that there are N new users for a test, makes them if they don't
	 * exist, and sets one of those users to be the current user.
	 * 
	 * @step. ^There \\w+ (\\d+) user[s]* where (.*) is me$
	 * 
	 * @param count
	 *            the number of users to make
	 * @param myNameAlias
	 *            the name of the user to set as the current user
	 * @throws Throwable
	 * 
	 */
	@Given("^There \\w+ (\\d+) user[s]* where (.*) is me$")
	public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias)
			throws Throwable {
		commonSteps.ThereAreNUsersWhereXIsMe(CURRENT_PLATFORM, count,
				myNameAlias);
		GivenUserHasAnAvatarPicture(myNameAlias, DEFAULT_USER_AVATAR);
	}

	/**
	 * Verifies that there are N new users for a test all sharing a common
	 * prefix in their names and makes them if they don't exist.
	 * 
	 * @step. ^There \\w+ (\\d+) shared user[s]* with name prefix (\\w+)$
	 * 
	 * @param count
	 *            the number of users to make
	 * @param namePrefix
	 *            the prefix for all of the users to share
	 * 
	 * @throws Exception
	 * 
	 */
	@Given("^There \\w+ (\\d+) shared user[s]* with name prefix (\\w+)$")
	public void ThereAreNSharedUsersWithNamePrefix(int count, String namePrefix)
			throws Exception {
		commonSteps.ThereAreNSharedUsersWithNamePrefix(count, namePrefix);
	}

	/**
	 * Sets the current user to one of the pre-defined users based on the name
	 * of that user.
	 * 
	 * @step. ^User (\\w+) is [Mm]e$
	 * 
	 * @param nameAlias
	 *            the user to set as current user.
	 * 
	 * @throws Exception
	 * 
	 */
	@Given("^User (\\w+) is [Mm]e$")
	public void UserXIsMe(String nameAlias) throws Exception {
		commonSteps.UserXIsMe(nameAlias);
	}

	/**
	 * Waits for a given time to verify that another user exists in search
	 * results
	 * 
	 * @step. ^(\\w+) waits? until (.*) exists in backend search results$
	 * 
	 * @param searchByNameAlias
	 *            the user to search for in the query results.
	 * @param query
	 *            the search query to pass to the backend, which will return a
	 *            list of users.
	 * 
	 * @throws Exception
	 * 
	 */
	@Given("^(\\w+) waits? until (.*) exists in backend search results$")
	public void UserWaitsUntilContactExistsInHisSearchResults(
			String searchByNameAlias, String query) throws Exception {
		commonSteps.WaitUntilContactIsFoundInSearch(searchByNameAlias, query);
	}

	/**
	 * Sends an image from one user to a conversation
	 * 
	 * @step. ^Contact (.*) sends image (.*) to (.*) conversation (.*)$
	 * 
	 * @param imageSenderUserNameAlias
	 *            the user to sending the image
	 * @param imageFileName
	 *            the file path name of the image to send. The path name is
	 *            defined relative to the image file defined in
	 *            Configuration.cnf.
	 * @param conversationType
	 *            "single user" or "group" conversation.
	 * @param dstConversationName
	 *            the name of the conversation to send the image to.
	 *
	 * @throws Exception
	 * 
	 */
	@When("^Contact (.*) sends image (.*) to (.*) conversation (.*)")
	public void ContactSendImageToConversation(String imageSenderUserNameAlias,
			String imageFileName, String conversationType,
			String dstConversationName) throws Exception {
		String imagePath = CommonUtils.getImagesPath(CommonAndroidSteps.class)
				+ imageFileName;
		Boolean isGroup = null;
		if (conversationType.equals("single user")) {
			isGroup = false;
		} else if (conversationType.equals("group")) {
			isGroup = true;
		}
		if (isGroup == null) {
			throw new Exception(
					"Incorrect type of conversation specified (single user | group) expected.");
		}
		commonSteps.UserSendsImageToConversation(imageSenderUserNameAlias,
				imagePath, dstConversationName, isGroup);
	}

	@After
	public void tearDown() throws Exception {
		try {
			// async calls/waiting instances cleanup
			CommonCallingSteps.getInstance().cleanupWaitingInstances();
			CommonCallingSteps.getInstance().cleanupCalls();
		} catch (Exception e) {
			// do not fail if smt fails here
			e.printStackTrace();
		}

		AndroidPage.clearPagesCollection();

		if (PlatformDrivers.getInstance().hasDriver(CURRENT_PLATFORM)) {
			PlatformDrivers.getInstance().quitDriver(CURRENT_PLATFORM);
		}

		commonSteps.getUserManager().resetUsers();
	}

	public boolean isSkipBeforeAfter() {
		return skipBeforeAfter;
	}

	public void setSkipBeforeAfter(boolean skipBeforeAfter) {
		CommonAndroidSteps.skipBeforeAfter = skipBeforeAfter;
	}

	/**
	 * Resets the password for the given email address
	 * 
	 * @param email
	 *            the email associated to the account
	 * @throws Exception
	 */

	@When("^I request reset password for (.*)$")
	public void WhenIRequestResetPassword(String email) throws Exception {
		try {
			email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.commonAndroidPage.requestResetPassword(email);
	}

	/**
	 * Resets the password for a given user's account to a newly defined
	 * password
	 * 
	 * @step. ^I reset (.*) password by URL to new (.*)$
	 * 
	 * @param newPass
	 *            the new password.
	 * 
	 * @throws Exception
	 * 
	 */
	@Then("^I reset password by URL to new (.*)$")
	public void WhenIResetPasswordByUrl(String newPass) throws Exception {
		PagesCollection.peoplePickerPage = PagesCollection.commonAndroidPage
				.resetByLink(link, newPass);
	}

	/**
	 * Get new password link from mail
	 * 
	 * @step. ^I get new password link$
	 * 
	 * @param name
	 *            the name of the user for which you want to reset the password.
	 * @throws Exception
	 */
	@Then("^I get new (.*) password link$")
	public void ThenIGetNewPaswordLink(String name) throws Exception {
		try {
			this.userToRegister = usrMgr.findUserByNameOrNameAlias(name);
		} catch (NoSuchUserException e) {
			if (this.userToRegister == null) {
				this.userToRegister = new ClientUser();
			}
			this.userToRegister.setName(name);
			this.userToRegister.addNameAlias(name);
		}
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", this.userToRegister.getEmail());
		this.passwordResetMessage = IMAPSMailbox.getInstance().getMessage(
				expectedHeaders, BackendAPIWrappers.UI_ACTIVATION_TIMEOUT);

		link = BackendAPIWrappers
				.getPasswordResetLink(this.passwordResetMessage);
	}

	/**
	 * Activates user using browser by URL from mail
	 * 
	 * @step. ^I activate user by URL$
	 * 
	 * @throws Exception
	 */
	@Then("^I activate user by URL$")
	public void WhenIActivateUserByUrl() throws Exception {
		String link = BackendAPIWrappers
				.getUserActivationLink(RegistrationPageSteps.activationMessage);
		PagesCollection.peoplePickerPage = PagesCollection.commonAndroidPage
				.activateByLink(link);
	}

	/**
	 * Rotate device to landscape
	 * 
	 * @step. ^I rotate UI to landscape$
	 * 
	 * @throws Exception
	 */
	@When("^I rotate UI to landscape$")
	public void WhenIRotateUILandscape() throws Exception {
		PagesCollection.loginPage.rotateLandscape();
	}

	/**
	 * Rotate device to portrait
	 * 
	 * @step. ^I rotate UI to portrait$
	 * 
	 * @throws Exception
	 */
	@When("^I rotate UI to portrait$")
	public void WhenIRotateUIPortrait() throws Exception {
		PagesCollection.loginPage.rotatePortrait();
	}

}
