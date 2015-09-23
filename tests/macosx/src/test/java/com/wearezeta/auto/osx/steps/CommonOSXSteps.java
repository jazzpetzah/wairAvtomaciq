package com.wearezeta.auto.osx.steps;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import static com.wearezeta.auto.osx.common.OSXCommonUtils.clearAppData;
import static com.wearezeta.auto.osx.common.OSXCommonUtils.killAllApps;
import com.wearezeta.auto.osx.common.OSXConstants;
import com.wearezeta.auto.osx.common.OSXExecutionContext;
import static com.wearezeta.auto.osx.common.OSXExecutionContext.APPIUM_HUB_URL;
import static com.wearezeta.auto.osx.common.OSXExecutionContext.CURRENT_PLATFORM;
import static com.wearezeta.auto.osx.common.OSXExecutionContext.CURRENT_SECONDARY_PLATFORM;
import static com.wearezeta.auto.osx.common.OSXExecutionContext.WIRE_APP_PATH;
import com.wearezeta.auto.osx.pages.MainWirePage;
import com.wearezeta.auto.osx.pages.MenuBarPage;
import com.wearezeta.auto.osx.pages.OSXPage;
import com.wearezeta.auto.osx.pages.OSXPagesCollection;
import com.wearezeta.auto.web.pages.LoginPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.RegistrationPage;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

public class CommonOSXSteps {

	private final CommonSteps commonSteps = CommonSteps.getInstance();

