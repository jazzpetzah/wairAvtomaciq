package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.*;

public class LoginPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Verify whether Welcome screen is visible
	 * 
	 * @step. ^I see [Ww]elcome screen$
	 * @throws Exception
	 */
	@Given("^I see [Ww]elcome screen$")
	public void GivenISeeWelcomeScreen() throws Exception {
		Assert.assertTrue(PagesCollection.loginPage.waitForInitialScreen());
	}

	/**
	 * Presses the "forgot password" button
	 * 
	 * @step. ^I press FORGOT PASSWORD button$
	 * 
	 * @throws Exception
	 */
	@When("I press FORGOT PASSWORD button")
	public void WhenIPressForgotPasswordButton() throws Exception {
		PagesCollection.commonAndroidPage = PagesCollection.loginPage
				.forgotPassword();
	}

	/**
	 * Presses the Log in Button underneath the email and password fields
	 * 
	 * @step. ^I press FORGOT PASSWORD button$
	 * 
	 * @throws Exception
	 */
	@When("I press Log in button")
	public void WhenIPressLogInButton() throws Exception {
		PagesCollection.contactListPage = PagesCollection.loginPage.LogIn();
		Assert.assertTrue("Login finished",
				PagesCollection.loginPage.waitForLogin());
	}

	/**
	 * Presses the Join button to begin the registration process
	 * 
	 * @step. ^I press Join button$
	 */
	@When("I press Join button")
	public void WhenIPressJoinButton() throws Exception {
		PagesCollection.registrationPage = PagesCollection.loginPage.join();
	}

	/**
	 * Checks to see that the login error message contains the correct text
	 * 
	 * @step. ^I see error message \"(.*)\"$
	 * @param expectedMsg
	 *            the expected error message
	 * 
	 * @throws Exception
	 */
	@Then("^I see error message \"(.*)\"$")
	public void ISeeErrorMessage(String expectedMsg) throws Exception {
		PagesCollection.loginPage.waitForLogin();
		PagesCollection.loginPage.verifyErrorMessageText(expectedMsg);
	}

}
