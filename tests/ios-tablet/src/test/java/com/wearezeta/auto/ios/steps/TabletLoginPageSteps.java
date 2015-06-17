package com.wearezeta.auto.ios.steps;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
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
	 * @step. ^I Sign in on tablet using my email$
	 * 
	 * @throws Exception
	 */
	@Given("^I Sign in on tablet using my email$")
	public void GivenISignInUsingEmail() throws Exception {
		Assert.assertNotNull(getTabletLoginPage().isVisible());
		final ClientUser self = usrMgr.getSelfUserOrThrowError();
		getTabletLoginPage().signIn();
		getTabletLoginPage().setLogin(self.getEmail());
		getTabletLoginPage().setPassword(self.getPassword());
		getTabletLoginPage().login();
	}
}
