package com.wearezeta.auto.ios.steps;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.wearezeta.auto.common.CommonCallingSteps;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.LoginPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;
import com.wearezeta.auto.ios.tools.IOSKeyboard;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class CommonIOSSteps {
	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(CommonIOSSteps.class
			.getSimpleName());

	private final CommonSteps commonSteps = CommonSteps.getInstance();
	private Date testStartedDate;

	static {
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

	public static final Platform CURRENT_PLATFORM = Platform.iOS;
	public static final String PLATFORM_VERSION = "8.1";

	private static String getUrl() throws Exception {
		return CommonUtils.getIosAppiumUrlFromConfig(CommonIOSSteps.class);
	}

	private static String getPath() throws Exception {
		return CommonUtils
				.getIosApplicationPathFromConfig(CommonIOSSteps.class);
	}

	@SuppressWarnings("unchecked")
	private Future<ZetaIOSDriver> resetIOSDriver(boolean enableAutoAcceptAlerts)
			throws Exception {
		final DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", CURRENT_PLATFORM.getName());
		capabilities.setCapability("app", getPath());
		final String deviceName = CommonUtils.getDeviceName(this.getClass());
		capabilities.setCapability("deviceName", deviceName);
		capabilities.setCapability("platformVersion", PLATFORM_VERSION);
		capabilities.setCapability("sendKeyStrategy", "grouped");
		final String backendType = CommonUtils.getBackendType(this.getClass());
		capabilities
				.setCapability(
						"processArguments",
						"--args -TutorialOverlaysEnabled 0 -SkipFirstTimeUseChecks 1 -DisableHockeyUpdates 1 -UseHockey 1 -ZMBackendEnvironmentType "
								+ backendType);
		if (enableAutoAcceptAlerts) {
			capabilities.setCapability("autoAcceptAlerts", true);
		}

		testStartedDate = new Date();
		return (Future<ZetaIOSDriver>) PlatformDrivers.getInstance()
				.resetDriver(getUrl(), capabilities);
	}

	@Before("~@noAcceptAlert")
	public void setUpAcceptAlerts() throws Exception {
		commonBefore(resetIOSDriver(true));
	}

	@Before("@noAcceptAlert")
	public void setUpNoAlerts() throws Exception {
		commonBefore(resetIOSDriver(false));
	}

	private void commonBefore(Future<ZetaIOSDriver> lazyDriver)
			throws Exception {
		try {
			// async calls/waiting instances cleanup
			CommonCallingSteps.getInstance().cleanupWaitingInstances();
			CommonCallingSteps.getInstance().cleanupCalls();
		} catch (Exception e) {
			// do not fail if smt fails here
			e.printStackTrace();
		}

		ZetaFormatter.setBuildNumber(IOSCommonUtils
				.readClientVersionFromPlist().getClientBuildNumber());

		PagesCollection.loginPage = new LoginPage(lazyDriver);
		ZetaFormatter.setLazyDriver(lazyDriver);
	}

	@When("^I see keyboard$")
	public void ISeeKeyboard() throws Exception {
		Assert.assertTrue(PagesCollection.dialogPage.isKeyboardVisible());
	}

	@When("^I dont see keyboard$")
	public void IDontSeeKeyboard() throws Exception {
		Assert.assertFalse(PagesCollection.dialogPage.isKeyboardVisible());
	}

	@When("^I press keyboard Delete button$")
	public void IPressKeyboardDeleteBtn() {
		PagesCollection.iOSPage.clickKeyboardDeleteButton();
	}

	@When("^I scroll up page a bit$")
	public void IScrollUpPageABit() throws Exception {
		PagesCollection.loginPage.smallScrollUp();
	}

	@When("^I accept alert$")
	public void IAcceptAlert() throws Exception {
		PagesCollection.loginPage.acceptAlert();
	}

	@When("^I dismiss alert$")
	public void IDismissAlert() throws Exception {
		PagesCollection.loginPage.dismissAlert();
	}

	/**
	 * Closes the app for a certain amount of time in seconds
	 * 
	 * @param seconds
	 *            time in seconds to close the app
	 * 
	 * @step. ^I close the app for (.*) seconds$
	 * @throws Exception 
	 */
	@When("^I close the app for (.*) seconds$")
	public void ICloseApp(int seconds) throws Exception {
		PagesCollection.iOSPage.minimizeApplication(seconds);
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

	@When("^(.*) archived conversation with (.*)$")
	public void ArchiveConversationWithUser(String userToNameAlias,
			String archivedUserNameAlias) throws Exception {
		commonSteps.ArchiveConversationWithUser(userToNameAlias,
				archivedUserNameAlias);
	}

	/**
	 * Silences conversation in backend
	 * 
	 * @step. ^(.*) silenced conversation with (.*)$
	 * 
	 * @param userToNameAlias
	 *            user that mutes the conversation
	 * @param mutedUserNameAlias
	 *            name of group conversation to mute
	 * @throws Exception
	 * 
	 */
	@When("^(.*) silenced conversation with (.*)$")
	public void MuteConversationWithUser(String userToNameAlias,
			String mutedUserNameAlias) throws Exception {
		commonSteps.MuteConversationWithUser(userToNameAlias,
				mutedUserNameAlias);
	}

	/**
	 * Verifies that an unread message dot is NOT seen in the conversation list
	 * 
	 * @step. ^(.*) archived conversation having groupname (.*)$
	 * 
	 * @param userToNameAlias
	 *            user that archives the group conversation
	 * @param archivedUserNameAlias
	 *            name of group conversation to archive
	 * @throws Exception
	 * 
	 */
	@When("^(.*) archived conversation having groupname (.*)$")
	public void ArchiveConversationHavingGroupname(String userToNameAlias,
			String archivedUserNameAlias) throws Exception {
		commonSteps.ArchiveConversationWithGroup(userToNameAlias,
				archivedUserNameAlias);
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

	@When("^Contact (.*) send message to user (.*)$")
	public void UserSendMessageToConversation(String msgFromUserNameAlias,
			String dstUserNameAlias) throws Exception {
		commonSteps.UserSentMessageToUser(msgFromUserNameAlias,
				dstUserNameAlias, CommonUtils.generateRandomString(10));
	}

	@When("^Contact (.*) send number (.*) of message to user (.*)$")
	public void UserSendNumberOfMessageToConversation(
			String msgFromUserNameAlias, int numberOfMessages,
			String dstUserNameAlias) throws Exception {
		for (int i = 0; i <= numberOfMessages; i++) {
			commonSteps.UserSentMessageToUser(msgFromUserNameAlias,
					dstUserNameAlias, CommonUtils.generateRandomString(10));
		}
	}

	@When("^Contact (.*) hotping conversation (.*)$")
	public void UserHotPingedConversation(String hotPingFromUserNameAlias,
			String dstConversationName) throws Exception {
		commonSteps.UserHotPingedConversation(hotPingFromUserNameAlias,
				dstConversationName);
	}

	@When("^I add contacts list users to Mac contacts$")
	public void AddContactsUsersToMacContacts() throws Exception {
		commonSteps.AddContactsUsersToMacContacts();
	}

	@When("^I remove contacts list users from Mac contacts$")
	public void IRemoveContactsListUsersFromMacContact() throws Exception {
		commonSteps.IRemoveContactsListUsersFromMacContact();
	}

	@When("^User (\\w+) change avatar picture to (.*)$")
	public void IChangeUserAvatarPicture(String userNameAlias, String path)
			throws Exception {
		String rootPath = CommonUtils
				.getSimulatorImagesPathFromConfig(getClass());
		commonSteps.IChangeUserAvatarPicture(userNameAlias, rootPath + "/"
				+ path);
	}

	@When("^User (\\w+) change name to (.*)$")
	public void IChangeUserName(String userNameAlias, String newName)
			throws Exception {
		commonSteps.IChangeUserName(userNameAlias, newName);
	}

	/**
	 * Changes a users name to a randomly generated name that starts with a
	 * certain letter
	 * 
	 * @param userNameAlias
	 *            user's name alias to change
	 * 
	 * @param startLetter
	 *            the first letter of the new username
	 * 
	 * @step. ^User (\\w+) name starts with (.*)$
	 */

	@When("^User (\\w+) name starts with (.*)$")
	public void IChangeUserNameToNameStartingWith(String userNameAlias,
			String startLetter) throws Exception {
		String newName = startLetter.concat(UUID.randomUUID().toString()
				.replace("-", ""));
		commonSteps.IChangeUserName(userNameAlias, newName);
	}

	@When("^User (\\w+) change accent color to (.*)$")
	public void IChangeAccentColor(String userNameAlias, String newColor)
			throws Exception {
		commonSteps.IChangeUserAccentColor(userNameAlias, newColor);
		Thread.sleep(1000);
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

	@Given("^(\\w+) wait[s]* up to (\\d+) second[s]* until (.*) exists in backend search results$")
	public void UserWaitsUntilContactExistsInHisSearchResults(
			String searchByNameAlias, int timeout, String query)
			throws Exception {
		commonSteps.WaitUntilContactIsFoundInSearch(searchByNameAlias, query,
				timeout);
	}

	@When("^Contact (.*) sends image (.*) to (.*) conversation (.*)")
	public void ContactSendImageToConversation(String imageSenderUserNameAlias,
			String imageFileName, String conversationType,
			String dstConversationName) throws Exception {
		String imagePath = IOSPage.getImagesPath() + imageFileName;
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

		IOSPage.clearPagesCollection();
		IOSKeyboard.dispose();

		if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
			IOSCommonUtils.collectSimulatorLogs(
					CommonUtils.getDeviceName(getClass()), testStartedDate);
		}

		if (PlatformDrivers.getInstance().hasDriver(CURRENT_PLATFORM)) {
			PlatformDrivers.getInstance().quitDriver(CURRENT_PLATFORM);
		}

		commonSteps.getUserManager().resetUsers();
	}
}
