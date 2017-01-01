package com.wearezeta.auto.web.steps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.PhoneNumberLoginPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PhoneNumberLoginPageSteps {

    @SuppressWarnings("unused")
    private static final Logger log = ZetaLogger.getLog(PhoneNumberLoginPageSteps.class.getSimpleName());

    private final WebAppTestContext context;

    public PhoneNumberLoginPageSteps(WebAppTestContext context) {
        this.context = context;
    }

    @When("^I sign in using phone number of user (.*)$")
    public void ISignInUsignPhoneNumberOfUser(String name) throws Exception {
        ClientUser user = context.getUserManager().findUserByNameOrNameAlias(name);
        context.getPagesCollection().getPage(PhoneNumberLoginPage.class).enterCountryCode(user.getPhoneNumber().getPrefix());
        context.getPagesCollection().getPage(PhoneNumberLoginPage.class).enterPhoneNumber(user.getPhoneNumber().withoutPrefix());
    }

    @When("^I enter phone number (.*) on phone number sign in$")
    public void IEnterPhoneNumber(String number) throws Exception {
        context.getPagesCollection().getPage(PhoneNumberLoginPage.class).enterPhoneNumber(
                new PhoneNumber(PhoneNumber.WIRE_COUNTRY_PREFIX, number).withoutPrefix());
    }

    @When("^I enter country code (.*) on phone number sign in$")
    public void ISelectCountryCode(String code) throws Exception {
        context.getPagesCollection().getPage(PhoneNumberLoginPage.class)
                .enterCountryCode(code);
    }

    @When("^I click on sign in button on phone number sign in$")
    public void IClickOnForwardButtonOnPhoneNumberSignIn() throws Exception {
        context.getPagesCollection().getPage(PhoneNumberLoginPage.class)
                .clickSignInButton();
    }

    @Then("^I see invalid phone number error message saying (.*)$")
    public void ISeeInvalidPhoneNumberErrorMessageSayingX(String message)
            throws Exception {
        assertThat("invalid phone number error",
                context.getPagesCollection().getPage(PhoneNumberLoginPage.class)
                        .getErrorMessage(), equalTo(message));
    }

    @When("I (un)?check option to remember me on phone login page")
    public void ICheckOptionToRememberMe(String uncheck) throws Exception {
        if (uncheck == null) {
            context.getPagesCollection().getPage(PhoneNumberLoginPage.class).checkRememberMe();
        } else {
            context.getPagesCollection().getPage(PhoneNumberLoginPage.class).uncheckRememberMe();
        }
    }
}
