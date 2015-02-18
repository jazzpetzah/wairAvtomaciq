package com.wearezeta.auto.web.steps;

import org.apache.log4j.Logger;

import com.google.common.io.Files;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.pages.InvitationCodePage;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.WebPage;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class CommonWebAppSteps {
	private final CommonSteps commonSteps = CommonSteps.getInstance();

	public static final Logger log = ZetaLogger.getLog(CommonWebAppSteps.class
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

	@Before("~@performance")
	public void setUp() throws Exception {

		String url = CommonUtils
				.getWebAppAppiumUrlFromConfig(CommonWebAppSteps.class);
		String path = CommonUtils
				.getWebAppApplicationPathFromConfig(CommonWebAppSteps.class);

		PagesCollection.invitationCodePage = new InvitationCodePage(url, path);

		ZetaFormatter.setDriver(PagesCollection.invitationCodePage.getDriver());

		// put AppleScript for execution to Selenium node
		if (WebCommonUtils.getWebAppBrowserNameFromConfig(
				CommonWebAppSteps.class).equals(WebAppConstants.Browser.SAFARI)) {
			try {
				WebAppExecutionContext.seleniumNodeIp = WebCommonUtils
						.getNodeIp(PagesCollection.invitationCodePage
								.getDriver());
			} catch (Exception e) {
				log.debug("Error on checking node IP for Safari test. Error message: "
						+ e.getMessage());
				e.printStackTrace();
			}
			WebAppExecutionContext.temporaryScriptsLocation = Files
					.createTempDir().getAbsolutePath();
		}
	}

	/**
	 * Creates specified number of users and sets user with specified name as
	 * main user
	 * 
	 * @step. ^There \\w+ (\\d+) user[s]* where (.*) is me$
	 * 
	 * @param count
	 *            number of users to create
	 * @param myNameAlias
	 *            user name or name alias to use as main user
	 * 
	 * @throws Exception
	 */
	@Given("^There \\w+ (\\d+) user[s]* where (.*) is me$")
	public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias)
			throws Exception {
		commonSteps.ThereAreNUsersWhereXIsMe(count, myNameAlias);
	}

	/**
	 * Creates connection between to users
	 * 
	 * @step. ^(.*) is connected to (.*)$
	 * 
	 * @param userFromNameAlias
	 *            user which sends connection request
	 * @param usersToNameAliases
	 *            user which accepts connection request
	 * 
	 * @throws Exception
	 */
	@Given("^(.*) is connected to (.*)$")
	public void UserIsConnectedTo(String userFromNameAlias,
			String usersToNameAliases) throws Exception {
		commonSteps.UserIsConnectedTo(userFromNameAlias, usersToNameAliases);
	}

	/**
	 * Creates group chat with specified users
	 * 
	 * @step. ^(.*) has group chat (.*) with (.*)$
	 * 
	 * @param chatOwnerNameAlias
	 *            user that creates group chat
	 * @param chatName
	 *            group chat name
	 * @param otherParticipantsNameAlises
	 *            list of users which will be added to chat separated by comma
	 * 
	 * @throws Exception
	 */
	@Given("^(.*) has group chat (.*) with (.*)$")
	public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
			String chatName, String otherParticipantsNameAlises)
			throws Exception {
		commonSteps.UserHasGroupChatWithContacts(chatOwnerNameAlias, chatName,
				otherParticipantsNameAlises);
	}

	/**
	 * Sets self user to be the current user
	 * 
	 * @step. ^User (\\w+) is [Mm]e$
	 * 
	 * @param nameAlias
	 *            user to be set as self user
	 * 
	 * @throws Exception
	 */
	@Given("^User (\\w+) is [Mm]e$")
	public void UserXIsMe(String nameAlias) throws Exception {
		commonSteps.UserXIsMe(nameAlias);
	}

	/**
	 * Sends connection request by one user to another
	 * 
	 * @step. ^(.*) has sent connection request to (.*)$
	 * 
	 * @param userFromNameAlias
	 *            user that sends connection request
	 * @param usersToNameAliases
	 *            user which receive connection request
	 *
	 * @throws Exception
	 */
	@Given("^(.*) has sent connection request to (.*)$")
	public void GivenConnectionRequestIsSentTo(String userFromNameAlias,
			String usersToNameAliases) throws Throwable {
		commonSteps.ConnectionRequestIsSentTo(userFromNameAlias,
				usersToNameAliases);
	}

	/**
	 * Pings BackEnd until user is indexed and avialable in search
	 * 
	 * @step. ^(\\w+) wait[s]* up to (\\d+) second[s]* until (.*) exists in
	 *        backend search results$
	 * 
	 * @param searchByNameAlias
	 *            user name to search string
	 * 
	 * @param timeout
	 *            max ping timeout in sec
	 * 
	 * @param query
	 *            querry string
	 * 
	 * @throws Exception
	 */
	@Given("^(\\w+) wait[s]* up to (\\d+) second[s]* until (.*) exists in backend search results$")
	public void UserWaitsUntilContactExistsInHisSearchResults(
			String searchByNameAlias, int timeout, String query)
			throws Exception {
		query = usrMgr.findUserByNameOrNameAlias(query).getName();
		commonSteps.WaitUntilContactIsFoundInSearch(searchByNameAlias, query,
				timeout);
	}

	/**
	 * Wait for specified amount of seconds
	 * 
	 * @step. ^I wait for (.*) seconds$
	 * 
	 * @param seconds
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 */
	@When("^I wait for (.*) seconds$")
	public void WaitForTime(String seconds) throws NumberFormatException,
			InterruptedException {
		commonSteps.WaitForTime(seconds);
	}

	@After
	public void tearDown() throws Exception {

		if (PagesCollection.loginPage != null) {
			PagesCollection.loginPage.Close();
		}

		WebPage.clearPagesCollection();

		commonSteps.getUserManager().resetUsers();
	}
}
