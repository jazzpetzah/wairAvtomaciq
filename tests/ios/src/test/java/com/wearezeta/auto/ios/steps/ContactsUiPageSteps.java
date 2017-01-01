package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.ContactsUiPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContactsUiPageSteps {
    private ContactsUiPage getContactsUiPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(ContactsUiPage.class);
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
                getContactsUiPage().isButtonVisible("Invite Others"));
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
        contact = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(contact, ClientUsersManager.FindBy.NAME_ALIAS);
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
        contact = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(contact, ClientUsersManager.FindBy.NAME_ALIAS);
        if (shouldNotBeVisible == null) {
            Assert.assertTrue(
                    String.format("User '%s' should be visible in ContactsUI user list", contact),
                    getContactsUiPage().isContactVisible(contact)
            );
        } else {
            Assert.assertTrue(
                    String.format("User '%s' should not be visible in ContactsUI user list", contact),
                    getContactsUiPage().isContactInvisible(contact)
            );
        }
    }

    /**
     * Tap the corresponding button on Contacts UI
     *
     * @param btnName one of available button namers
     * @throws Exception
     * @step. ^I tap (Invite Others|X) button on Contacts UI page$
     */
    @When("^I tap (Invite Others|X) button on Contacts UI page$")
    public void ITapInviteOthersButton(String btnName) throws Exception {
        getContactsUiPage().tapButton(btnName);
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
        contact = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .replaceAliasesOccurences(contact, ClientUsersManager.FindBy.NAME_ALIAS);
        getContactsUiPage().tapOpenButtonNextToUser(contact);
    }
}
