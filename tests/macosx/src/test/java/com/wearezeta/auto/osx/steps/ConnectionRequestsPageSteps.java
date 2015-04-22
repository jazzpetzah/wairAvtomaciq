package com.wearezeta.auto.osx.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.osx.pages.PagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConnectionRequestsPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Accepts connection request received from user
	 * 
	 * @step. ^I accept connection request from user (.*)$
	 * 
	 * @param username
	 *            user which sent connection request
	 */
	@When("^I accept connection request from user (.*)$")
	public void IAcceptConnectionRequestFromUser(String username) {
		String email = username;
		try {
			email = usrMgr.findUserByNameOrNameAlias(username).getEmail();
		} catch (NoSuchUserException e) {
			// do nothing
		}
		PagesCollection.connectionRequestsPage
				.acceptRequestFromUserWithEmail(email);
	}

	/**
	 * Ignores connection request sent from user
	 * 
	 * @step. ^I ignore connection request from user (.*)$
	 * 
	 * @param username
	 *            user which sent connection request
	 */
	@When("^I ignore connection request from user (.*)$")
	public void IIgnoreConnectionRequestFromUser(String username) {
		String email = username;
		try {
			email = usrMgr.findUserByNameOrNameAlias(username).getEmail();
		} catch (NoSuchUserException e) {
			// do nothing
		}
		PagesCollection.connectionRequestsPage
				.ignoreRequestFromUserWithEmail(email);

	}

	/**
	 * Accepts all displayed connection request
	 * 
	 * @step. ^I accept all connection requests$
	 */
	@When("^I accept all connection requests$")
	public void IAcceptAllConnectionRequests() {
		PagesCollection.connectionRequestsPage.acceptAllRequests();
	}

	/**
	 * Ignores all displayed connection requests
	 * 
	 * @step. ^I ignore all connection requests$
	 */
	@When("^I ignore all connection requests$")
	public void IIgnoreAllConnectionRequests() {
		PagesCollection.connectionRequestsPage.ignoreAllRequests();
	}

	/**
	 * Checks that connection request from user exists
	 * 
	 * @step. ^I see connection request from user (.*)$
	 * 
	 * @param username
	 *            user which sent connection request
	 * 
	 * @throws AssertionError
	 *             if there is no connection request from specified user
	 */
	@Then("^I see connection request from user (.*)$")
	public void ISeeConnectionRequestFromUser(String username) throws Exception {
		String email = username;
		try {
			email = usrMgr.findUserByNameOrNameAlias(username).getEmail();
		} catch (NoSuchUserException e) {
		}
		Assert.assertTrue(String.format(
				"Connection request from user with email %s "
						+ "was not found.", email),
				PagesCollection.connectionRequestsPage
						.isRequestFromUserWithEmailExists(email));
	}

	/**
	 * Checks that connection request from user does not exist
	 * 
	 * @step. ^I do not see connection request from user (.*)$
	 * 
	 * @param user
	 *            user which sent connection request
	 * 
	 * @throws AssertionError
	 *             if connection request from specified user exists
	 */
	@Then("^I do not see connection request from user (.*)$")
	public void IDoNotSeeConnectionRequestFromUser(String user)
			throws Exception {
		String email = user;
		try {
			email = usrMgr.findUserByNameOrNameAlias(user).getEmail();
		} catch (NoSuchUserException e) {
		}
		Assert.assertTrue(String.format(
				"Connection request from user with email %s "
						+ "was found but expected not to be.", email),
				PagesCollection.connectionRequestsPage
						.isRequestFromUserWithEmailNotExists(email));
	}
}
