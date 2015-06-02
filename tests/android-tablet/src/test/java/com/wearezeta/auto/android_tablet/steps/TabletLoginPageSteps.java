package com.wearezeta.auto.android_tablet.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class TabletLoginPageSteps {
	// private final ClientUsersManager usrMgr =
	// ClientUsersManager.getInstance();

	// private final AndroidTabletPagesCollection pagesCollection =
	// AndroidTabletPagesCollection
	// .getInstance();

	// private LoginPage getLoginPage() throws Exception {
	// return (LoginPage) pagesCollection
	// .getPageOrElseInstantiate(LoginPage.class);
	// }
	//
	// private EmailSignInPage getEmailSignInPage() throws Exception {
	// return (EmailSignInPage) pagesCollection
	// .getPageOrElseInstantiate(EmailSignInPage.class);
	// }

	@Given("^I Sign in on tablet using login (.*) and password (.*)$")
	public void GivenISignInOnTablet(String login, String password)
			throws Exception {
		// try {
		// login = usrMgr.findUserByEmailOrEmailAlias(login).getEmail();
		// } catch (NoSuchUserException e) {
		// // Ignore silently
		// }
		// try {
		// password = usrMgr.findUserByPasswordAlias(password).getPassword();
		// } catch (NoSuchUserException e) {
		// // Ignore silently
		// }
		// (new WelcomePageSteps()).ISwitchToEmailSignIn();
		// getEmailSignInPage().setLogin(login);
		// getEmailSignInPage().setPassword(password);
		// try {
		// getEmailSignInPage().logIn();
		// TabletPagesCollection.personalInfoPage =
		// TabletPagesCollection.loginPage
		// .initProfilePage();
		// AndroidTabletPagesCollection.personalInfoPage =
		// TabletPagesCollection.personalInfoPage;
		// TabletPagesCollection.contactListPage =
		// TabletPagesCollection.loginPage
		// .initContactListPage();
		// AndroidTabletPagesCollection.contactListPage =
		// TabletPagesCollection.contactListPage;
		// } catch (Exception e) {
		// // Ignore silently
		// }
		// Assert.assertNotNull("Login not passed",
		// TabletPagesCollection.personalInfoPage);
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
		// TabletPagesCollection.emailSignInPage.logIn();
		// TabletPagesCollection.personalInfoPage =
		// TabletPagesCollection.loginPage
		// .initProfilePage();
		// AndroidTabletPagesCollection.personalInfoPage =
		// TabletPagesCollection.personalInfoPage;
		// Assert.assertTrue("Login finished",
		// TabletPagesCollection.loginPage.waitForLogin());
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
		// TabletPagesCollection.registrationPage =
		// TabletPagesCollection.loginPage
		// .tabletJoin();
		// AndroidTabletPagesCollection.registrationPage =
		// TabletPagesCollection.registrationPage;
	}
}
