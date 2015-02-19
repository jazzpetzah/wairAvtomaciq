package com.wearezeta.auto.ios;

import java.util.UUID;

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
		PagesCollection.loginPage.close();
		IOSPage.clearPagesCollection();
		IOSKeyboard.dispose();

		commonSteps.getUserManager().resetUsers();
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

	/**
	* Closes the app for a certain amount of time in seconds
	* 
	* @param seconds
	*           time in seconds to close the app
	* 
	* @step. ^I close the app for (.*) seconds$
	*/
	@When("^I close the app for (.*) seconds$")
		public void ICloseApp(int seconds) {
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
	 * Verifies that an unread message dot is NOT seen in the conversation list
	 * 
	 * @step. ^(.*) archived conversation having groupname (.*)$
	 * 
	 * @param userToNameAlias
	 *            user that archives the group conversation
	 * @param archivedUserNameAlias
	 * 			  name of group conversation to archive
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
	public void UserSendNumberOfMessageToConversation(String msgFromUserNameAlias, int numberOfMessages,
			String dstUserNameAlias) throws Exception {
		for(int i = 0; i <= numberOfMessages; i++){
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

	@When("^User (\\w+) change  name to (.*)$")
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
	public void IChangeUserNameToNameStartingWith(String userNameAlias, String startLetter)
			throws Exception {
		String newName = startLetter.concat(UUID.randomUUID().toString().replace("-", ""));
		commonSteps.IChangeUserName(userNameAlias, newName);
	}

	@When("^User (\\w+) change  accent color to (.*)$")
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
		String imagePath = IOSPage
				.getImagesPath() + imageFileName;
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
}
