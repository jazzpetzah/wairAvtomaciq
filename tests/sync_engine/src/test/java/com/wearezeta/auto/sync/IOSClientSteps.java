package com.wearezeta.auto.sync;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.log.ZetaLogger;

import cucumber.api.java.en.Given;

public class IOSClientSteps {
	private static final Logger log = ZetaLogger.getLog(IOSClientSteps.class.getSimpleName());
	
	@Given("^IOS I Sign in using login (.*) and password (.*)$")
	public void IOSISignInUsingLoginAndPassword(String name, String password)
			throws Exception {
		if (ExecutionContext.isIosEnabled()) {
			name = SyncEngineUtil.retrieveRealUserContactPasswordValue(name);
			String login = null;
			for (ClientUser myUser : SyncEngineUtil.usersList) {
				if (myUser.getName().equals(name)) {
					login = myUser.getEmail();
					log.debug(login);
				}
			}
			password = SyncEngineUtil
					.retrieveRealUserContactPasswordValue(password);
			com.wearezeta.auto.ios.LoginPageSteps iosLoginPageSteps = new com.wearezeta.auto.ios.LoginPageSteps();
			iosLoginPageSteps.GivenISignIn(login, password);
			com.wearezeta.auto.ios.ContactListPageSteps iosContactListPageSteps = new com.wearezeta.auto.ios.ContactListPageSteps();
			iosContactListPageSteps.GivenISeeContactListWithMyName(name);
		}
	}

	@Given("IOS I open conversation with (.*)$")
	public void IOSIOpenConversationWith(String contact) throws Throwable {
		if (ExecutionContext.isIosEnabled()) {
			contact = SyncEngineUtil
					.retrieveRealUserContactPasswordValue(contact);
			com.wearezeta.auto.ios.ContactListPageSteps iosContactListPageSteps = new com.wearezeta.auto.ios.ContactListPageSteps();
			com.wearezeta.auto.ios.DialogPageSteps iosDialogPageSteps = new com.wearezeta.auto.ios.DialogPageSteps();
			iosContactListPageSteps.WhenITapOnContactName(contact);
			iosDialogPageSteps.WhenISeeDialogPage();
		}
	}
}
