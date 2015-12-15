package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.OtherUserOnPendingProfilePage;

import cucumber.api.java.en.Then;
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
	}

	@When("^I see cancel request button on pending profile page$")
	public void ISeeCancelRequestButtonOnPendingProfilePage() throws Exception {
		Assert.assertTrue("Pending label is not displayed",
				getOtherUserOnPendingProfilePage()
						.isCancelRequestButtonVisible());
	}

	/**
	 * Click on Cancel request button
	 * 
	 * @step. I click Cancel request button
	 * 
	 * @throws Exception
	 */
	@When("I click Cancel request button")
	public void IClickCancelRequestButton() throws Exception {
		getOtherUserOnPendingProfilePage().clickCancelRequestButton();
	}

	@When("^I click on start conversation button on pending profile page$")
	public void WhenIClickOnStartConversatonButtonOnPendingProfilePage()
			throws Exception {
		getOtherUserOnPendingProfilePage().clickStartConversationButton();
	}

	/**
	 * Verify if Cancel request confirmation page is shown
	 * 
	 * @step. I see Cancel request confirmation page
	 * 
	 * @throws Exception
	 */
	@When("I see Cancel request confirmation page")
	public void ISeeCancelRequestConfirmationPage() throws Exception {
		Assert.assertTrue("Cancel request confirmation page is not shown",
				getOtherUserOnPendingProfilePage()
						.isCancelRequestConfirmationVisible());
	}

	/**
	 * Click on yes button on Confirm Cancel Request page
	 * 
	 * @step. I confirm Cancel request by click on Yes button
	 * 
	 * @throws Exception
	 */
	@When("I confirm Cancel request by click on Yes button")
	public void IConfirCancelReques() throws Exception {
		getOtherUserOnPendingProfilePage().clickConfirmCancelRequestButton();
	}

	/**
	 * Presses back button to return to group info page or popover
	 * 
	 * @step. ^I go back to group info page or popover$
	 * @throws Throwable
	 */
	@When("^I go back to group info page or popover$")
	public void IGoBackToGroupInfoPageOrPopover() throws Throwable {
		getOtherUserOnPendingProfilePage()
				.clickBackButtonToReturnToGroupPageOrPopover();
	}

	/**
	 * Verifies that remove from group button is visible on pending user page
	 * 
	 * @step. ^I see remove from group conversation button$
	 * @throws Throwable
	 */
	@Then("^I see remove from group conversation button$")
	public void i_see_remove_from_group_conversation_button() throws Throwable {
		getOtherUserOnPendingProfilePage()
				.isRemoveFromGroupConversationVisible();
	}

}
