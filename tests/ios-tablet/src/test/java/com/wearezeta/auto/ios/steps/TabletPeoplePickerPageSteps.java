package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.TabletPeoplePickerPage;

import cucumber.api.java.en.When;

public class TabletPeoplePickerPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private TabletPeoplePickerPage getTabletPeoplePickerPage() throws Exception {
		return (TabletPeoplePickerPage) pagesCollecton
				.getPage(TabletPeoplePickerPage.class);
	}

	/**
	 * Verifies that people picker is visible on the ipad popover
	 * 
	 * @step. ^I see People picker page on iPad popover$
	 * @throws Throwable
	 */
	@When("^I see People picker page on iPad popover$")
	public void ISeePeoplePickerPageOniPadPopover() throws Throwable {
		boolean pickerVisible = getTabletPeoplePickerPage()
				.isPeoplePickerPageVisible();
		Assert.assertTrue("Peoplepicker not visible", pickerVisible);
	}

	/**
	 * Taps in the search field in the people picker popover
	 * 
	 * @step. ^I tap on Search input on People picker on iPad popover$
	 * @throws Throwable
	 */
	@When("^I tap on Search input on People picker on iPad popover$")
	public void ITapOnSearchInputOnPeoplePickerOniPadPopover() throws Throwable {
		getTabletPeoplePickerPage().pressIntoSearchField();
	}

	/**
	 * Types in the name of the user in the search field
	 * 
	 * @step. ^I input user name (.*) in People picker search field on iPad
	 *        popover$
	 * @param name
	 *            name of user to search for
	 * @throws Throwable
	 */
	@When("^I input user name (.*) in People picker search field on iPad popover$")
	public void IInputUserNameInPeoplePickerSearchFieldOniPadPopover(String name)
			throws Throwable {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		getTabletPeoplePickerPage().fillTextInTabletPeoplePickerSearchField(
				name);
	}

	/**
	 * Verifies correct user is found by search in list
	 * 
	 * @step. ^I see user (.*) found on People picker on iPad popover$
	 * @param name
	 *            name of user you want to see as result of search
	 * @throws Throwable
	 */
	@When("^I see user (.*) found on People picker on iPad popover$")
	public void ISeeUserFoundOnPeoplePickerOniPadPopover(String name)
			throws Throwable {
		try {
			name = usrMgr.findUserByNameOrNameAlias(name).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		Assert.assertTrue("User :" + name
				+ " is not presented on Pepople picker page",
				getTabletPeoplePickerPage().getSearchResultsElement(name).isPresent());
	}

	/**
	 * Chooses the connected user to add to conversation
	 * 
	 * @step. ^I click on connected user (.*) on People picker on iPad popover$
	 * @param name
	 *            name of user to select an click on
	 * @throws Throwable
	 */
	@When("^I click on connected user (.*) on People picker on iPad popover$")
	public void IClickOnConnectedUserOnPeoplePickerOniPadPopover(String name)
			throws Throwable {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		getTabletPeoplePickerPage().selectConnectedUser(name);
	}

	/**
	 * Clicks the Add to Conversation button to create the group chat
	 * 
	 * @step. ^I click on Add to Conversation button on iPad popover$
	 * @throws Throwable
	 */
	@When("^I click on Add to Conversation button on iPad popover$")
	public void IClickOnAddToConversationButtonOniPadPopover() throws Throwable {
		getTabletPeoplePickerPage().clickAddToConversationButtonOniPadPopover();
	}

}
