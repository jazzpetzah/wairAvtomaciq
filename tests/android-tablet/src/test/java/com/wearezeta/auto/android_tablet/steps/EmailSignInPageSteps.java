package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.pages.TabletEmailSignInPage;
import com.wearezeta.auto.android_tablet.pages.TabletWelcomePage;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.Given;
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
	public void ITapSignInButton() throws Exception {
		getEmailSignInPage().tapSignInButton();
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

	/**
	 * Press the "Phone Login" button on the email sign in to switch to
	 * phone number login
	 *
	 * @step. ^I switch to [Pp]hone [Ll]ogin screen$
	 * @throws Exception
	 */
	@When("^I switch to [Pp]hone [Ll]ogin screen$")
	public void ISwitchToPhoneLogin() throws Exception {
        getEmailSignInPage().tapPhoneLogin();
	}
}
