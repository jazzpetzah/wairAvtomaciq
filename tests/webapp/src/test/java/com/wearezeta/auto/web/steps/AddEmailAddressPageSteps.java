package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AddEmailAddressPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

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
		WebappPagesCollection.addEmailAddressPage.setEmail(user.getEmail());
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
		WebappPagesCollection.addEmailAddressPage.setEmail(email);
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
	public void IEnterPasswordOnAddEmailAddressDialog(String password) throws Exception {
		WebappPagesCollection.addEmailAddressPage.setPassword(password);
	}

	/**
	 * Click button
	 * 
	 * @step. ^I click add button on add email address dialog$
	 */
	@When("^I click add button on add email address dialog$")
	public void IClickAddButtonOnAddEmailAddressDialog() {
		WebappPagesCollection.addEmailAddressPage.clickAddButton();
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
				WebappPagesCollection.addEmailAddressPage.getErrorMessage(),
				equalTo(message));
	}

	/**
	 * Checks if a red dot is shown inside the email field on add email address
	 * dialog
	 *
	 * @step. ^a red dot is shown inside the email field on add email address
	 *        dialog$
	 * @throws Exception
	 */
	@Then("^a red dot is shown inside the email field on add email address dialog$")
	public void ARedDotIsShownOnTheEmailField() throws Exception {
		assertThat("Red dot on email field",
				WebappPagesCollection.addEmailAddressPage.isRedDotOnEmailField());
	}

	/**
	 * Checks if a red dot is shown inside the password field on add email
	 * address dialog form
	 *
	 * @step. ^a red dot is shown inside the password field on add email address
	 *        dialog$
	 * @throws Exception
	 */
	@Then("^a red dot is shown inside the password field on add email address dialog$")
	public void ARedDotIsShownOnThePasswordField() throws Exception {
		assertThat("Red dot on password field",
				WebappPagesCollection.addEmailAddressPage.isRedDotOnPasswordField());
	}
}
