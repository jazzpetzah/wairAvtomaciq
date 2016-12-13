package com.wearezeta.auto.web.steps;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.PhoneNumberPasswordPage;
import com.wearezeta.auto.web.pages.PhoneNumberVerificationPage;
import cucumber.api.java.en.Then;

import cucumber.api.java.en.When;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PhoneNumberPasswordPageSteps {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(PhoneNumberVerificationPage.class.getSimpleName());

    private final TestContext context;

    public PhoneNumberPasswordPageSteps(TestContext context) {
        this.context = context;
    }

    @When("^I enter password (.*) on phone login page$")
    public void IEnterPhonePassword(String password) throws Throwable {
        context.getPagesCollection().getPage(PhoneNumberPasswordPage.class)
                .enterPassword(password);
    }

    @When("^I press Sign In button on phone login page$")
    public void IClickSignIn() throws Throwable {
        context.getPagesCollection().getPage(PhoneNumberPasswordPage.class)
                .clickSignInButton();
    }

    @Then("^I see error \"(.*)\" on phone login page$")
    public void ISeeError(String expectedError) throws Throwable {
        String actualError = context.getPagesCollection().getPage(PhoneNumberPasswordPage.class).getFirstErrorText();
        assertThat(String.format("Error '%s' is NOT visible", expectedError), actualError, equalTo(expectedError));
    }
}
