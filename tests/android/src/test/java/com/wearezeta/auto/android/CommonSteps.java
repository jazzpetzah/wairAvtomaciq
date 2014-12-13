package com.wearezeta.auto.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.mail.MessagingException;
import javax.ws.rs.core.UriBuilderException;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.BackendRequestException;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CreateZetaUser;
import com.wearezeta.auto.common.TestPreparation;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.log.ZetaLogger;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class CommonSteps {
	static {
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

	private static final Logger log = ZetaLogger.getLog(CommonSteps.class.getSimpleName());
	
	public static final String CONNECTION_NAME = "CONNECT TO ";
	public static final String CONNECTION_MESSAGE = "Hello!";
	public static final String PATH_ON_DEVICE = "/mnt/sdcard/DCIM/Camera/userpicture.jpg";
	private static String pingId = null;
	private String path;

	private static boolean oldWayUsersGeneration = false;
	
	@Before("@performance")
	public void setUpPerformance() throws Exception {
		try {
			AndroidCommonUtils.disableHints();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CommonUtils.generatePerformanceUser();
		path = CommonUtils
				.getAndroidApplicationPathFromConfig(CommonSteps.class);
		if (PagesCollection.loginPage == null) {
			PagesCollection.loginPage = new LoginPage(
					CommonUtils
							.getAndroidAppiumUrlFromConfig(CommonSteps.class),
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
							.getAndroidAppiumUrlFromConfig(CommonSteps.class),
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
							.getAndroidAppiumUrlFromConfig(CommonSteps.class),
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

	@Given("^(.*) connection request is sended to me (.*)$")
	public void GivenConnectionRequestIsSendedToMe(String contact, String me)
			throws Throwable {
		ClientUser yourUser = null;
		me = CommonUtils.retrieveRealUserContactPasswordValue(me);
		for (ClientUser user : CommonUtils.yourUsers) {
			if (user.getName().toLowerCase().equals(me.toLowerCase())) {
				yourUser = user;
			}
		}

		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		for (ClientUser user : CommonUtils.yourUsers) {
			if (user.getName().toLowerCase().equals(contact.toLowerCase())) {
				user = BackEndREST.loginByUser(user);
				BackEndREST.sendConnectRequest(user, yourUser, CONNECTION_NAME
						+ user.getName(), CONNECTION_MESSAGE);
				break;
			}

		}

	}

	@Given("My Contact (.*) has group chat with me (.*) and his Contact (.*) with name (.*)")
	public void GivenMyContactCreateGroupChatWithMeAndHisContact(
			String contact1, String me, String contact2, String chatName)
			throws IllegalArgumentException, UriBuilderException, IOException,
			JSONException, BackendRequestException, InterruptedException {
		ClientUser yourUser = null;
		ClientUser yourContact = null;
		ClientUser contactContact = null;
		boolean flag1 = false;
		boolean flag2 = false;
		me = CommonUtils.retrieveRealUserContactPasswordValue(me);
		contact1 = CommonUtils.retrieveRealUserContactPasswordValue(contact1);
		contact2 = CommonUtils.retrieveRealUserContactPasswordValue(contact2);

		for (ClientUser user : CommonUtils.yourUsers) {
			if (user.getName().toLowerCase().equals(me.toLowerCase())) {
				yourUser = user;
				flag1 = true;
			}

			if (user.getName().toLowerCase().equals(contact2.toLowerCase())) {
				contactContact = user;
				flag2 = true;
			}
			if (flag1 && flag2) {
				break;
			}
		}
		for (ClientUser user : CommonUtils.contacts) {
			if (user.getName().toLowerCase().equals(contact1.toLowerCase())) {
				yourContact = user;
				break;
			}
		}
		List<ClientUser> users = new LinkedList<ClientUser>();
		yourUser = BackEndREST.loginByUser(yourUser);
		yourContact = BackEndREST.loginByUser(yourUser);
		users.add(yourUser);
		users.add(contactContact);

		BackEndREST.createGroupConversation(yourContact, users, chatName);
	}

	@Given("^User (.*) is connected with (.*)")
	public void GivenUserIsConnectedWith(String contact1, String contact2)
			throws IllegalArgumentException, UriBuilderException, IOException,
			JSONException, BackendRequestException, InterruptedException {
		contact1 = CommonUtils.retrieveRealUserContactPasswordValue(contact1);
		contact2 = CommonUtils.retrieveRealUserContactPasswordValue(contact2);
		ClientUser contactInfo1 = null;
		ClientUser contactInfo2 = null;
		boolean flag1 = false;
		boolean flag2 = false;
		List<ClientUser> newList = new ArrayList<ClientUser>();
		newList.addAll(CommonUtils.contacts);
		newList.addAll(CommonUtils.yourUsers);

		for (ClientUser user : newList) {
			if (user.getName().toLowerCase().equals(contact1.toLowerCase())) {
				contactInfo1 = user;
				flag1 = true;
			}
			if (user.getName().toLowerCase().equals(contact2.toLowerCase())) {
				contactInfo2 = user;
				flag2 = true;
			}
			if (flag1 && flag2) {
				break;
			}
		}

		contactInfo1 = BackEndREST.loginByUser(contactInfo1);
		BackEndREST.autoTestSendRequest(contactInfo1, contactInfo2);
		BackEndREST.autoTestAcceptAllRequest(contactInfo2);
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

	@Given("^Generate (\\d+) and connect to (.*) contacts$")
	public void GivenGenerateAndConnectAdditionalUsers(int usersNum,
			String userName) throws IllegalArgumentException,
			UriBuilderException, IOException, JSONException,
			BackendRequestException, InterruptedException {
		ClientUser yourUser = null;
		userName = CommonUtils.retrieveRealUserContactPasswordValue(userName);
		for (ClientUser user : CommonUtils.yourUsers) {
			if (user.getName().toLowerCase().equals(userName.toLowerCase())) {
				yourUser = user;
				break;
			}
		}
		CommonUtils.generateNUsers(usersNum);
		CommonUtils.sendConnectionRequestInThreads(yourUser);
		yourUser = BackEndREST.loginByUser(yourUser);
		BackEndREST.acceptAllConnections(yourUser);
	}

	@When("^(.*) ignore all requests$")
	public void IgnoreConnectRequest(String contact)
			throws IllegalArgumentException, UriBuilderException, IOException,
			JSONException, BackendRequestException {
		ClientUser yourСontact = null;
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		for (ClientUser user : CommonUtils.yourUsers) {
			if (user.getName().toLowerCase().equals(contact.toLowerCase())) {
				yourСontact = user;
			}
		}
		yourСontact = BackEndREST.loginByUser(yourСontact);
		BackEndREST.ignoreAllConnections(yourСontact);
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

	@When("^I wait for (.*) seconds$")
	public void WaitForTime(String seconds) throws NumberFormatException,
			InterruptedException {
		Thread.sleep(Integer.parseInt(seconds) * 1000);
	}

	@When("^User (.*) blocks user (.*)$")
	public void BlockContact(String contact, String login) throws Exception {
		ClientUser yourUser = null;
		login = CommonUtils.retrieveRealUserContactPasswordValue(login);
		for (ClientUser user : CommonUtils.yourUsers) {
			if (user.getName().toLowerCase().equals(login.toLowerCase())) {
				yourUser = user;
				break;
			}
		}
		yourUser = BackEndREST.loginByUser(yourUser);
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		for (ClientUser user : CommonUtils.contacts) {
			if (user.getName().toLowerCase().equals(contact.toLowerCase())) {
				user = BackEndREST.loginByUser(user);

				BackEndREST.changeConnectRequestStatus(user, yourUser.getId(),
						"blocked");
				break;
			}

		}
	}

	@When("^(.*) accept all requests$")
	public void AcceptConnectRequest(String contact)
			throws IllegalArgumentException, UriBuilderException, IOException,
			JSONException, BackendRequestException {
		ClientUser yourСontact = null;
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		for (ClientUser user : CommonUtils.yourUsers) {
			if (user.getName().toLowerCase().equals(contact.toLowerCase())) {
				yourСontact = user;
			}
		}
		yourСontact = BackEndREST.loginByUser(yourСontact);
		BackEndREST.acceptAllConnections(yourСontact);
	}

	@When("^I press back button$")
	public void PressBackButton() throws Exception {
		if (PagesCollection.loginPage != null) {
			PagesCollection.loginPage.navigateBack();
		}
	}

	@When("^Contact (.*) ping conversation (.*)$")
	public void userPingedConversation(String contact, String conversationName ) throws Exception{
		ClientUser yourСontact = null;
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		for (ClientUser user : CommonUtils.contacts) {
			if (user.getName().toLowerCase().equals(contact.toLowerCase())) {
				yourСontact = user;
			}
		}
		yourСontact = BackEndREST.loginByUser(yourСontact);
		pingId = BackEndREST.sendPingToConversation(yourСontact, conversationName);
		Thread.sleep(1000);
	}
	
	@When("^Contact (.*) hotping conversation (.*)$")
	public void userHotPingedConversation(String contact, String conversationName ) throws Exception{
		ClientUser yourСontact = null;
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		for (ClientUser user : CommonUtils.contacts) {
			if (user.getName().toLowerCase().equals(contact.toLowerCase())) {
				yourСontact = user;
			}
		}
		yourСontact = BackEndREST.loginByUser(yourСontact);
		BackEndREST.sendHotPingToConversation(yourСontact, conversationName,pingId);
		Thread.sleep(1000);
	}
	
	private static boolean isFirstRun = true;
	private static boolean isFirstRunPassed = false;

	private void commonBefore() throws Exception {
		try {
			AndroidCommonUtils.uploadPhotoToAndroid(PATH_ON_DEVICE);
		} catch (Exception ex) {
			System.out.println("Failed to deploy pictures into simulator");
		}

		try {
			AndroidCommonUtils.disableHints();
			String backendJSON = AndroidCommonUtils.createBackendJSON(CommonUtils.getBackendType(this.getClass()));
			AndroidCommonUtils.deployBackendFile(backendJSON);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

		path = CommonUtils
				.getAndroidApplicationPathFromConfig(CommonSteps.class);
		
		ZetaFormatter.setBuildNumber(AndroidCommonUtils.readClientVersionFromAdb());
	}
	
	@Given("I have (\\d+) users and (\\d+) contacts for (\\d+) users")
	public void IHaveUsersAndConnections(int users, int connections, int usersWithContacts) throws Exception {
		if (!oldWayUsersGeneration) {
			CommonUtils.yourUsers = new CopyOnWriteArrayList<ClientUser>();
			CommonUtils.contacts = new CopyOnWriteArrayList<ClientUser>();
		
			CommonUtils.generateUsers(users, connections);
			log.debug("Following users are failed to be activated: " + CreateZetaUser.failedToActivate);
			Thread.sleep(CommonUtils.BACKEND_SYNC_TIMEOUT);
			TestPreparation.createContactLinks(usersWithContacts);
		}
	}
}
