package com.wearezeta.auto.win.steps.win;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.win.pages.win.ContactContextMenuPage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.win.common.WrapperTestContext;

import cucumber.api.java.en.When;

public class ContactContextMenuPageSteps {

    private final WinPagesCollection winPagesCollection = WinPagesCollection.getInstance();
    @SuppressWarnings("unused")
    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection.getInstance();

    @SuppressWarnings("unused")
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    
    private final WrapperTestContext context;

    public ContactContextMenuPageSteps() {
        this.context = new WrapperTestContext();
    }

    public ContactContextMenuPageSteps(WrapperTestContext context) {
        this.context = context;
    }

    /**
     * Click the block in context menu
     *
     * @step. ^I click block in context menu$
     * @throws Exception
     */
    @When("^I click block in context menu$")
    public void IClickBlockButtonInContextMenu() throws Exception {
        winPagesCollection.getPage(ContactContextMenuPage.class).clickBlock();
    }

    /**
     * Click the leave in context menu
     *
     * @step. ^I click leave in context menu$
     * @throws Exception
     */
    @When("^I click leave in context menu$")
    public void IClickLeaveButtonInContextMenu() throws Exception {
        winPagesCollection.getPage(ContactContextMenuPage.class).clickLeave();
    }

    /**
     * Click the silence in context menu
     *
     * @step. ^I click silence in context menu$
     * @throws Exception
     */
    @When("^I click silence in context menu$")
    public void IClickSilenceButtonInContextMenu() throws Exception {
        winPagesCollection.getPage(ContactContextMenuPage.class).clickSilence();
    }

    /**
     * Click the notify in context menu
     *
     * @step. ^I click notify in context menu$
     * @throws Exception
     */
    @When("^I click notify in context menu$")
    public void IClickNotifyButtonInContextMenu() throws Exception {
        winPagesCollection.getPage(ContactContextMenuPage.class).clickNotify();
    }

    /**
     * Click the delete in context menu
     *
     * @step. ^I click delete in context menu$
     * @throws Exception
     */
    @When("^I click delete in context menu$")
    public void IClickDeleteButtonInContextMenu() throws Exception {
        winPagesCollection.getPage(ContactContextMenuPage.class).clickDelete();
    }

    /**
     * Click the archive in context menu
     *
     * @step. ^I click archive in context menu$
     * @throws Exception
     */
    @When("^I click archive in context menu$")
    public void IClickArchiveButtonInContextMenu() throws Exception {
        winPagesCollection.getPage(ContactContextMenuPage.class).clickArchive();
    }
}
