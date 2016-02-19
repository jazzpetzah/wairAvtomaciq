package com.wearezeta.auto.osx.steps;

import org.apache.log4j.Logger;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.driver.ZetaOSXWebAppDriver;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import static com.wearezeta.auto.osx.common.OSXCommonUtils.clearAppData;
import static com.wearezeta.auto.osx.common.OSXCommonUtils.clearAddressbookPermission;
import static com.wearezeta.auto.osx.common.OSXCommonUtils.getSizeOfAppInMB;
import static com.wearezeta.auto.osx.common.OSXCommonUtils.killAllApps;
import static com.wearezeta.auto.osx.common.OSXCommonUtils.startAppium4Mac;

import com.wearezeta.auto.osx.common.OSXExecutionContext;

import static com.wearezeta.auto.osx.common.OSXExecutionContext.APPIUM_HUB_URL;
import static com.wearezeta.auto.osx.common.OSXExecutionContext.WIRE_APP_PATH;

import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.osx.MainWirePage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.web.pages.RegistrationPage;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.steps.CommonWebAppSteps;

import static com.wearezeta.auto.web.steps.CommonWebAppSteps.log;
import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.RemoteWebDriver;

public class CommonOSXSteps {

	public static final Logger LOG = ZetaLogger.getLog(CommonOSXSteps.class
			.getName());

	private static final String DEFAULT_USER_PICTURE = "/images/aqaPictureContact600_800.jpg";
	private static final int WRAPPER_STARTUP_TIMEOUT_SECONDS = 10;

	private final CommonSteps commonSteps = CommonSteps.getInstance();

	@SuppressWarnings("unused")
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private ChromeDriverService service;

	private final OSXPagesCollection osxPagesCollection = OSXPagesCollection
			.getInstance();
	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	private Future<ZetaWebAppDriver> createWebDriver(
			Future<ZetaOSXDriver> osxDriver) throws IOException {
		final DesiredCapabilities capabilities = new DesiredCapabilities();
		ChromeOptions options = new ChromeOptions();
		// simulate a fake webcam and mic for testing
		options.addArguments("use-fake-device-for-media-stream");
		// allow skipping the security prompt for sharing the media device
		options.addArguments("use-fake-ui-for-media-stream");
		options.addArguments("disable-web-security");
		options.addArguments("env=" + OSXExecutionContext.ENV_URL);
		options.addArguments("enable-logging");
		options.setBinary(WIRE_APP_PATH + OSXExecutionContext.ELECTRON_SUFFIX);

		// allow skipping the security prompt for notifications in chrome 46++
		Map<String, Object> prefs = new HashMap<>();
		prefs.put("profile.managed_default_content_settings.notifications", 1);
		options.setExperimentalOption("prefs", prefs);

		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		capabilities.setCapability("platformName",
				OSXExecutionContext.CURRENT_SECONDARY_PLATFORM.name());

		setExtendedLoggingLevel(capabilities,
				OSXExecutionContext.EXTENDED_LOGGING_LEVEL);

		service = new ChromeDriverService.Builder()
				.usingDriverExecutable(
						new File(OSXExecutionContext.CHROMEDRIVER_PATH))
				.usingAnyFreePort().build();
		service.start();
		final ExecutorService pool = Executors.newFixedThreadPool(1);

		Callable<ZetaWebAppDriver> callableWebAppDriver = () -> new ZetaOSXWebAppDriver(
				service.getUrl(), capabilities, osxDriver.get());

		final Future<ZetaWebAppDriver> lazyWebDriver = pool
				.submit(callableWebAppDriver);
		PlatformDrivers
				.getInstance()
				.getDrivers()
				.put(OSXExecutionContext.CURRENT_SECONDARY_PLATFORM,
						lazyWebDriver);
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
				.put(OSXExecutionContext.CURRENT_PLATFORM, lazyOSXDriver);
		return lazyOSXDriver;
	}

