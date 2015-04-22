package com.wearezeta.auto.web.steps;

import java.util.logging.Level;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.CommonCallingSteps;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.PerformanceCommon;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.common.WebAppConstants.Browser;
import com.wearezeta.auto.web.pages.InvitationCodePage;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.WebPage;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class CommonWebAppSteps {
	private final CommonSteps commonSteps = CommonSteps.getInstance();

	public static final Logger log = ZetaLogger.getLog(CommonWebAppSteps.class
			.getSimpleName());

	public static final Platform CURRENT_PLATFORM = Platform.Web;
	private static final int MAX_DRIVER_CREATION_RETRIES = 3;
	private static final int MIN_WEBAPP_WINDOW_WIDTH = 1366;
	private static final int MIN_WEBAPP_WINDOW_HEIGHT = 768;

	private static final String DEFAULT_USER_PICTURE = PerformanceCommon.DEFAULT_PERF_IMAGE;

	static {
		System.setProperty("java.awt.headless", "false");
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

	private static void setCustomChromeProfile(
			DesiredCapabilities capabilities, String browserPlatform)
			throws Exception {
		ChromeOptions options = new ChromeOptions();
		// simulate a fake webcam and mic for testing
		options.addArguments("use-fake-device-for-media-stream");
		// allow skipping the security prompt for sharing the media device
		options.addArguments("use-fake-ui-for-media-stream");
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
	}

	private static void setCustomOperaProfile(DesiredCapabilities capabilities,
			String browserPlatform) throws Exception {
		final String userProfileRoot = WebCommonUtils
				.getOperaProfileRoot(browserPlatform);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("user-data-dir=" + userProfileRoot);
		// simulate a fake webcam and mic for testing
		options.addArguments("use-fake-device-for-media-stream");
		// allow skipping the security prompt for sharing the media device
		options.addArguments("use-fake-ui-for-media-stream");
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
	}

	private static void setCustomFirefoxProfile(DesiredCapabilities capabilities) {
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("dom.webnotifications.enabled", false);
		// allow skipping the security prompt for sharing the media device
		profile.setPreference("media.navigator.permission.disabled", true);
		capabilities.setCapability("firefox_profile", profile);
	}

	private static void setCustomSafariProfile(DesiredCapabilities capabilities) {
		SafariOptions options = new SafariOptions();
		options.setUseCleanSession(true);
		capabilities.setCapability(SafariOptions.CAPABILITY, options);
	}

	private static void setExtendedLoggingLevel(
			DesiredCapabilities capabilities, String loggingLevelName) {
		final LoggingPreferences logs = new LoggingPreferences();
		// set it to SEVERE by default
		Level level = Level.SEVERE;
		try {
			level = Level.parse(loggingLevelName);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			// Just continue with the default logging level
		}
		logs.enable(LogType.BROWSER, level);
		// logs.enable(LogType.CLIENT, Level.ALL);
		// logs.enable(LogType.DRIVER, Level.ALL);
		// logs.enable(LogType.PERFORMANCE, Level.ALL);
		// logs.enable(LogType.PROFILER, Level.ALL);
		// logs.enable(LogType.SERVER, Level.ALL);
		capabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);
		log.debug("Browser logging level has been set to '" + level.getName()
				+ "'");
	}

	private ZetaWebAppDriver resetWebAppDriver(String url) throws Exception {
		final DesiredCapabilities capabilities;
		switch (WebAppExecutionContext.getCurrentBrowser()) {
		case Chrome:
			capabilities = DesiredCapabilities.chrome();
			setCustomChromeProfile(capabilities,
					WebAppExecutionContext.getCurrentPlatform());
			break;
		case Opera:
			capabilities = DesiredCapabilities.chrome();
			setCustomOperaProfile(capabilities,
					WebAppExecutionContext.getCurrentPlatform());
			break;
		case Firefox:
			capabilities = DesiredCapabilities.firefox();
			// This is to fix Desktop Notifications alert appearance in
			// Firefox
			setCustomFirefoxProfile(capabilities);
			break;
		case Safari:
			capabilities = DesiredCapabilities.safari();
			setCustomSafariProfile(capabilities);
			break;
		case InternetExplorer:
			capabilities = DesiredCapabilities.internetExplorer();
			break;
		default:
			throw new NotImplementedException(
					"Incorrect browser name is set - "
							+ WebAppExecutionContext.getCurrentBrowser()
									.toString()
							+ ". Please choose one of the following: chrome | firefox | safari | ie");
		}
		if (WebAppExecutionContext.getCurrentPlatform().length() > 0) {
			// Use undocumented grid property to match platforms
			// https://groups.google.com/forum/#!topic/selenium-users/PRsEBcbpNlM
			capabilities.setCapability("applicationName",
					WebAppExecutionContext.getCurrentPlatform());
		}

		if (WebAppExecutionContext.LoggingManagement
				.isSupportedInCurrentBrowser()) {
			setExtendedLoggingLevel(capabilities,
					WebCommonUtils.getExtendedLoggingLevelInConfig(getClass()));
		}

		// This could useful for testing on your local machine running Opera
		// Do not forget to set real user name in profile path instead of
		// default one
		// setCustomOperaProfile(capabilities, "win7_opera");

		capabilities.setCapability("platformName", Platform.Web.getName());
		final ZetaWebAppDriver webDriver = (ZetaWebAppDriver) PlatformDrivers
				.getInstance().resetDriver(url, capabilities);
		webDriver.setFileDetector(new LocalFileDetector());
		return webDriver;
	}

	@Before("~@performance")
	public void setUp() throws Exception {
		try {
			// async calls/waiting instances cleanup
			CommonCallingSteps.getInstance().cleanupWaitingInstances();
			CommonCallingSteps.getInstance().cleanupCalls();
		} catch (Exception e) {
			// do not fail if smt fails here
			e.printStackTrace();
		}

		final String url = CommonUtils
				.getWebAppAppiumUrlFromConfig(CommonWebAppSteps.class);
		final String path = CommonUtils
				.getWebAppApplicationPathFromConfig(CommonWebAppSteps.class);
		int tryNum = 1;
		ZetaWebAppDriver webDriver;
		WebDriverWait wait;
		do {
			try {
				webDriver = resetWebAppDriver(url);
				wait = PlatformDrivers.createDefaultExplicitWait(webDriver);
				if (WebAppExecutionContext.getCurrentBrowser() == Browser.InternetExplorer) {
					// http://stackoverflow.com/questions/14373371/ie-is-continously-maximizing-and-minimizing-when-test-suite-executes
					webDriver.manage().window().setPosition(new Point(0, 0));
					webDriver
							.manage()
							.window()
							.setSize(
									new Dimension(MIN_WEBAPP_WINDOW_WIDTH,
											MIN_WEBAPP_WINDOW_HEIGHT));
				} else {
					webDriver.manage().window().maximize();
				}

				PagesCollection.invitationCodePage = new InvitationCodePage(
						webDriver, wait, path);
				PagesCollection.invitationCodePage.navigateTo();
				break;
			} catch (WebDriverException e) {
				log.debug(String
						.format("Driver initialization failed. Trying to recreate (%d of %d)...",
								tryNum, MAX_DRIVER_CREATION_RETRIES));
				e.printStackTrace();
				if (tryNum >= MAX_DRIVER_CREATION_RETRIES) {
					throw e;
				} else {
					tryNum++;
				}
			}
		} while (tryNum <= MAX_DRIVER_CREATION_RETRIES);
		ZetaFormatter.setDriver(PagesCollection.invitationCodePage.getDriver());
	}

	/**
	 * This step will throw special PendingException if the current browser does
	 * not support calling. This will cause Cucumber interpreter to skip the
	 * current test instead of failing it
	 * 
	 * @throws Exception
	 */
	@Given("^My browser supports calling$")
	public void MyBrowserSupportsCalling() throws Exception {
		if (!WebAppExecutionContext.Calling.isSupportedInCurrentBrowser()) {
			throw new PendingException("Browser "
					+ WebAppExecutionContext.getCurrentBrowser().toString()
					+ " does not support calling.");
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
		commonSteps.ThereAreNUsersWhereXIsMe(count, myNameAlias);
		IChangeUserAvatarPicture(myNameAlias, "default");
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
		commonSteps.ThereAreNUsersWhereXIsMe(count, myNameAlias);
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
		String avatar = null;
		final String rootPath = CommonUtils
				.getWebAppImagesPathFromConfig(getClass());
		if (path.equals("default")) {
			avatar = DEFAULT_USER_PICTURE;
		} else {
			avatar = rootPath + "/" + path;
		}
		commonSteps.IChangeUserAvatarPicture(userNameAlias, avatar);
	}

	/**
	 * Creates connection between to users
	 * 
	 * @step. ^(.*) is connected to (.*)
	 * 
	 * @param userFromNameAlias
	 *            user which sends connection request
	 * @param usersToNameAliases
	 *            user which accepts connection request
	 * 
	 * @throws Exception
	 */
	@Given("^(.*) is connected to (.*)")
	public void UserIsConnectedTo(String userFromNameAlias,
			String usersToNameAliases) throws Exception {
		commonSteps.UserIsConnectedTo(userFromNameAlias, usersToNameAliases);
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
	 * @step. ^(.*) (?:has|have) sent connection request to (.*)
	 * 
	 * @param userFromNameAlias
	 *            user that sends connection request
	 * @param usersToNameAliases
	 *            user which receive connection request
	 *
	 * @throws Exception
	 */
	@Given("^(.*) (?:has|have) sent connection request to (.*)")
	public void GivenConnectionRequestIsSentTo(String userFromNameAlias,
			String usersToNameAliases) throws Throwable {
		commonSteps.ConnectionRequestIsSentTo(userFromNameAlias,
				usersToNameAliases);
	}

	/**
	 * Pings BackEnd until user is indexed and avialable in search
	 * 
	 * @step. ^(\\w+) waits? up to (\\d+) seconds? until (.*) exists in backend
	 *        search results$
	 * 
	 * @param searchByNameAlias
	 *            user name to search string
	 * 
	 * @param timeout
	 *            max ping timeout in sec
	 * 
	 * @param query
	 *            querry string
	 * 
	 * @throws Exception
	 */
	@Given("^(\\w+) waits? up to (\\d+) seconds? until (.*) exists in backend search results$")
	public void UserWaitsUntilContactExistsInHisSearchResults(
			String searchByNameAlias, int timeout, String query)
			throws Exception {
		commonSteps.WaitUntilContactIsFoundInSearch(searchByNameAlias, query,
				timeout);
	}

	/**
	 * Wait for specified amount of seconds
	 * 
	 * @step. ^I wait for (.*) seconds?$
	 * 
	 * @param seconds
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 */
	@When("^I wait for (.*) seconds?$")
	public void WaitForTime(String seconds) throws NumberFormatException,
			InterruptedException {
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
	 * Send message to a conversation
	 * 
	 * @step. ^User (.*) sent message (.*) to conversation (.*)
	 * @param userToNameAlias
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

	private void writeBrowserLogsIntoMainLog(RemoteWebDriver driver) {
		log.debug("BROWSER CONSOLE LOGS:");
		LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
		for (LogEntry logEntry : logEntries) {
			log.debug(logEntry.getMessage());
		}
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

		WebPage.clearPagesCollection();

		if (PlatformDrivers.getInstance().hasDriver(CURRENT_PLATFORM)) {
			try {
				if (WebAppExecutionContext.LoggingManagement
						.isSupportedInCurrentBrowser()) {
					writeBrowserLogsIntoMainLog(PlatformDrivers.getInstance()
							.getDriver(CURRENT_PLATFORM));
				}
			} finally {
				PlatformDrivers.getInstance().quitDriver(CURRENT_PLATFORM);
			}
		}

		commonSteps.getUserManager().resetUsers();
	}
}
