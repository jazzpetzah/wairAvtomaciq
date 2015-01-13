package com.wearezeta.auto.sync;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.sync.ExecutionContext;

import cucumber.api.java.en.Given;

public class OSXClientSteps {
	private static final Logger log = ZetaLogger.getLog(OSXClientSteps.class.getSimpleName());
	
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	
	@Given("^OSX I Sign in using login (.*) and password (.*)$")
	public void OSXISignInUsingLoginAndPassword(String name, String password)
			throws Exception {
		if (ExecutionContext.isOsxEnabled()) {
			String login = usrMgr.findUserByNameOrNameAlias(name).getEmail();
			log.debug("iOS: Found user with login " + login + " by alias " + name);
			password = usrMgr.findUserByNameOrNameAlias(name).getPassword();
			
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
			contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
			com.wearezeta.auto.osx.steps.ContactListPageSteps osxContactListPageSteps = new com.wearezeta.auto.osx.steps.ContactListPageSteps();
			osxContactListPageSteps.GivenIOpenConversationWith(contact);
		}
	}
}
