package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.TabletLoginPage;

import cucumber.api.java.en.Given;

public class TabletLoginPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

	private TabletLoginPage getTabletLoginPage() throws Exception {
		return pagesCollection.getPage(TabletLoginPage.class);
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
		Assert.assertTrue("Login page is not visible", getTabletLoginPage().isVisible());
		final ClientUser self = usrMgr.getSelfUserOrThrowError();
		getTabletLoginPage().switchToEmailLogin();
		getTabletLoginPage().setLogin(self.getEmail());
		getTabletLoginPage().setPassword(self.getPassword());
		getTabletLoginPage().login();
		getTabletLoginPage().waitForLoginToFinish();
	}
}
