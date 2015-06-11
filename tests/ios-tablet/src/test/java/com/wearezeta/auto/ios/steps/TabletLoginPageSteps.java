package com.wearezeta.auto.ios.steps;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.TabletLoginPage;

import cucumber.api.java.en.Given;

public class TabletLoginPageSteps {
	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger
			.getLog(ContactListPageSteps.class.getSimpleName());
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private TabletLoginPage getTabletLoginPage() throws Exception {
		return (TabletLoginPage) pagesCollecton.getPage(TabletLoginPage.class);
	}

	/**
	 * Signing in on tablet with login and password
	 * 
	 * @step. ^I Sign in on tablet using login (.*) and password (.*)$
	 * @param login
	 *            login of the user
	 * @param password
	 *            password of the user
	 * @throws Exception
	 */
	@Given("^I Sign in on tablet using login (.*) and password (.*)$")
	public void GivenISignIn(String login, String password) throws Exception {
		try {
			login = usrMgr.findUserByEmailOrEmailAlias(login).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		try {
			password = usrMgr.findUserByPasswordAlias(password).getPassword();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		Assert.assertNotNull(getTabletLoginPage().isVisible());
		getTabletLoginPage().signIn();
		getTabletLoginPage().setLogin(login);
		getTabletLoginPage().setPassword(password);
		getTabletLoginPage().login();
	}
}
