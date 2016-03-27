package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.AddEmailAddressPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AddEmailAddressPageSteps {

    private final TestContext context;
    
    public AddEmailAddressPageSteps() {
        this.context = new TestContext();
    }

    public AddEmailAddressPageSteps(TestContext context) {
        this.context = context;
    }

	/**
	 * Enter email address of user via user alias
	 * 
	 * @step. ^I enter email of user (.*) on add email address dialog$
	 * @param name
	 *            user alias of user
	 * @throws Throwable
	 */
	@When("^I enter email of user (.*) on add email address dialog$")
	public void IEnterEmailOfUserOnAddEmailAddressDialog(String name)
			throws Throwable {
		ClientUser user = context.getUserManager().findUserByNameOrNameAlias(name);
		context.getPagesCollection().getPage(AddEmailAddressPage.class).setEmail(
				user.getEmail());
	}

	/**
	 * Enter specified email address (no user alias needed)
	 * 
	 * @step. ^I enter email address (.*) on add email address dialog$
	 * @param email
	 *            email address to be entered
	 * @throws Throwable
	 */
	@When("^I enter email address (.*) on add email address dialog$")
	public void IEnterEmailOnAddEmailAddressDialog(String email)
			throws Throwable {
		context.getPagesCollection().getPage(AddEmailAddressPage.class)
				.setEmail(email);
	}

	/**
	 * Enter password
	 * 
	 * @step. ^I enter password (.*) on add email address dialog$
	 * @param password
	 *            password to be entered
	 * @throws Exception
	 */
	@When("^I enter password (.*) on add email address dialog$")
	public void IEnterPasswordOnAddEmailAddressDialog(String password)
			throws Exception {
		context.getPagesCollection().getPage(AddEmailAddressPage.class).setPassword(
				password);
	}

	/**
	 * Click button
	 * 
	 * @step. ^I click add button on add email address dialog$
	 */
	@When("^I click add button on add email address dialog$")
	public void IClickAddButtonOnAddEmailAddressDialog() throws Exception {
		context.getPagesCollection().getPage(AddEmailAddressPage.class)
				.clickAddButton();
	}

	/**
	 * See Skip for now button
	 * 
	 * @step. ^I see Skip for now button on add email address dialog$
	 */
	@When("^I see Skip for now button on add email address dialog$")
	public void ISeeSkipForNowButtonOnAddEmailAddressDialog() throws Exception {
		context.getPagesCollection().getPage(AddEmailAddressPage.class)
				.isSkipButtonVisible();
	}

	/**
	 * Click Skip for now button
	 * 
	 * @step. ^I click Skip for now button on add email address dialog$
	 */
	@When("^I click Skip for now button on add email address dialog$")
	public void IClickSkipForNowButtonOnAddEmailAddressDialog()
			throws Exception {
		context.getPagesCollection().getPage(AddEmailAddressPage.class)
				.clickSkipForNowButton();
	}

	/**
	 * Check error message on add email address dialog
	 * 
	 * @step. ^I see error message on add email address dialog saying (.*)$
	 * @param message
	 *            error message
	 * @throws Exception
	 */
	@Then("^I see error message on add email address dialog saying (.*)$")
	public void ISeeErrorMessageOnAddEmailAddressDialog(String message)
			throws Exception {
		assertThat("invalid email error",
				context.getPagesCollection().getPage(AddEmailAddressPage.class)
						.getErrorMessage(), equalTo(message));
	}

	/**
	 * Checks if a orange line is shown around the email field on add email
	 * address dialog
	 *
	 * @step. ^the email field on add email address dialog is marked as error$
	 * @throws Exception
	 */
	@Then("^the email field on add email address dialog is marked as error$")
	public void EmailFieldIsMarkedAsError() throws Exception {
		assertThat("email field marked as error", context.getPagesCollection()
				.getPage(AddEmailAddressPage.class).isEmailFieldMarkedAsError());
	}

	/**
	 * Checks if a orange line is shown around the password field on add email
	 * address dialog form
	 *
	 * @step. ^the password field on add email address dialog is marked as
	 *        error$
	 * @throws Exception
	 */
	@Then("^the password field on add email address dialog is marked as error$")
	public void ARedDotIsShownOnThePasswordField() throws Exception {
		assertThat("password field marked as error", context.getPagesCollection()
				.getPage(AddEmailAddressPage.class)
				.isPasswordFieldMarkedAsError());
	}

}
