package com.wearezeta.auto.osx.steps.osx;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.osx.common.WrapperTestContext;
import com.wearezeta.auto.osx.pages.osx.ContactContextMenuPage;
import com.wearezeta.auto.web.pages.ContactListPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class ContactContextMenuPageSteps {

    private final WrapperTestContext context;

    public ContactContextMenuPageSteps() {
        this.context = new WrapperTestContext();
    }

    public ContactContextMenuPageSteps(WrapperTestContext context) {
        this.context = context;
    }

    @Given("^I open context menu of conversation (.*)$")
    public void IOpenContextMenuOfContact(String name) throws Exception {
        name = context.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        ContactListPage contactListPage = context.getWebappPagesCollection()
                .getPage(ContactListPage.class);
        Assert.assertTrue("No contact list loaded.",
                contactListPage.waitForContactListVisible());
        contactListPage.openContextMenuForContact(name);
    }

    @When("^I click block in context menu$")
    public void IClickBlockButtonInContextMenu() throws Exception {
        context.getOSXPagesCollection().getPage(ContactContextMenuPage.class)
                .clickBlock();
    }

    @When("^I click leave in context menu$")
    public void IClickLeaveButtonInContextMenu() throws Exception {
        context.getOSXPagesCollection().getPage(ContactContextMenuPage.class)
                .clickLeave();
    }

    @When("^I click silence in context menu$")
    public void IClickSilenceButtonInContextMenu() throws Exception {
        context.getOSXPagesCollection().getPage(ContactContextMenuPage.class)
                .clickSilence();
    }

    @When("^I click notify in context menu$")
    public void IClickNotifyButtonInContextMenu() throws Exception {
        context.getOSXPagesCollection().getPage(ContactContextMenuPage.class)
                .clickNotify();
    }

    @When("^I click delete in context menu$")
    public void IClickDeleteButtonInContextMenu() throws Exception {
        context.getOSXPagesCollection().getPage(ContactContextMenuPage.class)
                .clickDelete();
    }

    @When("^I click archive in context menu$")
    public void IClickArchiveButtonInContextMenu() throws Exception {
        context.getOSXPagesCollection().getPage(ContactContextMenuPage.class)
                .clickArchive();
    }
}
