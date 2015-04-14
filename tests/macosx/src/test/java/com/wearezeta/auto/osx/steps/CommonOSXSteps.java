package com.wearezeta.auto.osx.steps;

import io.appium.java_client.AppiumDriver;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
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
import cucumber.api.java.en.When;

public class CommonOSXSteps {

	private final CommonSteps commonSteps = CommonSteps.getInstance();

	public static final Logger log = ZetaLogger.getLog(CommonOSXSteps.class
			.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	public static final Platform CURRENT_PLATFORM = Platform.Mac;

	private Date testStartedTimestamp;

	private long startupTime = -1;

	public long getStartupTime() {
		return this.startupTime;
	}

	static {
		// for Jenkins slaves we should define that environment has display
		CommonUtils.defineNoHeadlessEnvironment();
		// disabling selenium logs to exclude not used output from log
		CommonUtils.disableSeleniumLogs();
	}

	public static void resetBackendSettingsIfOverwritten() throws IOException,
			Exception {
		if (!OSXCommonUtils.isBackendTypeSet(CommonUtils
				.getBackendType(CommonOSXSteps.class))) {
			log.debug("Backend setting were overwritten. Trying to restart app.");
			PagesCollection.mainMenuPage.quitWire();
			OSXCommonUtils.setZClientBackendAndDisableStartUI(CommonUtils
					.getBackendType(CommonOSXSteps.class));
			PagesCollection.mainMenuPage.startApp();
		}
	}

	private ZetaOSXDriver resetOSXDriver(String url) throws Exception {
		final DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
		capabilities.setCapability(CapabilityType.PLATFORM, CURRENT_PLATFORM
				.getName().toUpperCase());
		capabilities.setCapability("platformName", CURRENT_PLATFORM.getName());

		return (ZetaOSXDriver) PlatformDrivers.getInstance().resetDriver(url,
				capabilities);
	}

	private void commonBefore() throws Exception {
		this.testStartedTimestamp = new Date();
		final ZetaOSXDriver driver = resetOSXDriver(OSXExecutionContext.appiumUrl);
		final WebDriverWait wait = PlatformDrivers
				.createDefaultExplicitWait(driver);

		PagesCollection.mainMenuPage = new MainMenuAndDockPage(driver, wait);
		PagesCollection.welcomePage = new WelcomePage(driver, wait);
		PagesCollection.loginPage = new LoginPage(driver, wait);
		PagesCollection.problemReportPage = new ProblemReportPage(driver, wait);

		ZetaFormatter.setDriver((AppiumDriver) PagesCollection.welcomePage
				.getDriver());
		// saving time of startup for Sync Engine
		this.startupTime = new Date().getTime()
				- this.testStartedTimestamp.getTime();

		PagesCollection.welcomePage
				.sendProblemReportIfAppears(PagesCollection.problemReportPage);
	}

	@Before("@performance")
	public void setUpPerformance() throws Exception {
		CommonUtils.enableTcpForAppName(OSXConstants.Apps.WIRE);
		OSXCommonUtils.deleteWireLoginFromKeychain();
		OSXCommonUtils.removeAllZClientSettingsFromDefaults();
		OSXCommonUtils.deleteCacheFolder();

		OSXCommonUtils.setZClientBackendAndDisableStartUI(CommonUtils
				.getBackendType(this.getClass()));

		commonBefore();

		resetBackendSettingsIfOverwritten();
	}

	@Before("~@performance")
	public void setUp() throws Exception {
		CommonUtils.enableTcpForAppName(OSXConstants.Apps.WIRE);
		OSXCommonUtils.deleteWireLoginFromKeychain();
		OSXCommonUtils.removeAllZClientSettingsFromDefaults();
		OSXCommonUtils.deleteCacheFolder();

		OSXCommonUtils.setZClientBackendAndDisableStartUI(CommonUtils
				.getBackendType(this.getClass()));

		commonBefore();

		resetBackendSettingsIfOverwritten();
	}

	@Given("^(.*) has sent connection request to (.*)$")
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
		commonSteps.ThereAreNUsers(count);
	}

	@Given("^There \\w+ (\\d+) user[s]* where (.*) is me$")
	public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias)
			throws Exception {
		commonSteps.ThereAreNUsersWhereXIsMe(count, myNameAlias);
	}

	@When("^(.*) ignore all requests$")
	public void IgnoreAllIncomingConnectRequest(String userToNameAlias)
			throws Exception {
		commonSteps.IgnoreAllIncomingConnectRequest(userToNameAlias);
	}

	@When("^I wait for (.*) seconds$")
	public void WaitForTime(String seconds) throws NumberFormatException,
			InterruptedException {
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

	@Given("^(\\w+) wait[s]* up to (\\d+) second[s]* until (.*) exists in backend search results$")
	public void UserWaitsUntilContactExistsInHisSearchResults(
			String searchByNameAlias, int timeout, String query)
			throws Exception {
		commonSteps.WaitUntilContactIsFoundInSearch(searchByNameAlias, query,
				timeout);
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

	@After
	public void tearDown() throws Exception {
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
