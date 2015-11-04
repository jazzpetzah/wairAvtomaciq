package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.pages.AddEmailAddressPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AddEmailAddressPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

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
		ClientUser user = usrMgr.findUserByNameOrNameAlias(name);
		webappPagesCollection.getPage(AddEmailAddressPage.class).setEmail(
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
		webappPagesCollection.getPage(AddEmailAddressPage.class)
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
		webappPagesCollection.getPage(AddEmailAddressPage.class).setPassword(
				password);
	}

	/**
	 * Click button
	 * 
	 * @step. ^I click add button on add email address dialog$
	 */
	@When("^I click add button on add email address dialog$")
	public void IClickAddButtonOnAddEmailAddressDialog() throws Exception {
		webappPagesCollection.getPage(AddEmailAddressPage.class)
				.clickAddButton();
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
				webappPagesCollection.getPage(AddEmailAddressPage.class)
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
		assertThat("email field marked as error", webappPagesCollection
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
		assertThat("password field marked as error", webappPagesCollection
				.getPage(AddEmailAddressPage.class)
				.isPasswordFieldMarkedAsError());
	}
}
