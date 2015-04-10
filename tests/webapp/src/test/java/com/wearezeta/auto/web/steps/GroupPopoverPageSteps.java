package com.wearezeta.auto.web.steps;

import java.util.List;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.popovers.GroupPopoverContainer;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GroupPopoverPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Verify that Group Participants popover is shown
	 * 
	 * @step. ^I see Group Participants popover$
	 * @throws Exception
	 * 
	 */
	@When("^I see Group Participants popover$")
	public void ISeeUserProfilePopupPage() throws Exception {
		PagesCollection.popoverPage.waitUntilVisibleOrThrowException();
	}

	/**
	 * Verify that Group Participants popover is not visible
	 * 
	 * @step. ^I do not see Group Participants popover$
	 * @throws Exception
	 * 
	 */
	@Then("^I do not see Group Participants popover$")
	public void IDontSeeUserProfilePopupPage() throws Exception {
		PagesCollection.popoverPage.waitUntilInvisibleOrThrowException();
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
	}

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
	@When("^I choose to create conversation from Group Participants popover$")
	public void IChooseToCreateConversation() throws Exception {
		((GroupPopoverContainer) PagesCollection.popoverPage)
				.clickCreateConversation();
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
}
