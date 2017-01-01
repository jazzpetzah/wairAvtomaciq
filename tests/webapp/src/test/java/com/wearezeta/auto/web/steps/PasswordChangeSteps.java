package com.wearezeta.auto.web.steps;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.external.PasswordChangePage;
import com.wearezeta.auto.web.pages.external.PasswordChangeSuccessfullPage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PasswordChangeSteps {

    @SuppressWarnings("unused")
    private static final Logger log = ZetaLogger.getLog(PasswordChangeSteps.class.getSimpleName());
    private static final int VISIBILITY_TIMEOUT_SECONDS = 15;
    
    private final WebAppTestContext context;

    public PasswordChangeSteps(WebAppTestContext context) {
        this.context = context;
    }

    @Then("^I see Password Change page$")
    public void ISeePage() throws Exception {
        context.getPagesCollection().getPage(PasswordChangePage.class)
                .waitUntilVisible(VISIBILITY_TIMEOUT_SECONDS);
    }

    @When("^I enter password (\\S+) on Password Change page$")
    public void IEnterPassword(String passwordOrAlias) throws Exception {
        passwordOrAlias = context.getUsersManager().replaceAliasesOccurences(passwordOrAlias,
                FindBy.PASSWORD_ALIAS);
        context.getPagesCollection().getPage(PasswordChangePage.class).setNewPassword(
                passwordOrAlias);
    }

    @And("^I click Change Password button on Password Change page$")
    public void IClickChangePasswordButton() throws Exception {
        context.getPagesCollection().getPage(PasswordChangePage.class)
                .clickChangePasswordButton();
    }

    @Then("^I see Password Change Succeeded page$")
    public void ISeePasswordChangeSucceeded() throws Exception {
        assertThat(
                context.getPagesCollection().getPage(
                        PasswordChangeSuccessfullPage.class)
                        .isConfirmationTextVisible(), is(true));
    }

    @Then("^I dont see Password Change Succeeded page$")
    public void IDontSeePasswordChangeSucceeded() throws Exception {
        assertThat(
                context.getPagesCollection().getPage(
                        PasswordChangeSuccessfullPage.class)
                        .isConfirmationTextVisible(), is(false));
    }
}
