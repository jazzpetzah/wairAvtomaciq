package com.wearezeta.auto.ios;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.script.ScriptException;
import javax.ws.rs.core.UriBuilderException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.junit.Assert;

import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.BackendRequestException;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.CreateZetaUser;
import com.wearezeta.auto.common.TestPreparation;
import com.wearezeta.auto.common.UsersState;
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

public class CommonSteps {
	private static final Logger log = ZetaLogger.getLog(CommonSteps.class
			.getSimpleName());

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

	private static boolean isFirstRun = true;
	private static boolean isFirstRunPassed = false;

	private static boolean oldWayUsersGeneration = false;

	private void commonBefore() throws Exception {
		
		if (CommonUtils.getIsSimulatorFromConfig(CommonSteps.class)) {
			try {
				String[] picturepath = new String[] { CommonUtils
						.getUserPicturePathFromConfig(CommonSteps.class) };
				IOSSimulatorPhotoLibHelper.CreateSimulatorPhotoLib("8.1",
						picturepath, true, true);
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error("Failed to deploy pictures into simulator.\n"
						+ ex.getMessage());
			}
		}

		boolean generateUsersFlag = Boolean.valueOf(CommonUtils
				.getGenerateUsersFlagFromConfig(CommonSteps.class));
		oldWayUsersGeneration = Boolean.valueOf(CommonUtils
				.getIsOldWayUsersGeneration(CommonSteps.class));

		if (isFirstRun) {
			isFirstRun = false;
			if (generateUsersFlag && oldWayUsersGeneration) {
				CommonUtils.generateUsers(4);
				Thread.sleep(CommonUtils.BACKEND_SYNC_TIMEOUT);
				TestPreparation.createContactLinks(3);
			} else {
				CommonUtils.usePrecreatedUsers();
			}

			isFirstRunPassed = true;
		}

		if (!isFirstRunPassed) {
			throw new IOException("Skipped due to error in users creation.");
		}

		if (PagesCollection.loginPage != null
				&& PagesCollection.loginPage.getDriver().isSessionLost()) {
			log.info("Session was lost, reseting pages collection");
			IOSPage.clearPagesCollection();
		}

		ZetaFormatter.setBuildNumber(IOSCommonUtils
				.readClientVersionFromPlist().getClientBuildNumber());
	}

	@Given("I have at least (.*) connections")
	public void GivenIHaveAtMinimumConnections(int minimumConnections)
			throws Throwable {
		try {
			for (int i = 0; i < minimumConnections; i++) {
				String email = CreateZetaUser.registerUserAndReturnMail();
				ClientUser user = new ClientUser();
				user.setEmail(email);
				user.setPassword(CommonUtils
						.getDefaultPasswordFromConfig(CommonUtils.class));
				user.setUserState(UsersState.Created);
				CommonUtils.requiredContacts.add(user);
				BackEndREST.autoTestSendRequest(user,
						CommonUtils.findUserNamed("aqaUser"));
			}
			BackEndREST.autoTestAcceptAllRequest(CommonUtils
					.findUserNamed("aqaUser"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception {

		PagesCollection.loginPage.Close();
		IOSPage.clearPagesCollection();
		IOSKeyboard.dispose();
	}

	@Given("^I have group chat with name (.*) with (.*) and (.*)$")
	public void GivenIHaveGroupChatWith(String chatName, String contact1,
			String contact2) throws Throwable {
		boolean flag1 = false;
		boolean flag2 = false;
		contact1 = CommonUtils.retrieveRealUserContactPasswordValue(contact1);
		contact2 = CommonUtils.retrieveRealUserContactPasswordValue(contact2);
		List<ClientUser> chatContacts = new LinkedList<ClientUser>();
		for (ClientUser user : CommonUtils.contacts) {
			if (user.getName().toLowerCase().equals(contact1.toLowerCase())) {
				chatContacts.add(user);
				flag1 = true;
			}
			if (user.getName().toLowerCase().equals(contact2.toLowerCase())) {
				chatContacts.add(user);
				flag2 = true;
			}
			if (flag1 && flag2) {
				break;
			}
		}
		ClientUser user = CommonUtils.yourUsers.get(0);
		user = BackEndREST.loginByUser(user);
		BackEndREST.createGroupConversation(user, chatContacts, chatName);
	}

	@Given("I send invitation to (.*) by (.*)")
	public void ISendInvitationToUserByContact(String user, String contact)
			throws Throwable {
		user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		BackEndREST.autoTestSendRequest(CommonUtils.findUserNamed(contact),
				CommonUtils.findUserNamed(user));
	}

	@Given("I send (.*) connection requests to (.*)")
	public void ISendInvitationToUserByContact(int requests, String user)
			throws Throwable {
		// limited to 4 users at time of creation to save time creating users
		if (requests < 5) {
			List<ClientUser> unconnectedUsers = CommonUtils.yourUsers.subList(
					3, 7);
			for (int i = 0; i < requests; i++) {
				BackEndREST.autoTestSendRequest(unconnectedUsers.get(i),
						CommonUtils.findUserNamed(user));
			}
		}
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

	@Given("I have (\\d+) users and (\\d+) contacts for (\\d+) users")
	public void IHaveUsersAndConnections(int users, int connections,
			int usersWithContacts) throws Exception {
		if (!oldWayUsersGeneration) {
			CommonUtils.yourUsers = new CopyOnWriteArrayList<ClientUser>();
			CommonUtils.contacts = new CopyOnWriteArrayList<ClientUser>();

			CommonUtils.generateUsers(users, connections);
			log.debug("Following users are failed to be activated: "
					+ CreateZetaUser.failedToActivate);
			Thread.sleep(CommonUtils.BACKEND_SYNC_TIMEOUT);
			TestPreparation.createContactLinks(usersWithContacts);
		}
	}

	@When("I add contacts list users to Mac contacts")
	public void AddContactsUsersToMacContacts() throws ScriptException,
			IllegalArgumentException, UriBuilderException, IOException,
			JSONException, BackendRequestException, InterruptedException {
		CommonUtils.addUsersToMacContacts(CommonUtils.contacts);
	}

	@When("I remove contacts list users from Mac contacts")
	public void IRemoveContactsListUsersFromMacContact()
			throws ScriptException, IllegalArgumentException,
			UriBuilderException, IOException, JSONException,
			BackendRequestException, InterruptedException {
		CommonUtils.removeUsersFromMacContacts(CommonUtils.contacts);
	}
}
