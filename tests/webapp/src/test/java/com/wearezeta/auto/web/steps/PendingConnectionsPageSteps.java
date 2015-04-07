package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.When;

public class PendingConnectionsPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Accepts connection request received from specified user
	 * 
	 * @step. ^I accept connection request from user (.*)$
	 * 
	 * @param user
	 *            name of user which sent connection request
	 * @throws Exception 
	 */
	@When("^I accept connection request from user (.*)$")
	public void IAcceptConnectionRequestFromUser(String user) throws Exception {
		user = usrMgr.replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
		PagesCollection.pendingConnectionsPage.acceptRequestFromUser(user);
	}
	
	/**
	 * Ignore connection request received from specified user
	 * 
	 * @step. ^I ignore connection request from user (.*)$
	 * 
	 * @param user
	 *            name of user which sent connection request
	 * @throws Exception 
	 */
	@When("^I ignore connection request from user (.*)$")
	public void IIgnoreConnectionRequestFromUser(String user) throws Exception {
		user = usrMgr.replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
		PagesCollection.pendingConnectionsPage.ignoreRequestFromUser(user);
	}
}
