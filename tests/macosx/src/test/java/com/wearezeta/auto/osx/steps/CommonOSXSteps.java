package com.wearezeta.auto.osx.steps;

import io.appium.java_client.AppiumDriver;

import java.io.IOException;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.ws.rs.core.UriBuilderException;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.backend.BackendRequestException;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.common.OSXExecutionContext;
import com.wearezeta.auto.osx.pages.LoginPage;
import com.wearezeta.auto.osx.pages.MainMenuPage;
import com.wearezeta.auto.osx.pages.PagesCollection;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class CommonOSXSteps {
	private final CommonSteps commonSteps = CommonSteps.getInstance();

	public static final Logger log = ZetaLogger.getLog(CommonOSXSteps.class
			.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	static {
		System.setProperty("java.awt.headless", "false");
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

	public static PagesCollection senderPages;

	public static void resetBackendSettingsIfOverwritten() throws IOException,
			Exception {
		if (!OSXCommonUtils.isBackendTypeSet(CommonUtils
				.getBackendType(CommonOSXSteps.class))) {
			log.debug("Backend setting were overwritten. Trying to restart app.");
			PagesCollection.mainMenuPage.quitZClient();
			OSXCommonUtils.setZClientBackend(CommonUtils
					.getBackendType(CommonOSXSteps.class));
			PagesCollection.loginPage.startApp();
		}
	}

	@Before("@performance")
	public void setUpPerformance() throws Exception, UriBuilderException,
			IOException, MessagingException, JSONException,
			BackendRequestException, InterruptedException {
		senderPages = new PagesCollection();

		PagesCollection.mainMenuPage = new MainMenuPage(
				OSXExecutionContext.appiumUrl, OSXExecutionContext.wirePath);
		PagesCollection.loginPage = new LoginPage(
				OSXExecutionContext.appiumUrl, OSXExecutionContext.wirePath);
		ZetaFormatter.setDriver((AppiumDriver) PagesCollection.loginPage
				.getDriver());
		PagesCollection.loginPage.sendProblemReportIfFound();
	}

	@Before("~@performance")
	public void setUp() throws Exception {
		CommonUtils.enableTcpForAppName(OSXCommonUtils.APP_NAME);
		OSXCommonUtils.deleteZClientLoginFromKeychain();
		OSXCommonUtils.removeAllZClientSettingsFromDefaults();
		OSXCommonUtils.deleteCacheFolder();

		OSXCommonUtils.setZClientBackend(CommonUtils.getBackendType(this
				.getClass()));

		senderPages = new PagesCollection();

		PagesCollection.mainMenuPage = new MainMenuPage(
				OSXExecutionContext.appiumUrl, OSXExecutionContext.wirePath);
		PagesCollection.loginPage = new LoginPage(
				OSXExecutionContext.appiumUrl, OSXExecutionContext.wirePath);
		ZetaFormatter.setDriver((AppiumDriver) PagesCollection.loginPage
				.getDriver());
		PagesCollection.loginPage.sendProblemReportIfFound();

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
		commonSteps.BlockTcpConnectionForApp(OSXCommonUtils.APP_NAME);
	}

	@Given("^Internet connection is restored$")
	public void InternetConnectionIsRestored() throws Exception {
		commonSteps.EnableTcpConnectionForApp(OSXCommonUtils.APP_NAME);
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
		senderPages.closeAllPages();

		commonSteps.getUserManager().resetUsers();

		// workaround for stuck on Send picture test
		OSXCommonUtils.killWireIfStuck();

		CommonUtils.enableTcpForAppName(OSXCommonUtils.APP_NAME);
	}
}
