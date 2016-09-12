package com.wearezeta.auto.web.steps;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.PhoneNumberPasswordPage;
import com.wearezeta.auto.web.pages.PhoneNumberVerificationPage;

import cucumber.api.java.en.When;

public class PhoneNumberPasswordPageSteps {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(PhoneNumberVerificationPage.class.getSimpleName());

    private final TestContext context;

    public PhoneNumberPasswordPageSteps() {
        this.context = new TestContext();
    }

    public PhoneNumberPasswordPageSteps(TestContext context) {
        this.context = context;
    }

    /**
     * Enters password on phone login page
     *
     * @step. ^I enter password (.*) on phone login page$
     *
     * @param password password of the user
     * @throws Throwable
     */
    @When("^I enter password (.*) on phone login page$")
    public void IEnterPhonePassword(String password) throws Throwable {
        context.getPagesCollection().getPage(PhoneNumberPasswordPage.class)
                .enterPassword(password);
    }

    /**
     * Presses Sign In button on phone login page
     *
     * @step. ^I press Sign In button on phone login page$
     *
     * @throws Throwable
     */
    @When("^I press Sign In button on phone login page$")
    public void IClickSignIn() throws Throwable {
        context.getPagesCollection().getPage(PhoneNumberPasswordPage.class)
                .clickSignInButton();
    }
}
