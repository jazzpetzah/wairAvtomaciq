package com.wearezeta.auto.osx.steps.webapp;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import com.wearezeta.auto.web.pages.PhoneNumberLoginPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PhoneNumberLoginPageSteps {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger
            .getLog(PhoneNumberLoginPageSteps.class.getName());

    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
            .getInstance();

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    /**
     * Input fake phone number for given user
     *
     * @param name User name alias
     * @throws Exception
     */
    @When("^I sign in using phone number of user (.*)$")
    public void ISignInUsignPhoneNumberOfUser(String name) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(name);
        webappPagesCollection.getPage(PhoneNumberLoginPage.class).enterCountryCode(user.getPhoneNumber().getPrefix());
        webappPagesCollection.getPage(PhoneNumberLoginPage.class).enterPhoneNumber(user.getPhoneNumber().withoutPrefix());
    }

    /**
     * Enter phone number into the number field
     *
     *
     * @step. ^I enter phone number (.*) on phone number sign in$
     *
     * @param number phone number without country code
     * @throws java.lang.Exception
     */
    @When("^I enter phone number (.*) on phone number sign in$")
    public void IEnterPhoneNumber(String number) throws Exception {
        webappPagesCollection.getPage(PhoneNumberLoginPage.class)
                .enterPhoneNumber(new PhoneNumber(PhoneNumber.WIRE_COUNTRY_PREFIX, number).withoutPrefix());
    }

    /**
     * Directly enters the country code in the text field
     *
     * @step. ^I enter country code (.*) on phone number sign in$
     *
     * @param code country code (for e.g. +49)
     * @throws Exception
     */
    @When("^I enter country code (.*) on phone number sign in$")
    public void ISelectCountryCode(String code) throws Exception {
        webappPagesCollection.getPage(PhoneNumberLoginPage.class).enterCountryCode(code);
    }

    /**
     * Verifies that the error message is correct
     *
     * @step. ^I see invalid phone number error message saying (.*)$
     *
     * @param message
     * @throws Exception
     */
    @Then("^I see invalid phone number error message saying (.*)$")
    public void ISeeInvalidPhoneNumberErrorMessageSayingX(String message)
            throws Exception {
        assertThat("invalid phone number error",
                webappPagesCollection.getPage(PhoneNumberLoginPage.class)
                .getErrorMessage(), equalTo(message));
    }
}
