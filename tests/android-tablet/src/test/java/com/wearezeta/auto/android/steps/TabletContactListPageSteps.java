package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.When;

public class TabletContactListPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Taps profile button (SelfName)
	 * 
	 * @step. ^I tap on profile link$
	 * 
	 * @throws Exception
	 */
	@When("^I tap on profile link$")
	public void WhenITapOnProfileLink() throws Exception {
		AndroidPagesCollection.contactListPage.tapOnProfileLink();
	}

	/**
	 * Check that conversation list is visible and self name is correct
	 * 
	 * @step. ^I see conversation list loaded with my name (.*)$
	 * 
	 * @param name
	 *            user name string
	 * 
	 * @throws Exception
	 */
	@When("^I see conversation list loaded with my name (.*)$")
	public void WhenISeeConversationListWithMyName(String name)
			throws Exception {
		Assert.assertTrue(AndroidPagesCollection.contactListPage
				.isPeoplePickerButtonVisible());
		String currentName = null;
		try {
			currentName = usrMgr.findUserByNameOrNameAlias(name).getName();
		} catch (NoSuchUserException e) {
			currentName = name;
		}
		Assert.assertTrue(currentName.equals(AndroidPagesCollection.contactListPage
				.getSelfName()));
	}

	/**
	 * Tap on selected user in contact list
	 * 
	 * @step. ^I tap on tablet contact name (.*)$
	 * 
	 * @param value
	 *            user name string
	 * 
	 * @throws Exception
	 */
	@When("^I tap on tablet contact name (.*)$")
	public void WhenITapOnTabletContactName(String value) throws Exception {
		try {
			value = usrMgr.findUserByNameOrNameAlias(value).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		AndroidPagesCollection.currentPage = AndroidPagesCollection.contactListPage
				.tapOnName(value);
		if (AndroidPagesCollection.currentPage instanceof PersonalInfoPage) {
			AndroidPagesCollection.personalInfoPage = (PersonalInfoPage) AndroidPagesCollection.currentPage;
			AndroidPagesCollection.personalInfoPage = (TabletPersonalInfoPage) AndroidPagesCollection.personalInfoPage;
		} else if (AndroidPagesCollection.currentPage instanceof DialogPage) {
			AndroidPagesCollection.dialogPage = (DialogPage) AndroidPagesCollection.currentPage;
			AndroidPagesCollection.dialogPage = AndroidPagesCollection.contactListPage
					.initDialogPage();
		}
	}

	@When("^I swipe down on tablet contact list$")
	public void ISwipeDownContactList() throws Exception {
		AndroidPagesCollection.peoplePickerPage = (PeoplePickerPage) AndroidPagesCollection.contactListPage
				.swipeDown(1000);
		AndroidPagesCollection.peoplePickerPage = AndroidPagesCollection.contactListPage
				.initPeoplePickerPage();
	}
}
