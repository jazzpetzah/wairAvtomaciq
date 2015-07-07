package com.wearezeta.auto.ios.steps;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.wearezeta.auto.common.CommonCallingSteps;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.PerformanceCommon;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.LoginPage;
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

	private static boolean skipBeforeAfter = false;

	private final CommonSteps commonSteps = CommonSteps.getInstance();
	private static final String DEFAULT_USER_PICTURE = PerformanceCommon.DEFAULT_PERF_IMAGE;
	private Date testStartedDate;

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	static {
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

	public static final Platform CURRENT_PLATFORM = Platform.iOS;
	public static final String PLATFORM_VERSION = "8.3";

	private static String getUrl() throws Exception {
		return CommonUtils.getIosAppiumUrlFromConfig(CommonIOSSteps.class);
	}

	private static String getPath() throws Exception {
		return CommonUtils
				.getIosApplicationPathFromConfig(CommonIOSSteps.class);
	}

	public boolean isSkipBeforeAfter() {
		return skipBeforeAfter;
	}

	public void setSkipBeforeAfter(boolean skipBeforeAfter) {
		CommonIOSSteps.skipBeforeAfter = skipBeforeAfter;
	}

	@SuppressWarnings("unchecked")
	public Future<ZetaIOSDriver> resetIOSDriver(boolean enableAutoAcceptAlerts)
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

		setTestStartedDate(new Date());
		return (Future<ZetaIOSDriver>) PlatformDrivers.getInstance()
				.resetDriver(getUrl(), capabilities);
	}

	@Before("~@noAcceptAlert")
	public void setUpAcceptAlerts() throws Exception {
		if (this.isSkipBeforeAfter()) {
			return;
		}
		commonBefore(resetIOSDriver(true));
	}

	@Before("@noAcceptAlert")
	public void setUpNoAlerts() throws Exception {
		if (this.isSkipBeforeAfter()) {
			return;
		}
		commonBefore(resetIOSDriver(false));
	}

	public void commonBefore(Future<ZetaIOSDriver> lazyDriver) throws Exception {
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

		pagesCollecton.setFirstPage(new LoginPage(lazyDriver));
		ZetaFormatter.setLazyDriver(lazyDriver);
	}

	@When("^I see keyboard$")
	public void ISeeKeyboard() throws Exception {
		Assert.assertTrue(pagesCollecton.getCommonPage().isKeyboardVisible());
	}

	@When("^I dont see keyboard$")
	public void IDontSeeKeyboard() throws Exception {
		Assert.assertFalse(pagesCollecton.getCommonPage().isKeyboardVisible());
	}

	@When("^I press keyboard Delete button$")
	public void IPressKeyboardDeleteBtn() throws Exception {
		pagesCollecton.getCommonPage().clickKeyboardDeleteButton();
	}

	@When("^I scroll up page a bit$")
	public void IScrollUpPageABit() throws Exception {
		pagesCollecton.getCommonPage().smallScrollUp();
	}

	@When("^I accept alert$")
	public void IAcceptAlert() throws Exception {
		pagesCollecton.getCommonPage().acceptAlert();
	}

	@When("^I dismiss alert$")
	public void IDismissAlert() throws Exception {
		pagesCollecton.getCommonPage().dismissAlert();
	}

	/**
	 * Hide keyboard using mobile command
	 * 
	 * @step. ^I hide keyboard$
	 * 
	 * @throws Exception
	 */
	@When("^I hide keyboard$")
	public void IHideKeyboard() throws Exception {
		pagesCollecton.getCommonPage().hideKeyboard();
	}

	/**
	 * Hide keyboard by click on hide keyboard button
	 * 
	 * @step. ^I click hide keyboard button$
	 * 
	 * @throws Exception
	 */
	@When("^I click hide keyboard button$")
	public void IClickHideKeyboardBtn() throws Exception {
		pagesCollecton.getCommonPage().clickHideKeyboarButton();
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
		pagesCollecton.getCommonPage().minimizeApplication(seconds);
	}

	/**
	 * Locks screen for a certain amount of time in seconds
	 * 
	 * @param seconds
	 *            time in seconds to lock screen
	 * @step.^I lock screen for (.*) seconds$
	 * @throws Exception
	 */
	@When("^I lock screen for (.*) seconds$")
	public void ILockScreen(int seconds) throws Exception {
		pagesCollecton.getCommonPage().lockScreen(seconds);
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
		IChangeUserAvatarPicture(myNameAlias, "default");
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
		String contactMessage = CommonUtils.generateRandomString(10);
		commonSteps.UserSentMessageToUser(msgFromUserNameAlias,
				dstUserNameAlias, contactMessage);
		DialogPageSteps.message = contactMessage;
	}

	@When("^Contact (.*) send number (.*) of message to user (.*)$")
	public void UserSendNumberOfMessageToConversation(
			String msgFromUserNameAlias, int numberOfMessages,
			String dstUserNameAlias) throws Exception {
		for (int i = 1; i <= numberOfMessages; i++) {
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

	@When("^User (\\w+) change avatar picture to (.*)$")
	public void IChangeUserAvatarPicture(String userNameAlias, String path)
			throws Exception {

		String avatar = null;
		String rootPath = CommonUtils
				.getSimulatorImagesPathFromConfig(getClass());
		if (path.equals("default")) {
			avatar = DEFAULT_USER_PICTURE;
		} else {
			avatar = rootPath + "/" + path;
		}
		commonSteps.IChangeUserAvatarPicture(userNameAlias, avatar);
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
		newName = newName.substring(0, newName.length() / 2);
		commonSteps.IChangeUserName(userNameAlias, newName);
	}

	@When("^User (\\w+) change accent color to (.*)$")
	public void IChangeAccentColor(String userNameAlias, String newColor)
			throws Exception {
		commonSteps.IChangeUserAccentColor(userNameAlias, newColor);
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

	@Given("^(\\w+) waits? until (.*) exists in backend search results$")
	public void UserWaitsUntilContactExistsInHisSearchResults(
			String searchByNameAlias, String query) throws Exception {
		commonSteps.WaitUntilContactIsFoundInSearch(searchByNameAlias, query);
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

		pagesCollecton.clearAllPages();
		IOSKeyboard.dispose();

		if (CommonUtils.getIsSimulatorFromConfig(getClass())
				&& !skipBeforeAfter) {
			IOSCommonUtils
					.collectSimulatorLogs(
							CommonUtils.getDeviceName(getClass()),
							getTestStartedDate());
		}

		if (PlatformDrivers.getInstance().hasDriver(CURRENT_PLATFORM)) {
			PlatformDrivers.getInstance().quitDriver(CURRENT_PLATFORM);
		}

		commonSteps.getUserManager().resetUsers();
	}

	public Date getTestStartedDate() {
		return testStartedDate;
	}

	public void setTestStartedDate(Date testStartedDate) {
		this.testStartedDate = testStartedDate;
	}

	/**
	 * Rotate device to landscape
	 * 
	 * @step. ^I rotate UI to (landscape|portrait)$
	 * 
	 * @param orientation
	 *            must be landscape or portrait
	 * 
	 * @throws Exception
	 */
	@When("^I rotate UI to (landscape|portrait)$")
	public void WhenIRotateUILandscape(ScreenOrientation orientation)
			throws Exception {
		pagesCollecton.getCommonPage().rotateScreen(orientation);
		Thread.sleep(1000); // fix for animation
	}

	/**
	 * Tap in center of the screen
	 * 
	 * @step. ^I tap on center of the screen$
	 * 
	 * @throws Exception
	 */
	@When("^I tap on center of the screen$")
	public void ITapOnCenterOfTheScreen() throws Exception {
		pagesCollecton.getCommonPage().tapOnCenterOfScreen();
	}

	/**
	 * Tap in top left corner of the screen
	 * 
	 * @step. ^I tap on top left corner of the screen$
	 * 
	 * @throws Exception
	 */
	@When("^I tap on top left corner of the screen$")
	public void ITapOnTopLeftCornerOfTheScreen() throws Exception {
		pagesCollecton.getCommonPage().tapOnTopLeftScreen();
	}

	/**
	 * General swipe action
	 * 
	 * @step. ^I swipe left in current window$
	 * 
	 * @throws Exception
	 */
	@When("^I swipe left in current window$")
	public void ISwipeLeftInCurrentWindow() throws Exception {
		pagesCollecton.getCommonPage().swipeLeft(500);
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

}
