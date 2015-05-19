package com.wearezeta.auto.web.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PeoplePickerPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Selects user from search results in People Picker
	 * 
	 * @step. ^I select (.*) from People Picker results$
	 * 
	 * @param user
	 *            user name or email
	 * @throws Exception
	 */
	@When("^I select (.*) from People Picker results$")
	public void ISelectUserFromPeoplePickerResults(String user)
			throws Exception {
		user = usrMgr.replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
		PagesCollection.peoplePickerPage.selectUserFromSearchResult(user);
	}

	/**
	 * Input user name/email in search field of People Picker
	 * 
	 * @step. ^I type (.*) in search field of People Picker$
	 * 
	 * @param nameOrEmail
	 * @throws Exception
	 */
	@When("^I type (.*) in search field of People Picker$")
	public void ISearchForUser(String nameOrEmail) throws Exception {
		nameOrEmail = usrMgr.replaceAliasesOccurences(nameOrEmail,
				FindBy.NAME_ALIAS);
		nameOrEmail = usrMgr.replaceAliasesOccurences(nameOrEmail,
				FindBy.EMAIL_ALIAS);
		PagesCollection.peoplePickerPage.searchForUser(nameOrEmail);
	}

	/**
	 * Verify if user is found by Search in People Picker
	 * 
	 * @step. ^I( do not)? see user (.*) found in People Picker$
	 * 
	 * @param donot
	 *            if null method returns true if found otherwise true if not
	 *            found
	 * @param name
	 *            user name string
	 * @throws Exception
	 */
	@When("^I( do not)? see user (.*) found in People Picker$")
	public void ISeeUserFoundInPeoplePicker(String donot, String name)
			throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);

		if (donot == null) {
			Assert.assertTrue(PagesCollection.peoplePickerPage
					.isUserFound(name));
		} else {
			Assert.assertFalse(PagesCollection.peoplePickerPage
					.isUserFound(name));
		}
	}

	/**
	 * Click on the X button next to the suggested contact
	 * 
	 * @step. ^I remove user (.*) from suggestions in People Picker$
	 * 
	 * @param contact
	 *            name of contact
	 * @throws Exception
	 */
	@When("^I remove user (.*) from suggestions in People Picker$")
	public void IClickRemoveButton(String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		PagesCollection.peoplePickerPage.clickRemoveButtonOnSuggestion(contact);
	}

	/**
	 * Click on the + button next to the suggested contact
	 * 
	 * @step. ^I make a connection request for user (.*) directly from People
	 *        Picker$
	 * 
	 * @param contact
	 *            name of contact
	 * @throws Exception
	 */
	@When("^I make a connection request for user (.*) directly from People Picker$")
	public void IClickPlusButton(String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		PagesCollection.peoplePickerPage.clickPlusButtonOnSuggestion(contact);
	}

	/**
	 * Click X button to close People Picker page
	 * 
	 * @step. ^I close People Picker page$
	 * 
	 * @throws Exception
	 */
	@When("^I close People Picker$")
	public void IClosePeoplePicker() throws Exception {
		PagesCollection.peoplePickerPage.closeSearch();
	}

	/**
	 * Clicks on user found by search to open connect dialog
	 * 
	 * @step. I click on not connected user (.*) found in People Picker
	 * 
	 * @param name
	 *            user name string
	 * 
	 * @throws Exception
	 */
	@When("I click on not connected user (.*) found in People Picker")
	public void IClickNotConnecteUserFoundInPeoplePicker(String name)
			throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		PagesCollection.popoverPage = PagesCollection.peoplePickerPage
				.clickNotConnectedUserName(name);
	}

	/**
	 * Creates conversation with users selected in People Picker
	 * 
	 * @step. ^I choose to create conversation from People Picker$
	 * @throws Exception
	 */
	@When("^I choose to create conversation from People Picker$")
	public void IChooseToCreateConversationFromPeoplePicker() throws Exception {
		PagesCollection.peoplePickerPage.createConversation();
	}

	@Then("^I see more than (\\d+) suggestions? in people picker$")
	public void ISeeMoreThanXSuggestionsInPeoplePicker(int count) {
		assertThat("people suggestions",
				PagesCollection.peoplePickerPage.getNumberOfSuggestions(),
				greaterThan(count));
	}

	/**
	 * Verify whether Send Invitation button is visible on People Picker page
	 * 
	 * @step. ^I see Send Invitation button on People Picker page$
	 * 
	 * @throws Exception
	 */
	@When("^I see Send Invitation button on People Picker page$")
	public void ISeeSendInvitationButton() throws Exception {
		PagesCollection.peoplePickerPage
				.waitUntilSendInvitationButtonIsVisible();
	}

	/**
	 * Click Send Invitation button on People Picker page
	 * 
	 * @step. ^I click Send Invitation button on People Picker page$
	 * 
	 * @throws Exception
	 */
	@When("^I click Send Invitation button on People Picker page$")
	public void IClickSendInvitationButton() throws Exception {
		PagesCollection.popoverPage = PagesCollection.peoplePickerPage
				.clickSendInvitationButton();
	}

}