	public static final Logger LOG = ZetaLogger.getLog(CommonOSXSteps.class
			.getName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private ChromeDriverService service;

	private final OSXPagesCollection osxPagesCollection = OSXPagesCollection
			.getInstance();
	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	private Future<ZetaWebAppDriver> createWebDriver() throws IOException {
		final DesiredCapabilities capabilities = new DesiredCapabilities();
		ChromeOptions options = new ChromeOptions();
		// simulate a fake webcam and mic for testing
		options.addArguments("use-fake-device-for-media-stream");
		// allow skipping the security prompt for sharing the media device
		options.addArguments("use-fake-ui-for-media-stream");
		options.setBinary(WIRE_APP_PATH + OSXExecutionContext.ELECTRON_SUFFIX);
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		capabilities.setCapability("platformName",
				CURRENT_SECONDARY_PLATFORM.name());

		service = new ChromeDriverService.Builder()
				.usingDriverExecutable(
						new File(OSXExecutionContext.CHROMEDRIVER_PATH))
				.usingAnyFreePort().build();
		service.start();
		final ExecutorService pool = Executors.newFixedThreadPool(1);

		Callable<ZetaWebAppDriver> callableWebAppDriver = () -> new ZetaWebAppDriver(
				service.getUrl(), capabilities);

		final Future<ZetaWebAppDriver> lazyWebDriver = pool
				.submit(callableWebAppDriver);
		PlatformDrivers.getInstance().getDrivers()
				.put(CURRENT_SECONDARY_PLATFORM, lazyWebDriver);
		return lazyWebDriver;
	}

	private Future<ZetaOSXDriver> createOSXDriver()
			throws MalformedURLException {
		final DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
		capabilities.setCapability(CapabilityType.PLATFORM, "MAC");
		capabilities.setCapability("platformName", "Mac");

		final ExecutorService pool = Executors.newFixedThreadPool(1);

		Callable<ZetaOSXDriver> callableOSXDriver = () -> new ZetaOSXDriver(
				new URL(APPIUM_HUB_URL), capabilities);

		final Future<ZetaOSXDriver> lazyOSXDriver = pool
				.submit(callableOSXDriver);
		PlatformDrivers.getInstance().getDrivers()
				.put(CURRENT_PLATFORM, lazyOSXDriver);
		return lazyOSXDriver;
	}

	private void commonBefore() throws Exception {
		killAllApps();
		clearAppData();
		startApp();
	}

	private void startApp() throws Exception {
		final Future<ZetaOSXDriver> osxDriver = createOSXDriver();
		final Future<ZetaWebAppDriver> webDriver = createWebDriver();

		// get drivers instantly
		final ZetaOSXDriver app = osxDriver.get();
		webDriver.get();
		app.navigate().to(WIRE_APP_PATH);

		ZetaFormatter.setLazyDriver(osxDriver);

		osxPagesCollection.setFirstPage(new MainWirePage(osxDriver));
		MainWirePage mainWirePage = osxPagesCollection
				.getPage(MainWirePage.class);
		MenuBarPage menuBarPage = osxPagesCollection.getPage(MenuBarPage.class);

		mainWirePage.focusApp();
		Thread.sleep(3000);// wait for page to load TODO scan for spinner
		menuBarPage.switchEnvironment();
		webappPagesCollection.setFirstPage(new RegistrationPage(webDriver));
		webappPagesCollection.getPage(LoginPage.class);
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
		commonSteps.ThereAreNUsers(CURRENT_PLATFORM, count);
	}

	@Given("^There \\w+ (\\d+) user[s]* where (.*) is me$")
	public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias)
			throws Exception {
		commonSteps.ThereAreNUsersWhereXIsMe(CURRENT_PLATFORM, count,
				myNameAlias);
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
		String imagePath = OSXExecutionContext.USER_HOME + "/" + imageFileName;
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

	@When("^I change user (.*) avatar picture from file (.*)$")
	public void IChangeUserAvatarPictureFromFile(String user, String picture)
			throws Exception {
		String picturePath = OSXExecutionContext.USER_HOME + "/" + picture;
		try {
			user = usrMgr.findUserByNameOrNameAlias(user).getName();
		} catch (NoSuchUserException e) {
			// do nothing
		}
		LOG.debug("Setting avatar for user " + user + " from image "
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
		final Optional<BufferedImage> shot = osxPagesCollection.getPage(
				MainWirePage.class).takeScreenshot();
		if (shot.isPresent()) {
			OSXExecutionContext.SCREENSHOTS.put(screenshotAlias, shot.get());
		} else {
			throw new RuntimeException(
					"Selenium has failed to take screenshot of the current page");
		}
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
		BufferedImage reference = OSXExecutionContext.SCREENSHOTS
				.get(resultAlias);
		BufferedImage template = OSXExecutionContext.SCREENSHOTS
				.get(previousAlias);
		Assert.assertNotNull(reference);
		Assert.assertNotNull(template);
		double score = ImageUtil.getOverlapScore(reference, template,
				ImageUtil.RESIZE_NORESIZE);
		LOG.debug(String.format(
				"Score for comparison of screens %s and %s is %.3f",
				resultAlias, previousAlias, score));
		Assert.assertTrue(String.format(
				"Screens are the same, but expected not to be. "
						+ "Score %.3f >= 0.980d", score), score < 0.98d);
	}

	@When("^I kill the app$")
	public void KillApp() throws Exception {
		clearDrivers();
	}

	@When("^I restart the app$")
	public void restartApp() throws Exception {
		osxPagesCollection.getPage(MainWirePage.class).closeWindow();
		clearDrivers();
		startApp();
	}

	@After
	public void tearDown() throws Exception {
		try {
			// async calls/waiting instances cleanup
			CommonCallingSteps2.getInstance().cleanup();
		} catch (Exception e) {
			// do not fail if smt fails here
			e.printStackTrace();
		}

		commonSteps.getUserManager().resetUsers();
		try {
			osxPagesCollection.getPage(MainWirePage.class).closeWindow();
		} catch (Exception e) {
		}
		clearDrivers();
	}

	private void clearDrivers() throws Exception {
		OSXPage.clearPagesCollection();
		if (PlatformDrivers.getInstance().hasDriver(CURRENT_PLATFORM)) {
			PlatformDrivers.getInstance().quitDriver(CURRENT_PLATFORM);
		}
		if (PlatformDrivers.getInstance().hasDriver(CURRENT_SECONDARY_PLATFORM)) {
			PlatformDrivers.getInstance()
					.quitDriver(CURRENT_SECONDARY_PLATFORM);
		}
		if (service != null && service.isRunning()) {
			service.stop();
		}
	}

}
