package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.TabletPagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.Then;

public class TabletPersonalInfoPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Check that personal info page opened and name is correct
	 * 
	 * @step. ^I see personal info page loaded with my name (.*)$
	 * 
	 * @throws NoSuchUserException
	 */
	@Then("^I see personal info page loaded with my name (.*)$")
	public void ISeeMyProfileName(String name) throws NoSuchUserException {
		String currentName = null;
		try{
			currentName = usrMgr.findUserByNameOrNameAlias(name).getName();
		} catch (NoSuchUserException e) {
			currentName = name;
		}
		Assert.assertTrue(currentName.equals(TabletPagesCollection.personalInfoPage.getUserName()));
	}
}
