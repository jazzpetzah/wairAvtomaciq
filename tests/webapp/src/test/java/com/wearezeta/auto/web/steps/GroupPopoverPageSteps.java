package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.popovers.GroupPopoverContainer;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.List;
import org.junit.Assert;

public class GroupPopoverPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Verify that Group Participants popover is shown or not
	 *
	 * @step. ^I( do not)? see Group Participants popover$
	 * 
	 * @param shouldNotBeVisible
	 *            is set to null if "do not" part does not exist
	 * 
	 * @throws Exception
	 *
	 */
	@When("^I( do not)? see Group Participants popover$")
	public void ISeeUserProfilePopupPage(String shouldNotBeVisible)
			throws Exception {
		if (shouldNotBeVisible == null) {
			PagesCollection.popoverPage.waitUntilVisibleOrThrowException();
		} else {
			PagesCollection.popoverPage.waitUntilNotVisibleOrThrowException();
		}
	}

	/**
	 * Click leave group chat button on Group Participants popover
	 *
	 * @step. ^I click Leave button on Group Participants popover$
	 *
	 */
	@When("^I click Leave button on Group Participants popover$")
	public void IClickLeaveGroupChat() {
		((GroupPopoverContainer) PagesCollection.popoverPage)
				.clickLeaveGroupChat();
	}

	/**
	 * Confirm leaving group chat by clicking LEAVE button on Group Participants
	 * popover
	 *
	 * @step. ^I confirm leave group conversation on Group Participants popover$
	 * @throws Exception
	 *
	 */
	@When("^I confirm leave group conversation on Group Participants popover$")
	public void IClickConfirmLeaveGroupChat() throws Exception {
		((GroupPopoverContainer) PagesCollection.popoverPage)
				.confirmLeaveGroupChat();
	}

	/**
	 * Verifies whether back button tool tip is correct or not.
	 *
	 * @step. ^I see correct back button tool tip on Group Participants popover$
	 *
	 */
	@Then("^I see correct back button tool tip on Group Participants popover$")
	public void ThenISeeCorrectBackButtonToolTip() {
		Assert.assertTrue(((GroupPopoverContainer) PagesCollection.popoverPage)
				.getBackButtonToolTip().equals("Back"));
	}

	/**
	 * Verifies whether pending button tool tip is correct or not.
	 *
	 * @step. ^I see correct pending button tool tip on Group Participants
	 *        popover$
	 *
	 */
	@Then("^I see correct pending button tool tip on Group Participants popover$")
	public void ThenISeeCorrectPendingButtonToolTip() {
		Assert.assertTrue(((GroupPopoverContainer) PagesCollection.popoverPage)
				.getPendingButtonToolTip().equals("Open conversation"));
	}

	/**
	 * Click on a participant on Group Participants popover
	 *
	 * @step. ^I click on participant (.*) on Group Participants popover$
	 *
	 * @param name
	 *            user name string
	 * @throws Exception
	 */
	@When("^I click on participant (.*) on Group Participants popover$")
	public void IClickOnParticipant(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		((GroupPopoverContainer) PagesCollection.popoverPage)
				.clickOnParticipant(name);
	}

	/**
	 * Verifies whether Pending button is visible on Group Participants popover
	 *
	 * @step. ^I see Pending button on Group Participants popover$
	 *
	 * @throws Exception
	 */
	@Then("^I see Pending button on Group Participants popover$")
	public void ISeePendingButton() throws Exception {
		Assert.assertTrue(
				"Pending button is not visible on Group Participants popover",
				((GroupPopoverContainer) PagesCollection.popoverPage)
						.isPendingButtonVisible());
		Assert.assertTrue(
				"Pending button is not visible on Group Participants popover",
				((GroupPopoverContainer) PagesCollection.popoverPage)
						.getPendingButtonCaption().trim()
						.equalsIgnoreCase("Pending"));
	}

	/**
	 * Click Pending button on Group Participants popover
	 *
	 * @step. ^I click Pending button on Group Participants popover$
	 *
	 * @throws Exception
	 */
	@Then("^I click Pending button on Group Participants popover$")
	public void IClickPendingButton() throws Exception {
		((GroupPopoverContainer) PagesCollection.popoverPage)
				.clickPendingButton();
	}

	/**
	 * Remove participant from group chat by clicking "exit" button
	 *
	 * @step. ^I click Remove button on Group Participants popover$
	 * @throws Exception
	 *
	 */
	@When("^I click Remove button on Group Participants popover$")
	public void IRemoveUserFromGroupChat() throws Exception {
		((GroupPopoverContainer) PagesCollection.popoverPage)
				.clickRemoveFromGroupChat();
	}

	/**
	 * Verifies whether Remove button is visible on Group Participants popover
	 *
	 * @step. ^I see Remove button on Group Participants popover$
	 *
	 * @throws Exception
	 */
	@When("^I see Remove button on Group Participants popover$")
	public void ISeeRemoveUserFromGroupChat() throws Exception {
		((GroupPopoverContainer) PagesCollection.popoverPage)
				.isRemoveButtonVisible();
	}

	/**
	 * Confirm removing from group chat by clicking REMOVE button
	 *
	 * @step. ^I confirm remove from group chat on Group Participants popover$
	 * @throws Exception
	 *
	 */
	@When("^I confirm remove from group chat on Group Participants popover$")
	public void IConfirmRemoveFromGroupChat() throws Exception {
		((GroupPopoverContainer) PagesCollection.popoverPage)
				.confirmRemoveFromGroupChat();
	}

	/**
	 * Verifies that contact is displayed on Group Participants popover
	 *
	 * @step. ^I see (.*) displayed on Group Participants popovere$
	 *
	 * @param contactsAliases
	 * @throws Exception
	 */
	@When("^I see (.*) displayed on Group Participants popover$")
	public void ISeeContactsDisplayed(String contactsAliases) throws Exception {
		List<String> contacts = CommonSteps.splitAliases(contactsAliases);
		for (String contact : contacts) {
			contact = usrMgr.replaceAliasesOccurences(contact,
					FindBy.NAME_ALIAS);
			Assert.assertTrue(((GroupPopoverContainer) PagesCollection.popoverPage)
					.isParticipantVisible(contact));
		}
	}

	/**
	 * Set new title for converstaion on Group Participants popover
	 *
	 * @step. I change group conversation title to (.*) on Group Participants
	 *        popover
	 *
	 * @param title
	 *            new conversation title string
	 */
	@When("^I change group conversation title to (.*) on Group Participants popover$")
	public void IChangeGroupChatTitleTo(String title) {
		((GroupPopoverContainer) PagesCollection.popoverPage)
				.setConversationTitle(title);
	}

	/**
	 * Verify conversation title on Group Participants popover
	 *
	 * @step. ^I see conversation title (.*) on Group Participants popover$
	 *
	 * @param title
	 *            expected title string
	 */
	@Then("^I see conversation title (.*) on Group Participants popover$")
	public void ISeeConversationTitle(String title) {
		Assert.assertEquals(title,
				((GroupPopoverContainer) PagesCollection.popoverPage)
						.getConversationTitle());
	}

	/**
	 * Click on add people button
	 *
	 * @step. ^I click Add People button on Group Participants popover$
	 * @throws Exception
	 *
	 */
	@When("^I click Add People button on Group Participants popover$")
	public void IClickAddPeopleButton() throws Exception {
		((GroupPopoverContainer) PagesCollection.popoverPage)
				.clickAddPeopleButton();
	}

	/**
	 * Verifies there is a question if you want to add people
	 *
	 * @step. ^I see Add People message on Group Participants popover$
	 * @throws Exception
	 *
	 */
	@When("^I see Add People message on Group Participants popover$")
	public void ISeeAddPeopleMessage() throws Exception {
		Assert.assertTrue(((GroupPopoverContainer) PagesCollection.popoverPage)
				.isAddPeopleMessageShown());
	}

	/**
	 * Input user name in search field
	 *
	 * @step. ^I input user name (.*) in search field on Group Participants
	 *        popover$
	 *
	 * @param name
	 * @throws Exception
	 */
	@When("^I input user name (.*) in search field on Group Participants popover$")
	public void ISearchForUser(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		((GroupPopoverContainer) PagesCollection.popoverPage)
				.searchForUser(name);
	}

	/**
	 * Select user found in search results
	 *
	 * @step. ^I select (.*) from Group Participants popover search results$
	 *
	 * @param user
	 * @throws Exception
	 */
	@When("^I select (.*) from Group Participants popover search results$")
	public void ISelectUserFromSearchResults(String user) throws Exception {
		user = usrMgr.replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
		((GroupPopoverContainer) PagesCollection.popoverPage)
				.selectUserFromSearchResult(user);
	}

	/**
	 * Creates conversation with selected users from on Group Participants
	 * popover
	 *
	 * @step. ^I choose to create conversation from Group Participants popover$
	 * @throws Exception
	 */
	@When("^I choose to create group conversation from Group Participants popover$")
	public void IChooseToCreateGroupConversation() throws Exception {
		((GroupPopoverContainer) PagesCollection.popoverPage)
				.clickCreateGroupConversation();
	}

	/**
	 * Creates conversation with one user from on Group Participants popover
	 *
	 * @step. ^I click open conversation from Group Participants popover$
	 * @throws Exception
	 */
	@When("^I click open conversation from Group Participants popover$")
	public void IClickOpenConversation() throws Exception {
		((GroupPopoverContainer) PagesCollection.popoverPage)
				.clickOpenConvButton();
	}

	/**
	 * Click on continue people button
	 *
	 * @step. ^I confirm add to group chat on Group Participants popover$
	 * @throws Exception
	 *
	 */
	@When("^I confirm add to chat on Group Participants popover$")
	public void IClickConfirmAddToChat() throws Exception {
		((GroupPopoverContainer) PagesCollection.popoverPage)
				.confirmAddPeople();
	}

	/**
	 * Verifies whether add people button tool tip is correct or not.
	 *
	 * @throws java.lang.Exception
	 * @step. ^I see correct add people button tool tip$
	 *
	 */
	@Then("^I see correct add people button tool tip$")
	public void ThenISeeCorrectAddPeopleButtonToolTip() throws Exception {
		Assert.assertTrue(((GroupPopoverContainer) PagesCollection.popoverPage)
				.getAddPeopleButtonToolTip().equals(
						"Add people to conversation"));
	}

	/**
	 * Verifies whether leave conversation button tool tip is correct or not.
	 *
	 * @throws java.lang.Exception
	 * @step. ^I see correct leave conversation button tool tip$
	 *
	 */
	@Then("^I see correct leave conversation button tool tip$")
	public void ThenISeeCorrectLeaveConversationButtonToolTip()
			throws Exception {
		Assert.assertTrue(((GroupPopoverContainer) PagesCollection.popoverPage)
				.getLeaveGroupChatButtonToolTip().equals("Leave conversation"));
	}

	/**
	 * Verifies whether rename conversation button tool tip is correct or not.
	 *
	 * @throws java.lang.Exception
	 * @step. ^I see correct rename conversation button tool tip$
	 *
	 */
	@Then("^I see correct rename conversation button tool tip$")
	public void ThenISeeCorrectRenameConversationButtonToolTip()
			throws Exception {
		Assert.assertTrue(((GroupPopoverContainer) PagesCollection.popoverPage)
				.getRenameConversationToolTip().equals(
						"Change conversation name"));
	}

	/**
	 * Verifies whether remove from group button tool tip is correct or not.
	 *
	 * @step. ^I see correct remove from group button tool tip on Group
	 *        Participants popover$
	 *
	 */
	@Then("^I see correct remove from group button tool tip on Group Participants popover$")
	public void ThenISeeCorrectRemoveFromGroupChatButtonToolTip() {
		Assert.assertTrue(((GroupPopoverContainer) PagesCollection.popoverPage)
				.getRemoveFromGroupChatButtonToolTip().equals(
						"Remove from conversation"));
	}

	/**
	 * Compares if name on Single User Profile popover on Group Participants
	 * popover is same as expected
	 *
	 * @throws java.lang.Exception
	 * @step. ^I see username (.*) on Single User Profile popover on Group
	 *        Participants popover$
	 *
	 * @param name
	 *            user name string
	 */
	@When("^I see username (.*) on Group Participants popover$")
	public void IseeUserNameOnUserProfilePage(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		Assert.assertEquals(name,
				((GroupPopoverContainer) PagesCollection.popoverPage)
						.getUserName());
	}

	/**
	 * Verifies whether the users avatar exists on the popover
	 *
	 * @throws java.lang.Exception
	 * @step. ^I see the users avatar on Group Participants User Profile
	 *        popover$
	 *
	 */
	@When("^I see an avatar on Group Participants popover")
	public void IseeAvatarOnUserProfilePage() throws Exception {
		Assert.assertTrue(((GroupPopoverContainer) PagesCollection.popoverPage)
				.isAvatarVisible());
	}

	/**
	 * Verifies Mail is visible on Group Participants popover
	 *
	 * @step. ^I see Mail on Group Participants popover$
	 *
	 * @throws Exception
	 */
	@Then("^I see Mail on Group Participants popover$")
	public void ISeeMailOfUser() throws Exception {
		Assert.assertFalse(((GroupPopoverContainer) PagesCollection.popoverPage)
				.getUserMail().isEmpty());
	}

	/**
	 * Verifies Mail is not visible on Group Participants User Profile popover
	 *
	 * @step. ^I see Mail on Group Participants User Profile popover$
	 *
	 * @throws Exception
	 */
	@Then("^I do not see Mail on Group Participants popover$")
	public void IDoNotSeeMailOfUser() throws Exception {
		Assert.assertTrue(((GroupPopoverContainer) PagesCollection.popoverPage)
				.getUserMail().isEmpty());
	}

	/**
	 * Verifies whether click on mail would open mail client or not.
	 *
	 * @throws java.lang.Exception
	 * @step. ^Click on mail on Single Participant popover would open mail
	 *        client$
	 *
	 */
	@Then("^Click on mail on Group Participants popover would open mail client$")
	public void ThenISeeThatClickOnMailWouldOpenMailClient() throws Exception {
		Assert.assertTrue(((GroupPopoverContainer) PagesCollection.popoverPage)
				.getMailHref().contains("mailto:"));

	}

	/**
	 * Verifies Pending text box is visible on Single Participant popover
	 *
	 * @step. ^I see Pending text box on Group Participants popover$
	 *
	 * @throws Exception
	 */
	@Then("^I see Pending text box on Group Participants popover$")
	public void ISeePendingTextBox() throws Exception {
		Assert.assertTrue(((GroupPopoverContainer) PagesCollection.popoverPage)
				.isPendingTextBoxVisible());
	}

	/**
	 * Verifies whether open conversation button tool tip is correct or not.
	 *
	 * @throws java.lang.Exception
	 * @step. ^I see correct leave conversation button tool tip on Group
	 *        Participants popover$
	 *
	 */
	@Then("^I see correct open conversation button tool tip on Group Participants popover$")
	public void ThenISeeCorrectOpenConversationButtonToolTip() throws Exception {
		Assert.assertTrue(((GroupPopoverContainer) PagesCollection.popoverPage)
				.getOpenConvButtonToolTip().equals("Open conversation"));
	}

	/**
	 * Verifies whether open conversation button is visible on Group
	 * Participants popover
	 *
	 * @step. ^I see open conversation button on Group Participants popover$
	 *
	 * @throws Exception
	 */
	@Then("^I see open conversation button on Group Participants popover$")
	public void ISeeOpenConversationButton() throws Exception {
		Assert.assertTrue(
				"open conversation button is not visible on Group Participants popover",
				((GroupPopoverContainer) PagesCollection.popoverPage)
						.isOpenConvButtonVisible());
		Assert.assertTrue(
				"open conversation button is not visible on Group Participants popover",
				((GroupPopoverContainer) PagesCollection.popoverPage)
						.getOpenConvButtonCaption().trim()
						.equalsIgnoreCase("Open Conversation"));
	}
}
