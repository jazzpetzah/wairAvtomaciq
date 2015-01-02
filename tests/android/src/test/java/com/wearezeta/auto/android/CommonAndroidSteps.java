package com.wearezeta.auto.android;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.user_management.ClientUsersManager;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class CommonAndroidSteps {
	static {
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

	private final CommonSteps commonSteps = CommonSteps.getInstance();
	
	public static final String PATH_ON_DEVICE = "/mnt/sdcard/DCIM/Camera/userpicture.jpg";
	private String path;

	@Before("@performance")
	public void setUpPerformance() throws Exception {
		try {
			AndroidCommonUtils.disableHints();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ClientUsersManager.getInstance().generatePerformanceUser();
		path = CommonUtils
				.getAndroidApplicationPathFromConfig(CommonAndroidSteps.class);
		if (PagesCollection.loginPage == null) {
			PagesCollection.loginPage = new LoginPage(
					CommonUtils
							.getAndroidAppiumUrlFromConfig(CommonAndroidSteps.class),
					path);
			ZetaFormatter.setDriver(PagesCollection.loginPage.getDriver());
			PagesCollection.loginPage.dismissUpdate();
		}
	}

	@Before({ "~@unicode", "~@performance" })
	public void setUp() throws Exception {

		commonBefore();

		if (PagesCollection.loginPage == null) {
			PagesCollection.loginPage = new LoginPage(
					CommonUtils
							.getAndroidAppiumUrlFromConfig(CommonAndroidSteps.class),
					path);
			ZetaFormatter.setDriver(PagesCollection.loginPage.getDriver());
			PagesCollection.loginPage.dismissUpdate();
		}
	}

	@Before({ "@unicode", "~@performance" })
	public void setUpUnicode() throws Exception {

		commonBefore();

		if (PagesCollection.loginPage == null) {
			PagesCollection.loginPage = new LoginPage(
					CommonUtils
							.getAndroidAppiumUrlFromConfig(CommonAndroidSteps.class),
					path, true);
			ZetaFormatter.setDriver(PagesCollection.loginPage.getDriver());
			PagesCollection.loginPage.dismissUpdate();
		}
	}

	@After
	public void tearDown() throws Exception {
		PagesCollection.loginPage.Close();
		AndroidPage.clearPagesCollection();
	}


	@When("^I press back button$")
	public void PressBackButton() throws Exception {
		if (PagesCollection.loginPage != null) {
			PagesCollection.loginPage.navigateBack();
		}
	}

	private void commonBefore() throws Exception {
		try {
			AndroidCommonUtils.uploadPhotoToAndroid(PATH_ON_DEVICE);
		} catch (Exception ex) {
			System.out.println("Failed to deploy pictures into simulator");
		}

		try {
			AndroidCommonUtils.disableHints();
			String backendJSON = AndroidCommonUtils
					.createBackendJSON(CommonUtils.getBackendType(this
							.getClass()));
			AndroidCommonUtils.deployBackendFile(backendJSON);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		path = CommonUtils
				.getAndroidApplicationPathFromConfig(CommonAndroidSteps.class);

		ZetaFormatter.setBuildNumber(AndroidCommonUtils
				.readClientVersionFromAdb());
	}
	
	@When("^I minimize the application$")
	public void IMimizeApllication() throws InterruptedException {
		if (PagesCollection.loginPage != null) {
			PagesCollection.loginPage.minimizeApplication();
		}
	}

	@When("^I restore the application$")
	public void IRestoreApllication() {
		if (PagesCollection.loginPage != null) {
			PagesCollection.loginPage.restoreApplication();
		}
	}
	
	@Given("^(.*) connection request is sended to me (.*)$")
	public void GivenConnectionRequestIsSendedToMe(String contact, String me)
			throws Throwable {
		commonSteps.GivenConnectionRequestIsSendedToMe(contact, me);
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
		commonSteps.userPingedConversation(contact, conversationName);
	}

	@When("^Contact (.*) hotping conversation (.*)$")
	public void userHotPingedConversation(String contact,
			String conversationName) throws Exception {
		commonSteps.userHotPingedConversation(contact, conversationName);
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
