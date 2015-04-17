package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.android.pages.OtherUserPersonalInfoPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.android.pages.TabletPagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.When;

public class TabletPeoplePickerPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	
	/**
	 * Tap on tablet create conversation
	 * 
	 * @step. ^I tap on tablet create conversation$
	 * 
	 * @throws Throwable
	 */
	@When("^I tap on tablet create conversation$")
	public void WhenITapOnTabletCreateConversation() throws Throwable {
		PagesCollection.dialogPage = (DialogPage) PagesCollection.peoplePickerPage
				.tapCreateConversation();
		TabletPagesCollection.dialogPage = TabletPagesCollection.contactListPage.initDialogPage();
	}
	
	/**
	 * Tap on user name found on tablet People picker page
	 * 
	 * @step. ^I tap on user name found on tablet People picker page (.*)$
	 * 
	 * @param contact
	 * 			 contact name
	 * @throws Exception
	 */
	@When("^I tap on user name found on tablet People picker page (.*)$")
	public void WhenITapOnUserNameFoundOnTabletPeoplePickerPage(String contact)
			throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		// PagesCollection.peoplePickerPage.waitUserPickerFindUser(contact);
		PagesCollection.androidPage = PagesCollection.peoplePickerPage
				.selectContact(contact);
		TabletPagesCollection.connectToPage = TabletPagesCollection.peoplePickerPage.initConnectToPage();
		if (PagesCollection.androidPage instanceof OtherUserPersonalInfoPage) {
			PagesCollection.otherUserPersonalInfoPage = (OtherUserPersonalInfoPage) PagesCollection.androidPage;
		}
	}
	
}
