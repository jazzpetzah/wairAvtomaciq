package com.wearezeta.auto.osx.steps;

import java.util.List;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.osx.pages.PagesCollection;

import cucumber.api.java.en.When;

public class DebugSteps {

	private static final Logger log = ZetaLogger.getLog(DebugSteps.class
			.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@When("^I output page source$")
	public void IOutputPageSource() {
		log.debug(PagesCollection.mainMenuPage.getPageSource());
	}

	@When("^I output all user aliases with emails$")
	public void IOutputAllUserAliasesWithEmails() {
		List<ClientUser> users = usrMgr.getCreatedUsers();
		StringBuilder sb = new StringBuilder("\n");
		for (ClientUser user : users) {
			sb.append(String.format("%s=%s\n", user.getNameAliases(),
					user.getEmail()));
		}
		log.debug(sb.toString());
	}
}
