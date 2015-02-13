package com.wearezeta.auto.web.steps;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class ContactListPageSteps {

	private static final Logger log = ZetaLogger
			.getLog(ContactListPageSteps.class.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Checks that we can see signed in user in Contact List
	 * 
	 * @step. ^I see my name (.*) in Contact list$
	 * 
	 * @param name
	 *            user name string
	 * 
	 * @throws AssertionError
	 *             if user name does not appear in Contact List
	 */
	@Given("^I see my name (.*) in Contact list$")
	public void ISeeMyNameInContactList(String name) throws Exception {
		GivenISeeContactListWithName(name);
	}

	/**
	 * Checks that we can see conversation with specified name in Contact List
	 * 
	 * @step. I see Contact list with name (.*)
	 * 
	 * @param name
	 *            conversation name string
	 * 
	 * @throws AssertionError
	 *             if conversation name does not appear in Contact List
	 */
	@Given("I see Contact list with name (.*)")
	public void GivenISeeContactListWithName(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		log.debug("Looking for contact with name " + name);
		Assert.assertTrue("No contact list loaded.",
				PagesCollection.contactListPage.waitForContactListVisible());
		if (usrMgr.isSelfUserSet()
				&& usrMgr.getSelfUser().getName().equals(name)) {
			Assert.assertTrue(PagesCollection.contactListPage.getSelfName()
					.equals(name));
		} else {
			boolean result = false;
			for (int i = 0; i < 5; i++) {
				result = PagesCollection.contactListPage
						.isContactWithNameExists(name);
				if (result)
					break;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
			Assert.assertTrue(result);
		}
	}

	/**
	 * Opens conversation by choosing it from Contact List
	 * 
	 * @step. I open conversation with (.*)
	 * 
	 * @param contact
	 *            conversation name string
	 * 
	 * @throws Exception
	 */
	@Given("I open conversation with (.*)")
	public void GivenIOpenConversationWith(String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		PagesCollection.conversationPage = PagesCollection.contactListPage
				.openConversation(contact);
	}

	/**
	 * Clicks the self name item in the convo list to open self profile page
	 * 
	 * @step. ^I open self profile$
	 * 
	 * @throws Exception
	 */
	@When("^I open self profile$")
	public void IOpenSelfProfile() throws Exception {
		PagesCollection.selfProfilePage = PagesCollection.contactListPage
				.openSelfProfile();
	}

	/**
	 * Archive conversation by choosing it from Contact List
	 * 
	 * @step. ^I archive conversation (.*)$
	 * 
	 * @param contact
	 *            conversation name string
	 * 
	 */
	@When("^I archive conversation (.*)$")
	public void IClickArchiveButton(String contact) {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		PagesCollection.contactListPage.clickActionsButtonForContact(contact);
		PagesCollection.contactListPage
				.clickArchiveConversationForContact(contact);
	}

	/**
	 * Open archived conversations
	 * 
	 * @step. ^I open archive$
	 * 
	 */
	@When("^I open archive$")
	public void IOpenArchive() {
		PagesCollection.contactListPage.openArchive();
	}

	/**
	 * Checks that we cannot see conversation with specified name in Contact
	 * List
	 * 
	 * @step. ^I do not see Contact list with name (.*)$
	 * 
	 * @param name
	 *            conversation name string
	 * 
	 * @throws AssertionError
	 *             if conversation name appear in Contact List
	 */
	@Given("^I do not see Contact list with name (.*)$")
	public void IDoNotSeeContactListWithName(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		Assert.assertFalse(PagesCollection.contactListPage
				.isContactWithNameExists(name));
	}

	/**
	 * Checks that connection request is displayed in Conversation List
	 * 
	 * @step. ^I see connection request$
	 * 
	 * @throws Exception
	 */
	@When("^I see connection request$")
	public void ISeeConnectInvitation() throws Exception {
		GivenISeeContactListWithName(WebAppLocators.Common.CONTACT_LIST_ONE_PERSON_WAITING);
	}

	/**
	 * Opens list of connection requests from Contact list
	 * 
	 * @step. ^I open connection requests list$
	 * 
	 * @throws Exception
	 */
	@Given("^I open connection requests list$")
	public void IOpenConnectionRequestsList() throws Exception {
		PagesCollection.pendingConnectionsPage = PagesCollection.contactListPage
				.openConnectionRequestsList(WebAppLocators.Common.CONTACT_LIST_ONE_PERSON_WAITING);

	}

	/**
	 * Opens People Picker in Contact List
	 * 
	 * @step. ^I open People Picker from Contact List$
	 * 
	 * @throws Exception
	 */
	@When("^I open People Picker from Contact List$")
	public void IOpenPeoplePicker() throws Exception {
		PagesCollection.peoplePickerPage = PagesCollection.contactListPage
				.openPeoplePicker();
	}
}
