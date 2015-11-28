package com.wearezeta.auto.web.steps;

import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;

import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.common.Browser;
import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.RegistrationPage;
import com.wearezeta.auto.web.pages.WebPage;

import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CommonWebAppSteps {

	private final CommonSteps commonSteps = CommonSteps.getInstance();
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	public static final Logger log = ZetaLogger.getLog(CommonWebAppSteps.class
			.getSimpleName());

	public static final Map<String, Future<ZetaWebAppDriver>> webdrivers = new HashMap<>();

	public static final Platform CURRENT_PLATFORM = Platform.Web;

	private static final String DEFAULT_USER_PICTURE = "/images/aqaPictureContact600_800.jpg";

	private static void setCustomChromeProfile(DesiredCapabilities capabilities)
			throws Exception {
		ChromeOptions options = new ChromeOptions();
		// simulate a fake webcam and mic for testing
		options.addArguments("use-fake-device-for-media-stream");
		// allow skipping the security prompt for sharing the media device
		options.addArguments("use-fake-ui-for-media-stream");

		// allow skipping the security prompt for notifications in chrome 46++
		Map<String, Object> prefs = new HashMap<>();
		prefs.put("profile.managed_default_content_settings.notifications", 1);
		options.setExperimentalOption("prefs", prefs);
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
	}

	private static void setCustomOperaProfile(DesiredCapabilities capabilities)
			throws Exception {
		final String userProfileRoot = WebCommonUtils.getOperaProfileRoot();
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

	private static void setCustomIEProfile(DesiredCapabilities capabilities) {
		capabilities.setCapability("ie.ensureCleanSession", true);
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

	private static final long DRIVER_INIT_TIMEOUT = 60 * 1000;

	@Before("~@performance")
	public void setUp(Scenario scenario) throws Exception {
		String platform = WebAppExecutionContext.getPlatform();
		String osName = WebAppExecutionContext.getOsName();
		String osVersion = WebAppExecutionContext.getOsVersion();
		String browserName = WebAppExecutionContext.getBrowserName();
		String browserVersion = WebAppExecutionContext.getBrowserVersion();

		// create unique name for saucelabs and webdriver instances
		String uniqueName = getUniqueTestName(scenario);
		log.debug("Unique name for this test: " + uniqueName);

		// get custom capabilities
		DesiredCapabilities capabilities = getCustomCapabilities(platform,
				osName, osVersion, browserName, browserVersion, uniqueName);

		final String hubHost = System.getProperty("hubHost");
		final String hubPort = System.getProperty("hubPort");
		final String url = CommonUtils
				.getWebAppApplicationPathFromConfig(CommonWebAppSteps.class);
		final ExecutorService pool = Executors.newFixedThreadPool(1);

		Callable<ZetaWebAppDriver> callableWebAppDriver = new Callable<ZetaWebAppDriver>() {

			@Override
			public ZetaWebAppDriver call() throws Exception {
				final ZetaWebAppDriver lazyWebDriver = new ZetaWebAppDriver(
						new URL("http://" + hubHost + ":" + hubPort + "/wd/hub"),
						capabilities);
				// setup of the browser
				lazyWebDriver.setFileDetector(new LocalFileDetector());
				if (WebAppExecutionContext.getBrowser().equals(Browser.Safari)) {
					WebCommonUtils.closeAllAdditionalTabsInSafari(lazyWebDriver
							.getNodeIp());
					WebCommonUtils.clearHistoryInSafari(lazyWebDriver
							.getNodeIp());
				}
				if (WebAppExecutionContext.getBrowser()
						.isSupportingMaximizingTheWindow()) {
					lazyWebDriver.manage().window().maximize();
				} else {
					if (WebAppExecutionContext.getBrowser()
							.isSupportingSettingWindowSize()) {
						// http://stackoverflow.com/questions/14373371/ie-is-continously-maximizing-and-minimizing-when-test-suite-executes
						lazyWebDriver
								.manage()
								.window()
								.setSize(
										new Dimension(
												WebAppConstants.MIN_WEBAPP_WINDOW_WIDTH,
												WebAppConstants.MIN_WEBAPP_WINDOW_HEIGHT));
					}
				}
				return lazyWebDriver;
			}
		};

		final Future<ZetaWebAppDriver> lazyWebDriver = pool
				.submit(callableWebAppDriver);
		webdrivers.put(uniqueName, lazyWebDriver);
		lazyWebDriver.get(DRIVER_INIT_TIMEOUT, TimeUnit.MILLISECONDS).get(url);
		WebappPagesCollection.getInstance().setFirstPage(
				new RegistrationPage(lazyWebDriver, url));
		ZetaFormatter.setLazyDriver(lazyWebDriver);
	}

	private String getUniqueTestName(Scenario scenario) {
		String browserName = WebAppExecutionContext.getBrowserName();
		String browserVersion = WebAppExecutionContext.getBrowserVersion();
		String platform = WebAppExecutionContext.getPlatform();

		String id = scenario.getId().substring(
				scenario.getId().lastIndexOf(";") + 1);

		return scenario.getName() + " (" + id + ") on " + platform + " with "
				+ browserName + " " + browserVersion;
	}

	private static DesiredCapabilities getCustomCapabilities(String platform,
			String osName, String osVersion, String browserName,
			String browserVersion, String uniqueTestName) throws Exception {
		final DesiredCapabilities capabilities;
		Browser browser = Browser.fromString(browserName);
		switch (browser) {
		case Chrome:
			capabilities = DesiredCapabilities.chrome();
			setCustomChromeProfile(capabilities);
			break;
		case Opera:
			capabilities = DesiredCapabilities.chrome();
			setCustomOperaProfile(capabilities);
			break;
		case Firefox:
			capabilities = DesiredCapabilities.firefox();
			setCustomFirefoxProfile(capabilities);
			break;
		case Safari:
			capabilities = DesiredCapabilities.safari();
			setCustomSafariProfile(capabilities);
			break;
		case InternetExplorer:
			capabilities = DesiredCapabilities.internetExplorer();
			setCustomIEProfile(capabilities);
			break;
		case MicrosoftEdge:
			capabilities = DesiredCapabilities.edge();
			break;
		default:
			throw new NotImplementedException(
					"Unsupported/incorrect browser name is set: " + browserName);
		}

		if (browser.isSupportingConsoleLogManagement()) {
			setExtendedLoggingLevel(
					capabilities,
					WebCommonUtils
							.getExtendedLoggingLevelInConfig(CommonCallingSteps2.class));
		}

		capabilities.setCapability("platform", platform);
		capabilities.setCapability("version", browserVersion);
		capabilities.setCapability("os", osName);
		capabilities.setCapability("os_version", osVersion);
		capabilities.setCapability("browser_version", browserVersion);
		capabilities.setCapability("name", uniqueTestName);
		capabilities.setCapability("browserstack.debug", "true");

		return capabilities;
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
		commonSteps.ThereAreNUsersWhereXIsMe(CURRENT_PLATFORM, count,
				myNameAlias);
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
		commonSteps.ThereAreNUsersWhereXIsMe(CURRENT_PLATFORM, count,
				myNameAlias);
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
		commonSteps.ThereAreNUsersWhereXIsMeWithPhoneNumberOnly(
				CURRENT_PLATFORM, count, myNameAlias);
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
	 * Send personal invitation over the backend
	 *
	 *
	 * @step. ^(.*) send personal invitation to mail (.*) with name (.*) and
	 *        message (.*)$
	 *
	 * @param userToNameAlias
	 *            the name/alias of conversations list owner
	 * @param toMail
	 *            the email to send the invitation to
	 * @param message
	 *            the message for the invitee
	 * @throws Exception
	 */
	@When("^(.*) sends personal invitation to mail (.*) with message (.*)$")
	public void UserXSendsPersonalInvitation(String userToNameAlias,
			String toMail, String message) throws Exception {
		commonSteps.UserXSendsPersonalInvitationWithMessageToUserWithMail(
				userToNameAlias, toMail, message);
	}

	/**
	 * Verify that invitation email exists in user's mailbox
	 *
	 * @step. ^I verify user (.*) has received (?:an |\s*)email invitation$
	 *
	 * @param alias
	 *            user name/alias
	 * @throws Exception
	 * 
	 */
	@Then("^I verify user (.*) has received (?:an |\\s*)email invitation$")
	public void IVerifyUserReceiverInvitation(String alias) throws Exception {
		final String email = usrMgr.findUserByNameOrNameAlias(alias).getEmail();
		assertTrue(String.format(
				"Invitation email for %s has not been received", email),
				BackendAPIWrappers.getInvitationMessage(email).isValid());
	}

	/**
	 * Navigates to the prefilled personal invitation registration page
	 *
	 * @step. ^(.*) navigates to personal invitation registration page$
	 *
	 * @param alias
	 *            user name/alias
	 * @throws Exception
	 *
	 */
	@Then("^(.*) navigates to personal invitation registration page$")
	public void INavigateToPersonalInvitationRegistrationPage(String alias)
			throws Exception {
		final String email = usrMgr.findUserByNameOrNameAlias(alias).getEmail();
		String url = BackendAPIWrappers.getInvitationMessage(email)
				.extractInvitationLink();
		RegistrationPage registrationPage = WebappPagesCollection.getInstance()
				.getPage(RegistrationPage.class);
		registrationPage.setUrl(url);
		registrationPage.navigateTo();
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
	 * Verifies whether current browser log is empty or not
	 *
	 * @step. ^I verify browser log is empty$
	 *
	 * @throws Exception
	 */
	@Then("^I verify browser log is empty$")
	public void VerifyBrowserLogIsEmpty() throws Exception {
		if (PlatformDrivers.getInstance().hasDriver(CURRENT_PLATFORM)) {
			try {
				if (WebAppExecutionContext.getBrowser()
						.isSupportingConsoleLogManagement()) {
					List<LogEntry> browserLog = getBrowserLog(PlatformDrivers
							.getInstance()
							.getDriver(CURRENT_PLATFORM)
							.get(DRIVER_INIT_TIMEOUT, TimeUnit.MILLISECONDS));

					StringBuilder bLog = new StringBuilder("\n");
					browserLog.stream().forEach(
							(entry) -> {
								bLog.append(entry.getLevel()).append(":")
										.append(entry.getMessage())
										.append("\n");
							});
					assertTrue("BrowserLog is not empty: " + bLog.toString(),
							browserLog.isEmpty());
				}
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Refreshes page by getting and setting the current URL. Note: Alternative
	 * 'WebDriver.navigate().refresh()' hangs with Firefox.
	 *
	 * @step. ^I refresh page$
	 *
	 * @throws Exception
	 */
	@Then("^I refresh page$")
	public void IRefreshPage() throws Exception {
		WebappPagesCollection.getInstance().getPage(RegistrationPage.class)
				.refreshPage();
	}

	@SuppressWarnings("unchecked")
	private List<LogEntry> getBrowserLog(RemoteWebDriver driver) {
		return IteratorUtils.toList((Iterator<LogEntry>) driver.manage().logs()
				.get(LogType.BROWSER).iterator());
	}

	private void writeBrowserLogsIntoMainLog(RemoteWebDriver driver) {
		List<LogEntry> logEntries = getBrowserLog(driver);
		if (!logEntries.isEmpty()) {
			log.debug("BROWSER CONSOLE LOGS:");
			for (LogEntry logEntry : logEntries) {
				log.debug(logEntry.getMessage());
			}
			log.debug("--- END OF LOG ---");
		}

	}

	@After
	public void tearDown(Scenario scenario) throws Exception {
		try {
			// async calls/waiting instances cleanup
			CommonCallingSteps2.getInstance().cleanup();
		} catch (Exception e) {
			log.warn(e);
		}

		WebPage.clearPagesCollection();

		String uniqueName = getUniqueTestName(scenario);

		if (webdrivers.containsKey(uniqueName)) {
			Future<ZetaWebAppDriver> webdriver = webdrivers.get(uniqueName);
			try {
				ZetaWebAppDriver driver = webdriver.get(DRIVER_INIT_TIMEOUT, TimeUnit.MILLISECONDS);

				// save browser console if possible
				if (WebAppExecutionContext.getBrowser()
						.isSupportingConsoleLogManagement()) {
					writeBrowserLogsIntoMainLog(driver);
				}

				if (driver instanceof ZetaWebAppDriver) {
					// logout with JavaScript because otherwise backend will block
					// us because of top many login requests
					String logoutScript = "(typeof wire !== 'undefined') && wire.auth.repository.logout();";
					driver.executeScript(logoutScript);
				}

				// show link to saucelabs
				String link = "https://saucelabs.com/jobs/"
						+ driver.getSessionId();
				log.debug("See more information on " + link);
				String html = "<html><body><a id='link' href='"
						+ link
						+ "'>See more information on "
						+ link
						+ "</a><script>window.location.href = document.getElementById('link').getAttribute('href');</script></body></html>";
				scenario.embed(html.getBytes(Charset.forName("UTF-8")),
						"text/html");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				log.debug("Trying to quit webdriver for " + uniqueName);
				webdriver.get(DRIVER_INIT_TIMEOUT, TimeUnit.MILLISECONDS).quit();
				log.debug("Remove webdriver for " + uniqueName + " from map");
				webdrivers.remove(uniqueName);
			}
		}
		commonSteps.getUserManager().resetUsers();
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
		String imagePath = WebCommonUtils.getFullPicturePath(imageFileName);
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
		commonSteps.UserSentImageToConversation(imageSenderUserNameAlias,
				imagePath, dstConversationName, isGroup);
	}

	/**
	 * Unblocks user
	 *
	 * @step. ^(\\w+) unblocks (\\w+)$
	 *
	 * @param userAsNameAlias
	 *            user which wants to unblock another
	 * @param userToBlockNameAlias
	 *            user to unblock
	 *
	 * @throws Exception
	 */
	@Given("^(\\w+) unblocks user (\\w+)$")
	public void UserUnblocks(String userAsNameAlias, String userToBlockNameAlias)
			throws Exception {
		commonSteps.UnblockContact(userAsNameAlias, userToBlockNameAlias);
	}

	/**
	 * Open the sign in page directly (not through a link). This is useful when
	 * testing pages with dead ends (forget password, email verification)
	 *
	 * @step. ^I open Sign In page$
	 *
	 * @throws Exception
	 */
	@Given("^I open Sign In page$")
	public void IOpenSignInPage() throws Exception {
		WebappPagesCollection.getInstance().getPage(RegistrationPage.class)
				.openSignInPage();
	}

}
