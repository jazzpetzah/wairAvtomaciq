package com.wearezeta.auto.android;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;

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
		
		commonSteps.getUserManager().resetUsers();
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

	@Given("^(.*) has sent connection request to (.*)$")
	public void GivenConnectionRequestIsSentTo(String userFromNameAlias,
			String usersToNameAliases) throws Throwable {
		commonSteps.ConnectionRequestIsSentTo(userFromNameAlias,
				usersToNameAliases);
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

	@Given("^There \\w+ (\\d+) user[s]*$")
	public void ThereAreNUsers(int count) throws Exception {
		commonSteps.ThereAreNUsers(count);
	}

	@Given("^There \\w+ (\\d+) user[s]* where (.*) is me$")
	public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias)
			throws Exception {
		commonSteps.ThereAreNUsersWhereXIsMe(count, myNameAlias);
	}
}
