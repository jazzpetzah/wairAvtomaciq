package com.wearezeta.auto.web.steps;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.When;

public class PhoneNumberLoginPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger
			.getLog(PhoneNumberLoginPage.class.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Input fake phone number for given user
	 * 
	 * @param name
	 *            User name alias
	 * @throws Exception
	 */
	@When("^I sign in using phone number of user (.*)$")
	public void IEnterPhoneNumber(String name) throws Exception {
		ClientUser user = usrMgr.findUserByNameOrNameAlias(name);
		String number = user.getPhoneNumber().toString();
		number = number.replace(PhoneNumber.WIRE_COUNTRY_PREFIX, "");
		PagesCollection.phoneNumberLoginPage
				.enterCountryCode(PhoneNumber.WIRE_COUNTRY_PREFIX);
		PagesCollection.phoneNumberLoginPage.enterPhoneNumber(number);
	}

	@When("^I click on forward button on phone number sign in$")
	public void i_click_on_forward_button_on_phone_number_sign_in()
			throws Throwable {
		PagesCollection.phoneNumberVerificationPage = PagesCollection.phoneNumberLoginPage
				.clickForwardButton();
	}
}
