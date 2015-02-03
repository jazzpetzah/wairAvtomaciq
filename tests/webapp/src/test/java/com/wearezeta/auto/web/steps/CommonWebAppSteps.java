package com.wearezeta.auto.web.steps;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.pages.InvitationCodePage;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.WebPage;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;

public class CommonWebAppSteps {
	private final CommonSteps commonSteps = CommonSteps.getInstance();

	public static final Logger log = ZetaLogger.getLog(CommonWebAppSteps.class
			.getSimpleName());

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

	@After
	public void tearDown() throws Exception {

		if (PagesCollection.loginPage != null) {
			PagesCollection.loginPage.Close();
		}

		WebPage.clearPagesCollection();

		commonSteps.getUserManager().resetUsers();
	}
}
