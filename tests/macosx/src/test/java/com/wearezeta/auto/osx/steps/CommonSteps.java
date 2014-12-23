package com.wearezeta.auto.osx.steps;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.mail.MessagingException;
import javax.ws.rs.core.UriBuilderException;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.BackendRequestException;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.CreateZetaUser;
import com.wearezeta.auto.common.TestPreparation;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.pages.LoginPage;
import com.wearezeta.auto.osx.pages.MainMenuPage;
import com.wearezeta.auto.osx.pages.PagesCollection;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;

public class CommonSteps {
	public static final Logger log = ZetaLogger.getLog(CommonSteps.class
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

	private static boolean isFirstRun = true;
	private static boolean isFirstRunPassed = false;

	private static boolean oldWayUsersGeneration = false;

	@Before("@performance")
	public void setUpPerformance() throws Exception, UriBuilderException,
			IOException, MessagingException, JSONException,
			BackendRequestException, InterruptedException {
		CommonUtils.generatePerformanceUser();

		String path = CommonUtils
				.getOsxApplicationPathFromConfig(CommonSteps.class);
		senderPages = new PagesCollection();

		senderPages.setMainMenuPage(new MainMenuPage(CommonUtils
				.getOsxAppiumUrlFromConfig(CommonSteps.class), path));
		senderPages.setLoginPage(new LoginPage(CommonUtils
				.getOsxAppiumUrlFromConfig(CommonSteps.class), path));
		ZetaFormatter.setDriver(senderPages.getLoginPage().getDriver());
		senderPages.getLoginPage().sendProblemReportIfFound();
	}

	@Before("~@performance")
	public void setUp() throws Exception {
		boolean generateUsersFlag = Boolean.valueOf(CommonUtils
				.getGenerateUsersFlagFromConfig(CommonSteps.class));
		oldWayUsersGeneration = Boolean.valueOf(CommonUtils
				.getIsOldWayUsersGeneration(CommonSteps.class));

		OSXCommonUtils.deleteZClientLoginFromKeychain();
		OSXCommonUtils.removeAllZClientSettingsFromDefaults();
		OSXCommonUtils.deleteCacheFolder();
		
		OSXCommonUtils.setZClientBackend(CommonUtils.getBackendType(this
				.getClass()));

		if (isFirstRun) {
			isFirstRun = false;
			if (generateUsersFlag && oldWayUsersGeneration) {
				CommonUtils.generateUsers(4);
				log.debug("Following users are failed to be activated: "
						+ CreateZetaUser.failedToActivate);
				Thread.sleep(CommonUtils.BACKEND_SYNC_TIMEOUT);
				TestPreparation.createContactLinks(3);
			} else {
				CommonUtils.usePrecreatedUsers();
			}
			isFirstRunPassed = true;
		}

		if (!isFirstRunPassed) {
			throw new Exception("Skipped due to error in users creation.");
		}

		String path = CommonUtils
				.getOsxApplicationPathFromConfig(CommonSteps.class);
		senderPages = new PagesCollection();

		senderPages.setMainMenuPage(new MainMenuPage(CommonUtils
				.getOsxAppiumUrlFromConfig(CommonSteps.class), path));
		senderPages.setLoginPage(new LoginPage(CommonUtils
				.getOsxAppiumUrlFromConfig(CommonSteps.class), path));
		ZetaFormatter.setDriver(senderPages.getLoginPage().getDriver());
		senderPages.getLoginPage().sendProblemReportIfFound();
		
		if (!OSXCommonUtils.isBackendTypeSet(CommonUtils.getBackendType(this.getClass()))) {
			log.debug("Backend setting were overwritten. Trying to restart app.");
			senderPages.getMainMenuPage().quitZClient();
			OSXCommonUtils.setZClientBackend(CommonUtils.getBackendType(this
					.getClass()));
			senderPages.getLoginPage().startApp();
		}
	}

	@After
	public void tearDown() throws Exception {
		senderPages.closeAllPages();
		
		//workaround for stuck on Send picture test
		OSXCommonUtils.killWireIfStuck();
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
		BackEndREST.createGroupConversation(CommonUtils.yourUsers.get(0),
				chatContacts, chatName);
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

	@Given("^I have (\\d+) users and (\\d+) contacts for (\\d+) users$")
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
	
	@Given("^I have (\\d+) users and (\\d+) contacts for (\\d+) users no predefined$")
	public void IHaveUsersAndConnectionsWithoutPredefined(int users, int connections,
			int usersWithContacts) throws Exception {
		if (!oldWayUsersGeneration) {
			CommonUtils.yourUsers = new CopyOnWriteArrayList<ClientUser>();
			CommonUtils.contacts = new CopyOnWriteArrayList<ClientUser>();

			CommonUtils.generateUsers(users, connections, false);
			log.debug("Following users are failed to be activated: "
					+ CreateZetaUser.failedToActivate);
			Thread.sleep(CommonUtils.BACKEND_SYNC_TIMEOUT);
			TestPreparation.createContactLinks(usersWithContacts);
		}
	}
}
