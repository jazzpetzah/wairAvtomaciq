package com.wearezeta.auto.android;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.*;

import cucumber.api.java.en.*;

public class LoginPageSteps {

	@Given ("I see sign in screen")
	public void GiveniSeeSignInScreen() {
		Assert.assertNotNull(PagesCollection.loginPage.isVisible());
	}

	@Given("^I Sign in using login (.*) and password (.*)$")
	public void GivenISignIn(String login,String password) throws Exception  {
		if (login.equals(CommonUtils.YOUR_USER_1)) {
			login = CommonUtils.yourUsers.get(0).getEmail();
			password = CommonUtils.retrieveRealUserContactPasswordValue(password);
		}
		Assert.assertNotNull(PagesCollection.loginPage.isVisible());
		PagesCollection.loginPage.SignIn();
		PagesCollection.loginPage.setLogin(login);
		PagesCollection.loginPage.setPassword(password);
		PagesCollection.contactListPage =(ContactListPage)(PagesCollection.loginPage.LogIn());
		Assert.assertTrue("Login finished", PagesCollection.loginPage.waitForLogin());
	}

	@When("I press Sign in button")
	public void WhenIPressSignInButton() throws IOException {	 
		PagesCollection.loginPage.SignIn();
	}

	@When("I press Log in button")
	public void WhenIPressLogInButton() throws Exception {	 
		PagesCollection.loginPage.LogIn();
	}

	@When("I press Join button")
	public void WhenIPressJoinButton() throws Exception {

		PagesCollection.registrationPage = PagesCollection.loginPage.join();
	}

	@When ("I have entered login (.*)")
	public void WhenIHaveEnteredLogin(String login) {
		if (login.equals(CommonUtils.YOUR_USER_1)) {
			login = CommonUtils.yourUsers.get(0).getEmail();
		}
		PagesCollection.loginPage.setLogin(login);
	}

	@When ("I have entered password (.*)")
	public void WhenIHaveEnteredPassword(String password) throws InterruptedException {
		if (password.equals(CommonUtils.YOUR_PASS)) {
			password = CommonUtils.retrieveRealUserContactPasswordValue(password);
		}
		PagesCollection.loginPage.setPassword(password);
	}

	@Then("^I see welcome screen$")
	public void ThenISeeWelcomeScreen() {
		Assert.assertTrue("We don't see welcome buttons", PagesCollection.loginPage.isWelcomeButtonsExist());
	}

}
