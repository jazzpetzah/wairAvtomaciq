package com.wearezeta.auto.android_tablet.steps;

import cucumber.api.java.en.When;

public class TabletContactListPageSteps {

//	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Taps profile button (SelfName)
	 * 
	 * @step. ^I tap on profile link$
	 * 
	 * @throws Exception
	 */
	@When("^I tap on profile link$")
	public void WhenITapOnProfileLink() throws Exception {
//		AndroidTabletPagesCollection.contactListPage.tapOnProfileLink();
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
//		Assert.assertTrue(AndroidTabletPagesCollection.contactListPage
//				.isPeoplePickerButtonVisible());
//		String currentName = null;
//		try {
//			currentName = usrMgr.findUserByNameOrNameAlias(name).getName();
//		} catch (NoSuchUserException e) {
//			currentName = name;
//		}
//		Assert.assertTrue(currentName.equals(AndroidTabletPagesCollection.contactListPage
//				.getSelfName()));
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
//		try {
//			value = usrMgr.findUserByNameOrNameAlias(value).getName();
//		} catch (NoSuchUserException e) {
//			// Ignore silently
//		}
//		AndroidTabletPagesCollection.currentPage = AndroidTabletPagesCollection.contactListPage
//				.tapOnName(value);
//		if (AndroidTabletPagesCollection.currentPage instanceof PersonalInfoPage) {
//			AndroidTabletPagesCollection.personalInfoPage = (PersonalInfoPage) AndroidTabletPagesCollection.currentPage;
//			AndroidTabletPagesCollection.personalInfoPage = (TabletPersonalInfoPage) AndroidTabletPagesCollection.personalInfoPage;
//		} else if (AndroidTabletPagesCollection.currentPage instanceof DialogPage) {
//			AndroidTabletPagesCollection.dialogPage = (DialogPage) AndroidTabletPagesCollection.currentPage;
//			AndroidTabletPagesCollection.dialogPage = AndroidTabletPagesCollection.contactListPage
//					.initDialogPage();
//		}
	}

	@When("^I swipe down on tablet contact list$")
	public void ISwipeDownContactList() throws Exception {
//		AndroidTabletPagesCollection.peoplePickerPage = (PeoplePickerPage) AndroidTabletPagesCollection.contactListPage
//				.swipeDown(1000);
//		AndroidTabletPagesCollection.peoplePickerPage = AndroidTabletPagesCollection.contactListPage
//				.initPeoplePickerPage();
	}
}
