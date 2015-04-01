package com.wearezeta.auto.ios;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.DialogPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.PeoplePickerPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class OtherUserPersonalInfoPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@When("^I see (.*) user profile page$")
	public void WhenISeeOtherUserProfilePage(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage
				.isOtherUserProfileNameVisible(name));
	}

	@When("^I press Add button$")
	public void WhenIPressAddButton() throws Exception {
		PagesCollection.peoplePickerPage = (PeoplePickerPage) PagesCollection.otherUserPersonalInfoPage
				.addContactToChat();
	}

	@When("^I press Continue button$")
	public void WhenIPressContinueButton() {

		PagesCollection.otherUserPersonalInfoPage.continueToAddUser();
	}

	@When("^I swipe up on other user profile page$")
	public void ISwipeUpOnUserProfilePage() throws Exception {
		PagesCollection.otherUserPersonalInfoPage.swipeUp(1000);
	}

	@When("^I click Remove$")
	public void IClickRemove() {
		PagesCollection.otherUserPersonalInfoPage.removeFromConversation();
	}

	@When("^I see warning message$")
	public void ISeeAreYouSure() throws Throwable {
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage
				.isRemoveFromConversationAlertVisible());
	}

	@When("^I confirm remove$")
	public void IConfirmRemove() throws Throwable {
		PagesCollection.otherUserPersonalInfoPage.confirmRemove();
	}

	@Then("^I see the user profile from (.*)$")
	public void ISeeTheUserProfileFrom(String contact) throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		boolean isShown = PagesCollection.otherUserPersonalInfoPage
				.isOtherUserProfileEmailVisible(contact);
		Assert.assertTrue(
				"I can see the contacts email on the user profile page",
				isShown);
	}

	@When("I tap on start dialog button on other user profile page")
	public void ITapStartDialogOnOtherUserPage() throws Throwable {
		PagesCollection.dialogPage = PagesCollection.otherUserPersonalInfoPage
				.clickOnStartDialogButton();
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
		PagesCollection.dialogPage = PagesCollection.otherUserPersonalInfoPage.leavePageToDialogPage();
	}

	@When("^I swipe down on other user profile page$")
	public void ISwipeDownOnUserProfilePage() throws Exception {
		PagesCollection.dialogPage = (DialogPage) PagesCollection.otherUserPersonalInfoPage
				.swipeDown(1000);
	}
	
	/**
	 * Opens the conversation details menu by clicking the according button
	 * 
	 * @step. ^I press conversation menu button$
	 */
	@When("^I press conversation menu button$")
	public void IPressConversationMenuButton(){
		PagesCollection.otherUserPersonalInfoPage.openConversationMenu();
	}

	/**
	 * Presses the silence button in the conversation detail menu
	 * 
	 * @step. ^I press menu silence button$
	 */
	@When("^I press menu silence button$")
	public void IPressMenuSilenceButton(){
		PagesCollection.otherUserPersonalInfoPage.clickSilenceMenuButton();
	}

}
