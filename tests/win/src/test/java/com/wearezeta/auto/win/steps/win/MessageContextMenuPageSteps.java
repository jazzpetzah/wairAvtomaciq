package com.wearezeta.auto.win.steps.win;


import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.pages.ConversationPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.win.common.WrapperTestContext;
import com.wearezeta.auto.win.pages.win.ForeignMessageContextMenuPage;
import com.wearezeta.auto.win.pages.win.OwnMessageContextMenuPage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class MessageContextMenuPageSteps {

    private final WinPagesCollection winPagesCollection = WinPagesCollection.getInstance();
    @SuppressWarnings("unused")
    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection.getInstance();

    @SuppressWarnings("unused")
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    
    private final WrapperTestContext context;

    public MessageContextMenuPageSteps() {
        this.context = new WrapperTestContext();
    }

    public MessageContextMenuPageSteps(WrapperTestContext context) {
        this.context = context;
    }

    /**
     * Opens or closes the context menu for the last message in the current conversation
     *
     * @step. ^I click context menu of the last message$
     *
     * @throws java.lang.Exception
     */
    @Given("^I click context menu of the last message$")
    public void IClickContextMenuOfLast() throws Exception {
        webappPagesCollection.getPage(ConversationPage.class).clickContextMenuOnMessage(1);
    }

    /**
     * Click delete local or delete everywhere in message context menu
     *
     * @param everywhere whether to click local delete or delete everywhere
     * @step. ^I click delete (everywhere )?in message context menu for my own message$
     * @throws Exception
     */
    @When("^I click delete (everywhere )?in message context menu for my own message$")
    public void IClickDeleteButtonInContextMenu(String everywhere) throws Exception {
        if (everywhere == null) {
            winPagesCollection.getPage(OwnMessageContextMenuPage.class).clickDelete();
        }else{
            winPagesCollection.getPage(OwnMessageContextMenuPage.class).clickDeleteEverywhere();
        }
    }
    
    /**
     * Click delete local or delete everywhere in message context menu
     *
     * @step. ^I click delete in message context menu for foreign message$
     * @throws Exception
     */
    @When("^I click delete in message context menu for foreign message$")
    public void IClickDeleteButtonInForeignContextMenu() throws Exception {
        winPagesCollection.getPage(ForeignMessageContextMenuPage.class).clickDelete();
    }
    
    /**
     * Click edit in message context menu
     *
     * @step. ^I click edit in message context menu for my own message$
     * @throws Exception
     */
    @When("^I click edit in message context menu for my own message$")
    public void IClickEditButtonInContextMenu() throws Exception {
        winPagesCollection.getPage(OwnMessageContextMenuPage.class).clickEdit();
    }
}
