package com.wearezeta.auto.sync;

import org.apache.log4j.Logger;

import com.wearezeta.auto.android.ContactListPageSteps;
import com.wearezeta.auto.android.DialogPageSteps;
import com.wearezeta.auto.android.LoginPageSteps;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.sync.ExecutionContext;

import cucumber.api.java.en.Given;

public class AndroidClientSteps {
	
	private static final Logger log = ZetaLogger
			.getLog(AndroidClientSteps.class.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@Given("^Android I Sign in using login (.*) and password (.*)$")
	public void AndroidISignInUsingLoginAndPassword(String name, String password)
			throws Throwable {
		if (ExecutionContext.isAndroidEnabled()) {
			String login = usrMgr.findUserByNameOrNameAlias(name).getEmail();
			log.debug("Android: Found user with login " + login + " by alias "
					+ name);
			password = usrMgr.findUserByNameOrNameAlias(name).getPassword();

			if (PagesCollection.loginPage.isDismissUpdateVisible()) {
				PagesCollection.loginPage.dismissUpdate();
			}
			LoginPageSteps androidLoginPageSteps = new LoginPageSteps();
			androidLoginPageSteps.GivenISignIn(login, password);
			ContactListPageSteps androidContactListPageSteps = new ContactListPageSteps();
			androidContactListPageSteps.GivenISeeContactListWithMyName(name);
		}
	}

	@Given("Android I open conversation with (.*)$")
	public void AndroidIOpenConversationWith(String contact) throws Throwable {
		log.debug("Trying to open conversation on Android");
		if (ExecutionContext.isAndroidEnabled()) {
			contact = usrMgr.replaceAliasesOccurences(contact,
					FindBy.NAME_ALIAS);
			ContactListPageSteps androidContactListPageSteps = new ContactListPageSteps();
			DialogPageSteps androidDialogPageSteps = new DialogPageSteps();
			androidContactListPageSteps.WhenITapOnContactName(contact);
			androidDialogPageSteps.WhenISeeDialogPage();
		}
	}
}
