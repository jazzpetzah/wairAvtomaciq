package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.ContactsUiPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContactsUiPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private ContactsUiPage getContactsUiPage() throws Exception {
        return pagesCollection.getPage(ContactsUiPage.class);
    }

    /**
     * Verify that ContactsUI page is shown (by verifying search input)
     *
     * @throws Exception
     * @step. ^I see ContactsUI page$
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
     * @param contact username
     * @throws Exception
     * @step. ^I input user name (.*) in search on ContactsUI$
     */
    @When("^I input user name (.*) in search on ContactsUI$")
    public void IInputUserNameInSearchOnContactsUI(String contact) throws Exception {
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
     * @param shouldNotBeVisible equals to null if the contact should be visible
     * @param contact            user name
     * @throws Exception
     * @step. I (do not )?see contact (.*) in ContactsUI page list$
     */
    @Then("^I (do not )?see contact (.*) in ContactsUI page list$")
    public void ISeeContactInContactsUIList(String shouldNotBeVisible, String contact) throws Exception {
        try {
            contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        } catch (NoSuchUserException e) {
            // Ignore silently
        }
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("User " + contact
                            + " is not presented in ContactsUI user list",
                    getContactsUiPage().isContactPresentedInContactsList(contact));
        } else {
            Assert.assertFalse("User " + contact
                    + " is presented in ContactsUI user list", getContactsUiPage()
                    .isContactPresentedInContactsList(contact));
        }
    }

    /**
     * Presses the Invite others button in the people picker. To invite people
     * via mail.
     *
     * @throws Exception
     * @step. ^I tap Invite Others button on Contacts UI page$
     */
    @When("^I tap Invite Others button on Contacts UI page$")
    public void ITapInviteOthersButton() throws Exception {
        getContactsUiPage().tapInviteOthersButton();
    }

    /**
     * Click on Open button on ContactsUI next to user name
     *
     * @param contact user name
     * @throws Exception
     * @step. ^I tap Open button next to user name (.*) on ContactsUI$
     */
    @When("^I tap Open button next to user name (.*) on ContactsUI$")
    public void IClickOpenButtonNextToUser(String contact) throws Exception {
        contact = usrMgr.replaceAliasesOccurences(contact, ClientUsersManager.FindBy.NAME_ALIAS);
        getContactsUiPage().tapOpenButtonNextToUser(contact);
    }
}
