package com.wearezeta.auto.osx.steps;

import org.junit.Assert;

import com.wearezeta.auto.osx.pages.PagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class VerificationPageSteps {
	/**
	 * Checks that e-mail confirmation page appers after Create Account button
	 * clicked
	 * 
	 * @step. I see confirmation page
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             if confirmation page did not appear
	 */
	@Then("I see confirmation page")
	public void ISeeConfirmationPage() throws Exception {
		Assert.assertTrue(PagesCollection.verificationPage
				.isVerificationRequested());
	}

	/**
	 * Opens activation link in browser and stores response message
	 * 
	 * @step. ^I open activation link in browser$
	 * 
	 * @throws Exception
	 */
	@When("^I open activation link in browser$")
	public void IOpenActivationLinkInBrowser() throws Exception {
		PagesCollection.verificationPage.activateUserFromBrowser();
	}

	/**
	 * Checks that response message from activation using browser says that user
	 * activated successfully
	 * 
	 * @step. ^I see that user activated$
	 * 
	 * @throws AssertionError
	 *             if activation response different from expected on success
	 */
	@When("^I see that user activated$")
	public void ISeeUserActivated() {
		Assert.assertTrue(PagesCollection.verificationPage.isUserActivated());
	}
}
