package com.wearezeta.auto.ios.steps;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.ContactsUiPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContactsUiPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	private static final Logger log = ZetaLogger
			.getLog(ContactsUiPageSteps.class.getSimpleName());

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private ContactsUiPage getContactsUiPage() throws Exception {
		return (ContactsUiPage) pagesCollecton.getPage(ContactsUiPage.class);
	}

	/**
	 * Verify that ContactsUI page is shown (by verifying search input)
	 * 
	 * @step. ^I see ContactsUI page$
	 * 
	 * @throws Exception
	 */
	@When("^I see ContactsUI page$")
	public void ISeeContactsUIPage() throws Exception {
		Assert.assertTrue("Search on ContactsUI page is not shown",
				getContactsUiPage().isSearchInputVisible());
		Assert.assertTrue("Invite on ContactsUI page is not shown",
				getContactsUiPage().isInviteOthersButtonVisible());
	}

	/**
	 * Input user name in search field
	 * 
	 * @step. ^I input user name (.*) in search on ContactsUI$
	 * 
	 * @param contact
	 *            username
	 * @throws Exception
	 */
	@When("^I input user name (.*) in search on ContactsUI$")
	public void IInputUserNameInSearchOnContactsUI(String contact)
			throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		getContactsUiPage().inputTextToSearch(contact);
	}

	/**
	 * Verify is user is presented in ContactsUI page
	 * 
	 * @step. ^I see contact (.*) in ContactsUI page list$
	 * 
	 * @param contact
	 *            user name
	 * @throws Exception
	 */
	@Then("^I see contact (.*) in ContactsUI page list$")
	public void ISeeContactInContactsUIList(String contact) throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		Assert.assertTrue("User " + contact
				+ " is not presented in ContactsUI user list",
				getContactsUiPage().isContactPresentedInContactsList(contact));
	}

	/**
	 * Verify is user is NOT presented in ContactsUI page
	 * 
	 * @step. ^I DONT see contact (.*) in ContactsUI page list$
	 * 
	 * @param contact
	 *            user name
	 * @throws Exception
	 */
	@Then("^I DONT see contact (.*) in ContactsUI page list$")
	public void IDontSeeContactInContactsUIList(String contact)
			throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		Assert.assertFalse("User " + contact
				+ " is presented in ContactsUI user list", getContactsUiPage()
				.isContactPresentedInContactsList(contact));
	}

	/**
	 * Presses the Invite others button in the people picker. To invite people
	 * via mail.
	 * 
	 * @step. ^I press invite others button$
	 * @throws Exception
	 * 
	 */
	@When("^I press invite others button$")
	public void IPressInviteOthersButton() throws Exception {
		getContactsUiPage().tapInviteOthersButton();
	}

	/**
	 * Click on Open button on ContactsUI next to user name
	 * 
	 * @step. ^I click on Open button next to user name (.*) on ContactsUI$
	 * 
	 * @param contact
	 *            user name
	 * @throws Exception
	 */
	@When("^I click on Open button next to user name (.*) on ContactsUI$")
	public void IClickOpenButtonNextToUser(String contact) throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		getContactsUiPage().clickOpenButtonNextToUser(contact);
	}
}
