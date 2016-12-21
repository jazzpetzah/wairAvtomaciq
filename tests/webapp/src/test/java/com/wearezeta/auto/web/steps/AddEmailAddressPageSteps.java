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

    public AddEmailAddressPageSteps(TestContext context) {
        this.context = context;
    }

    @When("^I enter email of user (.*) on add email address dialog$")
    public void IEnterEmailOfUserOnAddEmailAddressDialog(String name)
            throws Throwable {
        ClientUser user = context.getUserManager().findUserByNameOrNameAlias(name);
        context.getPagesCollection().getPage(AddEmailAddressPage.class).setEmail(
                user.getEmail());
    }

    @When("^I enter email address (.*) on add email address dialog$")
    public void IEnterEmailOnAddEmailAddressDialog(String email) throws Throwable {
        context.getPagesCollection().getPage(AddEmailAddressPage.class)
                .setEmail(email);
    }

    @When("^I enter password (.*) on add email address dialog$")
    public void IEnterPasswordOnAddEmailAddressDialog(String password)
            throws Exception {
        context.getPagesCollection().getPage(AddEmailAddressPage.class).setPassword(
                password);
    }

    @When("^I click add button on add email address dialog$")
    public void IClickAddButtonOnAddEmailAddressDialog() throws Exception {
        context.getPagesCollection().getPage(AddEmailAddressPage.class)
                .clickAddButton();
    }

    @Then("^I see error message on add email address dialog saying (.*)$")
    public void ISeeErrorMessageOnAddEmailAddressDialog(String message)
            throws Exception {
        assertThat("invalid email error",
                context.getPagesCollection().getPage(AddEmailAddressPage.class)
                        .getErrorMessage(), equalTo(message));
    }

    @Then("^the email field on add email address dialog is marked as error$")
    public void EmailFieldIsMarkedAsError() throws Exception {
        assertThat("email field marked as error", context.getPagesCollection()
                .getPage(AddEmailAddressPage.class).isEmailFieldMarkedAsError());
    }

    @Then("^the password field on add email address dialog is marked as error$")
    public void ARedDotIsShownOnThePasswordField() throws Exception {
        assertThat("password field marked as error", context.getPagesCollection()
                .getPage(AddEmailAddressPage.class)
                .isPasswordFieldMarkedAsError());
    }

}
