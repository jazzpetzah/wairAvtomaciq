package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.web.pages.LoginPage;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.RegistrationPage;

import cucumber.api.java.en.Given;

public class InvitationCodePageSteps {

	private static final String INVITATION_CODE = "zeta22beta";

	/**
	 * Checks that opened page is Invitation page
	 * 
	 * @step. ^I see invitation page$
	 * @throws Exception
	 */
	@Given("^I see invitation page$")
	public void ISeeInvitationPage() throws Exception {
		// FIXME: This a stub
	}

	/**
	 * Enters invitation code into corresponding field then taps "Proceed"
	 * button
	 * 
	 * @step. I enter invitation code
	 *
	 * @throws AssertionError
	 *             if login operation was unsuccessful
	 */
	@Given("I enter invitation code")
	public void IEnterInvitationCode() throws Exception {
		if (PagesCollection.invitationCodePage.isVisible()) {
			PagesCollection.invitationCodePage.inputCode(INVITATION_CODE);
			PagesCollection.invitationCodePage.proceed();
		}

		PagesCollection.loginPage = new LoginPage(
				PagesCollection.invitationCodePage.getDriver(),
				PagesCollection.invitationCodePage.getWait(),
				CommonUtils
						.getWebAppApplicationPathFromConfig(CommonWebAppSteps.class));
		PagesCollection.registrationPage = new RegistrationPage(
				PagesCollection.invitationCodePage.getDriver(),
				PagesCollection.invitationCodePage.getWait());
	}
}
