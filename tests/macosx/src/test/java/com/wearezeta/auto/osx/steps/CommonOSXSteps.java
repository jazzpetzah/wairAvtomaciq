package com.wearezeta.auto.osx.steps;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.ws.rs.core.UriBuilderException;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.wearezeta.auto.common.BackendRequestException;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
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

	static {
		System.setProperty("java.awt.headless", "false");
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

	public static PagesCollection senderPages;

	@Before("@performance")
	public void setUpPerformance() throws Exception, UriBuilderException,
			IOException, MessagingException, JSONException,
			BackendRequestException, InterruptedException {
		commonSteps.getUserManager().generatePerformanceUser();

		String path = CommonUtils
				.getOsxApplicationPathFromConfig(CommonOSXSteps.class);
		senderPages = new PagesCollection();

		senderPages.setMainMenuPage(new MainMenuPage(CommonUtils
				.getOsxAppiumUrlFromConfig(CommonOSXSteps.class), path));
		senderPages.setLoginPage(new LoginPage(CommonUtils
				.getOsxAppiumUrlFromConfig(CommonOSXSteps.class), path));
		ZetaFormatter.setDriver(senderPages.getLoginPage().getDriver());
		senderPages.getLoginPage().sendProblemReportIfFound();
	}

	@Before("~@performance")
	public void setUp() throws Exception {

		OSXCommonUtils.deleteZClientLoginFromKeychain();
		OSXCommonUtils.removeAllZClientSettingsFromDefaults();
		OSXCommonUtils.deleteCacheFolder();

		OSXCommonUtils.setZClientBackend(CommonUtils.getBackendType(this
				.getClass()));

		String path = CommonUtils
				.getOsxApplicationPathFromConfig(CommonOSXSteps.class);
		senderPages = new PagesCollection();

		senderPages.setMainMenuPage(new MainMenuPage(CommonUtils
				.getOsxAppiumUrlFromConfig(CommonOSXSteps.class), path));
		senderPages.setLoginPage(new LoginPage(CommonUtils
				.getOsxAppiumUrlFromConfig(CommonOSXSteps.class), path));
		ZetaFormatter.setDriver(senderPages.getLoginPage().getDriver());
		senderPages.getLoginPage().sendProblemReportIfFound();

		if (!OSXCommonUtils.isBackendTypeSet(CommonUtils.getBackendType(this
				.getClass()))) {
			log.debug("Backend setting were overwritten. Trying to restart app.");
			senderPages.getMainMenuPage().quitZClient();
			OSXCommonUtils.setZClientBackend(CommonUtils.getBackendType(this
					.getClass()));
			senderPages.getLoginPage().startApp();
		}
	}

	@Given("^(.*) connection request is sended to me (.*)$")
	public void GivenConnectionRequestIsSendedToMe(String contact, String me)
			throws Throwable {
		commonSteps.GivenConnectionRequestIsSentToMe(contact, me);
	}

	@Given("My Contact (.*) has group chat with me (.*) and his Contact (.*) with name (.*)")
	public void GivenMyContactCreateGroupChatWithMeAndHisContact(
			String contact1, String me, String contact2, String chatName)
			throws Exception {
		commonSteps.GivenMyContactCreateGroupChatWithMeAndHisContact(contact1, me, contact2, chatName);
	}

	@Given("^User (.*) is connected with (.*)")
	public void GivenUserIsConnectedWith(String contact1, String contact2)
			throws Exception {
		commonSteps.GivenUserIsConnectedWith(contact1, contact2);
	}

	@Given("^I have group chat with name (.*) with (.*) and (.*)$")
	public void GivenIHaveGroupChatWith(String chatName, String contact1,
			String contact2) throws Exception {
		commonSteps.GivenIHaveGroupChatWith(chatName, contact1, contact2);
	}

	@Given("^Generate (\\d+) and connect to (.*) contacts$")
	public void GivenGenerateAndConnectAdditionalUsers(int usersNum,
			String userName) throws Exception {
		commonSteps.GivenGenerateAndConnectAdditionalUsers(usersNum, userName);
	}

	@When("^(.*) ignore all requests$")
	public void IgnoreConnectRequest(String contact) throws Exception {
		commonSteps.IgnoreConnectRequest(contact);
	}

	@When("^I wait for (.*) seconds$")
	public void WaitForTime(String seconds) throws NumberFormatException,
			InterruptedException {
		commonSteps.WaitForTime(seconds);
	}

	@When("^User (.*) blocks user (.*)$")
	public void BlockContact(String contact, String login) throws Exception {
		commonSteps.BlockContact(contact, login);
	}

	@When("^(.*) accept all requests$")
	public void AcceptConnectRequest(String contact) throws Exception {
		commonSteps.AcceptConnectRequest(contact);
	}

	@Given("I have (\\d+) users and (\\d+) contacts for (\\d+) users")
	public void IHaveUsersAndConnections(int users, int connections,
			int usersWithContacts) throws Exception {
		commonSteps.IHaveUsersAndConnections(users, connections, usersWithContacts);
	}

	@When("^Contact (.*) ping conversation (.*)$")
	public void userPingedConversation(String contact, String conversationName)
			throws Exception {
		commonSteps.UserPingedConversation(contact, conversationName);
	}

	@When("^Contact (.*) hotping conversation (.*)$")
	public void userHotPingedConversation(String contact,
			String conversationName) throws Exception {
		commonSteps.UserHotPingedConversation(contact, conversationName);
	}

	@Given("I send invitation to (.*) by (.*)")
	public void ISendInvitationToUserByContact(String user, String contact)
			throws Exception {
		commonSteps.ISendInvitationToUserByContact(user, contact);
	}

	@Given("I send (.*) connection requests to (.*)")
	public void ISendInvitationToUserByContact(int requests, String user)
			throws Throwable {
		commonSteps.ISendInvitationToUserByContact(requests, user);
	}

	@When("I add contacts list users to Mac contacts")
	public void AddContactsUsersToMacContacts() throws Exception {
		commonSteps.AddContactsUsersToMacContacts();
	}

	@When("I remove contacts list users from Mac contacts")
	public void IRemoveContactsListUsersFromMacContact() throws Exception {
		commonSteps.IRemoveContactsListUsersFromMacContact();
	}

	@Given("I have at least (.*) connections")
	public void GivenIHaveAtMinimumConnections(int minimumConnections)
			throws Exception {
		commonSteps.GivenIHaveAtMinimumConnections(minimumConnections);
	}
	
	@After
	public void tearDown() throws Exception {
		senderPages.closeAllPages();

		// workaround for stuck on Send picture test
		OSXCommonUtils.killWireIfStuck();
	}
}