	private static void setExtendedLoggingLevel(
			DesiredCapabilities capabilities, String loggingLevelName) {
		final LoggingPreferences logs = new LoggingPreferences();
		// set it to SEVERE by default
		Level level = Level.ALL;
		try {
			level = Level.parse(loggingLevelName);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			// Just continue with the default logging level
		}
		logs.enable(LogType.BROWSER, level);
		// logs.enable(LogType.CLIENT, Level.ALL);
		// logs.enable(LogType.DRIVER, Level.ALL);
		logs.enable(LogType.PERFORMANCE, Level.ALL);
		// logs.enable(LogType.PROFILER, Level.ALL);
		// logs.enable(LogType.SERVER, Level.ALL);
		capabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);
		log.debug("Browser logging level has been set to '" + level.getName()
				+ "'");
	}

	private void commonBefore() throws Exception {
		try {
			startAppium4Mac();
			killAllApps();
			clearAppData();
			clearAddressbookPermission();
		} catch (Exception e) {
			LOG.error(e);
		}
		startApp();
	}

	private void startApp() throws Exception {
		Future<ZetaOSXDriver> osxDriverFuture;
		Future<ZetaWebAppDriver> webDriverFuture;

		clearDrivers();
		osxDriverFuture = createOSXDriver();
		webDriverFuture = createWebDriver(osxDriverFuture);

		// get drivers instantly
		final ZetaOSXDriver osxDriver = osxDriverFuture.get();
		final ZetaWebAppDriver webappDriver = webDriverFuture.get();
		LOG.debug("Opening app");
		osxDriver.navigate().to(WIRE_APP_PATH);// open app

		ZetaFormatter.setLazyDriver(osxDriverFuture);

		osxPagesCollection.setFirstPage(new MainWirePage(osxDriverFuture));
		MainWirePage mainWirePage = osxPagesCollection
				.getPage(MainWirePage.class);

		LOG.debug("Activating app");
		osxDriver.navigate().to(WIRE_APP_PATH);// activate app
		waitForAppStartup(osxDriver);
		mainWirePage.focusApp();

		waitForWebappLoaded(webappDriver);
		webappPagesCollection
				.setFirstPage(new RegistrationPage(webDriverFuture));
	}

	@Before("@performance")
	public void setUpPerformance() throws Exception {
		commonBefore();
	}

	@Before("~@performance")
	public void setUp() throws Exception {
		commonBefore();
	}

	private void waitForAppStartup(ZetaOSXDriver osxdriver) throws Exception {
		assert DriverUtils.waitUntilLocatorAppears(osxdriver,
				By.xpath(OSXLocators.MainWirePage.xpathWindow),
				WRAPPER_STARTUP_TIMEOUT_SECONDS) : "Application did not started properly";
		LOG.debug("Application started");
	}

	private boolean waitForWebappLoaded(ZetaWebAppDriver webdriver)
			throws Exception {
		boolean started = DriverUtils
				.waitUntilLocatorAppears(
						webdriver,
						By.cssSelector(WebAppLocators.RegistrationPage.cssSwitchToSignInButton
								+ ","
								+ WebAppLocators.ContactListPage.cssSelfProfileAvatar),
						WRAPPER_STARTUP_TIMEOUT_SECONDS);
		if (started) {
			LOG.debug("Wrapper Webapp loaded");
		} else {
			LOG.warn("Wrapper Webapp did not load properly");
		}
		return started;
	}

	/**
	 * This step will throw special PendingException whether the current browser
	 * does support calling or not. This will cause Cucumber interpreter to skip
	 * the current test instead of failing it.
	 *
	 *
	 * @step. ^My browser( does not)? support[s] calling$
	 * @param doesNot
	 *            is set to null if "does not" part does not exist
	 * @throws Exception
	 */
	@Given("^My browser( does not)? support[s]? calling$")
	public void MyBrowserSupportsCalling(String doesNot) throws Exception {
		if (doesNot == null) {
			// should support calling
			if (!WebAppExecutionContext.getBrowser().isSupportingCalls()) {
				throw new PendingException("Browser "
						+ WebAppExecutionContext.getBrowser().toString()
						+ " does not support calling.");
			}
		} else {
			// should not support calling
			if (WebAppExecutionContext.getBrowser().isSupportingCalls()) {
				throw new PendingException(
						"Browser "
								+ WebAppExecutionContext.getBrowser()
										.toString()
								+ " does support calling but this test is just for browsers without support.");
			}
		}
	}

	/**
	 * Creates specified number of users and sets user with specified name as
	 * main user. Avatar picture for Self user is set automatically
	 *
	 * @step. ^There (?:is|are) (\\d+) users? where (.*) is me$
	 *
	 * @param count
	 *            number of users to create
	 * @param myNameAlias
	 *            user name or name alias to use as main user
	 *
	 * @throws Exception
	 */
	@Given("^There (?:is|are) (\\d+) users? where (.*) is me$")
	public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias)
			throws Exception {
		commonSteps.ThereAreNUsersWhereXIsMe(
				OSXExecutionContext.CURRENT_PLATFORM, count, myNameAlias);
		IChangeUserAvatarPicture(myNameAlias, "default");
	}

	/**
	 * Changes the accent color settings of the given user
	 *
	 *
	 * @step. ^User (\\w+) change accent color to
	 *        (StrongBlue|StrongLimeGreen|BrightYellow
	 *        |VividRed|BrightOrange|SoftPink|Violet)$
	 *
	 * @param userNameAlias
	 *            alias of the user where the accent color will be changed
	 * @param newColor
	 *            one of possible accent colors:
	 *            StrongBlue|StrongLimeGreen|BrightYellow
	 *            |VividRed|BrightOrange|SoftPink|Violet
	 *
	 * @throws Exception
	 */
	@Given("^User (\\w+) change accent color to (StrongBlue|StrongLimeGreen|BrightYellow|VividRed|BrightOrange|SoftPink|Violet)$")
	public void IChangeAccentColor(String userNameAlias, String newColor)
			throws Exception {
		commonSteps.IChangeUserAccentColor(userNameAlias, newColor);
	}

	/**
	 * Creates specified number of users and sets user with specified name as
	 * main user. Avatar picture for Self user is NOT set automatically
	 *
	 * @step. ^There (?:is|are) (\\d+) users? where (.*) is me without avatar
	 *        picture$
	 *
	 * @param count
	 *            number of users to create
	 * @param myNameAlias
	 *            user name or name alias to use as main user
	 *
	 * @throws Exception
	 */
	@Given("^There (?:is|are) (\\d+) users? where (.*) is me without avatar picture$")
	public void ThereAreNUsersWhereXIsMeWithoutAvatar(int count,
			String myNameAlias) throws Exception {
		commonSteps.ThereAreNUsersWhereXIsMe(
				OSXExecutionContext.CURRENT_PLATFORM, count, myNameAlias);
	}

	/**
	 * Creates specified number of users and sets user with specified name as
	 * main user. The user is registered with a phone number only and has no
	 * email address attached
	 *
	 * @step. ^There (?:is|are) (\\d+) users? where (.*) is me with phone number
	 *        only$
	 *
	 * @param count
	 *            number of users to create
	 * @param myNameAlias
	 *            user name or name alias to use as main user
	 *
	 * @throws Exception
	 */
	@Given("^There (?:is|are) (\\d+) users? where (.*) is me with phone number only$")
	public void ThereAreNUsersWhereXIsMeWithoutEmail(int count,
			String myNameAlias) throws Exception {
		commonSteps.ThereAreNUsersWhereXIsMeWithPhoneNumberOnly(count, myNameAlias);
	}

	/**
	 * Set avatar picture for a particular user
	 *
	 * @step. ^User (\\w+) changes? avatar picture to (.*)
	 *
	 * @param userNameAlias
	 *            user name/alias
	 * @param path
	 *            path to a picture on a local file system or 'default' to set
	 *            the default picture
	 * @throws Exception
	 */
	@When("^User (\\w+) changes? avatar picture to (.*)")
	public void IChangeUserAvatarPicture(String userNameAlias, String path)
			throws Exception {
		String avatar;
		final String rootPath = "/images/";
		if (path.equals("default")) {
			avatar = DEFAULT_USER_PICTURE;
		} else {
			avatar = rootPath + path;
		}
		URI uri = new URI(CommonWebAppSteps.class.getResource(avatar)
				.toString());
		log.debug("Change avatar of user " + userNameAlias + " to "
				+ uri.getPath());
		commonSteps.IChangeUserAvatarPicture(userNameAlias, uri.getPath());
	}

	/**
	 * Creates connection between to users
	 *
	 * @step. ^(\\w+) is connected to (.*)$
	 *
	 * @param userFromNameAlias
	 *            user which sends connection request
	 * @param usersToNameAliases
	 *            user which accepts connection request
	 *
	 * @throws Exception
	 */
	@Given("^(\\w+) is connected to (.*)$")
	public void UserIsConnectedTo(String userFromNameAlias,
			String usersToNameAliases) throws Exception {
		commonSteps.UserIsConnectedTo(userFromNameAlias, usersToNameAliases);
	}

	/**
	 * Blocks a user
	 *
	 * @step. ^(\\w+) blocked (\\w+)$
	 *
	 * @param userAsNameAlias
	 *            user which wants to block another
	 * @param userToBlockNameAlias
	 *            user to block
	 *
	 * @throws Exception
	 */
	@Given("^(\\w+) blocked (\\w+)$")
	public void UserBlocks(String userAsNameAlias, String userToBlockNameAlias)
			throws Exception {
		commonSteps.BlockContact(userAsNameAlias, userToBlockNameAlias);
	}

	/**
	 * Creates group chat with specified users
	 *
	 * @step. ^(.*) (?:has|have) group chat (.*) with (.*)
	 *
	 * @param chatOwnerNameAlias
	 *            user that creates group chat
	 * @param chatName
	 *            group chat name
	 * @param otherParticipantsNameAlises
	 *            list of users which will be added to chat separated by comma
	 *
	 * @throws Exception
	 */
	@Given("^(.*) (?:has|have) group chat (.*) with (.*)")
	public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
			String chatName, String otherParticipantsNameAlises)
			throws Exception {
		commonSteps.UserHasGroupChatWithContacts(chatOwnerNameAlias, chatName,
				otherParticipantsNameAlises);
	}

	/**
	 * Sets self user to be the current user. Avatar picture for this user is
	 * set automatically
	 *
	 * @step. ^User (\\w+) is [Mm]e$
	 *
	 * @param nameAlias
	 *            user to be set as self user
	 *
	 * @throws Exception
	 */
	@Given("^User (\\w+) is [Mm]e$")
	public void UserXIsMe(String nameAlias) throws Exception {
		commonSteps.UserXIsMe(nameAlias);
		IChangeUserAvatarPicture(nameAlias, "default");
	}

	/**
	 * Sets self user to be the current user. Avatar picture for this user is
	 * NOT set automatically
	 *
	 * @step. ^User (\\w+) is [Mm]e without avatar$
	 *
	 * @param nameAlias
	 *            user to be set as self user
	 *
	 * @throws Exception
	 */
	@Given("^User (\\w+) is [Mm]e without avatar$")
	public void UserXIsMeWithoutAvatar(String nameAlias) throws Exception {
		commonSteps.UserXIsMe(nameAlias);
	}

	/**
	 * Sends connection request by one user to another
	 *
	 * @step. ^(.*) sent connection request to (.*)
	 *
	 * @param userFromNameAlias
	 *            user that sends connection request
	 * @param usersToNameAliases
	 *            user which receive connection request
	 *
	 * @throws Exception
	 */
	@Given("^(.*) sent connection request to (.*)")
	public void GivenConnectionRequestIsSentTo(String userFromNameAlias,
			String usersToNameAliases) throws Throwable {
		commonSteps.ConnectionRequestIsSentTo(userFromNameAlias,
				usersToNameAliases);
	}

	/**
	 * Pings BackEnd until user is indexed and avialable in search
	 *
	 * @step. ^(\\w+) waits? until (.*) exists in backend search results$
	 *
	 * @param searchByNameAlias
	 *            user name to search string
	 *
	 * @param query
	 *            querry string
	 *
	 * @throws Exception
	 */
	@Given("^(\\w+) waits? until (.*) exists in backend search results$")
	public void UserWaitsUntilContactExistsInHisSearchResults(
			String searchByNameAlias, String query) throws Exception {
		commonSteps.WaitUntilContactIsFoundInSearch(searchByNameAlias, query);
	}

	/**
	 * Wait for specified amount of seconds
	 *
	 * @step. ^I wait for (\\d+) seconds?$
	 *
	 * @param seconds
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 */
	@When("^I wait for (\\d+) seconds?$")
	public void WaitForTime(int seconds) throws Exception {
		commonSteps.WaitForTime(seconds);
	}

	/**
	 * Mute conversation
	 *
	 * @step. ^(.*) muted conversation with (.*)$
	 * @param userToNameAlias
	 *            user who want to mute conversation
	 * @param muteUserNameAlias
	 *            conversation or user to be muted
	 * @throws Exception
	 */
	@When("^(.*) muted conversation with (.*)$")
	public void MuteConversationWithUser(String userToNameAlias,
			String muteUserNameAlias) throws Exception {
		commonSteps
				.MuteConversationWithUser(userToNameAlias, muteUserNameAlias);
	}

	/**
	 * Archive conversation on the backend
	 *
	 * @step. ^(.*) archived conversation with (.*)$
	 *
	 * @param userToNameAlias
	 *            the name/alias of conversations list owner
	 * @param archivedUserNameAlias
	 *            the name of conversation to archive
	 * @throws Exception
	 */
	@When("^(.*) archived conversation with (.*)$")
	public void ArchiveConversationWithUser(String userToNameAlias,
			String archivedUserNameAlias) throws Exception {
		commonSteps.ArchiveConversationWithUser(userToNameAlias,
				archivedUserNameAlias);
	}

	/**
	 * Send Ping into a conversation using the backend
	 *
	 * @step. ^User (.*) pinged in the conversation with (.*)$
	 *
	 * @param pingFromUserNameAlias
	 *            conversations list owner name/alias
	 * @param dstConversationName
	 *            the name of conversation to send ping to
	 * @throws Exception
	 */
	@When("^User (.*) pinged in the conversation with (.*)$")
	public void UserPingedConversation(String pingFromUserNameAlias,
			String dstConversationName) throws Exception {
		commonSteps.UserPingedConversation(pingFromUserNameAlias,
				dstConversationName);
	}

	/**
	 * Send Hotping into a conversation using the backend
	 *
	 * @step. ^User (.*) pinged twice in the conversation with (.*)$
	 *
	 * @param pingFromUserNameAlias
	 *            conversations list owner name/alias
	 * @param dstConversationName
	 *            the name of conversation to send ping to
	 * @throws Exception
	 */
	@When("^User (.*) pinged twice in the conversation with (.*)$")
	public void UserHotPingedConversation(String pingFromUserNameAlias,
			String dstConversationName) throws Exception {
		commonSteps.UserHotPingedConversation(pingFromUserNameAlias,
				dstConversationName);
	}

	/**
	 * Send message to a conversation
	 *
	 * @step. ^User (.*) sent message (.*) to conversation (.*)
	 * @param userFromNameAlias
	 *            user who want to mute conversation
	 * @param message
	 *            message to send
	 * @param conversationName
	 *            the name of existing conversation to send the message to
	 * @throws Exception
	 */
	@When("^User (.*) sent message (.*) to conversation (.*)")
	public void UserSentMessageToConversation(String userFromNameAlias,
			String message, String conversationName) throws Exception {
		commonSteps.UserSentMessageToConversation(userFromNameAlias,
				conversationName, message);
	}

	/**
	 * Add one or more of your contacts to the existing group conversation on
	 * the backend
	 *
	 * @step. ^User (.*) added contacts? (.*) to group chat (.*)
	 *
	 * @param asUser
	 *            user name to add as
	 * @param contacts
	 *            the comma separated list of contacts to add
	 * @param conversationName
	 *            conversation name to add contacts to
	 * @throws Exception
	 */
	@Given("^User (.*) added contacts? (.*) to group chat (.*)")
	public void UserXAddedContactsToGroupChat(String asUser, String contacts,
			String conversationName) throws Exception {
		commonSteps.UserXAddedContactsToGroupChat(asUser, contacts,
				conversationName);
	}

	/**
	 * Wait until suggestions are in the backend for a certain user
	 *
	 * @param userNameAlias
	 *            the name of the user
	 * @throws Exception
	 */
	@Given("^There are suggestions for user (.*) on backend$")
	public void suggestions(String userNameAlias) throws Exception {
		commonSteps.WaitUntilSuggestionFound(userNameAlias);
	}

	/**
	 * Add email(s) into address book of a user and upload address book in
	 * backend
	 *
	 * @param asUser
	 *            name of the user where the address book is uploaded
	 * @param emails
	 *            list of email addresses seperated by comma
	 * @throws Exception
	 */
	@Given("^User (.*) has contacts? (.*) in address book")
	public void UserXHasContactsInAddressBook(String asUser, String emails)
			throws Exception {
		commonSteps.UserXHasContactsInAddressBook(asUser, emails);
	}

	/**
	 * Record SHA256-hash of current user profile picture
	 *
	 * @step. (.*) takes? snapshot of current profile picture$
	 *
	 * @param asUser
	 *            user name/alias
	 * @throws Exception
	 */
	@Given("(.*) takes? snapshot of current profile picture$")
	public void UserXTakesSnapshotOfProfilePicture(String asUser)
			throws Exception {
		commonSteps.UserXTakesSnapshotOfProfilePicture(asUser);
	}

	/**
	 * Verify whether current user picture is changed since the last snapshot
	 * was made
	 *
	 * @step. ^I verify that current profile picture snapshot of (.*) differs?
	 *        from the previous one$
	 *
	 * @param userNameAlias
	 *            user name/alias
	 * @throws Exception
	 */
	@Then("^I verify that current profile picture snapshot of (.*) differs? from the previous one$")
	public void UserXVerifiesSnapshotOfProfilePictureIsDifferent(
			String userNameAlias) throws Exception {
		commonSteps
				.UserXVerifiesSnapshotOfProfilePictureIsDifferent(userNameAlias);
	}

	/**
	 * Will throw PendingException if the current browser does not support
	 * synthetic drag and drop
	 *
	 * @step. ^My browser supports synthetic drag and drop$
	 *
	 */
	@Given("^My browser supports synthetic drag and drop$")
	public void MyBrowserSupportsSyntheticDragDrop() {
		if (!WebAppExecutionContext.getBrowser()
				.isSupportingSyntheticDragAndDrop()) {
			throw new PendingException();
		}
	}

	/**
	 * Will click a menu bar item and a menu item within the menu bar item.
	 *
	 * @step. ^I click menu bar item \"(.*)\" and menu item \"(.*)\"$
	 *
	 * @param menuBarItemName
	 * @param menuItemName
	 * @throws java.lang.Exception
	 *
	 */
	@When("^I click menu bar item \"(.*)\" and menu item \"(.*)\"$")
	public void clickMenuBarItem(String menuBarItemName, String menuItemName)
			throws Exception {
		MainWirePage mainPage = osxPagesCollection.getPage(MainWirePage.class);
		mainPage.clickMenuBarItem(menuBarItemName, menuItemName);
	}

	/**
	 * Will click a menu bar item and a menu item within the menu bar item and
	 * another menu item within the menu item.
	 *
	 *
	 * @step ^I click menu bar item \"(.*)\" and menu items \"(.*)\" and
	 *       \"(.*)\"$
	 *
	 * @param menuBarItemName
	 * @param menuItemName
	 * @param menuItemName2
	 * @throws java.lang.Exception
	 *
	 */
	@When("^I click menu bar item \"(.*)\" and menu items \"(.*)\" and \"(.*)\"$")
	public void clickMenuBarItem(String menuBarItemName, String menuItemName,
			String menuItemName2) throws Exception {
		MainWirePage mainPage = osxPagesCollection.getPage(MainWirePage.class);
		mainPage.clickMenuBarItem(menuBarItemName, menuItemName, menuItemName2);
	}

	/**
	 * Will click a menu bar item.
	 *
	 *
	 * @step ^I click menu bar item with name \"(.*)\"$
	 *
	 * @param menuBarItemName
	 * @throws java.lang.Exception
	 *
	 */
	@When("^I click menu bar item with name \"(.*)\"$")
	public void clickMenuBarItem(String menuBarItemName) throws Exception {
		osxPagesCollection.getPage(MainWirePage.class).clickMenuBarItem(
				menuBarItemName);
	}

	/**
	 * Kills the app by cleaning all drivers.
	 *
	 * @step ^I kill the app$
	 *
	 * @throws java.lang.Exception
	 */
	@When("^I kill the app$")
	public void KillApp() throws Exception {
		clearDrivers();
	}

	/**
	 * Kills the app by cleaning all drivers and restarts it
	 *
	 * @step ^I restart the app$
	 *
	 * @throws java.lang.Exception
	 */
	@When("^I restart the app$")
	public void restartApp() throws Exception {
		osxPagesCollection.getPage(MainWirePage.class).pressShortCutForQuit();
		Thread.sleep(2000);
		clearDrivers();
		startApp();
	}

	/**
	 * Verifies app is quit.
	 *
	 * @step ^I verify app has quit$
	 *
	 * @throws java.lang.Exception
	 */
	@Then("^I verify app has quit$")
	public void IVerifyAppHasQuit() throws Exception {
		int exitCode = killAllApps();
		assertEquals(1, exitCode);
	}

	/**
	 * Verifies the size of the installed app.
	 *
	 * @step ^I verify the app is not bigger than (\\d+) MB$
	 *
	 * @param expectedSize
	 * @throws java.lang.Exception
	 *
	 */
	@Then("^I verify the app is not bigger than (\\d+) MB$")
	public void IVerifyAppIsNotTooBig(long expectedSize) throws Exception {
		assertThat(getSizeOfAppInMB(), lessThan(expectedSize));
	}

	@After
	public void tearDown() throws Exception {
		try {
			// async calls/waiting instances cleanup
			CommonCallingSteps2.getInstance().cleanup();
			writeBrowserLogsIntoMainLog(PlatformDrivers.getInstance()
					.getDriver(OSXExecutionContext.CURRENT_SECONDARY_PLATFORM)
					.get());
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
		OSXPagesCollection.getInstance().clearAllPages();
		if (PlatformDrivers.getInstance().hasDriver(
				OSXExecutionContext.CURRENT_PLATFORM)) {
			PlatformDrivers.getInstance().quitDriver(
					OSXExecutionContext.CURRENT_PLATFORM);
		}
		if (PlatformDrivers.getInstance().hasDriver(
				OSXExecutionContext.CURRENT_SECONDARY_PLATFORM)) {
			PlatformDrivers.getInstance().quitDriver(
					OSXExecutionContext.CURRENT_SECONDARY_PLATFORM);
		}
		if (service != null && service.isRunning()) {
			service.stop();
		}
	}

	private List<LogEntry> getBrowserLog(RemoteWebDriver driver) {
		List<LogEntry> logs = new ArrayList<LogEntry>();

		Iterator<LogEntry> browserIter = driver.manage().logs().get(LogType.BROWSER).iterator();
		Iterator<LogEntry> performanceIter = driver.manage().logs().get(LogType.PERFORMANCE).iterator();
		LogEntry performanceEntry = performanceIter.next();
		LogEntry browserEntry = browserIter.next();
		do {
			if (performanceEntry.getTimestamp() < browserEntry.getTimestamp()) {
				logs.add(performanceEntry);
				if (performanceIter.hasNext()) {
					performanceEntry = performanceIter.next();
				} else {
					browserEntry = browserIter.next();
				}
			} else {
				logs.add(browserEntry);
				if (browserIter.hasNext()) {
					browserEntry = browserIter.next();
				} else {
					performanceEntry = performanceIter.next();
				}
			}
		} while (performanceIter.hasNext() || browserIter.hasNext());

		//logs.addAll(driver.manage().logs().get(LogType.BROWSER).getAll());
		//logs.addAll(driver.manage().logs().get(LogType.PERFORMANCE).getAll());
		return logs;
	}

	private void writeBrowserLogsIntoMainLog(RemoteWebDriver driver) {
		List<LogEntry> logEntries = getBrowserLog(driver);
		if (!logEntries.isEmpty()) {
			log.debug("BROWSER CONSOLE LOGS:");
			for (LogEntry logEntry : logEntries) {
				log.debug(logEntry.getTimestamp() + ": [" + logEntry.getLevel()
						+ "] " + logEntry.getMessage());
			}
			log.debug("--- END OF LOG ---");
		}

	}

}
