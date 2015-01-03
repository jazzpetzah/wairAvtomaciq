package com.wearezeta.auto.ios;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.LoginPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;
import com.wearezeta.auto.ios.tools.IOSKeyboard;
import com.wearezeta.auto.ios.tools.IOSSimulatorPhotoLibHelper;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class CommonIOSSteps{
	private static final Logger log = ZetaLogger.getLog(CommonIOSSteps.class
			.getSimpleName());
	
	private final CommonSteps commonSteps = CommonSteps.getInstance();

	static {
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

	@Before("~@noAcceptAlert")
	public void setUpAcceptAlerts() throws Exception {
		commonBefore();

		String path = CommonUtils
				.getIosApplicationPathFromConfig(TestRun.class);

		if (PagesCollection.loginPage == null) {
			PagesCollection.loginPage = new LoginPage(
					CommonUtils.getIosAppiumUrlFromConfig(TestRun.class), path);
			ZetaFormatter.setDriver(PagesCollection.loginPage.getDriver());
		}

	}

	@Before("@noAcceptAlert")
	public void setUpNoAlerts() throws Exception {
		commonBefore();

		String path = CommonUtils
				.getIosApplicationPathFromConfig(TestRun.class);

		if (PagesCollection.loginPage == null) {
			PagesCollection.loginPage = new LoginPage(
					CommonUtils.getIosAppiumUrlFromConfig(TestRun.class), path,
					false);
			ZetaFormatter.setDriver(PagesCollection.loginPage.getDriver());
		}

	}

	private void commonBefore() throws Exception {

		if (CommonUtils.getIsSimulatorFromConfig(CommonIOSSteps.class)) {
			try {
				String[] picturepath = new String[] { CommonUtils
						.getUserPicturePathFromConfig(CommonIOSSteps.class) };
				IOSSimulatorPhotoLibHelper.CreateSimulatorPhotoLib("8.1",
						picturepath, true, true);
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error("Failed to deploy pictures into simulator.\n"
						+ ex.getMessage());
			}
		}

		if (PagesCollection.loginPage != null
				&& PagesCollection.loginPage.getDriver().isSessionLost()) {
			log.info("Session was lost, reseting pages collection");
			IOSPage.clearPagesCollection();
		}

		ZetaFormatter.setBuildNumber(IOSCommonUtils
				.readClientVersionFromPlist().getClientBuildNumber());
	}

	@After
	public void tearDown() throws Exception {
		PagesCollection.loginPage.Close();
		IOSPage.clearPagesCollection();
		IOSKeyboard.dispose();
	}

	@When("I see keyboard")
	public void ISeeKeyboard() {
		Assert.assertTrue(PagesCollection.dialogPage.isKeyboardVisible());
	}

	@When("I dont see keyboard")
	public void IDontSeeKeyboard() {
		Assert.assertFalse(PagesCollection.dialogPage.isKeyboardVisible());
	}

	@When("I press keyboard Delete button")
	public void IPressKeyboardDeleteBtn() {
		PagesCollection.iOSPage.clickKeyboardDeleteButton();
	}

	@When("I scroll up page a bit")
	public void IScrollUpPageABit() {
		PagesCollection.loginPage.smallScrollUp();
	}

	@When("I accept alert")
	public void IAcceptAlert() {
		PagesCollection.loginPage.acceptAlert();
	}

	@When("I dismiss alert")
	public void IDismissAlert() {
		PagesCollection.loginPage.dismissAlert();
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
}
