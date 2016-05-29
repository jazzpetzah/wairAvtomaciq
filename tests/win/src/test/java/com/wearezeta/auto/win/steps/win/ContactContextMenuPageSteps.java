package com.wearezeta.auto.win.steps.win;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.win.pages.win.ContactContextMenuPage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.When;

public class ContactContextMenuPageSteps {

    private final WinPagesCollection osxPagesCollection = WinPagesCollection
            .getInstance();
    @SuppressWarnings("unused")
    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
            .getInstance();

    @SuppressWarnings("unused")
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

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
