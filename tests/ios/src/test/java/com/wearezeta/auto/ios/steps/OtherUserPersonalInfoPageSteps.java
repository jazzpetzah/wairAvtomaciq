package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.OtherUserPersonalInfoPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class OtherUserPersonalInfoPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private OtherUserPersonalInfoPage getOtherUserPersonalInfoPage()
			throws Exception {
		return (OtherUserPersonalInfoPage) pagesCollecton
				.getPage(OtherUserPersonalInfoPage.class);
	}

	@When("^I see (.*) user profile page$")
	public void WhenISeeOtherUserProfilePage(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		Assert.assertTrue(getOtherUserPersonalInfoPage()
				.isOtherUserProfileNameVisible(name));
	}

	@When("^I press Add button$")
	public void WhenIPressAddButton() throws Exception {
		getOtherUserPersonalInfoPage().addContactToChat();
	}

	@When("^I press Continue button$")
	public void WhenIPressContinueButton() throws Exception {
		getOtherUserPersonalInfoPage().continueToAddUser();
	}

	@When("^I swipe up on other user profile page$")
	public void ISwipeUpOnUserProfilePage() throws Exception {
		getOtherUserPersonalInfoPage().swipeUp(1000);
	}

	@When("^I click Remove$")
	public void IClickRemove() throws Exception {
		getOtherUserPersonalInfoPage().removeFromConversation();
	}

	@When("^I see warning message$")
	public void ISeeAreYouSure() throws Throwable {
		Assert.assertTrue(getOtherUserPersonalInfoPage()
				.isRemoveFromConversationAlertVisible());
	}

	@When("^I confirm remove$")
	public void IConfirmRemove() throws Throwable {
		getOtherUserPersonalInfoPage().confirmRemove();
	}

	@Then("^I see the user profile from (.*)$")
	public void ISeeTheUserProfileFrom(String contact) throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		boolean isShown = getOtherUserPersonalInfoPage()
				.isOtherUserProfileEmailVisible(contact);
		Assert.assertTrue(
				"I can see the contacts email on the user profile page",
				isShown);
	}

	@When("I tap on start dialog button on other user profile page")
	public void ITapStartDialogOnOtherUserPage() throws Throwable {
		getOtherUserPersonalInfoPage().clickOnStartDialogButton();
	}

	/**
	 * Close other user personal info page to return to 1on1 dialog
	 * 
	 * @step. ^I close user profile page to return to dialog page$
	 * 
	 * @throws Exception
	 */
	@When("^I close user profile page to return to dialog page$")
	public void ICloseUserProfileForDialog() throws Exception {
		getOtherUserPersonalInfoPage().leavePageToDialogPage();
	}

	@When("^I swipe down on other user profile page$")
	public void ISwipeDownOnUserProfilePage() throws Exception {
		getOtherUserPersonalInfoPage().swipeDown(1000);
	}

	/**
	 * Opens the conversation details menu by clicking the according button
	 * 
	 * @step. ^I press conversation menu button$
	 * @throws Exception
	 */
	@When("^I press conversation menu button$")
	public void IPressConversationMenuButton() throws Exception {
		getOtherUserPersonalInfoPage().openConversationMenu();
	}

	/**
	 * Presses the silence button in the conversation detail menu
	 * 
	 * @step. ^I press menu silence button$
	 * @throws Exception
	 */
	@When("^I press menu silence button$")
	public void IPressMenuSilenceButton() throws Exception {
		getOtherUserPersonalInfoPage().clickSilenceMenuButton();
	}

	/**
	 * Presses the notify button in the conversation detail menu
	 * 
	 * @step. ^I press menu notify button$
	 * @throws Exception
	 */
	@When("^I press menu notify button$")
	public void IPressMenuNotifyButton() throws Exception {
		getOtherUserPersonalInfoPage().clickNotifyMenuButton();
	}

	/**
	 * Open ellipsis menu in conversation details
	 * 
	 * @step. ^I open ellipsis menu$
	 * @throws Exception
	 */
	@When("^I open ellipsis menu$")
	public void IOpenEllipsisMenu() throws Exception {
		getOtherUserPersonalInfoPage().openEllipsisMenu();
	}

	/**
	 * Click archive menu button in ellipsis menu
	 * 
	 * @step. ^I click archive menu button$
	 * @throws Exception
	 */
	@When("^I click archive menu button$")
	public void IClickArchiveMenu() throws Exception {
		getOtherUserPersonalInfoPage().clickArchiveMenuButton();
	}

	/**
	 * Click delete menu button in ellipsis menu
	 * 
	 * @step. ^I click delete menu button$
	 * @throws Exception
	 */
	@When("^I click delete menu button$")
	public void IClickDeleteMenu() throws Exception {
		getOtherUserPersonalInfoPage().clickDeleteMenuButton();
	}

	/**
	 * Click delete to confirm conversation content deletion
	 * 
	 * @step. ^I confirm delete conversation content$
	 * @throws Exception
	 */
	@When("^I confirm delete conversation content$")
	public void IConfirmDelete() throws Exception {
		getOtherUserPersonalInfoPage().clickConfirmDeleteButton();
	}
	
	/**
	 * Select Also Leave option on Delete conversation dialog
	 * 
	 * @step. ^I select Also Leave option on Delete conversation dialog$
	 * 
	 * @throws Exception
	 */
	@When("^I select Also Leave option on Delete conversation dialog$")
	public void ISelectAlsoLeaveOptionOnDeleteDialog() throws Exception {
		getOtherUserPersonalInfoPage().clickAlsoLeaveButton();
	}

	/**
	 * Clicks on the Block button in the profile menu
	 * 
	 * @step. ^I press menu Block button$
	 * @throws Throwable
	 */
	@When("^I press menu Block button$")
	public void IPressMenuBlockButton() throws Throwable {
		getOtherUserPersonalInfoPage().clickBlockMenuButton();
	}

	/**
	 * Confirms the blocking alert by clicking block
	 * 
	 * @step. ^I confirm blocking alert$
	 * @throws Throwable
	 */
	@When("^I confirm blocking alert$")
	public void IConfirmBlockingAlert() throws Throwable {
		getOtherUserPersonalInfoPage().clickBlockMenuButton();
	}

}
