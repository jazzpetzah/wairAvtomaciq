package com.wearezeta.auto.sync;

import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.SyncEngineUtil;

import cucumber.api.java.en.Given;

public class OSXClientSteps {
	@Given("^OSX I Sign in using login (.*) and password (.*)$")
	public void OSXISignInUsingLoginAndPassword(String login, String password)
			throws Exception {
		String name = login;
		if (ExecutionContext.isOsxEnabled()) {
			login = SyncEngineUtil.retrieveRealUserContactPasswordValue(login);
			for (ClientUser myUser : SyncEngineUtil.usersList) {
				if (myUser.getName().equals(login)) {
					login = myUser.getEmail();
				}
			}
			password = SyncEngineUtil
					.retrieveRealUserContactPasswordValue(password);
			com.wearezeta.auto.osx.steps.LoginPageSteps osxLoginPageSteps = new com.wearezeta.auto.osx.steps.LoginPageSteps();
			osxLoginPageSteps
					.GivenISignInUsingLoginAndPassword(login, password);
			com.wearezeta.auto.osx.steps.ContactListPageSteps osxClPageSteps = new com.wearezeta.auto.osx.steps.ContactListPageSteps();
			osxClPageSteps.GivenISeeContactListWithName(name);
		}
	}

	@Given("OSX I open conversation with (.*)$")
	public void OSXIOpenConversationWith(String contact) throws Throwable {
		if (ExecutionContext.isOsxEnabled()) {
			contact = SyncEngineUtil
					.retrieveRealUserContactPasswordValue(contact);
			com.wearezeta.auto.osx.steps.ContactListPageSteps osxContactListPageSteps = new com.wearezeta.auto.osx.steps.ContactListPageSteps();
			osxContactListPageSteps.GivenIOpenConversationWith(contact);
		}
	}
}
