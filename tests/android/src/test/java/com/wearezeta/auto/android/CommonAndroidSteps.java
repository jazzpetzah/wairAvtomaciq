package com.wearezeta.auto.android;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.logging.Level;

import javax.mail.Message;

import org.junit.Assert;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.CommonCallingSteps;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.GenerateWebLink;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.email.IMAPSMailbox;
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

	private static ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	private Future<Message> passwordResetMessage;
	private ClientUser userToRegister = null;
	private static boolean skipBeforeAfter = false;
	private final CommonSteps commonSteps = CommonSteps.getInstance();
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	public static final Platform CURRENT_PLATFORM = Platform.Android;

	public static final String PATH_ON_DEVICE = "/mnt/sdcard/DCIM/Camera/userpicture.jpg";

	private static String getUrl() throws Exception {
		return CommonUtils
				.getAndroidAppiumUrlFromConfig(CommonAndroidSteps.class);
	}

	private static String getPath() throws Exception {
		return CommonUtils
				.getAndroidApplicationPathFromConfig(CommonAndroidSteps.class);
	}

	public ZetaAndroidDriver resetAndroidDriver(String url, String path,
			boolean isUnicode, Class<?> cls) throws Exception {
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
				CommonUtils.getAndroidActivityFromConfig(cls));
		if (isUnicode) {
			capabilities.setCapability("unicodeKeyboard", true);
			capabilities.setCapability("resetKeyboard", true);
		}

		return (ZetaAndroidDriver) PlatformDrivers.getInstance().resetDriver(
				url, capabilities);
	}

	private void initFirstPage(boolean isUnicode) throws Exception {
		final ZetaAndroidDriver driver = resetAndroidDriver(getUrl(),
				getPath(), isUnicode, this.getClass());
		final WebDriverWait wait = PlatformDrivers
				.createDefaultExplicitWait(driver);
		PagesCollection.loginPage = new LoginPage(driver, wait);
		ZetaFormatter.setDriver(PagesCollection.loginPage.getDriver());
		PagesCollection.loginPage.dismissUpdate();
	}

	@Before("@performance")
	public void setUpPerformance() throws Exception {
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

	@When("^I press back button$")
	public void PressBackButton() throws Exception {
		if (PagesCollection.loginPage != null) {
			PagesCollection.loginPage.navigateBack();
		}
	}

	@When("^I hide keyboard")
	public void IHideKeyboard() {
		if (PagesCollection.loginPage != null) {
			PagesCollection.loginPage.hideKeyboard();
		}
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

		try {
			AndroidCommonUtils.uploadPhotoToAndroid(PATH_ON_DEVICE);
		} catch (Exception ex) {
			System.out.println("Failed to deploy pictures into simulator");
		}

		try {
			AndroidCommonUtils.disableHints();
			AndroidCommonUtils.disableHockeyUpdates();
			String backendJSON = AndroidCommonUtils
					.createBackendJSON(CommonUtils.getBackendType(this
							.getClass()));
			AndroidCommonUtils.deployBackendFile(backendJSON);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ZetaFormatter.setBuildNumber(AndroidCommonUtils
				.readClientVersionFromAdb());
	}

	@When("^I minimize the application$")
	public void IMimizeApllication() throws Exception {
		if (PagesCollection.loginPage != null) {
			PagesCollection.commonAndroidPage = PagesCollection.loginPage
					.minimizeApplication();
		}
	}

	/**
	 * Opens the Browser app
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
		IOpenGalleryApp();
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
			PagesCollection.dialogPage = (DialogPage) PagesCollection.androidPage;
		}
		Thread.sleep(5000);
		PagesCollection.dialogPage.sendMessageInInput();
	}

	/**
	 * Takes screenshot for comparison
	 * 
	 * @step. ^I take screenshot$
	 * 
	 * @throws IOException
	 * 
	 */
	@When("^I take screenshot$")
	public void WhenITake1stScreenshot() throws IOException {
		images.add(PagesCollection.loginPage.takeScreenshot());
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
		PagesCollection.dialogPage.tapOnCenterOfScreen();
	}

	/**
	 * Compare that 1st and 2nd screenshots are not equal
	 * 
	 * @step. ^I compare 1st and 2nd screenshots and they are different$
	 * 
	 * 
	 */
	@Then("^I compare 1st and 2nd screenshots and they are different$")
	public void ThenICompare1st2ndScreenshotsAndTheyAreDifferent() {
		double score = ImageUtil.getOverlapScore(images.get(0), images.get(1));
		Assert.assertTrue(score < 0.70d);
		images.clear();
	}

	@When("^I restore the application$")
	public void IRestoreApllication() {
		if (PagesCollection.loginPage != null) {
			PagesCollection.loginPage.restoreApplication();
		}
	}

	@Given("^(.*) has sent connection request to (.*)$")
	public void GivenConnectionRequestIsSentTo(String userFromNameAlias,
			String usersToNameAliases) throws Throwable {
		commonSteps.ConnectionRequestIsSentTo(userFromNameAlias,
				usersToNameAliases);
	}

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

	@Given("^(.*) has a name (.*)$")
	public void GivenUserHasAName(String name, String newName) throws Throwable {
		try {
			name = usrMgr.findUserByNameOrNameAlias(name).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		commonSteps.IChangeUserName(name, newName);
	}

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
		String link = GenerateWebLink.getInvitationToken(name);
		PagesCollection.commonAndroidPage.ConnectByInvitationLink(link);

	}

	@Given("^(.*) is connected to (.*)$")
	public void UserIsConnectedTo(String userFromNameAlias,
			String usersToNameAliases) throws Exception {
		commonSteps.UserIsConnectedTo(userFromNameAlias, usersToNameAliases);
	}

	@Given("^(.*) has group chat (.*) with (.*)$")
	public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
			String chatName, String otherParticipantsNameAlises)
			throws Exception {
		commonSteps.UserHasGroupChatWithContacts(chatOwnerNameAlias, chatName,
				otherParticipantsNameAlises);
	}

	@When("^(.*) ignore all requests$")
	public void IgnoreAllIncomingConnectRequest(String userToNameAlias)
			throws Exception {
		commonSteps.IgnoreAllIncomingConnectRequest(userToNameAlias);
	}

	@When("^I wait for (.*) second[s]*$")
	public void WaitForTime(String seconds) throws NumberFormatException,
			InterruptedException {
		commonSteps.WaitForTime(seconds);
	}

	@When("^User (.*) blocks user (.*)$")
	public void BlockContact(String blockAsUserNameAlias,
			String userToBlockNameAlias) throws Exception {
		commonSteps.BlockContact(blockAsUserNameAlias, userToBlockNameAlias);
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

	@When("^I add contacts list users to Mac contacts$")
	public void AddContactsUsersToMacContacts() throws Exception {
		commonSteps.AddContactsUsersToMacContacts();
	}

	@When("^I remove contacts list users from Mac contacts$")
	public void IRemoveContactsListUsersFromMacContact() throws Exception {
		commonSteps.IRemoveContactsListUsersFromMacContact();
	}

	@When("^Contact (.*) send message to user (.*)$")
	public void UserSendMessageToConversation(String msgFromUserNameAlias,
			String dstUserNameAlias) throws Exception {
		commonSteps.UserSentMessageToUser(msgFromUserNameAlias,
				dstUserNameAlias, CommonUtils.generateRandomString(10));
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

	@When("^I request reset password for (.*)$")
	public void WhenIRequestResetPassword(String email) throws Exception {
		try {
			email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.commonAndroidPage.requestResetPassword(email);
	}

	@Then("^I reset (.*) password by URL to new (.*)$")
	public void WhenIResetPasswordByUrl(String name, String newPass)
			throws Exception {
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

		String link = BackendAPIWrappers
				.getPasswordResetLink(this.passwordResetMessage);
		PagesCollection.peoplePickerPage = PagesCollection.commonAndroidPage
				.resetByLink(link, newPass);
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
	 * Verify mail subject
	 * 
	 * @step. ^mail subject is (.*)$
	 * 
	 * @param subject
	 *            string
	 * 
	 */
	@Then("^mail subject is (.*)$")
	public void ThenMailSubjectIs(String subject) {
		Assert.assertEquals(subject,
				PagesCollection.commonAndroidPage.getGmailSubject());
	}

	/**
	 * Verify mail content
	 * 
	 * @step. ^mail content contains my $
	 * 
	 * @param email
	 *            string
	 * 
	 */
	@Then("^mail content contains my (.*)$")
	public void ThenMailContentContains(String email) {
		try {
			email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		Assert.assertTrue(PagesCollection.commonAndroidPage.mailContains(email));
	}

	/**
	 * Rotate device to landscape
	 * 
	 * @step. ^I rotate UI to landscape$
	 * 
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
	 */
	@When("^I rotate UI to portrait$")
	public void WhenIRotateUIPortrait() throws Exception {
		PagesCollection.loginPage.rotatePortrait();
	}

}
