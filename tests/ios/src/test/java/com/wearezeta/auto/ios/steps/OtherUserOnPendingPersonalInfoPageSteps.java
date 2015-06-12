package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.OtherUserOnPendingProfilePage;

import cucumber.api.java.en.When;

public class OtherUserOnPendingPersonalInfoPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private OtherUserOnPendingProfilePage getOtherUserOnPendingProfilePage()
			throws Exception {
		return (OtherUserOnPendingProfilePage) pagesCollecton
				.getPage(OtherUserOnPendingProfilePage.class);
	}

	@When("^I see (.*) user pending profile page$")
	public void WhenISeeOtherUserProfilePage(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		Assert.assertTrue("Username is not displayed",
				getOtherUserOnPendingProfilePage().isUserNameDisplayed(name));
		Assert.assertTrue("Close button not displayed",
				getOtherUserOnPendingProfilePage().isClosePageButtonVisible());
		Assert.assertTrue("Pending label is not displayed",
				getOtherUserOnPendingProfilePage().isPendingLabelVisible());
	}

	@When("^I click on start conversation button on pending profile page$")
	public void WhenIClickOnStartConversatonButtonOnPendingProfilePage()
			throws Exception {
		getOtherUserOnPendingProfilePage().clickStartConversationButton();
	}

}
