package com.wearezeta.auto.sync;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.SyncEngineUtil;

import cucumber.api.java.en.Given;

public class OSXClientSteps {
	private static final Logger log = ZetaLogger.getLog(OSXClientSteps.class.getSimpleName());
	
	@Given("^OSX I Sign in using login (.*) and password (.*)$")
	public void OSXISignInUsingLoginAndPassword(String login, String password)
			throws Exception {
		if (ExecutionContext.isOsxEnabled()) {
			login = SyncEngineUtil.retrieveRealUserContactPasswordValue(login);
			String name = login;
			for (ClientUser myUser : SyncEngineUtil.usersList) {
				if (myUser.getName().equals(login)) {
					login = myUser.getEmail();
				}
			}
			log.debug("OSX: sign in using name " + name);
			password = SyncEngineUtil
					.retrieveRealUserContactPasswordValue(password);
			com.wearezeta.auto.osx.steps.LoginPageSteps osxLoginPageSteps = new com.wearezeta.auto.osx.steps.LoginPageSteps();
			osxLoginPageSteps
					.GivenISignInUsingLoginAndPassword(login, password);
			com.wearezeta.auto.osx.steps.ContactListPageSteps osxClPageSteps = new com.wearezeta.auto.osx.steps.ContactListPageSteps();
			osxClPageSteps.ISeeMyNameInContactList(name);
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
