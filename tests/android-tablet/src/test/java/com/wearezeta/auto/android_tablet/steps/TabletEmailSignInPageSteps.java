package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletEmailSignInPage;
import com.wearezeta.auto.android_tablet.pages.TabletWelcomePage;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class TabletEmailSignInPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletEmailSignInPage getEmailSignInPage() throws Exception {
		return (TabletEmailSignInPage) pagesCollection
				.getPage(TabletEmailSignInPage.class);
	}

	/**
	 * Enter self user credentials into the corresponding fields on email sign
	 * in screen and click Sign In button. Then verifies if login screen is not
	 * visible anymore
	 * 
	 * @step. ^I sign in using my email$
	 * 
	 * @throws Exception
	 */
	@Given("^I sign in using my email$")
	public void ISignInUsingMyEmail() throws Exception {
		// just to make sure we are on correct page
		((TabletWelcomePage) pagesCollection.getPage(TabletWelcomePage.class))
				.clickIHaveAnAccount();
		final ClientUser selfUser = usrMgr.getSelfUserOrThrowError();
		getEmailSignInPage().setLogin(selfUser.getEmail());
		getEmailSignInPage().setPassword(selfUser.getPassword());
		getEmailSignInPage().clickSignInButton();
		Assert.assertTrue("Sign in page is still visible after timeout",
				getEmailSignInPage().waitUntilNotVisible());
	}

	/**
	 * Enter a text into login field on sign in screen
	 * 
	 * @step. ^I enter login \"(.*)\"$
	 * @param login
	 *            the string to enter
	 * 
	 * @throws Exception
	 */
	@When("^I enter login \"(.*)\"$")
	public void IEnterLogin(String login) throws Exception {
		login = usrMgr.replaceAliasesOccurences(login, FindBy.EMAIL_ALIAS);
		getEmailSignInPage().setLogin(login);
	}

	/**
	 * Enter a text into password field on sign in screen
	 * 
	 * @step. ^I enter password \"(.*)\"$
	 * @param login
	 *            the string to enter
	 * 
	 * @throws Exception
	 */
	@When("^I enter password \"(.*)\"$")
	public void IEnterPassword(String password) throws Exception {
		password = usrMgr.replaceAliasesOccurences(password,
				FindBy.PASSWORD_ALIAS);
		getEmailSignInPage().setPassword(password);
	}

	/**
	 * Click Sign In button on sign in screen
	 * 
	 * @step. ^I click Sign In button$
	 * 
	 * @throws Exception
	 */
	@When("^I click Sign In button$")
	public void IClickSignInButton() throws Exception {
		getEmailSignInPage().clickSignInButton();
	}

}
