package com.wearezeta.auto.web.steps;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.PhoneNumberVerificationPage;

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

}
