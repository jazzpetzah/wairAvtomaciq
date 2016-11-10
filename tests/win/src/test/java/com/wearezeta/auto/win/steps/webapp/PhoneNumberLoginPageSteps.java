package com.wearezeta.auto.win.steps.webapp;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import com.wearezeta.auto.web.pages.PhoneNumberLoginPage;
import com.wearezeta.auto.win.common.WrapperTestContext;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PhoneNumberLoginPageSteps {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger
            .getLog(PhoneNumberLoginPageSteps.class.getName());
    private final WrapperTestContext context;

    public PhoneNumberLoginPageSteps() {
        this.context = new WrapperTestContext();
    }

    public PhoneNumberLoginPageSteps(WrapperTestContext context) {
        this.context = context;
    }

    @When("^I sign in using phone number of user (.*)$")
    public void ISignInUsignPhoneNumberOfUser(String name) throws Exception {
        ClientUser user = context.getUserManager().findUserByNameOrNameAlias(name);
        String number = user.getPhoneNumber().toString();
        number = number.replace(PhoneNumber.WIRE_COUNTRY_PREFIX, "");
        context.getWebappPagesCollection().getPage(PhoneNumberLoginPage.class)
                .enterCountryCode(PhoneNumber.WIRE_COUNTRY_PREFIX);
        context.getWebappPagesCollection().getPage(PhoneNumberLoginPage.class)
                .enterPhoneNumber(number);
    }

    @When("^I enter phone number (.*) on phone number sign in$")
    public void IEnterPhoneNumber(String number) throws Exception {
        context.getWebappPagesCollection().getPage(PhoneNumberLoginPage.class).enterPhoneNumber(number);
    }

    @When("^I enter country code (.*) on phone number sign in$")
    public void ISelectCountryCode(String code) throws Exception {
        context.getWebappPagesCollection().getPage(PhoneNumberLoginPage.class).enterCountryCode(code);
    }

    @When("^I click on forward button on phone number sign in$")
    public void IClickOnForwardButtonOnPhoneNumberSignIn() throws Exception {
        context.getWebappPagesCollection().getPage(PhoneNumberLoginPage.class).clickSignInButton();
    }

    @Then("^I see invalid phone number error message saying (.*)$")
    public void ISeeInvalidPhoneNumberErrorMessageSayingX(String message) throws Exception {
        assertThat("invalid phone number error", context.getWebappPagesCollection().getPage(PhoneNumberLoginPage.class)
                .getErrorMessage(), equalTo(message));
    }
}
