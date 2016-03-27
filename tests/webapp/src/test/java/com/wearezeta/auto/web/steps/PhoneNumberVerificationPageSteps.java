package com.wearezeta.auto.web.steps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.PhoneNumberVerificationPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PhoneNumberVerificationPageSteps {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger
			.getLog(PhoneNumberVerificationPage.class.getSimpleName());

        private final TestContext context;
        
    public PhoneNumberVerificationPageSteps() {
        this.context = new TestContext();
    }

    public PhoneNumberVerificationPageSteps(TestContext context) {
        this.context = context;
    }

	/**
	 * Fills out correct country code and phone number for a user
	 * 
	 * @step. ^I enter phone verification code for user (.*)$
	 * 
	 * @param name
	 *            name alias of the user
	 * @throws Throwable
	 */
	@When("^I enter phone verification code for user (.*)$")
	public void IEnterPhoneVerificationCodeForUser(String name)
			throws Throwable {
		ClientUser user = context.getUserManager().findUserByNameOrNameAlias(name);
		String code = BackendAPIWrappers.getLoginCodeByPhoneNumber(user
				.getPhoneNumber());
		context.getPagesCollection().getPage(PhoneNumberVerificationPage.class)
				.enterCode(code);
	}

	/**
	 * Fills out correct country code and phone number for a user that has no
	 * email
	 * 
	 * @step. ^I enter phone verification code for emailless user (.*)$
	 * 
	 * @param name
	 *            name alias of the user
	 * @throws Throwable
	 */
	@When("^I enter phone verification code for emailless user (.*)$")
	public void IEnterPhoneVerificationCodeForEmaillessUser(String name)
			throws Throwable {
		ClientUser user = context.getUserManager().findUserByNameOrNameAlias(name);
		String code = BackendAPIWrappers.getLoginCodeByPhoneNumber(user
				.getPhoneNumber());
		context.getPagesCollection().getPage(PhoneNumberVerificationPage.class)
				.enterCodeForEmaillessUser(code);
	}

	/**
	 * Enters wrong verification code by swapping all numbers into others
	 * 
	 * @step. ^I enter wrong phone verification code for user (.*)$
	 * 
	 * @param name
	 *            name alias of the user
	 * @throws Throwable
	 */
	@When("^I enter wrong phone verification code for user (.*)$")
	public void i_enter_wrong_phone_verification_code_for_user_user_Name(
			String name) throws Throwable {
		ClientUser user = context.getUserManager().findUserByNameOrNameAlias(name);
		String code = BackendAPIWrappers.getLoginCodeByPhoneNumber(user
				.getPhoneNumber());
		String wrongcode = "";
		if (code.charAt(0) != '1') {
			wrongcode = "1" + code.substring(1);
		} else {
			wrongcode = "0" + code.substring(1);
		}
		context.getPagesCollection().getPage(PhoneNumberVerificationPage.class)
				.enterCode(wrongcode);
	}

	/**
	 * Verifies that the error message is correct
	 * 
	 * @step. ^I see invalid phone code error message saying (.*)
	 * 
	 * @param message
	 *            error message
	 * @throws Exception
	 */
	@Then("^I see invalid phone code error message saying (.*)")
	public void TheSignInErrorMessageReads(String message) throws Exception {
		assertThat("invalid phone code error",
				context.getPagesCollection()
						.getPage(PhoneNumberVerificationPage.class)
						.getErrorMessage(), equalTo(message));
	}
}
