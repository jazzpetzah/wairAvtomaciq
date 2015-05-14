package com.wearezeta.auto.osx.steps;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.wearezeta.auto.common.CommonCallingSteps;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.common.OSXConstants;
import com.wearezeta.auto.osx.common.OSXExecutionContext;
import com.wearezeta.auto.osx.pages.OSXPage;
import com.wearezeta.auto.osx.pages.PagesCollection;
import com.wearezeta.auto.osx.pages.common.MainMenuAndDockPage;
import com.wearezeta.auto.osx.pages.common.ProblemReportPage;
import com.wearezeta.auto.osx.pages.welcome.LoginPage;
import com.wearezeta.auto.osx.pages.welcome.WelcomePage;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CommonOSXSteps {

	private final CommonSteps commonSteps = CommonSteps.getInstance();

	public static final Logger log = ZetaLogger.getLog(CommonOSXSteps.class
			.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	public static final Platform CURRENT_PLATFORM = Platform.Mac;

	private static final int MAX_DRIVER_CREATION_RETRIES = 3;

	private Date testStartedTimestamp;

	private long startupTime = -1;

	private static boolean backendSet = false;

	public long getStartupTime() {
		return this.startupTime;
	}

	static {
		// for Jenkins slaves we should define that environment has display
		CommonUtils.defineNoHeadlessEnvironment();
		// disabling selenium logs to exclude not used output from log
		CommonUtils.disableSeleniumLogs();
	}

	public static boolean resetBackendSettingsIfOverwritten()
			throws IOException, Exception {
		OSXCommonUtils.resetOSXPrefsDaemon();
		if (!OSXCommonUtils.isBackendTypeSet(CommonUtils
				.getBackendType(CommonOSXSteps.class))) {
			log.debug("Backend setting were overwritten. Trying to restart app.");
			OSXCommonUtils.killWireIfStuck();
			OSXCommonUtils.setZClientBackendAndDisableStartUI(CommonUtils
					.getBackendType(CommonOSXSteps.class));
			OSXCommonUtils.resetOSXPrefsDaemon();
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	private Future<ZetaOSXDriver> resetOSXDriver(String url) throws Exception {
		final DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
		capabilities.setCapability(CapabilityType.PLATFORM, CURRENT_PLATFORM
				.getName().toUpperCase());
		capabilities.setCapability("platformName", CURRENT_PLATFORM.getName());

		return (Future<ZetaOSXDriver>) PlatformDrivers.getInstance()
				.resetDriver(url, capabilities, MAX_DRIVER_CREATION_RETRIES,
						this::startApp);
	}

	private void startApp(RemoteWebDriver drv) {
		try {
			CommonUtils.enableTcpForAppName(OSXConstants.Apps.WIRE);
		} catch (Exception e) {
		}
		try {
			OSXCommonUtils.deleteWireLoginFromKeychain();
		} catch (Exception e) {
		}
		try {
			OSXCommonUtils.deletePreferencesFile();
		} catch (Exception e) {
		}
		try {
			OSXCommonUtils.deleteCacheFolder();
		} catch (Exception e) {
		}

		if (!backendSet) {
			try {
				OSXCommonUtils.removeAllZClientSettingsFromDefaults();
			} catch (Exception e) {
			}
			try {
				OSXCommonUtils.setZClientBackendAndDisableStartUI(CommonUtils
						.getBackendType(this.getClass()));
			} catch (Exception e) {
			}
			backendSet = true;
		}
		drv.navigate().to(OSXExecutionContext.wirePath);

		try {
			if (resetBackendSettingsIfOverwritten()) {
				drv.navigate().to(OSXExecutionContext.wirePath);
			}
		} catch (Exception e) {
		}
	}

	private void commonBefore() throws Exception {
		try {
			// async calls/waiting instances cleanup
			CommonCallingSteps.getInstance().cleanupWaitingInstances();
			CommonCallingSteps.getInstance().cleanupCalls();
		} catch (Exception e) {
			// do not fail if smt fails here
			e.printStackTrace();
		}

		this.testStartedTimestamp = new Date();
		final Future<ZetaOSXDriver> lazyDriver = resetOSXDriver(OSXExecutionContext.appiumUrl);

		PagesCollection.mainMenuPage = new MainMenuAndDockPage(lazyDriver);
		PagesCollection.welcomePage = new WelcomePage(lazyDriver);
		PagesCollection.loginPage = new LoginPage(lazyDriver);
		PagesCollection.problemReportPage = new ProblemReportPage(lazyDriver);

		ZetaFormatter.setLazyDriver(lazyDriver);
		// saving time of startup for Sync Engine
		this.startupTime = new Date().getTime()
				- this.testStartedTimestamp.getTime();
	}

	@Before("@performance")
	public void setUpPerformance() throws Exception {
		commonBefore();
	}

	@Before("~@performance")
	public void setUp() throws Exception {
		commonBefore();
	}

	@Given("^(.*) sent connection request to (.*)$")
	public void GivenConnectionRequestIsSentTo(String userFromNameAlias,
			String usersToNameAliases) throws Throwable {
		commonSteps.ConnectionRequestIsSentTo(userFromNameAlias,
				usersToNameAliases);
	}

	@Given("^(.*) has group chat (.*) with (.*)$")
	public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
			String chatName, String otherParticipantsNameAlises)
			throws Exception {
		commonSteps.UserHasGroupChatWithContacts(chatOwnerNameAlias, chatName,
				otherParticipantsNameAlises);
	}

	@Given("^(.*) is connected to (.*)$")
	public void UserIsConnectedTo(String userFromNameAlias,
			String usersToNameAliases) throws Exception {
		commonSteps.UserIsConnectedTo(userFromNameAlias, usersToNameAliases);
	}

	@Given("^There \\w+ (\\d+) user[s]*$")
	public void ThereAreNUsers(int count) throws Exception {
		commonSteps.ThereAreNUsers(Platform.Mac, count);
	}

	@Given("^There \\w+ (\\d+) user[s]* where (.*) is me$")
	public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias)
			throws Exception {
		commonSteps.ThereAreNUsersWhereXIsMe(Platform.Mac, count, myNameAlias);
	}

	@When("^(.*) ignore all requests$")
	public void IgnoreAllIncomingConnectRequest(String userToNameAlias)
			throws Exception {
		commonSteps.IgnoreAllIncomingConnectRequest(userToNameAlias);
	}

	@When("^I wait for (\\d+) seconds?$")
	public void WaitForTime(int seconds) throws Exception {
		commonSteps.WaitForTime(seconds);
	}

	@When("^User (.*) blocks user (.*)$")
	public void BlockContact(String blockAsUserNameAlias,
			String userToBlockNameAlias) throws Exception {
		commonSteps.BlockContact(blockAsUserNameAlias, userToBlockNameAlias);
	}

	@When("^User (.*) unblocks user (.*)$")
	public void UnblockContact(String unblockAsUserNameAlias,
			String userToUnblockNameAlias) throws Exception {
		commonSteps.UnblockContact(unblockAsUserNameAlias,
				userToUnblockNameAlias);
	}

	@When("^(.*) accept all requests$")
	public void AcceptAllIncomingConnectionRequests(String userToNameAlias)
			throws Exception {
		commonSteps.AcceptAllIncomingConnectionRequests(userToNameAlias);
	}

	@When("^Contact (.*) ping conversation (.*)$")
	public void UserPingedConversation(String pingFromUserNameAlias,
			String dstConversationName) throws Exception {
		commonSteps.UserPingedConversation(pingFromUserNameAlias,
				dstConversationName);
	}

	@When("^Contact (.*) hotping conversation (.*)$")
	public void UserHotPingedConversation(String hotPingFromUserNameAlias,
			String dstConversationName) throws Exception {
		commonSteps.UserHotPingedConversation(hotPingFromUserNameAlias,
				dstConversationName);
	}

	@When("^Contact (.*) sends image (.*) to (.*) conversation (.*)")
	public void ContactSendImageToConversation(String imageSenderUserNameAlias,
			String imageFileName, String conversationType,
			String dstConversationName) throws Exception {
		String imagePath = OSXExecutionContext.userDocuments + "/"
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

	@When("^I add contacts list users to Mac contacts$")
	public void AddContactsUsersToMacContacts() throws Exception {
		commonSteps.AddContactsUsersToMacContacts();
	}

	@When("^I remove contacts list users from Mac contacts$")
	public void IRemoveContactsListUsersFromMacContact() throws Exception {
		commonSteps.IRemoveContactsListUsersFromMacContact();
	}

	@When("^I change user (.*) avatar picture from file (.*)$")
	public void IChangeUserAvatarPictureFromFile(String user, String picture)
			throws Exception {
		String picturePath = OSXExecutionContext.userDocuments + "/" + picture;
		try {
			user = usrMgr.findUserByNameOrNameAlias(user).getName();
		} catch (NoSuchUserException e) {
			// do nothing
		}
		log.debug("Setting avatar for user " + user + " from image "
				+ picturePath);
		commonSteps.IChangeUserAvatarPicture(user, picturePath);
	}

	@Given("^There \\w+ (\\d+) shared user[s]* with name prefix (\\w+)$")
	public void ThereAreNSharedUsersWithNamePrefix(int count, String namePrefix)
			throws Exception {
		commonSteps.ThereAreNSharedUsersWithNamePrefix(count, namePrefix);
	}

	@Given("^User (\\w+) is [Mm]e$")
	public void UserXIsMe(String nameAlias) throws Exception {
		commonSteps.UserXIsMe(nameAlias);
	}

	@Given("^Internet connection is lost$")
	public void InternetConnectionIsLost() throws Exception {
		commonSteps.BlockTcpConnectionForApp(OSXConstants.Apps.WIRE);
	}

	@Given("^Internet connection is restored$")
	public void InternetConnectionIsRestored() throws Exception {
		commonSteps.EnableTcpConnectionForApp(OSXConstants.Apps.WIRE);
	}

	@Given("^(\\w+) waits? until (.*) exists in backend search results$")
	public void UserWaitsUntilContactExistsInHisSearchResults(
			String searchByNameAlias, String query) throws Exception {
		commonSteps.WaitUntilContactIsFoundInSearch(searchByNameAlias, query);
	}

	@When("^Contact (.*) sends random message to conversation (.*)")
	public void ContactSendsRandomMessageToConversation(
			String msgFromUserNameAlias, String dstUserNameAlias)
			throws Exception {
		String message = UUID.randomUUID().toString();
		ContactSendsMessageToConversation(msgFromUserNameAlias, message,
				dstUserNameAlias);
	}

	@When("^Contact (.*) sends message (.*) to conversation (.*)$")
	public void ContactSendsMessageToConversation(String msgFromUserNameAlias,
			String message, String dstUserNameAlias) throws Exception {
		commonSteps.UserSentMessageToUser(msgFromUserNameAlias,
				dstUserNameAlias, message);
	}

	@When("^Contact (.*) posts messages and media link (.*) to conversation (.*)$")
	public void ContactPostsMessagesAndMediaLink(String msgFromUserNameAlias,
			String mediaLink, String dstUserNameAlias) throws Exception {
		final int RANDOM_MESSAGE_COUNT = 20;
		for (int i = 0; i <= RANDOM_MESSAGE_COUNT; i++) {
			ContactSendsRandomMessageToConversation(msgFromUserNameAlias,
					dstUserNameAlias);
			Thread.sleep(500);
		}
		ContactSendsMessageToConversation(msgFromUserNameAlias, mediaLink,
				dstUserNameAlias);
	}

	/**
	 * Changes accent color of specified user to one from the list using REST
	 * API
	 * 
	 * @step. ^User (\\w+) changes accent color to
	 *        (StrongBlue|StrongLimeGreen|BrightYellow
	 *        |VividRed|BrightOrange|SoftPink|Violet)$
	 * 
	 * @param userNameAlias
	 *            user name
	 * @param newColor
	 *            one of possible accent colors:
	 *            StrongBlue|StrongLimeGreen|BrightYellow
	 *            |VividRed|BrightOrange|SoftPink|Violet
	 * 
	 * @throws Exception
	 */
	@When("^User (\\w+) changes accent color to (StrongBlue|StrongLimeGreen|BrightYellow|VividRed|BrightOrange|SoftPink|Violet)$")
	public void IChangeAccentColor(String userNameAlias, String newColor)
			throws Exception {
		commonSteps.IChangeUserAccentColor(userNameAlias, newColor);
	}

	/**
	 * Stores screenshot of the whole screen for usage during execution by
	 * specified alias
	 * 
	 * @step. ^I take fullscreen shot and save it as (.*)$
	 * 
	 * @param screenshotAlias
	 *            string id for stored screenshot
	 * @throws Exception
	 */
	@When("^I take fullscreen shot and save it as (.*)$")
	public void ITakeFullscreenShotAndSaveItAsAlias(String screenshotAlias)
			throws Exception {
		BufferedImage shot = PagesCollection.mainMenuPage.takeScreenshot();
		OSXExecutionContext.screenshots.put(screenshotAlias, shot);
	}

	/**
	 * Compares screenshots stored with specified aliases
	 * 
	 * @step. ^I see that screen (.*) is changed in comparison with (.*)$
	 * 
	 * @param resultAlias
	 *            current screen appearance
	 * @param previousAlias
	 *            screen stored before
	 * 
	 * @throws AssertionError
	 *             if screenshots are similar enough
	 */
	@Then("^I see that screen (.*) is changed in comparison with (.*)$")
	public void ISeeScreensAreDifferent(String resultAlias, String previousAlias) {
		BufferedImage reference = OSXExecutionContext.screenshots
				.get(resultAlias);
		BufferedImage template = OSXExecutionContext.screenshots
				.get(previousAlias);
		Assert.assertNotNull(reference);
		Assert.assertNotNull(template);
		double score = ImageUtil.getOverlapScore(reference, template,
				ImageUtil.RESIZE_NORESIZE);
		log.debug(String.format(
				"Score for comparison of screens %s and %s is %.3f",
				resultAlias, previousAlias, score));
		Assert.assertTrue(String.format(
				"Screens are the same, but expected not to be. "
						+ "Score %.3f >= 0.980d", score), score < 0.98d);
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

		OSXCommonUtils.collectSystemLogs(testStartedTimestamp);

		OSXPage.clearPagesCollection();

		commonSteps.getUserManager().resetUsers();

		// workaround for stuck on Send picture test
		OSXCommonUtils.killWireIfStuck();

		CommonUtils.enableTcpForAppName(OSXConstants.Apps.WIRE);

		if (PlatformDrivers.getInstance().hasDriver(CURRENT_PLATFORM)) {
			PlatformDrivers.getInstance().quitDriver(CURRENT_PLATFORM);
		}
	}

}
