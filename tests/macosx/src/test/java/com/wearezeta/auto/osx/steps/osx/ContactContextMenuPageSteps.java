package com.wearezeta.auto.osx.steps.osx;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.osx.pages.osx.ContactContextMenuPage;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.ContactListPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class ContactContextMenuPageSteps {

    private final TestContext webContext;
    private final TestContext wrapperContext;

    public ContactContextMenuPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

    @Given("^I open context menu of conversation (.*)$")
    public void IOpenContextMenuOfContact(String name) throws Exception {
        name = webContext.getUserManager().replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
        ContactListPage contactListPage = webContext.getPagesCollection(WebappPagesCollection.class)
                .getPage(ContactListPage.class);
        Assert.assertTrue("No contact list loaded.",
                contactListPage.waitForContactListVisible());
        contactListPage.openContextMenuForContact(name);
    }

    @When("^I click block in context menu$")
    public void IClickBlockButtonInContextMenu() throws Exception {
        wrapperContext.getPagesCollection(OSXPagesCollection.class).getPage(ContactContextMenuPage.class)
                .clickBlock();
    }

    @When("^I click leave in context menu$")
    public void IClickLeaveButtonInContextMenu() throws Exception {
        wrapperContext.getPagesCollection(OSXPagesCollection.class).getPage(ContactContextMenuPage.class)
                .clickLeave();
    }

    @When("^I click silence in context menu$")
    public void IClickSilenceButtonInContextMenu() throws Exception {
        wrapperContext.getPagesCollection(OSXPagesCollection.class).getPage(ContactContextMenuPage.class)
                .clickSilence();
    }

    @When("^I click notify in context menu$")
    public void IClickNotifyButtonInContextMenu() throws Exception {
        wrapperContext.getPagesCollection(OSXPagesCollection.class).getPage(ContactContextMenuPage.class)
                .clickNotify();
    }

    @When("^I click delete in context menu$")
    public void IClickDeleteButtonInContextMenu() throws Exception {
        wrapperContext.getPagesCollection(OSXPagesCollection.class).getPage(ContactContextMenuPage.class)
                .clickDelete();
    }

    @When("^I click archive in context menu$")
    public void IClickArchiveButtonInContextMenu() throws Exception {
        wrapperContext.getPagesCollection(OSXPagesCollection.class).getPage(ContactContextMenuPage.class)
                .clickArchive();
    }
}
