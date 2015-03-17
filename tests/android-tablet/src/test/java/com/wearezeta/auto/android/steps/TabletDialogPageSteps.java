package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.android.pages.TabletPagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.*;

public class TabletDialogPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Checks visibility of dialog page
	 * 
	 * @step. ^I see tablet dialog page$
	 * 
	 * @throws Exception
	 */
	@When("^I see tablet dialog page$")
	public void WhenISeeTabletDialogPage() throws Exception {
		if (TabletPagesCollection.dialogPage == null) {
			TabletPagesCollection.dialogPage = TabletPagesCollection.contactListPage
					.initDialogPage();
			PagesCollection.dialogPage = TabletPagesCollection.dialogPage;
		}
		Assert.assertTrue(TabletPagesCollection.dialogPage
				.isProfileButtonDisplayed());
	}

	/**
	 * Click on participant profile button
	 * 
	 * @step. ^I tap on profile button$
	 * 
	 * @throws Exception
	 */
	@When("^I tap on profile button$")
	public void WhenITapOnProfileButton() throws Exception {
		TabletPagesCollection.dialogPage.tapOnProfileButton();
		TabletPagesCollection.otherUserPersonalInfoPage = TabletPagesCollection.dialogPage
				.initOtherUserPersonalInfoPage();
		PagesCollection.otherUserPersonalInfoPage = TabletPagesCollection.otherUserPersonalInfoPage;
	}

	/**
	 * Check that participant pop-over visible
	 * 
	 * @step. ^I see participant pop-over$
	 * 
	 * @throws Exception
	 */
	@Then("^I see participant pop-over$")
	public void ThenISeeUserPopOver() throws Exception {
		Assert.assertTrue(TabletPagesCollection.dialogPage.isPopOverDisplayed());
	}

	/**
	 * Check name and email in participant pop-over
	 * 
	 * @step. ^I see (.*) name and email in pop-over$
	 * 
	 * @param contact
	 *            user name string
	 * 
	 * @throws Exception
	 */
	@Then("^I see (.*) name and email in pop-over$")
	public void ThenISeeUserNameAndMailInPopOver(String contact)
			throws Exception {
		ClientUser dstUser = usrMgr.findUserByNameOrNameAlias(contact);
		contact = dstUser.getName();
		String email = dstUser.getEmail();
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage
				.isOtherUserNameVisible(contact));
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage
				.isOtherUserMailVisible(email));
	}

	/**
	 * Check that participant pop-over will be closed after tapping on close
	 * button
	 * 
	 * @step. ^I can close pop-over by close button$
	 * 
	 * @throws Exception
	 */
	@Then("^I can close pop-over by close button$")
	public void ThenICanClosePopOverByCloseButton() throws Exception {
		TabletPagesCollection.dialogPage.tapOnParticipantsClose();
		Assert.assertTrue(!TabletPagesCollection.dialogPage
				.isPopOverDisplayed());
	}

	/**
	 * Check that participant pop-over will be closed after tapping outside
	 * pop-over
	 * 
	 * @step. ^I can close pop-over by tapping outside$
	 * 
	 * @throws Exception
	 */
	@Then("^I can close pop-over by tapping outside$")
	public void ThenICanClosePopOverByTappingOutside() throws Exception {
		TabletPagesCollection.dialogPage.tapOnProfileButton();
		Assert.assertTrue(!TabletPagesCollection.dialogPage
				.isPopOverDisplayed());
	}
}