package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.pages.LoginPage;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.Given;

public class InvitationCodePageSteps {

	private static final String INVITATION_CODE = "zeta22beta";

	private boolean skipInvitation = false;

	/**
	 * Checks that opened page is Invitation page
	 * 
	 * @step. ^I see invitation page$
	 * @throws Exception
	 */
	@Given("^I see invitation page$")
	public void ISeeInvitationPage() throws Exception {
		if (!PagesCollection.invitationCodePage.isVisible()) {
			skipInvitation = true;
		}
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
		if (!skipInvitation) {
			PagesCollection.invitationCodePage.inputCode(INVITATION_CODE);
//			PagesCollection.loginPage = PagesCollection.invitationCodePage
//					.proceed();
			
			PagesCollection.authorizationPage = PagesCollection.invitationCodePage
					.proceed();
		} else {
			final ZetaWebAppDriver driver = (ZetaWebAppDriver) PlatformDrivers
					.getInstance()
					.getDriver(CommonWebAppSteps.CURRENT_PLATFORM);
			PagesCollection.loginPage = new LoginPage(driver,
					PlatformDrivers.createDefaultExplicitWait(driver));
		}
	}
}
