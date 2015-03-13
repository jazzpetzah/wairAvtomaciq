package com.wearezeta.auto.web.steps;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.PerformanceCommon;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.pages.InvitationCodePage;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.WebPage;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class CommonWebAppSteps {
	private final CommonSteps commonSteps = CommonSteps.getInstance();

	public static final Logger log = ZetaLogger.getLog(CommonWebAppSteps.class
			.getSimpleName());
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	public static final Platform CURRENT_PLATFORM = Platform.Web;
	private static final int MAX_DRIVER_CREATION_RETRIES = 3;

	private static final String DEFAULT_USER_PICTURE = PerformanceCommon.DEFAULT_PERF_IMAGE;

	static {
		System.setProperty("java.awt.headless", "false");
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

	private static String getBrowser() throws Exception {
		return WebCommonUtils
				.getWebAppBrowserNameFromConfig(CommonWebAppSteps.class);
	}

	private static void setCustomOperaProfile(DesiredCapabilities capabilities,
			String browserPlatform) throws Exception {
		final String userProfileRoot = WebCommonUtils
				.getOperaProfileRoot(browserPlatform);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("user-data-dir=" + userProfileRoot);
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
	}

	private static void setCustomFirefoxProfile(DesiredCapabilities capabilities) {
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("dom.webnotifications.enabled", false);
		capabilities.setCapability("firefox_profile", profile);
	}

	private static void setCustomSafariProfile(DesiredCapabilities capabilities) {
		SafariOptions options = new SafariOptions();
		options.setUseCleanSession(true);
		capabilities.setCapability(SafariOptions.CAPABILITY, options);
	}

	private ZetaWebAppDriver resetWebAppDriver(String url) throws Exception {
		final String browser = getBrowser();
		final DesiredCapabilities capabilities;
		final String webPlatformName = WebCommonUtils
				.getPlatformNameFromConfig(WebPage.class);
		switch (browser) {
		case "chrome":
			capabilities = DesiredCapabilities.chrome();
			if (webPlatformName.toLowerCase().contains("opera")) {
				// This is to fix Desktop Notifications alerts appearance in
				// Opera
				setCustomOperaProfile(capabilities, webPlatformName);
			}
			break;
		case "firefox":
			capabilities = DesiredCapabilities.firefox();
			// This is to fix Desktop Notifications alert appearance in
			// Firefox
			setCustomFirefoxProfile(capabilities);
			break;
		case "safari":
			capabilities = DesiredCapabilities.safari();
			setCustomSafariProfile(capabilities);
			break;
		case "ie":
			capabilities = DesiredCapabilities.internetExplorer();
			break;
		default:
			throw new NotImplementedException(
					"Incorrect browser name is set - "
							+ browser
							+ ". Please choose one of the following: chrome | firefox | safari | ie");
		}
		if (webPlatformName.length() > 0) {
			// Use undocumented grid property to match platforms
			// https://groups.google.com/forum/#!topic/selenium-users/PRsEBcbpNlM
			capabilities.setCapability("applicationName", webPlatformName);
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
		final String url = CommonUtils
				.getWebAppAppiumUrlFromConfig(CommonWebAppSteps.class);
		final String path = CommonUtils
				.getWebAppApplicationPathFromConfig(CommonWebAppSteps.class);
		WebAppExecutionContext.browserName = getBrowser();
		int tryNum = 1;
		ZetaWebAppDriver webDriver;
		WebDriverWait wait;
		do {
			try {
				webDriver = resetWebAppDriver(url);
				wait = PlatformDrivers.createDefaultExplicitWait(webDriver);
				webDriver.manage().window().maximize();

				PagesCollection.invitationCodePage = new InvitationCodePage(
						webDriver, wait, path);
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

		// put AppleScript for execution to Selenium node
		if (WebAppExecutionContext.browserName
				.equals(WebAppConstants.Browser.SAFARI)) {
			try {
				WebAppExecutionContext.seleniumNodeIp = WebCommonUtils
						.getNodeIp(PagesCollection.invitationCodePage
								.getDriver());
			} catch (Exception e) {
				log.debug("Error on checking node IP for Safari test. Error message: "
						+ e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates specified number of users and sets user with specified name as
	 * main user
	 * 
	 * @step. ^There \\w+ (\\d+) user[s]* where (.*) is me$
	 * 
	 * @param count
	 *            number of users to create
	 * @param myNameAlias
	 *            user name or name alias to use as main user
	 * 
	 * @throws Exception
	 */
	@Given("^There \\w+ (\\d+) user[s]* where (.*) is me$")
	public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias)
			throws Exception {
		commonSteps.ThereAreNUsersWhereXIsMe(count, myNameAlias);
		IChangeUserAvatarPicture(myNameAlias, "default");
	}

	@When("^User (\\w+) change avatar picture to (.*)$")
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
	 * @step. ^(.*) is connected to (.*)$
	 * 
	 * @param userFromNameAlias
	 *            user which sends connection request
	 * @param usersToNameAliases
	 *            user which accepts connection request
	 * 
	 * @throws Exception
	 */
	@Given("^(.*) is connected to (.*)$")
	public void UserIsConnectedTo(String userFromNameAlias,
			String usersToNameAliases) throws Exception {
		commonSteps.UserIsConnectedTo(userFromNameAlias, usersToNameAliases);
	}

	/**
	 * Creates group chat with specified users
	 * 
	 * @step. ^(.*) has group chat (.*) with (.*)$
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
	@Given("^(.*) has group chat (.*) with (.*)$")
	public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
			String chatName, String otherParticipantsNameAlises)
			throws Exception {
		commonSteps.UserHasGroupChatWithContacts(chatOwnerNameAlias, chatName,
				otherParticipantsNameAlises);
	}

	/**
	 * Sets self user to be the current user
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
	 * Sends connection request by one user to another
	 * 
	 * @step. ^(.*) has sent connection request to (.*)$
	 * 
	 * @param userFromNameAlias
	 *            user that sends connection request
	 * @param usersToNameAliases
	 *            user which receive connection request
	 *
	 * @throws Exception
	 */
	@Given("^(.*) has sent connection request to (.*)$")
	public void GivenConnectionRequestIsSentTo(String userFromNameAlias,
			String usersToNameAliases) throws Throwable {
		commonSteps.ConnectionRequestIsSentTo(userFromNameAlias,
				usersToNameAliases);
	}

	/**
	 * Pings BackEnd until user is indexed and avialable in search
	 * 
	 * @step. ^(\\w+) wait[s]* up to (\\d+) second[s]* until (.*) exists in
	 *        backend search results$
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
	@Given("^(\\w+) wait[s]* up to (\\d+) second[s]* until (.*) exists in backend search results$")
	public void UserWaitsUntilContactExistsInHisSearchResults(
			String searchByNameAlias, int timeout, String query)
			throws Exception {
		query = usrMgr.findUserByNameOrNameAlias(query).getName();
		commonSteps.WaitUntilContactIsFoundInSearch(searchByNameAlias, query,
				timeout);
	}

	/**
	 * Wait for specified amount of seconds
	 * 
	 * @step. ^I wait for (.*) seconds$
	 * 
	 * @param seconds
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 */
	@When("^I wait for (.*) seconds$")
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

	@After
	public void tearDown() throws Exception {
		if (PagesCollection.invitationCodePage != null) {
			PagesCollection.invitationCodePage.close();
		}

		WebPage.clearPagesCollection();

		if (PlatformDrivers.getInstance().hasDriver(CURRENT_PLATFORM)) {
			PlatformDrivers.getInstance().quitDriver(CURRENT_PLATFORM);
		}

		commonSteps.getUserManager().resetUsers();
	}
}
