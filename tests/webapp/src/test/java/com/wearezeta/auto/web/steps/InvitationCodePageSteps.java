package com.wearezeta.auto.web.steps;

import java.io.IOException;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.web.pages.LoginPage;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.Given;

public class InvitationCodePageSteps {

	private static final String INVITATION_CODE = "zeta22beta";

	private boolean skipInvitation = false;

	@Given("^I see invitation page$")
	public void ISeeInvitationPage() {
		if (!PagesCollection.invitationCodePage.isVisible()) {
			skipInvitation = true;
		}
	}

	@Given("I enter invitation code")
	public void IEnterInvitationCode() throws IOException {
		if (!skipInvitation) {
			PagesCollection.invitationCodePage.inputCode(INVITATION_CODE);
			PagesCollection.loginPage = PagesCollection.invitationCodePage
					.proceed();
		} else {
			PagesCollection.loginPage = new LoginPage(
					CommonUtils
							.getWebAppAppiumUrlFromConfig(InvitationCodePageSteps.class),
					CommonUtils
							.getWebAppApplicationPathFromConfig(InvitationCodePageSteps.class));
		}
	}
}
