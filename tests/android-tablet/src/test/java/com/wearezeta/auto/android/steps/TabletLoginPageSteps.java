package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.android.pages.TabletPagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class TabletLoginPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	
	/**
	 * Enter user email and password into corresponding fields on sign in screen
	 * then taps "Sign In" button
	 * 
	 * @step. ^I Sign in to self profile using login (.*) and password (.*)$
	 * 
	 * @param login
	 *            user login string
	 * @param password
	 *            user password string
	 * 
	 * @throws AssertionError
	 *             if login operation was unsuccessful
	 */
	@Given("^I Sign in to self profile using login (.*) and password (.*)$")
	public void GivenISignInToProfile(String login, String password) throws Exception {
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
		Assert.assertNotNull(TabletPagesCollection.loginPage.isVisible());
		TabletPagesCollection.loginPage.SignIn();
		TabletPagesCollection.loginPage.setLogin(login);
		TabletPagesCollection.loginPage.setPassword(password);
		try {
			TabletPagesCollection.loginPage.doLogIn();
			TabletPagesCollection.personalInfoPage = TabletPagesCollection.loginPage.initProfilePage();
			PagesCollection.personalInfoPage = TabletPagesCollection.personalInfoPage;
		} catch (Exception e) {
			// Ignore silently
		}
		Assert.assertNotNull("Login not passed", TabletPagesCollection.personalInfoPage);
	}
	
	/**
	 * Taps Login button on the corresponding screen
	 * 
	 * @step. I attempt to press Login button
	 * 
	 * @throws Exception
	 */
	@When("I attempt to press Login button")
	public void WhenIPressTabletLogInButton() throws Exception {
		TabletPagesCollection.loginPage.doLogIn();
		TabletPagesCollection.personalInfoPage = TabletPagesCollection.loginPage.initProfilePage();
		PagesCollection.personalInfoPage = TabletPagesCollection.personalInfoPage;
		Assert.assertTrue("Login finished",
				TabletPagesCollection.loginPage.waitForLogin());
	}
	
	/**
	 * Taps Join button on Welcome page
	 * 
	 * @step. I press tablet Join button
	 * 
	 * @throws Exception
	 */
	@When("I press tablet Join button")
	public void WhenIPressJoinButton() throws Exception {
		TabletPagesCollection.registrationPage = TabletPagesCollection.loginPage.tabletJoin();
		PagesCollection.registrationPage = TabletPagesCollection.registrationPage;
	}
}