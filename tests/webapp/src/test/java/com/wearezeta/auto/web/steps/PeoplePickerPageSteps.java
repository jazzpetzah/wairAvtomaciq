package com.wearezeta.auto.web.steps;

import java.util.List;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.And;
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
		WebappPagesCollection.peoplePickerPage.selectUserFromSearchResult(user);
	}

	/**
	 * Workaround for bug WEBAPP-1386
	 * 
	 * @step. ^I wait for the search field of People Picker to be empty$
	 * 
	 * @throws Exception
	 */
	@When("^I wait for the search field of People Picker to be empty$")
	public void IWaitForSearchFieldToBeEmpty() throws Exception {
		WebappPagesCollection.peoplePickerPage.waitForSearchFieldToBeEmpty();
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
		// adding spaces to ensure trimming of input
		PagesCollection.peoplePickerPage.searchForUser(" " + nameOrEmail + " ");
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
			Assert.assertTrue(WebappPagesCollection.peoplePickerPage
					.isUserFound(name));
		} else {
			Assert.assertTrue(WebappPagesCollection.peoplePickerPage
					.isUserNotFound(name));
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
		WebappPagesCollection.peoplePickerPage.clickRemoveButtonOnSuggestion(contact);
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
		WebappPagesCollection.peoplePickerPage.clickPlusButtonOnSuggestion(contact);
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
		WebappPagesCollection.peoplePickerPage.closeSearch();
	}

	/**
	 * Clicks on user found by search to open connect dialog
	 * 
	 * @step. ^I click on (not connected|pending) user (.*) found in People
	 *        Picker$
	 * 
	 * @param userType
	 *            either "not connected" or "pending"
	 * @param name
	 *            user name string
	 * 
	 * @throws Exception
	 */
	@When("^I click on (not connected|pending) user (.*) found in People Picker$")
	public void IClickNotConnecteUserFoundInPeoplePicker(String userType,
			String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		if (userType.equalsIgnoreCase("not connected")) {
			WebappPagesCollection.popoverPage = WebappPagesCollection.peoplePickerPage
					.clickNotConnectedUserName(name);
		} else if (userType.equalsIgnoreCase("pending")) {
			WebappPagesCollection.popoverPage = WebappPagesCollection.peoplePickerPage
					.clickPendingUserName(name);
		}
	}

	/**
	 * Creates conversation with users selected in People Picker
	 * 
	 * @step. ^I choose to create conversation from People Picker$
	 * @throws Exception
	 */
	@When("^I choose to create conversation from People Picker$")
	public void IChooseToCreateConversationFromPeoplePicker() throws Exception {
		WebappPagesCollection.peoplePickerPage.createConversation();
	}

	@Then("^I see more than (\\d+) suggestions? in people picker$")
	public void ISeeMoreThanXSuggestionsInPeoplePicker(int count) {
		assertThat("people suggestions",
				WebappPagesCollection.peoplePickerPage.getNumberOfSuggestions(),
				greaterThan(count));
	}

	/**
	 * Verify whether Bring Your Friends button is visible on People Picker page
	 * 
	 * @step. ^I see Bring Your Friends button on People Picker page$
	 * 
	 * @throws Exception
	 */
	@When("^I see Bring Your Friends button on People Picker page$")
	public void ISeeSendInvitationButton() throws Exception {
		WebappPagesCollection.peoplePickerPage
				.waitUntilBringYourFriendsButtonIsVisible();
	}

	/**
	 * Verify whether Gmail Import button is visible on People Picker page
	 * 
	 * @step. ^I do not see Gmail Import button on People Picker page$
	 * 
	 * @throws Exception
	 */
	@When("^I do not see Gmail Import button on People Picker page$")
	public void IDoNotSeeGmailImportButton() throws Exception {
		WebappPagesCollection.bringYourFriendsPopover
				.waitUntilGmailImportButtonIsNotVisible();
	}

	/**
	 * Click Bring Your Friends button on People Picker page
	 * 
	 * @step. ^I click Bring Your Friends button on People Picker page$
	 * 
	 * @throws Exception
	 */
	@When("^I click Bring Your Friends button on People Picker page$")
	public void IClickSendInvitationButton() throws Exception {
		WebappPagesCollection.bringYourFriendsPopover = WebappPagesCollection.peoplePickerPage
				.clickBringYourFriendsButton();
	}

	/**
	 * Closes and opens People Picker until Top People list is visible on People
	 * Picker page
	 * 
	 * @step. ^I wait till Top People list appears$
	 * 
	 * @throws Exception
	 */

	@When("^I wait till Top People list appears$")
	public void IwaitTillTopPeopleListAppears() throws Exception {
		if (!WebappPagesCollection.peoplePickerPage.isTopPeopleLabelVisible())
			WebappPagesCollection.contactListPage = WebappPagesCollection.peoplePickerPage
					.closeSearch();
		WebappPagesCollection.peoplePickerPage = WebappPagesCollection.contactListPage
				.openPeoplePicker();
		Assert.assertTrue("Top People list is not shown",
				WebappPagesCollection.peoplePickerPage.isTopPeopleLabelVisible());
	}

	/**
	 * Selects users from Top People in People Picker
	 * 
	 * @step. ^I select (.*) from Top People$
	 * 
	 * @param namesOfTopPeople
	 *            comma separated list of names of top people to select
	 * @throws Exception
	 */

	@When("^I select (.*) from Top People$")
	public void ISelectUsersFromTopPeople(String namesOfTopPeople)
			throws Exception {
		for (String alias : CommonSteps.splitAliases(namesOfTopPeople)) {
			final String userName = usrMgr.findUserByNameOrNameAlias(alias)
					.getName();
			WebappPagesCollection.peoplePickerPage.clickNameInTopPeople(userName);
		}
	}

	private static List<String> selectedTopPeople;

	public static List<String> getSelectedTopPeople() {
		return selectedTopPeople;
	}

	@When("^I remember user names selected in Top People$")
	public void IRememberUserNamesSelectedInTopPeople() throws Exception {
		selectedTopPeople = WebappPagesCollection.peoplePickerPage
				.getNamesOfSelectedTopPeople();
	}

	/**
	 * Verifies whether Search is opened on People Picker Page
	 *
	 * @step. I see Search is opened$
	 *
	 * @throws Exception
	 */
	@Then("^I see Search is opened$")
	public void ISeeSearchIsOpened() throws Exception {
		final String searchMissingMessage = "Search is not visible on People Picker Page";
		Assert.assertTrue(searchMissingMessage,
				WebappPagesCollection.peoplePickerPage.isSearchOpened());
	}

	/**
	 * Verify if group conversation is found by Search in People Picker
	 * 
	 * @step. ^I( do not)? see group conversation (.*) found in People Picker$
	 * 
	 * @param donot
	 *            if null method returns true if found otherwise true if not
	 *            found
	 * @param name
	 *            group conversation name string
	 * @throws Exception
	 */
	@When("^I( do not)? see group conversation (.*) found in People Picker$")
	public void ISeeGroupFoundInPeoplePicker(String donot, String name)
			throws Exception {

		if (donot == null) {
			Assert.assertTrue(WebappPagesCollection.peoplePickerPage
					.isGroupConversationFound(name));
		} else {
			Assert.assertTrue(WebappPagesCollection.peoplePickerPage
					.isGroupConversationNotFound(name));
		}
	}

	/**
	 * Verify if More button is shown in People Picker
	 * 
	 * @step. ^I see More button$
	 * 
	 * @throws Exception
	 */
	@And("^I see More button$")
	public void ISeeMoreButton() throws Exception {
		final String searchMissingMessage = "More button is not visible on People Picker Page";
		Assert.assertTrue(searchMissingMessage,
				WebappPagesCollection.peoplePickerPage.isMoreButtonVisible());

	}

	/**
	 * Click More button on People Picker page
	 * 
	 * @step. ^I click on More button$
	 * 
	 * @throws Exception
	 */
	@When("^I click on More button$")
	public void IClickOnMoreButton() throws Exception {
		WebappPagesCollection.peoplePickerPage.clickMoreButton();
	}

	/**
	 * Verify if More button is shown in People Picker
	 * 
	 * @step. ^I see (\\d+) people in Top people list$
	 * 
	 */
	@Then("^I see (\\d+) people in Top people list$")
	public void ISeeXPeopleInTopPeopleList(int count) {
		assertThat("people suggestions",
				WebappPagesCollection.peoplePickerPage.getNumberOfTopPeople(),
				equalTo(count));
	}
}
