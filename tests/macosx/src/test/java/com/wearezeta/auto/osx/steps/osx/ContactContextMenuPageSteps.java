package com.wearezeta.auto.osx.steps.osx;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.osx.pages.osx.ContactContextMenuPage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.web.pages.ContactListPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class ContactContextMenuPageSteps {

	private final OSXPagesCollection osxPagesCollection = OSXPagesCollection
			.getInstance();
	@SuppressWarnings("unused")
	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	@SuppressWarnings("unused")
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();


	/**
	 * Opens the right-click menu for a specific conversation
	 *
	 * @param name
	 *            the conversation name to open the context menu for
	 *
	 * @step. ^I open context menu of conversation (.*)$
	 *
	 * @throws java.lang.Exception
	 * @throws AssertionError
	 *             if contact list is not loaded or avatar does not appear at
	 *             the top of Contact List
	 */
	@Given("^I open context menu of conversation (.*)$")
	public void IOpenContextMenuOfContact(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		ContactListPage contactListPage = webappPagesCollection
				.getPage(ContactListPage.class);
		Assert.assertTrue("No contact list loaded.",
				contactListPage.waitForContactListVisible());
		webappPagesCollection.getInstance().getPage(ContactListPage.class)
				.openContextMenuForContact(name);
	}

	/**
	 * Click the block in context menu
	 *
	 * @step. ^I click block in context menu$
	 * @throws Exception
	 */
	@When("^I click block in context menu$")
	public void IClickBlockButtonInContextMenu() throws Exception {
		osxPagesCollection.getPage(ContactContextMenuPage.class)
				.clickBlock();
	}

	/**
	 * Click the leave in context menu
	 *
	 * @step. ^I click leave in context menu$
	 * @throws Exception
	 */
	@When("^I click leave in context menu$")
	public void IClickLeaveButtonInContextMenu() throws Exception {
		osxPagesCollection.getPage(ContactContextMenuPage.class)
				.clickLeave();
	}

	/**
	 * Click the silence in context menu
	 *
	 * @step. ^I click silence in context menu$
	 * @throws Exception
	 */
	@When("^I click silence in context menu$")
	public void IClickSilenceButtonInContextMenu() throws Exception {
		osxPagesCollection.getPage(ContactContextMenuPage.class)
				.clickSilence();
	}

	/**
	 * Click the notify in context menu
	 *
	 * @step. ^I click notify in context menu$
	 * @throws Exception
	 */
	@When("^I click notify in context menu$")
	public void IClickNotifyButtonInContextMenu() throws Exception {
		osxPagesCollection.getPage(ContactContextMenuPage.class)
				.clickNotify();
	}

	/**
	 * Click the delete in context menu
	 *
	 * @step. ^I click delete in context menu$
	 * @throws Exception
	 */
	@When("^I click delete in context menu$")
	public void IClickDeleteButtonInContextMenu() throws Exception {
		osxPagesCollection.getPage(ContactContextMenuPage.class)
				.clickDelete();
	}

	/**
	 * Click the archive in context menu
	 *
	 * @step. ^I click archive in context menu$
	 * @throws Exception
	 */
	@When("^I click archive in context menu$")
	public void IClickArchiveButtonInContextMenu() throws Exception {
		osxPagesCollection.getPage(ContactContextMenuPage.class)
				.clickArchive();
	}
}
