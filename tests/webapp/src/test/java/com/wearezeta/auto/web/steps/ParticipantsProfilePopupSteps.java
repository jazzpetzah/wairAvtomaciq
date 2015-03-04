package com.wearezeta.auto.web.steps;

import java.util.List;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ParticipantsProfilePopupSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Verify that participant profile popup is shown
	 * 
	 * @step. ^I see Participant Profile Popup Page$
	 * @throws Exception
	 * 
	 */
	@When("^I see Participant Profile Popup Page$")
	public void ISeeUserProfilePopupPage() throws Exception {
		Assert.assertTrue(PagesCollection.participantsPopupPage
				.isParticipantsProfilePopupPageVisible());
	}

	/**
	 * Click on leave group chat button in participant profile popup
	 * 
	 * @step. ^I click leave group chat$
	 * 
	 */
	@When("^I click leave group chat$")
	public void IClickLeaveGroupChat() {
		PagesCollection.participantsPopupPage.leaveGroupChat();
	}

	/**
	 * Confirm leaving group chat by clicking LEAVE button
	 * 
	 * @step. ^I confirm leave group chat$
	 * 
	 */
	@When("^I confirm leave group chat$")
	public void IClickConfirmLeaveGroupChat() {
		PagesCollection.participantsPopupPage.confirmLeaveGroupChat();
	}

	/**
	 * Click on a participant in participant profile popup
	 * 
	 * @step. ^I click on participant (.*)$
	 * 
	 * @param name
	 *            user name string
	 */
	@When("^I click on participant (.*)$")
	public void IClickOnParticipant(String name) {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		PagesCollection.participantsPopupPage.clickOnParticipant(name);
	}

	/**
	 * Remove participant from group chat by clicking - button
	 * 
	 * @step. ^I remove user from group chat$
	 * 
	 */
	@When("^I remove user from group chat$")
	public void IRemoveUserFromGroupChat() {
		PagesCollection.participantsPopupPage.removeFromGroupChat();
	}

	/**
	 * Confirm removing from group chat by clicking REMOVE button
	 * 
	 * @step. ^I confirm remove from group chat$
	 * 
	 */
	@When("^I confirm remove from group chat$")
	public void IClickConfirmRemoveFromGroupChat() {
		PagesCollection.participantsPopupPage.confirmRemoveFromGroupChat();
	}

	/**
	 * Verifies that contact is displayed on Participant profile popup
	 * 
	 * @step. ^I see (.*) displayed on Participant Profile Page$
	 * 
	 * @param contactsAliases
	 */
	@When("^I see (.*) displayed on Participant Profile Page$")
	public void ISeeContactsDisplayedOnParticipantPopup(String contactsAliases) {

		List<String> contacts = CommonSteps.splitAliases(contactsAliases);
		for (String s : contacts) {
			s = usrMgr.replaceAliasesOccurences(s, FindBy.NAME_ALIAS);
			Assert.assertTrue(PagesCollection.participantsPopupPage
					.isParticipantVisible(s));
		}
	}

	/**
	 * Set new title for converstaion in participants popup
	 * 
	 * @step. I change group conversation title to (.*)
	 * 
	 * @param title
	 *            new conversation title string
	 */
	@When("I change group conversation title to (.*)")
	public void IChangeGroupChatTitleTo(String title) {
		PagesCollection.participantsPopupPage.setConversationTitle(title);
	}

	/**
	 * Verify conversation title in participants popup
	 * 
	 * @step. ^I see conversation title (.*) in Participants profile$
	 * 
	 * @param title
	 *            expected title string
	 */
	@Then("^I see conversation title (.*) in Participants profile$")
	public void ISeeConversationTitle(String title) {
		Assert.assertEquals(title,
				PagesCollection.participantsPopupPage.getConversationTitle());
	}
}
