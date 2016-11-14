package com.wearezeta.auto.win.steps.webapp;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.web.pages.AddEmailAddressPage;
import com.wearezeta.auto.win.common.WrapperTestContext;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AddEmailAddressPageSteps {
    private final WrapperTestContext context;

    public AddEmailAddressPageSteps() {
        this.context = new WrapperTestContext();
    }

    public AddEmailAddressPageSteps(WrapperTestContext context) {
        this.context = context;
    }

    @When("^I enter email of user (.*) on add email address dialog$")
    public void IEnterEmailOfUserOnAddEmailAddressDialog(String name) throws Throwable {
        ClientUser user = context.getUserManager().findUserByNameOrNameAlias(name);
        context.getWebappPagesCollection().getPage(AddEmailAddressPage.class).setEmail(user.getEmail());
    }

    @When("^I enter email address (.*) on add email address dialog$")
    public void IEnterEmailOnAddEmailAddressDialog(String email) throws Throwable {
        context.getWebappPagesCollection().getPage(AddEmailAddressPage.class).setEmail(email);
    }

    @When("^I enter password (.*) on add email address dialog$")
    public void IEnterPasswordOnAddEmailAddressDialog(String password) throws Exception {
        context.getWebappPagesCollection().getPage(AddEmailAddressPage.class).setPassword(password);
    }

    @When("^I click add button on add email address dialog$")
    public void IClickAddButtonOnAddEmailAddressDialog() throws Exception {
        context.getWebappPagesCollection().getPage(AddEmailAddressPage.class).clickAddButton();
    }

    @Then("^I see error message on add email address dialog saying (.*)$")
    public void ISeeErrorMessageOnAddEmailAddressDialog(String message) throws Exception {
        assertThat("invalid email error", context.getWebappPagesCollection().getPage(AddEmailAddressPage.class).getErrorMessage(), equalTo(message));
    }

    @Then("^a red dot is shown inside the email field on add email address dialog$")
    public void ARedDotIsShownOnTheEmailField() throws Exception {
        assertThat("Error on email field", context.getWebappPagesCollection().getPage(AddEmailAddressPage.class).isEmailFieldMarkedAsError());
    }

    @Then("^a red dot is shown inside the password field on add email address dialog$")
    public void ARedDotIsShownOnThePasswordField() throws Exception {
        assertThat("Error on password field", context.getWebappPagesCollection().getPage(AddEmailAddressPage.class).isPasswordFieldMarkedAsError());
    }
}
