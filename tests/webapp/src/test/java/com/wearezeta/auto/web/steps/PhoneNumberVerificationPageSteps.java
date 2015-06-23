package com.wearezeta.auto.web.steps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.PhoneNumberVerificationPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PhoneNumberVerificationPageSteps {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger
			.getLog(PhoneNumberVerificationPage.class.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@When("^I enter phone verification code for user (.*)$")
	public void i_enter_phone_verification_code_for_user_user_Name(String name)
			throws Throwable {
		ClientUser user = usrMgr.findUserByNameOrNameAlias(name);
		String code = BackendAPIWrappers.getLoginCodeByPhoneNumber(user
				.getPhoneNumber());
		PagesCollection.contactListPage = PagesCollection.phoneNumberVerificationPage
				.enterCode(code);
	}

	@When("^I enter wrong phone verification code for user (.*)$")
	public void i_enter_wrong_phone_verification_code_for_user_user_Name(
			String name) throws Throwable {
		ClientUser user = usrMgr.findUserByNameOrNameAlias(name);
		String code = BackendAPIWrappers.getLoginCodeByPhoneNumber(user
				.getPhoneNumber());
		String wrongcode = code.replace("0", "1");
		wrongcode = wrongcode.replace("2", "3");
		wrongcode = wrongcode.replace("4", "5");
		wrongcode = wrongcode.replace("6", "7");
		wrongcode = wrongcode.replace("8", "9");
		PagesCollection.contactListPage = PagesCollection.phoneNumberVerificationPage
				.enterCode(wrongcode);
	}

	@Then("^I see invalid phone code error message saying (.*)")
	public void TheSignInErrorMessageReads(String message) throws Exception {
		assertThat("invalid phone code error",
				PagesCollection.phoneNumberVerificationPage.getErrorMessage(),
				equalTo(message));
	}
}
