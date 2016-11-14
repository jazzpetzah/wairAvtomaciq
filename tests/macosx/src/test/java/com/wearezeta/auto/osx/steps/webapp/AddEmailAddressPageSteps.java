package com.wearezeta.auto.osx.steps.webapp;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.AddEmailAddressPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AddEmailAddressPageSteps {

    private final TestContext webContext;
    private final TestContext wrapperContext;

    public AddEmailAddressPageSteps() {
        this.webContext = new TestContext();
        this.wrapperContext = new TestContext();
    }
    
    public AddEmailAddressPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }
    
    /**
     * Enter email address of user via user alias
     *
     * @step. ^I enter email of user (.*) on add email address dialog$
     * @param name user alias of user
     * @throws Throwable
     */
    @When("^I enter email of user (.*) on add email address dialog$")
    public void IEnterEmailOfUserOnAddEmailAddressDialog(String name)
            throws Throwable {
        ClientUser user = webContext.getUserManager().findUserByNameOrNameAlias(name);
        webContext.getPagesCollection().getPage(AddEmailAddressPage.class).setEmail(
                user.getEmail());
    }

    /**
     * Enter specified email address (no user alias needed)
     *
     * @step. ^I enter email address (.*) on add email address dialog$
     * @param email email address to be entered
     * @throws Throwable
     */
    @When("^I enter email address (.*) on add email address dialog$")
    public void IEnterEmailOnAddEmailAddressDialog(String email)
            throws Throwable {
        webContext.getPagesCollection().getPage(AddEmailAddressPage.class)
                .setEmail(email);
    }

    /**
     * Enter password
     *
     * @step. ^I enter password (.*) on add email address dialog$
     * @param password password to be entered
     * @throws Exception
     */
    @When("^I enter password (.*) on add email address dialog$")
    public void IEnterPasswordOnAddEmailAddressDialog(String password)
            throws Exception {
        webContext.getPagesCollection().getPage(AddEmailAddressPage.class).setPassword(
                password);
    }

    /**
     * Click button
     *
     * @step. ^I click add button on add email address dialog$
     */
    @When("^I click add button on add email address dialog$")
    public void IClickAddButtonOnAddEmailAddressDialog() throws Exception {
        webContext.getPagesCollection().getPage(AddEmailAddressPage.class)
                .clickAddButton();
    }

    /**
     * Check error message on add email address dialog
     *
     * @step. ^I see error message on add email address dialog saying (.*)$
     * @param message error message
     * @throws Exception
     */
    @Then("^I see error message on add email address dialog saying (.*)$")
    public void ISeeErrorMessageOnAddEmailAddressDialog(String message)
            throws Exception {
        assertThat("invalid email error",
                webContext.getPagesCollection().getPage(AddEmailAddressPage.class)
                .getErrorMessage(), equalTo(message));
    }

}
