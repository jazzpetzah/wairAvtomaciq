package com.wearezeta.auto.sync;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.sync.ExecutionContext;

import cucumber.api.java.en.Given;

public class IOSClientSteps {
	private static final Logger log = ZetaLogger.getLog(IOSClientSteps.class.getSimpleName());
	
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	
	@Given("^IOS I Sign in using login (.*) and password (.*)$")
	public void IOSISignInUsingLoginAndPassword(String name, String password)
			throws Throwable {
		if (ExecutionContext.isIosEnabled()) {
			String login = usrMgr.findUserByNameOrNameAlias(name).getEmail();
			log.debug("iOS: Found user with login " + login + " by alias " + name);
			password = usrMgr.findUserByNameOrNameAlias(name).getPassword();
			
			com.wearezeta.auto.ios.LoginPageSteps iosLoginPageSteps = new com.wearezeta.auto.ios.LoginPageSteps();
			iosLoginPageSteps.GivenISignIn(login, password);
			com.wearezeta.auto.ios.ContactListPageSteps iosContactListPageSteps = new com.wearezeta.auto.ios.ContactListPageSteps();
			iosContactListPageSteps.GivenISeeContactListWithMyName(name);
		}
	}

	@Given("IOS I open conversation with (.*)$")
	public void IOSIOpenConversationWith(String contact) throws Throwable {
		if (ExecutionContext.isIosEnabled()) {
			contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
			com.wearezeta.auto.ios.ContactListPageSteps iosContactListPageSteps = new com.wearezeta.auto.ios.ContactListPageSteps();
			com.wearezeta.auto.ios.DialogPageSteps iosDialogPageSteps = new com.wearezeta.auto.ios.DialogPageSteps();
			iosContactListPageSteps.WhenITapOnContactName(contact);
			iosDialogPageSteps.WhenISeeDialogPage();
		}
	}
}
