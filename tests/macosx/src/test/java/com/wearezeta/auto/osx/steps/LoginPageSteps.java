package com.wearezeta.auto.osx.steps;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginPageSteps {

	private static final Logger LOG = ZetaLogger.getLog(LoginPageSteps.class
			.getName());

	com.wearezeta.auto.web.steps.LoginPageSteps parentSteps = new com.wearezeta.auto.web.steps.LoginPageSteps();

	/**
	 * Enters user email and password into corresponding fields on sign in
	 * screen then taps "Sign In" button
	 * 
	 * @step. ^I Sign in using login (.*) and password (.*)$
	 * 
	 * @param login
	 *            user login string
	 * @param password
	 *            user password string
	 * 
	 * @throws AssertionError
	 *             if login operation was unsuccessful
	 */
	@Given("^I Sign in using login (.*) and password (.*)$")
	public void ISignInUsingLoginAndPassword(String login, String password)
			throws Exception {

		parentSteps.ISignInUsingLoginAndPassword(login, password);
	}

	/**
	 * Presses Sign In button on the corresponding page
	 *
	 * @step. ^I press [Ss]ign [Ii]n button$
	 *
	 * @throws Exception
	 *             if Selenium fails to wait until sign in action completes
	 */
	@When("^I press [Ss]ign [Ii]n button$")
	public void IPressSignInButton() throws Exception {
		parentSteps.IPressSignInButton();
	}

	/**
	 * Verifies whether an account is signed in properly
	 *
	 * @step. ^I am signed in properly$
	 *
	 * @throws Exception
	 */
	@Then("^I am signed in properly$")
	public void IAmSignedInProperly() throws Exception {
		parentSteps.IAmSignedInProperly();
	}

	/**
	 * Checks if a error message is shown on the sign in page
	 *
	 * @step. ^the sign in error message reads (.*)
	 * @param message
	 *            expected error message
	 * @throws Exception
	 */
	@Then("^the sign in error message reads (.*)")
	public void TheSignInErrorMessageReads(String message) throws Exception {
		parentSteps.TheSignInErrorMessageReads(message);
	}

	/**
	 * Checks if a red dot is shown inside the email field on the sign in form
	 *
	 * @step. ^a red dot is shown inside the email field on the sign in form$
	 * @throws Exception
	 */
	@Then("^a red dot is shown inside the email field on the sign in form$")
	public void ARedDotIsShownOnTheEmailField() throws Exception {
		parentSteps.ARedDotIsShownOnTheEmailField();
	}

	/**
	 * Checks if a red dot is shown inside the password field on the sign in
	 * form
	 *
	 * @step. ^a red dot is shown inside the password field on the sign in form$
	 * @throws Exception
	 */
	@Then("^a red dot is shown inside the password field on the sign in form$")
	public void ARedDotIsShownOnThePasswordField() throws Exception {
		parentSteps.ARedDotIsShownOnThePasswordField();
	}

	/**
	 * Types email string into the corresponding input field on sign in page
	 * 
	 * @step. ^I enter email (\\S+)$
	 * 
	 * @param email
	 *            user email string
	 */
	@When("^I enter email (\\S+)$")
	public void IEnterEmail(String email) {
		parentSteps.IEnterEmail(email);
	}

	/**
	 * Types password string into the corresponding input field on sign in page
	 * 
	 * @step. ^I enter password \"([^\"]*)\"$
	 * 
	 * @param password
	 *            password string
	 */
	@When("^I enter password \"([^\"]*)\"$")
	public void IEnterPassword(String password) {
		parentSteps.IEnterPassword(password);
	}

	/**
	 * Verifies whether Sign In page is the current page
	 * 
	 * @step. ^I see Sign In page$
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             if current page is not Sign In page
	 */
	@Given("^I see Sign In page$")
	public void ISeeSignInPage() throws Exception {
		parentSteps.ISeeSignInPage();
	}

	/**
	 * Verifies whether the expected login error is visible on the page
	 * 
	 * @step. ^I see login error \"(.*)\"$
	 * 
	 * @param expectedError
	 *            the text of error
	 * @throws Exception
	 */
	@Then("^I see login error \"(.*)\"$")
	public void ISeeLoginError(String expectedError) throws Exception {
		parentSteps.ISeeLoginError(expectedError);
	}
}
