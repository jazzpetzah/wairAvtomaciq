package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.popovers.SendInvitationPopoverContainer;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SendInvitationPopoverPageSteps {

	@Then("^I( do not)?see Send Invitation popover$")
	public void ISeeSendInvitationPopover(String shouldNotBeVisible)
			throws Exception {
		if (shouldNotBeVisible == null) {
			((SendInvitationPopoverContainer) PagesCollection.popoverPage)
					.waitUntilVisibleOrThrowException();
		} else {
			((SendInvitationPopoverContainer) PagesCollection.popoverPage)
					.waitUntilNotVisibleOrThrowException();
		}
	}

	/**
	 * Emulates key combination Cmd/Ctrl+C to copy invitation text into cliboard
	 * 
	 * @step. ^I copy invitation text into clipboard using keyboard$
	 * 
	 * @throws Exception
	 */
	@When("^I copy invitation text into clipboard using keyboard$")
	public void ICopyInvitationUsingKeyboard() throws Exception {
		((SendInvitationPopoverContainer) PagesCollection.popoverPage)
				.copyToClipboard();
	}

}
