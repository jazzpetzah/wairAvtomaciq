package com.wearezeta.auto.android;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.email.IMAPSMailbox;
import com.wearezeta.auto.common.email.MBoxChangesListener;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SettingsPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	private MBoxChangesListener listener;
	private ClientUser userToRegister = null;
	
	@When("^I request reset password for (.*)$")
	public void WhenIRequestResetPassword(String email) throws Exception{
		try {
			email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.settingsPage.requestResetPassword(email);	
	}
	
	@Then("^I reset (.*) password by URL to new (.*)$")
	public void WhenIResetPasswordByUrl(String name, String newPass) throws Exception{
		try {
			this.userToRegister = usrMgr.findUserByNameOrNameAlias(name);
		} catch (NoSuchUserException e) {
			if (this.userToRegister == null) {
				this.userToRegister = new ClientUser();
			}
			this.userToRegister.setName(name);
			this.userToRegister.addNameAlias(name);
		}
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", this.userToRegister.getEmail());
		this.listener = IMAPSMailbox.createDefaultInstance().startMboxListener(
				expectedHeaders);
		
		String link = BackendAPIWrappers.getPasswordResetLink(this.listener);
		PagesCollection.peoplePickerPage = PagesCollection.settingsPage.resetByLink(link,newPass);
	}
	
	@Then("^I see settings page$")
	public void ISeeSettingsPage() throws Throwable {
	    Assert.assertTrue(PagesCollection.settingsPage.isSettingsPageVisible());
	}

}
