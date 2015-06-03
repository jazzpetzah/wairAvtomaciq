package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletPeoplePickerPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

public class TabletPeoplePickerPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletPeoplePickerPage getPeoplePickerPage() throws Exception {
		return (TabletPeoplePickerPage) pagesCollection
				.getPage(TabletPeoplePickerPage.class);
	}

	/**
	 * Verify that People Picker is visible
	 * 
	 * @step. ^I see People Picker page$
	 * 
	 * @throws Exception
	 */
	@When("^I see People Picker page$")
	public void WhenITapOnTabletCreateConversation() throws Exception {
		Assert.assertTrue("People Picker page is not visible",
				getPeoplePickerPage().waitUntilVisible());
	}

	/**
	 * Enter user name or email into the corresponding People Picker field
	 * 
	 * @step. ^I enter \"(.*)\" into Search input on People [Pp]icker page$
	 * 
	 * @param searchCriteria
	 *            user name/email/phone number or the corresponding aliases
	 * @throws Exception
	 */
	@When("^I enter \"(.*)\" into Search input on People [Pp]icker page")
	public void IEnterStringIntoSearchField(String searchCriteria)
			throws Exception {
		searchCriteria = usrMgr.replaceAliasesOccurences(searchCriteria,
				FindBy.EMAIL_ALIAS);
		searchCriteria = usrMgr.replaceAliasesOccurences(searchCriteria,
				FindBy.NAME_ALIAS);
		searchCriteria = usrMgr.replaceAliasesOccurences(searchCriteria,
				FindBy.PHONENUMBER_ALIAS);
		getPeoplePickerPage().typeTextInPeopleSearch(searchCriteria);
	}

	/**
	 * Tap one of found items in People Picker results
	 * 
	 * @step. ^I tap the found item (.*) on [Pp]eople [Pp]icker page$
	 * 
	 * @param item
	 *            user name/email/phone number or the corresponding aliases
	 * @throws Exception
	 */
	@When("^I tap the found item (.*) on [Pp]eople [Pp]icker page$")
	public void ITapUserName(String item) throws Exception {
		item = usrMgr.replaceAliasesOccurences(item, FindBy.EMAIL_ALIAS);
		item = usrMgr.replaceAliasesOccurences(item, FindBy.NAME_ALIAS);
		item = usrMgr.replaceAliasesOccurences(item, FindBy.PHONENUMBER_ALIAS);
		getPeoplePickerPage().tapFoundItem(item);
	}

	/**
	 * Click the X button to close People Picker
	 * 
	 * @step. ^I close People Picker$
	 * 
	 * @throws Exception
	 */
	@And("^I close People Picker$")
	public void IClosePeoplePicker() throws Exception {
		getPeoplePickerPage().tapCloseButton();
	}

}
