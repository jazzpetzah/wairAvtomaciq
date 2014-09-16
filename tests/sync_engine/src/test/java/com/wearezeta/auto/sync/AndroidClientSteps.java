package com.wearezeta.auto.sync;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.SyncEngineUtil;

import cucumber.api.java.en.Given;

public class AndroidClientSteps {
	private static final Logger log = ZetaLogger.getLog(AndroidClientSteps.class.getSimpleName());
	
	@Given("^Android I Sign in using login (.*) and password (.*)$")
	public void AndroidISignInUsingLoginAndPassword(String name, String password)
			throws Throwable {
		log.debug("Starting Android sign in");
		if (ExecutionContext.isAndroidEnabled()) {
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
			com.wearezeta.auto.android.LoginPageSteps androidLoginPageSteps = new com.wearezeta.auto.android.LoginPageSteps();
			androidLoginPageSteps.GivenISignIn(login, password);
			com.wearezeta.auto.android.ContactListPageSteps androidContactListPageSteps = new com.wearezeta.auto.android.ContactListPageSteps();
			androidContactListPageSteps.GivenISeeContactListWithMyName(name);
		}
	}

	@Given("Android I open conversation with (.*)$")
	public void AndroidIOpenConversationWith(String contact) throws Throwable {
		log.debug("Trying to open conversation on Android");
		if (ExecutionContext.isAndroidEnabled()) {
			contact = SyncEngineUtil
					.retrieveRealUserContactPasswordValue(contact);
			com.wearezeta.auto.android.ContactListPageSteps androidContactListPageSteps = new com.wearezeta.auto.android.ContactListPageSteps();
			com.wearezeta.auto.android.DialogPageSteps androidDialogPageSteps = new com.wearezeta.auto.android.DialogPageSteps();
			androidContactListPageSteps.WhenITapOnContactName(contact);
			androidDialogPageSteps.WhenISeeDialogPage();
		}
	}
}
