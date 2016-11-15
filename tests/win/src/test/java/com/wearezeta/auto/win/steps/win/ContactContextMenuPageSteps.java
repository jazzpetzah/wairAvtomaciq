package com.wearezeta.auto.win.steps.win;

import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.win.pages.win.ContactContextMenuPage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;

import cucumber.api.java.en.When;

public class ContactContextMenuPageSteps {

    private final TestContext webContext;
    private final TestContext wrapperContext;

    public ContactContextMenuPageSteps(TestContext webContext, TestContext wrapperContext) {
        this.webContext = webContext;
        this.wrapperContext = wrapperContext;
    }

    @When("^I click block in context menu$")
    public void IClickBlockButtonInContextMenu() throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(ContactContextMenuPage.class).clickBlock();
    }

    @When("^I click leave in context menu$")
    public void IClickLeaveButtonInContextMenu() throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(ContactContextMenuPage.class).clickLeave();
    }

    @When("^I click silence in context menu$")
    public void IClickSilenceButtonInContextMenu() throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(ContactContextMenuPage.class).clickSilence();
    }

    @When("^I click notify in context menu$")
    public void IClickNotifyButtonInContextMenu() throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(ContactContextMenuPage.class).clickNotify();
    }

    @When("^I click delete in context menu$")
    public void IClickDeleteButtonInContextMenu() throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(ContactContextMenuPage.class).clickDelete();
    }

    @When("^I click archive in context menu$")
    public void IClickArchiveButtonInContextMenu() throws Exception {
        wrapperContext.getPagesCollection(WinPagesCollection.class).getPage(ContactContextMenuPage.class).clickArchive();
    }
}
