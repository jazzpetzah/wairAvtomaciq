package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.junit.Assert;

public class PendingConnectionsPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Checks if given mail is present in connection request from user
	 *
	 *
	 * @step. ^I see mail (.*)in connection request from user (.*)$
	 *
	 * @param user
	 *            name of user which sent connection request
	 * @param mailAlias
	 *            the mail alias to test for when mail alias is shown
	 * @throws Exception
	 */
	@Then("^I see mail (.*)in connection request from user (.*)$")
	public void ICanSeeEmailFromUser(String mailAlias, String user)
			throws Exception {
		user = usrMgr.replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
		mailAlias = mailAlias.trim();
		if ("".equals(mailAlias)) {
			// no mail given. just check if any text is in mail field
			Assert.assertFalse(PagesCollection.pendingConnectionsPage
					.getEmailByName(user).isEmpty());
		} else {
			// mail given. strict check for mail
			String email = null;
			try {
				email = usrMgr.findUserByEmailOrEmailAlias(mailAlias)
						.getEmail();
			} catch (NoSuchUserException e) {
				// Ignore silently
			}
			Assert.assertTrue(PagesCollection.pendingConnectionsPage
					.getEmailByName(user).equals(email));

		}
	}

	/**
	 * Checks the connection message of a user connection request
	 *
	 * @step. ^I see connection message \"(.*)\" in connection request from user
	 *        (.*)$
	 * @param message
	 *            connection message from user
	 * @param user
	 *            name of user which sent connection request
	 * @throws Exception
	 */
	@Then("^I see connection message \"(.*)\" in connection request from user (.*)$")
	public void ISeeConnectionMessageFromUser(String message, String user)
			throws Exception {
		user = usrMgr.replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
		Assert.assertTrue(PagesCollection.pendingConnectionsPage
				.getMessageByName(user).equals(message));

	}

	/**
	 * Checks if the avatar on a connection request from a user is visible
	 *
	 * @step. ^I see avatar in connection request from user (.*)$
	 * @param user
	 *            name of user which sent connection request
	 * @throws Exception
	 */
	@Then("^I see avatar in connection request from user (.*)$")
	public void ISeeAvatarFromUser(String user) throws Exception {
		user = usrMgr.replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
		Assert.assertTrue(PagesCollection.pendingConnectionsPage
				.isAvatarByNameVisible(user));

	}

	/**
	 * Checks if the accept button on a connection request from a user is
	 * visible
	 *
	 * @step. ^I see accept button in connection request from user (.*)$
	 * @param user
	 *            name of user which sent connection request
	 * @throws Exception
	 */
	@Then("^I see accept button in connection request from user (.*)$")
	public void ISeeAcceptButtonConnectionFromUser(String user)
			throws Exception {
		user = usrMgr.replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
		Assert.assertTrue(PagesCollection.pendingConnectionsPage
				.isAcceptRequestButtonForUserVisible(user));
	}

	/**
	 * Checks if the ignore button on a connection request from a user is
	 * visible
	 *
	 * @step. ^I see ignore button in connection request from user (.*)$
	 * @param user
	 *            name of user which sent connection request
	 * @throws Exception
	 */
	@Then("^I see ignore button in connection request from user (.*)$")
	public void ISeeIgnoreButtonConnectionFromUser(String user)
			throws Exception {
		user = usrMgr.replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
		Assert.assertTrue(PagesCollection.pendingConnectionsPage
				.isIgnoreRequestButtonForUserVisible(user));
	}

	/**
	 * Checks if the accept button on a connection request from a user is in
	 * correct color
	 *
	 * @step. ^I see correct color for accept button in connection request from
	 *        user (.*)$
	 * @param user
	 *            name of user which sent connection request
	 * @throws Exception
	 */
	@Then("^I see correct color for accept button in connection request from user (.*)$")
	public void ISeeCorrectColorForAcceptButtonConnectionFromUser(String user)
			throws Exception {
		user = usrMgr.replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
		AccentColor accentColor = usrMgr.getSelfUser().getAccentColor();
		Assert.assertTrue(PagesCollection.pendingConnectionsPage
				.getAcceptRequestButtonBgColor(user).equals(accentColor));
	}

	/**
	 * Checks if the ignore button on a connection request from a user is in
	 * correct color
	 *
	 * @step. ^I see correct color for ignore button in connection request from
	 *        user (.*)$
	 * @param user
	 *            name of user which sent connection request
	 * @throws Exception
	 */
	@Then("^I see correct color for ignore button in connection request from user (.*)$")
	public void ISeeCorrectColorForIgnoreButtonConnectionFromUser(String user)
			throws Exception {
		user = usrMgr.replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
		AccentColor accentColor = usrMgr.getSelfUser().getAccentColor();
		Assert.assertTrue(PagesCollection.pendingConnectionsPage
				.getIgnoreRequestButtonBorderColor(user).equals(accentColor));
	}

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
