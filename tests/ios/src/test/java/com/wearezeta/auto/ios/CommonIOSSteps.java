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

public class CommonIOSSteps {
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

	@When("^I see keyboard$")
	public void ISeeKeyboard() {
		Assert.assertTrue(PagesCollection.dialogPage.isKeyboardVisible());
	}

	@When("^I dont see keyboard$")
	public void IDontSeeKeyboard() {
		Assert.assertFalse(PagesCollection.dialogPage.isKeyboardVisible());
	}

	@When("^I press keyboard Delete button$")
	public void IPressKeyboardDeleteBtn() {
		PagesCollection.iOSPage.clickKeyboardDeleteButton();
	}

	@When("^I scroll up page a bit$")
	public void IScrollUpPageABit() {
		PagesCollection.loginPage.smallScrollUp();
	}

	@When("^I accept alert$")
	public void IAcceptAlert() {
		PagesCollection.loginPage.acceptAlert();
	}

	@When("^I dismiss alert$")
	public void IDismissAlert() {
		PagesCollection.loginPage.dismissAlert();
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

	@When("^I change (.*) avatar picture to (.*)$")
	public void IChangeMyAvatarPicture(String userNameAlias, String path)
			throws Exception {
		commonSteps.IChangeUserAvatarPicture(userNameAlias, path);
	}

	@When("^I change (.*) name to (.*)$")
	public void IChangeMyName(String userNameAlias, String newName)
			throws Exception {
		commonSteps.IChangeUserName(userNameAlias, newName);
	}
}
