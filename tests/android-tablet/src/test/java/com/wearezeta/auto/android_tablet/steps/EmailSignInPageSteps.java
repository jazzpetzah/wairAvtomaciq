package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.pages.TabletEmailSignInPage;
import com.wearezeta.auto.android_tablet.pages.TabletWelcomePage;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class EmailSignInPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletEmailSignInPage getEmailSignInPage() throws Exception {
		return pagesCollection.getPage(TabletEmailSignInPage.class);
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
		pagesCollection.getPage(TabletWelcomePage.class).tapSignInButton();
		final ClientUser selfUser = usrMgr.getSelfUserOrThrowError();
		getEmailSignInPage().setLogin(selfUser.getEmail());
		getEmailSignInPage().setPassword(selfUser.getPassword());
		getEmailSignInPage().tapSignInButton();
		getEmailSignInPage().verifyNotVisible();
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
	 * @param password the string to enter
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
	 * Tap Sign In button on sign in screen
	 * 
	 * @step. ^I tap Sign In button$
	 * 
	 * @throws Exception
	 */
	@When("^I tap Sign In button$")
	public void IClickSignInButton() throws Exception {
		getEmailSignInPage().tapSignInButton();
	}

	/**
	 * Checks to see that the login error message contains the correct text
	 * After providing a false email address or password
	 * 
	 * @step. ^I see error message \"(.*)\"$
	 * @param expectedMsg
	 *            the expected error message
	 * 
	 * @throws Exception
	 */
	@Then("^I see error message \"(.*)\"$")
	public void ISeeErrorMessage(String expectedMsg) throws Exception {
		getEmailSignInPage().verifyErrorMessageText(expectedMsg);
	}

	/**
	 * Tap OK button on an alert
	 * 
	 * @step. ^I tap OK button on the error message$
	 * 
	 * @throws Exception
	 */
	@When("^I tap OK button on the error message$")
	public void ITapOKOnErrorMessage() throws Exception {
		getEmailSignInPage().acceptErrorMessage();
	}
}
